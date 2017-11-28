//Michael Kotcher

package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import java.util.ArrayList;

public class TrnController {
	private String trainID;
	private String line;
	private String currentStation;
	private PIController pi;
	private TrainControllerGUI mainGUI;
	private TrnControllerGUI controlGUI;
	private TrainController controller;
	private int driveMode;					//0 is auto, 1 is manual
	private int blockMode;					//0 is moving, 1 is fixed
	private int temperature;				//F
	private int beacon;						//32 bit integer
	private int stationTimeCounter;
	private int currentBlock;
	private int trainDirection;
	private double ctcAuth;					//meters
	private double mboAuth;					//meters
	private double distToStation;			//meters
	private double overallAuth;				//meters
	private double actualSpeed;				//meters per second
	private double setpointSpeed;			//meters per second
	private double speedLimit;				//meters per second
	private double power;					//watts
	private double safeBrakingDistance;		//meters
	private boolean rightDoor;
	private boolean leftDoor;
	private boolean lightState;
	private boolean sBrakes;
	private boolean eBrakes;
	private boolean passEBrakes;
	private boolean inStation;
	private boolean ready;
	private ArrayList<BlockInfo> mapInfo;
	private BlockInfo currentBlockInfo;
	private String[] stationList;
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;
	
	public TrnController(String id, String ln, TrainController C, ArrayList<BlockInfo> map, TrainControllerGUI g, String[] s) {
		trainID = id;
		line = ln;
		currentStation = null;
		ready = false;
		if (line.equals("RED")) {
			currentBlock = 77;	//yard
		}
		else {
			currentBlock = 152;	//yard_OUT
		}
		controller = C;
		pi = new PIController(200, 300);
		driveMode = 0;
		blockMode = 0;
		beacon = 0;
		ctcAuth = 0;
		mboAuth = 0;
		overallAuth = 0;
		actualSpeed = 0;
		setpointSpeed = 0;
		speedLimit = 0;
		power = 0;
		safeBrakingDistance = 0;
		rightDoor = false;
		leftDoor = false;
		lightState = false;
		sBrakes = false;
		eBrakes = false;
		passEBrakes = false;
		temperature = 70;
		mapInfo = map;
		stationList = s;
		trainDirection = 0;
		mainGUI = g;
		controlGUI = new TrnControllerGUI(pi, this, trainID);
		g.add(controlGUI);
	}
	
	public boolean updateTime() {
		actualSpeed = controller.receiveTrainActualSpeed(trainID);
		ctcAuth = controller.receiveCtcAuthority(trainID);
		passEBrakes = controller.receivePassengerEmergencyBrake(trainID);	//NEED TO IMPLEMENT
		currentBlock = controller.receiveTrainPosition(trainID);
		currentBlockInfo = mapInfo.get(currentBlock - 1);
		speedLimit = currentBlockInfo.getSpeedLimit();
		if (driveMode == 0) {		//if auto
			setpointSpeed = controller.receiveSetpointSpeed(trainID);
			if (inStation) {
				stationTimeCounter++;
				if (stationTimeCounter == 120)
				{
					stationTimeCounter = 0;
					closeLeft();
					closeRight();
					inStation = false;
					announceDeparting(currentStation);
				}
			}
			else if (passEBrakes) {
				engineOff();
				eBrakesOn();
			}
			else if (brakingCheck()) {
				engineOff();
				sBrakesOn();
			}
			else {
				calcAuth();
				calcPowerOutput();
				if (currentBlock != 0) {
					stationCheck();
					if (lightCheck()) {
						lightsOn();
					}
					else {
						lightsOff();
					}
				}
			}
		}
		else {		//if manual - THIS NEEDS WORK
			if (inStation) {
				if (actualSpeed > 0) {
					stationTimeCounter = 0;
					inStation = false;
					announceDeparting(currentStation);
				}
			}
			else if (passEBrakes) {
				engineOff();
				eBrakesOn();
			}
			else if (brakingCheck()) {
				engineOff();
				sBrakesOn();
			}
			else {
				calcAuth();
				calcPowerOutput();
				stationCheck();
			}
		}
		decodeBeacon();		//may have to poll the train model for beacon info as well
		updateGUI();
		return true;
	}
	
	public boolean updateGUI() {
		controlGUI.setLeft(leftDoor);
		controlGUI.setRight(rightDoor);
		controlGUI.setLights(lightState);
		controlGUI.setService(sBrakes);
		controlGUI.setEmergency(eBrakes);
		controlGUI.setPower(power);
		controlGUI.setSpeed(actualSpeed);
		controlGUI.setSetpoint(setpointSpeed);
		controlGUI.setAuth(overallAuth);
		controlGUI.guiUpdate();
		return true;
	}
	
	public void openLeft() {
		leftDoor = true;
		controller.transmitLeft(trainID, leftDoor);
		controlGUI.setLeft(true);
	}
	
