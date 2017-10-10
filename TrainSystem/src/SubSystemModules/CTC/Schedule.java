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
		line = "Red";
		name = "Train1";
		authority = 100;
		
		departureTime = new SimTime("0900");
		stops.add(new Stop("Cathy","0900","0902"));
		stops.add(new Stop("Schenley","0908","0910"));
		stops.add(new Stop("Suthy","0915","0917"));
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
