package Modules.Mbo;

import java.time.LocalDateTime;
import java.util.*;

public class MboScheduler {
	
	private String date;
	private int desiredThroughput;
	private ArrayList<OperatorSchedule> operators;
	private ArrayList<TrainSchedule> trains;

	private class OperatorSchedule {
		private String name;
		private String startTime;
		private String stopTime;

		private OperatorSchedule(String[] schedule) {
			name = schedule[0];
			startTime = schedule[1];
			stopTime = schedule[2];
		}
	}

	private class TrainSchedule {
		private String name;
		private String startTime;
		private String stopTime;

		private TrainSchedule(String[] schedule) {
			name = schedule[0];
			startTime = schedule[1];
			stopTime = schedule[2];
		}
	}

	public MboScheduler() {
		this.operators = new ArrayList<OperatorSchedule>();
		this.trains = new ArrayList<TrainSchedule>();
	}

	public boolean updateTrainSchedules(String[][] schedules) {
		this.trains = new ArrayList<TrainSchedule>();
		for (String[] schedule : schedules) {
			if (schedule[0].equals("") && schedule[1].equals("") && schedule[2].equals("")) {
				return false;
			}
			trains.add(new TrainSchedule(schedule));
		}
		return true;
	}

	public boolean updateOperatorSchedules(String[][] schedules) {
		this.operators = new ArrayList<OperatorSchedule>();
		for (String[] schedule : schedules) {
			if (schedule[0].equals("") && schedule[1].equals("") && schedule[2].equals("")) {
				return false;
			}
			operators.add(new OperatorSchedule(schedule));
		}
		return true;
	}

	public String generateSchedule(String filename, double throughput) {
		StringBuilder output = new StringBuilder();
		for (TrainSchedule train : trains) {
			output.append(String.format("%s\t%s\t%s\r\n", train.name, train.startTime, train.stopTime));
		}
		for (OperatorSchedule operator : operators) {
			output.append(String.format("%s\t%s\t%s\r\n", operator.name, operator.startTime, operator.stopTime));
		}
		output.append(String.format("%f\r\n", throughput));
		return output.toString();
	}
}