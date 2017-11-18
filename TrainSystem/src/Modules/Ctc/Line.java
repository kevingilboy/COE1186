package Modules.Ctc;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modules.TrackModel.Block;

public enum Line {
	GREEN("Green",150,62,151),RED("Red",76,8,76);
	private final String text;
	
	public JTable dispatchedTable;
	public DefaultTableModel dispatchedData;
	public JTable queueTable;
	public DefaultTableModel queueData;
	
	public Block[] blocks;
	public ArrayList<Block> blocksAL;
	
	public int yardIn;
	public int yardOut;
	public int yardOutNext;
	
	private Line(String s, int out, int outNext, int in) {
		text = s;
		yardIn = in;
		yardOut = out;
		yardOutNext = out;
	}
	@Override
	public String toString() {
		return text;
	}
}
