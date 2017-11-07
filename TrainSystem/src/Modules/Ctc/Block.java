package Modules.Ctc;

import java.io.*;
import java.util.ArrayList;

public class Block {
	public String line;
	public String section;
	public int id;
	public double length;
	public String station;
	public String switchBlock;
	public String switchID;
	public String partOfSection;
	public int direction;
	public String switchType;
	public boolean occupied;
	public boolean status;
	public boolean altSwitchState;

	public Block(String line, String section, int id, double length, String station, String switchBlock,
			String switchID, String partOfSection, int direction, String switchType) {
		this.line 			= line;
		this.section 		= section;
		this.length 		= length;
		this.station 		= station;
		this.switchBlock	= switchBlock;
		this.switchID		= switchID;
		this.partOfSection  = partOfSection;
		this.direction 		= direction;
		this.switchType		= switchType;
		this.occupied		= false;
		this.status			= true;
		this.altSwitchState	= false;
	}

	public static Block[] parseFile(String f){

		BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ",";
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		blocks.add(null);
		try {
			br = new BufferedReader(new FileReader(f));

			// Read from csv file and create blocks for each line, then
			// add each block to an arraylist of blocks

			while ((currline = br.readLine()) != null){
				String [] blockInfo = currline.split(delimeter);

				String 	line 			= blockInfo[0];
				String 	section 		= blockInfo[1];
				int 	id 				= Integer.parseInt(blockInfo[2]);
				double 	length 			= Double.parseDouble(blockInfo[3]);
				String 	station 		= blockInfo[6];
				String  switchBlock     = blockInfo[7];
				String 	switchID     	= blockInfo[11];
				String  partOfSection   = blockInfo[12];
				int     direction       = Integer.parseInt(blockInfo[13]);
				String  switchType      = blockInfo[15];


				Block currBlock = new Block(line,
											section,
											id,
											length,
											station,
											switchBlock,
											switchID,
											partOfSection,
											direction,
											switchType );

				blocks.add(currBlock);
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
		Block[] blockArray = blocks.toArray(new Block[blocks.size()]);
		return blockArray;
	}

}
