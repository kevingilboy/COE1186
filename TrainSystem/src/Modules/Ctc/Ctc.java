//Kevin Gilboy

package Modules.Ctc;

import Simulator.Simulator;

import Modules.TrackController.TrackController;
import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrainController.TrainController;

public class Ctc extends CtcCore{

	public Simulator simulator = null;
	public TrackController trackController = null;
	public TrackModel trackModel = null;
	public TrainModel trainModel = null;
	public TrainController trainController = null;
	
	public Ctc() {
		ctc = this;

		initializeBlocks();
		startGui();
		while(gui==null) {
			
		}
		return;
	}
	
	@Override
	public void dispatchTrain(String name) {
		Schedule schedule = removeScheduleByName(name);
		
		Train train = new Train(schedule);
		trains.put(name, train);
		
		trainModel.dispatchTrain(name, train.line.toString().toUpperCase());
		trainController.dispatchTrain(name, train.line.toString().toUpperCase()); 
	}
	
	@Override
	public void pause() {
	    simulator.pause();
	}
	@Override
	public void play() {
	    simulator.play();
	}
	@Override
	public void setSpeedup(int newSpeed) {
	    simulator.setSpeedup(newSpeed);
	}

	@Override
	public boolean communicationEstablished() {
		// TODO Auto-generated method stub
		return true;
	}
}
