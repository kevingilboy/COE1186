package Modules.TrackModel;

public class Block{

	public static boolean STATUS_WORKING = true;
	public static boolean STATUS_NOT_WORKING = false;
	public static boolean STATUS_MAINTENANCE = STATUS_NOT_WORKING;
	public static boolean STATUS_REPAIRED = STATUS_WORKING;
	public static int DIRECTION_INCREASING_ID = 1;
	public static int DIRECTION_DECREASING_ID = -1;

	private boolean railStatus = STATUS_WORKING;
	private boolean powerStatus = STATUS_WORKING;
	private boolean trackCircuitStatus = STATUS_WORKING;

	private boolean occupied;
	private boolean reserved;
	
	private String line; // [0]
	private String section; // [1]	
	private int id; // [2]
	private double length; // [3]
	private double grade; // [4]
	private double elevation; // [5]
	private double cumElevation; // [6] 
	private int speedLimit; // [7]
	private int direction; // [8]
	private Light light; // [9]
	private Crossing crossing; // [10]
	private Station station; // [11]
	private Switch switch_; // [12]
	private Beacon beacon; // [13]
	private boolean underground; // [14]

	private double[] x_coordinates; // [15]
	private double[] y_coordinates; 

	public Block(){
		this("", "", 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, false, false, null, null);
	}

	public Block(	String line,
					String section,
					int id,
					double length,
					double grade,
					double elevation,
					double cumElevation,
					int speedLimit,
					int direction,
					Light light,
					Crossing crossing,
					Station station,
					Switch switch_,
					Beacon beacon,
					boolean underground,	
					boolean occupied,	
					double[] x_coordinates,
					double[] y_coordinates	){
	
		this.line = line;
		this.section = section;
		this.id = id;
		this.length = length;
		this.grade = grade;
		this.elevation = elevation;
		this.cumElevation = cumElevation;
		this.speedLimit = speedLimit;
		this.direction = direction;
		this.light = light;
		this.crossing = crossing;
		this.station = station;
		this.switch_ = switch_;
		this.beacon = beacon;
		this.underground = underground;
		this.occupied = occupied;
		this.x_coordinates = x_coordinates;
		this.y_coordinates = y_coordinates;
		this.reserved = false;
	}

	public String getLine(){
		return line;
	}

	public String getSection(){
		return section;
	}

	public int getId(){
		return id;
	}

	public double getLength(){
		return length;
	}

	public double getGrade(){
		return grade;
	}

	public double getElevation(){
		return elevation;
	}

	public double getCumElevation(){
		return cumElevation;
	}

	public int getSpeedLimit(){
		return speedLimit;
	}

	public int getDirection(){
		return direction;
	}

	public Light getLight(){
		return light;
	}

	public Crossing getCrossing(){
		return crossing;
	}

	public Station getStation(){
		return station;
	}

	public Switch getSwitch(){
		return switch_;
	}

	public Beacon getBeacon(){
		return beacon;
	}

	public boolean getUndergroundStatus(){
		return underground;
	}

	public boolean getOccupied(){
		return occupied;
	}
	
	public boolean getReserved(){
		return reserved;
	}

	public void setRailStatus(boolean status){
		railStatus = status;
		setOccupancy(!status);
	}

	public void setPowerStatus(boolean status){
		powerStatus = status;
		setOccupancy(!status);
	}

	public void setTrackCircuitStatus(boolean status){
		trackCircuitStatus = status;
		setOccupancy(!status);
	}

	public double[] getXCoordinates(){
		return x_coordinates;
	}

	public double[] getYCoordinates(){
		return y_coordinates;
	}

	public double[] getCoordinatesAtMeter(int m){
		double[] coordinates = new double[2];
		coordinates[0] = x_coordinates[m];
		coordinates[1] = y_coordinates[m];
		return coordinates;
	}

	public void setMaintenance(boolean status){
		if (!status == STATUS_MAINTENANCE){
			railStatus = STATUS_MAINTENANCE;
			powerStatus = STATUS_MAINTENANCE;
			trackCircuitStatus = STATUS_MAINTENANCE;
			setOccupancy(true);
		} else {
			railStatus = STATUS_REPAIRED;
			powerStatus = STATUS_REPAIRED;
			trackCircuitStatus = STATUS_REPAIRED;
			setOccupancy(false);
		}
	}

	public boolean getRailStatus(){
		return railStatus;
	}

	public boolean getPowerStatus(){
		return powerStatus;
	}

	public boolean getTrackCircuitStatus(){
		return trackCircuitStatus;
	}

	public boolean getStatus(){
		return (railStatus && powerStatus && trackCircuitStatus);
	}

	public void setOccupancy(boolean occupancy){
		this.occupied = occupancy;
	}
	
	public void setReserved(boolean reserved){
		this.reserved = reserved;
	}

	public String toString(){
		String sectionStr = getSection();
		String idStr = Integer.toString(getId() + 1); // (+1) for displaying values
													  // zero-index from parsing
		String stationStr = "";

		if (getStation() != null){
			stationStr = " (" +  getStation().getId() + ")";
		}

		return (sectionStr + idStr + stationStr);
	}
}
