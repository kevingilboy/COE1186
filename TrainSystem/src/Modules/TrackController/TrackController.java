package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;

public class TrackController implements Module{
	//Set by other modules
	public TrackModel trackModel;
	public String associatedLine = "Green";
	public String[] associatedBlocks = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151","152"};
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
