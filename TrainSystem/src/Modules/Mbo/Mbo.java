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

	final private static double TRAIN_MAX_ACCELERATION_SERVICE_BRAKE = -1.2;
	final private static double FRICTION_COEFFICIENT = 0.16;
	final private static double G = 9.8;
	final private static double TRAIN_MASS = 37103.86; 	// kg
	final private static double TRAIN_LENGTH = 32.2; 	// m

	public Mbo(){
		this.time = new SimTime(7,0,0);
		thisMbo = this;
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
		movingBlockModeEnabled = true;
		initTrack();
		startGui();
		gui.setVisible(false);
	}

	private void initTrack() {
		redLine = new TrackCsvParser().parse("Modules/Mbo/MboRedLine.csv");
		greenLine = new TrackCsvParser().parse("Modules/Mbo/MboGreenLine.csv");
	}

	private void startGui() {
		gui = new MboGui(thisMbo);
	}

	/**
	 * Called by the SimulatorGUI class to show the GUI when this module is selected
	 */
	public void showGUI(){
		gui.setVisible(true);
	}
	
	/*
		public void testInitTrains() {
			trains.put("RED 1", new TrainInfo("RED 1", time, this));
			trains.put("RED 2", new TrainInfo("RED 2", time, this));
		}
	*/

	public Object[][] getTrainData() {
		return getTrainData("");
	}

	public double[] getTrainPosition(String regex) {
		return trains.get(regex).getPosition();
	}

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

	@Override
	public boolean updateTime(SimTime time) {
		//System.out.println("entering MBO");
		this.time = time;
		//System.out.println("Time updated");
		this.updateTrainInfo();
		//System.out.println("Info updated");
		for (TrainInfo train : trains.values()) {
			trainController.setMboAuthority(train.getName(), train.getAuthority());
		//	System.out.printf("Did auth for %s\n", train.getName());
			trainController.setSafeBrakingDistance(train.getName(), train.getSafeBrakingDistance());
		//	System.out.printf("Did dist for %s\n", train.getName());
		}
		//System.out.println("gui");
		gui.update(time);
		//System.out.println("done");
		return true;
	}

	public void enableMovingBlockMode(boolean enabled) {
		movingBlockModeEnabled = enabled;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String train, double[] pos, long checksum) {

		// check that checksum is valid
		crc.reset();
		//System.out.printf("Received %f:%f for %s\n", pos[0], pos[1], train);
		//String[] segments = signal.split(":");
		//long checksum = Long.parseLong(segments[1]);
    	String signal = train + ":" + Double.toString(pos[0]) + "," + Double.toString(pos[1]);
		crc.update(signal.getBytes());
		//System.out.printf("Checksum %s: %x %x\n", train, crc.getValue(), checksum);
		if (checksum != crc.getValue()) return false;

		// add train if necessary
		//String[] vals = segments[0].split(";");
		//String train = vals[0];
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train, time, pos, this));
		}

		// update train's position
		//double x = Double.parseDouble(vals[1]);
		//double y = Double.parseDouble(vals[2]);
		String blockId;
		MboBlock block = getBlockFromCoordinates(pos);
		if (block == null) {
			blockId = new String();
		} else {
			blockId = block.getID();
		}
		trains.get(train).updatePosition(pos, blockId, time);
		//System.out.printf("Put %s on %s\n", train, trains.get(train).getBlockName());

		//System.out.println("received train position...");
		return true;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String train, double[] pos, double weight, long checksum) {

		// check that checksum is valid
		crc.reset();
		//System.out.printf("Received %f:%f for %s\n", pos[0], pos[1], train);
		//String[] segments = signal.split(":");
		//long checksum = Long.parseLong(segments[1]);
    	String signal = train + ":" + Double.toString(weight) + ":" + Double.toString(pos[0]) + "," + 
     		Double.toString(pos[1]);
		crc.update(signal.getBytes());
		//System.out.printf("Checksum %s: %x %x\n", train, crc.getValue(), checksum);
		if (checksum != crc.getValue()) return false;		
		// add train if necessary
		//String[] vals = segments[0].split(";");
		//String train = vals[0];
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train, time, pos, this));
		}
		trains.get(train).setWeight(weight);

		// update train's position
		//double x = Double.parseDouble(vals[1]);
		//double y = Double.parseDouble(vals[2]);
		String blockId;
		MboBlock block = getBlockFromCoordinates(pos);
		if (block == null) {
			blockId = new String();
		} else {
			blockId = block.getID();
		}
		trains.get(train).updatePosition(pos, blockId, time);
		//System.out.printf("Put %s on %s\n", train, trains.get(train).getBlockName());

		//System.out.println("received train position...");
		return true;
	}	

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

	public void updateTrainInfo() {
		for (String train : trains.keySet()) {
			//System.out.printf("Updating info for %s\n", train);
			trains.get(train).setAuthority(calculateAuthority(train));
			//System.out.printf("auth set");
			//System.out.printf("auth for %s\n", train);
			trains.get(train).setSafeBrakingDistance(calculateSafeBrakingDistance(train));
			//System.out.printf("Updated %s\n", train);
		}
	}

	private double calculateAuthority(String trainID) {
		double minDistance = Double.MAX_VALUE;
		double[] pos = trains.get(trainID).getPosition();
		for (String other : trains.keySet()) {
			if (trainID.equals(other)) continue;
			double[] otherPos = trains.get(other).getPosition();
			//double dispX = pos[0] - otherPos[0];
			//double dispY = pos[1] - otherPos[1];
			MboBlock otherBlock = getBlockFromCoordinates(otherPos);
		//	System.out.printf("other block %s", otherBlock.getID());
			if (!otherBlock.isYardLine()) {
				double newDist = calculateDistanceBetweenPositions(pos, otherPos, trains.get(trainID).getDirection());
				if (newDist < minDistance) minDistance = newDist;
		//		System.out.printf("%f to %f\n", newDist, minDistance);
			}
			//double newDist = Math.pow((Math.pow(dispX, 2) + Math.pow(dispY, 2)), 0.5);
		}
		//System.out.printf("Authority for %s: %f\n", trainID, minDistance);

		// train model's position signals have error +/-2m
		double error = 4;

		return Math.max(0, minDistance - error - TRAIN_LENGTH);
	}

	public double debug_getAuthority(String trainID) {
		return trains.get(trainID).getAuthority();
	}

	public int calculateDistanceBetweenPositions(double[] pos1, double[] pos2, int direction) {
		
		double distance = 0;

		// get the blocks
		MboBlock block1 = getBlockFromCoordinates(pos1);
		MboBlock block2 = getBlockFromCoordinates(pos2);

		// fail if blocks on different lines
		//System.out.printf("%f %f\n", pos2[0], pos2[1]);
		//System.out.printf("%s %s\n", block1, block2);
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
				//System.out.printf("%d %d\n", index1, index2);
				direction = nextBlock[1];
				//index1 = (index1 + 1) % line.size();
				//System.out.printf("Index: %d %d\n", index1, index2);
				//	System.out.printf("from %d to %d\n", index1, index2);
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

		//TODO figure this shit out a better way
		// problem is as the train comes off the track
		//if (distance > lineLength / 2) distance = lineLength - distance;

		return (int) Math.round(distance);
	}

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
		double potentialSpeed = train.getSpeed();
		int distance = 0;
		while (potentialSpeed > 0) {
			MboBlock potentialBlock = getBlockAfterMoving(line, blockIndex, blockDisplacement, distance, train.getDirection());
			potentialSpeed = calculateSpeedAfterMeter(potentialSpeed, potentialBlock, train.getMass());
			distance += 1;
		}

    	return distance;
    }
