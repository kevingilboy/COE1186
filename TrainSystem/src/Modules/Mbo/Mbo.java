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
		trains.put("BLUE 1", new TrainInfo("BLUE 1"));
		trains.put("BLUE 2", new TrainInfo("BLUE 2"));
		trains.put("BLUE 3", new TrainInfo("BLUE 3"));
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
		String blockId = getBlockFromCoordinates(pos);
		trains.get(train).updatePosition(pos, blockId);

		return true;
	}

	private String getBlockFromCoordinates(double[] pos) {
		for (MboBlock block : redLine) {
			if (block.onBlock(pos[0], pos[1])) {
				return block.getID();
			}
		}
		for (MboBlock block : greenLine) {
			if (block.onBlock(pos[0], pos[1])) {
				return block.getID();
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
		//System.out.printf("Authority for %s: %f\n", trainID, minDistance);
		return minDistance;
	}

	public double debug_getAuthority(String trainID) {
		return trains.get(trainID).getAuthority();
	}

	private double calculateSafeBrakingDistance(String trainID) {
		return 0;	
	}

	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void main(String[] args) {
		new Mbo();
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
 			trains.get("RED 1").updatePosition(red1, getBlockFromCoordinates(red1));
			// red A3
			trains.get("RED 2").updatePosition(red2, getBlockFromCoordinates(red2));
			gui.update();
		}*/
	}
}
