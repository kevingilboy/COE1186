package Modules.TrainController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;

public class TrainController implements Module{
	public TrackModel trackModel;
	public TrainModel trainModel;

	public TrainController(){

	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}
