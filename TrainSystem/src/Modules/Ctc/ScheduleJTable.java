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
	
	private DefaultCellEditor editor;
	
	public ScheduleJTable(Object[][] data, Object[] cols) {
		super(data,cols);
	}

	public ScheduleJTable(DefaultTableModel tm) {
		super(tm);
	}
	
	public ScheduleJTable() {
		super(new DefaultTableModel(null,columns));
	}
	
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
	
	public void fireScheduleChanged() {
		((DefaultTableModel) this.getModel()).setDataVector(schedule.toStringArray(), columns);
		addBlankRow();
		addComboBoxToColumn();
		((DefaultTableModel) this.getModel()).fireTableDataChanged();
	}
	
	private void addComboBoxToColumn() {
		JComboBox<String> blockCB = new JComboBox<String>();
		for(Block block : schedule.line.blocks) {
			blockCB.addItem(Integer.toString(block.getId()));
		}
		
		blockCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {					
					int row = table.getSelectedRow();
	
					schedule.addStop(row, schedule.line.blocks[Integer.parseInt((String)blockCB.getSelectedItem())]);
					fireScheduleChanged();
				}
			}
		});
		editor = new DefaultCellEditor(blockCB);
		this.getColumnModel().getColumn(0).setCellEditor(editor);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        this.getColumnModel().getColumn(0).setCellRenderer(renderer);
	}
	
	/*@Override
    public TableCellEditor getCellEditor(int row, int column)
    {
		int modelColumn = convertColumnIndexToModel( column );
		int modelRow = convertRowIndexToModel( row );
		if (modelColumn == 0) {
			String val = (String)this.getValueAt(modelRow, modelColumn);
			if(val!=null) {
				schedule.addStop(modelRow, schedule.line.blocks[Integer.parseInt(val)]);
				fireScheduleChanged();
			}
		}
		
		return super.getCellEditor(row, column);
    }*/
}
