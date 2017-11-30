// Shamelessly lifted from Kevin Le

package Modules.Mbo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import java.util.*;
import java.lang.*;

public class TrackCsvParser{

	private ArrayList<MboBlock> blocks = new ArrayList<MboBlock>();

	public TrackCsvParser(){
		// ...
	}

	public ArrayList<MboBlock> parse(String csvFile){

		BufferedReader br = null;
		String currentLine = "";
		String delimeter = ",";

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));

			while ((currentLine = br.readLine()) != null){
				String [] blockData = currentLine.split(delimeter);

				/* Parse cells 0-8 */
				String line 			= blockData[0];
				String section 			= blockData[1]; 
				int id 					= Integer.parseInt(blockData[2]); // Subtract 1 for indexing
				double length 			= Math.ceil(Double.parseDouble(blockData[3]));
				double grade 			= Double.parseDouble(blockData[4]);
				double elevation 		= Double.parseDouble(blockData[5]);
				double cumElevation 	= Double.parseDouble(blockData[6]);
				int speedLimit 			= Integer.parseInt(blockData[7]);
				int direction 			= Integer.parseInt(blockData[8]);

				/* Parse cell 15 */
				double[] x_coordinates = new double[(int)length];
				double[] y_coordinates = new double[(int)length];

				String coordDelimiter = ";";
				String xyDelimiter = "_";

				String[] coords = (blockData[15]).split(coordDelimiter);

				for (int i = 0; i < coords.length; i++){
					x_coordinates[i] = Double.parseDouble((coords[i].split(xyDelimiter))[0]);
					y_coordinates[i] = Double.parseDouble((coords[i].split(xyDelimiter))[1]);
				}

				/* Generate block from currently parsed CSV line */
				MboBlock currentBlock = new MboBlock(	line,
												section,
												id,
												length,
												grade,
												elevation,
												cumElevation,
												speedLimit,
												direction,
												x_coordinates,
												y_coordinates);

				/* Add block to list of blocks */
				blocks.add(currentBlock);

			} // end while(readline())
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

    	return blocks;
	}

	/*public void showParsedTrack(){
		if (blocks.size() > 0){
			for (int i = 0; i < blocks.size(); i++){
				System.out.print(	blocks.get(i).getLine() + "\t" +
								 	blocks.get(i).getSection() + "\t" + 
								 	blocks.get(i).getId() + "\t" +
								 	blocks.get(i).getLength() + "\t" + 
								 	blocks.get(i).getGrade() + "\t" +
								 	blocks.get(i).getElevation() + "\t" +
								 	blocks.get(i).getCumElevation() + "\t" +
								 	blocks.get(i).getSpeedLimit() + "\t" +
								 	blocks.get(i).getDirection() + "\t" +
								 	blocks.get(i).getLight() + "\t" +
								 	blocks.get(i).getCrossing() + "\t" +
								 	blocks.get(i).getStation() + "\t" +
								 	blocks.get(i).getSwitch() + "\t" +
								 	blocks.get(i).getBeacon() + "\t" +
								 	blocks.get(i).getUndergroundStatus() + "\t" +
								 	blocks.get(i).getOccupied()	+ "\n");
			}
		} else {
			System.out.println("No parsed track to display.");
		}
	}*/
}