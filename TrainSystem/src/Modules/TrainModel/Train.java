/**
 * Author: Jennifer Patterson
 * Course: CoE 1186 - Software Engineering
 * Group: HashSlinging Slashers
 * Date Created: 10/3/17
 * Date Modified: 11/26/17
 */

package Modules.TrainModel;
import java.awt.Color;
import java.awt.EventQueue;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.CRC32;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import Modules.TrackModel.Block;
import Modules.TrackModel.Station;
import Modules.TrackModel.TrackModel;

/**
 * Class that implements functionality separately for each train that is active on the track
 * Trains become instantiated through the CTC when they are dispatched from the yard.
 * 
 * @author Jennifer
 *
 */
public class Train {
	public final double TRAIN_WEIGHT = 163600; 	// currently in lbs
	public final int TRAIN_CAPACITY = 222; 	// per 1 car
	public final double TRAIN_LENGTH = 32.2; 	// in meters currently
	public final double TRAIN_WIDTH = 2.65; 	// m currently
	public final double TRAIN_HEIGHT = 3.42; 	// m currently
	public final double TRAIN_MAX_ACCELERATION = 0.5; 	// m/s^2
	public final double TRAIN_MAX_ACCELERATION_SERVICE_BRAKE = -1.2; 	// m/s^2
	public final double TRAIN_MAX_ACCELERATION_E_BRAKE = -2.73; 	// m/s^2
	public final double TRAIN_MAX_SPEED = 70; 	// km/h currently
	public final double TRAIN_MAX_GRADE = 60;	// percent
	public final int TRAIN_NUM_WHEELS = 8;
	public final double G = 9.8; 	// m/s^2
	public final double FRICTION_COEFFICIENT = 0.16;
	public final double AVE_PASSENGER_WEIGHT = 142.97; //in lbs
	
	//following variables are for back-end conversions
	//public final double M_PER_SEC_TO_MPH = 2.23694;
	public final double SECONDS_PER_HOUR = 3600;      
    public final double METERS_PER_MILE = 1609.34;    
    public final double FEET_PER_METER = 3.28;         
    public final double KG_PER_POUND = 0.454; 
    public final String DEGREE = "\u00b0";
    
    public final int APPROACHING = 0;
    public final int ARRIVING = 1;
    public final int DEPARTING = 2;
    public final int EN_ROUTE = 3;
    
    // Declaring a variables for the GUI
    public TrainModelGUI trainModelGUI;
    public JFrame trainModelFrame;
    public TrainModel trnMdl;
    public TrackModel trkMdl;
    public ArrayList<Block> track;
    public Position position;
    
    // Declaring variables in order of sections they appear in on the GUI
    
    // Train Specs
    private String trainID;
    private double trainHeight;
    private double trainWeight;
    private double trainLength;
    private double trainWidth;
    private int trainCars;
    private int trainCapacity;
    
    // Track Information
    private int prevBlock;
    private int currentBlock;
    private int nextBlock;
    private double currentX;
    private double currentY;
    private String lineColor;
    private double grade = 0;
    private int currentSpeedLimit;
    private boolean GPSAntenna;
    private boolean MBOAntenna;
    
    // Failure Modes Activation
    private boolean engineFailureActive;
    private boolean signalFailureActive;
    private boolean brakeFailureActive;
    
    // Station Control
    //private Station nextStation;
    private double timeOfArrival;
    //private Station nextStation;
    private int arrivalStatus;
    private int numPassengers;
    private String station;
    private int beacon;
    
    // Speed/Authority
    private double currentSpeed;
    private double CTCSpeed;
    private double CTCAuthority;
    private double powerIn;
    
    // Train Operations
    private boolean leftDoorIsOpen;
    private boolean rightDoorIsOpen;
    private boolean lightsAreOn;
    private int temperature;
    private boolean serviceBrake;
    private boolean emerBrake;
    private boolean driverEmerBrake;
    
