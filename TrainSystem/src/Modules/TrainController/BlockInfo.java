public class BlockInfo
{
	private double speedLimit;
	private int direction;
	private boolean underground;
	private String stationName;
	private boolean positiveDirection;
	private boolean negativeDirection;
	
	public BlockInfo(double s, boolean u, String str, boolean pd, boolean nd, int d)
	{
		speedLimit = s;
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