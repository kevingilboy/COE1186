package Modules.TrackModel;

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

	private ArrayList<Block> blocks = new ArrayList<Block>();

	public TrackCsvParser(){
		// ...
	}

	public ArrayList<Block> parse(String csvFile){

		BufferedReader br = null;
		String currentLine = "";
		String delimeter = ",";

		try {
			br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(csvFile)));

			while ((currentLine = br.readLine()) != null){
				String [] blockData = currentLine.split(delimeter);

				/* Parse cells 0-8 */
				String line 			= blockData[0];
				String section 			= blockData[1]; 
				int id 					= Integer.parseInt(blockData[2]) - 1; // Subtract 1 for indexing
				double length 			= Math.ceil(Double.parseDouble(blockData[3]));
				double grade 			= Double.parseDouble(blockData[4]);
				double elevation 		= Double.parseDouble(blockData[5]);
				double cumElevation 	= Double.parseDouble(blockData[6]);
				int speedLimit 			= Integer.parseInt(blockData[7]);
				int direction 			= Integer.parseInt(blockData[8]);
				
				/* Parse cell 9 */
				Light light 			= null;
				if ((Integer.parseInt(blockData[9])) != 0){
					light = new Light();
				}

				/* Parse cell 10 */
				Crossing crossing 		= null;
				if ((Integer.parseInt(blockData[10])) != 0){
					crossing = new Crossing();
				}

				/* Parse cell 11 */
				Station station 		= null;
				try{
					if (!(blockData[11].equals("0"))){
						station = new Station();
						
						String stationDelimeter = "\\.";
						String [] stationData = blockData[11].split(stationDelimeter);

						station.setId(stationData[0]);

						boolean dsPositive;
						boolean dsNegative;

						if (stationData[1].equals("R")){
							dsPositive = station.DOOR_SIDE_RIGHT;
						} else if (stationData[1].equals("L")) {
							dsPositive = station.DOOR_SIDE_LEFT;
						} else {
							dsPositive = station.DOOR_SIDE_NONE;
						}

						if (stationData[2].equals("R")){
							dsNegative = station.DOOR_SIDE_RIGHT;
						} else if (stationData[2].equals("L")) {
							dsNegative = station.DOOR_SIDE_LEFT;
						} else {
							dsNegative = station.DOOR_SIDE_NONE;
						}

						station.setDoorSideDirectionPositive(dsPositive);
						station.setDoorSideDirectionNegative(dsNegative);
					}
				} catch (NumberFormatException e){
					// ...
				} catch (ArrayIndexOutOfBoundsException e){
					// ...
				}
				
				/* Parse cell 12 */
				Switch switch_ 			= null;
				try{
					if (!(blockData[12].equals("0"))){

						switch_ = new Switch();

						String switchDelimeter = "\\.";
						String [] switchData = blockData[12].split(switchDelimeter);

						boolean edgeType;

						if (switchData[0].equals("HEAD")){
							edgeType = switch_.EDGE_TYPE_HEAD;
						} else {
							edgeType = switch_.EDGE_TYPE_TAIL;
						}

						switch_.setEdgeType(edgeType);
						switch_.setPortNormal(Integer.parseInt(switchData[1]) - 1); // Subtract 1 for indexing
						switch_.setPortAlternate(Integer.parseInt(switchData[2]) - 1); // Subtract 1 for indexing

					}
				} catch (NumberFormatException e){
					// ...
				} catch (ArrayIndexOutOfBoundsException e){
					// ...
				}

				/* Parse cell 13 */
				Beacon beacon 			= null;
				if ((Integer.parseInt(blockData[13])) != 0){
					beacon = new Beacon();
					beacon.setInfo(Integer.parseInt(blockData[13]));
				}

				/* Parse cell 14 */
				boolean underground 	= false;
				if ((Integer.parseInt(blockData[14])) != 0){
					underground = true;
				}

				boolean occupied 		= false;

				/* Parse cell 15 */
				double[] x_coordinates = new double[(int)length];
				double[] y_coordinates = new double[(int)length];

				String coordDelimiter = ";";
				String xyDelimiter = "_";

				String[] coords = (blockData[15]).split(coordDelimiter);

				for (int i = 0; i < coords.length; i++){
					x_coordinates[i] = 1.5*Double.parseDouble((coords[i].split(xyDelimiter))[0]);
					y_coordinates[i] = 1.5*Double.parseDouble((coords[i].split(xyDelimiter))[1]);
				}

				/* Generate block from currently parsed CSV line */
				Block currentBlock = new Block(	line,
												section,
												id,
												length,
												grade,
												elevation,
												cumElevation,
												speedLimit,
												direction,
												light,
												crossing,
												station,
												switch_,
												beacon,
												underground,	
												occupied,
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

    	// Assign each switch references to its normal and alternate switches
    	for (int i = 0; i < blocks.size(); i++){
    		if (blocks.get(i).getSwitch() != null){
    			Switch s = blocks.get(i).getSwitch();
    			Switch s_normal = blocks.get(s.getPortNormal()).getSwitch();
    			Switch s_alternate = blocks.get(s.getPortAlternate()).getSwitch();

    			s.setSwitchNormal(s_normal);
    			s.setSwitchAlternate(s_alternate);

    			if (s.getEdge() == Switch.EDGE_TYPE_TAIL){
    				if (s_normal.getPortAlternate() == i){
    					s.setTailType(Switch.TAIL_TYPE_ALTERNATE);
    					// System.out.println(blocks.get(i).getLine() + " | ALT TAIL = " + Integer.toString(i));
    				}
    			}
    		}
    	}

    	return blocks;
	}

	public void parseLightPositions(String csvFile, ArrayList<Block> track){
		BufferedReader br = null;
		String currentLine = "";
		String delimeter = ",";

		try {
			br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(csvFile)));

			while ((currentLine = br.readLine()) != null){
				String [] data = currentLine.split(delimeter);

				String line 			= data[0];
				int id 					= Integer.parseInt(data[1]); 
				int x_coordinate 		= (int)(1.5*(double)Integer.parseInt(data[2]));
				int y_coordinate 		= (int)(1.5*(double)Integer.parseInt(data[3]));

				for (int i = 0; i < blocks.size(); i++){
					if (track.get(id).getLight() != null){
						track.get(id).getLight().setXCoordinate(x_coordinate);
						track.get(id).getLight().setYCoordinate(y_coordinate);
					}
				}
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
	}

	public void showParsedTrack(){
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
	}
}