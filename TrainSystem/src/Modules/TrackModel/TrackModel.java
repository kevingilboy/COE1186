/**
 * COE 1186
 * TrackModel.java
 * Model of the Track System
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

import Shared.Module;
import Shared.SimTime;

import Simulator.Simulator;
import Modules.TrainModel.TrainModel;
import Modules.TrainModel.Train;
import Modules.TrainModel.Position;
import Modules.Ctc.Ctc;

import java.util.*;

public class TrackModel implements Module{
	
	// Allowable external references to
	// other modules
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
	
	// Reference to GUI
	private TrackModelGUI trackModelGUI;

	/**
	 * Called by the SimulatorGUI class to show 
	 * the GUI when this module is selected
	 */
	public void showGUI(){
		trackModelGUI.showGUI();
	}

	/**
	 * Constructor (instantiates GUI)
	 */
	public TrackModel(){	
		trackModelGUI = new TrackModelGUI(this);
	}

	/**
	 * Access a specific block on the track given
	 * the line name and block ID.
	 * 
	 * @param  line  : name of the line ("red" or "green")
	 * @param  id    : index of the block on the track
	 * @return Block : block object
	 */
	public Block getBlock(String line, int id){
		Block block = new Block();

		// get block at index (id)
		if ((line.toLowerCase()).equals("red")){
			block = redLineBlocks.get(id);
		} else if ((line.toLowerCase()).equals("green")){
			block = greenLineBlocks.get(id);
		}

		return block;
	}

	/**
	 * Access the entire ArrayList of blocks in a track
	 * specified by the track's line name. Returns null
	 * if no track has been set.
	 * @param  line  : name of the line ("red" or "green")
	 * @return track : ArrayList of Block objects
	 */
	public ArrayList<Block> getTrack(String line){
		ArrayList<Block> track = null;

		if ((line.toLowerCase()).equals("green")){
			track = greenLineBlocks;
		} else if ((line.toLowerCase()).equals("red")){
			track = redLineBlocks;
		}

		return track;
	}
 
 	/**
 	 * Sets the track for a particular line given
 	 * the line name and an ArrayList of Block objects
 	 * in that track.
 	 * @param line  : name of the line ("red" or "green")
 	 * @param track : ArrayList of Block objects
 	 */
	public void setTrack(String line, ArrayList<Block> track){

		if ((line.toLowerCase()).equals("green")){
			greenLineBlocks = track;
		} else if ((line.toLowerCase()).equals("red")){
			redLineBlocks = track;
		}

	}

	/**
	 * Externally called method to dispatch a train to
	 * a specified track.
	 * @param line    : name of the line ("red" or "green")
	 * @param trainID : identifier of the train
	 * @param pos     : the train's Position object
	 */
	public void dispatchTrain(String line, String trainID, Position pos){

		if ((line.toLowerCase()).equals("red")){
			redTrainIDs.add(trainID);
			redPositions.add(pos);
			trackModelGUI.redLineDisplay.dispatchTrain(trainID, pos);
		} else if ((line.toLowerCase()).equals("green")){
			greenTrainIDs.add(trainID);
			greenPositions.add(pos);
			trackModelGUI.greenLineDisplay.dispatchTrain(trainID, pos);
		}

	}

	/**
	 * Transmits the authority sent from the Ctc to a
	 * specific train.
	 * @param trainName : train to transmit to
	 * @param authority : authority transmited from the Ctc
	 */
	public void transmitCtcAuthority(String trainName, double authority){
		trainModel.transmitCtcAuthority(trainName, authority);
	}

	/**
	 * Transmits the suggested setpoint speed from the Ctc
	 * to a specific train.
	 * @param trainName : train to transmit to
	 * @param speed     : suggested setpoint speed
	 *                    transmited from the Ctc
	 */
	public void transmitSuggestedTrainSetpointSpeed(String trainName, double speed){
		//TODO filter speed to speed limit (I recommend questionmark colon)
		trainModel.transmitSuggestSetpointSpeed(trainName, speed);
	}

	/**
	 * If a beacon block is occupied, send that block ID and its beacon
	 * information to the Train Model. The Train Model determines which
	 * train is at that location and recieves the beacon information 
	 * for that train.
	 * 
	 * @param track : track to check occupancy on
	 */
	public void checkOccupiedBeaconBlocks(ArrayList<Block> track){

		// If the track has been initialized
		if (track.size() > 0){

			// Determine the line
			String line = track.get(0).getLine();
			int blockID = 0;
			int beaconInfo = 0;

			// Check for occupancy of beacon blocks
			for (int i = 0; i < track.size(); i++){
				if ((track.get(i).getBeacon() != null) && (track.get(i).getOccupied()) && (track.get(i).getStatus())){
					blockID = i;
					beaconInfo = track.get(i).getBeacon().getInfo();

					// Deliver beacon signal to the occupying train
					trainModel.setBeaconBlockOccupancy(line, blockID, beaconInfo);

				} else if ((track.get(i).getBeacon() == null) && (track.get(i).getOccupied())){
					blockID = i;
					beaconInfo = 0;

					// Deliver beacon signal of "0" for a non-beacon block
					trainModel.setBeaconBlockOccupancy(line, blockID, beaconInfo);

				}
			}
		}
	}

	/**
	 * When a station block gets occupied, generate ticket sales
	 * as well as the number of people waiting at the station and
	 * send the respective information to the CTC and Train Model.
	 * 
	 * @param track : track to check occupancy on
	 */
	public void checkOccupiedStationBlocks(ArrayList<Block> track){

		// If the track has been initialized
		if (track.size() > 0){

			// Determine the line
			String line = track.get(0).getLine();
			int blockID = 0;
			int numTicketSales = 0;

			for (int i = 0; i < track.size(); i++){

				// Check for occupancy of station blocks
				if ((track.get(i).getStation() != null) && (track.get(i).getOccupied()) && (track.get(i).getStatus())){
					blockID = i;

					// Get the ticket sales generated at the station
					numTicketSales = track.get(i).getStation().getTicketSales();

					// Set the number of passengers embarking the train
					// to the number of ticket sales generated (assuming all
					// passengers who purchased tickets board the train)
					trainModel.setPassengersEmbarking(blockID, line, numTicketSales);
				}
			}
		}
	}

	/**
	 * Method called by the Train Model when passengers board
	 * the train. This information is sent to the CTC
	 * for calculation of throughput.
	 * 
	 * @param trainName     : name of train to board passengers onto
	 * @param numPassengers : number of passengers boarding the train
	 */
	public void passengersBoarded(String trainName, int numPassengers){
		ctc.addPassengers(trainName, numPassengers);

		Train train = trainModel.getTrain(trainName);
		String line = train.getLine();

		if ((line.toLowerCase()).equals("green")){
			trackModelGUI.greenLineDisplay.dynamicTrackView.updatePassengers(trainName, numPassengers, TrackRenderWindow.PASSENGERS_EMBARKING);
		} else {
			trackModelGUI.redLineDisplay.dynamicTrackView.updatePassengers(trainName, numPassengers, TrackRenderWindow.PASSENGERS_EMBARKING);
		}
	}

	/**
	 * Method called by the Train Model when passengers unboard
	 * the train. This information is sent to the CTC
	 * for calculation of throughput.
	 * 
	 * @param trainName     : name of train passengers unboarding from
	 * @param numPassengers : number of passengers unboarding the train
	 */
	public void passengersUnboarded(String trainName, int numPassengers){
		ctc.removePassengers(trainName, numPassengers);

		Train train = trainModel.getTrain(trainName);
		String line = train.getLine();

		if ((line.toLowerCase()).equals("green")){

			trackModelGUI.greenLineDisplay.dynamicTrackView.updatePassengers(trainName, numPassengers, TrackRenderWindow.PASSENGERS_DISEMBARKING);

			for (int i = 0; i < greenLineBlocks.size(); i++){
				if ((greenLineBlocks.get(i).getStation() != null) && (greenLineBlocks.get(i).getOccupied()) && (greenLineBlocks.get(i).getStatus())){
					greenLineBlocks.get(i).getStation().updateWaitingPassengers();
				}
			}

		} else {

			trackModelGUI.redLineDisplay.dynamicTrackView.updatePassengers(trainName, numPassengers, TrackRenderWindow.PASSENGERS_DISEMBARKING);

			for (int i = 0; i < redLineBlocks.size(); i++){
				if ((redLineBlocks.get(i).getStation() != null) && (redLineBlocks.get(i).getOccupied()) && (redLineBlocks.get(i).getStatus())){
					redLineBlocks.get(i).getStation().updateWaitingPassengers();
				}
			}
		}
	}

	/**
	 * Updates the track GUI's dynamic display
	 * each time the track model has been updated
	 * with new information.
	 * 
	 * @param currentTime : tracks the system's current time
	 */
	public void updateDynamicDisplay(SimTime currentTime) {
		trackModelGUI.refresh();
	}

	/**
	 * Send the generated ticket sales from a station
	 * to the Ctc module.
	 * 
	 * @param numTicketSales : number of ticket sales to send
	 */
	public void sendTicketSalesToCtc(int numTicketSales){
		ctc.addTicketSales(numTicketSales);
	}

	/**
	 * Removes a specified train from the track
	 * once the train has been sent back to the yard.
	 * 
	 * @param line : name of the line ("red" or "green")
	 * @param name : identifier of the train to remove
	 */
	public void trainPoofByName(String line, String name) {

		trackModelGUI.trainPoofByName(line, name);

		if(line.equals("GREEN")) {
			getBlock(line, greenLineBlocks.size()-2).setOccupancy(false);
		} else if(line.equals("RED")) {
			getBlock(line, redLineBlocks.size()-1).setOccupancy(false);
		}		

	}

	/**
	 * Perform the specified operations after each clock 
	 * tick called by the Simulator. This method
	 * primarily checks for occupancy at specific blocks
	 * with beacons or stations.
	 * 
	 * @param  time : SimTime object used to synchronize
	 *                the system
	 * @return true if the operations for this module's
	 * response to a clock tick are successfully executed.
	 */
	@Override
	public boolean updateTime(SimTime time){

		checkOccupiedBeaconBlocks(redLineBlocks);
		checkOccupiedBeaconBlocks(greenLineBlocks);
		checkOccupiedStationBlocks(redLineBlocks);
		checkOccupiedStationBlocks(greenLineBlocks);

		return true;
	}
	
	/**
	 * Method checked by the Simulator on system
	 * startup to ensure proper communication
	 * with all modules before proceeding.
	 * 
	 * @return true if connection with the Track Model
	 *         module has been established.
	 */
	@Override
	public boolean communicationEstablished(){
		return true;
	}
}