    // Other Variables not associated with GUI outputs
    private boolean trainActive;
    private double force;
    private double normalForce;
    private double downwardForce;
    private double totalForce;
    private double friction;
    private double slope;
    private double finalSpeed;
    private double trainAcceleration;
    private double distTravelled;
    private double metersIn;
    private boolean setExit;
    
    public Train(String line, String trainID, TrainModel tm, TrackModel tkmdl) {
    	this.trkMdl = tkmdl;
    	this.lineColor = line;
    	this.track = this.trkMdl.getTrack(this.lineColor);
    	this.position = new Position(track);
    	this.trnMdl = tm;
    	this.trainActive = true;
    	this.trainID = trainID;
    	// Train Specs
    	this.trainCars = 1;
    	this.trainCapacity = TRAIN_CAPACITY * this.trainCars;
        this.trainHeight = TRAIN_HEIGHT;
        this.trainWeight = TRAIN_WEIGHT * this.trainCars;
        this.trainLength = TRAIN_LENGTH * this.trainCars;
        this.trainWidth = TRAIN_WIDTH;
        
        // Track Information
        this.prevBlock = 0;
        this.currentBlock = 0;
        this.nextBlock = 0;
        this.grade = 0;
        //this.currentSpeedLimit = 0;
        this.GPSAntenna = true;
        this.MBOAntenna = false;
        
        // Failure Modes Activation
        this.engineFailureActive = false;
        this.signalFailureActive = false;
        this.brakeFailureActive = false;
        
        // Station Control
        //this.nextStation = new Station();
        this.timeOfArrival = 0;
        this.arrivalStatus = EN_ROUTE;
        this.numPassengers = 0;
        this.beacon = 0;
        //this.station = null;
        
        // Speed/Authority
        this.currentSpeed = 0;
        this.CTCSpeed = 0;
        this.CTCAuthority = 0;
        this.powerIn = 0.0;
        
        // Train Operations
        this.leftDoorIsOpen = false;
        this.rightDoorIsOpen = false;
        this.lightsAreOn  =false;
        this.temperature = 0;
        this.serviceBrake = false;
        this.emerBrake = false;
        
        // Other Variables not associated with GUI outputs
        this.force = 0;
        this.normalForce = 0;
        this.downwardForce = 0;
        this.totalForce = 0;
        this.friction = 0;
        this.slope = 0;
        this.finalSpeed = 0;
        this.trainAcceleration = 0;
        this.metersIn = 0.0;
    	this.setExit = false;
    	this.trainModelGUI = null;
    }
    
    /**
     * Method that creates a new GUI for a train model
     * @return
     */
    public TrainModelGUI CreateNewGUI() {
        //Create a GUI object
    	trainModelGUI = new TrainModelGUI(this);
    	setValuesForDisplay();
    	return trainModelGUI;
    }
    
    /**
     * Returns a train's GUI
     * @return
     */
    public TrainModelGUI getTrainGUI() {
    	return this.trainModelGUI;
    }
    
    /**
     * Displays a trains GUI
     */
    public void showTrainGUI() {
        //Make sure to set it visible
        this.getTrainGUI().setTitle(this.getTrainID());
        this.getTrainGUI().setVisible(true);
    }

