//Michael Kotcher

package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

public class TrnController {
	private String trainID;
	private String line;
	private String currentStation;
	private PIController pi;
	private TrainControllerGUI controlGUI;
	private TrainController controller;
	//private int deltaTime;
	private int driveMode;		//0 is auto, 1 is manual
	private int blockMode;		//0 is moving, 1 is fixed
	private int temperature;
	private int beacon;
	private int stationTimeCounter;
	private double ctcAuth;
	private double mboAuth;
	private double distToStation;
	private double overallAuth;
	private double actualSpeed;
	private double setpointSpeed;
	private double speedLimit;
	private double power;
	private double safeBrakingDistance;
	private boolean rightDoor;
	private boolean leftDoor;
	private boolean lightState;
	private boolean sBrakes;
	private boolean eBrakes;
	private boolean passEBrakes;
	private boolean inStation;
	private double position[2];
	//private mapInfo
	
	public TrnController(String id, String ln, int startBlock, TrainController C) {
		trainID = id;
		line = ln;
		currentStation = null;
		controller = C;
		pi = new PIController(//p and i values HERE);
		controlGUI = new TrainControllerGUI(pi, this, trainID);
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
	}
	
	public boolean timeUpdate() {
		actualSpeed = controller.receiveTrainActualSpeed(trainID);
		ctcAuth = controller.receiveCtcAuthority(trainID);
		passEBrakes = controller.receivePassengerEmergency(trainID);
		position = controller.receiveTrainPosition(trainID);
		if (driveMode == 0) {		//if auto
			setpointSpeed = controller.receiveSetpointSpeed(trainID);
			if (inStation) {
				stationTimeCounter++;
				if (stationTimeCounter == 120)
				{
					closeLeft();
					closeRight();
					inStation = false;
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
				calcPowerOutput();
				stationCheck();
				if (lightCheck()) {
					lightsOn();
				}
				else {
					lightsOff();
				}
			}
		}
		else {		//if manual
			if (passEBrakes) {
				engineOff();
				eBrakesOn();
			}
			else if (brakingCheck()) {
				engineOff();
				sBrakesOn();
			}
		}
		updateGUI();
	}
	
	public void updateGUI() {
		//
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
		controlGUI.setRight(true);
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
		//controlGUI.setSetpoint(speed);
	}
	
	public void setEmergencyBrake(boolean status) {
		ebrakes = status;
		//controlGUI.setEmergency(status);
	}
	
	public void setBeacon(int value) {
		beacon = value;
	}
	
	/*public void
	
	public double getActualSpeed() {
		return controller.receiveTrainActualSpeed(trainID);
	}*/
	
	private void calcPowerOutput()
	{
		if (sBrakes || eBrakes) {
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
		if (overallAuth <= safeBrakingDistance) {
			//engineOff();
			//sBrakesOn();
			controlGUI.setPower(power);
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean lightCheck() {
		//somehow access stored map data
		//if underground {
			return true;
		//}
		//else {
			return false;
		//}
	}
	
	private void stationCheck()
	{
		if (actualSpeed == 0) {
			//somehow access stored map data
			//if current block equals station {
				inStation = true;
				announceArrived(currentStation);
				//open doors, side derived map info
				//move
			//}
		}
	}
	
	private void decodeBeacon(int beacon)
	{
		String stationName;
		//get station name
		if (stationName == currentStation) {
			currentStation = null;
		}
		else {
			currentStation = stationName;
			announceApproach(stationName);
		}
	}
	
	private void announceApproach(String stationName) {
		String announcement = "Now approaching station: " + stationName + "";
		controller.transmitAnnouncement(trainID, announcement);
	}
	
	private void announceArrived(String stationName) {
		String announcement = "Arrived at station: " + stationName + "";
		controller.transmitAnnouncement(trainID, announcement);
	}
}