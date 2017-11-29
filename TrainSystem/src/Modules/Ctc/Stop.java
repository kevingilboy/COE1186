package Modules.Ctc;

import Shared.SimTime;

public class Stop {
	public int blockId;
	public SimTime timeToDwell;
	
	public Stop(int blockId) {
		this.blockId = blockId;
		this.timeToDwell = null;
	}
}
