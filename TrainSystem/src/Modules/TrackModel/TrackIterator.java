package Modules.TrackModel;

import java.util.*;

public class TrackIterator{
	ArrayList<Block> currTrack;
	int currBlockID;
	int prevBlockID;

	public TrackIterator(ArrayList<Block> currTrack, int currBlockID, int prevBlockID){
		this.currTrack = currTrack;
		this.currBlockID = currBlockID;
		this.prevBlockID = prevBlockID;
	}

	public int nextBlock(){
		
		Block currBlock = currTrack.get(currBlockID);
		Block prevBlock = currTrack.get(prevBlockID);
		int nextID = -1;

		// HANDLE NON-SWITCH BLOCKS
		if (currBlock.getSwitch() == null){
			int direction = 0;

			if ((currBlockID - prevBlockID) > 0){
				direction = Block.DIRECTION_INCREASING_ID;
			} else if ((currBlockID - prevBlockID) < 0){
				direction = Block.DIRECTION_DECREASING_ID;
			}

			nextID = currBlockID + direction;
		}

		// HANDLE SWITCH BLOCKS
		if (currBlock.getSwitch() != null){

			Switch s = currBlock.getSwitch();

			/* ._______________________________________________.
			 * |-------------- SWITCH CASES (lol) -------------|
			 * |                                               |
			 * |                            (3)>>              |
			 * |                      /=>[ Tail N ]========..  |
			 * |            (1)>>    /    <<(4)                |
			 * | ..=======[ Head ]<=|                          |
			 * |          <<(2)      \      (5)>>              |
			 * |                      \=>[ Tail A ]========..  |
	         * |                          <<(6)                |
	         * |_______________________________________________|                    
	         *
			 * (1) Entering switch block from HEAD edge
			 * (2) Leaving switch block from HEAD edge
			 * (3) Leaving switch block from TAIL NORMAL edge
			 * (4) Entering switch block from TAIL NORMAL edge
			 * (5) Leaving switch block from TAIL ALTERNATE edge
			 * (6) Entering switch block from TAIL ALTERNATE edge
			 *  
			 */
			
			// Case (1): Entering switch block from HEAD edge
			
			if ((s.getEdge() == Switch.EDGE_TYPE_HEAD)&&(prevBlock.getSwitch() == null)){
				System.out.println("<< SWITCH  CASE 1: Entering switch block from HEAD edge >>");

				if (s.getState() == Switch.STATE_NORMAL){
					nextID = s.getPortNormal();
				} else if (s.getState() == Switch.STATE_ALTERNATE){
					nextID = s.getPortAlternate();
				}
			}

			// Case (2): Leaving switch block from HEAD edge
			
			if ((s.getEdge() == Switch.EDGE_TYPE_HEAD)&&(prevBlock.getSwitch() != null)){
				System.out.println("<< SWITCH CASE 2: Leaving switch block from HEAD edge >>");

				int direction = 0;

				// [...][N-2][N-1][N][Head][Tail Normal = N+1]
				
				if ((currBlockID - s.getPortNormal()) > 0){
					direction = Block.DIRECTION_INCREASING_ID;
				} else if ((currBlockID - s.getPortNormal()) < 0){
					direction = Block.DIRECTION_DECREASING_ID;
				}

				nextID = currBlockID + direction;
			}

			// Case (3): Leaving switch block from TAIL NORMAL edge
			// Case (5): Leaving switch block from TAIL ALTERNATE edge
			
			if ((s.getEdge() == Switch.EDGE_TYPE_TAIL)&&(prevBlock.getSwitch() != null)){
				System.out.println("<< SWITCH CASE 3 or 5: Leaving switch block from TAIL edge >>");
				
				int direction = 0;

				if (currBlock.getSection() == prevBlock.getSection()){
					
					if ((currBlockID - prevBlockID) > 0){
						direction = Block.DIRECTION_INCREASING_ID;
					} else if ((currBlockID - prevBlockID) < 0){
						direction = Block.DIRECTION_DECREASING_ID;
					}

				} else if (currBlock.getSection() != prevBlock.getSection()){
					// HEAD block is on a different section
					
					if (currTrack.get(currBlockID + 1).getSection() == currBlock.getSection()){
						if ((currTrack.get(currBlockID + 1).getId() - currBlockID) > 0){
							direction = Block.DIRECTION_INCREASING_ID;
						} else if ((currTrack.get(currBlockID + 1).getId() - currBlockID) < 0){
							direction = Block.DIRECTION_DECREASING_ID;
						}
					} else if (currTrack.get(currBlockID - 1).getSection() == currBlock.getSection()){
						if ((currBlockID - currTrack.get(currBlockID - 1).getId()) > 0){
							direction = Block.DIRECTION_INCREASING_ID;
						} else if ((currBlockID - currTrack.get(currBlockID - 1).getId()) < 0){
							direction = Block.DIRECTION_DECREASING_ID;
						}
					} else {
						// Handle case where tail block is on a section of block length 1... :(
					}

					nextID = currBlockID + direction;
				}
			}
			 
			// Case (4): Entering switch block from TAIL NORMAL edge
			// Case (6): Entering switch block from TAIL ALTERNATE edge
			
			if ((s.getEdge() == Switch.EDGE_TYPE_TAIL)&&(prevBlock.getSwitch() == null)){
				System.out.println("<< SWITCH CASE 4 or 6: Entering switch block from TAIL edge >>");
				nextID = s.getPortNormal();
			}

			// 
			//
			//
			//
			//
			// END CASES
		}

		// return next;
		return nextID; // place holder
	}
}