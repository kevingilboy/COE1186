package Modules.TrainModel;

import Shared.Module;

import Shared.SimTime;

import java.util.HashMap;

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
	public static HashMap<Integer, Train> trainList;
	public SimTime currentTime = new SimTime("00:00:00");
	//Instantiate a GUI for this train

	public TrainModel(){
		trainList = new HashMap<Integer, Train>();
		Train train = new Train("GREEN", "Train 1", this);
		dispatchTrain(train.getTrainID(), train);
		//instantiateGUI(train);
        train.showTrainGUI();
	}

	/**
	 * updateTime is the shared clock between all submodules
	 */
	@Override
	public boolean updateTime(SimTime time) {
		currentTime = time;
		for(Train t : trainList.values()) {
			t.updateVelocity();
	        t.setValuesForDisplay();
		}
        //if(trainModelGUI.isDisplayable() == false) {
            //System.exit(0);
        //}
		return true;
	}
	
	/**
	 * Dispatch a train to a specific block (will add that functionality later)
	 * @param trainID
	 * @param train
	 */
	public void dispatchTrain(String trainID, Train train) {
		trainList.put(trainID.hashCode(), train);
		instantiateGUI(train);
	}
	
	private void instantiateGUI(Train train) {
		TrainModelGUI trainModelGUI = train.CreateNewGUI();
		trainModelGUI.addTraintoGUIList(train);
		//return trainModelGUI;
	}
	
	public Train getTrain(String ID) {
		return trainList.get(ID.hashCode());
	}
	
	
	/*public static void runTrainModel () throws InterruptedException {
        Train train = new Train("GREEN", "Train 1");
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUI = train.CreateNewGUI(train);
        train.showTrainGUI();
        
        //Constantly update velocity then the GUI
        while(true){
        	long millis = System.currentTimeMillis();
            //code to run
            train.updateVelocity();
            train.setValuesForDisplay(trainModelGUI);
            if(trainModelGUI.isDisplayable() == false) {
                System.exit(0);
            }
            Thread.sleep(1000 - millis % 1000);
        }
    }*/

	
	/*private int calcDeltaTime(SimTime start, SimTime end) {
		return start-end; // TODO: Ask Kevin how we would be doing this
	}*/
	
	/*public Train getTrainAtBlock(Block block) {
		// TODO: Should I iterate through every single entry in the hashmap to find the
		// train at the specified block?
		return;
	}*/
	
	public void setBeacon(String trainID, int beaconVal) {
		
	}
	
	public void dispatchTrainToBlock(String trainID, Train train, Block block) {
		trainList.put(trainID.hashCode(), train);
	}
	
	
	public double transmitCtcAuthority(String trainID, double authority) {
		return authority;
	}
	
	public double suggestSetpointSpeed(String trainID, double setpoint) {
		return setpoint;
	}
	
	/**
	 * Set the Emergency Brake status based on the train controller, so he will
	 * call this setter if he sets the emergency brake. Then the passenger button
	 * on the train itself will not be "pushable".
	 * 
	 * @param trainID
	 * @param eBrake
	 */
	public void setDriverEmergencyBrake(String trainID, boolean eBrake) {
		
	}
	
	/**
	 * Set the Emergency Brake status based on the passengers. In this case, the driver
	 * will be allowed to override a request for the emergency brake, but only once the 
	 * train has come to a complete stop can he deactivate the e brake
	 * @param trainID
	 * @param eBrake
	 */
	public void setPassengerEmergencyBrake(String trainID, boolean eBrake) {
		
	}
	
	/**
	 * Set by the driver only
	 * 
	 * @param trainID
	 * @param sBrake
	 */
	public void setServiceBrake(String trainID, boolean sBrake) {
		
	}
	
	/*public String[] getPosition(String trainID) {
		return ;
	}*/
	
	public void setPosition(String trainID, double[] coordinates) {
		
	}
	
	public void setPower(String trainID, double powerCommand) {
		
	}
	
	public void setDoors(String trainID, boolean left, boolean right) {
		
	}
	
	public void setLights(String trainID, boolean lights) {
		
	}
	
	public void setTemp(String trainID, int temp) {
		
	}
	
	// TODO: I don't think I need this??
	public void setActualTrainSpeed(String trainID, double actualSpeed) {
		
	}
	
	
}
