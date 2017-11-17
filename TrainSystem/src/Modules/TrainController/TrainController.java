//Michael Kotcher

package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrackModel.Block;

public class TrainController implements Module {
	public TrackModel trackModel;
	public TrainModel trainModel;
	private SimTime time;
	private SimTime prevTime;
	private ArrayList<TrnController> controlList;
	

	public TrainController(TrackModel tkmodel, TrainModel tnmodel) {
		trackModel = tkmodel;
		trainModel = tnmodel;
		controlList = new ArrayList<TrnController>();
		receiveMap();
		//GET MAP INFO HERE
	}
	
	public void dispatchTrain(String trainID, String line) {
		controlList.add(new TrnController(trainID, line, startBlock, this);
	}
	
	public void setMboAuthority(String trainID, double auth) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setMboAuthority(auth);
			}
		}
	}
	
	public void setSafeBrakingDistance(String trainID, double dist) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setSafeBrakingDistance(dist);
			}
		}
	}
	
	public void setBeacon(String trainID, double value) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setBeacon(value);
			}
		}
	}
	
	public void transmitPower(String trainID, double power) {
		trainModel.setPower(trainID, power);
	}
	
	public void transmitLeft(String trainID, boolean status) {
		trainModel.setLeft(trainID, status);
	}
	
	public void transmitRight(String trainID, boolean status) {
		trainModel.setRight(trainID, status);
	}
	
	public void transmitService(String trainID, boolean status) {
		trainModel.setService(trainID, status);
	}
	
	public void transmitEmergency(String trainID, boolean status) {
		trainModel.setEmergency(trainID, status);
	}
	
	public void transmitLights(String trainID, boolean status) {
		trainModel.setLights(trainID, status);
	}
	
	public void transmitTemperature(String trainID, int temperature) {
		trainModel.setTemperature(trainID, temperature);
	}
	
	public void transmitAnnouncement(String trainID, String announcement) {
		trainModel.setAnnouncement(trainID, announcement);
	}
	
	public double receiveTrainActualSpeed(String trainID) {
		return trainModel.getActualSpeed(trainID);
	}
	
	public double receiveSetpointSpeed(String trainID) {
		return trainModel.getSetpointSpeed(trainID);
	}
	
	public double receiveCtcAuthority(String trainID) {
		return trainModel.getCtcAuthority(trainID);
	}
	
	public boolean receivePassengerEmergency(String trainID) {
		return trainModel.getEmergencyBrake(trainID);
	}
	
	public double[] receiveTrainPosition(String trainID) {
		return trainModel.getPosition(trainID);
	}
	
	private void receiveMap()
	{
		//dafuq
		ArrayList<Block> redBlocks = 
	}

	@Override
	public boolean updateTime(SimTime time) {
		for (TrnController T : controlList) {
			T.updateTime();
		}
		return true;
	}
}
