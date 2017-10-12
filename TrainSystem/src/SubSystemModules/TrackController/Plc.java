import java.awt.*;

public class Plc{

	/*
	 	plc.txt format:
	 	[0][lights] 	 : [1][lights logic]
	   	[2][crossing] 	 : [3][crossing logic]
	   	[4][switch]		 : [5][switch logic]
	 	[6][maintenance] : [7][maintenance logic]
	 */

	public String 	lights; 			// [0]
	public String 	lightsLogic;		// [1]
	public String 	crossing;			// [2]
	public String 	crossingLogic;		// [3]
	public String 	switchS;			// [4]
	public String 	switchLogic;		// [5]
	public String 	maintenance;		// [6]
	public String   maintenanceLogic;	// [7]

	public Plc(){
		this("", "", "", "", "", "", "", "");
	}

	public Plc(String lights,
					String 	lightsLogic,
					String 	crossing,
					String 	crossingLogic,
					String 	switchS,
					String 	switchLogic,
					String 	maintenance,
					String 	maintenanceLogic){

		this.lights 			= lights;
		this.lightsLogic 		= lightsLogic;
		this.crossing 			= crossing;
		this.crossingLogic 		= crossingLogic;
		this.switchS 			= switchS;
		this.switchLogic 		= switchLogic;
		this.maintenance		= maintenance;
		this.maintenanceLogic	= maintenanceLogic;
	}
}