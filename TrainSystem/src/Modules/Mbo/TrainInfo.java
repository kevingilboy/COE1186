package Modules.Mbo;

public class TrainInfo {
	
	private String name;
	private double[2] position;
	private LocalDateTime timeSignalReceived;
	private double[2] previousPosition;
	private LocalDateTime timePreviousSignalReceived;
	private double[2] velocity;
	private double speed;
	private double authority;
	private LocalDateTime timeSignalTransmitted;

	public TrainInfo() {

	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}