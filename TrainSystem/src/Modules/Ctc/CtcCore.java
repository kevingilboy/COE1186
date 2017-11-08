//Kevin Gilboy
package Modules.Ctc;

import Shared.Module;
import Shared.SimTime;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class CtcCore implements Module,TimeControl {
	public CtcCore ctcCore;

	public CtcGui gui;
	public SimTime startTime;
	public SimTime currentTime;
	public int speedup = 1;
	
	public HashMap<String,Train> trains = new HashMap<>();
	public HashMap<String,Schedule> schedules = new HashMap<>();
	public Block[] redBlocks;
	public Block[] greenBlocks;
	
	public int runningTicketSales;
	public double throughput;

	public void startGui(){
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						gui = new CtcGui(ctcCore);
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
