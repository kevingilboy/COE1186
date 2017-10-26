import java.awt.Color;
import java.awt.EventQueue;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class TrainModel {
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
    
    // Declaring a variables for the GUI
    public TrainModelNewGUI trainModelGUI;
    public JFrame trainModelFrame;
    
    // Declaring variables in order of sections they appear in on the GUI
    
    // Train Specs
    private int trainIDNum;
    private double trainHeight;
    private double trainWeight;
    private double trainLength;
    private double trainWidth;
    private int trainCars;
    private int trainCapacity;
    
    // Track Information
    //private Block currentBlock;
    //private Block nextBlock;
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
    private boolean arrivalStatus;
    private int numPassengers;
    
    // Speed/Authority
    private double currentSpeed;
    private double CTCSpeed;
    private double CTCAuthority;
    private double powerOut;
    private double powerIn;
    
    // Train Operations
    private boolean leftDoorIsOpen;
    private boolean rightDoorIsOpen;
    private boolean lightsAreOn;
    private int temperature;
    private boolean serviceBrake;
    private boolean emerBrake;
    
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
    
    public TrainModel (String line, int trainID) {
    	
    	this.trainActive = true;
    	this.trainIDNum = trainID;
    	// Train Specs
    	this.trainCars = 2;
    	this.trainCapacity = TRAIN_CAPACITY * this.trainCars;
        this.trainHeight = TRAIN_HEIGHT;
        this.trainWeight = TRAIN_WEIGHT * this.trainCars;
        this.trainLength = TRAIN_LENGTH * this.trainCars;
        this.trainWidth = TRAIN_WIDTH;
        
        // Track Information
        //private Block currentBlock;
        //private Block nextBlock;
        this.lineColor = line;
        this.grade = 0;
        this.currentSpeedLimit = 0;
        this.GPSAntenna = false;
        this.MBOAntenna = false;
        
        // Failure Modes Activation
        this.engineFailureActive = false;
        this.signalFailureActive = false;
        this.brakeFailureActive = false;
        
        // Station Control
        //private Station nextStation;
        this.timeOfArrival = 0;
        this.arrivalStatus = false;
        this.numPassengers = 0;
        
        // Speed/Authority
        this.currentSpeed = 0;
        this.CTCSpeed = 0;
        this.CTCAuthority = 100; // 100 miles
        this.powerOut = 0.0;
        this.powerIn = 0.0;
        
        // Train Operations
        this.leftDoorIsOpen = false;
        this.rightDoorIsOpen = false;
        this.lightsAreOn  =false;
        this.temperature = 70;
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
    	
    	this.trainModelGUI = null;
    }
    
    public TrainModelNewGUI CreateNewGUI(TrainModel trainModel) {
        //Create a GUI object
        TrainModelNewGUI trainModelGUI = new TrainModelNewGUI(trainModel);
        
        //Initialize the GUI
        trainModel.setValuesForDisplay(trainModelGUI);
        
        this.trainModelGUI = trainModelGUI;
        return  trainModelGUI;  //Return the GUI object
    }
    
    public void showTrainGUI() {
        //Make sure to set it visible//Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        this.trainModelGUI.setTitle("Train ID " + this.trainIDNum);
        //trainModelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //trainModelFrame.getContentPane().add(this.trainModelGUI);
        //this.trainModelGUI.pack();
        this.trainModelGUI.setVisible(true);     //Make sure to set it visible
    }

    
    public void setValuesForDisplay(TrainModelNewGUI tmGUI) {
    	 tmGUI.tempSpinner.setValue(this.temperature);
         
         this.powerIn = tmGUI.returnPowerInput();
         
         if(tmGUI.serviceBrakeStatus()) {	// if the brakes were applied (button pressed)
        	 this.serviceBrake = true;
         } else {
        	 this.serviceBrake = false;
         }
         if(tmGUI.emerBrakeStatus()) {	// if the e brake was pushed (button pressed)
        	 this.emerBrake = true;
         } else {
        	 this.emerBrake = false;
         }

         tmGUI.numCarsSpinner.setText(Integer.toString(this.trainCars));
         tmGUI.heightVal.setText(Double.toString(truncateTo(this.trainHeight, 2)));
         tmGUI.widthVal.setText(Double.toString(truncateTo(this.trainWidth, 2)));
         tmGUI.lengthVal.setText(Double.toString(truncateTo(this.trainLength, 2)));
         tmGUI.weightVal.setText(Double.toString(truncateTo(this.trainWeight, 2)));
         tmGUI.capacityVal.setText(Integer.toString(this.trainCapacity));
         tmGUI.gradeVal.setText(Double.toString(0.0));
         tmGUI.speedLimitVal.setText("45");
         tmGUI.setpointSpeedLabel.setText(Double.toString(truncateTo((this.currentSpeed*SECONDS_PER_HOUR/METERS_PER_MILE), 2)));
         
         if (this.lineColor.equals("GREEN")) {
        	 tmGUI.lblLine.setText(lineColor);
        	 tmGUI.lblLine.setForeground(Color.GREEN);
         } else {
        	 tmGUI.lblLine.setText("RED");
        	 tmGUI.lblLine.setForeground(Color.RED);
         }
    }
    
    public void updateTrainValues() {
    	// So far the values of the train are all hard coded EXCEPT the velocity and power
    	// Need to add functionality to output a velocity from a power input
    	// Step 1: input power and convert the power to a force based on the starting velocity
    	//powerIn = 180;
    	double trainMass = trainWeight*KG_PER_POUND;
    	
    	if (this.currentSpeed == 0) {
    		this.force = (this.powerIn * 1000)/1;
    	} else {
    		this.force = (this.powerIn * 1000)/this.currentSpeed;
    	}
    	
    	// Step 2: Calculate the slope of the train's current angle (Degrees = Tan-1 (Slope Percent/100))
    	this.slope = Math.atan2(this.grade,100);
    	double angle = Math.toDegrees(this.slope);
    	
    	// Step 3: Calculate the forces acting on the train using the coefficient of friction (TODO: FIND OUT WHAT THAT IS)
    	// and the train's weight in lbs converted to kg divided over the wheels times gravity (G)
    	this.normalForce = (trainMass/12) * G * Math.sin(angle);	// divide by 12 for the number of wheels
    	this.downwardForce = (trainMass/12) * G * Math.cos(angle);	// divide by 12 for the number of wheels
    	
    	// TODO FIGURE OUT WHY THE CURRENT FRICTION COEFFICIENT RETURNS 0 AND 0.01 RETURNS A VALUE
    	this.friction = (FRICTION_COEFFICIENT * this.downwardForce) + this.normalForce;
    	
    	this.totalForce = this.force - this.friction;
    	
    	this.force = this.totalForce;
    	
    	// Step 4: Calculate acceleration using the F = ma equation, where m = the mass of the body moving
    	this.trainAcceleration = this.force/trainMass;
    	
    	// and have to check to make sure this acceleration does not exceed our max.
    	if (this.trainAcceleration >= TRAIN_MAX_ACCELERATION * 1) {	// time elapsed (one second)
    		// set the acceleration as the max acceleration because we cannot exceed that
    		this.trainAcceleration = TRAIN_MAX_ACCELERATION * 1;	// time elapsed (one second)
    	}
    	
    	if (emerBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_E_BRAKE*1);
    	}
    	if(serviceBrake) {
    		this.trainAcceleration = (TRAIN_MAX_ACCELERATION_SERVICE_BRAKE*1);
    	}
    	
    	// Step 5: Calculate the final speed by adding the old speed with the acceleration x the time elapsed (one second)
    	this.finalSpeed = this.currentSpeed + (this.trainAcceleration * 1);
    	
    	if(this.finalSpeed < 0) {
            this.finalSpeed = 0;
        }
    	
    	// resetting the current speed based on our calculations
    	this.currentSpeed =  this.finalSpeed;
    	//System.out.println(finalSpeed);
    	
    	
    }
    
    static double truncateTo( double unroundedNumber, int decimalPlaces ){
        int truncatedNumberInt = (int)( unroundedNumber * Math.pow( 10, decimalPlaces ) );
        double truncatedNumber = (double)( truncatedNumberInt / Math.pow( 10, decimalPlaces ) );
        return truncatedNumber;
    }
}
