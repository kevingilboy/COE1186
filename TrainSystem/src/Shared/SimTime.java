package Shared;

public class SimTime {
	public int hr;
	public int min;
	public int sec;
	
	public SimTime(int hours, int minutes) {
		hr = hours;
		min = minutes;
		sec = 0;
	}
	
	public SimTime(String time) {
		if(time.length()==4) {
			hr = Integer.parseInt(time.substring(0,2));
			min = Integer.parseInt(time.substring(2,4));
			sec = 0;
		}
		else if(time.length()==3) {
			hr = Integer.parseInt(time.substring(0,1));
			min = Integer.parseInt(time.substring(1,3));
			sec = 0;
		}
	}
	
	public void incrementSecond() {
		sec++;
		if(sec==60) {
			sec = 0;
			min++;
			if(min==60) {
				min = 0;
				hr++;
				if(hr==24) {
					hr = 0;
				}
			}
		}
	}
	
	
	public String toString() {
		return(String.format("%02d",hr)+":"+String.format("%02d",min)+":"+String.format("%02d",sec));
	}
}