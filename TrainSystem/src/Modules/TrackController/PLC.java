package Modules.TrackController;

import libs.org.apache.commons.jexl3.*;
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
				//Replace all white space
				for(int i=0; i<logic.length; i++){
					logic[i] = logic[i].replaceAll("\\s+","");
				}
				//Save expressions
				if(parts[0].equals("proceed")) {
					canProceedExpr = parts[1];
				}
				else if(parts[0].equals("light")) {
					canDoCrossingExpr = parts[1];
				}
				else if(parts[0].equals("switch")) {
					canSwitchExpr = parts[1];
				}
				else if(parts[0].equals("crossing")) {
					canMaintExpr = parts[1];
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
	
	public boolean canProceed(int pb, int nb, int nnb){
		return vitalCheckProceed(pb, nb, nnb);
	}
	
	public boolean canLight(int pb, int nb, int nnb){
		return vitalCheckLight(pb, nb, nnb);
		//return vitalCheck(pb,nb,nnb);
	}
	
	public boolean canSwitch(int pb, int nb, int nnb){
		return vitalCheckSwitch(pb, nb, nnb);
	}
	
	public boolean canCrossing(int pb, int nb, int nnb){
		return vitalCheckCrossing(pb, nb, nnb);
	}
	//-------------------------------------------------------
	//-------------------------------------------------------
	//-------------------------------------------------------
	//TODO ensure trackModel displays all track errors with Occupied signal
	//-------------------------------------------------------
	//-------------------------------------------------------
	//-------------------------------------------------------
	private boolean vitalCheckProceed(int pb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied();
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied();
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied();
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheck(int pb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied();
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied();
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied();
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheckLight(int pb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied();
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied();
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied();
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheckSwitch(int pb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied();
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied();
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied();
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheckCrossing(int pb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied();
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied();
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied();
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
}