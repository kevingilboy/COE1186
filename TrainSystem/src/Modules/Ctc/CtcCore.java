package Modules.Ctc;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackCsvParser;
import Modules.TrackModel.TrackIterator;
import Modules.TrackController.TrackController;
import Modules.TrackModel.Block;
import Modules.TrackModel.Switch;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
		
		//Trigger TrackControllers to update before proceeding
		for(TrackController tc : ctc.trackControllers) {
			//Wait for tc to update before continuing
			while(!tc.updateTime(currentTime)) {};
		}
		
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
			
		for(Train train : trains.values()) {
			/*
			 * LOCATION
			 */
			//Check if train has moved
			int currentLocation = train.currLocation;
			int nextLocation = (new TrackIterator(train.line.blocksAL, train.currLocation, train.prevLocation)).nextBlock();
			Boolean currOccupied = ctc.getWaysideOfBlock(train.line, currentLocation).receiveBlockInfoForCtc(train.line.toString(), currentLocation).getOccupied();
			Boolean nextOccupied = ctc.getWaysideOfBlock(train.line, nextLocation).receiveBlockInfoForCtc(train.line.toString(), nextLocation).getOccupied();
			if(!currOccupied && nextOccupied) {
				//Train has moved on
				train.prevLocation = currentLocation;
				train.currLocation = nextLocation;
			}
			
			//Get Wayside of current location
			TrackController wayside = ctc.getWaysideOfBlock(train.line,train.currLocation);
			
			/*
			 * AUTHORITY
			 */
			//Calculate authority
			ArrayList<Integer> authorityAl = calculateAuthorityPath(train);
			
			//Translate to int array for Wayside
			int[] authority = new int[authorityAl.size()];
			for(int i=0; i<authorityAl.size(); i++) {
				authority[i] = authorityAl.get(i);
			}
			
			//Transmit authority
			transmitCtcAuthority(train.name, wayside, authority);
			
			/*
			 * SPEED
			 */
			//Calculate speed
			calculateSuggestedSpeed(train);
			double suggestedSpeedInMps = train.suggestedSpeed / 2.23694;
			transmitSuggestedSpeed(train.name, wayside, suggestedSpeedInMps);
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
	
	public ArrayList<Integer> calculateAuthorityPath(Train train) {
		Queue<ArrayList<Integer>> q = new LinkedList<ArrayList<Integer>>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		int currBlockId = train.currLocation;
		int prevBlockId = train.prevLocation;
		int stopBlockId = train.schedule.getNextStop();
		
		q.add(new ArrayList<Integer>(Arrays.asList(prevBlockId,currBlockId)));
		
		while(!q.isEmpty()) {
			//-------------------
			// Pop next path from queue
			//-------------------
			path = q.remove();
			currBlockId = path.get(path.size()-1);
			prevBlockId = path.get(path.size()-2);
				
			//TODO below is a temporary fix for the switch issue
			if(currBlockId>train.line.yardOut ||currBlockId<0) continue;
			
			//-------------------
			// If approaching the yard, ditch the path
			//-------------------
			if(prevBlockId==train.line.yardIn && currBlockId==-1) {
				continue;
			}
			
			//-------------------
			// If block is occupied, ditch the path
			//-------------------
			if(train.line.blocks[currBlockId].getOccupied()) {
				path.remove(path.size()-1);
				continue;
			}
			
			//-------------------
			// If at the stop, path is the optimal route so exit the while
			//-------------------
			if(currBlockId == stopBlockId) {
				break;
			}
			
			//-------------------
			// If block is on bidirectional track which is occupied, ditch the path
			//-------------------
			int nbId = (new TrackIterator(train.line.blocksAL, currBlockId, prevBlockId)).nextBlock();
			if((nbId<=train.line.yardIn && nbId>=0) && train.line.blocks[nbId].getDirection()==0) {
				if(bidirectionalStretchOccupied(train.line,nbId,currBlockId)) {
					continue;
				}
			}
			
			//-------------------
			// Otherwise add adj blocks to the queue
			//-------------------
			Switch swCurr= train.line.blocks[currBlockId].getSwitch();
			Switch swPrev;
			if(prevBlockId==-1) {
				swPrev = null;
			}
			else{
				swPrev = train.line.blocks[prevBlockId].getSwitch();
			}
			//Entering a switch
			if(swCurr!=null && swPrev==null){
				// CASE: Entering a head from a non-switch, pursue both ports
				if(swCurr.getEdge()==Switch.EDGE_TYPE_HEAD && swPrev==null) {
					int normId = swCurr.getPortNormal();
					int altId = swCurr.getPortAlternate();
					
					//Follow both paths if valid
					if(train.line.blocks[normId].getDirection()>=train.line.blocks[currBlockId].getDirection()) {
						ArrayList<Integer> newPath = cloneAndAppendAL(path,normId);
						q.add(newPath);
					}
					if(train.line.blocks[altId].getDirection()>=train.line.blocks[currBlockId].getDirection()) {
						ArrayList<Integer> newPath = cloneAndAppendAL(path,altId);
						q.add(newPath);
					}
				}
				// CASE: Entering a tail from a non-switch, pursue the normal port 
				else if(swCurr.getEdge()==Switch.EDGE_TYPE_TAIL && swPrev==null) {
					int nextBlockId = swCurr.getPortNormal();
					ArrayList<Integer> newPath = cloneAndAppendAL(path,nextBlockId);
					q.add(newPath);
				}
			}
			// CASE : Not a switch or about to leave a switch so just use a vanilla TrackIterator to pursue the next block
			else {
				int nextBlockId = (new TrackIterator(train.line.blocksAL, currBlockId, prevBlockId)).nextBlock();
				ArrayList<Integer> newPath = cloneAndAppendAL(path,nextBlockId);
				q.add(newPath);
			}
		} //while q not empty
		
		path.remove(0);

		double dist = 0;
		for(int blockId : path) {
			dist += train.line.blocks[blockId].getLength();
		}
		train.authority = dist * 0.000621371192237; //convert distance to miles for display
		
		//-------------------
		// Return the found path as the authority
		//-------------------
		return path;
	}
	
	public void calculateSuggestedSpeed(Train train) {
		train.suggestedSpeed = train.line.blocks[train.currLocation].getSpeedLimit();
	}
	
	public <T> ArrayList<T> cloneAndAppendAL(ArrayList<T> oldAl, T newEl) {
		ArrayList<T> newAl = new ArrayList<T>();
		for(T oldEl : oldAl) {
			newAl.add(oldEl);
		}
		newAl.add(newEl);
		return newAl;
	}
	
	private boolean bidirectionalStretchOccupied(Line line, int currBlockId, int prevBlockId) {
		//TODO test this method
		Boolean[] visited = new Boolean[line.blocks.length];
		for(int i=0; i<visited.length; i++) {
			visited[i] = false;
		}
		visited[prevBlockId] = true;
		
		do {
			//System.out.println(currBlockId);
			if(visited[currBlockId] == true) break;
			//If block is occupied, treat the bidirectional stretch as occupied
			Boolean currOccupied = ctc.getWaysideOfBlock(line, currBlockId).receiveBlockInfoForCtc(line.toString(), currBlockId).getOccupied();
			if(currOccupied) {
				return true;
			}
			
			visited[currBlockId] = true;
			
			//Block is not occupied, move to the next block
			int nextBlockId = (new TrackIterator(line.blocksAL, currBlockId, prevBlockId)).nextBlock();
			prevBlockId = currBlockId;
			currBlockId = nextBlockId;
		} while(line.blocks[currBlockId].getDirection()==0);
		
		//The bidirectional stretch must not be occupied
		return false;
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
	protected void transmitSuggestedSpeed(String name, TrackController wayside, double speed) {
		wayside.transmitSuggestedTrainSetpointSpeed(name,speed);
	}
	protected void transmitCtcAuthority(String name, TrackController wayside, int[] auth) {
		wayside.transmitCtcAuthority(name,auth);
	}
}
