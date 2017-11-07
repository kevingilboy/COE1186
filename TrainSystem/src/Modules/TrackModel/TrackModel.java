package Modules.TrackModel;

import Shared.Module;
import Shared.SimTime;

import Simulator.Simulator;
import Modules.TrainModel.TrainModel;
import Modules.Ctc.Ctc;

public class TrackModel implements Module{
	public Simulator simulator;
	public Ctc ctc;
	public TrainModel trainModel;

	public TrackModel(){

	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}
