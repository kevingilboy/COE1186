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
}
