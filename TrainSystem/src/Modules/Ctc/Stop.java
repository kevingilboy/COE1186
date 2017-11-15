package Modules.Ctc;

import Modules.TrackModel.Block;
import Shared.SimTime;

public class Stop {
	public Block block;
	public SimTime timeToDwell;
	
	public Stop(Block block) {
		this.block = block;
		this.timeToDwell = null;
	}
}
