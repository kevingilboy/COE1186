package Modules.TrackController;

import org.apache.commons.jexl2.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PLC {
	//Parent Class
	private TrackController tc;
	//PLC Variables
	private JexlEngine jexl;
	private String canProceedLogic;
	private String lightLogic;
	private String canSwitchLogic;
	private String switchingLogic;
	private String crossingLogic;
	private String maintenanceLogic;
	private String line;
	
	public PLC(TrackController tc, String plcPath){
		this.tc = tc;
		this.line = tc.associatedLine;
		parsePLC(plcPath);
		jexl = new JexlEngine();
	}
	
	public boolean parsePLC(String plcPath){
		//System.out.println("parsing file: " + plcPath);
		BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ":";
		try {
			br = new BufferedReader(new FileReader(plcPath));
			// Read from plc file and save logic expression for each
			while ((currline = br.readLine()) != null){
				String [] logic = currline.split(delimeter);
				//Remove white space
				for(int i=0; i<logic.length; i++){
					logic[i] = logic[i].replaceAll("\\s+","");
				}
				if(logic[0].equals("proceed")) {
					canProceedLogic = logic[1];
				}
				else if(logic[0].equals("light")) {
					lightLogic = logic[1];
				}
				else if(logic[0].equals("switch")) {
					canSwitchLogic = logic[1];
				}
				else if(logic[0].equals("state")) {
					switchingLogic = logic[1];
				}
				else if(logic[0].equals("crossing")) {
					crossingLogic = logic[1];
				}
				else if(logic[0].equals("maintenance")) {
					maintenanceLogic = logic[1];
				}
			}			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return true;
	}
	
	//Given path[]
	public boolean canProceedPath(int[] path){
		return vitalProceedCheckPath(canProceedLogic, path);
	}
	
	private boolean vitalProceedCheckPath(String logic, int[] path){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			if(path.length>=2) {
				if(path[0] < 0){
					context.set("nb_occupied", tc.trackModel.getBlock(line, path[2]).getOccupied());
				} else {
					context.set("nb_occupied", tc.trackModel.getBlock(line, path[1]).getOccupied());
				}
				if(path.length>=3) {
					if(path[0] < 0){
						context.set("nnb_occupied", tc.trackModel.getBlock(line, path[3]).getOccupied());
					} else {
						context.set("nnb_occupied", tc.trackModel.getBlock(line, path[2]).getOccupied());
					}
				} else {
					//to remove warnings when approaching destination
					context.set("nnb_occupied", false);
				}
			} else {
				//to remove warnings when arrived at destination
				context.set("nb_occupied", false);
				context.set("nnb_occupied", false);
			}
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}

	public boolean switchStatePath(int[] path){
		return vitalSwitchStatePath(switchingLogic, path);
	}
	
	private boolean vitalSwitchStatePath(String logic, int[] path){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			if(path.length>=2) {
				context.set("nb_state", tc.trackModel.getBlock(line, path[1]).getSwitch().getState());
				context.set("nb_port_norm", tc.trackModel.getBlock(line, path[1]).getSwitch().getPortNormal());
				context.set("nb_port_alt", tc.trackModel.getBlock(line, path[1]).getSwitch().getPortAlternate());
				if(path.length>=3) {
					context.set("nnb", path[2]);
				}
			}
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	//Given block	
	public boolean canLightBlock(int cb){
		return vitalCheckBlock(lightLogic, cb);
	}
	
	public boolean canSwitchBlock(int nb){
		return vitalCheckSwitchBlock(canSwitchLogic, nb);
	}
	
	public boolean canCrossingBlock(int cb){
		return vitalCheckBlock(crossingLogic, cb);
	}
	
	public boolean canMaintenanceBlock(int cb){
		return vitalCheckBlock(maintenanceLogic, cb);
	}
	
	private boolean vitalCheckBlock(String logic, int cb){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){
			//context.set("ppb_occupied", tc.trackModel.getBlock(line, cb-2).getOccupied());
			context.set("pb_occupied", tc.trackModel.getBlock(line, cb-1).getOccupied());
			context.set("cb_occupied", tc.trackModel.getBlock(line, cb).getOccupied());
			context.set("nb_occupied", tc.trackModel.getBlock(line, cb+1).getOccupied());
			//context.set("nnb_occupied", tc.trackModel.getBlock(line, cb+2).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheckSwitchBlock(String logic, int nb){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		int norm = tc.trackModel.getBlock(line, nb).getSwitch().getPortNormal();
		int alt = tc.trackModel.getBlock(line, nb).getSwitch().getPortAlternate();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){
			context.set("norm_occupied", tc.trackModel.getBlock(line, norm).getOccupied());
			context.set("alt_occupied", tc.trackModel.getBlock(line, alt).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
}