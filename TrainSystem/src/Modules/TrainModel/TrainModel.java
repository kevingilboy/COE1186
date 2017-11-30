/**
 * Author: Jennifer Patterson
 * Course: CoE 1186 - Software Engineering
 * Group: HashSlinging Slashers
 * Date Created: 10/3/17
 * Date Modified: 11/26/17
 */

package Modules.TrainModel;

import Shared.Module;
import Shared.SimTime;
import java.util.HashMap;

import Modules.Mbo.Mbo;
import Modules.TrackModel.*;

/**
 * Class for the overarching class of the TrainModel (which acts as the interface of the train model between other
 * objects within the train system)
 * The primary data structure implemented as the backing structure is a hash map of train IDs to train objects
 * 
 * @author Jennifer
 *
 */
public class TrainModel implements Module{
	public TrackModel trackModel;
	public Mbo mbo;
	public static HashMap<Integer, Train> trainList;
	public SimTime currentTime = new SimTime("00:00:00");
	// double powSum = 0.0;

	private boolean shown = false;

	/**
	 * Constructor of the Train Model class
	 * This class is backed by a HashMap that stores trains as objects with keys as the
	 * built in java hash of the Train ID strings
	 */
	public TrainModel(){
		trainList = new HashMap<Integer, Train>();
		// Train train = new Train(line, "Train 1", this);
	}

	/**
	 * updateTime is the shared clock between all submodules
	 */
	@Override
	public boolean updateTime(SimTime time) {
		currentTime = time;
		/*if(!shown) {
			dispatchTrain("train1","GREEN");
		}*/
		if(!trainList.isEmpty()) {
			if(!shown) { // if no GUI has yet been shown. This should only ever be true once
				int ID = trainList.keySet().iterator().next();
		        Train first = trainList.get(ID);
		        //instantiateGUI(first);
		        first.showTrainGUI();
		        shown = true;
			}
			for(Train t : trainList.values()) {
				t.updateVelocity();
		        t.setValuesForDisplay();
		        setMBOSignal(t.getTrainID());
			}
	        
		}
		return true;
	}
	
	/**
	 * Dispatch a train from the yard/make a train exist
	 * 
	 * @param trainID
	 * @param train
	 */
	public void dispatchTrain(String trainID, String line) {
		Train newTrain = new Train(line, trainID, this, this.trackModel);
		instantiateGUI(newTrain);
		trainList.put(trainID.hashCode(), newTrain);
		trackModel.dispatchTrain(line, trainID, newTrain.getPosition());
		System.out.println("(TrainModel) Train dispatched: " + trainID);
	}
	
	/**
	 * Instantiates a specified train Gui when passed a train object
	 * This was, when every train is dispatched, its GUi is already available to be displayed if the user
	 * selects a train from the list.
	 * 
	 * @param train
	 */
	private void instantiateGUI(Train train) {
		TrainModelGUI trainModelGUI = train.CreateNewGUI();
		for(Train t : trainList.values()) {
			// adds all the active trains to the new train's GUI
			if (train == t) {
				continue;
			} else {
				trainModelGUI.addTraintoGUIList(t);
			}
			// adds this new train to all the other train's GUI lists
			if(trainList.size() > 0) {
				TrainModelGUI otherGUI = t.getTrainGUI();
				otherGUI.addTraintoGUIList(train);	
			}
		}
		//trainModelGUI.addTraintoGUIList(train);
	}
	
	/**
	 * When passed an existing train's ID, will return the train object by retrieving the value at the
	 * ID's hashcode index in the HashMap
	 * 
	 * @param ID
	 * @return
	 */
	public Train getTrain(String ID) {
		return trainList.get(ID.hashCode());
	}
	
	/*private int calcDeltaTime(SimTime start, SimTime end) {
		return start-end; // TODO: Ask Kevin how we would be doing this
	}*/
	
