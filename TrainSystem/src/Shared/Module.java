package Shared;

public interface Module {
	/**
	 * This is called to notify each module that one
	 * unit of time has passed. Typically should be 
	 * used for calling GUI repaint(), calculating
	 * changes in location, etc.
	 */
	boolean updateTime(SimTime time);
		 
	/**
	 * This is called after all communication routes
	 * are established so that Modules can do final 
	 * initialization that requires other modules
	 * before the simulator starts to tick.
	 */
	boolean communicationEstablished();
}