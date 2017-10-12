import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class CTC {
	private JFrame frame;
	
	private Object[] selectedTrainColumnNames = {"Stop","Arrival","Departure"};
	private Object[][] selectedTrainInitialData = new Object[9][selectedTrainColumnNames.length];
	
	/**
	 * Dispatched train tales
	 */
	private Object[] dispatchedTrainsColumnNames = {"Train","Location","Speed","Authority","Passengers"};
	private Object[][] dispatchedTrainsInitialData = new Object[0][dispatchedTrainsColumnNames.length];

	private DefaultTableModel dispatchedGreenData;
	private JTable dispatchedGreenTable;

	private DefaultTableModel dispatchedRedData;
	private JTable dispatchedRedTable;

	private DefaultTableModel dispatchSelectedData;
	private ScheduleJTable dispatchSelectedTable;
		
	/**
	 * Creator tales
	 */
	private ScheduleJTable trainCreationTable;
	private DefaultTableModel trainCreationData;
	private JTextField trainCreationDepartTime;
	private JTextField trainCreationLine;
	
	/**
	 * Queue tales
	 */
	private Object[] queueTrainColumnNames = {"Train","Authority","Departure"};
	private Object[][] queueTrainInitialData = new Object[0][queueTrainColumnNames.length];

	private JTable queueRedTable;
	private DefaultTableModel queueRedData;
	
	private JTable queueGreenTable;
	private DefaultTableModel queueGreenData;
	
	private ScheduleJTable queueSelectedTable;
	private DefaultTableModel queueSelectedData;
	
	/**
	 * Train objects hashed by name
	 */
	private Map<String,Schedule> trainsInQueue = new HashMap<>();
	private Map<String,Schedule> trainsDispatched = new HashMap<>();
	
	/**
	 * Threads
	 */
	public ScheduleEditor scheduleEditor;
	public Schedule scheduleForScheduleEditor = null;

	/**
	 * Constants
	 */
	public final int GUI_WINDOW_HEIGHT = 600;
	public final int GUI_WINDOW_WIDTH = 1200;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CTC window = new CTC();
					window.frame.setResizable(false);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CTC() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, GUI_WINDOW_WIDTH, GUI_WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		
		JSeparator middleLeftSeparator = new JSeparator();
		middleLeftSeparator.setOrientation(SwingConstants.VERTICAL);
		middleLeftSeparator.setForeground(Color.GRAY);
		middleLeftSeparator.setBackground(Color.BLACK);
		middleLeftSeparator.setBounds(250, 0, 1, 512);
		contentPane.add(middleLeftSeparator);
		
		JSeparator horizontalSeparator = new JSeparator();
		horizontalSeparator.setBounds(250, 264, 487, 2);
		contentPane.add(horizontalSeparator);
		
		JSeparator middleRightSeparator = new JSeparator();
		middleRightSeparator.setOrientation(SwingConstants.VERTICAL);
		middleRightSeparator.setForeground(Color.GRAY);
		middleRightSeparator.setBackground(Color.BLACK);
		middleRightSeparator.setBounds(737, 0, 1, 512);
		contentPane.add(middleRightSeparator);

		/**
		 * LEFT FRAME
		 */
		
		JLabel label = new JLabel("08:45");
		setHeader(label);
		setClockFont(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(75, 64, 100, 44);
		contentPane.add(label);

		JLabel tempLbl = new JLabel("Temp");
		setBold(tempLbl);
		tempLbl.setBounds(23, 172, 115, 33);
		contentPane.add(tempLbl);
		
		JSpinner tempSpinner = new JSpinner();
		tempSpinner.setModel(new SpinnerNumberModel(new Integer(70), null, null, new Integer(1)));
		tempSpinner.setBounds(134, 169, 66, 40);
		contentPane.add(tempSpinner);
		
		JLabel fLbl = new JLabel("F");
		fLbl.setBounds(209, 172, 14, 33);
		contentPane.add(fLbl);
		
		JLabel weatherLbl = new JLabel("Weather");
		setBold(weatherLbl);
		weatherLbl.setBounds(23, 128, 115, 33);
		contentPane.add(weatherLbl);
		
		JComboBox<String> weatherSelect = new JComboBox<String>();
		weatherSelect.setModel(new DefaultComboBoxModel<String>(new String[] {"Sunny", "Rainy"}));
		weatherSelect.setBounds(124, 125, 100, 39);
		contentPane.add(weatherSelect);
		
		JLabel lblThroughput = new JLabel("Throughput: ");
		setBold(lblThroughput);
		lblThroughput.setBounds(0, 479, 158, 33);
		contentPane.add(lblThroughput);
		
		JLabel lblThroughputAmt = new JLabel("###");
		lblThroughputAmt.setBounds(134, 479, 89, 33);
		contentPane.add(lblThroughputAmt);
		
		/**
		 * MID TOP
		 */		
		JLabel lblQueue = new JLabel("Queue");
		setHeader(lblQueue);
		lblQueue.setBounds(463, 28, 115, 33);
		contentPane.add(lblQueue);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(287, 55, 397, 97);
		contentPane.add(tabbedPane_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane_1.addTab("Red", null, scrollPane_2, null);
		
		queueRedData = new DefaultTableModel(queueTrainInitialData,queueTrainColumnNames);
		queueRedTable = new JTable(queueRedData);
		queueRedTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = queueRedTable.rowAtPoint(e.getPoint());
				String trainName = (String) queueRedData.getValueAt(row, 0);
				Schedule schedule = trainsInQueue.get(trainName);
				openScheduleInTable(queueSelectedTable,queueSelectedData,schedule);
			}
		});
		scrollPane_2.setViewportView(queueRedTable);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane_1.addTab("Green", null, scrollPane_3, null);
		
		queueGreenData = new DefaultTableModel(queueTrainInitialData,queueTrainColumnNames);
		queueGreenTable = new JTable(queueGreenData);
		queueGreenTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = queueGreenTable.rowAtPoint(e.getPoint());
				String trainName = (String) queueGreenData.getValueAt(row, 0);
				Schedule schedule = trainsInQueue.get(trainName);
				openScheduleInTable(queueSelectedTable,queueSelectedData,schedule);
			}
		});
		scrollPane_3.setViewportView(queueGreenTable);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(277, 184, 221, 62);
		contentPane.add(scrollPane_4);
		
		queueSelectedData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		queueSelectedTable = new ScheduleJTable(queueSelectedData);
		scrollPane_4.setViewportView(queueSelectedTable);
		
		JButton editQueueSchedule = new JButton("Edit Schedule");
		stylize(editQueueSchedule);
		editQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName="";
				int row;
				String line = tabbedPane_1.getTitleAt(tabbedPane_1.getSelectedIndex());
				
				//Get the train name
				if(line=="Red") {
					row = queueRedTable.getSelectedRow();
					if(row<0) return;
					trainName = (String) queueRedData.getValueAt(row, 0);
				}
				else if(line=="Green") {
					row = queueGreenTable.getSelectedRow();
					if(row<0) return;
					trainName = (String) queueGreenData.getValueAt(row, 0);
				}

				scheduleForScheduleEditor = trainsInQueue.get(trainName);

				Thread scheduleEditorThread = new Thread() {
					public void run() {
						try {
							SwingUtilities.invokeAndWait(openScheduleEditor);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
				         
						while(scheduleEditor.editing==true) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Schedule schedule = scheduleEditor.currentSchedule;
						trainsInQueue.put(schedule.name,schedule);
						updateQueueTable();
						
						System.out.println("Finished on " + Thread.currentThread());
					}
				};
				scheduleEditorThread.start();				
			}
		});
		editQueueSchedule.setBounds(513, 186, 171, 24);
		contentPane.add(editQueueSchedule);
		
		JLabel label_2 = new JLabel("Selected Train Schedule");
		setSubHeader(label_2);
		label_2.setBounds(399, 152, 198, 33);
		contentPane.add(label_2);
		
		JButton dispatchQueueSchedule = new JButton("Dispatch Now");
		stylize(dispatchQueueSchedule);
		dispatchQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String line = tabbedPane_1.getTitleAt(tabbedPane_1.getSelectedIndex());
				String trainName="";
				if(line=="Red") {
					int row = queueRedTable.getSelectedRow();
					trainName = (String) queueRedData.getValueAt(row, 0);
				}
				else if(line=="Green") {
					int row = queueGreenTable.getSelectedRow();
					trainName = (String) queueGreenData.getValueAt(row, 0);
				}
				Schedule schedule = trainsInQueue.remove(trainName);
				trainsDispatched.put(trainName, schedule);
				schedule.dispatched = true;
				
				queueSelectedData.setDataVector(selectedTrainInitialData,selectedTrainColumnNames);
				openScheduleInTable(queueSelectedTable,queueSelectedData,null);
				updateQueueTable();
				updateDispatchedTable();
				
			}
		});
		dispatchQueueSchedule.setBounds(512, 218, 171, 24);
		contentPane.add(dispatchQueueSchedule);
		
		/**
		 * MID BOTTOM
		 */
		JLabel lblNewLabel = new JLabel("Dispatch Center");
		setHeader(lblNewLabel);
		lblNewLabel.setBounds(463, 274, 115, 33);
		contentPane.add(lblNewLabel);
		
		JToggleButton tglManual = new JToggleButton("Manual");
		tglManual.setSelected(true);
		tglManual.setBounds(372, 308, 131, 33);
		contentPane.add(tglManual);
		
		JToggleButton tglAuto = new JToggleButton("Auto");
		tglAuto.setBounds(502, 308, 131, 33);
		contentPane.add(tglAuto);
		
		JLabel lblManualTrainCreation = new JLabel("Manual Train Creation");
		setSubHeader(lblManualTrainCreation);
		lblManualTrainCreation.setBounds(430, 347, 221, 33);
		contentPane.add(lblManualTrainCreation);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(372, 385, 264, 62);
		contentPane.add(scrollPane_1);
		
		trainCreationData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		trainCreationTable = new ScheduleJTable(trainCreationData);
		scrollPane_1.setViewportView(trainCreationTable);
		
		JLabel lblDepartAt = new JLabel("Depart at");
		setBold(lblDepartAt);
		lblDepartAt.setBounds(260, 411, 115, 33);
		contentPane.add(lblDepartAt);
		
		trainCreationDepartTime = new JTextField();
		trainCreationDepartTime.setEditable(false);
		trainCreationDepartTime.setText("NA");
		trainCreationDepartTime.setBounds(314, 408, 52, 39);
		contentPane.add(trainCreationDepartTime);
		trainCreationDepartTime.setColumns(10);
		
		JLabel lblLine = new JLabel("Line");
		setBold(lblLine);
		lblLine.setBounds(287, 377, 57, 33);
		contentPane.add(lblLine);
			
		trainCreationLine = new JTextField();
		trainCreationLine.setEditable(false);
		trainCreationLine.setText("NA");
		trainCreationLine.setColumns(10);
		trainCreationLine.setBounds(314, 374, 52, 39);
		contentPane.add(trainCreationLine);
		
		JButton editToDispatchSchedule = new JButton("<html><center>Create/Edit<br>Schedule</center></html>");
		stylize(editToDispatchSchedule);
		editToDispatchSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scheduleForScheduleEditor = trainCreationTable.schedule;
				Thread scheduleEditorThread = new Thread() {
					public void run() {
						try {
							SwingUtilities.invokeAndWait(openScheduleEditor);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
				         
						while(scheduleEditor.editing==true) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Schedule schedule = scheduleEditor.currentSchedule;
						trainCreationLine.setText(schedule.line);
						trainCreationDepartTime.setText(schedule.departureTime.toString());
						openScheduleInTable(trainCreationTable,trainCreationData,schedule);
						
						System.out.println("Finished on " + Thread.currentThread());
					}
				};
				scheduleEditorThread.start();				
			}
		});
		editToDispatchSchedule.setBounds(645, 383, 89, 64);
		contentPane.add(editToDispatchSchedule);
		
		JButton addToDispatchToQueue = new JButton("Add To Queue");
		stylize(addToDispatchToQueue);
		addToDispatchToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Schedule schedule = trainCreationTable.schedule;
				trainsInQueue.put(schedule.name, schedule);
				updateQueueTable();
				
				//Remove from the creator
				trainCreationData.setDataVector(selectedTrainInitialData,selectedTrainColumnNames);
				openScheduleInTable(trainCreationTable,trainCreationData,null);
			}
		});
		addToDispatchToQueue.setBounds(430, 459, 131, 41);
		contentPane.add(addToDispatchToQueue);
			
		/**
		 * RIGHT SIDE
		 */
		JLabel lblDispatchedTrains = new JLabel("Dispatched Trains");
		setHeader(lblDispatchedTrains);
		lblDispatchedTrains.setBounds(930, 28, 115, 33);
		contentPane.add(lblDispatchedTrains);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(745, 55, 397, 187);
		contentPane.add(tabbedPane);
		
		JScrollPane dispatchedRedScrollPane = new JScrollPane();
		
		dispatchedRedData = new DefaultTableModel(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		dispatchedRedTable = new JTable(dispatchedRedData);
		dispatchedRedTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = dispatchedRedTable.rowAtPoint(e.getPoint());
				String trainName = (String) dispatchedRedData.getValueAt(row, 0);
				Schedule schedule = trainsDispatched.get(trainName);
				openScheduleInTable(dispatchSelectedTable,dispatchSelectedData,schedule);
			}
		});
		dispatchedRedScrollPane.setViewportView(dispatchedRedTable);
		tabbedPane.addTab("Red", null, dispatchedRedScrollPane, null);
		
		JScrollPane dispatchedGreenScrollPane = new JScrollPane();
		
		dispatchedGreenData = new DefaultTableModel(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		dispatchedGreenTable = new JTable(dispatchedGreenData);
		dispatchedGreenTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = dispatchedGreenTable.rowAtPoint(e.getPoint());
				String trainName = (String) dispatchedGreenData.getValueAt(row, 0);
				Schedule schedule = trainsDispatched.get(trainName);
				openScheduleInTable(dispatchSelectedTable,dispatchSelectedData,schedule);
			}
		});
		dispatchedGreenScrollPane.setViewportView(dispatchedGreenTable);
		tabbedPane.addTab("Green", null, dispatchedGreenScrollPane, null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(745, 274, 221, 210);
		contentPane.add(scrollPane);
		
		dispatchSelectedData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		dispatchSelectedTable = new ScheduleJTable(dispatchSelectedData);
		scrollPane.setViewportView(dispatchSelectedTable);
		
		JLabel lblSelectedTrainSchedule = new JLabel("Selected Train Schedule");
		setSubHeader(lblSelectedTrainSchedule);
		lblSelectedTrainSchedule.setBounds(867, 242, 198, 33);
		contentPane.add(lblSelectedTrainSchedule);
		
		JButton editSelectedDispatchedTrainSchedule = new JButton("Edit Schedule");
		stylize(editSelectedDispatchedTrainSchedule);
		editSelectedDispatchedTrainSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName="";
				int row;
				String line = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
				
				//Get the train name
				if(line=="Red") {
					row = dispatchedRedTable.getSelectedRow();
					if(row<0) return;
					trainName = (String) dispatchedRedData.getValueAt(row, 0);
				}
				else if(line=="Green") {
					row = dispatchedGreenTable.getSelectedRow();
					if(row<0) return;
					trainName = (String) dispatchedGreenData.getValueAt(row, 0);
				}

				scheduleForScheduleEditor = trainsDispatched.get(trainName);

				Thread scheduleEditorThread = new Thread() {
					public void run() {
						try {
							SwingUtilities.invokeAndWait(openScheduleEditor);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
				         
						while(scheduleEditor.editing==true) {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Schedule schedule = scheduleEditor.currentSchedule;
						trainsDispatched.put(schedule.name,schedule);
						updateDispatchedTable();
						
						System.out.println("Finished on " + Thread.currentThread());
					}
				};
				scheduleEditorThread.start();				
			}
		});
		editSelectedDispatchedTrainSchedule.setBounds(971, 288, 171, 41);
		contentPane.add(editSelectedDispatchedTrainSchedule);

		JLabel logoPineapple = new JLabel(new ImageIcon("pineapple_icon.png"));
		logoPineapple.setBounds(GUI_WINDOW_WIDTH-150,GUI_WINDOW_HEIGHT-12-100,138,76);
		contentPane.add(logoPineapple);
	}
	
	private void openScheduleInTable(ScheduleJTable table,DefaultTableModel data, Schedule schedule) {
		if(schedule!=null) {
			data.setDataVector(schedule.toStringArray(), selectedTrainColumnNames);
		}
		data.fireTableDataChanged();
		table.schedule = schedule;						
	}
	
	private void updateQueueTable(){
		//Clear the red and green tables
		queueRedData.setDataVector(queueTrainInitialData, queueTrainColumnNames);
		queueGreenData.setDataVector(queueTrainInitialData, queueTrainColumnNames);
		
		//Cycle through each dispatched train's schedule
		Schedule schedule;
		for(String trainName:trainsInQueue.keySet()) {
			schedule = trainsInQueue.get(trainName);
			Object[] row = {schedule.name,schedule.authority,schedule.departureTime.toString()};
			if(schedule.line=="Red") {
				queueRedData.addRow(row);
			}
			else if(schedule.line=="Green"){
				queueGreenData.addRow(row);
			}
		}

		//Update the tables in the GUI
		queueRedData.fireTableDataChanged();
		queueGreenData.fireTableDataChanged();
	}

	private void updateDispatchedTable(){
		//Clear the red and green tables
		dispatchedRedData.setDataVector(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		dispatchedGreenData.setDataVector(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);

		//Cycle through each dispatched train's schedule
		Schedule schedule;
		for(String trainName:trainsDispatched.keySet()) {
			schedule = trainsDispatched.get(trainName);
			//Object[] row; //build the row here, but for now we fake the functionality below
			if(schedule.line=="Red") {
				Object[] row = {schedule.name,"C9","0",schedule.authority+" mi","0"};
				dispatchedRedData.addRow(row);
			}
			else if(schedule.line=="Green"){
				Object[] row = {schedule.name,"J62","0",schedule.authority+" mi","0"};
				dispatchedGreenData.addRow(row);
			}
		}

		//Update the tables in the GUI
		queueRedData.fireTableDataChanged();
		queueGreenData.fireTableDataChanged();
	}
	
	final Runnable openScheduleEditor = new Runnable() {
	    public void run() {
	        scheduleEditor = new ScheduleEditor(scheduleForScheduleEditor);
	        scheduleEditor.frame.setVisible(true);
	        scheduleEditor.frame.setResizable(false);
	    }
	};

	private static void stylize(JButton button){
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
	}
	private static void setHeader(JLabel lbl){
		lbl.setFont(new Font(lbl.getFont().getName(),Font.BOLD,16));
	}
	private static void setClockFont(JLabel lbl){
		lbl.setFont(new Font("Courier New",Font.BOLD,18));
	}
	private static void setSubHeader(JLabel lbl){
		lbl.setFont(new Font(lbl.getFont().getName(),Font.BOLD+Font.ITALIC,14));
	}
	private static void setBold(JLabel lbl){
		lbl.setFont(new Font(lbl.getFont().getName(),Font.BOLD,lbl.getFont().getSize()));
	}
}
