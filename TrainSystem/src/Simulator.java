//package Simulator;

import SubSystemModules.CTC.*;
import SubSystemModules.TrackController.*;
import SubSystemModules.TrackModel.*;
import SubSystemModules.TrainModel.*;
import SubSystemModules.TrainController.*;
import SubSystemModules.MBO.*;

import Shared.SimTime;

public class Simulator {
	public final int speedup = 10;
	public final String startTime = "0600";
	public final String endTime = "0100";

	
	public Simulator() throws InterruptedException {
		CtcSample ctc = new CtcSample();
		//Mbo mbo = new Mbo();
		
		SimTime start = new SimTime(startTime);
		SimTime finish = new SimTime(endTime);
		SimTime time = new SimTime(start.hr,start.min);
		
		while(time.hr!=finish.hr || time.min!=finish.min) {
			System.out.println(time.toString());
			
			ctc.updateTime(time);
			//mbo.updateTime(time);
			
			time.incrementSecond();
			Thread.sleep(1000/speedup);
		}
		
		System.out.println("Done :)");
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Simulator();		
	}
	
}