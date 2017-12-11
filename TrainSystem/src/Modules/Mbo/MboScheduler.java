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
		private boolean assigned;

		private OperatorSchedule(String[] schedule) {
			name = schedule[0];
			startTime = schedule[1];
			stopTime = schedule[2];
			assigned = false;
		}
	}

	private class TrainSchedule {
		private String name;
		private String startTime;
		private String stopTime;
		private String line;
		private String operator;

		private TrainSchedule(String[] schedule) {
			name = schedule[0];
			startTime = schedule[1];
			stopTime = schedule[2];
			line = name.toUpperCase().contains("RED") ? "RED" : "GREEN";
			operator = null;
		}
	}

	public MboScheduler() {
		this.operators = new ArrayList<OperatorSchedule>();
		this.trains = new ArrayList<TrainSchedule>();
	}

	public boolean updateTrainSchedules(String[][] schedules) {
		this.trains = new ArrayList<TrainSchedule>();
		for (String[] schedule : schedules) {
			if (schedule[0].equals("") || schedule[1].equals("") || schedule[2].equals("")) {
				return false;
			}
			trains.add(new TrainSchedule(schedule));
		}
		return true;
	}

	public boolean updateOperatorSchedules(String[][] schedules) {
		this.operators = new ArrayList<OperatorSchedule>();
		for (String[] schedule : schedules) {
			if (schedule[0].equals("") || schedule[1].equals("") || schedule[2].equals("")) {
				return false;
			}
			operators.add(new OperatorSchedule(schedule));
		}
		return true;
	}

	public String generateSchedule(String filename, String title, double throughput) {

		// match operators to trains
		// currently requires one operator to ride train for entire duration of schedule
		// if operator changes during day, requires two seperate schedules
		for (TrainSchedule train : trains) {
			for (OperatorSchedule operator : operators) {
				if (operator.startTime.equals(train.startTime) && operator.stopTime.equals(train.stopTime) && !operator.assigned) {
					train.operator = operator.name;
					operator.assigned = true;
				}
			}
			if (train.operator == null) {
				return "Not enough operators for trains";
			}
		}

		// output results
		StringBuilder output = new StringBuilder();
		output.append(String.format("%s\r\n\r\n", title));
		for (TrainSchedule train : trains) {
			output.append(String.format("%s,%s,%s,%s\r\n", train.name, train.startTime, train.line, train.operator));
			output.append("\r\n");
		}
		output.append(String.format("%f\r\n", throughput));
		return output.toString();
	}
}