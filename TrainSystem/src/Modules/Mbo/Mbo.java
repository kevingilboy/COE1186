package Modules.Mbo;

import java.util.TreeMap;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;
import java.util.zip.CRC32;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException; 

import Shared.Module;
import Shared.SimTime;
import Modules.Ctc.Train;
import Modules.TrainController.TrainController;
import Modules.TrainModel.TrainModel;

public class Mbo implements Module {

	public TrainController trainController;
	public TrainModel trainModel;
	private Mbo thisMbo;
	private MboGui gui;
	private SimTime time;
	private TreeMap<String, TrainInfo> trains;
	private ArrayList<MboBlock> redLine;
	private ArrayList<MboBlock> greenLine;
	private boolean movingBlockModeEnabled;
	private CRC32 crc;

	final private static double TRAIN_MAX_ACCELERATION_SERVICE_BRAKE = -1.2; // m/s^2
	final private static double FRICTION_COEFFICIENT = 0.16; // dimensionless	
	final private static double G = 9.8;				// m/s^2
	final private static double TRAIN_MASS = 37103.86; 	// kg
	final private static double TRAIN_LENGTH = 32.2; 	// m

	public Mbo(){
		this.time = new SimTime(7,0,0);
		thisMbo = this;
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
		movingBlockModeEnabled = false;
		initTrack();
		startGui();
		gui.setVisible(false);
	}

	// loads the red and green line from MBO specific CSV files
	private void initTrack() {
		redLine = new TrackCsvParser().parse("Modules/Mbo/MboRedLine.csv");
		greenLine = new TrackCsvParser().parse("Modules/Mbo/MboGreenLine.csv");
	}

