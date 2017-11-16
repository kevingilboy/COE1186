package Modules.Mbo;

import Shared.Module;
import Shared.SimTime;

import Modules.TrainController.TrainController;
import Modules.TrainModel.TrainModel;

public class Mbo implements Module {

	public TrainController trainController;
	public TrainModel trainModel;
	private SimTime time;

	public Mbo(){

	}

	public Object[][] getTrainData(String regex) {
		return null;
	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}

	private void receiveTrainPosition(String trainID) {

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
