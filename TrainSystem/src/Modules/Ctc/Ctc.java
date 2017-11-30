//Kevin Gilboy

package Modules.Ctc;

import Simulator.Simulator;

import Modules.TrackController.TrackController;
import Modules.TrackModel.TrackModel;
import Modules.TrainModel.TrainModel;
import Modules.TrainController.TrainController;

public class Ctc extends CtcCore{

	public Simulator simulator = null;
	public TrackController[] trackControllers;
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
		//TODO prettify
		trackControllers = new TrackController[4];
		trackControllers[0] = new TrackController("Green",new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","127","128","129","130","131","132","133","134","135","136","137","138","139","140","141","142","143","144","145","146","147","148","149"},"G1");
		trackControllers[1] = new TrackController("Green",new String[]{"53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","118","119","120","121","122","123","124","125","126","127","128","129","150","151"},"G2");
		trackControllers[2] = new TrackController("Red",  new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","76"},"R1");
		trackControllers[3] = new TrackController("Red",  new String[]{"33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75"},"R2");
		for(TrackController tc : trackControllers) {
			tc.trackModel = trackModel;
		}
		
		return true;
	}
	
	public TrackController getWaysideOfBlock(Line line, int blockNum) {
		if(line==Line.GREEN) {
			if( (0 <= blockNum && blockNum < 53) || (127 <= blockNum && blockNum < 150) ) {
				return trackControllers[0];
			}
			else if( (53 <= blockNum && blockNum < 127) || (150 <= blockNum && blockNum < 152) ){
				return trackControllers[1];
			}
		}
		else if(line==Line.RED) {
			if( (0 <= blockNum && blockNum < 33) || (76 == blockNum) ) {
				return trackControllers[2];
			}
			else if(33 <= blockNum && blockNum < 76){
				return trackControllers[3];	
			}
		}
		
		return null;
	}
}
