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
	
	
	public Schedule(Line line) {
		this.line = line;
		nextStopIndex = 0;
		stops = new ArrayList<Stop>();
	}
	
	public void addStop(int index, int blockId) {
		//If stop does not exist, then add it. Else replace the current stop
		if(index>=stops.size()) {
			stops.add(index,new Stop(blockId));
		}
		else {
			stops.set(index,new Stop(blockId));
		}
		calculateDwellTimes();
	}
	
	public int getNextStop() {
		return stops.get(nextStopIndex).blockId;
	}
	
	public void removeStop(int index) {
		stops.remove(index);
		
		//Recalc dwell times in case a stop is removed from the middle of the schedule
		calculateDwellTimes();
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
	private void calculateDwellTimes() {
		Queue<ArrayList<Integer>> q = new LinkedList<ArrayList<Integer>>();
		ArrayList<Integer> path;
		int currBlockId = line.yardOut;
		int prevBlockId = -1;
		
		for(Stop stop : stops) {
			q.add(new ArrayList<Integer>(Arrays.asList(prevBlockId,currBlockId)));
			path = new ArrayList<Integer>();
			
			while(!q.isEmpty()) {
				//-------------------
				// Pop next path from queue
				//-------------------
				path = q.remove();
				currBlockId = path.get(path.size()-1);
				prevBlockId = path.get(path.size()-2);
				
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
						if(line.blocks[normId].getDirection()>=line.blocks[currBlockId].getDirection()) {
							ArrayList<Integer> newPath = cloneAndAppendAL(path,normId);
							q.add(newPath);
						}
						if(line.blocks[altId].getDirection()>=line.blocks[currBlockId].getDirection()) {
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
					int nextBlockId = (new TrackIterator(line.blocksAL, currBlockId, prevBlockId)).nextBlock();
					ArrayList<Integer> newPath = cloneAndAppendAL(path,nextBlockId);
					q.add(newPath);
				}
				for(int j=1; j<path.size(); j++) {
					int i = path.get(j);
					System.out.print(line.blocks[i].getSection()+Integer.toString(i+1)+", ");
				}
				System.out.println("");
			} //while q not empty
			
			//-------------------
			// Calculate and set the dwell time
			//-------------------
			double runningTime = 0;
			for(int i=1;i<path.size();i++) {
				int blockId = path.get(i);
				runningTime += line.blocks[blockId].getSpeedLimit()/1000.0 * line.blocks[blockId].getLength();
			}
			int hr = (int)runningTime/3600;
			int min = (int)(runningTime%3600)/60;
			int sec = (int)runningTime%60;
			stop.timeToDwell = new SimTime(hr,min,sec);
			
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
	private void calculateDwellTimesOld() {
		//TODO convert this to BFS algorithm
		int currBlockId = line.yardOut;
		int prevBlockId = -1;
		
		TrackIterator ti;
		
		//Cycle through each stop
		for(Stop stop : stops) {
			//Store the cumulative travel time in runningTime
			double runningTime = 0;
			
			//Loop until the stop is reached by the iterator
			while(stop.blockId != currBlockId) {	
				runningTime += line.blocks[currBlockId].getSpeedLimit()/1000.0 * line.blocks[currBlockId].getLength();
				
				ti = new TrackIterator(line.blocksAL, currBlockId, prevBlockId);
				prevBlockId = currBlockId;
				currBlockId = ti.nextBlock();
			}
			
			//Set the dwell time
			int hr = (int)runningTime/3600;
			int min = (int)(runningTime%3600)/60;
			int sec = (int)runningTime%60;
			stop.timeToDwell = new SimTime(hr,min,sec);
		}
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
			grid[i][0] = line.blocks[stop.blockId+1].toString();
			grid[i][1] = stop.timeToDwell.toString();
		}
		return grid;
	}
}
