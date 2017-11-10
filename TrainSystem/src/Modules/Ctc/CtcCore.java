//Kevin Gilboy
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
import java.util.HashMap;

public abstract class CtcCore implements Module,TimeControl {
	public CtcCore ctc;

	public CtcGui gui;
	public SimTime startTime;
	public SimTime currentTime;
	public int speedup = 1;
	
	public HashMap<String,Train> trains = new HashMap<>();
	public HashMap<String,Schedule> schedules = new HashMap<>();

	public TrackCsvParser trackParser = new TrackCsvParser();
	public Block[] redBlocks;
	public Block[] greenBlocks;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initializeBlocks() {
		ArrayList<Block> redBlocksAL = trackParser.parse("Modules/Ctc/RedLineFinal.csv");
		redBlocks = redBlocksAL.toArray(new Block[redBlocksAL.size()]);
		
		ArrayList<Block> greenBlocksAL = trackParser.parse("Modules/Ctc/GreenLineFinal.csv");
		greenBlocks = greenBlocksAL.toArray(new Block[greenBlocksAL.size()]);
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
	 * ADDERS
	 */
	public void addTicketSales(int tickets) {
		runningTicketSales+=tickets;
		calculateThroughput();
	}
	
	public void addTrain(String name, Schedule schedule) {
		Train train = new Train();
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
