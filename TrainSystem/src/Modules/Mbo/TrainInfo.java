package Modules.Mbo;

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

	public TrainInfo() {

	}

	private void calculateVelocity() {

	}

	private void updateLatestSignal() {

	}
}