    /**
     * Check all values for updates and reflect these updates on the GUI per clock tick
     */
    public void setValuesForDisplay() {
    	this.trainModelGUI.tempLabel.setText(Integer.toString(this.temperature) + DEGREE + "F");
         
        if(this.trainModelGUI.serviceBrakeStatus()) {	// if the brakes were applied (button pressed)
        	this.serviceBrake = true;
        } else {
        	this.serviceBrake = false;
        }
        if(this.trainModelGUI.emerBrakeStatus()) {	// if the e brake was pushed (button pressed)
        	this.emerBrake = true;
        } else {
        	this.emerBrake = false;
        }

        this.trainModelGUI.numCarsSpinner.setText(Integer.toString(this.trainCars));
        this.trainModelGUI.heightVal.setText(Double.toString(truncateTo(this.trainHeight, 2)));
        this.trainModelGUI.widthVal.setText(Double.toString(truncateTo(this.trainWidth, 2)));
        this.trainModelGUI.lengthVal.setText(Double.toString(truncateTo(this.trainLength, 2)));
        this.trainModelGUI.weightVal.setText(Double.toString(truncateTo(this.trainWeight, 2)));
        this.trainModelGUI.capacityVal.setText(Integer.toString(this.trainCapacity));
        this.trainModelGUI.powerVal.setText(Double.toString(this.powerIn));
        
        if(GPSAntenna == true) {
        	this.trainModelGUI.gpsAntennaStatusLabel.setText("ON");
        } else {
        	this.trainModelGUI.gpsAntennaStatusLabel.setText("OFF");
        }
        if(MBOAntenna == true) {
        	this.trainModelGUI.mboAntennaStatusLabel.setText("ON");
        } else {
        	this.trainModelGUI.mboAntennaStatusLabel.setText("OFF");
        }
     	
     	this.trainModelGUI.timeVal.setText(trnMdl.currentTime.toString());
     	this.trainModelGUI.stationVal.setText("Pioneer");
     	
     	if(rightDoorIsOpen == true) {
        	this.trainModelGUI.rightDoorStatusLabel.setText("OPEN");
        } else {
        	this.trainModelGUI.rightDoorStatusLabel.setText("CLOSED");
        }
     	if(leftDoorIsOpen == true) {
        	this.trainModelGUI.leftDoorStatusLabel.setText("OPEN");
        } else {
        	this.trainModelGUI.leftDoorStatusLabel.setText("CLOSED");
        }

     	if(lightsAreOn == true) {
     		this.trainModelGUI.lightStatusLabel.setText("ON");
        } else {
        	this.trainModelGUI.lightStatusLabel.setText("OFF");
        }
     	
     	this.trainModelGUI.numPassengers.setText(Integer.toString(this.numPassengers));
     	this.trainModelGUI.authorityVal.setText(Double.toString(this.CTCAuthority));
     	
     	if(serviceBrake == true) {
     		this.trainModelGUI.serviceLabel.setText("ON");
        } else {
        	this.trainModelGUI.serviceLabel.setText("OFF");
        }
     	if(emerBrake == true) {
     		this.trainModelGUI.emergencyLabel.setText("ON");
        } else {
        	this.trainModelGUI.emergencyLabel.setText("OFF");
        }
     	
     	if(this.arrivalStatus == ARRIVING) {
     		this.trainModelGUI.arrivalStatusLabel.setText("ARRIVING");
     	} else if (this.arrivalStatus == EN_ROUTE) {
     		this.trainModelGUI.arrivalStatusLabel.setText("EN ROUTE");
     	} else {
     		this.trainModelGUI.arrivalStatusLabel.setText("DEPARTING");
     	}
     	this.trainModelGUI.currentSpeedLabel.setText(Double.toString(truncateTo((this.currentSpeed*SECONDS_PER_HOUR/METERS_PER_MILE), 2)));
         
     	if (this.lineColor.equals("GREEN")) {
     		this.trainModelGUI.lblLine.setText(lineColor);
     		this.trainModelGUI.lblLine.setForeground(Color.GREEN);
     	} else {
     		this.trainModelGUI.lblLine.setText("RED");
     		this.trainModelGUI.lblLine.setForeground(Color.RED);
        }
     	
    }
    
