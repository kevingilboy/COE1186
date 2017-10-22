
public class Stop {
	public String block;
	public SimTime arrivalTime;
	public SimTime departureTime;
	
	public Stop(String blk,String arrival,String departure) {
		block = blk;
		arrivalTime = new SimTime(arrival);
		departureTime = new SimTime(departure);
	}
}
