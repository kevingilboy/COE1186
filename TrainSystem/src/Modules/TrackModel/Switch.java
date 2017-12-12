package Modules.TrackModel;

public class Switch{

	public static boolean STATE_NORMAL = true;
	public static boolean STATE_ALTERNATE = false;
	public static boolean STATUS_WORKING = true;
	public static boolean STATUS_NOT_WORKING = false;
	public static boolean EDGE_TYPE_HEAD = true;
	public static boolean EDGE_TYPE_TAIL = false;
	public static boolean TAIL_TYPE_NORMAL = true;
	public static boolean TAIL_TYPE_ALTERNATE = false;
	
	private boolean state;
	private boolean status;
	private boolean edge;
	private int portNormal;
	private int portAlternate;
	private Switch switchNormal;
	private Switch switchAlternate;

	private boolean tailType;

	public Switch(){
		// Default state, status, edge, and ports
		state = STATE_NORMAL;
		status = STATUS_WORKING;
		edge = EDGE_TYPE_HEAD;
		portNormal = 0;
		portAlternate = 0;
		switchNormal = null;
		switchAlternate = null;
		tailType = TAIL_TYPE_NORMAL;
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
		state = newState;

			if (state == STATE_NORMAL){
		if (status == STATUS_WORKING){
			if (tailType == TAIL_TYPE_ALTERNATE){
				if (state == STATE_NORMAL){
					switchNormal.setPortState(STATE_ALTERNATE);
					switchAlternate.setPortState(STATE_ALTERNATE);
				} else if (state == STATE_ALTERNATE){
					switchNormal.setPortState(STATE_NORMAL);
					switchAlternate.setPortState(STATE_NORMAL);
				}
			} else {
				if (state == STATE_NORMAL){
					switchNormal.setPortState(STATE_NORMAL);
					switchAlternate.setPortState(STATE_ALTERNATE);
				} else if (state == STATE_ALTERNATE){
					switchNormal.setPortState(STATE_ALTERNATE);
					switchAlternate.setPortState(STATE_NORMAL);
				}
			}
		}
	}
	
	protected void setPortState(boolean newState) {
		state = newState;
	}

	public void setStatus(boolean newStatus){
		status = newStatus;
		switchNormal.setPortStatus(newStatus);
		switchAlternate.setPortStatus(newStatus);
	}

	protected void setPortStatus(boolean newStatus){
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

	public void setSwitchNormal(Switch n){
		switchNormal = n;
	}

	public void setSwitchAlternate(Switch s){
		switchAlternate = s;
	}

	public void setTailType(boolean type){
		tailType = type;
	}

	public boolean getTailType(){
		return tailType;
	}
}