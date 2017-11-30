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
		thisMbo = this;
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
		initTrack();
		startGui();
		gui.setVisible(true);
		testInitTrains();
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {

			}
			// red A1
			double[] red1 = {227.416376,119.890932};
			double[] red2 = {251.654612,106.711009};
 			trains.get("RED 1").updatePosition(red1, getBlockFromCoordinates(red1));
 			System.out.printf("RED 1 at %s\n", getTrainData("RED 1")[0][3]);
			// red A3
			trains.get("RED 2").updatePosition(red2, getBlockFromCoordinates(red2));
			//this.updateTrainInfo();
			gui.update();
		}
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
		trains.put("RED 1", new TrainInfo("RED 1"));
		trains.put("RED 2", new TrainInfo("RED 2"));
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
		this.time = time;
		this.updateTrainInfo();
		return true;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String train, double[] pos, long checksum) {

		// check that checksum is valid
		crc.reset();
		System.out.printf("Received %f:%f for %s\n", pos[0], pos[1], train);
		//String[] segments = signal.split(":");
		//long checksum = Long.parseLong(segments[1]);
		crc.update(String.format("%s;%.0f;%.0f",train,pos[0],pos[1]).getBytes());
		//System.out.printf("Checksum %s: %x %x\n", train, crc.getValue(), checksum);
		//if (checksum != crc.getValue()) return false;

		// add train if necessary
		//String[] vals = segments[0].split(";");
		//String train = vals[0];
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train));
		}

		// update train's position
		//double x = Double.parseDouble(vals[1]);
		//double y = Double.parseDouble(vals[2]);
		trains.get(train).updatePosition(pos, getBlockFromCoordinates(pos));

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
			trains.get(train).setAuthority(calculateAuthority(train));
			trains.get(train).setSafeBrakingDistance(calculateSafeBrakingDistance(train));
		//	trainController.setMboAuthority(train, trains.get(train).getAuthority());
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
		System.out.printf("Authority for %s: %f\n", trainID, minDistance);
		return minDistance;
	}

	public double debug_getAuthority(String trainID) {
		return trains.get(trainID).getAuthority();
	}

	private double calculateSafeBrakingDistance(String trainID) {

		// get the current block
		TrainInfo train = trains.get(trainID);
		MboBlock block = train.getBlock();
		//MboBlock block = getBlockFromCoordinates(train.getPosition());
		System.out.printf("Block is %s\n", block);
		ArrayList<MboBlock> line;
		if (block.getLine().equals("red")) {
			line = redLine;
		} else {
			line = greenLine;
		}
		int blockIndex = line.indexOf(block);
		System.out.printf("%s on %s at %s.\n", trainID, block.getLine(), block.getID());

		// get displacement into block
		// the ith coordinate is i meters in
		double xval = train.getPosition()[0];
		int blockDisplacement = Arrays.asList(block.getXCoordinates()).indexOf(xval);
		System.out.printf("%s is %d meters in at %f.\n", trainID, blockDisplacement, xval);
		
		double potentialSpeed = train.getSpeed();
		int distance = 0;
		while (potentialSpeed > 0) {
			MboBlock potentialBlock = getBlockAfterMoving(line, blockIndex, blockDisplacement, distance);
			potentialSpeed = calculateSpeedAfterMeter(potentialSpeed, potentialBlock);
			distance += 1;
		}
    	return distance;
    }

    private MboBlock getBlockAfterMoving(ArrayList<MboBlock> line, int index, int displacement, int distance) {
    	distance -= line.get(index).getLength() - displacement;
    	while (distance > 0) {
    		index++;
    		distance -= line.get(index % line.size()).getLength();
    	}
    	return line.get(index);
    }

    private double calculateSpeedAfterMeter(double speed, MboBlock block) {
    	
    	// TODO real mass!
    	double trainMass = 75000;

    	// Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	double slope = Math.atan2(block.getGrade(),100);
    	double angle = Math.toDegrees(slope);
    	
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
