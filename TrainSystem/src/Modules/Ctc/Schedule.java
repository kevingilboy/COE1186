package Modules.Ctc;

import Shared.SimTime;

import Modules.TrackModel.TrackIterator;

import java.util.ArrayList;
import java.util.Arrays;

import Modules.TrackModel.Block;

public class Schedule {
	public Line line;
	public ArrayList<Stop> stops;
	
	public SimTime departureTime;
	
	public String name;

	
	public Schedule(Line line) {
		this.line = line;
		stops = new ArrayList<Stop>();
	}
	
	public void addStop(int index, Block block) {
		//If stop does not exist, then add it. Else replace the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(block));
		}
		else {
			stops.set(index,new Stop(block));
		}
		calculateDwellTimes();
	}
	
	public void removeStop(int index) {
		stops.remove(index);
		calculateDwellTimes();
	}


	public void setName(String name) {
		this.name = name;	
	}

	public void setDepartureTime(SimTime time) {
		this.departureTime = time;
		calculateDwellTimes();
	}
	
	private void calculateDwellTimes() {
		int currBlockId = line.yardOutNext;
		int prevBlockId = -1;
		
		TrackIterator ti;
		
		for(Stop stop : stops) {
			double runningTime = 0;
			while(stop.block.getId() != currBlockId) {				
				runningTime += line.blocks[currBlockId].getSpeedLimit()/1000.0 * line.blocks[currBlockId].getLength();
				
				ti = new TrackIterator(line.blocksAL, currBlockId, prevBlockId);
				prevBlockId = currBlockId;
				currBlockId = ti.nextBlock();
			}
			int hr = (int)runningTime/3600;
			int min = (int)(runningTime%3600)/60;
			int sec = (int)runningTime%60;
			stop.timeToDwell = new SimTime(hr,min,sec);
		}
	}
	
	public Object[][] toStringArray() {
		Object[][] grid = new Object[stops.size()][3];
		for(int i=0;i<stops.size();i++) {
			Stop stop = stops.get(i);
			grid[i][0] = stop.block.getId();
			grid[i][1] = stop.timeToDwell.toString();
		}
		return grid;
	}
}
