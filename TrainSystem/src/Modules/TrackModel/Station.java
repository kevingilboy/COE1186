package Modules.TrackModel;

import java.util.Random;

public class Station{

	public int MIN_GENERATED_TICKET_SALES = 10;
	public int MAX_GENERATED_TICKET_SALES = 50;
	public boolean DOOR_SIDE_RIGHT = true;
	public boolean DOOR_SIDE_LEFT = false;
	public boolean DOOR_SIDE_NONE = false;

	private String id;
	private int ticketSales;

	// Side of train the door should open when moving
	// in direction of increasing Block ID.
	private boolean doorSideDirectionPositive;

	// Side of train the door should open when moving
	// in direction of decreasing Block ID. If this
	// field is set to zero, only the positive direction
	// will be considered.
	private boolean doorSideDirectionNegative; 

	public Station(){
		id = "";
		ticketSales = 0;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

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
		ticketSales = randomizeTicketSales(MIN_GENERATED_TICKET_SALES, MAX_GENERATED_TICKET_SALES);
		return ticketSales;
	}

	private int randomizeTicketSales(int min, int max){
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

}