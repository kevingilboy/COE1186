package Modules.Mbo;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Shared.SimTime;

@SuppressWarnings("serial")
public class TrainInfoTable extends JTable{	
	private static Object[] columns = {"<html><br><br><center>Train Name<br><br></center></html>",
									   "<html><center>Time Most<br>Recent Signal<br>Received</center></html>",
						    		   "<html><center>Coordinates<br>Received<br>(mi, mi)</center></html>",
									   "<html><center>Calculated<br>Location</center></html>",
									   "<html><center>Calculated<br>Velocity<br>(mi/h)</center></html>",
									   "<html><center>Transmitted<br>Authority<br>(mi)</center></html>",
									   "<html><center>Transmitted<br>Safe Braking<br>Distance (mi)</center></html>"};
	private static Object[] blankRow = new Object[columns.length];
	private TrainInfoTable table = this;
	public Object[][] trains = null;
	
	
	public TrainInfoTable(Object[][] data, Object[] cols) {
		super(data,cols);
	}

	public TrainInfoTable(DefaultTableModel tm) {
		super(tm);
	}
	
	public TrainInfoTable() {
		super(new DefaultTableModel(null,columns));
	}
	
	/**
	 * This blank row allows stops to be added
	 */
	public void addBlankRow() {
		((DefaultTableModel) this.getModel()).addRow(blankRow);
	}
	
	public void clear() {
		this.setModel(new DefaultTableModel(null,columns));
		trains = null;
	}

	public void setTrains(Object[][] trains) {
		this.trains = trains;		
		fireTrainsChanged();
	}
	
	/**
	 * Update the JTable with the newest schedule, add a blank row
	 */
	public void fireTrainsChanged() {
		//Populate table with schedule
		((DefaultTableModel) this.getModel()).setDataVector(trains, columns);
		
		//Add a row for new trains
		addBlankRow();
		
		//Trigger changes to be rendered visually
		((DefaultTableModel) this.getModel()).fireTableDataChanged();
	}
}
