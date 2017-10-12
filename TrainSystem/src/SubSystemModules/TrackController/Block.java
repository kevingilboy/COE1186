import java.awt.*;

public class Block{

	/*
	 	greenline.csv format:
	 	[0][line]
	   	[1][section]
	   	[2][id]
	   	[3][length]
	   	[4][grade]
	   	[5][speedLimit]
	 	[6][station-doorOpenSide]
	 	[7][switchBlock] - T/F
	 	[8][underground]
	 	[9][elevation]
	 	[10][cumElevation]
	 	[11][switchID] - use to find blocks?
	 	[12][partOfSection: head/tail/none]
	 	[13][direction]
	 	[14][crossing] - T/F
	 	[15][switchType] - ??
		[16][status]
		[17][occupancy]
	 */

	public String 	line; 			// [0]
	public String 	section;		// [1]
	public int 		id;				// [2]
	public double 	length;			// [3]
	public double 	grade;			// [4]
	public int 		speedLimit;		// [5]
	public String 	station;		// [6]
	public String   switchBlock;	// [7]
	public String   underground;    // [8]
	public double 	elevation;		// [9]
	public double 	cumElevation;	// [10]
	public String 	switchID; 		// [11]
	public String 	partOfSection;  // [12]
	public int      direction;		// [13]
	public String   crossing;		// [14]
	public String 	switchType;		// [15]
	public String	status;			// [16]
	public boolean	occupancy;		// [17]

	public Block(){
		this("", "", 0, 0.0, 0.0, 0, "", "", "", 0.0, 0.0, "", "", 0, "", "", "", false);
	}

	public Block(	String 	line,
					String 	section,
					int 	id,
					double 	length,
					double 	grade,
					int 	speedLimit,
					String 	station,
					String 	switchBlock,
					String 	underground,
					double 	elevation,
					double 	cumElevation,
					String 	switchID,
					String 	partOfSection,
					int 	direction,
					String  crossing,
					String 	switchType,
					String 	status,
					boolean occupancy	){

		this.line 			= line;
		this.section 		= section;
		this.length 		= length;
		this.grade 			= grade;
		this.speedLimit 	= speedLimit;
		this.station 		= station;
		this.switchBlock	= switchBlock;
		this.underground	= underground;
		this.elevation 		= elevation;
		this.cumElevation 	= cumElevation;
		this.switchID		= switchID;
		this.partOfSection  = partOfSection;
		this.direction 		= direction;
		this.crossing 		= crossing;
		this.switchType		= switchType;
		this.status			= status;
		this.occupancy		= occupancy;
	}
}