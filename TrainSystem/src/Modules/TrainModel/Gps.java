package Modules.TrainModel;

import Shared.Module;
import Modules.TrackModel.*;

// TODO: change name to Position
public class Gps {
	private static String line;
	public final double[] currPos = new double[2];
	private Block currBlock;
	private Block prevBlock;
	private Block nextBlock;
	private int startDirection;
	
	
	
	public Gps(String lineColor) {
		this.line = lineColor;
		this.currPos[0] = 0;
		this.currPos[1] = 1;
	}
	
	public Gps(String lineColor, Block startBlock, Block prevBlock) {
		this.line = lineColor;
		this.currBlock = startBlock;
		this.prevBlock = prevBlock;
	}
	
	/*public double[] getPos() {
		
	}*/
	
	public void setPos() {
		
	}
	
	public Block reAssignCurrBlock() {
		prevBlock = currBlock;
		currBlock = nextBlock;
		return currBlock;
	}
}
