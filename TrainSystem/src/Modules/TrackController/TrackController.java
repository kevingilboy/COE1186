package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;
import Modules.TrackModel.TrackModel;
import Modules.TrackModel.Block;

/**
 * COE 1186
 * TrackController.java
 * Purpose: A single Wayside Object.
 *
 * @author Nick Petro
 * @version 1.0 12/14/2017
 */
public class TrackController implements Module{
	//Externally accessed
	public TrackModel trackModel;
	public TrackControllerGUI tcgui;
	public PLC tcplc;
	public String controllerName;
	public String associatedLine;
	public String[] associatedBlocks;
	public boolean isMovingBlockMode;
	public boolean manualMode;
	//Internal variables
	private TrackController tc;
	private String initialPLCPath = "Modules/TrackController/init.plc";

	/**
	 * Constructor.
	 * @param associatedLine A String
	 * @param associatedBlocks An array of Strings 
	 * @param controllerName A String 
	 */
	public TrackController(String associatedLine, String[] associatedBlocks, String controllerName){
		this.associatedLine = associatedLine;
		this.associatedBlocks = associatedBlocks;
		this.controllerName = controllerName;
		this.manualMode = false;
		this.tcgui = new TrackControllerGUI(this, this.controllerName);
		this.tcplc = new PLC(this, initialPLCPath);
		this.tc = this;
	}
	
	/**
	 * External function called for by Module.
	 * @param time The SimTime from the Simulator
	 * @return A boolean indicating that all the functions done per second have completed.
	 */
	@Override
	public boolean updateTime(SimTime time) {
		updateStates();
		return true;
	}
	
	/**
	 * Internal function to cycle through all controlled blocks and update all lights and crossings.
	 *     It will update the GUI for the current block selected by the GUI.
	 */
	private void updateStates(){
		for(int i=0; i<associatedBlocks.length; i++){
			lightsAndCrossings(Integer.parseInt(associatedBlocks[i]));
			if(Integer.parseInt(associatedBlocks[i]) == tcgui.getSelectedBlockId()){
				guiUpdate(tc);
			}
		}
	}
	
	/**
	 * Internal function to update the states of the lights and crossings on a given block.
	 * @param blockId An integer indicating the block
	 */
	private void lightsAndCrossings(int blockId){
		if(trackModel.getBlock(associatedLine, blockId).getLight() != null){
			//has light
			boolean lightState = trackModel.getBlock(associatedLine, blockId).getLight().getState();
			boolean newLightState = tcplc.canLightBlock(blockId);
			if(newLightState != lightState){
				//new state doesn't equal current state so update
				transmitLightState(blockId, newLightState);
			}
		}
		if(trackModel.getBlock(associatedLine, blockId).getCrossing() != null){
			//has crossing
			boolean crossingState = trackModel.getBlock(associatedLine, blockId).getCrossing().getState();
			boolean newCrossingState = tcplc.canCrossingBlock(blockId);
			if(newCrossingState != crossingState){
				//new state doesn't equal current state so update
				transmitCrossingState(blockId, newCrossingState);
			}
		}
	}
	
	/**
	 * Internal function to update the GUI if needed.
	 * @param tc The TrackController that has the GUI
	 */
	private void guiUpdate(TrackController tc){
		tcgui.displayInfo(tc);
	}
	
	/**
	 * External function to transmit the suggested setpoint speed to the track model after making it vital.
	 * @param trainName A String indicating the train for the given authority
	 * @param speed A double of the suggested setpoint speed from the CTC
	 */
	public void transmitSuggestedTrainSetpointSpeed(String trainName, double speed, int cb){
		int trackSpeedLimit = trackModel.getBlock(associatedLine, cb).getSpeedLimit();
		if(Math.ceil(speed) > trackSpeedLimit){
			trackModel.transmitSuggestedTrainSetpointSpeed(trainName, trackSpeedLimit);
		} else {
			trackModel.transmitSuggestedTrainSetpointSpeed(trainName, speed);
		}
	}
	
