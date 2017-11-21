package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;

public class TrackController implements Module{
	//Set by other modules
	public TrackModel trackModel;
	public String associatedLine;
	public String[] associatedBlocks;
	//Subclass variables
	private TrackControllerGUI tcgui;
	private PLC tcplc;
	private String initialPLCPath = null;
	//Set internally
	private String line;
	private String section;
	private int blockId;
	private boolean hasLight;
	private boolean lightState;
	private boolean hasSwitch;
	private boolean switchState;
	private boolean hasCrossing;
	private boolean crossingState;
	private boolean status;
	private boolean occupancy;

	//Constructor
	public TrackController(String associatedLine, String[] associatedBlocks){
		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
		this.associatedLine = associatedLine;
		this.associatedBlocks = associatedBlocks;
	}
	
	public TrackController(){
		//initialize first track controller -- Maybe remove the initialization 
		//from the simulator so that CTC initializes all of them?
		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
	}

	@Override
	public boolean updateTime(SimTime time) {
		updateStates();
		return true;
	}
	//Internal Functions
	public void receiveBlockInfo(String line, int blockId){
		if (trackModel.getBlock(line, blockId).getLight() != null){
			hasLight = true;
			lightState = trackModel.getBlock(line, blockId).getLight().getState();
		}
		if (trackModel.getBlock(line, blockId).getSwitch() != null){
			hasSwitch = true;
			switchState = trackModel.getBlock(line, blockId).getSwitch().getState();
		}
		if (trackModel.getBlock(line, blockId).getCrossing() != null){
			hasCrossing = true;
			crossingState = trackModel.getBlock(line, blockId).getCrossing().getState();
		}
		status = trackModel.getBlock(line, blockId).getStatus();
		occupancy = trackModel.getBlock(line, blockId).getOccupied();
	}
	
	private void updateStates(){
		for(int i=0; i<associatedBlocks.length; i++){
			receiveBlockInfo(line, Integer.parseInt(associatedBlocks[i]));
			runPLC();
			if(Integer.parseInt(associatedBlocks[i]) == tcgui.getSelectedBlockId()){
				tcgui.displayInfo();
			}
		}
	}
	
	private void runPLC(){
		boolean holdLightState, holdSwitchState, holdCrossingState;
		if(hasLight){
			if(/*tcplc.getLightLogic()*/true){
				holdLightState = true;
			} else {
				holdLightState = false;
			}
			if(holdLightState != lightState){
				transmitLightState(line, blockId, holdLightState);
			}
		}
		if(hasSwitch){
			if(/*tcplc.getSwitchLogic()*/true){
				holdSwitchState = true;
			} else {
				holdSwitchState = false;
			}
			if(holdSwitchState != switchState){
				transmitLightState(line, blockId, holdSwitchState);
			}
		}
		if(hasCrossing){
			if(/*tcplc.getCrossingLogic()*/true){
				holdCrossingState = true;
			} else {
				holdCrossingState = false;
			}
			if(holdCrossingState != crossingState){
				transmitCrossingState(line, blockId, holdCrossingState);
			}
		}
	}
	
	private void guiUpdate(){
		tcgui.displayInfo();
	}
	
	//Getters and Setters
	public PLC getTcplc() {
		return tcplc;
	}
	
	public String[] getAssociatedBlocks(){
		return associatedBlocks;
	}
	
	//CTC Functions
	public void transmitSuggestedTrainSetpointSpeed(String trainName, int speed){
		trackModel.transmitSuggestedTrainSetpointSpeed(trainName, speed);
	}
	
	public void transmitCtcAuthority(String trainName, int authority){
		trackModel.transmitCtcAuthority(trainName, authority);
	}
	
	//-------------------------------------------------------
	//-------------------------------------------------------
	//-------------------------------------------------------
	//TODO figure out why this doesn't like passing a block??
	//-------------------------------------------------------
	//-------------------------------------------------------
	//-------------------------------------------------------
	/*public Block receiveBlockInfoForCtc(String line, int blockId){
		return trackModel.getBlock(line, blockId);
	}*/
	
	public void transmitBlockMaintenance(String line, int blockId, boolean maintenance){
		trackModel.getBlock(line, blockId).setMaintenance(maintenance);
	}
	
	//Hardware Control
	public void transmitSwitchState(String line, int blockId, boolean state){
		//logic before transmitting
		trackModel.getBlock(line, blockId).getSwitch().setState(state);
	}
	
	private void transmitLightState(String line, int blockId, boolean state){
		//logic before transmitting
		trackModel.getBlock(line, blockId).getLight().setState(state);
	}
	
	private void transmitCrossingState(String line, int blockId, boolean state){
		//logic before transmitting
		trackModel.getBlock(line, blockId).getCrossing().setState(state);
	}

	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}
}
