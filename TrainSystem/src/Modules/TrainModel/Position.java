package Modules.TrainModel;

import Modules.TrackModel.*;
import java.util.*;

public class Position{

	public static int STARTING_BLOCK_GREEN = 0;
	public static int STARTING_BLOCK_GREEN_DIRECTION = 0;
	public static int STARTING_BLOCK_RED = 0;
	public static int STARTING_BLOCK_RED_DIRECTION = 0;

	private ArrayList<Block> track;
	
	private int previousBlockID;
	private int currentBlockID;
	private int nextBlockID;
	private double blockMeterPosition;
	private double[] coordinates; // (double x_coords, double y_coords)
	private double[] inverseCoordinates; // Used for moving in negative direction along a block

	public Position(ArrayList<Block> track){
		this.track = track;
		// Initialize position from this track's YARD (out) block,
		// where all trains should be dispatched from. Upon
		// dispatch, a train should instantiate a Position class
		// with this constructor and the track it is dispatched to
		// given by trackModel.getTrack();
		
		currentBlockID = track.get(track.size() - 1).getId();
		previousBlockID = -1; // No previous block upon dispatch, indicated by (-1) for previous
		nextBlockID = 0; // default, not used
		blockMeterPosition = 0.0;
		coordinates = track.get(currentBlockID).getCoordinatesAtMeter((int)blockMeterPosition);
		inverseCoordinates = track.get(currentBlockID).getCoordinatesAtMeter((int)((this.track).get(currentBlockID).getLength()) - 1);
	} 


	// Moves the train to a new position given a distance from 
	// the previous position
	public int moveTrain(double distance){

		blockMeterPosition += distance;
		nextBlockID = nextBlock(currentBlockID, previousBlockID);

		if ((int)blockMeterPosition >= track.get(currentBlockID).getLength()){

			previousBlockID = currentBlockID;
			currentBlockID = nextBlockID;

			blockMeterPosition -= track.get(previousBlockID).getLength();
		}

		coordinates = track.get(currentBlockID).getCoordinatesAtMeter((int)blockMeterPosition);
		inverseCoordinates = track.get(currentBlockID).getCoordinatesAtMeter(   
														(int)(track.get(currentBlockID).getLength()) - (int)blockMeterPosition - 1);

		int direction = 0;

		
		if (currentBlockID != track.get(track.size() - 1).getId()){
			if (nextBlockID > currentBlockID){
				direction = 1;
			} else if(nextBlockID < currentBlockID){
				direction = -1;
			}

			if (track.get(currentBlockID).getSwitch() != null){
				if ( (track.get(currentBlockID).getSwitch().getEdge() == Switch.EDGE_TYPE_HEAD) && 
					(previousBlockID > currentBlockID) && 
					(previousBlockID != track.get(track.size()-1).getId()) ){

					direction = -1;
				}

				if ( (track.get(currentBlockID).getSwitch().getEdge() == Switch.EDGE_TYPE_TAIL) &&
					(previousBlockID < currentBlockID) &&
					(track.get(previousBlockID).getSwitch() == null) &&
					(previousBlockID != track.get(track.size()-1).getId()) ){

					
					direction = 1;
				}
			}

		} else {
			direction = 1;
		}

		return direction;
	}

	// Returns the current coordinates of the train
	public double[] getCoordinates(){
		return coordinates;
	}

	public double[] getInverseCoordinates(){
		return inverseCoordinates;
	}

	public int getCurrentBlock(){
		return currentBlockID;
	}

	// Returns the next block ID given the current and previous block ID's
	// using the algorithms configured in the TrackIterator class
	private int nextBlock(int currBlockID, int prevBlockID){

		// The TrackIterator class determines the next block based on the
		// track's current configuration of switches and track statuses
		int nextBlockID = (new TrackIterator(track, currBlockID, prevBlockID)).nextBlock();
		return nextBlockID;
	}
}