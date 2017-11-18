package Modules.Ctc;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Modules.TrackModel.Block;

@SuppressWarnings("serial")
public class ScheduleJTable extends JTable{	
	private static Object[] columns = {"Stop","Time to Station"};
	private static Object[] blankRow = new Object[columns.length];
	private ScheduleJTable table = this;
	public Schedule schedule = null;
	
	
	public ScheduleJTable(Object[][] data, Object[] cols) {
		super(data,cols);
	}

	public ScheduleJTable(DefaultTableModel tm) {
		super(tm);
	}
	
	public ScheduleJTable() {
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
		schedule = null;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;		
		fireScheduleChanged();
	}
	
	/**
	 * Update the JTable with the newest schedule, add a blank row
	 */
	public void fireScheduleChanged() {
		//Populate table with schedule
		((DefaultTableModel) this.getModel()).setDataVector(schedule.toStringArray(), columns);
		
		//Add a row for new stops
		addBlankRow();
		
		//Add ComboBoxes to the stop selection column
		addComboBoxToColumn();
		
		//Trigger changes to be rendered visually
		((DefaultTableModel) this.getModel()).fireTableDataChanged();
	}
	
	/**
	 * Adds a combo box to the stops column for users to specify a stop
	 */
	private void addComboBoxToColumn() {
		//Create the ComboBox and add the line blocks to it
		JComboBox<String> blockCB = new JComboBox<String>();
		blockCB.addItem("");
		blockCB.addItem("-REMOVE-");
		for(Block block : schedule.line.blocks) {
			blockCB.addItem(Integer.toString(block.getId()));
		}
		
		//Add a listener to the ComboBox, add stop when state changes
		blockCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {					
					int row = table.getSelectedRow();
					String blockText = (String)blockCB.getSelectedItem();
					
					if(blockText.equals("-REMOVE-")) {
						if(row<schedule.stops.size()) {
							schedule.removeStop(row);
						}
						
						if(schedule.stops.size()==0) {
							addBlankRow();
						}
					}
					else if(!blockText.equals("")){
						schedule.addStop(row, schedule.line.blocks[Integer.parseInt(blockText)]);
					}
					
					fireScheduleChanged();
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
}
