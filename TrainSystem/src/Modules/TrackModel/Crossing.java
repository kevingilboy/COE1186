package Modules.TrackModel;

public class Crossing{
	
	public boolean STATE_GATES_OPEN = true;
	public boolean STATE_GATES_CLOSED = false;
	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean state;
	private boolean status;
	
	public Crossing(){
		// Default state and status
		state = STATE_GATES_OPEN;
		status = STATUS_WORKING;
	}

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