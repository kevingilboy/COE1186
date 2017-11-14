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
		departureTime = new SimTime("0845");
		stops.add(new Stop("Shadyside","08:47:00","08:49:00"));
		stops.add(new Stop("Herron Ave","08:53:00","08:55:00"));
		stops.add(new Stop("Swissvale","09:01:00","09:02:00"));
	}

	public Object[][] toStringArray() {
		Object[][] grid = new Object[stops.size()][3];
		for(int i=0;i<stops.size();i++) {
			Stop stop = stops.get(i);
			grid[i][0] = stop.block;
			grid[i][1] = stop.arrivalTime.toString();
			grid[i][2] = stop.departureTime.toString();
		}
		return grid;
	}
}
