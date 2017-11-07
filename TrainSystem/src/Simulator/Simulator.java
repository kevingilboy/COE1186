package Simulator;

import Shared.SimTime;

import Modules.Ctc.Ctc;
import Modules.TrackController.TrackController;
import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrainController.TrainController;
import Modules.Mbo.Mbo;

import java.util.Timer;
import java.util.TimerTask;

public class Simulator {
	public int speedup = 1;
	public SimTime startTime = new SimTime("07:00:00");
	public SimTime currentTime = new SimTime("07:00:00");
	public SimTime endTime = new SimTime("21:00:00");
	
	private Timer timer;
	private boolean running = false;
	
	private Ctc ctc;
	private TrackController trackController;
	private TrackModel trackModel;
	private TrainModel trainModel;
	private TrainController trainController;
	private Mbo mbo;

	
	public Simulator() throws InterruptedException {		
		//Initialize all modules
		ctc = new Ctc();
		trackController = new TrackController();
		trackModel = new TrackModel();
		trainController = new TrainController();
		trainModel = new TrainModel();
		mbo = new Mbo();
		
		//Pass cross references
		ctc.simulator = this;
		ctc.trackModel = trackModel;
		ctc.trainModel = trainModel;
		ctc.trainController = trainController;

		trackController.trackModel = trackModel;

		trackModel.simulator = this;
		trackModel.trainModel = trainModel;
		trackModel.ctc = ctc;

		trainModel.trackModel = trackModel;

		trainController.trackModel = trackModel;
		trainController.trainModel = trainModel;

		mbo.trainController = trainController;
		
		//Start timer
		play();
	}
	
	public void pause() {
	    this.timer.cancel();
	    
	    running = false;
	}
	
	public void play() {
	    this.timer = new Timer();
	    TimerTask runSimulation = new incrementTime();
	    this.timer.schedule( runSimulation, 0, 1000/speedup );
	    
	    running = true;
	}
	
	public void setSpeedup(int newSpeed) {
		speedup = newSpeed;
		if(running) {
			pause();
			play();
		}
	}
	
	private class incrementTime extends TimerTask{
		public void run(){
			//System.out.println(currentTime.toString());
			
			//Update all modules
			ctc.updateTime(currentTime);
			
			//Increment time
			currentTime.incrementSecond();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Simulator();		
	}
}
