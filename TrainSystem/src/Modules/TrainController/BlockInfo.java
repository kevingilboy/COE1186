public class BlockInfo
{
	private double speedLimit;
	private boolean underground;
	private String stationName;
	private boolean positiveDirection;
	private boolean negativeDirection;
	
	public BlockInfo(double s, String str, boolean u, boolean pd, boolean nd)
	{
		speedLimit = s;
		underground = u;
		stationName = str;
		positiveDirection = pd;
		negativeDirection = nd;
	}
}