	public void closeLeft() {
		leftDoor = false;
		controller.transmitLeft(trainID, leftDoor);
		controlGUI.setLeft(false);
	}
	
	public void openRight() {
		rightDoor = true;
		controller.transmitRight(trainID, rightDoor);
		controlGUI.setRight(true);
	}
	
	public void closeRight() {
		rightDoor = false;
		controller.transmitRight(trainID, rightDoor);
		controlGUI.setRight(false);
	}
	
	public void lightsOn() {
		lightState = true;
		controller.transmitLights(trainID, lightState);
		controlGUI.setLights(true);
	}
	
	public void lightsOff() {
		lightState = false;
		controller.transmitLights(trainID, lightState);
		controlGUI.setLights(false);
	}
	
	public void sBrakesOn() {
		sBrakes = true;
		controller.transmitService(trainID, sBrakes);
		controlGUI.setService(true);
	}
	
	public void sBrakesOff() {
		sBrakes = false;
		controller.transmitService(trainID, sBrakes);
		controlGUI.setService(false);
	}
	
	public void eBrakesOn() {
		eBrakes = true;
		controller.transmitEmergency(trainID, eBrakes);
		controlGUI.setEmergency(true);
	}
	
	public void eBrakesOff() {
		eBrakes = false;
		controller.transmitEmergency(trainID, eBrakes);
		controlGUI.setEmergency(false);
	}
	
	public void engineOff() {
		power = 0;
		controller.transmitPower(trainID, power);
		controlGUI.setPower(power);
	}
	
	public void setTemperature(int temp) {
		temperature = temp;
		controller.transmitTemperature(trainID, temperature);
	}
	
	public void setDriveMode(int mode) {
		driveMode = mode;
	}
	
	public void setCtcAuthority(double auth) {
		ctcAuth = auth;
	}
	
	public void setMboAuthority(double auth) {
		mboAuth = auth;
	}
	
	public void setSafeBrakingDistance(double dist) {
		safeBrakingDistance = dist;
	}
	
	public void setSetpointSpeed(double speed) {
		setpointSpeed = speed;
		controlGUI.setSetpoint(speed);
	}
	
	public void setPassengerEmergencyBrake(boolean status) {
		passEBrakes = status;
	}
	
	public void setBeacon(int value) {
		beacon = value;
	}
	
	private void calcPowerOutput()
	{
		if (sBrakes || eBrakes) {
			power = 0;
		}
		else if (!ready) {
			power = 0;
		}
		else {
			if (setpointSpeed > speedLimit) {
				setpointSpeed = speedLimit;
			}
			power = pi.getOutput(actualSpeed, setpointSpeed);
		}
		controlGUI.setPower(power);
		controller.transmitPower(trainID, power);
	}
	
	private void calcAuth() {
		if (blockMode == 1) {
			overallAuth = ctcAuth;
		}
		else {
			distToStation = ctcAuth;
			overallAuth = Math.min(ctcAuth, mboAuth);
		}
		controlGUI.setAuth(overallAuth);
	}
	
	private boolean brakingCheck() {
		if (overallAuth <= safeBrakingDistance || distToStation <= safeBrakingDistance) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean lightCheck() {
		if (currentBlockInfo.isUnderground()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void stationCheck() {
		if (actualSpeed == 0 && currentBlockInfo.getStationName() != "") {
			inStation = true;
			announceArrived(currentBlockInfo.getStationName());
			if (driveMode == 0) {
				trainDirection = controller.receiveTrainDirection(trainID);
				if (currentBlockInfo.getDirection() == 1 || currentBlockInfo.getDirection() == -1) {
					if (currentBlockInfo.getPositive()) {
						openRight();
					}
					else {
						openLeft();
					}
				}
				else {
					if (trainDirection == 1) {
						if (currentBlockInfo.getPositive()) {
							openRight();
						}
						else {
							openLeft();
						}
					}
					else {
						if (currentBlockInfo.getNegative()) {
							openRight();
						}
						else {
							openLeft();
						}
					}
				}
			}
		}
	}
	
	private void decodeBeacon()
	{
		String stationName;
		if (beacon != 0)
		{
			if (currentStation == null) {
				stationName = stationList[beacon];
				currentStation = stationName;
				announceApproach(stationName);
			}
			else { //currentStation == beaconStation
				currentStation = null;
				announceEnRoute();
			}
		}
	}
	
	public void signalReady() {
		ready = true;
	}
	
	private void announceApproach(String stationName) {
		controller.transmitAnnouncement(trainID, APPROACHING, stationName);
	}
	
	private void announceArrived(String stationName) {
		controller.transmitAnnouncement(trainID, ARRIVED, stationName);
	}
	
	private void announceDeparting(String stationName) {
		controller.transmitAnnouncement(trainID, DEPARTING, stationName);
	}
	
	private void announceEnRoute() {
		controller.transmitAnnouncement(trainID, ENROUTE, "");
	}
}