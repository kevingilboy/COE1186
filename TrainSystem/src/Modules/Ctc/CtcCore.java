package Modules.Ctc;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackCsvParser;
import Modules.TrackModel.Block;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
			ArrayList<Block> blocks = new TrackCsvParser().parse("Modules/Ctc/"+line.toString()+"LineFinal.csv");
			line.blocksAL = blocks;
			line.blocks = blocks.toArray(new Block[blocks.size()]);
		}
	}
	
	public boolean updateTime(SimTime time) {
		if(startTime==null) {
			startTime = new SimTime(time);
		}
		currentTime = new SimTime(time);
		
		//Throughput
		calculateThroughput();
		
		//Auto-dispatch from queue
		for(Schedule schedule : ctc.schedules.values()) {
			if(schedule.departureTime.equals(ctc.currentTime)) {
				String name = schedule.name;
				ctc.dispatchTrain(name);
				gui.autoDispatchFromQueue(name);
			}
		}
		
		//Calculate authority
		for(Train train : trains.values()) {
			/*
			ArrayList<Integer> authorityAl = train.calculateAuthorityPath();
			
			int[] authority = new int[authorityAl.size()];
			for(int i=0; i<authorityAl.size(); i++) {
				authority[i] = authorityAl.get(i);
			}
			
			transmitCtcAuthority(train.name, authority);
			*/
		}
		
		gui.repaint();
		
		return true;
	}
	
	/*
	 * SCHEDULES
	 */
	protected Schedule getScheduleByName(String name){
		Schedule schedule = schedules.get(name);
		return schedule;
	}
	
	protected void addSchedule(String name, Schedule schedule) {
		schedules.put(name, schedule);
	}
	
	protected Schedule removeScheduleByName(String name) {
		Schedule removedSchedule = schedules.remove(name);
		return removedSchedule;
	}
	
	/*
	 * TRAINS
	 */	
	protected Train getTrainByName(String name){
		Train train = trains.get(name);
		return train;
	}
	
	protected void dispatchTrain(String name) {
		Schedule schedule = removeScheduleByName(name);
		
		Train train = new Train(schedule);
		trains.put(name, train);
	}
	
	//TrainModel uses this method to add passengers to the train
	public void addPassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers += number;
	}
	
	//TrainModel uses this method to remove passengers from a train
	public void removePassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers -= number;
	}
	
	/*
	 * TICKETS
	 */
	//TrackModel uses this method to add ticket sales for throughput
	public void addTicketSales(int tickets) {
		runningTicketSales+=tickets;
	}
	
	protected void calculateThroughput() {
		double timeElapsed = SimTime.hoursBetween(startTime, currentTime);
		throughput = runningTicketSales/timeElapsed;
	}
	
	/*
	 * TRANSMITTERS
	 */
	protected void transmitSuggestedSpeed(String name, int speed) {
		ctc.trackController.transmitSuggestedTrainSetpointSpeed(name,speed);
	}
	protected void transmitCtcAuthority(String name, int[] auth) {
		ctc.trackController.transmitCtcAuthority(name,auth);
	}
}
