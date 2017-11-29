//Michael Kotcher

package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrackModel.Block;
import Modules.TrackModel.Station;
import Modules.TrainModel.TrainModel;
import Modules.TrainModel.Train;
import Modules.TrainModel.Position;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainController implements Module {
	private HashMap<String, TrnController> controlList;
	private TrainControllerGUI mainGUI;
	public TrackModel trackModel;
	public TrainModel trainModel;
	private ArrayList<BlockInfo> redInfo;
	private ArrayList<BlockInfo> greenInfo;
	private SimTime time;
	private String[] stationList;
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;

	public TrainController() {
		controlList = new HashMap<String, TrnController>();
		mainGUI = new TrainControllerGUI();
		stationList = new String[]{"Pioneer", "Edgebrook", "Station", "Whited", "South Bank", "Central", "Inglewood", "Overbrook", "Glenbury", "Dormont", "Mt. Lebanon", "Poplar", "Castle Shannon", "Glenbury", "Overbrook", "Inglewood", "Central", "Shadyside", "Herron Avenue", "Swissville", "Penn Station", "Steel Plaza", "First Avenue", "Station Square", "South Hills Junction"};
	}
	
	public void dispatchTrain(String trainID, String line) {
		if (line.equals("RED")) {
			controlList.put(trainID, new TrnController(trainID, line, this, redInfo, mainGUI, stationList));
		}
		else {
			controlList.put(trainID, new TrnController(trainID, line, this, greenInfo, mainGUI, stationList));
		}
	}
	
	public void setMboAuthority(String trainID, double auth) {
		TrnController C = controlList.get(trainID);
		C.setMboAuthority(auth);
	}
	
	public void setSafeBrakingDistance(String trainID, double dist) {
		TrnController C = controlList.get(trainID);
		C.setSafeBrakingDistance(dist);
	}
	
	public void setBeacon(String trainID, int value) {		//may have to change to a GETTER
		TrnController C = controlList.get(trainID);
		C.setBeacon(value);
	}
	
	public void setPassengerEmergencyBrake(String trainID, boolean status) {
		TrnController C = controlList.get(trainID);
		C.setPassengerEmergencyBrake(status);
	}
	
	public void transmitPower(String trainID, double power) {
		trainModel.setPower(trainID, power);
	}
	
	public void transmitLeft(String trainID, boolean status) {
		trainModel.setLeftDoor(trainID, status);
	}
	
	public void transmitRight(String trainID, boolean status) {
		trainModel.setRightDoor(trainID, status);
	}
	
	public void transmitService(String trainID, boolean status) {
		trainModel.setServiceBrake(trainID, status);
	}
	
	public void transmitEmergency(String trainID, boolean status) {
		trainModel.setDriverEmergencyBrake(trainID, status);
	}
	
	public void transmitLights(String trainID, boolean status) {
		trainModel.setLights(trainID, status);
	}
	
	public void transmitTemperature(String trainID, int temperature) {
		trainModel.setTemp(trainID, temperature);
	}
	
	public void transmitAnnouncement(String trainID, int status, String stationName) {
		trainModel.setArrivalStatus(trainID, status, stationName);
	}
	
	public double receiveTrainActualSpeed(String trainID) {
		return trainModel.getVelocity(trainID);
	}
	
	public double receiveSetpointSpeed(String trainID) {
		return trainModel.getSetpointSpeed(trainID);
	}
	
	public double receiveCtcAuthority(String trainID) {
		return trainModel.getAuthority(trainID);
	}
	
	public int receiveTrainPosition(String trainID) {
		return trainModel.getBlockID(trainID);
	}
	
	public int receiveTrainDirection(String trainID) {
		return trainModel.getTrain(trainID).getPosition().getCurrentDirection();
	}
	
	public boolean receivePassengerEmergencyBrake(String trainID) {
		return trainModel.getPassengerEmergencyBrake(trainID);
	}
	
	public int receiveBeaconValue(String trainID) {
		return trainModel.getBeacon(trainID);
	}
	
	public void receiveMap() {
		ArrayList<Block> redBlocks = trackModel.getTrack("RED");
		ArrayList<Block> greenBlocks = trackModel.getTrack("GREEN");
		redInfo = new ArrayList<BlockInfo>();
		greenInfo = new ArrayList<BlockInfo>();
		Station S;
		for (Block B : redBlocks) {
			S = B.getStation();
			if(S==null) {
				redInfo.add(new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), null, false, false, B.getDirection()));
			}
			else {
				redInfo.add(new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), S.getId(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));
			}
		}
		for (Block B : greenBlocks) {
			S = B.getStation();
			if(S==null) {
				greenInfo.add(new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), null, false, false, B.getDirection()));			
			}
			else {
				greenInfo.add(new BlockInfo(B.getSpeedLimit(), B.getUndergroundStatus(), S.getId(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));			
			}
		}
	}

	@Override
	public boolean updateTime(SimTime time) {
		for (TrnController T : controlList.values()) {
			T.updateTime();
		}
		return true;
	}

	@Override
	public boolean communicationEstablished() {
		receiveMap();
		return true;
	}
}
