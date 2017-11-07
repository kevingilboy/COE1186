package Shared;

public class SimTime {
	public int hr;
	public int min;
	public int sec;
	
	public SimTime(int hours, int minutes, int seconds) {
		hr = hours;
		min = minutes;
		sec = seconds;
	}
	
	public SimTime(SimTime time) {
		hr = time.hr;
		min = time.min;
		sec = time.sec;
	}
	
	public SimTime(String time) {
		if(time.length()==8) {
			String[] parts = time.split(":");
			if(parts.length==3) {
				hr =  Integer.parseInt(parts[0]);
				min = Integer.parseInt(parts[1]);
				sec = Integer.parseInt(parts[2]);
			}
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
	
	public boolean equals(SimTime x) {
		if (hr == x.hr && min == x.min && sec == x.sec) {
			return true;
		}
		return false;
	}
	
	public static double hoursBetween(SimTime a, SimTime b) {
		double aTimeInHr = a.hr + a.min/60.0 + a.sec/3600.0;
		double bTimeInHr = b.hr + b.min/60.0 + b.sec/3600.0;
		
		double hoursBetween = bTimeInHr-aTimeInHr;
		
		return hoursBetween;
	}
	
	public String toString() {
		return(String.format("%02d",hr)+":"+String.format("%02d",min)+":"+String.format("%02d",sec));
	}
}