package Modules.Ctc;

import Modules.TrackModel.Block;
import Modules.TrackModel.TrackIterator;

public class Train {
	public String name;
	public Line line;
	public int currLocation;
	public int prevLocation;
	public int speed;
	public int authority;
	public Schedule schedule;
	public int passengers;
	
	public TrackIterator ti;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
		this.name = schedule.name;
		this.line = schedule.line;
		this.passengers = 0;
		
		//Set up locations for the iterator
		prevLocation = -1;
		if(line==Line.RED) {
			currLocation = 76;
		}
		else if(line == Line.GREEN) {
			currLocation = 151;
		}
		
		ti = new TrackIterator(line.blocksAL,currLocation,prevLocation);
	}

}
