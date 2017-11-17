package Modules.TrackController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PLC{
	//PLC Variables
	private int[] lightLogic;
	private int[] switchLogic;
	private int[] crossingLogic;
	
	public PLC(){
		
	}
	
	public boolean parsePLC(String plcPath){
		System.out.println("parsing file: " + plcPath);
		BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ";";

		try {
			br = new BufferedReader(new FileReader(plcPath));
			int location = 0;
			
			// Read from plc file and create logic for each line, then
			// add each statement to an arraylist of logic statements
			while ((currline = br.readLine()) != null){
				String [] logicStatement = currline.split(delimeter);
				location++;
				for(int i=0; i<6; i++){
					logicStatement[i] = logicStatement[i].replaceAll("\\s+","");
					switch(location){
						case 1: lightLogic[i] = Integer.parseInt(logicStatement[i]);
							break;
						case 2: switchLogic[i] = Integer.parseInt(logicStatement[i]);
							break;
						case 3: crossingLogic[i] = Integer.parseInt(logicStatement[i]);
							break;
						default: return false;
							break;
					}
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
	
	public int[] getLightLogic(){
		return lightLogic;
	}
	
	public int[] getSwitchLogic(){
		return switchLogic;
	}
	
	public int[] getCrossingLogic(){
		return crossingLogic;
	}
}