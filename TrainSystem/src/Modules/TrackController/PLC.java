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
	private String line;
	
	public PLC(TrackController tc, String plcPath){
		this.tc = tc;
		this.line = tc.associatedLine;
		//parsePLC(plcPath);
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
	
	public boolean canProceed(int pb, int cb, int nb, int nnb){
		return vitalCheckProceed(pb, cb, nb, nnb);
	}
	
	public boolean canLight(int pb, int cb, int nb, int nnb){
		return vitalCheck(lightLogic, pb, cb, nb, nnb);
	}
	
	public boolean canSwitch(int pb, int cb, int nb, int nnb){
		return vitalCheck(switchLogic, pb, cb, nb, nnb);
	}
	
	public boolean canCrossing(int pb, int cb, int nb, int nnb){
		return vitalCheck(crossingLogic, pb, cb, nb, nnb);
	}
	
	//TODO: Do I need a separate proceed check?
	private boolean vitalCheckProceed(int pb, int cb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(proceedLogic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied());
			context.set("cb_occupied", tc.trackModel.getBlock(line, cb).getOccupied());
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied());
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
	
	private boolean vitalCheck(String logic, int pb, int cb, int nb, int nnb){
		boolean result = true;
		Expression e = jexl.createExpression(logic);
		JexlContext context = new MapContext();
		//Compute evaluation 3 times in order to assure vitality of signal
		for(int iii = 0; iii < 3; iii++){ 
			context.set("pb_occupied", tc.trackModel.getBlock(line, pb).getOccupied());
			context.set("cb_occupied", tc.trackModel.getBlock(line, cb).getOccupied());
			context.set("nb_occupied", tc.trackModel.getBlock(line, nb).getOccupied());
			context.set("nnb_occupied", tc.trackModel.getBlock(line, nnb).getOccupied());
			//Compound evaluation expression
			result &= (boolean) e.evaluate(context); 
		}
		return result;
	}
}