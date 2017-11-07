package SubSystemModules.CTC;

import Shared.SimTime;
import Shared.Module;

public class CtcSample implements Module{
	public CtcSample() {
		
	}

	@Override
	public void updateTime(SimTime time) {
		System.out.println(" - From CTC: Yay");
	}
}
