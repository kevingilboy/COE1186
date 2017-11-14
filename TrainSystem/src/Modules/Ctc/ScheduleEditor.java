package Modules.Ctc;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Modules.TrackModel.Block;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

public class ScheduleEditor {
	public Schedule currentSchedule;
	public boolean editing = true;
	public JFrame frame;
	private JTextField nameInput;
	private JComboBox<String> lineSelect;
	private JButton btnCreateSchedule;
	
	public ScheduleJTable scheduleTable;
	public DefaultTableModel scheduleData;
	private Object[] selectedTrainColumnNames = {"Stop","Arrival","Departure"};
	private Object[][] selectedTrainInitialData = new Object[4][selectedTrainColumnNames.length];
	private JTextField departTime;
	public CtcCore ctc;


	/**
	 * Create the application.
	 */
	public ScheduleEditor() {
		initialize();
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ScheduleEditor(CtcCore ctc, Schedule schedule) {		
		this.ctc = ctc;
		initialize();	
		if(schedule==null) {
			currentSchedule = new Schedule();
		}
		else {
			currentSchedule = schedule;
			nameInput.setText(currentSchedule.name);
			nameInput.setEnabled(false);
			btnCreateSchedule.setText("Save Edits");

			if(currentSchedule.dispatched==true){
				lineSelect.setEnabled(false);
			}
		}		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 700);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		
		JLabel lblScheduleEditor = new JLabel("<html><u>Schedule Editor</u></html>");
		lblScheduleEditor.setHorizontalAlignment(SwingConstants.CENTER);
		lblScheduleEditor.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblScheduleEditor.setBounds(0, 6, 518, 33);
		contentPane.add(lblScheduleEditor);
		
		JLabel lblTrainName = new JLabel("Train Name");
		lblTrainName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTrainName.setBounds(101, 54, 77, 33);
		contentPane.add(lblTrainName);

		nameInput = new JTextField();
		nameInput.setBounds(181, 51, 199, 39);
		nameInput.setColumns(10);
		contentPane.add(nameInput);
		
		JLabel lblLine = new JLabel("Line");
		lblLine.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLine.setBounds(142, 96, 34, 33);
		contentPane.add(lblLine);
		
		lineSelect = new JComboBox<String>();
		lineSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO CLEAR THE SCHEDULE
			}
		});
		lineSelect.setModel(new DefaultComboBoxModel<String>(new String[] {"Red", "Green"}));
		lineSelect.setBounds(181, 93, 199, 39);
		contentPane.add(lineSelect);
		
		JLabel lblDepart = new JLabel("Depart @");
		lblDepart.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDepart.setBounds(116, 141, 60, 33);
		frame.getContentPane().add(lblDepart);
		
		departTime = new JTextField();
		departTime.setColumns(10);
		departTime.setBounds(181, 143, 199, 39);
		frame.getContentPane().add(departTime);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(55, 200, 400, 316);
		frame.getContentPane().add(scrollPane);
		
		scheduleData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		scheduleTable = new ScheduleJTable(scheduleData);
		scheduleTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		setUpBlockColumn(scheduleTable);
		scrollPane.setViewportView(scheduleTable);
		
		btnCreateSchedule = new JButton("Create Schedule");
		btnCreateSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSchedule.name = nameInput.getText();
				currentSchedule.line = (String) lineSelect.getSelectedItem();
				
				frame.dispose();
				editing = false;
			}
		});
		btnCreateSchedule.setBounds(178, 548, 171, 41);
		contentPane.add(btnCreateSchedule);
	}
	
	private void setUpBlockColumn(ScheduleJTable table) {
		JComboBox<String> blockCB = new JComboBox<String>();
		if((String) lineSelect.getSelectedItem() == "Red") {
			for(Block block :ctc.redBlocks) {
				blockCB.addItem(Integer.toString(block.getId()));
			}
		}
		else {
			for(Block block :ctc.greenBlocks) {
				blockCB.addItem(Integer.toString(block.getId()));
			}
		}

		table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(blockCB));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScheduleEditor window = new ScheduleEditor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
