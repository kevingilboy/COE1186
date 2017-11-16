package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

public class TrnController {
	private String trainID;
	private String line;
	private PIController pi;
	private TrainControllerGUI controlGUI;
	private TrainController controller;
	private int deltaTime;
	private int driveMode;		//0 is auto, 1 is manual
	private int blockMode;		//0 is moving, 1 is fixed
	private int temperature;
	private double ctcAuth;
	private double mboAuth;
	private double overallAuth;
	private double actualSpeed;
	private double setpointSpeed;
	private double power;
	private double safeBrakingDistance;
	private boolean rightDoor;
	private boolean leftDoor;
	private boolean lightState;
	private boolean sBrakes;
	private boolean eBrakes;
	//private double position[2];
	//private int_32 beacon;
	//private mapInfo
	
	public TrnController(String id, String ln, int startBlock, TrainController C) {
		trainID = id;
		line = ln;
		controller = C;
		ctcAuth = 0;
		mboAuth = 0;
		overallAuth = 0;
		actualSpeed = 0;
		setpointSpeed = 0;
		power = 0;
		safeBrakingDistance = 0;
		rightDoor = false;
		leftDoor = false;
		lightState = false;
		sBrakes = false;
		eBrakes = false;
		temperature = 70;
		driveMode = 0;
		blockMode = 0;
	}
	
	public boolean timeUpdate() {
		//still have questions...
		controlGUI.updateGUI();
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
		controlGUI.setSetpoint(speed);
	}
	
	public void setEmergencyBrake(boolean status) {
		ebrakes = status;
		controlGUI.setEmergency(status);
	}
	
	public void setBeacon(int_32 value) {
		beacon = value;
	}
	
	public double getActualSpeed() {
		return controller.receiveTrainActualSpeed(trainID);
	}
	
	public void calcPowerOutput()
	{
		if (sBrakes || eBrakes) {
			power = 0;
		}
		else {
			power = pi.getOutput(actualSpeed, setpointSpeed);
		}
		controlGUI.setPower(power);
		controller.transmitPower(trainID, power);
	}
	
	public void calcAuth() {
		overallAuth = Math.min(ctcAuth, mboAuth);
		controlGUI.setAuth(overallAuth);
	}
	
	public void brakingCheck() {
		if (overallAuth <= safeBrakingDistance) {
			power = 0;
			sBrakesOn();
			controlGUI.setPower(power);
		}
	}
}