	/**
	 * External function to transmit the authority to the track model after making it vital in any mode.
	 * @param trainName A String indicating the train for the given authority
	 * @param authority An array of integers corresponding to blockId's starting with the current blockId
	 */
	public void transmitCtcAuthority(String trainName, int[] authority){
		double distAuthority = 0;
		boolean canProceed = tcplc.canProceedPath(authority);
		if(canProceed){
			//can proceed
			if (authority.length > 2){
				//can proceed and authority >2 (ie [cb,nb,...])
				if ((authority[0] >= 0) && (trackModel.getBlock(associatedLine, authority[0]).getSwitch() != null) && (trackModel.getBlock(associatedLine, authority[1]).getSwitch() != null)){
					//can proceed and currently on a switch so dont do switchStateCalc
					distAuthority = calcAuthDist(authority);
					trackModel.transmitCtcAuthority(trainName, distAuthority);
				} else if (trackModel.getBlock(associatedLine, authority[1]).getSwitch() != null){
					//can proceed and entering a switch so check state
					if (manualMode){
						//manual mode, requires toggle to complete routing
						boolean switchStateCalc = tcplc.switchStatePath(authority);
						if (trackModel.getBlock(associatedLine, authority[1]).getSwitch().getEdge()){
							//HEAD switch
							if (switchStateCalc) {
								//correct state so proceed
								distAuthority = calcAuthDist(authority);
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							} else {
								//not correct state so stop
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							}
						} else {
							//TAIL switch
							if((trackModel.getBlock(associatedLine, authority[2]).getSwitch().getState()) && (authority[1] == trackModel.getBlock(associatedLine, authority[2]).getSwitch().getPortNormal())
								|| (!trackModel.getBlock(associatedLine, authority[2]).getSwitch().getState()) && (authority[1] == trackModel.getBlock(associatedLine, authority[2]).getSwitch().getPortAlternate())){
								//correct destination
								if(switchStateCalc) {
									//correct destination, correct state
									System.out.println("here");
									distAuthority = calcAuthDist(authority);
									trackModel.transmitCtcAuthority(trainName, distAuthority);
								} else {
									//TODO not running into this state?? (could be switch at 150 error)
									//correct destination, wrong state
									trackModel.transmitCtcAuthority(trainName, distAuthority);
								}
							} else {
								//wrong state -- wait
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							}	
						}
					} else {
						//not manual mode and can proceed and entering a switch so check state
						boolean canSwitch = tcplc.canSwitchBlock(authority[1]);
						if (canSwitch){
							boolean switchStateCalc = tcplc.switchStatePath(authority);
							if (switchStateCalc) {
								//correct state
								transmitSwitchState(authority[1], trackModel.getBlock(associatedLine, authority[1]).getSwitch().getState());
								distAuthority = calcAuthDist(authority);
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							} else {
								//not correct state so switch it
								transmitSwitchState(authority[1], !trackModel.getBlock(associatedLine, authority[1]).getSwitch().getState());
								distAuthority = calcAuthDist(authority);
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							}
						} else {
							//cant switch
							boolean switchStateCalc = tcplc.switchStatePath(authority);
							System.out.print("switch state calc: ");
							System.out.println(switchStateCalc);
							if (switchStateCalc) {
								//correct state so proceed anyways
								// -- this happens when the train approaches from the tail and detects itself occupying a tail during canSwitch logic
								System.out.println("entered because it was correct state");
								distAuthority = calcAuthDist(authority);
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							} else {
								//not correct state so stop
								System.out.println("incorrect state -- waiting");
								trackModel.transmitCtcAuthority(trainName, distAuthority);
							}
						}
					}
				} else {
					//authority is >2 and can proceed and doesnt have switch
					distAuthority = calcAuthDist(authority);
					trackModel.transmitCtcAuthority(trainName, distAuthority);
				}
			} else {
				//authority is <2 and can proceed
				if (authority.length > 1){
					//authority is >1 (ie [cb,nb]) and can proceed
					distAuthority = calcAuthDist(authority);
					trackModel.transmitCtcAuthority(trainName, distAuthority);
				} else {
					//authority is <=1 (ie [cb]) and can proceed
					// -- this is when the train is approaching its destination block
					trackModel.transmitCtcAuthority(trainName, distAuthority);
				}
			}
		} else {
			//cannot proceed
			if(isMovingBlockMode){
				//MBO mode so allow the MBO to stop the trains from crashing
				distAuthority = calcAuthDist(authority);
				trackModel.transmitCtcAuthority(trainName, distAuthority);
			} else {
				trackModel.transmitCtcAuthority(trainName, distAuthority);
			}
		}
	} 
	
