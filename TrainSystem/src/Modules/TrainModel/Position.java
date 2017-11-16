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
	private int direction; 
	private double blockMeterPosition;
	private double[] coordinates; // (double x_coords, double y_coords)
	// private int nextBlockID;

	public Position(ArrayList<Block> track){
		this.track = track;

		// Initialize position from this track's YARD (out) block,
		// where all trains should be dispatched from. Upon
		// dispatch, a train should instantiate a Position class
		// with this constructor and the track it is dispatched to
		// given by trackModel.getTrack();
		
		currentBlockID = track.get(track.size() - 1).getId();
		previousBlockID = -1;
		direction = 1;
		blockMeterPosition = 0.0;
		coordinates = track.get(currentBlockID).getCoordinatesAtMeter((int)blockMeterPosition);
	} 


	// Moves the train to a new position given a distance from 
	// the previous position
	public void moveTrain(double distance){

		if (direction == 1){
			blockMeterPosition += distance;

			if ((int)blockMeterPosition >= track.get(currentBlockID).getLength()){
				System.out.println("\n~~~~SECTION UPDATE (+1)~~~~");
				int nextBlockID = nextBlock(currentBlockID, previousBlockID);
				previousBlockID = currentBlockID;
				currentBlockID = nextBlockID;

				System.out.print("block: "); System.out.print(track.get(currentBlockID).getSection());
				System.out.print(track.get(currentBlockID).getId());

				System.out.print(", (before)blockMeterPosition: "); System.out.print(blockMeterPosition);
				blockMeterPosition -= track.get(previousBlockID).getLength();
				
				System.out.print(", (edge)block length(): "); System.out.print(track.get(currentBlockID).getLength());
				System.out.print(", blockMeterPosition: "); System.out.println(blockMeterPosition);
			}
				
			System.out.print("block: "); System.out.print(track.get(currentBlockID).getSection());
			System.out.print(track.get(currentBlockID).getId());
			System.out.print(", block length(): "); System.out.print(track.get(currentBlockID).getLength());
			System.out.print(", blockMeterPosition: "); System.out.println(blockMeterPosition);

		} else if (direction == -1){
			blockMeterPosition -= distance;

			if ((int)blockMeterPosition < 0){
				System.out.println("~~~~SECTION UPDATE (-1)~~~~");
				int nextBlockID = nextBlock(currentBlockID, previousBlockID);
				previousBlockID = currentBlockID;
				currentBlockID = nextBlockID;
				blockMeterPosition += track.get(previousBlockID).getLength();
				System.out.print("(edge)block length(): "); System.out.print(track.get(currentBlockID).getLength());
				System.out.print(", blockMeterPosition: "); System.out.println(blockMeterPosition);

			}

			System.out.print("block length(): "); System.out.print(track.get(currentBlockID).getLength());
			System.out.print(", blockMeterPosition: "); System.out.println(blockMeterPosition);
		
		}

		coordinates = track.get(currentBlockID).getCoordinatesAtMeter((int)blockMeterPosition);
	}

	// Returns the current coordinates of the train
	public double[] getCoordinates(){
		return coordinates;
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