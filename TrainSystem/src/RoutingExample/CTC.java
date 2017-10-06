
public class CTC extends Module implements MsgRouting{
	Reality reality;

	public CTC(Reality r){
		reality = r;
	}
	
	public void start() {
		Msg msg = new Msg("ctc","trackcontroller","Hello Track Controller!");
		reality.messageHandler(msg);
	}

	@Override
	public void messageHandler(Msg msg) {
		
	}
	
}
