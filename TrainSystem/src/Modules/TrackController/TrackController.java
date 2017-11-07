package Modules.TrackController;

import Shared.Module;
import Shared.SimTime;

import Modules.TrackModel.TrackModel;

public class TrackController implements Module{
	public TrackModel trackModel;

	public TrackController(){

	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}
