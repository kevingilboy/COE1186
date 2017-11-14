package Modules.Ctc;

import Shared.SimTime;

import java.util.ArrayList;

public class Schedule {
	public ArrayList<Stop> stops;
	public SimTime departureTime;
	public Line line;
	public String name;
	public boolean dispatched;
	
	public Schedule() {
		/*
		 * Hardcode some stops for now
		 */
		stops = new ArrayList<Stop>();
		
		dispatched = false;
		line = Line.GREEN;
		departureTime = new SimTime("08:45:00");
		//stops.add(new Stop(Line.GREEN.blocks[3],new SimTime("08:47:00")));
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

	public void setName(String name) {
		this.name = name;	
	}

	public void setDepartureTime(SimTime time) {
		this.departureTime = time;
		//TODO recalculate dwells
	}
}
