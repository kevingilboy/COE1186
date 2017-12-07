package Simulator;

import Shared.*;

import Modules.Ctc.*;
import Modules.TrackController.*;
import Modules.TrackModel.*;
import Modules.TrainModel.*;
import Modules.TrainController.*;
import Modules.Mbo.*;

import java.util.Timer;
import java.util.TimerTask;

public class Simulator {
	public int speedup = 1;
	
	public int ticksPerSecond = 4;
	
	public SimTime startTime = new SimTime("07:00:00");
	public SimTime currentTime = new SimTime("07:00:00");
	public SimTime endTime = new SimTime("21:00:00");
	
	public double temperature;
	public String weather;
	
	private Timer timer;
	private boolean simulationRunning = false;
	private boolean timerTaskRunning = false;
	
	private Module[] modules;
	private Ctc ctc;
	private TrackController trackController;
	private TrackModel trackModel;
	private TrainModel trainModel;
	private TrainController trainController;
	private Mbo mbo;
	
	public Simulator() throws InterruptedException {		
		//Initialize all modules
		ctc = new Ctc();
		//trackController = new TrackController();
		trackModel = new TrackModel();
		trainController = new TrainController();
		trainModel = new TrainModel();
		mbo = new Mbo();
		
		modules = new Module[]{ctc,mbo,trainController,trackModel,trainModel};
		
		//Pass cross references
		ctc.simulator = this;
		ctc.trackModel = trackModel;
		ctc.trainModel = trainModel;
		ctc.trainController = trainController;

		//trackController.trackModel = trackModel;

		trackModel.simulator = this;
		trackModel.trainModel = trainModel;
		trackModel.ctc = ctc;

		trainModel.trackModel = trackModel;
		trainModel.mbo = mbo;

		trainController.trackModel = trackModel;
		trainController.trainModel = trainModel;

		mbo.trainController = trainController;
		
		temperature = 69;
		weather = "SUNNY";
		
		//Tell each module that communication has been established
		for(Module module : modules) {
			//Wait for module to finish before proceeding
			while(!module.communicationEstablished()) {};
		}
		
		//Wait for Ctc to press play...
		//play();
	}
	
	public void pause() {
		//Cancel the timer
	    this.timer.cancel();
	    
	    simulationRunning = false;
	}
	
	public void play() {
		//Create the timer
	    this.timer = new Timer();
	    TimerTask runSimulation = new incrementTime();
	    this.timer.schedule( runSimulation, 0, (1000/speedup)/ticksPerSecond );
	    
	    simulationRunning = true;
	}
	
	public void setSpeedup(int newSpeed) {
		//Store the new speed
		speedup = newSpeed;
		
		//If running, restart it to implement new speedup
		if(simulationRunning) {
			pause();
			play();
		}
	}
	
	private class incrementTime extends TimerTask{
		public void run(){
			//Make sure previous second has finished before continuing
			if(timerTaskRunning) {
				return;
			}
			
			//Lock the timer
			timerTaskRunning = true;
			
			//Update all modules
			for(Module module : modules) {
				//Wait for module to finish updating before proceeding
				while(!module.updateTime(currentTime)) {};
			}
			trackModel.updateDynamicDisplay(currentTime);
			
			//Increment time
			currentTime.incrementTime(ticksPerSecond);
			
			//Unlock the timer
			timerTaskRunning = false;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Simulator();		
	}
}
