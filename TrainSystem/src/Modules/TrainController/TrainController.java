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
	private HashMap<String, TrnController> controlList;		//stores individual controller objects for each train
	private TrainControllerGUI mainGUI;						//reference to main GUI
	public TrackModel trackModel;							//communication references to other modules
	public TrainModel trainModel;
	private ArrayList<BlockInfo> redInfo;					//holds all relevant info about both tracks
	private ArrayList<BlockInfo> greenInfo;
	private String[] stationList;							//array of station names
	private int blockMode;									//0 indicates MBO mode, 1 indicates fixed mode
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;
	
	public final double SPEEDCONVERSION = 3.6;			//1 m/s = 3.6 kph

	/**
	 * Called by the SimulatorGUI class to show the GUI when this module is selected
	 */
	public void showGUI(){
		mainGUI.showGUI();
	}

	public TrainController() {
		controlList = new HashMap<String, TrnController>();
		mainGUI = new TrainControllerGUI();
		stationList = new String[]{"", "Pioneer", "Edgebrook", "Station", "Whited", "South Bank", "Central", "Inglewood", "Overbrook", "Glenbury", "Dormont", "Mt. Lebanon", "Poplar", "Castle Shannon", "Glenbury", "Overbrook", "Inglewood", "Central", "Shadyside", "Herron Avenue", "Swissville", "Penn Station", "Steel Plaza", "First Avenue", "Station Square", "South Hills Junction"};
	}

	public TrnController getController(String ID){
		return controlList.get(ID);
	}
	
	//gets P and I values from main GUI, or waits until values are available, and creates an individual train controller (TrnController) object with relevant information about its track
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
	
	//called by the MBO, sets authority to a particular train
	public void setMboAuthority(String trainID, double auth) {
		TrnController C = controlList.get(trainID);
		if(C==null) return;
		C.setMboAuthority(auth);
	}
	
	//called by the MBO, sets the safe braking distance of a particular train
	public void setSafeBrakingDistance(String trainID, double dist) {
		TrnController C = controlList.get(trainID);
		if(C==null) return;
		C.setSafeBrakingDistance(dist);
	}
	
	//called by the Train Model, sets the passenger e-brake status of a particular train
	/*public void setPassengerEmergencyBrake(String trainID, boolean status) {
		TrnController C = controlList.get(trainID);
		C.setPassengerEmergencyBrake(status);
	}*/
	
	/////these functions called by a particular TrnController object to pass a value through this object to the Train Model////////
	
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////these functions called by a particular TrnController object to access a value from the Train Model through this object/////
	
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//called by the CTC to choose whether the system will run in fixed or moving block mode
	public void enableMovingBlockMode(Boolean isMovingBlock) {
		if (isMovingBlock) {
			blockMode = 0;		//set to moving block mode
		}
		else {
			blockMode = 1;		//set to fixed block mode
		}
	}
	
	//stores all valid track information from the Track Model in local ArrayLists that are then passed to each individual TrnController object as it is created
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

	//called by the Simulator at every clock tick to drive the simulation
	@Override
	public boolean updateTime(SimTime time) {
		for (TrnController T : controlList.values()) {
			T.updateTime();
		}
		return true;
	}

	//allows the module to receive map info from the Track Model before the simulation actually begins
	@Override
	public boolean communicationEstablished() {
		receiveMap();
		return true;
	}

	public void trainPoofByName(String line, String name) {
		TrnController tc = controlList.remove(name);
		tc = null;
		mainGUI.trainPoofByName(name);
	}
}
