package Modules.Mbo;

import java.time.LocalDateTime;

public class MboScheduler {
	
	private String date;
	private int desiredThroughput;

	private class OperatorSchedule {
		private String name;
		private LocalDateTime startTime;
		private LocalDateTime stopTime;
	}

	private class TrainSchedule {
		private String name;
		private LocalDateTime startTime;
		private LocalDateTime stopTime;
	}

	public MboScheduler() {

	}

	public boolean updateTrainSchedules(String[][] schedules) {
		return false;
	}

	public boolean updateOperatorSchedules(String[][] schedules) {
		return false;
	}

	public String generateSchedule(String filename, double throughput) {
		return null;
	}
}