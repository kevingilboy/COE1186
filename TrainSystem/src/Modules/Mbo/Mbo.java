package Modules.Mbo;

import Shared.Module;
import Shared.SimTime;

import Modules.TrainController.TrainController;

public class Mbo implements Module{
	public TrainController trainController;

	public Mbo(){

	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}