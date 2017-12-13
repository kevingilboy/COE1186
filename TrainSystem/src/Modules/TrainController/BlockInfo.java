//Michael Kotcher
//Hash Slinging Slashers

package Modules.TrainController;

public class BlockInfo
{
	private double speedLimit;				//m/s
	
	//-1 for only travelling in decreasing block ID's, +1 for only travelling in increasing block ID's, 0 for bidirectional
	private int direction;
	
	private boolean underground;
	
	private String stationName;
	
	//if the train is travelling in a positive direction or on a unidirectional track, the proper door side is stored here
	//true for right doors, and false for left doors
	private boolean positiveDirection;
	
	//if the train is travelling in a negative direction, the proper door side is stored here
	//true for right doors, and false for left doors
	private boolean negativeDirection;
	
	public final double SPEEDCONVERSION = 3.6;			//1 m/s = 3.6 kph
	
	public BlockInfo(double s, boolean u, String str, boolean pd, boolean nd, int d)	//speed limit input is kph
	{
		speedLimit = (s / SPEEDCONVERSION);		//conversion from kilometers per hour to meters per second
		underground = u;
		stationName = str;
		positiveDirection = pd;
		negativeDirection = nd;
		direction = d;
	}
	
	public double getSpeedLimit() {
		return speedLimit;
	}
	
	public boolean isUnderground() {
		return underground;
	}
	
	public String getStationName() {
		return stationName;
	}
	
	public boolean getPositive() {
		return positiveDirection;
	}
	
	public boolean getNegative() {
		return negativeDirection;
	}
	
	public int getDirection() {
		return direction;
	}
}