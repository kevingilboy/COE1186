import java.util.*;

public class Reality implements MsgRouting {
	Map<String,Module> moduleObjByName = new HashMap<>();
	
	public Reality() {
		//Initialize all the objects
		CTC ctc = new CTC(this);
		TrackController trkCt = new TrackController(this);
		
		//Map the strings to the objects
		moduleObjByName.put("ctc", ctc);
		moduleObjByName.put("trackcontroller", trkCt);
		
		//Tell the ctc to send a msg
		ctc.start();			
	}
	
	@Override
	public void messageHandler(Msg msg) {
		//Get the module's class
		Module module = moduleObjByName.get(msg.dest);
		
		//Fwd it the message
		module.messageHandler(msg);
	}
	
	
	public static void main(String args[]) {
		new Reality();
	}
}
