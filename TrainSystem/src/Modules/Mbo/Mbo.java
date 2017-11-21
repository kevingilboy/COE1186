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
	}

	public Object[][] getTrainData(String regex) {
		Object output[][] = new Object[3][1];
		output[0][0] = trains.get("RED 1");
		output[1][0] = trains.get("RED 2");
		output[2][0] = trains.get("RED 3");
		System.out.println(output);
		return output;
	}

	public Object[][] getTrainData(String regex) {
		return null;
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
