
public class TrackController extends Module implements MsgRouting{
	Reality reality;
	
	public TrackController(Reality r) {
		reality = r;
	}
	
	@Override
	public void messageHandler(Msg msg) {
		System.out.println("Track Controller: "+msg.stringMsg);
	}
}
