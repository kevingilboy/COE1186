//Michael Kotcher

package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrackModel.Block;
import Modules.TrackModel.Station;

public class TrainController implements Module {
	public TrackModel trackModel;
	public TrainModel trainModel;
	private SimTime time;
	private SimTime prevTime;
	private HashMap<Integer, TrnController> controlList;
	private ArrayList<BlockInfo> redInfo;
	private ArrayList<BlockInfo> greenInfo;

	public TrainController(TrackModel tkmodel, TrainModel tnmodel) {
		trackModel = tkmodel;
		trainModel = tnmodel;
		controlList = new HashMap<Integer, TrnController>();
		receiveMap();
	}
	
	public void dispatchTrain(String trainID, String line) {
		if (line.equals("RED")) {
			controlList.put(trainID.hashCode(), new TrnController(trainID, line, this, redInfo);
		}
		else {
			controlList.put(trainID.hashCode(), new TrnController(trainID, line, this, greenInfo);
		}
	}
	
	public void setMboAuthority(String trainID, double auth) {
		TrnController C = controlList.get(trainID.hashCode());
		C.setMboAuthority(auth);
	}
	
	public void setSafeBrakingDistance(String trainID, double dist) {
		TrnController C = controlList.get(trainID.hashCode());
		C.setSafeBrakingDistance(dist);
	}
	
	public void setBeacon(String trainID, double value) {
		TrnController C = controlList.get(trainID.hashCode());
		C.setBeacon(value);
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
	
	public int receiveTrainPosition(String trainID) {
		return trainModel.getPosition(trainID).getCurrentBlock();
	}
	
	private void receiveMap() {
		ArrayList<Block> redBlocks = trackModel.getTrack("red");
		ArrayList<Block> greenBlocks = trackModel.getTrack("green");
		redInfo = new ArrayList<BlockInfo>(77);
		greenInfo = new ArrayList<BlockInfo>(152);
		Station S;
		for (Block B : redBlocks) {
			S = B.getStation();
			redInfo.set(B.getID() - 1, new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), S.getID(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));
		}
		for (Block B : greenBlocks) {
			S = B.getStation();
			greenInfo.set(B.getID() - 1, new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), S.getID(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));
		}
	}

	@Override
	public boolean updateTime(SimTime time) {
		for (TrnController T : controlList) {
			T.updateTime();
		}
		return true;
	}
}
