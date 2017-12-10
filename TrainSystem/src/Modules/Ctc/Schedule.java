package Modules.Ctc;

import Shared.SimTime;
import Modules.TrackModel.Switch;
import Modules.TrackModel.TrackIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Schedule {
	public String name;
	public Line line;
	
	public SimTime departureTime;
	public ArrayList<Stop> stops;
	public int nextStopIndex;
	
	private final SimTime DEFAULT_DWELL = new SimTime("00:02:00");
	
	public Schedule(Line line) {
		this.line = line;
		nextStopIndex = 0;
		stops = new ArrayList<Stop>();
	}
	
	/**
	 * Add a stop with a dwell time 
	 */
	public void addStop(int index, int blockId, SimTime dwell) {
		//If stop does not exist, then add it. Else update the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(blockId,dwell));
		}
		else {
			stops.set(index,new Stop(blockId,dwell));
		}
		
		//We can still calculate time between stops
		calculateTimeToDestinations();
	}
	
	/**
	 * Add a stop without a dwell time (yet...)
	 */
	public void addStop(int index, int blockId) {
		//If stop does not exist, then add it. Else update the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(blockId));
			
			//Add default time to dwell
			stops.get(index).timeToDwell = new SimTime(DEFAULT_DWELL);
		}
		else {
			Stop existingStop = stops.get(index);
			existingStop.blockId = blockId;
		}
		
		//We can still calculate time between stops
		calculateTimeToDestinations();
	}
	
	/**
	 * Add a stop with a dwell time (may or may not exist)
	 */
	public void addStop(int index, SimTime timeToDwell) {
		//If stop does not exist, then add it. Else update the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(timeToDwell));
		}
		else {
			Stop existingStop = stops.get(index);
			existingStop.timeToDwell = timeToDwell;
		}
	}
	
	public int getNextStop() {
		if(stops.size()==0) 
			return -1;
		else
			return stops.get(nextStopIndex).blockId;
	}
	
	public void removeStop(int index) {
		stops.remove(index);
		
		//Recalc dwell times in case a stop is removed from the middle of the schedule
		calculateTimeToDestinations();
	}


	public void setName(String name) {
		this.name = name;	
	}

	public void setDepartureTime(SimTime time) {
		this.departureTime = time;
	}
	
	/**
	 * This function calculates time between stops
	 */
	private void calculateTimeToDestinations() {
		Queue<ArrayList<Integer>> q = new LinkedList<ArrayList<Integer>>();
		ArrayList<Integer> path;
		int currBlockId = line.yardOut;
		int prevBlockId = -1;
		
		for(Stop stop : stops) {
			//Sending the train to the yard is auto generated so no need for travel time
			if(stop.blockId==line.yardIn) {
				stop.timeToDest = new SimTime(0,0,0);
				return;
			}
			
			q.add(new ArrayList<Integer>(Arrays.asList(prevBlockId,currBlockId)));
			path = new ArrayList<Integer>();
			
			while(!q.isEmpty()) {
				//-------------------
				// Pop next path from queue
				//-------------------
				path = q.remove();
				currBlockId = path.get(path.size()-1);
				prevBlockId = path.get(path.size()-2);
				
				//TODO below is a temporary fix for the switch issue
				if(currBlockId>line.yardOut ||currBlockId<0) continue;
				
				
				//-------------------
				// If approaching the yard, ditch the path
				//-------------------
				if(prevBlockId==line.yardIn && currBlockId==-1) {
					continue;
				}
				
				//-------------------
				// If at the stop, path is the optimal route so exit the while
				//-------------------
				if(currBlockId == stop.blockId) {
					break;
				}
				
				//-------------------
				// Otherwise add adj blocks to the queue
				//-------------------
				Switch swCurr= line.blocks[currBlockId].getSwitch();
				Switch swPrev;
				if(prevBlockId==-1) {
					swPrev = null;
				}
				else{
					swPrev = line.blocks[prevBlockId].getSwitch();
				}
				//Entering a switch
				if(swCurr!=null && swPrev==null){
					// CASE: Entering a head from a non-switch, pursue both ports
					if(swCurr.getEdge()==Switch.EDGE_TYPE_HEAD && swPrev==null) {
						int normId = swCurr.getPortNormal();
						int altId = swCurr.getPortAlternate();
						
						//Follow both paths if valid
						if(line.blocks[normId].getDirection() == line.blocks[altId].getDirection()) {
							//int indexToFollow = (currBlockId+1==normId) ? normId : altId;
							ArrayList<Integer> normPath = cloneAndAppendAL(path,normId);
							q.add(normPath);
							ArrayList<Integer> altPath = cloneAndAppendAL(path,altId);
							q.add(altPath);
						}
						else {
							if(line.blocks[normId].getDirection()>=line.blocks[currBlockId].getDirection()) {
								ArrayList<Integer> newPath = cloneAndAppendAL(path,normId);
								q.add(newPath);
							}
							if(line.blocks[altId].getDirection()>=line.blocks[currBlockId].getDirection()) {
								ArrayList<Integer> newPath = cloneAndAppendAL(path,altId);
								q.add(newPath);
							}
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
					int nextBlockId = Ctc.getNextBlockId(line, currBlockId, prevBlockId);
					ArrayList<Integer> newPath = cloneAndAppendAL(path,nextBlockId);
					q.add(newPath);
				}
			} //while q not empty
			
			//Remove the first
			path.remove(0);
			
			//-------------------
			// Calculate and set the time to destination
			//-------------------
			double runningTime = 0;
			for(int blockId : path) {
				runningTime += line.blocks[blockId].getLength() / (line.blocks[blockId].getSpeedLimit() * (1/3600.0) * 1000);
			}
			int hr = (int)runningTime/3600;
			int min = (int)(runningTime%3600)/60;
			int sec = (int)runningTime%60;
			stop.timeToDest = new SimTime(hr,min,sec);
			
			//-------------------
			// Determine the new curr and prev blocks
			//  Must take care of case where the next block is one block away
			//-------------------
			if(path.size()>=2) {
				currBlockId = path.get(path.size()-1);
				prevBlockId = path.get(path.size()-2);
			}
			else if(path.size()==1) {
				currBlockId = path.get(0);
				prevBlockId = currBlockId;
			}
			
		} //for each stop
	}
	
	public <T> ArrayList<T> cloneAndAppendAL(ArrayList<T> oldAl, T newEl) {
		ArrayList<T> newAl = new ArrayList<T>();
		for(T oldEl : oldAl) {
			newAl.add(oldEl);
		}
		newAl.add(newEl);
		return newAl;
	}
	
	/**
	 * Convert the schedule to a 2D object array for JTable viewing
	 */
	public Object[][] toStringArray() {
		Object[][] grid = new Object[stops.size()][3];
		for(int i=0;i<stops.size();i++) {
			Stop stop = stops.get(i);
			
			//Get params
			int blockId = stop.blockId;
			SimTime timeToDwell = stop.timeToDwell;
			SimTime timeToDest = stop.timeToDest;
			
			//Handle null cases since some stops might not have dwells
			grid[i][0] = blockId != -1 ? line.blocks[blockId].toString() : "";
			grid[i][1] = timeToDwell != null ? timeToDwell.toString() : "";
			grid[i][2] = timeToDest != null ? timeToDest.toString() : "";
		}
		return grid;
	}
}
