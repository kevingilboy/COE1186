/**
 * COE 1186
 * Station.java
 * 
 * Model of a Station.
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

import java.util.Random;

public class Station{

	// Passengers and ticket sales variables
	public int MIN_GENERATED_PASSENGERS = 10;
	public int MAX_GENERATED_PASSENGERS = 100;
	public int MIN_GENERATED_TICKET_SALES = 0;
	public int MAX_GENERATED_TICKET_SALES = 444;

	// Train door sides when arriving at
	// station
	public boolean DOOR_SIDE_RIGHT = true;
	public boolean DOOR_SIDE_LEFT = false;
	public boolean DOOR_SIDE_NONE = false;

	// Side of train the door should open when moving
	// in direction of increasing Block ID.
	private boolean doorSideDirectionPositive;

	// Side of train the door should open when moving
	// in direction of decreasing Block ID. If this
	// field is set to zero, only the positive direction
	// will be considered.
	private boolean doorSideDirectionNegative; 

	// Other station variables
	private String id;
	private int ticketSales;
	private int nextWaitingPassengers;
	private int waitingPassengers;

	public Station(){
		id = "";
		ticketSales = 0;
		updateWaitingPassengers();
		waitingPassengers = getWaitingPassengers();
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	/**
	 * Getters and Setters.
	 */
	public boolean getDoorSideDirectionPositive(){
		return doorSideDirectionPositive;
	}

	public boolean getDoorSideDirectionNegative(){
		return doorSideDirectionNegative;
	}

	public void setDoorSideDirectionPositive(boolean direction){
		this.doorSideDirectionPositive = direction;
	}

	public void setDoorSideDirectionNegative(boolean direction){
		this.doorSideDirectionNegative = direction;
	}

	public int getTicketSales(){
		// Maximum number of ticket sales is the number of people
		// waiting at the station. Not all of them might board.
		ticketSales = getWaitingPassengers();
		return ticketSales;
	}

	/**
	 * Updates the number of passengers
	 * waiting at the station.
	 */
	public void updateWaitingPassengers(){
		// Called after a train arrives at a station and embarks / disembarks passengers.
		nextWaitingPassengers = randomize(MIN_GENERATED_PASSENGERS, MAX_GENERATED_PASSENGERS);
	}

	/**
	 * Returns the next generated number of 
	 * waiting passengers after a passenger
	 * update has occured.
	 * @return passengers: next waiting passengers
	 */
	public int getWaitingPassengers(){
		int passengers = waitingPassengers;
		waitingPassengers = nextWaitingPassengers;
		return passengers;
	}

	/**
	 * Decrements the number of passengers
	 * when passengers unboard a train onto
	 * a station.
	 * 
	 * @param num : number of passengers removed
	 */
	public void removePassengers(int num){
		waitingPassengers -= num;
	}

	/**
	 * Generates a random integer between
	 * a specified minimum and maximum value.
	 * 
	 * @param  min : lower boundary of randomization
	 * @param  max : upper boundary of randomization
	 * @return random integer between min and max
	 */
	private int randomize(int min, int max){
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}