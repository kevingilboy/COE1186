package Modules.TrackModel;

public class Block{

	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean railStatus = STATUS_WORKING;
	private boolean powerStatus = STATUS_WORKING;
	private boolean trackCircuitStatus = STATUS_WORKING;

	private boolean occupied;

	private String line;
	private String section;
	private int id;
	private double length;
	private double grade;
	private double elevation;
	private double cumElevation;
	private int speedLimit;
	private int direction;
	private Light light;
	private Crossing crossing;
	private Station station;
	private Switch switch_;
	private Beacon beacon;
	private boolean underground;

	public Block(){
		this(	"", "", 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, false, false);
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
					boolean occupied	){
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

	public void setOccupancy(boolean occupancy){
		this.occupied = occupancy;
	}
}