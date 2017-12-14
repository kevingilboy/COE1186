/**
 * COE 1186
 * Beacon.java
 * 
 * Model of a Beacon.
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

public class Beacon{

	// Maximum 32 bits of allowable
	// information to transmit to a 
	// train upon passing.
	private int info_32bit;
	
	public Beacon(){
		info_32bit = 0;
	}

	/**
	 * Access the beacon's information.
	 * @return 32-bit integer storing
	 * the beacon's information.
	 */
	public int getInfo(){
		return info_32bit;
	}

	/**
	 * Set's te beacon's information
	 * (Called by the CSV parser upon
	 * loading a new track with pre-
	 * defined information at each 
	 * beacon).
	 * @param info_32bit : info stored
	 * at this beacon.
	 */
	public void setInfo(int info_32bit){
		this.info_32bit = info_32bit;
	}
}