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
	public static HashMap<String, Train> trainList;
	Train train = new Train("GREEN", "Train 1", this);
	public SimTime currentTime = new SimTime("00:00:00");
	//Instantiate a GUI for this train
    TrainModelGUI trainModelGUI = train.CreateNewGUI();

	public TrainModel(){
		trainList = new HashMap<String, Train>();
        train.showTrainGUI();
	}

	/**
	 * updateTime is the shared clock between all submodules
	 */
	@Override
	public boolean updateTime(SimTime time) {
		currentTime = time;
		train.updateVelocity();
        train.setValuesForDisplay(trainModelGUI);
        if(trainModelGUI.isDisplayable() == false) {
            //System.exit(0);
        }
		return true;
	}
	
	//public void addTrain(String trainID, Train train) {
		
	//}
	
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

	
	private void updateList() {
		
	}
	
	/*private int calcDeltaTime(SimTime start, SimTime end) {
		return start-end; // TODO: Ask Kevin how we would be doing this
	}*/
	
	/*public Train getTrainAtBlock(Block block) {
		// TODO: Should I iterate through every single entry in the hasmap to find the
		// train at the specified block?
		return;
	}*/
	
	public void setBeacon(String trainID, int beaconVal) {
		
	}
	
	public void dispatchTrainToBlock(/* Not sure what goes in here yet...*/) {
		
	}
	
	public double transmitCtcAuthority(String trainID, double authority) {
		return authority;
	}
	
	public double suggestSetpointSpeed(String trainID, double setpoint) {
		return setpoint;
	}
	
	public void setEmergencyBrake(String trainID, boolean eBrake) {
		
	}
	
	public void setServiceBrake(String trainID, boolean sBrake) {
		
	}
	
	/*public String[] getPosition(String trainID) {
		return ;
	}*/
	
	public void setPosition(String trainID, String[] coordinates) {
		
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
