package Modules.Ctc;

import Shared.SimTime;

import java.util.ArrayList;

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
		if(stops.get(index)==null) {
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
		//TODO replace hardcoded dwell times
		for(Stop stop : stops) {
			stop.timeToDwell = new SimTime(00,06,00);
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
