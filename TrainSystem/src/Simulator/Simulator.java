package Simulator;

import Shared.*;
import Shared.Module;

import Modules.Ctc.*;
import Modules.TrackController.*;
import Modules.TrackModel.*;
import Modules.TrainModel.*;
import Modules.TrainController.*;
import Modules.Mbo.*;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

/*--- REQUIRED LIBRARIES FOR HSS DARK THEME ----*/
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;
import javax.swing.UIManager;
import java.awt.Font;
import java.io.*;
/*----------------------------------------------*/

public class Simulator {
	public int speedup = 1;
	
	public int ticksPerSecond = 4;
	
	public SimTime startTime = new SimTime("07:00:00");
	public SimTime currentTime = new SimTime("07:00:00");
	public SimTime endTime = new SimTime("21:00:00");
	
	public double temperature = 69;
	public String weather = "SUNNY";
	
	private Timer timer;
	public boolean simulationRunning = false;
	private boolean timerTaskRunning = false;
	
	private SimulatorGui simulatorGui;
	private boolean simulatorGuiReady = false;
	
	private Module[] modules;
	
	protected Ctc ctc;
	protected TrackModel trackModel;
	protected TrainModel trainModel;
	protected TrainController trainController;
	protected Mbo mbo;
	
	public Simulator() throws InterruptedException {
		initializeSystemFonts();
		
		//Self declaration needed to pass simulator into new thread
		Simulator simulator = this;
		
		//Launch Simulator GUI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					simulatorGui = new SimulatorGui(simulator);
					simulatorGuiReady = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//Wait for the Simulator GUI to load
		while(!simulatorGuiReady) {
			sleep(200);
		}
		
		initializeModules();
		
		modules = new Module[]{ctc,mbo,trainController,trackModel,trainModel};
		
		initializeCommunication();
		
		//Wait for Ctc to press play...
		//play();
	}

	/*
	 * ------------------------------
	 *  MODULE INITIALIZATION
	 * ------------------------------
	 */
	private void initializeModules() {

		//Initialize all modules
		ctc = new Ctc();
		simulatorGui.moduleObjectInitialized(ModuleType.CTC);
		sleep(200);

		simulatorGui.moduleObjectInitialized(ModuleType.TRACKCONTROLLER);
		sleep(200);
		
		trackModel = new TrackModel();
		simulatorGui.moduleObjectInitialized(ModuleType.TRACKMODEL);
		sleep(200);
		
		trainModel = new TrainModel();
		simulatorGui.moduleObjectInitialized(ModuleType.TRAINMODEL);
		sleep(200);
		
		trainController = new TrainController();
		simulatorGui.moduleObjectInitialized(ModuleType.TRAINCONTROLLER);
		sleep(200);
		
		mbo = new Mbo();
		simulatorGui.moduleObjectInitialized(ModuleType.MBO);
		sleep(200);
	}
	
	private void initializeCommunication() {
		//Pass cross references
		ctc.simulator = this;
		ctc.trackModel = trackModel;
		ctc.trainModel = trainModel;
		ctc.trainController = trainController;
		simulatorGui.moduleCommunicationInitialized(ModuleType.CTC);
		simulatorGui.moduleCommunicationInitialized(ModuleType.TRACKCONTROLLER);

		//trackController.trackModel = trackModel;

		trackModel.simulator = this;
		trackModel.trainModel = trainModel;
		trackModel.ctc = ctc;
		simulatorGui.moduleCommunicationInitialized(ModuleType.TRACKMODEL);

		trainModel.trackModel = trackModel;
		trainModel.mbo = mbo;
		simulatorGui.moduleCommunicationInitialized(ModuleType.TRAINMODEL);

		trainController.trackModel = trackModel;
		trainController.trainModel = trainModel;
		simulatorGui.moduleCommunicationInitialized(ModuleType.TRAINCONTROLLER);

		mbo.trainController = trainController;
		simulatorGui.moduleCommunicationInitialized(ModuleType.MBO);
		
		//Tell each module that communication has been established
		for(Module module : modules) {
			//Wait for module to finish before proceeding
			while(!module.communicationEstablished()) {};
		}
	}	
	
	/*
	 * ------------------------------
	 *  THREAD CONTROL
	 * ------------------------------
	 */
	protected void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ------------------------------
	 *  SIMULATION TIME CONTROL
	 * ------------------------------
	 */
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
			simulatorGui.repaint();
			
			//Increment time
			currentTime.incrementTime(ticksPerSecond);
			
			//Unlock the timer
			timerTaskRunning = false;
		}
	}

	public void initializeSystemFonts(){
		try {
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("Shared/fonts/RobotoCondensed-Bold.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("Shared/fonts/RobotoCondensed-BoldItalic.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("Shared/fonts/RobotoCondensed-Italic.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("Shared/fonts/RobotoCondensed-Regular.ttf")));

		    //System.out.println("Loaded custom fonts!");
		} catch (IOException|FontFormatException e) {
			System.out.println("HssVisualizer Error: Cannot load custom font.");
		}
	}
	
	public void transmitEnableMovingBlockMode(Boolean isMovingBlockMode) {
		trainController.enableMovingBlockMode(isMovingBlockMode);
		mbo.enableMovingBlockMode(isMovingBlockMode);
	}
	
	public void trainPoofByName(String line, String name) {
		line = line.toUpperCase();
		trackModel.trainPoofByName(line, name);
		trainModel.trainPoofByName(line, name);
		trainController.trainPoofByName(line, name);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Simulator();		
	}
}
