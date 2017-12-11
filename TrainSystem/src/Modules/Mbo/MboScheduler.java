package Modules.Mbo;

import java.time.LocalDateTime;
import java.util.*;

import Shared.SimTime;

public class MboScheduler {
	
	private String date;
	private int desiredThroughput;
	private ArrayList<OperatorSchedule> operators;
	private ArrayList<TrainSchedule> trains;

	private final static int[] greenLineStations = {2,9,16,22,31,39,48,57,65,73,77,88,96,114,123,132,141};
	private final static int[] redLineStations = {7,16,21,25,35,45,48,60};

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
		int totalMinutes = 0;
		for (TrainSchedule train : trains) {
			
			// create the train's schedule and append it at the end
			StringBuilder schedule = new StringBuilder();

			// get the time the train needs to be on the track
			SimTime start = new SimTime(train.startTime);
			SimTime stop = new SimTime(train.stopTime);
			int minutes = (stop.hr - start.hr)*60 + (stop.min - start.min);
			int stops = 0;
			totalMinutes += minutes;

			// stop for 2 minutes at every station
			// loop around the track, allowing for 5 minutes between stations, until need to return
			int elapsed = 0, index = 0;
			int[] stations = train.line.equals("RED") ? redLineStations : greenLineStations;
			while (minutes > elapsed) {
				schedule.append(String.format("%d,00:02:00\r\n", stations[index]));
				index = (index + 1) % stations.length;
				elapsed += 7;
				stops += 1;
			}

			// header info for each train
			String header = String.format("%s,%s,%s,%d,%s\r\n", train.name, train.startTime, train.line, stops, train.operator);
			output.append(String.format("%s%s\r\n", header, schedule.toString()));
		}
		output.append(String.format("%f\r\n", throughput));
		
		// check if throughput requirement can be met
		double possibleThroughput = (totalMinutes / 60.0) * 444;
		if (possibleThroughput < throughput) return String.format("Given schedule can only support throughput of %.2f passengers/hour.", possibleThroughput);

		return output.toString();
	}
}