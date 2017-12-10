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
	private int blockMode = 0;
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;
	
	public final double SPEEDCONVERSION = 3.6;			//1 m/s = 3.6 kph

	public TrainController() {
		controlList = new HashMap<String, TrnController>();
		mainGUI = new TrainControllerGUI();
		stationList = new String[]{"", "Pioneer", "Edgebrook", "Station", "Whited", "South Bank", "Central", "Inglewood", "Overbrook", "Glenbury", "Dormont", "Mt. Lebanon", "Poplar", "Castle Shannon", "Glenbury", "Overbrook", "Inglewood", "Central", "Shadyside", "Herron Avenue", "Swissville", "Penn Station", "Steel Plaza", "First Avenue", "Station Square", "South Hills Junction"};
	}
	
	public void dispatchTrain(String trainID, String line) {
		double p, i;
		p = mainGUI.getP();
		i = mainGUI.getI();
		while (i == -1 || p == -1) {
			p = mainGUI.getP();
			i = mainGUI.getI();
		}
		if (line.equals("RED")) {
			controlList.put(trainID, new TrnController(trainID, line, this, redInfo, mainGUI, stationList, p, i, blockMode));
		}
		else {
			controlList.put(trainID, new TrnController(trainID, line, this, greenInfo, mainGUI, stationList, p, i, blockMode));
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
		trainModel.setPassengerEmergencyBrake(trainID, status);
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
	
	public double receiveTrainActualSpeed(String trainID) {						//train model calculates actual speed in m/s
		return trainModel.getVelocity(trainID);
	}
	
	public double receiveSetpointSpeed(String trainID) {
		return (trainModel.getSetpointSpeed(trainID) / SPEEDCONVERSION);		//train model passes along setpoint from ctc, which is in kph
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
	
	public void enableMovingBlockMode(Boolean isMovingBlock) {
		if (isMovingBlock) {
			//Set to moving block mode
			blockMode = 0;
		}
		else {
			//Set to fixed block mode
			blockMode = 1;
		}
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
				redInfo.add(new BlockInfo((double)B.getSpeedLimit(), B.getUndergroundStatus(), "", false, false, B.getDirection()));
			}
			else {
				redInfo.add(new BlockInfo((double)B.getSpeedLimit(), B.getUndergroundStatus(), S.getId(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));
			}
		}
		for (Block B : greenBlocks) {
			S = B.getStation();
			if(S==null) {
				greenInfo.add(new BlockInfo((double)B.getSpeedLimit(), B.getUndergroundStatus(), "", false, false, B.getDirection()));			
			}
			else {
				greenInfo.add(new BlockInfo((double)B.getSpeedLimit(), B.getUndergroundStatus(), S.getId(), S.getDoorSideDirectionPositive(), S.getDoorSideDirectionNegative(), B.getDirection()));			
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
		System.out.println("Train Controller Communication Established!");
		return true;
	}
}
