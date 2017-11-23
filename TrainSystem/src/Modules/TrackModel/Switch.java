package Modules.TrackModel;

public class Switch{

	public static boolean STATE_NORMAL = true;
	public static boolean STATE_ALTERNATE = false;
	public static boolean STATUS_WORKING = true;
	public static boolean STATUS_NOT_WORKING = false;
	public static boolean EDGE_TYPE_HEAD = true;
	public static boolean EDGE_TYPE_TAIL = false;
	
	private boolean state;
	private boolean status;
	private boolean edge;
	private int portNormal;
	private int portAlternate;

	public Switch(){
		// Default state, status, edge, and ports
		state = STATE_NORMAL;
		status = STATUS_WORKING;
		edge = EDGE_TYPE_HEAD;
		portNormal = 0;
		portAlternate = 0;
	}

	public boolean getState(){
		return state;
	}

	public boolean getStatus(){
		return status;
	}

	public boolean getEdge(){
		return edge;
	}

	public int getPortNormal(){
		return portNormal;
	}

	public int getPortAlternate(){
		return portAlternate;
	}

	public void setState(boolean newState){
		// NEED TO MODIFY STATE UPDATE TO UPDATE "SWITCH" AT ALL THREE PORTS
		state = newState;
	}

	public void setStatus(boolean newStatus){
		status = newStatus;
	}

	public void setEdgeType(boolean e){
		edge = e;
	}

	public void setPortNormal(int p){
		portNormal = p;
	}

	public void setPortAlternate(int p){
		portAlternate = p;
	}
}