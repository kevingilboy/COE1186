import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ScheduleJTable extends JTable{	
	public Schedule schedule = null;
	
	public ScheduleJTable(Object[][] data, Object[] cols) {
		super(data,cols);
	}

	public ScheduleJTable(DefaultTableModel tm) {
		super(tm);
	}

}
