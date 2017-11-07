package Modules.TrainModel;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;

public class TrainModel implements Module{
	public TrackModel trackModel;

	public TrainModel(){

	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}
