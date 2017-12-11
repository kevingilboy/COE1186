package Shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimTime {
	public int hr;
	public int min;
	public int sec;
	public int ms;
	
    static Pattern checkTwoDigit = Pattern.compile("^[0-9]{2}:[0-9]{2}:[0-9]{2}$");
	
	public SimTime(int hours, int minutes, int seconds) {
		hr = hours;
		min = minutes;
		sec = seconds;
		ms = 0;
	}
	
	public SimTime(SimTime time) {
		hr = time.hr;
		min = time.min;
		sec = time.sec;
		ms = time.ms;
	}
	
	public SimTime(String time) {
		if(time.length()==8) {
			String[] parts = time.split(":");
			if(parts.length==3) {
				hr =  Integer.parseInt(parts[0]);
				min = Integer.parseInt(parts[1]);
				sec = Integer.parseInt(parts[2]);
				ms = 0;
			}
		}
	}
	
	public void incrementTime(int ticksPerSecond) {
		int msPerTick = (int)((1.0/ticksPerSecond) * 1000);
		ms+=msPerTick;
		if(ms==1000) {
			ms = 0;
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
	
	public SimTime add(SimTime t) {
		double time = convertToHours(this);
		double timeToElapse = convertToHours(t);
		
		time += timeToElapse;
		
		SimTime result = convertToSimTime(time);
		return result;
	}
	
	private double convertToHours(SimTime st) {
		return st.hr + st.min/60.0 + st.sec/3600.0;
	}
	
	private SimTime convertToSimTime(double hours) {
		int hr = (int) Math.floor(hours);
		
		double minutes = (hours - hr) * 60;
		int min = (int) Math.floor(minutes);
		
		double seconds = (minutes - min) * 60;
		int sec = (int) Math.floor(seconds);
		
		return new SimTime(hr,min,sec);
	}
	
	public static boolean isValid(String timeString) {
		//Check to make sure the string has the right format
		if(!checkTwoDigit.matcher(timeString).matches()) {
			return false;
		}
		
		//Split into its parts
		String[] components = timeString.split(":");
		if (components.length==3) { //redundant check for three components
			//Check if all in the right bounds
			int h = Integer.parseInt(components[0]);
			int m = Integer.parseInt(components[1]);
			int s = Integer.parseInt(components[2]);
			if(h>=0 && h<24 &&
					m>=0 && m<60 &&
					s>=0 && s<60) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return(String.format("%02d",hr)+":"+String.format("%02d",min)+":"+String.format("%02d",sec));
	}
}