	// initializes the MBOGui object
	private void startGui() {
		gui = new MboGui(thisMbo);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				//... silence awt exceptions
			}
	    });
	}

	/**
	 * Called by the SimulatorGUI class to show the GUI when this module is selected
	 */
	public void showGUI(){
		gui.setVisible(true);
	}

	// returns relevant information about trains for display by the MBOGui
	// trains that contain a string matching the given regex are returned
	public Object[][] getTrainData(String regex) {
		Pattern pattern = Pattern.compile(".*" + regex + ".*");
		ArrayList<Object[]> trainObjs = new ArrayList<Object[]>();
		for (String trainName : trains.keySet()) {
			if (pattern.matcher(trainName).matches()) {
				trainObjs.add(trains.get(trainName).toDataArray());
			}
		}
		Object[][] output = new Object[trainObjs.size()][3];
		int index = 0;
		for (Object[] train : trainObjs) {
			output[index] = train;
			index++;
		}
		return output;
	}

	// when search box is empty, return data for all trains currently in yard
	public Object[][] getTrainData() {
		return getTrainData("");
	}

	// returns the pixel coordinates of the train
	public double[] getTrainPosition(String regex) {
		return trains.get(regex).getPosition();
	}


	// on every clock tic, update the info for the trains, and then transmit authority, SBD, and recommended speed
	@Override
	public boolean updateTime(SimTime time) {
		this.time = time;
		this.updateTrainInfo();
		for (TrainInfo train : trains.values()) {
			trainController.setMboAuthority(train.getName(), train.getAuthority());
			trainController.setSafeBrakingDistance(train.getName(), train.getSafeBrakingDistance());
			double recommendedSpeed;
			if (train.getAuthority() <= train.getSafeBrakingDistance()) {
				recommendedSpeed = 0;
			} else {
				recommendedSpeed = train.getSpeed();
			}
		}
		gui.update(time);
		return true;
	}

	// capacity to toggle moving block mode currently resides with CTC GUI
	public void enableMovingBlockMode(boolean enabled) {
		movingBlockModeEnabled = enabled;
	}

	public boolean isMovingBlockModeEnabled() {
		return movingBlockModeEnabled;
	}

	// returns true if checksum is valid, otherwise false
	// DEPRECATED in favor of receiveTrainPosition that includes the train's weight
	public boolean receiveTrainPosition(String train, double[] pos, long checksum) {

		// check that checksum is valid
		crc.reset();
    	String signal = train + ":" + Double.toString(pos[0]) + "," + Double.toString(pos[1]);
		crc.update(signal.getBytes());
		if (checksum != crc.getValue()) return false;

		// add train if necessary
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train, time, pos, this));
		}

		// update train's position
		String blockId;
		MboBlock block = getBlockFromCoordinates(pos);
		if (block == null) {
			blockId = new String();
		} else {
			blockId = block.getID();
		}
		trains.get(train).updatePosition(pos, blockId, time);

		return true;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String train, double[] pos, double weight, long checksum) {

		// check that checksum is valid
		crc.reset();
    	String signal = train + ":" + Double.toString(weight) + ":" + Double.toString(pos[0]) + "," + 
     		Double.toString(pos[1]);
		crc.update(signal.getBytes());
		if (checksum != crc.getValue()) return false;		
		
		// add train if necessary
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train, time, pos, this));
		}
		trains.get(train).setWeight(weight);

		// update train's position
		String blockId;
		MboBlock block = getBlockFromCoordinates(pos);
		if (block == null) {
			blockId = new String();
		} else {
			blockId = block.getID();
		}
		trains.get(train).updatePosition(pos, blockId, time);

		return true;
	}	

	// returns the MboBlock containing the given coordinates and null otherwise	
	public MboBlock getBlockFromCoordinates(double[] pos) {
		for (MboBlock block : redLine) {
			if (block.onBlock(pos[0], pos[1])) {
				return block;
			}
		}
		for (MboBlock block : greenLine) {
			if (block.onBlock(pos[0], pos[1])) {
				return block;
			}
		}
		return null;
	}

	// removes trains that have returned to yard; sets authority and SBD
	public void updateTrainInfo() {
		ArrayList<String> trainsToRemove = new ArrayList<String>();
		
		for (String train : trains.keySet()) {

			// remove the train from the hashmap if it's returned to the yard
			if (trains.get(train).isPoofable()) {
				trainsToRemove.add(train);
				continue;
			}

			// calculate and set the train's commanded values in a safety-critical manner
			trains.get(train).setAuthority(calculateAuthoritySafetyCritical(train));
			trains.get(train).setSafeBrakingDistance(calculateSafeBrakingDistanceSafetyCritical(train));
		}
		
		// avoids ConcurrentExecutionException 
		while(trainsToRemove.size()>0) {
			trains.remove(trainsToRemove.remove(0));
		}
	}

	// wraps a triply redundant voting system around the authority calculation
	private double calculateAuthoritySafetyCritical(String trainID) {
		double[] authorities = new double[3];
		do {
			for (int i = 0; i < 3; i++) authorities[i] = calculateAuthority(trainID);
		} while (authorities[0] != authorities[1] || authorities[1] != authorities[2]);
		return authorities[0];
	}

	// calculates authority by taking the minimum of the forward distances to other trains
	// ie TrainA and TrainB are 1 meter apart traveling clockwise on a circular track of circumference 10
	// TrainA has authority 9; TrainB has authority 1
	private double calculateAuthority(String trainID) {
		double minDistance = Double.MAX_VALUE;
		double[] pos = trains.get(trainID).getPosition();
		for (String other : trains.keySet()) {
			if (trainID.equals(other)) continue;
			double[] otherPos = trains.get(other).getPosition();
			MboBlock otherBlock = getBlockFromCoordinates(otherPos);
			if (!otherBlock.isYardLine()) {
				double newDist = calculateDistanceBetweenPositions(pos, otherPos, trains.get(trainID).getDirection());
				if (newDist < minDistance) minDistance = newDist;
			}
		}

		// train model's position signals have error +/-2m
		double error = 4;

		// account for both the error and the length of the train
		return Math.max(0, minDistance - error - TRAIN_LENGTH);
	}

	// used in debugging, should be removed in a future version
	public double debug_getAuthority(String trainID) {
		return trains.get(trainID).getAuthority();
	}

	// returns the distance that a train traveling in DIRECTION would have to cover to travel from pos1 to pos2
	public int calculateDistanceBetweenPositions(double[] pos1, double[] pos2, int direction) {
		
		double distance = 0;

		// get the blocks
		MboBlock block1 = getBlockFromCoordinates(pos1);
		MboBlock block2 = getBlockFromCoordinates(pos2);

		// fail if blocks on different lines
		if (!block1.getLine().equals(block2.getLine())) {
			return -1;
		}

		// get reference to line
		ArrayList<MboBlock> line = block1.getLine().equals("RED") ? redLine : greenLine;
		int lineLength = block1.getLine().equals("RED") ? 5598 : 14447;

		// if the two are on different blocks, add the distance to the end of block1
		int offset = block1.getOffset(pos1);
		if (block1 != block2) {
			distance = (direction == 1) ? block1.getLength() - offset : offset;
			offset = 0;
		}

		// add the lengths of all blocks between these
		int index1 = line.indexOf(block1);
		int index2 = line.indexOf(block2);
		while (index1 != index2) {
			if (index1 >= 1){
				distance += line.get(index1).getLength();
				int[] nextBlock = line.get(index1).getNextBlockInfo(direction);
				index1 = nextBlock[0] - 1;
				direction = nextBlock[1];
			} else {
				index1 = index2;
			}
		}

		// if on seperate blocks, added entire length of block2. remove.
		if (block1 != block2) distance -= block2.getLength();

		// add the displacement within the block
		// if positions on different blocks, this is just offset of second position within block2
		distance += block2.getOffset(pos2) - offset;

		// if both were initially on the same block but pos2 < pos1, result is negative; convert to positive
		if (distance < 0) distance += lineLength; 

		return (int) Math.round(distance);
	}

	// wraps a triply redudant voting system around SBD calculation
	private double calculateSafeBrakingDistanceSafetyCritical(String trainID) {
		double[] distances = new double[3];
		do {
			for (int i = 0; i < 3; i++) distances[i] = calculateSafeBrakingDistance(trainID);
		} while (distances[0] != distances[1] || distances[1] != distances[2]);
		return distances[0];
	}

	// returns the distance it would take the given train to stop if it cut all power and threw the service brake
	private double calculateSafeBrakingDistance(String trainID) {

		// get the current block
		TrainInfo train = trains.get(trainID);
		MboBlock block = getBlockFromCoordinates(train.getPosition());
		ArrayList<MboBlock> line;
		if (block.getLine().equals("red")) {
			line = redLine;
		} else {
			line = greenLine;
		}

		// get displacement into block. this is positive from the start of the block's coordinate list
		int blockIndex = line.indexOf(block);
		int blockDisplacement = block.getOffset(train.getPosition());
		
		// calculate the safe braking distance by determining how far a meter on each block slows the train down
		// and continuing until the train has slowed to a stop
		double potentialSpeed = train.getSpeed();
		int distance = 0;
		while (potentialSpeed > 0) {
			int[] blockInfo = getBlockAfterMoving(line, blockIndex, blockDisplacement, distance, train.getDirection());
			potentialSpeed = calculateSpeedAfterMeter(potentialSpeed, line.get(blockInfo[0]), blockInfo[1], train.getMass());
			distance += 1;
		}

    	return distance;
    }

    // determine the block index and direction of travel for a train after traveling a given distance from a certain 
    // point on either line
    private int[] getBlockAfterMoving(ArrayList<MboBlock> line, int index, int displacement, int distance, int direction) {
    	distance -= (direction == 1) ? line.get(index).getLength() - displacement : displacement;
    	while (distance > 0) {
    		int[] nextBlockInfo = line.get(index).getNextBlockInfo(direction);
    		index = nextBlockInfo[0];
    		direction = nextBlockInfo[1];
    		distance -= line.get(index).getLength();
    	}
    	return new int[]{index, direction};
    }

    // determine the final velocity of a train after traveling a meter on a given block in a given direction
    private double calculateSpeedAfterMeter(double speed, MboBlock block, int direction, double mass) {
    	
    	// if train is traveling the block in the backward direction, flip the grade
    	double grade = (direction == 1) ? block.getGrade() : (100 - block.getGrade());

    	// Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	double angle = Math.atan2(block.getGrade(),100);
    	
    	// Step 3: Calculate the forces acting on the train using the coefficient of friction
    	// and the train's weight in lbs converted to kg divided over the wheels (where the force is technically
    	// being applied times gravity (G)
    	double normalForce = (mass/12) * G * Math.sin(angle);	// divide by 12 for the number of wheels
    	double downwardForce = (mass/12) * G * Math.cos(angle);	// divide by 12 for the number of wheels

    	// compute friction forc
    	double friction = (FRICTION_COEFFICIENT * downwardForce) + normalForce;

    	// Calculate acceleration using the F = ma equation, where m = the mass of the body moving
    	// add acceleration due to brake
    	double trainAcceleration = friction/mass + (TRAIN_MAX_ACCELERATION_SERVICE_BRAKE*1);
    	
    	// calculate the speed after traveling 1m with that acceleration
    	double finalSpeed = Math.pow(Math.pow(speed, 2) + 2*trainAcceleration, 0.5);

    	return finalSpeed;
	}

	@Override
	public boolean communicationEstablished() {
		return true;
	}

	public static void main(String[] args) {
		new Mbo();
	}
}