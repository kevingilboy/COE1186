package Modules.Ctc;

import Modules.TrackModel.Block;

public class Train {
	public String name;
	public Line line;
	public Block location;
	public int speed;
	public int authority;
	public Schedule schedule;
	public int passengers;
	
	public Train(Schedule schedule) {
		this.schedule = schedule;
		this.name = schedule.name;
		this.line = schedule.line;
		this.passengers = 0;
		
		if(line==Line.RED) {
			location = line.blocks[76];
		}
		else if(line == Line.GREEN) {
			location = line.blocks[151];
		}
	}

}
