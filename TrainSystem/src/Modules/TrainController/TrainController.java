package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;

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
		//GET MAP INFO HERE
	}
	
	public void dispatchTrainToBlock(String trainID, String line, int startBlock {
		controlList.add(new TrnController(trainID, line, startBlock, this);
	}
	
	public void setCtcAuthority(String trainID, double auth) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setCtcAuthority(auth);
			}
		}
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
	
	public void setSetpointSpeed(String trainID, double speed) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setSetpointSpeed(speed);
			}
		}
	}
	
	public void setEmergencyBrake(String trainID, boolean status) {
		for (TrnController C : controlList) {
			if (C.getID == trainID) {
				C.setEmergencyBrake(status);
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
	
	public double receiveTrainActualSpeed(String trainID)
	{
		return trainModel.getActualSpeed(trainID);
	}

	@Override
	public boolean updateTime(SimTime time) {
		for (TrnController T : controlList) {
			T.updateTime(time
		}
		return true;
	}
}