	/**
	 * External function to receive a Block for the CTC.
	 * @param blockId An integer indicating the block
	 * @return The Block from the track model.
	 */
	public Block receiveBlockInfoForCtc(int blockId){
		return trackModel.getBlock(associatedLine, blockId);
	}
	
	/**
	 * External function to transmit the switch state to the track model.
	 * @param blockId An integer indicating the block
	 * @param maintenance A boolean specifying if the block should go under maintenance or not
	 * @return A boolean indicating the success of the maintenance.
	 */
	public boolean transmitBlockMaintenance(int blockId, boolean maintenance){
		boolean canMaintenance = tcplc.canMaintenanceBlock(blockId);
		if(canMaintenance){
			trackModel.getBlock(associatedLine, blockId).setMaintenance(maintenance);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * External function to transmit the switch state to the track model.
	 * @param blockId An integer indicating the block
	 * @param state A boolean specifying the desired state
	 * @return A boolean indicating the success of the switch.
	 */
	public boolean transmitCtcSwitchState(int blockId, boolean state){
		boolean canSwitch = tcplc.canSwitchBlock(blockId);
		if(canSwitch || manualMode){
			transmitSwitchState(blockId, state);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * External function to transmit the light state to the track model for reservations.
	 * @param blockId An integer indicating the block
	 * @param state A boolean specifying the desired state
	 */
	//TODO fix header and write function
	public void transmitCtcReservation(int blockId, boolean reserved){
		trackModel.getBlock(associatedLine, blockId).setReserved(reserved);
	}
	
	/**
	 * Internal function to transmit the switch state to the track model.
	 * @param blockId An integer indicating the block
	 * @param state A boolean specifying the desired state
	 */
	private void transmitSwitchState(int blockId, boolean state){
		//false == alt, true == norm
		trackModel.getBlock(associatedLine, blockId).getSwitch().setState(state);
	}
	
	/**
	 * Internal function to transmit the light state to the track model.
	 * @param blockId An integer indicating the block
	 * @param state A boolean specifying the desired state
	 */
	private void transmitLightState(int blockId, boolean state){
		//false == red, true == green
		trackModel.getBlock(associatedLine, blockId).getLight().setState(state);
	}
	
	/**
	 * Internal function to transmit the crossing state to the track model.
	 * @param blockId An integer indicating the block
	 * @param state A boolean specifying the desired state
	 */
	private void transmitCrossingState(int blockId, boolean state){
		//false == on, true == off
		trackModel.getBlock(associatedLine, blockId).getCrossing().setState(state);
	}
	
	/**
	 * Internal function to translate the authority from an array of blocks to a distance in meters.
	 * @param authority An array of integers corresponding to blockId's
	 * @return A double indicating the authority as a distance.
	 */
	private double calcAuthDist(int[] authority){
		double distAuth = 0;
		for(int i=0; i<authority.length; i++){
			if(authority[i] >= 0){
				if(i == (authority.length-1)){
					//add half the length for the final block so that we ensure we stop within that block
					distAuth += (trackModel.getBlock(associatedLine, authority[i]).getLength() /2);
				} else {
					distAuth += trackModel.getBlock(associatedLine, authority[i]).getLength();
				}
				
			}
		}
		return distAuth;
	}
	
	/**
	 * External function called for by Module.
	 * @return A boolean indicating that the module is initalized.
	 */
	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}
}
