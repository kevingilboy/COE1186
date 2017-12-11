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
	private int driveMode;						//0 is auto, 1 is manual
	private int blockMode;						//0 is moving, 1 is fixed
	private int temperature;					//F
	private int beacon;							//32 bit integer
	private int currentBlock;
	private int trainDirection;
	private int storedBeacon;
	private double ctcAuth;						//meters
	private double mboAuth;						//meters
	private double distToStation;				//meters
	private double overallAuth;					//meters
	private double actualSpeed;					//meters per second
	private double setpointSpeed;				//meters per second
	private double speedLimit;					//meters per second
	private double power;						//watts
	private double safeBrakingDistance;			//meters
	private double estimatedBrakingDistance;	//meters
	private boolean rightDoor;
	private boolean leftDoor;
	private boolean lightState;
	private boolean sBrakes;
	private boolean eBrakes;
	private boolean passEBrakes;
	private boolean inStation;
	//private boolean ready;
	private boolean passed;
	private ArrayList<BlockInfo> mapInfo;
	private BlockInfo currentBlockInfo;
	private String[] stationList;
	
	public final int APPROACHING = 0;
	public final int ARRIVED = 1;
	public final int DEPARTING = 2;
	public final int ENROUTE = 3;
	
	//creates an individual train controller object, assigning references and initial values
	public TrnController(String id, String ln, TrainController C, ArrayList<BlockInfo> map, TrainControllerGUI g, String[] s, double p, double i, int b) {
		trainID = id;
		line = ln;
		currentStation = null;
		//ready = true;
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
		temperature = 69;
		mapInfo = map;
		stationList = s;
		trainDirection = 0;
		mainGUI = g;
		controlGUI = new TrnControllerGUI(this, trainID);
		g.add(controlGUI);
		inStation = false;
		passed = true;
	}
	
	//drives the simulation of a train - receives current information, calculates power, and takes any other necessary actions
	public boolean updateTime() {
		actualSpeed = controller.receiveTrainActualSpeed(trainID);
		estimatedBrakingDistance = estimateBrakingDist(actualSpeed);		//estimate the braking distance, this value only used in fixed block mode
		ctcAuth = controller.receiveCtcAuthority(trainID);
		passEBrakes = controller.receivePassengerEmergencyBrake(trainID);
		currentBlock = controller.receiveTrainPosition(trainID);
		currentBlockInfo = mapInfo.get(currentBlock);
		speedLimit = (double)currentBlockInfo.getSpeedLimit();
		controlGUI.setSpeedLimit(speedLimit);
		beacon = controller.receiveBeaconValue(trainID);
		calcAuth();
		if (driveMode == 0) {		//if auto
			setpointSpeed = controller.receiveSetpointSpeed(trainID);		//in auto, the train uses the setpoint speed from the CTC
			if (inStation) {
				if (overallAuth > 0)	//train leaves a station in auto mode when the dwell time is over and the CTC sends a non-zero authority
				{
					closeLeft();
					closeRight();
					inStation = false;
					announceDeparting(currentStation);
					calcPowerOutput();		//must immediately calculate new power, or the train will think it is in station again when stationCheck() is performed
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
				if (actualSpeed > 0 && power > 0) {		//driver chooses when to leave station by setting a non-zero setpoint speed
					inStation = false;
					announceDeparting(currentStation);
				}
			}
			else if (brakingCheck()) {
				//actions performed in function
			}
			else {
				calcPowerOutput();		//setpoint comes from the driver (TrnControllerGUI)
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
	
	//sets values to the TrnControllerGUI object, and updates the GUI
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
	
	/////////these functions perform basic actions on the train and notify the Train Model to update its information///////////////
	
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
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//called by the TrnControllerGUI object if the train is switched from auto to manual driving mode, or vice versa
	public void setDriveMode(int mode) {
		driveMode = mode;
	}
	
	//called by the CTC
	/*public void setCtcAuthority(double auth) {
		ctcAuth = auth;
	}*/
	
	///////////////these functions called by the TrainController object when the MBO sets values to the train////////////////////
	
	public void setMboAuthority(double auth) {
		mboAuth = auth;
	}
	
	public void setSafeBrakingDistance(double dist) {
		safeBrakingDistance = dist;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//called by the TrnControllerGUI object when the driver sets a new speed, regulates speed to current speed limit
	public void setSetpointSpeed(double speed) {
		if (speed > speedLimit) {
			speed = speedLimit;
		}
		setpointSpeed = speed;
	}
	
	/*public void setPassengerEmergencyBrake(boolean status) {
		passEBrakes = status;
	}
	
	public void setBeacon(int value) {
		beacon = value;
	}*/
	
	//calculates a power output to send to the Train Model
	private void calcPowerOutput()
	{
		if (sBrakes || eBrakes || leftDoor || rightDoor) {		//won't calculate power if brakes are on or doors are open
			power = 0;
		}
		/*else if (!ready) {
			power = 0;
		}*/
		else {
			if (setpointSpeed > speedLimit) {		//regulate setpoint to speed limit
				setpointSpeed = speedLimit;
			}
			double power1 = pi.getOutput(actualSpeed, setpointSpeed);	
			double power2 = pi.getOutput(actualSpeed, setpointSpeed);
			double power3 = pi.getOutput(actualSpeed, setpointSpeed);
			if (power1 == power2 && power2 == power3) {		//use a voting system between three results as safety critical architecture
				power = power1;
			}
			else {		//if they don't all agree, use minimum power output
				power = Math.min(power1, Math.min(power2, power3));
			}
		}
		controller.transmitPower(trainID, power);
	}
	
	//calculate the current overall authority, based on the ctc authority, mbo authority, and block mode
	private void calcAuth() {
		distToStation = ctcAuth;		//ctc always sends the distance to the next station, or block to stop at
		if (blockMode == 1) {			//if fixed block mode
			overallAuth = ctcAuth;		//overall authority only takes ctc input into consideration
		}
		else {							//if moving block mode
			overallAuth = Math.min(ctcAuth, mboAuth);		//overall authority is the lesser of the ctc and mbo authority
		}
	}
	
	//check if brakes need to be applied, and take necessary actions
	private boolean brakingCheck() {
		if (passEBrakes) {		//if passengers have pulled the brake
			engineOff();		//set power to zero
			eBrakesOn();		//activate e-brakes
			return true;
		}
		/*else if (overallAuth == 0 && actualSpeed > 0) {
			engineOff();
			eBrakesOn();
			return true;
		}*/
		
		//if the authority is less than braking distance in moving block mode or less than estimated braking distance in fixed block mode
		else if ((overallAuth <= safeBrakingDistance && actualSpeed > 0 && blockMode == 0) || (distToStation <= safeBrakingDistance && actualSpeed > 0 && blockMode == 0) || (overallAuth <= estimatedBrakingDistance && actualSpeed > 0 && blockMode == 1) || (distToStation <= estimatedBrakingDistance && actualSpeed > 0 && blockMode == 1)) {
			engineOff();		//set power to zero
			sBrakesOn();		//apply service brakes
			return true;
		}
		else if (overallAuth == 0 && actualSpeed == 0) {		//if stopped train has zero authority
			sBrakesOff();		//remove brakes
			eBrakesOff();
			return true;		//don't allow train to calculate a power output
		}
		else if (overallAuth > 0 && actualSpeed == 0) {			//if stopped train has authority
			sBrakesOff();		//remove brakes
			eBrakesOff();
			return false;		//allow train to calculate a power output
		}
		else {
			return false;		//allow train to calculate a power output
		}
	}
	
	//checks if current block is underground to control automatic light function
	private boolean lightCheck() {
		if (currentBlockInfo.isUnderground()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//checks if a train is at a station, and takes necessary actions
	private void stationCheck() {
		if (actualSpeed == 0 && power == 0 && !currentBlockInfo.getStationName().equals("") && !inStation) {	//if stopped train is in a station block and is not already in station
			inStation = true;
			sBrakesOff();
			eBrakesOff();
			announceArrived(currentBlockInfo.getStationName());
			trainDirection = controller.receiveTrainDirection(trainID);
			if (driveMode == 0) {		//auto driving
				if (currentBlockInfo.getDirection() == 1 || currentBlockInfo.getDirection() == -1) {	//for unidirectional tracks
					if (currentBlockInfo.getPositive()) {		//only positive door side is considered
						openRight();
					}
					else {
						openLeft();
					}
				}
				else {
					if (trainDirection == 1) {		//if train is moving in the positive direction
						if (currentBlockInfo.getPositive()) {		//open the door indicated by the positive door side
							openRight();
						}
						else {
							openLeft();
						}
					}
					else {
						if (currentBlockInfo.getNegative()) {		//open the door indicated by the negative door side
							openRight();
						}
						else {
							openLeft();
						}
					}
				}
			}
			else {		//manual driving - does the same as auto but sets setpoint to zero and just calculates a recommended door side
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
	
	//calculates when announcements need to be made based on beacon values
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
	
	//calculates an estimated braking distance with known information (does not factor in friction)
	private double estimateBrakingDist(double initVelocity){
    	double stopDist = ((-1)*(initVelocity)*(initVelocity)) / (2*(-1.2));	//service break decel = -1.2 as given
    	return stopDist;
	}
	
	/*public void signalReady() {
		ready = true;
	}*/
	
	///////////////////////these functions transmit announcement info to the Train Model/////////////////////////////////////
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