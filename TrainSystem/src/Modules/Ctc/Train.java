//Kevin Gilboy
//This object is a train and holds all relevant metadata

package Modules.Ctc;

import Shared.SimTime;

public class Train {
	public String name;
	public Line line;
	public Schedule schedule;
	
	public SimTime arrivalAtCurrLocation;
	public int currLocation;
	public int prevLocation;
	public double suggestedSpeed;
	public double authority;
	public int passengers;
	
	public boolean dwelling;
	public SimTime timeToFinishDwelling;
	
	public boolean manualSpeedMode;
	public double manualSpeed;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
		this.name = schedule.name;
		this.line = schedule.line;
		this.passengers = 0;
		
		//Set up locations for the iterator
		prevLocation = -1;
		currLocation = line.yardOut;
		
		//Not dwelling
		dwelling = false;
		
		//Not manual mode
		manualSpeedMode = false;
		manualSpeed = -1;
		
		arrivalAtCurrLocation = new SimTime(23,59,59);
	}
	
}
