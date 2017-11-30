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

	public Mbo(){
		thisMbo = this;
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
		initTrack();
		startGui();
		gui.setVisible(true);
		/*testInitTrains();
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {

			}
			this.updateTrainInfo();
			// red A1
			double[] red1 = {227.416376,119.890932};
			double[] red2 = {251.654612,106.711009};
 			trains.get("RED 1").updatePosition(red1, getBlockFromCoordinates(red1).getID());
			// red A3
			trains.get("RED 2").updatePosition(red2, getBlockFromCoordinates(red2).getID());
			gui.update();
		}*/
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
		trains.put("RED 3", new TrainInfo("RED 3"));
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
		String blockId = getBlockFromCoordinates(pos).getID();
		trains.get(train).updatePosition(pos, blockId);

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
		MboBlock block = getBlockFromCoordinates(train.getPosition());
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
		

		// Step 1: input power and convert the power to a force based on the starting velocity
	    //double trainMass = trainWeight*KG_PER_POUND;
    	double trainMass = 75000; // TODO put a real mass in!!
/*
    	// this is ensuring that we never get a negative speed
    	if (this.currentSpeed == 0) {
    		this.force = (this.powerIn * 1000)/1;
    	} else {
    		this.force = (this.powerIn * 1000)/this.currentSpeed;
    	}
    	setGrade();
    	// Step 2: Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	this.slope = Math.atan2(this.grade,100);
    	double angle = Math.toDegrees(this.slope);
    	
    	// Step 3: Calculate the forces acting on the train using the coefficient of friction
    	// and the train's weight in lbs converted to kg divided over the wheels (where the force is technically
    	// being applied times gravity (G)
    	this.normalForce = (trainMass/12) * G * Math.sin(angle);	// divide by 12 for the number of wheels
    	this.downwardForce = (trainMass/12) * G * Math.cos(angle);	// divide by 12 for the number of wheels

    	// compute friction force
    	this.friction = (FRICTION_COEFFICIENT * this.downwardForce) + this.normalForce;
    	
    	// sum of the forces
    	this.totalForce = this.force - this.friction;
    	    	
    	this.force = this.totalForce;
    	
    	// Step 4: Calculate acceleration using the F = ma equation, where m = the mass of the body moving
    	this.trainAcceleration = this.force/trainMass;
    	
    	// and have to check to make sure this acceleration does not exceed our max.
    	if (this.trainAcceleration >= TRAIN_MAX_ACCELERATION * 1) {	// time elapsed (one second)
    		// set the acceleration as the max acceleration because we cannot exceed that
    		this.trainAcceleration = TRAIN_MAX_ACCELERATION * 1;	// time elapsed (one second)
    	}
    	
    	// decelerates the train based on the values given in the spec sheet for the emergency brake
    	if (emerBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_E_BRAKE*1);
    	}
    	
    	// decelerates the train based onthe values given in the spec sheet for the service brake
    	if(serviceBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_SERVICE_BRAKE*1);
    	}
    	
    	// Step 5: Calculate the final speed by adding the old speed with the acceleration x the time elapsed (one second)
    	this.finalSpeed = this.currentSpeed + (this.trainAcceleration * 1);
    	
    	// NO NEGATIVE SPEEDS YINZ
    	if(this.finalSpeed < 0) {
            this.finalSpeed = 0;
        }
    	
    	// resetting the current speed based on our calculations
    	this.currentSpeed =  this.finalSpeed;
    	this.distTravelled = this.currentSpeed * 1; // speed times the time between clock ticks = distance travelled
    	//System.out.println(finalSpeed);
    	
    	if(!(currentBlock == this.position.getCurrentBlock())) {
    		metersIn = 0;
    	} else {
    		metersIn += this.distTravelled;
    	}
    	this.position.moveTrain(this.distTravelled); // method call to tell the position class how far to move the train
    	
    */
		return Double.MAX_VALUE;	
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
