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

	// Red and Green line CSV filenames to parse into ArrayList<Block> data
	String redLineFile = "Modules/TrackModel/Track Layout/RedLineFinal.csv";
	String greenLineFile = "Modules/TrackModel/Track Layout/GreenLineFinal.csv";

	DynamicDisplay greenLineDisplay;
	DynamicDisplay redLineDisplay;

	private ArrayList<String> redTrainIDs = new ArrayList<String>();
	private ArrayList<Position> redPositions = new ArrayList<Position>();

	
	private ArrayList<String> greenTrainIDs = new ArrayList<String>();
	private ArrayList<Position> greenPositions = new ArrayList<Position>();

	public TrackModel(){	

		// Parse CSV files and store in block collection structure
		redLineBlocks = (new TrackCsvParser()).parse(redLineFile);
		greenLineBlocks = (new TrackCsvParser()).parse(greenLineFile);

		// Instantiate dyPamic displays for each track
		greenLineDisplay = new DynamicDisplay(redLineBlocks);
		redLineDisplay = new DynamicDisplay(greenLineBlocks);
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

	// Respond to the Simulator's regularly call to this modules's updateTime()
	@Override
	public boolean updateTime(SimTime time){
		// ...
		return true;
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
			redLineDisplay.dispatchTrain(trainID, pos);
		} else if (line.toLowerCase() == "green"){
			greenTrainIDs.add(trainID);
			greenPositions.add(pos);
			greenLineDisplay.dispatchTrain(trainID, pos);
		}
	}

	public void transmitCtcAuthority(String trainName, int authority){
		// ...
	}

	public void transmitSuggestedTrainSetpointSpeed(String trainName, int speed){
		// ...
	}
}
