package Modules.Ctc;

import Shared.SimTime;

public class Stop {
	public int blockId;
	public SimTime timeToDest;
	public SimTime timeToDwell;
	
	public Stop(int blockId, SimTime timeToDwell) {
		this.blockId = blockId;
		this.timeToDest = null;
		this.timeToDwell = timeToDwell;
	}
	
	public Stop(int blockId) {
		this.blockId = blockId;
		this.timeToDest = null;
		this.timeToDwell = null;
	}
	
	public Stop(SimTime timeToDwell) {
		this.blockId = -1;
		this.timeToDest = null;
		this.timeToDwell = timeToDwell;
	}
}