    /**
     * Same concecpt behind this method as the setValuesForDisplay() method, except this method is purely calculations
     * surrounding the train's velocity, which is it's core functionality
     */
    public void updateVelocity() {
    	// Step 1: input power and convert the power to a force based on the starting velocity
    	//powerIn = 180;
    	double trainMass = trainWeight*KG_PER_POUND;
    	
    	// this is ensuring that we never get a negative speed
    	if (this.currentSpeed == 0) {
    		this.force = (this.powerIn * 1000)/1;
    	} else {
    		this.force = (this.powerIn * 1000)/this.currentSpeed;
    	}
    	setGrade();
    	// Step 2: Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	this.slope = Math.atan2(this.grade,100);
    	double angle = Math.toDegrees(this.slope);
    	
    	// Step 3: Calculate the forces acting on the train using the coefficient of friction
    	// and the train's weight in lbs converted to kg divided over the wheels (where the force is technically
    	// being applied times gravity (G)
    	this.normalForce = (trainMass/12) * G * Math.sin(angle);	// divide by 12 for the number of wheels
    	this.downwardForce = (trainMass/12) * G * Math.cos(angle);	// divide by 12 for the number of wheels

    	// compute friction force
    	this.friction = (FRICTION_COEFFICIENT * this.downwardForce) + this.normalForce;
    	
    	// sum of the forces
    	this.totalForce = this.force - this.friction;
    	    	
    	this.force = this.totalForce;
    	
    	// Step 4: Calculate acceleration using the F = ma equation, where m = the mass of the body moving
    	this.trainAcceleration = this.force/trainMass;
    	
    	// and have to check to make sure this acceleration does not exceed our max.
    	if (this.trainAcceleration >= TRAIN_MAX_ACCELERATION * 1) {	// time elapsed (one second)
    		// set the acceleration as the max acceleration because we cannot exceed that
    		this.trainAcceleration = TRAIN_MAX_ACCELERATION * 1;	// time elapsed (one second)
    	}
    	
    	// decelerates the train based on the values given in the spec sheet for the emergency brake
    	if (emerBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_E_BRAKE*1);
    	}
    	
