package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrackModel.Block;

public class TrackController implements Module{
	//Set externally
	public TrackModel trackModel;
	public String associatedLine = "Green";
	public String[] associatedBlocks = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149","150","151"};
	//Subclass variables
	private TrackController tc;
	private TrackControllerGUI tcgui;
	private PLC tcplc;
	private String initialPLCPath = "Modules/TrackController/plc.txt";
	//Set internally per block
	private String line;
	private String section; //might not need this
	private int blockId;
	private boolean hasLight;
	private boolean lightState;
	private boolean hasSwitch;
	private boolean switchState;
	private boolean hasCrossing;
	private boolean crossingState;
	private boolean status; //might not need this
	private boolean occupancy;

	//Constructor
	/*
	public TrackController(String associatedLine, String[] associatedBlocks){
		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
		this.associatedLine = associatedLine;
		this.associatedBlocks = associatedBlocks;
	}*/
	
	public TrackController(){
		//initialize first track controller -- Maybe remove the initialization 
		//from the simulator so that CTC initializes all of them?
		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
		this.tc = this;
	}

	@Override
	public boolean updateTime(SimTime time) {
		updateStates();
		return true;
	}
	
	//Internal Functions
	private void updateStates(){
		for(int i=0; i<associatedBlocks.length; i++){
			receiveBlockInfo(associatedLine, Integer.parseInt(associatedBlocks[i])-1);
			lightsAndCrossings(Integer.parseInt(associatedBlocks[i])-1);
			if(Integer.parseInt(associatedBlocks[i]) == tcgui.getSelectedBlockId()){
				guiUpdate(tc);
			}
		}
	}
	
	public void receiveBlockInfo(String line, int blockId){
		this.line = line;
		this.blockId = blockId;
		status = trackModel.getBlock(line, blockId).getStatus();
		occupancy = trackModel.getBlock(line, blockId).getOccupied();
		if (trackModel.getBlock(line, blockId).getLight() != null){
			hasLight = true;
			lightState = trackModel.getBlock(line, blockId).getLight().getState();
		} else {
			hasLight = false;
			lightState = false;
		}
		if (trackModel.getBlock(line, blockId).getSwitch() != null){
			hasSwitch = true;
			switchState = trackModel.getBlock(line, blockId).getSwitch().getState();
		} else {
			hasSwitch = false;
			switchState = false;
		}
		if (trackModel.getBlock(line, blockId).getCrossing() != null){
			hasCrossing = true;
			crossingState = trackModel.getBlock(line, blockId).getCrossing().getState();
		} else {
			hasCrossing = false;
			crossingState = false;
		}
	}
	
	private void lightsAndCrossings(int blockNum){
		boolean holdLightState, holdCrossingState;
		if(hasLight){
			holdLightState = tcplc.canLightBlock(blockNum);
			if(holdLightState != lightState){
				transmitLightState(line, blockId, holdLightState);
			}
		}
		if(hasCrossing){
			holdCrossingState = tcplc.canCrossingBlock(blockNum);
			if(holdCrossingState != crossingState){
				transmitCrossingState(line, blockId, holdCrossingState);
			}
		}
	}
	
	private void guiUpdate(TrackController tc){
		tcgui.displayInfo(tc);
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
	// transmit as int[] authority = currentBlock, nextBlocks[]
	public void transmitCtcAuthority(String trainName, int[] authority){
		boolean canProceed = tcplc.canProceedPath(authority);
		double distAuthority = 0;
		if(canProceed){
			if(trackModel.getBlock(associatedLine, authority[1]).getSwitch() != null){
				boolean canSwitch = tcplc.canSwitchPath(authority);
				if(canSwitch){
					if(compareSwitchState(authority[0], authority[1])){//switch state not correct
						transmitSwitchState(associatedLine, authority[1], !trackModel.getBlock(line, blockId).getSwitch().getState());
						distAuthority = calcAuthDist(authority);
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					} else {
						transmitSwitchState(associatedLine, authority[1], trackModel.getBlock(line, blockId).getSwitch().getState());
						distAuthority = calcAuthDist(authority);
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					}
				} else { //has switch but cant switch state (correct or not)
					if (trackModel.getBlock(associatedLine, authority[1]).getLight() != null){
						if(trackModel.getBlock(associatedLine, authority[1]).getLight().getState() != false){
							distAuthority = calcAuthDist(authority);
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						} else { //light is red
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						}
					} else {//cant switch switch and has no light
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					}
				}
			} else { //nb doesnt have switch && can proceed
				distAuthority = calcAuthDist(authority);
				trackModel.transmitCtcAuthority(trainName, distAuthority);
			}
		} else { //cannot proceed
			trackModel.transmitCtcAuthority(trainName, distAuthority);
		}
	}
	
	public Block receiveBlockInfoForCtc(String line, int blockId){
		return trackModel.getBlock(line, blockId);
	}
	
	public void transmitBlockMaintenance(String line, int blockId, boolean maintenance){
		//check canMaintenance
		trackModel.getBlock(line, blockId).setMaintenance(maintenance);
	}
	
	public void transmitCtcSwitchState(String line, int blockId, boolean state){
		boolean canSwitch = tcplc.canSwitchBlock(blockId);
		if(canSwitch){
			transmitSwitchState(line, blockId, state);
		}
	}
	
	//Hardware Control
	private void transmitSwitchState(String line, int blockId, boolean state){
		//false == norm, true == alt
		trackModel.getBlock(line, blockId).getSwitch().setState(state);
	}
	
	private void transmitLightState(String line, int blockId, boolean state){
		//false == red, true == green
		trackModel.getBlock(line, blockId).getLight().setState(state);
	}
	
	private void transmitCrossingState(String line, int blockId, boolean state){
		//false == off, true == on
		trackModel.getBlock(line, blockId).getCrossing().setState(state);
	}
	
	private boolean compareSwitchState(int cb, int nb){
		if(trackModel.getBlock(associatedLine,cb).getSwitch().getState()){
			if(trackModel.getBlock(associatedLine,cb).getSwitch().getPortAlternate() == nb){
				return true;
			} else {
				return false;
			}
		} else { //state = false
			if(trackModel.getBlock(associatedLine,cb).getSwitch().getPortNormal() == nb){
				return true;
			} else {
				return false;
			}
		}
	} 
	
	private double calcAuthDist(int[] authority){
		double distAuth = 0;
		for(int i=1; i<authority.length; i++){
			distAuth += trackModel.getBlock(line, authority[i]).getLength();
		}
		return distAuth;
	}

	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}
}
