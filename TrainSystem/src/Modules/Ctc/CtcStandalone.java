package Modules.Ctc;

import java.util.Timer;
import java.util.TimerTask;

import Shared.SimTime;

public class CtcStandalone extends CtcCore{
	private CtcStandalone ctc;
	private Timer timer;
	private boolean running = false;
	
	public CtcStandalone() {
		ctcCore = this;
		ctc = this;
		startTime = new SimTime("07:00:00");
		currentTime = new SimTime(startTime);
		initializeBlocks();
		startGui();
		
		play();
	}
	

	
	public void initializeBlocks() {
		redBlocks = Block.parseFile("redline.csv");
		greenBlocks = Block.parseFile("greenline.csv");
	}
	
	public static void main(String[] args) {
		new CtcStandalone();
	}
	
	private class incrementTime extends TimerTask{
		public void run(){
			currentTime.incrementSecond();
			ctc.updateTime(currentTime);
		}
	}
	
	@Override
	public void pause() {
	    this.timer.cancel();
	    running = false;
	}
	@Override
	public void play() {
	    this.timer = new Timer();
	    TimerTask runSimulation = new incrementTime();
	    this.timer.schedule( runSimulation, 0, 1000/speedup );
		running = true;
	}
	@Override
	public void setSpeedup(int newSpeed) {
		speedup = newSpeed;
		if(running) {
			pause();
			play();
		}
	}
}
