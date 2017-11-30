//Kevin Gilboy

package Modules.Ctc;

import Simulator.Simulator;
import Shared.Module;
import Shared.SimTime;

import Modules.TrackController.TrackController;
import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrainController.TrainController;
import Modules.TrackModel.TrackCsvParser;
import Modules.TrackModel.TrackIterator;
import Modules.TrackModel.Block;
import Modules.TrackModel.Switch;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Ctc implements Module,TimeControl {
	public Ctc ctc;
	
	public CtcGui gui;
	public SimTime startTime;
	public SimTime currentTime;
	public int speedup = 1;
	
	public HashMap<String,Train> trains = new HashMap<>();
	public HashMap<String,Schedule> schedules = new HashMap<>();

	public TrackCsvParser trackParser = new TrackCsvParser();
	
	public int runningTicketSales;
	public double throughput;

	public Simulator simulator = null;
	public TrackController[] trackControllers;
	public TrackModel trackModel = null;
	public TrainModel trainModel = null;
	public TrainController trainController = null;
	
	public Ctc() {
		ctc = this;

		initializeBlocks();
		startGui();
		while(gui==null) {
			
		}
		return;
	}
	
	/*
	 * ------------------------------
	 *  GUI
	 * ------------------------------
	 */
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
	
	/*
	 * ------------------------------
	 *  BLOCKS
	 * ------------------------------
	 */
	
	/**
	 * Load in blocks from csv
	 */
	public void initializeBlocks() {
		for(Line line : Line.values()) {
			ArrayList<Block> blocks = new TrackCsvParser().parse("Modules/Ctc/"+line.toString()+"LineFinal.csv");
			line.blocksAL = blocks;
			line.blocks = blocks.toArray(new Block[blocks.size()]);
		}
	}
	
	/**
	 * Gets the next block ID given a current and previous block
	 */
	public int getNextBlockId(Line line, int currBlock, int prevBlock) {
		TrackIterator ti = new TrackIterator(line.blocksAL, currBlock, prevBlock);
		return ti.nextBlock();
	}
	
	/**
	 * Gets the current wayside of a block
	 */
	public TrackController getWaysideOfBlock(Line line, int blockNum) {
		if(line==Line.GREEN) {
			if( (0 <= blockNum && blockNum < 53) || (127 <= blockNum && blockNum < 150) ) {
				return trackControllers[0];
			}
			else if( (53 <= blockNum && blockNum < 127) || (150 <= blockNum && blockNum < 152) ){
				return trackControllers[1];
			}
		}
		else if(line==Line.RED) {
			if( (0 <= blockNum && blockNum < 33) || (76 == blockNum) ) {
				return trackControllers[2];
			}
			else if(33 <= blockNum && blockNum < 76){
				return trackControllers[3];	
			}
		}
		
		return null;
	}
	
	/**
	 * Checks the TrackModel block occupancy via the TrackController
	 */
	public Boolean getTrackCircuit(Line line, int blockNum) {
		TrackController wayside = getWaysideOfBlock(line, blockNum);
		Block block = wayside.receiveBlockInfoForCtc(line.toString(), blockNum);
		Boolean occupied = block.getOccupied();
		return occupied;
	}
	
	/*
	 * ------------------------------
	 *  SCHEDULES
	 * ------------------------------
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
	 * ------------------------------
	 *  TRAINS
	 * ------------------------------
	 */
	/**
	 * Returns a train given a name
	 */
	protected Train getTrainByName(String name){
		Train train = trains.get(name);
		return train;
	}
	
	/**
	 * Dispatch a train and call respective methods in TrainModel and TrainController
	 */
	public void dispatchTrain(String name) {
		Schedule schedule = removeScheduleByName(name);
		
		Train train = new Train(schedule);
		trains.put(name, train);
		
		trainModel.dispatchTrain(name, train.line.toString().toUpperCase());
		trainController.dispatchTrain(name, train.line.toString().toUpperCase()); 
	}
	
	/**
	 * TrainModel uses this method to add passengers to the train
	 */
	public void addPassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers += number;
	}
	
	/**
	 * TrainModel uses this method to remove passengers from a train
	 */
	public void removePassengers(String trainName, int number) {
		Train train = getTrainByName(trainName);
		train.passengers -= number;
	}
	
	/**
	 * Calculate authority via a BFS
	 */
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
			int nbId = getNextBlockId(train.line, currBlockId, prevBlockId);
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
				int nextBlockId = getNextBlockId(train.line, currBlockId, prevBlockId);
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
			Boolean currOccupied = getTrackCircuit(line, currBlockId);
			if(currOccupied) {
				return true;
			}
			
			visited[currBlockId] = true;
			
			//Block is not occupied, move to the next block
			int nextBlockId = getNextBlockId(line, currBlockId, prevBlockId);
			prevBlockId = currBlockId;
			currBlockId = nextBlockId;
		} while(line.blocks[currBlockId].getDirection()==0);
		
		//The bidirectional stretch must not be occupied
		return false;
	}
	
	public <T> ArrayList<T> cloneAndAppendAL(ArrayList<T> oldAl, T newEl) {
		ArrayList<T> newAl = new ArrayList<T>();
		for(T oldEl : oldAl) {
			newAl.add(oldEl);
		}
		newAl.add(newEl);
		return newAl;
	}
	
	
	/*
	 * ------------------------------
	 *  TICKETS
	 * ------------------------------
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
	 * ------------------------------
	 *  TRANSMITTERS
	 * ------------------------------
	 */
	protected void transmitSuggestedSpeed(String name, TrackController wayside, double speed) {
		wayside.transmitSuggestedTrainSetpointSpeed(name,speed);
	}
	protected void transmitCtcAuthority(String name, TrackController wayside, int[] auth) {
		wayside.transmitCtcAuthority(name,auth);
	}
	
	/*
	 * ------------------------------
	 *  MODULE REQUIREMENTS
	 * ------------------------------
	 */
	@Override
	public boolean updateTime(SimTime time) {
		if(startTime==null) {
			startTime = new SimTime(time);
		}
		currentTime = new SimTime(time);
		
		//Trigger TrackControllers to update before proceeding
		for(TrackController tc : trackControllers) {
			//Wait for tc to update before continuing
			while(!tc.updateTime(currentTime)) {};
		}
		
		//Throughput
		calculateThroughput();
		
		//Auto-dispatch from queue
		for(Schedule schedule : schedules.values()) {
			if(schedule.departureTime.equals(currentTime)) {
				String name = schedule.name;
				dispatchTrain(name);
				gui.autoDispatchFromQueue(name);
			}
		}
			
		for(Train train : trains.values()) {
			/*
			 * LOCATION
			 */
			//Check if train has moved
			int currentLocation = train.currLocation;
			int nextLocation = getNextBlockId(train.line, train.currLocation, train.prevLocation);
			Boolean currOccupied = getTrackCircuit(train.line, currentLocation);
			Boolean nextOccupied = getTrackCircuit(train.line, nextLocation);
			if(!currOccupied && nextOccupied) {
				//Train has moved on
				train.prevLocation = currentLocation;
				train.currLocation = nextLocation;
			}
			
			//Get Wayside of current location
			TrackController wayside = getWaysideOfBlock(train.line,train.currLocation);
			
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
	
	@Override
	public boolean communicationEstablished() {
		//TODO prettify
		trackControllers = new TrackController[4];
		trackControllers[0] = new TrackController("Green",new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149"},"G1");
		trackControllers[1] = new TrackController("Green",new String[]{"53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","150","151"},"G2");
		trackControllers[2] = new TrackController("Red",  new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","76"},"R1");
		trackControllers[3] = new TrackController("Red",  new String[]{"33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75"},"R2");
		for(TrackController tc : trackControllers) {
			tc.trackModel = trackModel;
		}
		
		gui.updateSelectedBlock();
		
		return true;
	}
	
	/*
	 * ------------------------------
	 *  TIMECONTROL REQUIREMENTS
	 * ------------------------------
	 */
	@Override
	public void pause() {
	    simulator.pause();
	}
	@Override
	public void play() {
	    simulator.play();
	}
	@Override
	public void setSpeedup(int newSpeed) {
	    simulator.setSpeedup(newSpeed);
	}
}
