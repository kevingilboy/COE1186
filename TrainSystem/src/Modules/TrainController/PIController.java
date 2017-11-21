//Michael Kotcher
//This is a Java translation of the MiniPID open source controller
//https://github.com/tekdemo/MiniPID

package Modules.TrainController;

public class PIController {
	private double P;
	private double I;
	//private double D;					//set D and F to zero to do nothing
	//private double F;			

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
	
	public PIController(double p, double i) {
		init();
		P = p;
		I = i;
	}
	
	/*public PIController(double p, double i, double d) {
		init();
		P = p;
		I = i;
		D = d;
	}
	
	public PIController(double p, double i, double d, double f) {
		init();
		P = p;
		I = i;
		D = d;
		F = f;
	}
	Shouldn't be used because this is just going to be a PI controller*/
	
	private void init() {
		P = 0;
		I = 0;
		//D = 0;
		//F = 0;

		maxIOutput = 0;
		maxError = 0;
		errorSum = 0;
		maxOutput = 0; 
		minOutput = 0;
		setpoint = 0;
		lastActual = 0;
		firstRun = true;
		reversed = false;
		outputRampRate = 0;
		lastOutput = 0;
		outputFilter = 0.1;
		setpointRange = 0;
	}
	
	public void setP(double p) {
		P = p;
		checkSigns();
	}
	
	public void setI(double i) {
		if (I != 0) {
			errorSum = errorSum * I / i;
		}
		if (maxIOutput != 0) {
			maxError = maxIOutput / i;
		}
		I = i;
		checkSigns();
	}
	
	public double getP() {
		return P;
	}
	
	public double getI() {
		return I;
	}
	
	public void setMaxIOutput(double maximum) {		//Not used
		/* Internally maxError and Izone are similar, but scaled for different purposes. 
		 * The maxError is generated for simplifying math, since calculations against 
		 * the max error are far more common than changing the I term or Izone. 
		 */
		maxIOutput = maximum;
		if (I != 0) {
			maxError = maxIOutput / I;
		}
	}
	
	public void setOutputLimits(double minimum,double maximum){		//Limits are 0 and 820 kW
		if (maximum < minimum) {
			return;
		}
		maxOutput = maximum;
		minOutput = minimum;

		// Ensure the bounds of the I term are within the bounds of the allowable output swing
		if (maxIOutput == 0 || maxIOutput > (maximum - minimum)) {
			setMaxIOutput(maximum - minimum);
		}
	}
	
	/*public void setDirection(bool reversed) {		//Not used
		this.reversed = reversed;
	}*/
	
	public void setSetpoint(double setpoint) {
		this.setpoint = setpoint;
	}
	
	public double getOutput(double actual, double setpoint) {
		double output;
		double Poutput;
		double Ioutput;
		//double Doutput;
		//double Foutput;

		this.setpoint = setpoint;

		//Ramp the setpoint used for calculations if user has opted to do so
		if(setpointRange != 0) {
			setpoint = clamp(setpoint, actual - setpointRange, actual + setpointRange);
		}

		//Do the simple parts of the calculations
		double error = setpoint - actual;

		//Calculate F output. Notice, this->depends only on the setpoint, and not the error. 
		//Foutput = F * setpoint;

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
	
	public double getOutput() {
		return getOutput(lastActual, setpoint);
	}
	
	public double getOutput(double actual) {
		return getOutput(actual, setpoint);
	}
	
	public void reset() {		//Probably not used
		firstRun = true;
		errorSum = 0;
	}
	
	/*public void setOutputRampRate(double rate) {		//Not used
		outputRampRate = rate;
	}*/
	
	public void setSetpointRange(double range) {		//Not used, unless I can get have speed limit
		setpointRange = range;
	}
	
	/*public void setOutputFilter(double strength) {		//Not used
		if (strength == 0 || bounded(strength, 0, 1)) {
			outputFilter = strength;
		}
	}*/
	
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
		if (reversed) {	//all values should be below zero
			if (P > 0)
				P *= -1;
			if (I > 0)
				I *= -1;
		}
		else {	//all values should be above zero
			if(P < 0)
				P *= -1;
			if(I < 0)
				I *= -1;
		}
	}
}