/**
 * GPS class that handles the trains current position relative to the front of the train
 * @author Jennifer Patterson
 *
 */
public class Gps {
	//send broken down parts for each of the block objects (prev, current and next), with only one line field for each of
	// the different blocks
	private String line;
	private Block prevBlockID;
	private String sectionPrev;
	
	private Block currentBlockID;
	private double currBlockLength;
	private double metersIn;
	
	private Block nextBlockID;
	private double nextBlockLength;
	
	
	
	/**
	 * Constructor variable for the Gps signal
	 * 
	 * @param prevBlockID
	 */
	public Gps () {
		//this.prevBlockID = prevBlockID;
		this.currentBlockID = trackModel.getCurrentBlock();
		this.nextBlockID = trackModel.getNextBlock();
		this.metersIn = 0;
		this.line = null;
		this.section = null;
	}
	
	private void updatePrevBlock() {
		// prevBlock = currentBlock;
	}
	
	private void updateCurrentBlock() {
		// currentBlock = nextBlock;
	}
	
	/**
	 * TODO: Will need to get this from the Track model
	 */
	private Block getNextBlock(Block currentBlock) {
		// need to call a function from the track model class that given a current
		// block will send me back a next block object
	}
	
	private Gps setGps() {
		return this;
	}
}
