package Modules.Ctc;

public class Train {
	public String name;
	public int location;
	public int speed;
	public int authority;
	public Schedule schedule;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
	}

}
