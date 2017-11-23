package Modules.Mbo;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import Shared.Module;
import Shared.SimTime;

import Modules.TrainController.TrainController;
import Modules.TrainModel.TrainModel;

public class Mbo implements Module {

	public TrainController trainController;
	public TrainModel trainModel;
	private SimTime time;
	private HashMap<String, TrainInfo> trains;

	public Mbo(){
		this.trains = new HashMap<String,TrainInfo>();
		build_initTrains();
	}

	private void build_initTrains() {
		trains.put("RED 1", new TrainInfo("RED 1"));
		trains.put("RED 2", new TrainInfo("RED 2"));
		trains.put("RED 3", new TrainInfo("RED 3"));
		trains.put("GREEN 1", new TrainInfo("GREEN 1"));
		trains.put("GREEN 2", new TrainInfo("GREEN 2"));
	}

	public Object[][] getTrainData(String regex) {
		Pattern pattern = Pattern.compile(".*" + regex + ".*");
		ArrayList<Object[]> trainObjs = new ArrayList<Object[]>();
		for (String trainName : trains.keySet()) {
			if (!regex.equals("") && pattern.matcher(trainName).matches()) {
				trainObjs.add(trains.get(trainName).toDataArray());
			}
		}
		Object[][] output = new Object[trainObjs.size()][1];
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

	public boolean receiveTrainPosition(String signal) {
		return false;
	}

	private void transmitSafeBrakingDistance(String trainID, double distance) {

	}

	private void transmitMboAuthority(String trainID, double authority) {

	}

	private void updateTrainInfo() {

	}

	private double[] calculateAuthority(String trainID) {
		return null;
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
