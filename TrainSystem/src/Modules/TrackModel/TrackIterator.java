/**
 * COE 1186
 * TrackIterator.java
 * 
 * A helper class for the train's
 * Position class to iterate through a 
 * track based state machine logic for 
 * the track's current configuration and
 * the train's current and previous positions
 * to obtain a next position.
 *
 * NOTE: This does NOT perform the functionalities
 * of the Track Controller to determine and control
 * switch states, nor does it control the movement
 * of the train  (done by the Train Controller).
 * 
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

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

	/**
	 * Returns the ID of the next block to iterate to
	 * based on the current configuration of the track
	 * and direction the train is moving.
	 * 
	 * @return nextBlock: the next block to iterate to.
	 */
	public int nextBlock(){
		
		// Need to consider directional component for allowable iterations to NEXT, 
		// as well as returning a direction to the caller of nextBlock():
		// possibly int[] = { nextBlockID, direction(0, 1, or -1) }

		Block currBlock = currTrack.get(currBlockID);
		Block prevBlock = null;

		int nextID = -1; // Default / Error value for next block ID
		int direction = 0;

		// ---------------- STATE DESCRIPTION ---------------------------
		// 
		// 		Train is dispatched and is currently on a YARD block
		// 		moving away from the yard.
		// 
		// ---------------- NEXT STATE LOGIC ----------------------------
		// 
		// 	If the train has just been dispatched, it has 'no' previous
		// 	block ID, i.e. the previous block id is -1 (initialized by
		// 	the Position class)
		// 	
		// 	If (previous block id < 0), we are dispatching to a YARD 
		// 	block that must contain a switch to move the train to the
		// 	track. The outcoming YARD block is always at the last index
		// 	of the track and is a TAIL block of the switch, so the 
		// 	train must move to the switch's HEAD block.
		// 	
		// --------------------------------------------------------------
		
		if (prevBlockID < 0){
			nextID =  (currTrack.get(currTrack.size() - 1)).getSwitch().getPortNormal();
		}

		// ---------------- STATE DESCRIPTION ---------------------------
		// 
		// 		The train has moved onto the track and now has a 
		// 		previous block. Consider all cases for iterating through
		// 		the track.
		// 		
		// --------------------------------------------------------------

		if (prevBlockID >= 0){

			prevBlock = currTrack.get(prevBlockID);		

			// ---------------- STATE DESCRIPTION ---------------------------
			// 
			// 		Train is on a block that is not part of a switch.
			// 
			// ---------------- NEXT STATE LOGIC ----------------------------
			// 
			// 	If (previous block id < current block id), we are
			// 	moving in the INCREASING block direction.
			// 
			// 					nextID = current++
			// 
			// 	If (current block id > previous block id), we are
			// 	moving in the DECREASING block direction: 
			// 
			// 					nextID = current--
			// 
			// --------------------------------------------------------------

			if (currBlock.getSwitch() == null){

				if (prevBlockID < currBlockID){
					direction = Block.DIRECTION_INCREASING_ID;
				} else if (prevBlockID > currBlockID){
					direction = Block.DIRECTION_DECREASING_ID;
				}

				nextID = currBlockID + direction;
			}

			// ------------------ HANDLE SWITCH BLOCKS ---------------------
			//
			//      |-------------- SWITCH CASES (lol) -------------|
			//      |                                               |
			//      |                            (3)>>              |
			//      |                      /=>[ Tail N ]========..  |
			//      |            (1)>>    /    <<(4)                |
			//      | ..=======[ Head ]<=|                          |
			//      |          <<(2)      \      (5)>>              |
			//      |                      \=>[ Tail A ]========..  |
	        //      |                          <<(6)                |
	        //      |_______________________________________________|                    
	        //
			// ---------------- STATE DESCRIPTION --------------------------
			// 
			// 		Train is on a block that contains a switch edge,
			// 		HEAD, TAIL (normal), or TAIL (alternate)
			// 		
			// -------------------- CASE SUMMARY ---------------------------
			// (1) Entering switch block from HEAD edge
			// (2) Leaving switch block from HEAD edge
			// (3) Leaving switch block from TAIL NORMAL edge
			// (4) Entering switch block from TAIL NORMAL edge
			// (5) Leaving switch block from TAIL ALTERNATE edge
			// (6) Entering switch block from TAIL ALTERNATE edge
			// -------------------------------------------------------------
			
			if (currBlock.getSwitch() != null){

				Switch s = currBlock.getSwitch();

				// ---------------- STATE DESCRIPTION ---------------------------
				// 
				// 	  (1) Train is on a HEAD block moving toward a switch TAIL,
				// 	  previous block has no switch component. Decide which tail
				// 	  to move to.
				// 
				// ---------------- NEXT STATE LOGIC ----------------------------
				// 
				// 	If the switch is in the normal state, move to the normal 
				// 	port:
				// 						nextID = port normal
				// 	
				// 	If the switch is in the alternate state, move to the 
				// 	alternate port: 
				// 						nextID = port alternate	
				// 
				// --------------------------------------------------------------

				if ((s.getEdge() == Switch.EDGE_TYPE_HEAD) && (prevBlock.getSwitch() == null)){

					if (s.getState() == Switch.STATE_NORMAL){
						nextID = s.getPortNormal();
					} else if (s.getState() == Switch.STATE_ALTERNATE){
						nextID = s.getPortAlternate();
					}
				}

				// ---------------- STATE DESCRIPTION ---------------------------
				// 
				// 	  (2) Train is on a HEAD block moving away from the switch,
				// 	  previous block has a switch component. Train must move in
				// 	  direction away from the switch.
				// 	  
				// ---------------- NEXT STATE LOGIC ----------------------------
				// 	
				// 	The next block should always be in the direction away from
				// 	the normal port, regardless of which tail edge the train
				// 	was previously on.
				// 	
				// 	The normal port of the HEAD block should have an ID that
				// 	differs from the HEAD block's ID by 1.
				// 	
				// 	If (normal port id < HEAD id), block values must INCREASE* 
				// 	moving away from the switch. 
				// 	
				// 					nextID = current++
				// 					
				// 	If (normal port id > HEAD id), block values must DECREASE*				
				// 	moving away from the switch.
				// 	
				// 					nextID = current--	
				// 
				// --------------------------------------------------------------
				
				if ((s.getEdge() == Switch.EDGE_TYPE_HEAD) && (prevBlock.getSwitch() != null)){

					if (s.getPortNormal() < currBlockID){
						direction = Block.DIRECTION_INCREASING_ID;
					} else if (s.getPortNormal() > currBlockID){
						direction = Block.DIRECTION_DECREASING_ID;
					}
				
					nextID = currBlockID + direction;
				}

				// ---------------- STATE DESCRIPTION ---------------------------
				// 
				// 	  (3)(5) Train is on a TAIL block moving away from the 
				// 	  switch, previous block has a switch component. 
				// 	  
				// 	  Train must move in direction away from the switch.
				// 	  
				// --------------------------------------------------------------

				if ((s.getEdge() == Switch.EDGE_TYPE_TAIL) && (prevBlock.getSwitch() != null)){

					// ---------------- STATE DESCRIPTION ---------------------------
					// 
					// 	  (3) Train is on a TAIL block at the switch's NORMAL port
					// 	  moving away from the switch, previous block has a switch
					// 	  component.
					// 	  
					// 	  Train must move in direction away from switch.
					// 	  
					// ---------------- NEXT STATE LOGIC ----------------------------
					// 
					//    if the previous block is a switch component, it must be
					//    a head. If the previous block's NORMAL port ID is equal to
					//    the current block ID, compare the HEAD and TAIL blocks
					//    directly. 
					//    
					//    .______________._. 
					//    |   M   |  N  /   \
					//    |_______|____/  O  \
					//    				\     \
					//    	  ___________\.____\._____
					//           H  |  G  |  F  |  E    <-- (Head)
					//    	  ______|__^__|_____|_____ 
					//    		       |______ (Normal Tail)	
					//    	        <-----     
					//           (Train Direction)
					//    
					//    If (previous block id < current block id), block values
					//    must INCREASE moving away from the switch.
					//    
					//    				nextID = current++
					//    				
					//    If (previous block id > current block id), block values
					//    must DECREASE moving away from the switch.
					//    	
					//    				nextID = current--
					// 
					// --------------------------------------------------------------

					if (currTrack.get(prevBlockID).getSwitch().getPortNormal() == currBlockID){

						if (prevBlockID < currBlockID){
							direction = Block.DIRECTION_INCREASING_ID;
						} else if (prevBlockID > currBlockID){
							direction = Block.DIRECTION_DECREASING_ID;
						}

						nextID = currBlockID + direction;
					}

					// ---------------- STATE DESCRIPTION ---------------------------
					// 
					// 	  (3) Train is on a TAIL block at the switch's ALTERNATE 
					// 	  port moving away from the switch, previous block has a 
					// 	  switch component.
					// 	  
					// 	  Train must move in direction away from switch.
					// 	  
					// ---------------- NEXT STATE LOGIC ----------------------------
					// 
					//    if the previous block is a switch component, it must be
					//    a head. If the previous block's ALTERNATE port ID is equal 
					//    to the current block ID, the train must move in a
					//    direction dependent on the difference between the HEAD
					//    block's section and the current TAIL block's ID
					//    
					//                  <-----.
					//    .______________._.   \ (Train Direction)
					//    |   M   |  N  /   \	\ 
					//    |_______|____/  O  \
					//    				\     \ <-- (Alternate Tail) 
					//    	  ___________\.____\._____
					//           H  |  G  |  F  |  E    <-- (Head)
					//    	  ______|_____|_____|_____ 
					//    				
					//    				
					//    THIS HAS SO MANY CASES, these states are verified with the
					//    provided track layouts and just believe me they work....
					//    
					// --------------------------------------------------------------	

					if (currTrack.get(prevBlockID).getSwitch().getPortAlternate() == currBlockID){

						int normalPort = currTrack.get(s.getPortNormal()).getSwitch().getPortNormal();

						if ((prevBlockID < currBlockID) && (prevBlockID < normalPort)){
							direction = Block.DIRECTION_DECREASING_ID;
						} else if ((prevBlockID < currBlockID) && (prevBlockID > normalPort)){
							direction = Block.DIRECTION_INCREASING_ID;
						} else if ((prevBlockID > currBlockID) && (prevBlockID < normalPort)){
							direction = Block.DIRECTION_DECREASING_ID;
						} else if ((prevBlockID > currBlockID) && (prevBlockID > normalPort)){
							direction = Block.DIRECTION_INCREASING_ID;
						} else {
							// This should never happen...
						}

						nextID = currBlockID + direction;
					}				

				} // End cases for train on TAIL block moving away from switch

				// ---------------- STATE DESCRIPTION ---------------------------
				// 
				// 	  (4)(6) Train is on a TAIL block moving toward the 
				// 	  switch, previous block has no switch component.
				// 	  
				// 	  Train must move to the head block of the switch.
				// 	  
				// --------------------------------------------------------------
				// 	
				//    NOTE: THIS PROMPTS A REVISION OF THE ABOVE CASE FOR 
				//    LEAVING A SWITCH FROM THE HEAD EDGE. MOVING FROM AN ALT
				//    TAIL BLOCK TO A HEAD BLOCK THAT MAY BE BIDIRECTIONAL
				//    MEANS THAT THE DIRECTION MUST BE CONSIDERED.
				//    
				// ---------------- NEXT STATE LOGIC ----------------------------
				// 	
				// 	  If the current block is a tail and the previous block
				// 	  does not have a switch, then the train is moving toward a
				// 	  HEAD block regardless of the TAIL type. 
				// 	  
				// 	  The HEAD block is the normal port index of either tail
				// 	  type, so move the train to the normal port.
				// 	  
				// 	  				 next = port normal
				//
				// --------------------------------------------------------------	

				if ((s.getEdge() == Switch.EDGE_TYPE_TAIL) && (prevBlock.getSwitch() == null)){

					nextID = s.getPortNormal();
				}

			} // End cases for train on an edge of a SWITCH block
		
		} // End all track cases 

		// return next;
		return nextID;
	}
}