package Modules.Mbo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class TrainInfo {
	
	private String name;
	private double[] position;
	private String block;
	private LocalDateTime timeSignalReceived;
	private double[] previousPosition;
	private LocalDateTime timePreviousSignalReceived;
	private double[] velocity;
	private double speed;
	private double authority;
	private LocalDateTime timeSignalTransmitted;

	public TrainInfo(String name) {
		this.name = name;
		this.position = new double[2];
		this.position[0] = 0.0;
		this.position[1] = 0.0;
		this.velocity = new double[]{0,0};
		timeSignalReceived = LocalDateTime.now();
	}

	public Object[] toDataArray() {
		Object[] output = new Object[6];
		output[0] = name;
		output[1] = timeSignalReceived;
		output[2] = Arrays.toString(position);
		output[3] = block;
		output[4] = speed;
		output[5] = authority;
		return output;
	}

	public void updatePosition(double[] pos) {
		timeSignalReceived = LocalDateTime.now();
		position = pos;
		// todo do more
	}

	public void updatePosition(double[] pos, String block) {
		timeSignalReceived = LocalDateTime.now();
		position = pos;
		this.block = block;
		// todo do more
	}

	public double[] getPosition() {
		return position;
	}

	public double getAuthority() {
		return authority;
	}

	public void setAuthority(double auth) {
		authority = auth;
		System.out.printf("Authority for %s is %f\n", name, authority);
	}

	private void calculateVelocity() {
		double elapsedHours = timePreviousSignalReceived.until(timeSignalReceived, ChronoUnit.MILLIS);
		velocity[0] = (position[0] - previousPosition[0]) / elapsedHours;
		velocity[1] = (position[1] - previousPosition[1]) / elapsedHours;
		speed = Math.pow(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2), 0.5) * 100;


	}

	private void updateLatestSignal() {

	}
}