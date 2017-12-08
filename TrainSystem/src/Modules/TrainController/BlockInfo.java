//Michael Kotcher

package Modules.TrainController;

public class BlockInfo
{
	private double speedLimit;		//m/s
	private int direction;
	private boolean underground;
	private String stationName;
	private boolean positiveDirection;
	private boolean negativeDirection;
	
	public final double SPEEDCONVERSION = 3.6;			//1 m/s = 3.6 kph
	
	public BlockInfo(double s, boolean u, String str, boolean pd, boolean nd, int d)	//speed limit input is kph
	{
		speedLimit = (s / SPEEDCONVERSION);
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