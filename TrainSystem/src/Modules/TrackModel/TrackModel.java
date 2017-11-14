/**
 * Train Simulation System
 * Track Model sub-system class developed by Kevin Le
 */

package Modules.TrackModel;

import Shared.Module;
import Shared.SimTime;

import Simulator.Simulator;
import Modules.TrainModel.TrainModel;
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

	public TrackModel(){	

		// Parse CSV files and store in block collection structure
		redLineBlocks = (new TrackCsvParser()).parse(redLineFile);
		greenLineBlocks = (new TrackCsvParser()).parse(greenLineFile);

		// Instantiate dyPamic displays for each track
		//new DynamicDisplay(greenLineBlocks);
		new DynamicDisplay(redLineBlocks);

		//------------------ TESTING TRACK ITERATOR ----------------------------
		// greenLineBlocks.get(28 - 1).getSwitch().setState(Switch.STATE_ALTERNATE);
		// 
		/*
		int shift = 1;
		int prev = 149 - shift;
		int curr = 150 - shift;
		int next = nextBlock("green", curr, prev);
		
		System.out.println("prev = " + Integer.toString(prev + shift) +
			 			   ", curr = " + Integer.toString(curr + shift) + 
			 			   ", next = " + Integer.toString(next + shift));
		*/
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

	// Get the next block for the train to move to
	// based on the current block and previous blocks
	// occupied by a train (no train needs to be specified)
	public int nextBlock(String line, int currBlockID, int prevBlockID){
		TrackIterator trackIterator = null;
		int nextBlockID;

		// The TrackIterator class determines the next block based on the
		// track's current configuration of switches and track statuses
		if (line.toLowerCase() == "green"){
			trackIterator = new TrackIterator(greenLineBlocks, currBlockID, prevBlockID);
		} else if (line.toLowerCase() == "red"){
			trackIterator = new TrackIterator(redLineBlocks, currBlockID, prevBlockID);
		}

		nextBlockID = trackIterator.nextBlock();
		return nextBlockID;
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
}
