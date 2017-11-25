package Modules.Ctc;

import java.util.ArrayList;
import Modules.TrackModel.Switch;
import Modules.TrackModel.TrackIterator;

public class Train {
	public String name;
	public Line line;
	public int currLocation;
	public int prevLocation;
	public int speed;
	public int authority;
	public Schedule schedule;
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

	public ArrayList<Integer> calculateAuthority() {
		int nextLocation = (new TrackIterator(line.blocksAL, currLocation, prevLocation)).nextBlock();
		ArrayList<Integer> authority = BFS(line,nextLocation,currLocation,schedule.getNextStop(),new ArrayList<Integer>());
		return authority;
	}
	
	private static ArrayList<Integer> BFS(Line line, int currBlockId,int prevBlockId, int destBlockId, ArrayList<Integer> path) {
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
					pathNorm = BFS(line,norm,currBlockId,destBlockId,path);
				}
				if(line.blocks[alt].getDirection()>=line.blocks[currBlockId].getDirection()) {
					pathAlt = BFS(line,alt,currBlockId,destBlockId,path);
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
				return BFS(line,swCurr.getPortNormal(),currBlockId,destBlockId,path);
			}
		}
		// CASE : Not a switch or about to leave a switch so just use a vanilla TrackIterator to pursue the next block
		else {
			int nextBlockId = (new TrackIterator(line.blocksAL, currBlockId, prevBlockId)).nextBlock();
			return BFS(line,nextBlockId,currBlockId,destBlockId,path);
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
}
