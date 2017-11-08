package Modules.TrackModel;

import Shared.Module;
import Shared.SimTime;

import Simulator.Simulator;
import Modules.TrainModel.TrainModel;
import Modules.Ctc.Ctc;

import java.util.*;

public class TrackModel implements Module{
	
	/* references to other modules */
	public Simulator simulator;
	public Ctc ctc;
	public TrainModel trainModel;

	/* Track Model class references */
	public TrackCsvParser trackParser = new TrackCsvParser();
	private ArrayList<Block> blocks = new ArrayList<Block>();

	/* Constructor */
	public TrackModel(){
		String filePath = "Modules/TrackModel/Track Layout/GreenLineFinal.csv";
		blocks = trackParser.parse(filePath);
		trackParser.showParsedTrack();
	}

	/* update the track model from the simulation clock tick */
	@Override
	public boolean updateTime(SimTime time){
		return true;
	}

	/* Main method for standalone operation */
	public static void main(String[] args){
		new TrackModel();
	}
}
