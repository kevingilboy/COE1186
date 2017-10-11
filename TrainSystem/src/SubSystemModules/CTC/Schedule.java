import java.util.ArrayList;

public class Schedule {
	public ArrayList<Stop> stops;
	public SimTime departureTime;
	public String line;
	public String name;
	public int authority;
	
	public Schedule() {
		/*
		 * Hardcode some stops for now
		 */
		stops = new ArrayList<Stop>();
		
		departureTime = new SimTime("0845");
		stops.add(new Stop("Shadyside","0847","0849"));
		stops.add(new Stop("Herron Ave","0853","0855"));
		stops.add(new Stop("Swissvale","0901","0902"));
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
