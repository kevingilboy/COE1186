/**
 * COE 1186
 * DynamicDisplay.java
 * 
 * Model of a Light.
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

public class Light{

	// Light state and status variables
	public boolean STATE_LIGHTS_ON = true;
	public boolean STATE_LIGHTS_OFF = false;
	public boolean STATUS_WORKING = true;
	public boolean STATUS_NOT_WORKING = false;

	private boolean state;
	private boolean status;

	// Coordinates of light used for
	// rendering on dynamic display
	private int x_coordinate;
	private int y_coordinate;

	public Light(){
		state = STATE_LIGHTS_ON;
		status = STATUS_WORKING;

		x_coordinate = 0;
		y_coordinate = 0;
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
		if (status == STATUS_WORKING){
			state = newState;
		} else {
			state = STATE_LIGHTS_OFF;
		}
	}

	public void setStatus(boolean newStatus){
		status = newStatus;
		if (status == STATUS_NOT_WORKING){
			setState(STATE_LIGHTS_OFF);
		}
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