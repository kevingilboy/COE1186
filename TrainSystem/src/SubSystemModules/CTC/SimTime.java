
public class SimTime {
	public int hr;
	public int min;
	
	public SimTime(int hours, int minutes) {
		hr = hours;
		min = minutes;
	}
	
	public SimTime(String time) {
		if(time.length()==4) {
			hr = Integer.parseInt(time.substring(0,2));
			min = Integer.parseInt(time.substring(2,4));
		}
		else if(time.length()==3) {
			hr = Integer.parseInt(time.substring(0,1));
			min = Integer.parseInt(time.substring(1,3));
		}
		//else {
		//	throw new Exception();
		//}
	}
	
	public String toString() {
		return(String.format("%02d",hr)+":"+String.format("%02d",min));
	}
}
