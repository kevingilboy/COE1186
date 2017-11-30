package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrackModel.Block;

public class TrackController implements Module{
	//Set externally
	public TrackModel trackModel;
	public String controllerName;
	public String associatedLine;
	public String[] associatedBlocks;
	//Subclass variables
	private TrackController tc;
	public TrackControllerGUI tcgui;
	private PLC tcplc;
	private String initialPLCPath = "Modules/TrackController/init.plc";
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
	public TrackController(String associatedLine, String[] associatedBlocks, String controllerName){
		this.associatedLine = associatedLine;
		this.associatedBlocks = associatedBlocks;
		this.controllerName = controllerName;

		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
		this.tc = this;
	}
	
	/*
	public TrackController(){
		//initialize first track controller -- Maybe remove the initialization 
		//from the simulator so that CTC initializes all of them?
		this.tcgui = new TrackControllerGUI(this);
		this.tcplc = new PLC(this, initialPLCPath);
		this.tc = this;
	}*/

	@Override
	public boolean updateTime(SimTime time) {
		updateStates();
		return true;
	}
	
	//Internal Functions
	private void updateStates(){
		for(int i=0; i<associatedBlocks.length; i++){
			receiveBlockInfo(associatedLine, Integer.parseInt(associatedBlocks[i]));
			lightsAndCrossings(Integer.parseInt(associatedBlocks[i]));
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
	public void transmitSuggestedTrainSetpointSpeed(String trainName, double speed){
		trackModel.transmitSuggestedTrainSetpointSpeed(trainName, speed);
	}
	// transmit as int[] authority = currentBlock, nextBlocks[]
	public void transmitCtcAuthority(String trainName, int[] authority){
		boolean canProceed = tcplc.canProceedPath(authority);
		double distAuthority = 0;
		if(canProceed && authority.length > 1){
			//can proceed and authority >1
			boolean canSwitch = tcplc.canSwitchPath(authority);
			//System.out.println(trackModel.getBlock(associatedLine, authority[0]).getSection().substring(0,4));
			if(trackModel.getBlock(associatedLine, authority[0]).getSection().length() > 1){
				if(trackModel.getBlock(associatedLine, authority[0]).getSection().substring(0,4).equals("YARD")){	
					//if coming from the yard
					if(canSwitch && authority.length > 2){
						if(!(compareSwitchState(authority[0], authority[1]))){
							//switch state not correct
							transmitSwitchState(associatedLine, authority[0], !trackModel.getBlock(line, blockId).getSwitch().getState());
							distAuthority = calcAuthDist(authority);
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						} else { 
							//switch is in the right state
							//transmitSwitchState(associatedLine, authority[1], trackModel.getBlock(line, blockId).getSwitch().getState());
							distAuthority = calcAuthDist(authority);
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						}
					} else { 
						//has switch but cant switch state (correct or not)
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					}
				}
			} else if(trackModel.getBlock(associatedLine, authority[1]).getSwitch() != null){
				//not yard and has switch
				if(canSwitch && authority.length > 2){
					//can switch and has >2 authority
					if(!(compareSwitchState(authority[1], authority[2]))){
						//switch state not correct
						transmitSwitchState(associatedLine, authority[1], !trackModel.getBlock(line, blockId).getSwitch().getState());
						distAuthority = calcAuthDist(authority);
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					} else { 
						//switch is in the right state
						//transmitSwitchState(associatedLine, authority[1], trackModel.getBlock(line, blockId).getSwitch().getState());
						distAuthority = calcAuthDist(authority);
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					}
				} else { 
					//has switch but cant switch state (correct or not)
					trackModel.transmitCtcAuthority(trainName, distAuthority);
					/*if (trackModel.getBlock(associatedLine, authority[1]).getLight() != null){
						if(trackModel.getBlock(associatedLine, authority[1]).getLight().getState() != false){
							distAuthority = calcAuthDist(authority);
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						} else { //light is red
							trackModel.transmitCtcAuthority(trainName, distAuthority);
						}
					} else {//cant switch switch and has no light
						trackModel.transmitCtcAuthority(trainName, distAuthority);
					}*/
				}
			} else { 
				//nb doesnt have switch && not coming from yard && can proceed
				distAuthority = calcAuthDist(authority);
				trackModel.transmitCtcAuthority(trainName, distAuthority);
			}
		} else { 
			//cannot proceed or authority is only current block
			trackModel.transmitCtcAuthority(trainName, distAuthority);
		}
	}
	
	public Block receiveBlockInfoForCtc(String line, int blockId){
		return trackModel.getBlock(line, blockId);
	}
	
	public void transmitBlockMaintenance(String line, int blockId, boolean maintenance){
		boolean canMaintenance = tcplc.canMaintenanceBlock(blockId);
		if(canMaintenance){
			trackModel.getBlock(line, blockId).setMaintenance(maintenance);
		}
	}
	
	public void transmitCtcSwitchState(String line, int blockId, boolean state){
		boolean canSwitch = tcplc.canSwitchBlock(blockId);
		if(canSwitch){
			transmitSwitchState(line, blockId, state);
		}
	}
	
	//Hardware Control
	private void transmitSwitchState(String line, int blockId, boolean state){
		//false == alt, true == norm
		trackModel.getBlock(line, blockId).getSwitch().setState(state);
	}
	
	private void transmitLightState(String line, int blockId, boolean state){
		//false == red, true == green
		trackModel.getBlock(line, blockId).getLight().setState(state);
	}
	
	private void transmitCrossingState(String line, int blockId, boolean state){
		//false == on, true == off
		trackModel.getBlock(line, blockId).getCrossing().setState(state);
	}
	
	//Helper Functions
	private boolean compareSwitchState(int nb, int nnb){
		if(trackModel.getBlock(associatedLine,nb).getSwitch().getState()){
			//state = true
			if(trackModel.getBlock(associatedLine,nb).getSwitch().getPortNormal() == nnb){
				return true;
			} else {
				return false;
			}
		} else { 
			//state = false
			if(trackModel.getBlock(associatedLine,nb).getSwitch().getPortAlternate() == nnb){
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
