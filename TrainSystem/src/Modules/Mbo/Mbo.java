package Modules.Mbo;

import java.util.HashMap;
import java.time.LocalDateTime;

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
		Object output[][] = new Object[5][1];
		for (String trainName : trains.keySet()) {
			int index = 0;
			if (!regex.equals("") && trainName.equals(regex)) {
				output[index] = trains.get(regex).toDataArray();
				index++;	
			}
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
}
