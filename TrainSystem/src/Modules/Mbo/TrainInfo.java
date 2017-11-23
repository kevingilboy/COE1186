package Modules.Mbo;

import java.time.LocalDateTime;

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
	}

	public Object[] toDataArray() {
		Object[] output = new Object[3];
		output[0] = name;
		output[1] = position[0];
		output[2] = position[1];
		return output;
	}

	public void updatePosition(double x, double y) {
		position[0] = x;
		position[1] = y;
		// todo do more
	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}