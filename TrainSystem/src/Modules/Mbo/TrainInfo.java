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

	public void updatePosition(double x, double y) {
		timeSignalReceived = LocalDateTime.now();
		position[0] = x;
		position[1] = y;
		// todo do more
	}

	public double[] getPosition() {
		return position;
	}

	public double getAuthority() {
		return authority;
	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}