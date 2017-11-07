package Modules.TrackModel;

public class Light{

	public boolean STATE_LIGHTS_ON = true;
	public boolean STATE_LIGHTS_OFF = false;
	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean state;
	private boolean status;
	
	public Light(){
		// Default state and status
		state = STATE_LIGHTS_ON;
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