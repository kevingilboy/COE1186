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
	private int currentBlock;
	private int trainDirection;
	private int storedBeacon;
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
	private boolean passed;
	private ArrayList<BlockInfo> mapInfo;
	private BlockInfo currentBlockInfo;
	private String[] stationList;
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;
	
	public TrnController(String id, String ln, TrainController C, ArrayList<BlockInfo> map, TrainControllerGUI g, String[] s, double p, double i, int b) {
		trainID = id;
		line = ln;
		currentStation = null;
		ready = true;
		if (line.equals("RED")) {
			currentBlock = 76;	//yard
		}
		else {
			currentBlock = 151;	//yard_OUT
		}
		controller = C;
		pi = new PIController(p, i);
		driveMode = 0;
		blockMode = b;
		//blockMode = 1;
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
		controlGUI = new TrnControllerGUI(this, trainID);
		g.add(controlGUI);
		inStation = false;
		passed = true;
	}
	
	public boolean updateTime() {
		actualSpeed = controller.receiveTrainActualSpeed(trainID);
		ctcAuth = controller.receiveCtcAuthority(trainID);
		passEBrakes = controller.receivePassengerEmergencyBrake(trainID);
		currentBlock = controller.receiveTrainPosition(trainID);
		currentBlockInfo = mapInfo.get(currentBlock);
		speedLimit = (double)currentBlockInfo.getSpeedLimit();
		controlGUI.setSpeedLimit(speedLimit);
		beacon = controller.receiveBeaconValue(trainID);
		calcAuth();
		if (driveMode == 0) {		//if auto
			setpointSpeed = controller.receiveSetpointSpeed(trainID);
			if (inStation) {
				if (overallAuth > 0)
				{
					closeLeft();
					closeRight();
					inStation = false;
					announceDeparting(currentStation);
					calcPowerOutput();
				}
			}
			else if (brakingCheck()) {
				//actions performed in function
			}
			else {
				calcPowerOutput();
			}
			if (lightCheck()) {
				lightsOn();
			}
			else {
				lightsOff();
			}
		}
		else {		//if manual
			if (inStation) {
				calcPowerOutput();
				if (actualSpeed > 0 && power > 0) {
					inStation = false;
					announceDeparting(currentStation);
				}
			}
			else if (brakingCheck()) {
				//actions performed in function
			}
			else {
				calcPowerOutput();
			}
		}
		decodeBeacon();
		stationCheck();
		if (!inStation) {
			controlGUI.setSuggestedDoor(0);
		}
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
	}
	
	public void closeLeft() {
		leftDoor = false;
		controller.transmitLeft(trainID, leftDoor);
	}
	
	public void openRight() {
		rightDoor = true;
		controller.transmitRight(trainID, rightDoor);
	}
	
	public void closeRight() {
		rightDoor = false;
		controller.transmitRight(trainID, rightDoor);
	}
	
	public void lightsOn() {
		lightState = true;
		controller.transmitLights(trainID, lightState);
	}
	
	public void lightsOff() {
		lightState = false;
		controller.transmitLights(trainID, lightState);
	}
	
	public void sBrakesOn() {
		sBrakes = true;
		controller.transmitService(trainID, sBrakes);
	}
	
	public void sBrakesOff() {
		sBrakes = false;
		controller.transmitService(trainID, sBrakes);
	}
	
	public void eBrakesOn() {
		eBrakes = true;
		controller.transmitEmergency(trainID, eBrakes);
	}
	
	public void eBrakesOff() {
		eBrakes = false;
		controller.transmitEmergency(trainID, eBrakes);
	}
	
	public void engineOff() {
		power = 0;
		controller.transmitPower(trainID, power);
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
		if (speed > speedLimit) {
			speed = speedLimit;
		}
		setpointSpeed = speed;
	}
	
	public void setPassengerEmergencyBrake(boolean status) {
		passEBrakes = status;
	}
	
	public void setBeacon(int value) {
		beacon = value;
	}
	
	private void calcPowerOutput()
	{
		if (sBrakes || eBrakes || leftDoor || rightDoor) {
			power = 0;
		}
		else if (!ready) {
			power = 0;
		}
		else {
			if (setpointSpeed > speedLimit) {
				setpointSpeed = speedLimit;
			}
			double power1 = pi.getOutput(actualSpeed, setpointSpeed);
			double power2 = pi.getOutput(actualSpeed, setpointSpeed);
			double power3 = pi.getOutput(actualSpeed, setpointSpeed);
			if (power1 == power2 && power2 == power3) {
				power = power1;
			}
			else {
				power = Math.min(power1, Math.min(power2, power3));
			}
		}
		controller.transmitPower(trainID, power);
	}
	
	private void calcAuth() {
		distToStation = ctcAuth;
		if (blockMode == 1) {
			overallAuth = ctcAuth;
		}
		else {
			overallAuth = Math.min(ctcAuth, mboAuth);
		}
	}
	
	private boolean brakingCheck() {
		if (passEBrakes) {
			engineOff();
			eBrakesOn();
			return true;
		}
		/*else if (overallAuth == 0 && actualSpeed > 0) {
			engineOff();
			eBrakesOn();
			return true;
		}*/
		else if ((overallAuth <= safeBrakingDistance && actualSpeed > 0) || (distToStation <= safeBrakingDistance && actualSpeed > 0)) {
			engineOff();
			sBrakesOn();
			return true;
		}
		else if (overallAuth == 0 && actualSpeed == 0) {
			sBrakesOff();
			eBrakesOff();
			return true;
		}
		else if (overallAuth >= 0 && actualSpeed == 0) {
			sBrakesOff();
			eBrakesOff();
			return false;
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
		if (actualSpeed == 0 && power == 0 && !currentBlockInfo.getStationName().equals("") && !inStation) {
			inStation = true;
			sBrakesOff();
			eBrakesOff();
			announceArrived(currentBlockInfo.getStationName());
			trainDirection = controller.receiveTrainDirection(trainID);
			if (driveMode == 0) {		//auto driving
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
			else {		//manual driving
				setpointSpeed = 0;
				controlGUI.setSetpoint(0);
				if (currentBlockInfo.getDirection() == 1 || currentBlockInfo.getDirection() == -1) {
					if (currentBlockInfo.getPositive()) {
						controlGUI.setSuggestedDoor(1);
					}
					else {
						controlGUI.setSuggestedDoor(-1);
					}
				}
				else {
					if (trainDirection == 1) {
						if (currentBlockInfo.getPositive()) {
							controlGUI.setSuggestedDoor(1);
						}
						else {
							controlGUI.setSuggestedDoor(-1);
						}
					}
					else {
						if (currentBlockInfo.getNegative()) {
							controlGUI.setSuggestedDoor(1);
						}
						else {
							controlGUI.setSuggestedDoor(-1);
						}
					}
				}
			}
		}
	}
	
	private void decodeBeacon() {
		if (!inStation) {
			if (storedBeacon == 0 && passed && beacon != 0) {
				currentStation = stationList[beacon];
				announceApproach(currentStation);
				storedBeacon = beacon;
				passed = false;
			}
			else if (beacon == storedBeacon && passed && beacon != 0) {
				announceEnRoute();
				storedBeacon = 0;
				passed = false;
			}
			else if (beacon == 0) {
				passed = true;
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