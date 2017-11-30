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
	private String proceedLogic;
	private String lightLogic;
	private String switchLogic;
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
					proceedLogic = logic[1];
				}
				else if(logic[0].equals("light")) {
					lightLogic = logic[1];
				}
				else if(logic[0].equals("switch")) {
					switchLogic = logic[1];
				}
				else if(logic[0].equals("crossing")) {
					crossingLogic = logic[1];
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
		return vitalCheckPath(proceedLogic, path);
	}
	
	public boolean canLightPath(int[] path){
		return vitalCheckPath(lightLogic, path);
	}
	
	public boolean canSwitchPath(int[] path){
		return vitalCheckPath(switchLogic, path);
	}
	
	public boolean canCrossingPath(int[] path){
		return vitalCheckPath(crossingLogic, path);
	}
	
	public boolean canMaintenancePath(int[] path){
		return vitalCheckPath(maintenanceLogic, path);
	}
	
	private boolean vitalCheckPath(String logic, int[] path){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("ppb_occupied", tc.trackModel.getBlock(line, path[0]-2).getOccupied());
			context.set("pb_occupied", tc.trackModel.getBlock(line, path[0]-1).getOccupied());
			context.set("cb_occupied", tc.trackModel.getBlock(line, path[0]).getOccupied());
			//TODO FROM KG - check if PATH[1] and [2] EXIST
			context.set("nb_occupied", tc.trackModel.getBlock(line, path[1]).getOccupied());
			context.set("nnb_occupied", tc.trackModel.getBlock(line, path[2]).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	//Given block
	public boolean canProceedBlock(int cb){
		return vitalCheckBlock(proceedLogic, cb);
	}
	
	public boolean canLightBlock(int cb){
		return vitalCheckBlock(lightLogic, cb);
	}
	
	public boolean canSwitchBlock(int cb){
		return vitalCheckBlock(switchLogic, cb);
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
			context.set("ppb_occupied", tc.trackModel.getBlock(line, cb-2).getOccupied());
			context.set("pb_occupied", tc.trackModel.getBlock(line, cb-1).getOccupied());
			context.set("cb_occupied", tc.trackModel.getBlock(line, cb).getOccupied());
			context.set("nb_occupied", tc.trackModel.getBlock(line, cb+1).getOccupied());
			context.set("nnb_occupied", tc.trackModel.getBlock(line, cb+2).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	
}