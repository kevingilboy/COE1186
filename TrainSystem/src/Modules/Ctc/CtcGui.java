package Modules.Ctc;

import Modules.TrackModel.Block;
import Modules.TrackModel.Light;
import Modules.TrackModel.Crossing;
import Modules.TrackModel.Station;
import Modules.TrackModel.Switch;
import Modules.TrackModel.Beacon;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.io.File;
import java.awt.Container;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CtcGui {
	private CtcCore ctc;
	
	private JFrame frame;
	
	private Object[] selectedTrainColumnNames = {"Stop","Arrival","Departure"};
	private Object[][] selectedTrainInitialData = new Object[11][selectedTrainColumnNames.length];
	
	private JLabel lblThroughputAmt;
	
	/**
	 * Dispatched train tables
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
	 * Creator tables
	 */
	private ScheduleJTable trainCreationTable;
	private DefaultTableModel trainCreationData;
	private JTextField trainCreationDepartTime;
	private JTextField trainCreationLine;
	private JTextField trainCreationName;
	
	/**
	 * Queue tables
	 */
	private Object[] queueTrainColumnNames = {"Train","Authority","Departure"};
	private Object[][] queueTrainInitialData = new Object[0][queueTrainColumnNames.length];

	private JTable queueRedTable;
	private DefaultTableModel queueRedData;
	
	private JTable queueGreenTable;
	private DefaultTableModel queueGreenData;
	
	private ScheduleJTable queueSelectedTable;
	private DefaultTableModel queueSelectedData;
	
	private JSpinner blockNumberSpinner;
	private JComboBox<String> blockLineComboBox;
	JToggleButton selectedBlockToggle;
	JButton btnRepairBlock;
	JButton btnCloseTrack;
	JLabel selectedBlockOccupiedIndicator;
	JLabel selectedBlockStatusIndicator;
	
	JLabel lblSpeedup;
	JButton btnPause;
	JButton btnPlay;
	int currentSpeedupIndex = 0;
	int[] availableSpeedups = {1,2,4,8,16};
	JButton btnDecSpeed;
	JButton btnIncSpeed;
	
	/**
	 * Threads
	 */
	public ScheduleEditor scheduleEditor;
	public Schedule scheduleForScheduleEditor = null;

	/**
	 * Constants
	 */
	private final int GUI_WINDOW_HEIGHT = 800;
	private final int GUI_WINDOW_WIDTH = 1400;

	/**
	 * Real Time
	 */
	private JLabel clockLabel = new JLabel("00:00:00");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new CtcGui(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CtcGui(CtcCore ctc) {
		this.ctc = ctc;
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		finally{
			initialize();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle("CTC");
		frame.setBounds(100, 100, GUI_WINDOW_WIDTH, GUI_WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		
		JSeparator horizontalBar = new JSeparator();
		horizontalBar.setBackground(Color.BLACK);
		horizontalBar.setBounds(45, 128, 1300, 2);
		frame.getContentPane().add(horizontalBar);
		
		JSeparator horizontalBar2 = new JSeparator();
		horizontalBar2.setBackground(Color.BLACK);
		horizontalBar2.setBounds(45, 625, 1300, 2);
		frame.getContentPane().add(horizontalBar2);
		
		JSeparator leftVerticalBar = new JSeparator();
		leftVerticalBar.setOrientation(SwingConstants.VERTICAL);
		leftVerticalBar.setBackground(Color.BLACK);
		leftVerticalBar.setBounds(450, 148, 2, 440);
		frame.getContentPane().add(leftVerticalBar);
		
		JSeparator rightVerticalBar = new JSeparator();
		rightVerticalBar.setOrientation(SwingConstants.VERTICAL);
		rightVerticalBar.setBackground(Color.BLACK);
		rightVerticalBar.setBounds(920, 148, 2, 440);
		frame.getContentPane().add(rightVerticalBar);
		
		/**
		 * TOP BAR
		 */

		JLabel hazardIcon = new JLabel();
		hazardIcon.setIcon(new ImageIcon(CtcGui.class.getResource("/javax/swing/plaf/metal/icons/ocean/warning.png")));
		hazardIcon.setBounds(509, 657, 37, 32);
		frame.getContentPane().add(hazardIcon);
		
		clockLabel.setFont(new Font("Courier New",Font.BOLD,28));
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clockLabel.setBounds(592, -1, 200, 44);
		contentPane.add(clockLabel);
		
		JLabel lblThroughput = new JLabel("Throughput: ");
		lblThroughput.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblThroughput.setBounds(631, 95, 98, 33);
		contentPane.add(lblThroughput);
		
		lblThroughputAmt = new JLabel("###");
		lblThroughputAmt.setBounds(724, 97, 89, 33);
		contentPane.add(lblThroughputAmt);
		
		/**
		 * LEFT FRAME
		 */
		
		JLabel lblManualTrainCreation = new JLabel("<html><u>Train Creation</u></htm>");
		lblManualTrainCreation.setHorizontalAlignment(SwingConstants.CENTER);
		lblManualTrainCreation.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblManualTrainCreation.setBounds(-14, 142, 468, 33);
		contentPane.add(lblManualTrainCreation);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(18, 403, 221, 210);
		contentPane.add(scrollPane_1);
		
		trainCreationData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		trainCreationTable = new ScheduleJTable(trainCreationData);
		trainCreationTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		scrollPane_1.setViewportView(trainCreationTable);
		
		JLabel lblDepartAt = new JLabel("Depart @");
		lblDepartAt.setFont(new Font("SansSerif", Font.ITALIC, 14));
		lblDepartAt.setBounds(178, 344, 110, 33);
		contentPane.add(lblDepartAt);
		
		trainCreationDepartTime = new JTextField();
		trainCreationDepartTime.setEditable(false);
		trainCreationDepartTime.setText("N/A");
		trainCreationDepartTime.setBounds(173, 366, 66, 39);
		contentPane.add(trainCreationDepartTime);
		trainCreationDepartTime.setColumns(10);
		
		JLabel lblLine = new JLabel("Line");
		lblLine.setFont(new Font("SansSerif", Font.ITALIC, 14));
		lblLine.setBounds(99, 344, 45, 33);
		contentPane.add(lblLine);
			
		trainCreationLine = new JTextField();
		trainCreationLine.setEditable(false);
		trainCreationLine.setText("N/A");
		trainCreationLine.setColumns(10);
		trainCreationLine.setBounds(92, 366, 52, 39);
		contentPane.add(trainCreationLine);
		
		JButton editToDispatchSchedule = new JButton("<html><center>Create/Edit<br>Schedule</center></html>");
		editToDispatchSchedule.setFont(new Font("Dialog", Font.PLAIN, 16));
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
		editToDispatchSchedule.setBounds(251, 403, 171, 64);
		contentPane.add(editToDispatchSchedule);
		
		JButton addToDispatchToQueue = new JButton("<html><center>Add Schedule<br>To Queue \u2192</center></html>");
		addToDispatchToQueue.setFont(new Font("Dialog", Font.PLAIN, 16));
		addToDispatchToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Schedule schedule = trainCreationTable.schedule;
				ctc.addSchedule(schedule.name, schedule);
				updateQueueTable();
				
				//Remove from the creator
				trainCreationData.setDataVector(selectedTrainInitialData,selectedTrainColumnNames);
				openScheduleInTable(trainCreationTable,trainCreationData,null);
			}
		});
		addToDispatchToQueue.setBounds(251, 509, 171, 67);
		contentPane.add(addToDispatchToQueue);
		
		/**
		 * MID FRAME
		 */	
		
		JLabel lblQueue = new JLabel("<html><u>Queue</u></html>");
		lblQueue.setHorizontalAlignment(SwingConstants.CENTER);
		lblQueue.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblQueue.setBounds(450, 142, 472, 33);
		contentPane.add(lblQueue);
		
		JTabbedPane queueTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		queueTabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
		queueTabbedPane.setBounds(489, 174, 406, 187);
		contentPane.add(queueTabbedPane);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		queueTabbedPane.addTab("Green", null, scrollPane_3, null);
		queueGreenData = new DefaultTableModel(queueTrainInitialData,queueTrainColumnNames);
		queueGreenTable = new JTable(queueGreenData);
		queueGreenTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		queueGreenTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = queueGreenTable.rowAtPoint(e.getPoint());
				String trainName = (String) queueGreenData.getValueAt(row, 0);
				Schedule schedule = ctc.getScheduleByName(trainName);
				openScheduleInTable(queueSelectedTable,queueSelectedData,schedule);
			}
		});
		scrollPane_3.setViewportView(queueGreenTable);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		queueTabbedPane.addTab("Red", null, scrollPane_2, null);
		queueRedData = new DefaultTableModel(queueTrainInitialData,queueTrainColumnNames);
		queueRedTable = new JTable(queueRedData);
		queueRedTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		queueRedTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = queueRedTable.rowAtPoint(e.getPoint());
				String trainName = (String) queueRedData.getValueAt(row, 0);
				Schedule schedule = ctc.getScheduleByName(trainName);
				openScheduleInTable(queueSelectedTable,queueSelectedData,schedule);
			}
		});
		scrollPane_2.setViewportView(queueRedTable);
		
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(476, 403, 221, 210);
		contentPane.add(scrollPane_4);
		
		queueSelectedData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		queueSelectedTable = new ScheduleJTable(queueSelectedData);
		queueSelectedTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		scrollPane_4.setViewportView(queueSelectedTable);
		
		JButton editQueueSchedule = new JButton("Edit Schedule");
		editQueueSchedule.setFont(new Font("Dialog", Font.PLAIN, 16));
		editQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName="";
				int row;
				String line = queueTabbedPane.getTitleAt(queueTabbedPane.getSelectedIndex());
				
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

				scheduleForScheduleEditor = ctc.getScheduleByName(trainName);

				Thread scheduleEditorThread = new Thread() {
					public void run() {
						try {
							SwingUtilities.invokeAndWait(openScheduleEditor);
							while(scheduleEditor.editing==true) {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							Schedule schedule = scheduleEditor.currentSchedule;
							ctc.addSchedule(schedule.name,schedule);
							updateQueueTable();
							
							System.out.println("Finished on " + Thread.currentThread());
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				scheduleEditorThread.start();				
			}
		});
		editQueueSchedule.setBounds(714, 403, 171, 41);
		contentPane.add(editQueueSchedule);
		
		JButton deleteQueueSchedule = new JButton("Delete selected");
		deleteQueueSchedule.setFont(new Font("Dialog", Font.PLAIN, 16));
		deleteQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String line = queueTabbedPane.getTitleAt(queueTabbedPane.getSelectedIndex());
				String trainName="";
				if(line=="Red") {
					int row = queueRedTable.getSelectedRow();
					trainName = (String) queueRedData.getValueAt(row, 0);
				}
				else if(line=="Green") {
					int row = queueGreenTable.getSelectedRow();
					trainName = (String) queueGreenData.getValueAt(row, 0);
				}
				ctc.removeScheduleByName(trainName);

				queueSelectedData.setDataVector(selectedTrainInitialData,selectedTrainColumnNames);
				openScheduleInTable(queueSelectedTable,queueSelectedData,null);
				updateQueueTable();
				
			}
		});
		deleteQueueSchedule.setBounds(714, 456, 171, 41);
		contentPane.add(deleteQueueSchedule);
		
		JButton dispatchQueueSchedule = new JButton("Dispatch Now \u2192");
		dispatchQueueSchedule.setFont(new Font("Dialog", Font.PLAIN, 16));
		dispatchQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String line = queueTabbedPane.getTitleAt(queueTabbedPane.getSelectedIndex());
				String trainName="";
				if(line=="Red") {
					int row = queueRedTable.getSelectedRow();
					trainName = (String) queueRedData.getValueAt(row, 0);
				}
				else if(line=="Green") {
					int row = queueGreenTable.getSelectedRow();
					trainName = (String) queueGreenData.getValueAt(row, 0);
				}
				Schedule schedule = ctc.removeScheduleByName(trainName);
				ctc.addTrain(trainName,schedule);
				schedule.dispatched = true;
				
				queueSelectedData.setDataVector(selectedTrainInitialData,selectedTrainColumnNames);
				openScheduleInTable(queueSelectedTable,queueSelectedData,null);
				updateQueueTable();
				updateDispatchedTable();
			}
		});
		dispatchQueueSchedule.setBounds(714, 509, 171, 67);
		contentPane.add(dispatchQueueSchedule);
		
		
			
		/**
		 * RIGHT FRAME
		 */
		JLabel lblDispatchedTrains = new JLabel("<html><u>Dispatched Trains</u></html>");
		lblDispatchedTrains.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispatchedTrains.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblDispatchedTrains.setBounds(920, 142, 468, 33);
		contentPane.add(lblDispatchedTrains);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
		tabbedPane.setBounds(946, 174, 425, 187);
		contentPane.add(tabbedPane);
		
		JScrollPane dispatchedRedScrollPane = new JScrollPane();
		
		dispatchedRedData = new DefaultTableModel(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		dispatchedRedTable = new JTable(dispatchedRedData);
		dispatchedRedTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		dispatchedRedTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = dispatchedRedTable.rowAtPoint(e.getPoint());
				String trainName = (String) dispatchedRedData.getValueAt(row, 0);
				Schedule schedule = ctc.getTrainByName(trainName).schedule;
				openScheduleInTable(dispatchSelectedTable,dispatchSelectedData,schedule);
			}
		});
		
		JScrollPane dispatchedGreenScrollPane = new JScrollPane();
		dispatchedGreenData = new DefaultTableModel(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		dispatchedGreenTable = new JTable(dispatchedGreenData);
		dispatchedGreenTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		dispatchedGreenTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = dispatchedGreenTable.rowAtPoint(e.getPoint());
				String trainName = (String) dispatchedGreenData.getValueAt(row, 0);
				Schedule schedule = ctc.getTrainByName(trainName).schedule;
				openScheduleInTable(dispatchSelectedTable,dispatchSelectedData,schedule);
			}
		});
		dispatchedGreenScrollPane.setViewportView(dispatchedGreenTable);
		tabbedPane.addTab("Green", null, dispatchedGreenScrollPane, null);
		dispatchedRedScrollPane.setViewportView(dispatchedRedTable);
		tabbedPane.addTab("Red", null, dispatchedRedScrollPane, null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(942, 403, 221, 210);
		contentPane.add(scrollPane);
		
		dispatchSelectedData = new DefaultTableModel(selectedTrainInitialData,selectedTrainColumnNames);
		dispatchSelectedTable = new ScheduleJTable(dispatchSelectedData);
		dispatchSelectedTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
		scrollPane.setViewportView(dispatchSelectedTable);
		
		JLabel lblSelectedTrainSchedule = new JLabel("Selected Train's Schedule");
		lblSelectedTrainSchedule.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblSelectedTrainSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedTrainSchedule.setBounds(920, 367, 456, 33);
		contentPane.add(lblSelectedTrainSchedule);
		
		JButton editSelectedDispatchedTrainSchedule = new JButton("Edit Schedule");
		editSelectedDispatchedTrainSchedule.setFont(new Font("Dialog", Font.PLAIN, 16));
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

				scheduleForScheduleEditor = ctc.getTrainByName(trainName).schedule;

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
						ctc.addTrain(schedule.name,schedule);
						updateDispatchedTable();
						
						System.out.println("Finished on " + Thread.currentThread());
					}
				};
				scheduleEditorThread.start();				
			}
		});
		editSelectedDispatchedTrainSchedule.setBounds(1179, 403, 171, 41);
		contentPane.add(editSelectedDispatchedTrainSchedule);
		
		JButton btnimportschedule = new JButton("<html><center>Import<br>Schedule</center></html>");
		btnimportschedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("CSV file", new String[] {"csv"});
				fc.setFileFilter(filter);
				fc.showSaveDialog(frame);
				//File file = fc.getSelectedFile();
				//TODO import CSV file and add schedule to queue
			}
		});
		btnimportschedule.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnimportschedule.setBounds(144, 221, 163, 64);
		frame.getContentPane().add(btnimportschedule);
		
		JLabel lblSelectedTrainsSchedule = new JLabel("Selected Train's Schedule");
		lblSelectedTrainsSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedTrainsSchedule.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblSelectedTrainsSchedule.setBounds(464, 367, 456, 33);
		frame.getContentPane().add(lblSelectedTrainsSchedule);
		
		trainCreationName = new JTextField();
		trainCreationName.setText("N/A");
		trainCreationName.setEditable(false);
		trainCreationName.setColumns(10);
		trainCreationName.setBounds(18, 366, 52, 39);
		frame.getContentPane().add(trainCreationName);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("SansSerif", Font.ITALIC, 14));
		lblName.setBounds(25, 344, 45, 33);
		frame.getContentPane().add(lblName);
		
		JLabel lblAutomatic = new JLabel("Automatic");
		lblAutomatic.setHorizontalAlignment(SwingConstants.CENTER);
		lblAutomatic.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblAutomatic.setBounds(-14, 187, 477, 33);
		frame.getContentPane().add(lblAutomatic);
		
		JLabel lblManual = new JLabel("Manual");
		lblManual.setHorizontalAlignment(SwingConstants.CENTER);
		lblManual.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblManual.setBounds(-14, 297, 477, 33);
		frame.getContentPane().add(lblManual);
		
		JLabel logoPineapple = new JLabel();
		Image img = new ImageIcon(this.getClass().getResource("pineapple_icon.png")).getImage();
		logoPineapple.setIcon(new ImageIcon(img));
		logoPineapple.setBounds(1250,660,138,76);
		contentPane.add(logoPineapple);
		
		JLabel lbltrackStatusAnd = new JLabel("<html><u>Track Status<br> and Maintenance</u></htm>");
		lbltrackStatusAnd.setHorizontalAlignment(SwingConstants.LEFT);
		lbltrackStatusAnd.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbltrackStatusAnd.setBounds(383, 651, 163, 63);
		frame.getContentPane().add(lbltrackStatusAnd);
		
		JLabel selectedBlockLine = new JLabel("Line");
		selectedBlockLine.setFont(new Font("SansSerif", Font.ITALIC, 14));
		selectedBlockLine.setBounds(575, 657, 37, 33);
		frame.getContentPane().add(selectedBlockLine);
		
		JLabel selectedBlockNum = new JLabel("Block");
		selectedBlockNum.setFont(new Font("SansSerif", Font.ITALIC, 14));
		selectedBlockNum.setBounds(631, 657, 37, 33);
		frame.getContentPane().add(selectedBlockNum);
		
		btnCloseTrack = new JButton("Close for Maintenance");
		btnCloseTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Block block = getSelectedBlock();
				block.setMaintenance(true);
				updateSelectedBlock();
			}
		});
		btnCloseTrack.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnCloseTrack.setBounds(815, 672, 200, 32);
		frame.getContentPane().add(btnCloseTrack);
		
		btnRepairBlock = new JButton("Repair Block");
		btnRepairBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Block block = getSelectedBlock();
				block.setMaintenance(false);
				updateSelectedBlock();
			}
		});
		btnRepairBlock.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnRepairBlock.setBounds(815, 705, 200, 32);
		frame.getContentPane().add(btnRepairBlock);
		
		selectedBlockToggle = new JToggleButton("Toggle Switch");
		selectedBlockToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Block block = getSelectedBlock();
				block.getSwitch().setState(selectedBlockToggle.isSelected());
			}
		});
		selectedBlockToggle.setFont(new Font("Dialog", Font.PLAIN, 16));
		selectedBlockToggle.setBounds(815, 639, 200, 32);
		frame.getContentPane().add(selectedBlockToggle);
		
		JLabel lblOccupied = new JLabel("Occupied");
		lblOccupied.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblOccupied.setBounds(725, 688, 70, 33);
		frame.getContentPane().add(lblOccupied);
		
		selectedBlockOccupiedIndicator = new JLabel();
		selectedBlockOccupiedIndicator.setBounds(791, 688, 45, 32);
		setIndicator(selectedBlockOccupiedIndicator,"grey");
		frame.getContentPane().add(selectedBlockOccupiedIndicator);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblStatus.setBounds(745, 655, 52, 33);
		frame.getContentPane().add(lblStatus);
		
		selectedBlockStatusIndicator = new JLabel();
		selectedBlockStatusIndicator.setBounds(791, 655, 45, 32);
		setIndicator(selectedBlockStatusIndicator,"grey");
		frame.getContentPane().add(selectedBlockStatusIndicator);
		
		blockLineComboBox = new JComboBox<String>();
		blockLineComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(blockLineComboBox.getSelectedItem()=="Green") {
					blockNumberSpinner.setModel(new SpinnerNumberModel(1, 1, ctc.greenBlocks.length-1, 1));
				}
				else if(blockLineComboBox.getSelectedItem()=="Red") {
					blockNumberSpinner.setModel(new SpinnerNumberModel(1, 1, ctc.redBlocks.length-1, 1));
				}
				blockNumberSpinner.setValue(1);
			}
		});
		blockLineComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Green", "Red"}));
		blockLineComboBox.setBounds(558, 687, 71, 26);
		frame.getContentPane().add(blockLineComboBox);
		
		blockNumberSpinner = new JSpinner();
		if(blockLineComboBox.getSelectedItem()=="Green") {
			blockNumberSpinner.setModel(new SpinnerNumberModel(1, 1, ctc.greenBlocks.length-1, 1));
		}
		else if(blockLineComboBox.getSelectedItem()=="Red") {
			blockNumberSpinner.setModel(new SpinnerNumberModel(1, 1, ctc.redBlocks.length-1, 1));
		}
		blockNumberSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				updateSelectedBlock();
			}
		});
		blockNumberSpinner.setBounds(631, 686, 70, 28);
		frame.getContentPane().add(blockNumberSpinner);
		
		updateSelectedBlock();
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(709, 638, 325, 101);
		frame.getContentPane().add(panel);
		
		btnPlay = new JButton("<html><center>Play</center></html>");
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(true);
				btnPlay.setEnabled(false);
				ctc.play();
			}
		});
		btnPlay.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnPlay.setBounds(612, 35, 82, 32);
		frame.getContentPane().add(btnPlay);
		
		btnPause = new JButton("<html><center>Pause</center></html>");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(false);
				btnPlay.setEnabled(true);
				ctc.pause();
			}
		});
		btnPause.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnPause.setBounds(690, 35, 82, 32);
		frame.getContentPane().add(btnPause);
		
		JButton btnIncSpeed = new JButton("<html><center>&gt&gt</center></html>");
		btnIncSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSpeedupIndex = Math.min(currentSpeedupIndex+1,availableSpeedups.length-1);
				lblSpeedup.setText(Integer.toString(availableSpeedups[currentSpeedupIndex])+"X");
				ctc.setSpeedup(availableSpeedups[currentSpeedupIndex]);
			}
		});
		btnIncSpeed.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnIncSpeed.setBounds(712, 68, 60, 32);
		frame.getContentPane().add(btnIncSpeed);
		
		btnDecSpeed = new JButton("<html><center>&lt&lt</center></html>");
		btnDecSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSpeedupIndex = Math.max(currentSpeedupIndex-1,0);
				lblSpeedup.setText(Integer.toString(availableSpeedups[currentSpeedupIndex])+"X");
				ctc.setSpeedup(availableSpeedups[currentSpeedupIndex]);
			}
		});
		btnDecSpeed.setFont(new Font("Dialog", Font.PLAIN, 16));
		btnDecSpeed.setBounds(612, 68, 60, 32);
		frame.getContentPane().add(btnDecSpeed);
		
		lblSpeedup = new JLabel("1X");
		lblSpeedup.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeedup.setBounds(673, 67, 39, 33);
		frame.getContentPane().add(lblSpeedup);
	}
	
	private Block getSelectedBlock() {
		Block block = null;
		int blockNumber = (int) blockNumberSpinner.getValue();
		if(blockLineComboBox.getSelectedItem()=="Green") {
			block = ctc.greenBlocks[blockNumber];
		}
		else if(blockLineComboBox.getSelectedItem()=="Red") {
			block = ctc.redBlocks[blockNumber];					
		}
		return block;
	}
	
	private void updateSelectedBlock() {
		Block block = getSelectedBlock();
		
		if(block.STATUS_WORKING==true) {
			//Track is open, check if switch
			if(block.getSwitch() != null) {
				selectedBlockToggle.setEnabled(true);
				selectedBlockToggle.setSelected(block.getSwitch().getState());
			}
			else {
				selectedBlockToggle.setEnabled(false);
			}
			
			btnRepairBlock.setEnabled(false);
			btnCloseTrack.setEnabled(true);
			setIndicator(selectedBlockStatusIndicator,"green");
		}
		else {
			//Track is closed, disable switch toggle regardless
			selectedBlockToggle.setEnabled(false);
			
			btnRepairBlock.setEnabled(true);
			btnCloseTrack.setEnabled(false);
			setIndicator(selectedBlockStatusIndicator,"red");
		}
		
		if(block.getOccupied() == true) {
			setIndicator(selectedBlockOccupiedIndicator,"green");
		}
		else {
			setIndicator(selectedBlockOccupiedIndicator,"grey");
		}
	}

	private void setIndicator(JLabel label, String color) {
		Image imgStatus = new ImageIcon(this.getClass().getResource(color+"StatusIcon.png")).getImage();
		label.setIcon(new ImageIcon(imgStatus));
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
		for(String trainName:ctc.schedules.keySet()) {
			schedule = ctc.getScheduleByName(trainName);
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
		for(String trainName:ctc.trains.keySet()) {
			schedule = ctc.getTrainByName(trainName).schedule;
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
	
	public void repaint() {
		//Update Throughput Label
		lblThroughputAmt.setText(Double.toString(ctc.throughput));
		
		//Update Time
		clockLabel.setText(ctc.currentTime.toString());
		
		//Update the info of the selected block
		updateSelectedBlock();
		
		//Dispatch trains who are scheduled to be dispatched
		//checkQueueForDisaptches();
		
		//Update the locations of trains
		//updateDispatchedTable();
	}
	
	final Runnable openScheduleEditor = new Runnable() {
	    public void run() {
	        scheduleEditor = new ScheduleEditor(ctc,scheduleForScheduleEditor);
	        scheduleEditor.frame.setVisible(true);
	        scheduleEditor.frame.setResizable(false);
	    }
	};
}
