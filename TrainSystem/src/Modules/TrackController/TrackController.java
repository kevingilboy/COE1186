package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;

public class TrackController implements Module{
	//Set by other modules
	public TrackModel trackModel;
	public String associatedLine;
	public int[] associatedBlocks;
	//Subclass variables
	private TrackControllerGUI tcgui;
	private PLC tcplc;
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
	public TrackController(String associatedLine, int[] associatedBlocks){
		tcgui = new TrackControllerGUI();
		tcplc = new PLC();
		this.associatedLine = associatedLine;
		this.associatedBlocks = associatedBlocks;
		
	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
	//Internal Functions
	private void receiveBlockInfo(String line, int blockId){
		hasLight = trackModel.getBlock(line, blockId);
		lightState = trackModel.getBlock(line, blockId);
		hasSwitch = trackModel.getBlock(line, blockId);
		switchState = trackModel.getBlock(line, blockId);
		hasCrossing = trackModel.getBlock(line, blockId);
		crossingState = trackModel.getBlock(line, blockId);
		status = trackModel.getBlock(line, blockId);
		occupancy = trackModel.getBlock(line, blockId);
	}
	
	private void updateStates(){
		
	}
	
	private boolean uploadPLC(String plcPath){
		return true;
	}
	
	private void runPLC(boolean occupancy){
		
	}
	
	private void guiUpdate(){
		
	}
	
	//CTC Functions
	public void transmitSuggestedTrainSetpointSpeed(String trainName, int speed){
		trackModel.transmitSuggestedTrainSetpointSpeed(trainName, speed);
	}
	
	public void transmitCtcAuthority(String trainName, int authority){
		trackModel.transmitCtcAuthority(trainName, authority);
	}
	
	public Block receiveBlockInfoForCtc(String line, int blockId){
		return trackModel.getBlock(line, blockId);
	}
	
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
}