/*
    private MboBlock getBlockAfterMoving(ArrayList<MboBlock> line, int index, int displacement, int distance) {
    	distance -= line.get(index).getLength() - displacement;
    	//System.out.println(distance);
    	while (distance > 0) {
    		index++;
    		if (index >= line.size()) index = 0;
    		distance -= line.get(index).getLength();
    		//System.out.printf("index %d\n", index);
    	}
    	return line.get(index);
    }
*/
    private MboBlock getBlockAfterMoving(ArrayList<MboBlock> line, int index, int displacement, int distance, int direction) {
    	distance -= (direction == 1) ? line.get(index).getLength() - displacement : displacement;
    	while (distance > 0) {
    		int[] nextBlockInfo = line.get(index).getNextBlockInfo(direction);
    		index = nextBlockInfo[0];
    		direction = nextBlockInfo[1];
    		distance -= line.get(index).getLength();
    	}
    	return line.get(index);
    }

    private double calculateSpeedAfterMeter(double speed, MboBlock block, double mass) {
    	
    	// TODO real mass!

    	// Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	double angle = Math.atan2(block.getGrade(),100);
//    	double angle = Math.toDegrees(slope);
    	
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
		// TODO Auto-generated method stub
		return true;
	}

	public static void main(String[] args) {
		new Mbo();
	}
}