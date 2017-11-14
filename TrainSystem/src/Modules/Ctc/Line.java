package Modules.Ctc;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modules.TrackModel.Block;

public enum Line {
	GREEN("Green"),RED("Red");
	private final String text;
	
	public JTable dispatchedTable;
	public DefaultTableModel dispatchedData;
	public JTable queueTable;
	public DefaultTableModel queueData;
	
	public Block[] blocks;
	
	private Line(String s) {
		text = s;
	}
	@Override
	public String toString() {
		return text;
	}
}
