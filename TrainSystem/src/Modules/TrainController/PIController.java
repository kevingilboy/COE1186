//Michael Kotcher
//This is a Java translation of the MiniPID open source controller
//shortened to include only the necessary pieces
//https://github.com/tekdemo/MiniPID

package Modules.TrainController;

public class PIController {
	private double P;
	private double I;	

	private double maxError;
	
	private double errorSum;

	private double maxOutput; 
	
	private double minOutput;

	private double setpoint;

	private double lastActual;

	private boolean firstRun;

	private double lastOutput;

	private double outputFilter;		//used to prevent output spikes
	
	public PIController(double p, double i) {
		P = p;
		I = i;
		init();
	}
	
	private void init() {
		maxError = 0;
		errorSum = 0;
		maxOutput = 120000;		//120 kW from the spec sheet
		minOutput = 0;
		setpoint = 0;
		lastActual = 0;
		firstRun = true;
		lastOutput = 0;
		outputFilter = 0.1;		//set to a value by the open source's code documentation
	}
	
	public double getOutput(double actual, double setpoint) {
		double output;
		double Poutput;
		double Ioutput;

		this.setpoint = setpoint;

		//Do the simple parts of the calculations
		double error = setpoint - actual;

		//Calculate P term
		Poutput = P * error;	 

		//If this->is our first time running this-> we don't actually _have_ a previous input or output. 
		//For sensor, sanely assume it was exactly where it is now.
		//For last output, we can assume it's the current time-independent outputs. 
		if (firstRun) {
			lastActual = actual;
			lastOutput = Poutput;//+Foutput;
			firstRun = false;
		}

		lastActual = actual;

		//The Iterm is more complex. There's several things to factor in to make it easier to deal with.
		// 1. maxIoutput restricts the amount of output contributed by the Iterm.
		// 2. prevent windup by not increasing errorSum if we're already running against our max Ioutput
		// 3. prevent windup by not increasing errorSum if output is output=maxOutput	
		Ioutput = I * errorSum;

		//And, finally, we can just add the terms up
		output = Poutput + Ioutput;

		//Figure out what we're doing with the error.
		if (minOutput != maxOutput && !bounded(output, minOutput, maxOutput)) {
			errorSum = error;
			// reset the error sum to a sane level
			// Setting to current error ensures a smooth transition when the P term 
			// decreases enough for the I term to start acting upon the controller
			// From that point the I term will build up as would be expected
		}
		else {
			errorSum += error;
		}

		//Restrict output to our specified output and ramp limits
		if (minOutput != maxOutput) { 
			output = clamp(output, minOutput, maxOutput);
		}
		if (outputFilter != 0) {
			output = lastOutput * outputFilter + output * (1 - outputFilter);
		}

		lastOutput = output;
		return output;
	}
	
	private double clamp(double value, double min, double max) {
		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}
		return value;
	}
	
	private boolean bounded(double value, double min, double max) {
		return (min < value) && (value < max);
	}
	
	private void checkSigns() {					
		if(P < 0)				//all values should be above zero
			P *= -1;
		if(I < 0)
			I *= -1;
	}
}