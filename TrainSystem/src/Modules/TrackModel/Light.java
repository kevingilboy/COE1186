package Modules.TrackModel;

public class Light{

	public boolean STATE_LIGHTS_ON = true;
	public boolean STATE_LIGHTS_OFF = false;
	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean state;
	private boolean status;

	private int x_coordinate;
	private int y_coordinate;

	public Light(){
		// Default state and status
		state = STATE_LIGHTS_ON;
		status = STATUS_WORKING;

		x_coordinate = 0;
		y_coordinate = 0;
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

	public int getXCoordinate(){
		return x_coordinate;
	}

	public int getYCoordinate(){
		return y_coordinate;
	}

	public void setXCoordinate(int x){
		x_coordinate = x;
	}

	public void setYCoordinate(int y){
		y_coordinate = y;
	}
}