    	// decelerates the train based onthe values given in the spec sheet for the service brake
    	if(serviceBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_SERVICE_BRAKE*1);
    	}
    	
    	// Step 5: Calculate the final speed by adding the old speed with the acceleration x the time elapsed (one second)
    	this.finalSpeed = this.currentSpeed + (this.trainAcceleration * 1);
    	
    	// NO NEGATIVE SPEEDS YINZ
    	if(this.finalSpeed < 0) {
            this.finalSpeed = 0;
        }
    	
    	// resetting the current speed based on our calculations
    	this.currentSpeed =  this.finalSpeed;
    	this.distTravelled = this.currentSpeed * 1; // speed times the time between clock ticks = distance travelled
    	//System.out.println(finalSpeed);
    	
    	if(!(currentBlock == this.position.getCurrentBlock())) {
    		metersIn = 0;
    	} else {
    		metersIn += this.distTravelled;
    	}
    	this.position.moveTrain(this.distTravelled); // method call to tell the position class how far to move the train
    	
    }
    
    /**
     * This method truncates any floating point numbers passed to it to the number of decimal points specified
     * by passing in an integer. I found this to be the most intuitive solution for trimming numbers for me personally
     * @param unroundedNumber
     * @param decimalPlaces
     * @return
     */
    static double truncateTo(double unroundedNumber, int decimalPlaces){
        int truncatedNumberInt = (int)( unroundedNumber * Math.pow( 10, decimalPlaces ) );
        double truncatedNumber = (double)( truncatedNumberInt / Math.pow( 10, decimalPlaces ) );
        return truncatedNumber;
    }
    
    /**
     * Returns the train ID of the current train object
     * @return
     */
    public String getTrainID() {
    	return this.trainID;
    }
    
    public void activateEngineFailure() {
    	this.engineFailureActive = true;
    }
    
    public void activateSignalFailure() {
    	// TODO
    	this.setGPSAntenna(false);
    	this.setMBOAntenna(false);
    	this.signalFailureActive = true;
    }
    
    public void activateBrakeFailure() {
    	this.brakeFailureActive = true;
    }
    
    public void endFailureMode() {
    	this.engineFailureActive = false;
    	this.signalFailureActive = false;
    	this.brakeFailureActive = false;
    }
    
    public void setArrivalStatus(int status) {
    	this.arrivalStatus = status;
    }
    
    public void setStation(String station) {
    	this.station = station;
    }
    
    public void setBeacon(int beaconVal) {
    	this.beacon = beaconVal;
    }
    
    public int getBeacon() {
    	return this.beacon;
    }
    
    /**
     * Returns the current x and y coordinates of this train in a double array
     */
    public double[] getCoordinates() {
    	double []coords = this.position.getCoordinates();
    	coords[0] = this.currentX;
    	coords[1] = this.currentY;
    	return coords;
    }
    
    public Position getPosition(){
        return this.position;
    }
    
    public void setPosition(Position pos) {
    	this.position = pos;
    	//setGrade();
    }
    
 // Called by the Train Controller
    public int getBlock(){
        return this.position.getCurrentBlock();
    }
    
    // Called by the MBO
    /*public double[] getCoordinates(){
        
    }*/
    
    /**
     * Startings of a method to compute the checksum of the signal sent to the MBO with the
     * Train's current position as X and Y coordinates
     */
    public long checkSum() {
    	CRC32 crc = new CRC32();
    	crc.reset(); // in order to reuse the object for all signals
    	String signal = this.trainID + ":" + Double.toString(this.currentX) + "," + Double.toString(this.currentY);
    	crc.update(signal.getBytes()); // signal is a String containing your data
    	long checksum = crc.getValue();
    	return checksum;
    }
    
    /**
     * Set the current x and y positions of the current train
     * @param pos
     */
    public void setCoordinates(double[] pos) {
    	this.currentX = pos[0];
    	this.currentY = pos[1];
    }
    
    public String getLine() {
    	return this.lineColor;
    }
    
    /**
     * Sets the grade of the current block/position of the train
     * @param g
     */
    public void setGrade() {
    	this.grade = trkMdl.getBlock(this.lineColor,this.currentBlock).getGrade();
    	//int currentBlockID = position.getCurrentBlock();
    	//Block current = trackModel.getBlock(curentBlockID);
    	//grade = currentBlock.getGrade();
    }
    
    /**
     * Returns the set point speed for this specific train object
     * @return
     */
    public double getSetpoint() {
    	return this.CTCSpeed;
    }
    
    /**
     * Sets the setpoint speed of a specific train when this method is called in the train model after
     * the parent method is called by the track model
     */
    public void setSetpoint(double setpoint) {
    	this.CTCSpeed = setpoint;
    }
    
    /**
     * Called by the train model class when it's parent method is called by the train controller to set a 
     * power input command based on the setpoint speed
     */
    public void setPower(double pow) {
    	this.powerIn = pow;
    }
    
    /**
     * Returns the train's current velocity
     * @return
     */
    public double getVelocity() {
    	return this.currentSpeed;
    }
    
    
    public double getAuthority() {
    	return this.CTCAuthority;
    }
    
    public void setAuthority(double authority) {
    	this.CTCAuthority = authority;
    }
    
    public void setEBrake(boolean ebrake) {
    	this.emerBrake = ebrake;
    	trnMdl.setPassengerEmergencyBrake(this.trainID, ebrake);
    }
    
    public void setServiceBrake(boolean sBrake) {
    	this.serviceBrake = sBrake;
    }
    
    public boolean getEBrake() {
    	return this.emerBrake;
    }
    
    public void setRightDoors(boolean right) {
    	this.rightDoorIsOpen = right;
    }
    
    public void setLeftDoors(boolean left) {
    	this.leftDoorIsOpen = left;
    }
    
    public void setLights(boolean lights) {
    	this.lightsAreOn = lights;
    }
    
    public void setTemp(int temp) {
    	this.temperature = temp;
    }
    
    /**
     * Sets the number of passengers exiting the train using a random number generator
     * This method should only ever be called when train is STOPPED at a station
     * @param numPassengers
     * @return
     */
    public int setNumDeparting() {
    	Random rand = new Random();

    	int  n = rand.nextInt(this.numPassengers);
    	if (this.numPassengers - n >= 0) {
    		this.numPassengers = this.numPassengers - n;
    		return this.numPassengers;
    	}
    	return 0;
    }
    
    public void setMBOAntenna(boolean status) {
    	this.MBOAntenna = status;
    }
    
    public void setGPSAntenna(boolean status) {
    	this.GPSAntenna = status;
    }
    
    public void setExitAllGuis(boolean yes) {
    	if(yes)
    		trnMdl.exitAll();
    }
}
