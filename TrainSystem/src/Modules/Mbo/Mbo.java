package Modules.Mbo;

import java.util.TreeMap;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;
import java.util.zip.CRC32;

import Shared.Module;
import Shared.SimTime;

import Modules.TrainController.TrainController;
import Modules.TrainModel.TrainModel;

public class Mbo implements Module {

	public TrainController trainController;
	public TrainModel trainModel;
	private SimTime time;
	private TreeMap<String, TrainInfo> trains;
	private CRC32 crc;

	public Mbo(){
		this.trains = new TreeMap<String,TrainInfo>();
		this.crc = new CRC32();
//		build_initTrains();
	}

	public void testInitTrains() {
		trains.put("RED 1", new TrainInfo("RED 1"));
		trains.put("RED 2", new TrainInfo("RED 2"));
		trains.put("GREEN 1", new TrainInfo("GREEN 1"));
		trains.put("GREEN 2", new TrainInfo("GREEN 2"));
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
		// TODO do stuff with the new time
		return true;
	}

	// returns true if checksum is valid, otherwise false
	public boolean receiveTrainPosition(String signal) {

		// check that checksum is valid
		crc.reset();
		String[] segments = signal.split(":");
		long checksum = Long.parseLong(segments[1]);
		crc.update(segments[0].getBytes());
		if (checksum != crc.getValue()) return false;

		// add train if necessary
		String[] vals = segments[0].split(";");
		String train = vals[0];
		if (trains.get(train) == null) {
			trains.put(train, new TrainInfo(train));
		}

		// update train's position
		double x = Double.parseDouble(vals[1]);
		double y = Double.parseDouble(vals[2]);
		trains.get(train).updatePosition(x,y);

		return true;
	}

	private void transmitSafeBrakingDistance(String trainID, double distance) {

	}

	private void transmitMboAuthority(String trainID, double authority) {

	}

	private void updateTrainInfo() {

	}

	private double calculateAuthority(String trainID) {
		return 0;
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
}
