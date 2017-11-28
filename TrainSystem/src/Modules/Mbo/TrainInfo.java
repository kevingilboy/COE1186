package Modules.Mbo;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TrainInfo {
	
	private String name;
	private double[] position;
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
		timeSignalReceived = LocalDateTime.now();
	}

	public Object[] toDataArray() {
		Object[] output = new Object[3];
		output[0] = name;
		output[1] = timeSignalReceived;
		output[2] = Arrays.toString(position);
		return output;
	}

	public void updatePosition(double[] pos) {
		timeSignalReceived = LocalDateTime.now();
		position = pos;
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
	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}