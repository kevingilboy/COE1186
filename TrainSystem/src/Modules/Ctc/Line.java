//Kevin Gilboy
//This enum holds the RED and GREEN lines

package Modules.Ctc;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modules.TrackModel.Block;

public enum Line {
	GREEN("Green",150,151),RED("Red",76,76);
	private final String text;
	
	public JTable dispatchedTable;
	public DefaultTableModel dispatchedData;
	public JTable queueTable;
	public DefaultTableModel queueData;
	
	public Block[] blocks;
	public ArrayList<Block> blocksAL;
	
	public int yardIn;
	public int yardOut;
	
	private Line(String s, int in, int out) {
		text = s;
		yardIn = in;
		yardOut = out;
	}
	@Override
	public String toString() {
		return text;
	}
}
