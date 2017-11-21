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
	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}