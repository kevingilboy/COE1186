package Modules.Ctc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Modules.TrackModel.Switch;
import Modules.TrackModel.TrackIterator;
import Shared.SimTime;

public class Train {
	public String name;
	public Line line;
	public Schedule schedule;
	
	public int currLocation;
	public int prevLocation;
	public int suggestedSpeed;
	public boolean overrideSuggestedSpeed = false;
	public double authority;
	public int passengers;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
		this.name = schedule.name;
		this.line = schedule.line;
		this.passengers = 0;
		
		//Set up locations for the iterator
		prevLocation = -1;
		currLocation = line.yardOut;
	}
	
	public ArrayList<Integer> calculateAuthorityPath() {
		Queue<ArrayList<Integer>> q = new LinkedList<ArrayList<Integer>>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		int currBlockId = currLocation;
		int prevBlockId = prevLocation;
		int stopBlockId = schedule.getNextStop();
		
		q.add(new ArrayList<Integer>(Arrays.asList(prevBlockId,currBlockId)));
		
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
			// If block is on bidirectional track which is occupied, ditch the path
			//-------------------
			if(line.blocks[currBlockId].getDirection()==0) {
				if(bidirectionalStretchOccupied(line,currBlockId,prevBlockId)) {
					continue;
				}
			}
			
			//-------------------
			// If block is occupied, ditch the path
			//-------------------
			if(line.blocks[currBlockId].getOccupied()) {
				continue;
			}
			
			//-------------------
			// If at the stop, path is the optimal route so exit the while
			//-------------------
			if(currBlockId == stopBlockId) {
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
		} //while q not empty
		
		path.remove(0);
		/*
		for(int j=1; j<path.size(); j++) {
			int i = path.get(j);
			System.out.print(line.blocks[i].getSection()+Integer.toString(i+1)+", ");
		}
		System.out.println("");
		*/
		double dist = 0;
		for(int blockId : path) {
			dist += line.blocks[blockId].getLength();
		}
		this.authority = dist;
		
		//-------------------
		// Return the found path as the authority
		//-------------------
		return path;
	}

	public ArrayList<Integer> calculateDFSAuthority() {
		int nextLocation = (new TrackIterator(line.blocksAL, currLocation, prevLocation)).nextBlock();
		ArrayList<Integer> authority = DFS(line,nextLocation,currLocation,schedule.getNextStop(),new ArrayList<Integer>());
		return authority;
	}
	
	private ArrayList<Integer> DFS(Line line, int currBlockId,int prevBlockId, int destBlockId, ArrayList<Integer> path) {
		//-------------------
		// Base Cases
		//-------------------
		
		//Check if block is on bidirectional track and whether it is occupied
		if(line.blocks[currBlockId].getDirection()==0) {
			if(bidirectionalStretchOccupied(line,currBlockId,prevBlockId)) {
				return path;
			}
		}
		//Check if block is occupied
		else if(line.blocks[currBlockId].getOccupied()) {
			return path;
		}
		//Check if block leads to the yard
		else if(currBlockId==line.yardIn || currBlockId==line.yardOut) {
			return path;
		}
		//Check if block is the destination
		else if(currBlockId == destBlockId) {
			path.add(currBlockId);
			return path;
		}
		
		//-------------------
		// This block is valid and not the destination, so add it to the authority
		//-------------------
		path.add(currBlockId);
		
		//-------------------
		// Below the next block in the BFS is calculated.
		// This needs to keep switches in mind.
		//-------------------
		
		Switch swCurr= line.blocks[currBlockId].getSwitch();
		Switch swPrev= line.blocks[prevBlockId].getSwitch();
		//Entering a switch
		if(swCurr!=null && swPrev==null){
			// CASE: Entering a head from a non-switch, pursue both ports
			if(swCurr.getEdge()==Switch.EDGE_TYPE_HEAD && swPrev==null) {
				int norm = swCurr.getPortNormal();
				int alt = swCurr.getPortAlternate();
				ArrayList<Integer> pathNorm = new ArrayList<Integer>();
				ArrayList<Integer> pathAlt = new ArrayList<Integer>();
				
				//Follow both paths
				if(line.blocks[norm].getDirection()>=line.blocks[currBlockId].getDirection()) {
					pathNorm = DFS(line,norm,currBlockId,destBlockId,path);
				}
				if(line.blocks[alt].getDirection()>=line.blocks[currBlockId].getDirection()) {
					pathAlt = DFS(line,alt,currBlockId,destBlockId,path);
				}
				
				//Need to determine and return the best path, which has priority:
				// 1. Path from taking the normal port arrives at the dest
				// 2. Path from taking the alt port arrives at the dest
				// 3. Whichever path is longest
				if(pathNorm.get(pathNorm.size())==destBlockId) {
					return pathNorm;
				}
				else if(pathAlt.get(pathAlt.size())==destBlockId) {
					return pathAlt;
				}
				else {
					if(pathNorm.size()>=pathAlt.size()) {
						return pathNorm;
					}
					else {
						return pathAlt;
					}
				}
			}
			// CASE: Entering a tail from a non-switch, pursue the normal port 
			else if(swCurr.getEdge()==Switch.EDGE_TYPE_TAIL && swPrev==null) {
				return DFS(line,swCurr.getPortNormal(),currBlockId,destBlockId,path);
			}
		}
		// CASE : Not a switch or about to leave a switch so just use a vanilla TrackIterator to pursue the next block
		else {
			int nextBlockId = (new TrackIterator(line.blocksAL, currBlockId, prevBlockId)).nextBlock();
			return DFS(line,nextBlockId,currBlockId,destBlockId,path);
		}
		
		return path;
	}
	
	private static boolean bidirectionalStretchOccupied(Line line, int currBlockId, int prevBlockId) {
		//TODO test this method
		do {
			//If block is occupied, treat the bidirectional stretch as occupied
			if(line.blocks[currBlockId].getOccupied()) {
				return true;
			}
			
			//Block is not occupied, move to the next block
			int nextBlockId = (new TrackIterator(line.blocksAL, currBlockId, prevBlockId)).nextBlock();
			prevBlockId = currBlockId;
			currBlockId = nextBlockId;
		} while(line.blocks[currBlockId].getDirection()==0);
		
		//The bidirectional stretch must not be occupied
		return false;
	}
	
	public void calculateSuggestedSpeed() {
		if(!overrideSuggestedSpeed) {
			this.suggestedSpeed = line.blocks[this.currLocation].getSpeedLimit();
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
}
