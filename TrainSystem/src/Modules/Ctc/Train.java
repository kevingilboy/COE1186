package Modules.Ctc;

public class Train {
	public String name;
	public Line line;
	public int location;
	public int speed;
	public int authority;
	public Schedule schedule;
	public int passengers;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
		this.line = schedule.line;
	}

}
