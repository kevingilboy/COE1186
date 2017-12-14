/**
 * COE 1186
 * Block.java
 * 
 * Model of a Crossing.
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

public class Crossing{
	
	// Gate state and status variables
	public boolean STATE_GATES_OPEN = true;
	public boolean STATE_GATES_CLOSED = false;
	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean state;
	private boolean status;
	
	// Initialize gate to open and working
	public Crossing(){
		// Default state and status
		state = STATE_GATES_OPEN;
		status = STATUS_WORKING;
	}

	/**
	 * Getters and setters.
	 */
	
	public boolean getState(){
		return state;
	}

	public boolean getStatus(){
		return status;
	}

	public void setState(boolean newState){
		state = newState;
	}

	public void setStatus(boolean newStatus){
		status = newStatus;
	}
}