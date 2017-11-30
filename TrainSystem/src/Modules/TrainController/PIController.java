//Michael Kotcher
//This is a Java translation of the MiniPID open source controller
//shortened to include only the necessary pieces
//https://github.com/tekdemo/MiniPID

//I BELIEVE that the output is in Watts

package Modules.TrainController;

public class PIController {
	private double P;
	private double I;	

	private double maxIOutput;			//set to zero to do nothing
	private double maxError;
	private double errorSum;

	private double maxOutput; 
	private double minOutput;

	private double setpoint;

	private double lastActual;

	private boolean firstRun;
	private boolean reversed;

	private double outputRampRate;		//set to zero to do nothing
	private double lastOutput;

	private double outputFilter;		//set to zero to do nothing

	private double setpointRange;		//set to zero to do nothing
	
	/*public static void main(String[] args) {
		new PIController(2000, 0.8071);
	}*/
	
	
	
	public PIController(double p, double i) {
		P = p;
		I = i;
		init();
	}
	
	private void init() {
		maxIOutput = 0;
		maxError = 0;
		errorSum = 0;
		maxOutput = 820000; 	//820 kW - may be for just one car in the train, LOOK INTO THIS MORE
		minOutput = 0;
		setpoint = 0;
		lastActual = 0;
		firstRun = true;
		reversed = false;
		outputRampRate = 0;
		lastOutput = 0;
		outputFilter = 0.1;
		setpointRange = 0;
		//System.out.println(getOutput(40, 45));
	}
	
	public double getOutput(double actual, double setpoint) {
		double output;
		double Poutput;
		double Ioutput;

		this.setpoint = setpoint;

		//Ramp the setpoint used for calculations if user has opted to do so
		if(setpointRange != 0) {
			setpoint = clamp(setpoint, actual - setpointRange, actual + setpointRange);
		}

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

		//Calculate D Term
		//Note, this->is negative. this->actually "slows" the system if it's doing
		//the correct thing, and small values helps prevent output spikes and overshoot 

		//Doutput = -1 * D * (actual - lastActual);
		lastActual = actual;



		//The Iterm is more complex. There's several things to factor in to make it easier to deal with.
		// 1. maxIoutput restricts the amount of output contributed by the Iterm.
		// 2. prevent windup by not increasing errorSum if we're already running against our max Ioutput
		// 3. prevent windup by not increasing errorSum if output is output=maxOutput	
		Ioutput = I * errorSum;
		if(maxIOutput != 0) {
			Ioutput = clamp(Ioutput, -maxIOutput, maxIOutput); 
		}	

		//And, finally, we can just add the terms up
		//output = Foutput + Poutput + Ioutput + Doutput;
		output = Poutput + Ioutput;

		//Figure out what we're doing with the error.
		if (minOutput != maxOutput && !bounded(output, minOutput, maxOutput)) {
			errorSum = error;
			// reset the error sum to a sane level
			// Setting to current error ensures a smooth transition when the P term 
			// decreases enough for the I term to start acting upon the controller
			// From that point the I term will build up as would be expected
		}
		else if(outputRampRate != 0 && !bounded(output, lastOutput - outputRampRate, lastOutput+outputRampRate)) {
			errorSum = error; 
		}
		else if(maxIOutput != 0) {
			errorSum = clamp(errorSum + error, -maxError, maxError);
			// In addition to output limiting directly, we also want to prevent I term 
			// buildup, so restrict the error directly
		}
		else {
			errorSum += error;
		}

		//Restrict output to our specified output and ramp limits
		if (outputRampRate != 0) {
			output = clamp(output, lastOutput - outputRampRate, lastOutput + outputRampRate);
		}
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
		if (reversed) {				//all values should be below zero
			if (P > 0)
				P *= -1;
			if (I > 0)
				I *= -1;
		}
		else {						//all values should be above zero
			if(P < 0)
				P *= -1;
			if(I < 0)
				I *= -1;
		}
	}
}