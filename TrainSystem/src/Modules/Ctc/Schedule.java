package Modules.Ctc;

import Shared.SimTime;

import Modules.TrackModel.TrackIterator;

import java.util.ArrayList;

public class Schedule {
	public Line line;
	public ArrayList<Stop> stops;
	public int nextStopIndex;
	
	public SimTime departureTime;
	
	public String name;

	
	public Schedule(Line line) {
		this.line = line;
		nextStopIndex = 0;
		stops = new ArrayList<Stop>();
	}
	
	public void addStop(int index, int blockId) {
		//If stop does not exist, then add it. Else replace the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(blockId));
		}
		else {
			stops.set(index,new Stop(blockId));
		}
		calculateDwellTimes();
	}
	
	public int getNextStop() {
		return stops.get(nextStopIndex).blockId;
	}
	
	public void removeStop(int index) {
		stops.remove(index);
		
		//Recalc dwell times in case a stop is removed from the middle of the schedule
		calculateDwellTimes();
	}


	public void setName(String name) {
		this.name = name;	
	}

	public void setDepartureTime(SimTime time) {
		this.departureTime = time;
	}
	
	/**
	 * This function calculates time between stops
	 */
	private void calculateDwellTimes() {
		int currBlockId = line.yardOut;
		int prevBlockId = -1;
		
		TrackIterator ti;
		
		//Cycle through each stop
		for(Stop stop : stops) {
			//Store the cumulative travel time in runningTime
			double runningTime = 0;
			
			//Loop until the stop is reached by the iterator
			while(stop.blockId != currBlockId) {	
				runningTime += line.blocks[currBlockId].getSpeedLimit()/1000.0 * line.blocks[currBlockId].getLength();
				
				ti = new TrackIterator(line.blocksAL, currBlockId, prevBlockId);
				prevBlockId = currBlockId;
				currBlockId = ti.nextBlock();
			}
			
			//Set the dwell time
			int hr = (int)runningTime/3600;
			int min = (int)(runningTime%3600)/60;
			int sec = (int)runningTime%60;
			stop.timeToDwell = new SimTime(hr,min,sec);
		}
	}
	
	/**
	 * Convert the schedule to a 2D object array for JTable viewing
	 */
	public Object[][] toStringArray() {
		Object[][] grid = new Object[stops.size()][3];
		for(int i=0;i<stops.size();i++) {
			Stop stop = stops.get(i);
			grid[i][0] = line.blocks[stop.blockId+1].toString();
			grid[i][1] = stop.timeToDwell.toString();
		}
		return grid;
	}
}
