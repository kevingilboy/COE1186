import java.util.*;

public class Reality implements MsgRouting {
	Map<String,Module> moduleObjByName = new HashMap<>();
	
	public Reality() {
		//Initialize all the objects
		CTC ctc = new CTC(this);
		TrackController trkCt = new TrackController(this);
		MBO mbo = new MBO(this);

		//Map the strings to the objects
		moduleObjByName.put("ctc", ctc);
		moduleObjByName.put("trackcontroller", trkCt);
        moduleObjByName.put("mbo", mbo);

		//Tell the ctc to send a msg
		ctc.start();			
	}
	
	@Override
	public void messageHandler(Msg msg) {
		//Get the module's class
		Module module = moduleObjByName.get(msg.dest);
		
		//Fwd it the message
		if (isMessageLegal(msg.src, msg.dest)) {
			module.messageHandler(msg);
		} else {
			System.out.printf("Illegal message from %s to %s.\n", msg.src, msg.dest);
	    }
	}
	
    private boolean isMessageLegal(String src, String dst) {
        boolean result = false;
        switch (src) {
            case "ctc":
				result = dst.equals("trackcontroller");
				break;
			case "trackcontroller":
				result = dst.equals("ctc") || dst.equals("trackmodel");
                break;
            case "trackmodel":
                result = dst.equals("trackcontroller") || dst.equals("trainmodel");
                break;
			case "trainmodel":
                result = dst.equals("trackmodel") || dst.equals("traincontroller");
                break;
            case "traincontroller":
				result = dst.equals("trainmodel") || dst.equals("mbo");
				break;
			case "mbo":
				result = dst.equals("traincontroller");
				break;
			default:
				break;
		}
		return result;
	}

	public static void main(String args[]) {
		new Reality();
	}
}
