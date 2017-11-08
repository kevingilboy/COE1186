package Modules.TrackModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
			br = new BufferedReader(new FileReader(csvFile));

			while ((currentLine = br.readLine()) != null){
				String [] blockData = currentLine.split(delimeter);

				String line 			= blockData[0];
				String section 			= blockData[1]; 
				int id 					= Integer.parseInt(blockData[2]);
				double length 			= Double.parseDouble(blockData[3]);
				double grade 			= Double.parseDouble(blockData[4]);
				double elevation 		= Double.parseDouble(blockData[5]);
				double cumElevation 	= Double.parseDouble(blockData[6]);
				int speedLimit 			= Integer.parseInt(blockData[7]);
				int direction 			= Integer.parseInt(blockData[8]);
				
				Light light 			= null;
				if ((Integer.parseInt(blockData[9])) != 0){
					light = new Light();
				}

				Crossing crossing 		= null;
				if ((Integer.parseInt(blockData[10])) != 0){
					crossing = new Crossing();
				}

				Station station 		= null;
				try{
					if (!(blockData[11].equals("0"))){
						station = new Station();
						
						String stationDelimeter = ".";
						String [] stationData = blockData[11].split(stationDelimeter);

						station.setId(stationData[0]);

						boolean dsPositive;
						boolean dsNegative;

						if (stationData[1] == "R"){
							dsPositive = station.DOOR_SIDE_RIGHT;
						} else if (stationData[1] == "L") {
							dsPositive = station.DOOR_SIDE_LEFT;
						} else {
							dsPositive = station.DOOR_SIDE_NONE;
						}

						if (stationData[2] == "R"){
							dsNegative = station.DOOR_SIDE_RIGHT;
						} else if (stationData[2] == "L") {
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
				
				Switch switch_ 			= null;
				try{
					if (!(blockData[12].equals("0"))){
						switch_ = new Switch();

						String switchDelimeter = ".";
						String [] switchData = blockData[12].split(switchDelimeter);

						boolean edgeType;
						if (switchData[0] == "HEAD"){
							edgeType = switch_.EDGE_TYPE_HEAD;
						} else {
							edgeType = switch_.EDGE_TYPE_TAIL;
						}

						switch_.setEdgeType(edgeType);
						switch_.setPortNormal(Integer.parseInt(switchData[1]));
						switch_.setPortAlternate(Integer.parseInt(switchData[2]));
					}
				} catch (NumberFormatException e){
					// ...
				} catch (ArrayIndexOutOfBoundsException e){
					// ...
				}

				Beacon beacon 			= null;
				if ((Integer.parseInt(blockData[13])) != 0){
					beacon = new Beacon();
					beacon.setInfo(Integer.parseInt(blockData[13]));
				}

				boolean underground 	= false;
				if ((Integer.parseInt(blockData[14])) != 0){
					underground = true;
				}

				boolean occupied 		= false;

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
												occupied);

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
}