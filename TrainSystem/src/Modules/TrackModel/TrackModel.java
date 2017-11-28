/**
 * Train Simulation System
 * Track Model sub-system class developed by Kevin Le
 */

package Modules.TrackModel;

import Shared.Module;
import Shared.SimTime;

import Simulator.Simulator;
import Modules.TrainModel.TrainModel;
import Modules.TrainModel.Position;
import Modules.Ctc.Ctc;

import java.util.*;

public class TrackModel implements Module{
	
	// Allowable references to other modules
	public Simulator simulator;
	public Ctc ctc;
	public TrainModel trainModel;

	// Data structure for storing blocks connected in each track
	private ArrayList<Block> redLineBlocks = new ArrayList<Block>();
	private ArrayList<Block> greenLineBlocks = new ArrayList<Block>();

	// Data structures for tracking active train information
	private ArrayList<String> redTrainIDs = new ArrayList<String>();
	private ArrayList<String> greenTrainIDs = new ArrayList<String>();
	private ArrayList<Position> redPositions = new ArrayList<Position>();
	private ArrayList<Position> greenPositions = new ArrayList<Position>();
	
	// GUI references
	private TrackModelGUI trackModelGUI;

	// Constructor
	public TrackModel(){	

		// Instantiate Track Model GUI
		trackModelGUI = new TrackModelGUI(this);
	}

	// Access a specific block on track specified by line and block ID
	public Block getBlock(String line, int id){
		Block block = new Block();

		// get block at index (id - 1)
		if (line.toLowerCase() == "red"){
			block = redLineBlocks.get(id - 1);
		} else if (line.toLowerCase() == "green"){
			block = greenLineBlocks.get(id - 1);
		}

		return block;
	}

	// Access the entire ArrayList of blocks in a track specified
	// by the line
	public ArrayList<Block> getTrack(String line){
		ArrayList<Block> track = null;

		if (line.toLowerCase() == "green"){
			track = greenLineBlocks;
		} else if (line.toLowerCase() == "red"){
			track = redLineBlocks;
		}

		return track;
	}
 
 	// Set the track for a particular line
	public void setTrack(String line, ArrayList<Block> track){
		if (line.toLowerCase() == "green"){
			greenLineBlocks = track;
		} else if (line.toLowerCase() == "red"){
			redLineBlocks = track;
		}
	}

	// Main method for standalone operation of the Track Model
	public static void main(String[] args){
		new TrackModel();
	}

	// INTER-MODULE COMMUNICATION CLASSES
	public void dispatchTrain(String line, String trainID, Position pos){
		
		if (line.toLowerCase() == "red"){
			redTrainIDs.add(trainID);
			redPositions.add(pos);
			trackModelGUI.redLineDisplay.dispatchTrain(trainID, pos);
		} else if (line.toLowerCase() == "green"){
			greenTrainIDs.add(trainID);
			greenPositions.add(pos);
			trackModelGUI.greenLineDisplay.dispatchTrain(trainID, pos);
		}
	}

	public void transmitCtcAuthority(String trainName, double authority){
		// ...
	}

	public void transmitSuggestedTrainSetpointSpeed(String trainName, int speed){
		// ...
	}

	// Respond to the Simulator's regularly call to this modules's updateTime()
	@Override
	public boolean updateTime(SimTime time){
		checkOccupiedBeaconBlocks(redLineBlocks);
		checkOccupiedBeaconBlocks(greenLineBlocks);
		return true;
	}

	// If a beacon block is occupied, send that block ID and its beacon
	// information to the Train Model. The Train Model determines which
	// train is at that location and recieves the beacon information 
	// for that train.
	public void checkOccupiedBeaconBlocks(ArrayList<Block> track){
		// If the track has been initialized
		if (track.size() > 0){
			String line = track.get(0).getLine();
			int blockID = 0;
			int beaconInfo = 0;

			for (int i = 0; i < track.size(); i++){
				if (track.get(i).getBeacon() != null){
					if (track.get(i).getOccupied()){
						blockID = i;
						beaconInfo = track.get(i).getBeacon().getInfo();
						trainModel.setBeaconBlockOccupancy(line, blockID, beaconInfo);
					}
				}
			}
		}
	}

	@Override
	public boolean communicationEstablished(){
		// ... 
		return true;
	}
}
