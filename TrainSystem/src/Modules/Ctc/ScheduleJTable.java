package Modules.Ctc;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ScheduleJTable extends JTable{	
	private static Object[] columns = {"Stop","Time to Station"};
	private static Object[] blankRow = new Object[columns.length];
	
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
	
	public void addBlankRow() {
		((DefaultTableModel) this.getModel()).addRow(blankRow);
	}
	
	public void repaint() {
		
	}
	
	public void clear() {
		this.setModel(new DefaultTableModel(null,columns));
		schedule = null;
	}

	public void fireScheduleChanged() {
		((DefaultTableModel) this.getModel()).setDataVector(schedule.toStringArray(), columns);
		addBlankRow();
		((DefaultTableModel) this.getModel()).fireTableDataChanged();	
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;		
		fireScheduleChanged();
	}
}
