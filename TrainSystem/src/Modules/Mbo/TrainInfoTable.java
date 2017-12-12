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
		
		//Add a row for new stops
		addBlankRow();
		
		//Add ComboBoxes to the stop selection column
		//addComboBoxToColumn();
		
		//Add TextAreas to the dwell time column
		//addTextAreaToColumn();
		
		//Trigger changes to be rendered visually
		((DefaultTableModel) this.getModel()).fireTableDataChanged();
	}
	/*
	public boolean checkDataValid() {
	    DefaultTableModel dtm = (DefaultTableModel) this.getModel();
	    int rows = dtm.getRowCount();
	    for (int i = 0 ; i < rows ; i++) {
	    	String block = (String)dtm.getValueAt(i,0);
	    	String timeToDwell = (String)dtm.getValueAt(i,1);
	    	String timeToDest = (String)dtm.getValueAt(i,2);
	    	
	    	//Ignore if the last blank row, it is purposefully null
	    	if(block == null && timeToDwell == null && timeToDest == null) {
	    		continue;
	    	}
	    	
	    	//Make sure row has a block and a time to dwell
	    	if(block.equals("") || block==null || 
	    			timeToDwell==null || !SimTime.isValid(timeToDwell)) {
	    		return false;
	    	}
	    }
		return true;
	}
	
	/**
	 * Adds a combo box to the stops column for users to specify a stop
	 *//*
	private void addComboBoxToColumn() {
		//Create the ComboBox and add the line blocks to it
		JComboBox<String> blockCB = new JComboBox<String>();
		blockCB.addItem("");
		blockCB.addItem("-REMOVE-");
		for(Block block : schedule.line.blocks) {
			blockCB.addItem(block.toString());
		}
		
		//Add a listener to the ComboBox, add stop when state changes
		blockCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {					
					int row = table.getSelectedRow();
					String blockText = (String)blockCB.getSelectedItem();
					
					//Sometimes this is accidentally triggered so check the row
					if(row==-1) {
						return;
					}
					
					// CASE : -REMOVE-, remove the row
					if(blockText.equals("-REMOVE-")) {
						//Make sure the last row is not being removed
						if(row<schedule.stops.size()) {
							schedule.removeStop(row);
						}
						if(schedule.train!=null && schedule.stops.size()==0) {
							schedule.addStop(0, schedule.line.yardIn);
						}
						
						//If no rows exist, add one for future stop additions
						if(schedule.stops.size()==0) {
							addBlankRow();
						}
					}
					else if(!blockText.equals("")){
						//Match the first number (single or multi-digit)
						Matcher m = Pattern.compile("\\d+").matcher(blockText);
						m.find();
						int blockNum = Integer.valueOf(m.group());
						
						if(blockNum!=-1) {
							schedule.addStop(row, blockNum-1);
						}
					}
					
					fireTrainsChanged();
				}
			}
		});
		
		//Put the ComboBox into the JTable cell
		DefaultCellEditor editor = new DefaultCellEditor(blockCB);
		this.getColumnModel().getColumn(0).setCellEditor(editor);
		
		//Add a tooltip
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        this.getColumnModel().getColumn(0).setCellRenderer(renderer);
        
	}
	private void addTextAreaToColumn() {
		//Create the ComboBox and add the line blocks to it
		JTextField textArea = new JTextField();
		
		//Add a listener to the ComboBox, add stop when state changes
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int row = table.getSelectedRow();
				String timeToDwell = textArea.getText();
				
				if(SimTime.isValid(timeToDwell) && row>=0) {
					schedule.addStop(row, new SimTime(timeToDwell));
					fireTrainsChanged();
				}
			}
		});
	
		//Put the TextArea into the JTable cell
		DefaultCellEditor textAreaEditor = new DefaultCellEditor(textArea);
		this.getColumnModel().getColumn(1).setCellEditor(textAreaEditor);
		
		//Add a tooltip
	    DefaultTableCellRenderer textAreaRenderer = new DefaultTableCellRenderer();
	    textAreaRenderer.setToolTipText("Type time to spend at the station");
	    this.getColumnModel().getColumn(1).setCellRenderer(textAreaRenderer);
	}*/
}
