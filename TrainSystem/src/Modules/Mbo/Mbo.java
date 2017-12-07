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
	private CRC32 crc;

	final private static double TRAIN_MAX_ACCELERATION_SERVICE_BRAKE = -1.2;
	final private static double FRICTION_COEFFICIENT = 0.16;
	final private static double G = 9.8;

	public Mbo(){
		this.time = new SimTime(7,0,0);
		thisMbo = this;
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
		initTrack();
		startGui();
		gui.setVisible(true);
	}

	private void initTrack() {
		redLine = new TrackCsvParser().parse("Modules\\Mbo\\RedLineFinal.csv");
		greenLine = new TrackCsvParser().parse("Modules\\Mbo\\GreenLineFinal.csv");
	}

	private void startGui() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						gui = new MboGui(thisMbo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		gui.setVisible(true);
	}

	public void testInitTrains() {
		trains.put("RED 1", new TrainInfo("RED 1", time));
		trains.put("RED 2", new TrainInfo("RED 2", time));
	}

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
		this.updateTrainInfo();
		//System.out.println("gui");
		gui.update(time);
		//System.out.println("done");
		return true;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String train, double[] pos, long checksum) {

		// check that checksum is valid
		crc.reset();
		//System.out.printf("Received %f:%f for %s\n", pos[0], pos[1], train);
		//String[] segments = signal.split(":");
		//long checksum = Long.parseLong(segments[1]);
		//crc.update(String.format("%s;%.0f;%.0f",train,pos[0],pos[1]).getBytes());
		//System.out.printf("Checksum %s: %x %x\n", train, crc.getValue(), checksum);
		//if (checksum != crc.getValue()) return false;

		// add train if necessary
		//String[] vals = segments[0].split(";");
		//String train = vals[0];
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train, time));
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

	private MboBlock getBlockFromCoordinates(double[] pos) {
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

	private void transmitSafeBrakingDistance(String trainID, double distance) {

	}

	private void transmitMboAuthority(String trainID, double authority) {

	}

	public void updateTrainInfo() {
		for (String train : trains.keySet()) {
			//System.out.printf("Updating info for %s\n", train);
			trains.get(train).setAuthority(calculateAuthority(train));
			//System.out.printf("auth for %s\n", train);
			trains.get(train).setSafeBrakingDistance(calculateSafeBrakingDistance(train));
			//System.out.printf("Updated %s\n", train);
			//trainController.setMboAuthority(train, trains.get(train).getAuthority());
			//trainController.setSafeBrakingDistance(train, trains.get(train).getSafeBrakingDistance());
		}
	}

	private double calculateAuthority(String trainID) {
		double minDistance = Double.MAX_VALUE;
		double[] pos = trains.get(trainID).getPosition();
		for (String other : trains.keySet()) {
			if (trainID.equals(other)) continue;
			double[] otherPos = trains.get(other).getPosition();
			double dispX = pos[0] - otherPos[0];
			double dispY = pos[1] - otherPos[1];
			double newDist = Math.pow((Math.pow(dispX, 2) + Math.pow(dispY, 2)), 0.5);
			if (newDist < minDistance) minDistance = newDist;
		}
		//System.out.printf("Authority for %s: %f\n", trainID, minDistance);
		return minDistance;
	}

	public double debug_getAuthority(String trainID) {
		return trains.get(trainID).getAuthority();
	}

	private double calculateSafeBrakingDistance(String trainID) {

		// get the current block
		TrainInfo train = trains.get(trainID);
		//MboBlock block = train.getBlock();
		MboBlock block = getBlockFromCoordinates(train.getPosition());
		if (block == null) return 1000; // HACK
		//System.out.printf("Block is %s\n", block);
		ArrayList<MboBlock> line;
		if (block.getLine().equals("red")) {
			line = redLine;
		} else {
			line = greenLine;
		}
		int blockIndex = line.indexOf(block);
		//System.out.printf("%s on %s at %s.\n", trainID, block.getLine(), block.getID());

		// get displacement into block
		// the ith coordinate is i meters in
		double xval = train.getPosition()[0];
		int blockDisplacement = Arrays.asList(block.getXCoordinates()).indexOf(xval);
		//System.out.printf("%s is %d meters in at %f.\n", trainID, blockDisplacement, xval);
		
		double potentialSpeed = train.getSpeed();
		int distance = 0;
		while (potentialSpeed > 0) {
			MboBlock potentialBlock = getBlockAfterMoving(line, blockIndex, blockDisplacement, distance);
			//System.out.printf("block index %s\n", potentialBlock);
			potentialSpeed = calculateSpeedAfterMeter(potentialSpeed, potentialBlock);
			//System.out.printf("speed %f\n", potentialSpeed);
			distance += 1;
		}
		//System.out.printf("%s can stop in %d meters.\n", train, distance);
    	return distance;
    }

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

    private double calculateSpeedAfterMeter(double speed, MboBlock block) {
    	
    	// TODO real mass!
    	double trainMass = 75000;

    	// Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	double angle = Math.atan2(block.getGrade(),100);
//    	double angle = Math.toDegrees(slope);
    	
    	// Step 3: Calculate the forces acting on the train using the coefficient of friction
    	// and the train's weight in lbs converted to kg divided over the wheels (where the force is technically
    	// being applied times gravity (G)
    	double normalForce = (trainMass/12) * G * Math.sin(angle);	// divide by 12 for the number of wheels
    	double downwardForce = (trainMass/12) * G * Math.cos(angle);	// divide by 12 for the number of wheels

    	// compute friction forc
    	double friction = (FRICTION_COEFFICIENT * downwardForce) + normalForce;

    	// Calculate acceleration using the F = ma equation, where m = the mass of the body moving
    	// add acceleration due to brake
    	double trainAcceleration = friction/trainMass + (TRAIN_MAX_ACCELERATION_SERVICE_BRAKE*1);
    	
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
