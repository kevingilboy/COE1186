package Modules.Mbo;

import Shared.Module;
import Shared.SimTime;

import Modules.TrainController.TrainController;
import Modules.TrainModel.TrainModel;

public class Mbo implements Module{
	public TrainController trainController;
	public TrainModel trainModel;

	public Mbo(){

	}

	public Object[][] getTrainData(String regex) {
		return null;
	}

	@Override
	public boolean updateTime(SimTime time) {

		return true;
	}
}
