
public class MBO extends Module implements MsgRouting{
	Reality reality;
	
	public MBO(Reality r) {
		reality = r;
	}
	
	@Override
	public void messageHandler(Msg msg) {
		System.out.println("MBO: "+msg.stringMsg);
	}
}