	/**
	 * Retrieves a train at a current block. Used to determine when a train is at a beacon block
	 * 
	 * @param block
	 * @param line
	 * @return
	 */
	public Train getTrainAtBlock(int block, String line) {
		// TODO: Should I iterate through every single entry in the hashmap to find the
		// train at the specified block?
		for(Train t : trainList.values()) {
			if((t.getBlock() == block) && (t.getLine().equals(line.toUpperCase()))) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * This sets the specified train's beacon value
	 * 
	 * @param trainID
	 * @param beaconVal
	 */
	public void setBeacon(String trainID, int beaconVal) {
		this.getTrain(trainID).setBeacon(beaconVal);
	}
	
	/**
	 * Train Controller calls this method on the train model to get the train's beacon info
	 * 
	 * @param trainID
	 * @return
	 */
	public int getBeacon(String trainID) {
		return this.getTrain(trainID).getBeacon();
	}
	
	/**
	 * Track model calls this method to inform me that a train is on a beacon block, and I have
	 * to determine which train that is. I then set the beacon for that train so the train controller
	 * can get it from me
	 * 
	 * @param line
	 * @param blockId
	 * @param beaconInfo
	 */
	public void setBeaconBlockOccupancy(String line, int blockId, int beaconInfo){
	    // Figure out which train is occupying the block at 'id'
	    // set that train's beacon information to 'beaconInfo'
		Train beaconTrain = getTrainAtBlock(blockId, line);
		// something like
		setBeacon(beaconTrain.getTrainID(), beaconInfo);
		
	}
	
	/**
	 * Trainsmit the authority and receive/set it
	 * 
	 * @param trainID
	 * @param authority
	 * @return
	 */
	public double transmitCtcAuthority(String trainID, double authority) {
		this.getTrain(trainID).setAuthority(authority);
		return authority;
	}
	
	/**
	 * Receive and trainsmit the train's suggested speed
	 * 
	 * @param trainID
	 * @param setpoint
	 * @return
	 */
	public double transmitSuggestSetpointSpeed(String trainID, double setpoint) {
		this.getTrain(trainID).setSetpoint(setpoint);
		return setpoint;
	}
	
	/**
	 * For the train Controller to know the train's actual speed
	 * 
	 * @param trainID
	 * @return
	 */
	public double getVelocity(String trainID) {
		return this.getTrain(trainID).getVelocity();
	}
	
	/**
	 * For the Train Controller to receive the train's setpoint speed
	 * 
	 * @param trainID
	 * @return
	 */
	public double getSetpointSpeed(String trainID) {
		return this.getTrain(trainID).getSetpoint();
	}
	
	/**
	 * For the train controller to receive the train's authority
	 * 
	 * @param trainID
	 * @return
	 */
	public double getAuthority(String trainID) {
		return this.getTrain(trainID).getAuthority();
	}
	
	/**
	 * Called by the train controller to retrieve the train's current block as an
	 * integer ID
	 * 
	 * @param trainID
	 * @return
	 */
	public int getBlockID(String trainID) {
		return this.getTrain(trainID).getBlock();
	}
	
	/**
	 * Set the Emergency Brake status based on the train controller, so he will
	 * call this setter if he sets the emergency brake. Then the passenger button
	 * on the train itself will not be "pushable".
	 * 
	 * @param trainID
	 * @param eBrake
	 */
	public void setPassengerEmergencyBrake(String trainID, boolean eBrake) {
		this.getTrain(trainID).setEBrake(eBrake);
		//trainController.setPassengerEmergencyBrake(trainID, eBrake);
	}
	
	/**
	 * This is a getter method of the status of the emergency brake set by the passenger
	 * JButton at a specific train's GUI. The train controller will call this method every clock
	 * tick to see the status of the ebrake
	 * @param trainID
	 * @return eBrake
	 */
	public boolean getPassengerEmergencyBrake(String trainID) {
		return this.getTrain(trainID).getEBrake();
	}
	
	public void setDriverEmergencyBrake(String trainID, boolean eBrake) {
		this.getTrain(trainID).setEBrake(eBrake);
	}
	
	
	/**
	 * Set by the driver only
	 * 
	 * @param trainID
	 * @param sBrake
	 */
	public void setServiceBrake(String trainID, boolean sBrake) {
		this.getTrain(trainID).setServiceBrake(sBrake);
	}
	
	/**
	 * Returns the current position of the train to the caller of this method
	 * @param trainID
	 * @return
	 */
	public Position getPosition(String trainID) {
		return this.getTrain(trainID).getPosition();
	}
	
	/**
	 * Sets the position of train
	 * @param trainID
	 * @param position
	 */
	public void setPosition(String trainID, Position position) {
		this.getTrain(trainID).setPosition(position);
	}
	
	/**
	 * Returns the coordinates as a double array of the specified train
	 * 
	 * @param trainID
	 */
	public double[] getCoordinates(String trainID) {
		double coord[] = this.getTrain(trainID).getCoordinates();
		return coord;
		// this is where we would call setMBOCoordinates or something
	}
	
	/**
	 * Train Controller calls this on a specific train to get the current block ID
	 * 
	 * @param trainID
	 * @return
	 */
	public int getBlock(String trainID) {
		return this.getTrain(trainID).getBlock();
	}
	
	/**
	 * Called by the train controller to set a specified train's power command based on the speed
	 * limit and authority provided to it
	 * 
	 * @param trainID
	 * @param powerCommand
	 */
	public void setPower(String trainID, double powerCommand) {
		this.getTrain(trainID).setPower(powerCommand);
	}
	
	/**
	 * Called by the train controller to set a specified train's right door as open or closed
	 * 
	 * @param trainID
	 * @param right
	 */
	public void setRightDoor(String trainID, boolean right) {
		this.getTrain(trainID).setRightDoors(right);
	}
	
	/**
	 * Called by the train controller to set a specified train's left door as open or closed
	 * 
	 * @param trainID
	 * @param left
	 */
	public void setLeftDoor(String trainID, boolean left) {
		this.getTrain(trainID).setLeftDoors(left);
	}
	
	/**
	 * Called by the train controller to set a specified train's lights as on or off
	 * 
	 * @param trainID
	 * @param lights
	 */
	public void setLights(String trainID, boolean lights) {
		this.getTrain(trainID).setLights(lights);
	}
	
	/**
	 * Called by the train controller to set the train cabin's temperature
	 * 
	 * @param trainID
	 * @param temp
	 */
	public void setTemp(String trainID, int temp) {
		this.getTrain(trainID).setTemp(temp);
	}
	
	/**
	 * Called by the train controller to set a train's current arrival status
	 * 
	 * @param trainID
	 * @param status
	 * @param station
	 */
	public void setArrivalStatus(String trainID, int status, String station) {
		this.getTrain(trainID).setArrivalStatus(status);
		this.getTrain(trainID).setStation(station);
	}
	
	// TODO: not sure yet if I need this??
	public void setGPSAntenna(String trainID, boolean status) {
		this.getTrain(trainID).setGPSAntenna(status);
	}
	
	/**
	 * Called every clock tick to send the MBO a train ID, it's respective coordinates, and a checksum
	 * to act as a level of security and ensure that a position value is not corrupt
	 * @param trainID
	 */
	public void setMBOSignal(String trainID) {
		System.out.print("Sending signal to MBO...");
		mbo.receiveTrainPosition(trainID, getCoordinates(trainID), this.getTrain(trainID).checkSum());
		System.out.println("sent.");
		// call method to send the MBO an "incoming signal" status that passes a trainID
	}

	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * When the exit all field is selected from the File menu in any train's GUI, every single train model GUI
	 * is closed
	 */
	public void exitAll() {
		if(!trainList.isEmpty()) {
			for(Train t : trainList.values()) {
				// set the System.exit(DISPOSE);
				t.getTrainGUI().dispose();
			}
		}
	}

}
