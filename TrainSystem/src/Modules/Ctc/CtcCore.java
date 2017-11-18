package Modules.Ctc;

import Shared.Module;
import Shared.SimTime;

//Temporary import while TM is under development
//Once project is complete, I will need the Block, Switch, Station, and Beacon classes kept locally
import Modules.TrackModel.TrackCsvParser;
import Modules.TrackModel.Block;
import Modules.TrackModel.Light;
import Modules.TrackModel.Crossing;
import Modules.TrackModel.Station;
import Modules.TrackModel.Switch;
import Modules.TrackModel.Beacon;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class CtcCore implements Module,TimeControl {
	public CtcCore ctcCore;
	public Ctc ctc;
	public CtcStandalone ctcStandalone;
	
	public CtcGui gui;
	public SimTime startTime;
	public SimTime currentTime;
	public int speedup = 1;
	
	public HashMap<String,Train> trains = new HashMap<>();
	public HashMap<String,Schedule> schedules = new HashMap<>();

	public TrackCsvParser trackParser = new TrackCsvParser();
	
	public int runningTicketSales;
	public double throughput;

	public void startGui(){
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						gui = new CtcGui(ctc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void initializeBlocks() {
		for(Line line : Line.values()) {
			ArrayList<Block> blocks = trackParser.parse("Modules/Ctc/"+line.toString()+"LineFinal.csv");
			line.blocksAL = blocks;
			line.blocks = blocks.toArray(new Block[blocks.size()]);
		}
	}
	
	public boolean updateTime(SimTime time) {
		if(startTime==null) {
			startTime = new SimTime(time);
		}
		currentTime = new SimTime(time);
		
		//DO STUFF HERE
		calculateThroughput();
		//TODO update trains
		
		gui.repaint();
		
		return true;
	}
	
	/*
	 * FUNCTIONALITY
	 */	
	protected void calculateThroughput() {
		double timeElapsed = SimTime.hoursBetween(startTime, currentTime);
		throughput = runningTicketSales/timeElapsed;
	}

	/*
	 * GETTERS
	 */
	public Schedule getScheduleByName(String name){
		Schedule schedule = schedules.get(name);
		return schedule;
	}
	
	public Train getTrainByName(String name){
		Train train = trains.get(name);
		return train;
	}
	
	/*
	 * SETTERS
	 */

	/*
	 * TICKETS
	 */
	public void addTicketSales(int tickets) {
		runningTicketSales+=tickets;
		calculateThroughput();
	}
	
	public void addPassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers += number;
	}
	
	public void removePassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers -= number;
	}
	
	/*
	 * ADDERS
	 */	
	public void dispatchTrain(String name) {
		Schedule schedule = removeScheduleByName(name);
		
		Train train = new Train(schedule);
		trains.put(name, train);
		
		
	}
	
	public void addSchedule(String name, Schedule schedule) {
		schedules.put(name, schedule);
	}
	
	/*
	 * REMOVERS
	 */
	public Schedule removeScheduleByName(String name) {
		Schedule removedSchedule = schedules.remove(name);
		return removedSchedule;
	}
}
