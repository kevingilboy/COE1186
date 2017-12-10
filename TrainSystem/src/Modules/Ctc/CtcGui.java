package Modules.Ctc;

import Modules.TrackModel.Block;
import Modules.TrackModel.Light;
import Modules.TrackModel.Crossing;
import Modules.TrackModel.Station;
import Modules.TrackModel.Switch;
import Shared.SimTime;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;

/*--- REQUIRED LIBRARIES FOR HSS DARK THEME ----*/
import java.awt.GraphicsEnvironment;
import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import java.awt.FontFormatException;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Insets;
/*----------------------------------------------*/

public class CtcGui {

	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Variations of Roboto Condensed Font
	 */
	
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 20);
	Font font_20_bold = new Font("Roboto Condensed Bold", Font.BOLD, 28);
	Font font_24_bold = new Font("Roboto Condensed", Font.BOLD, 38);

	/**
	 * Set any UI configurations done by the UI manager
	 *
	 * NOTE: This method must be called first in the GUI instantiation!
	 */
	public void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
			UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
			UIManager.put("TabbedPane.selected", Color.GRAY);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * JComponent styling wrappers
	 */
	public void stylizeButton(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(102, 0, 153)); // Purple
	}

	public void stylizeButton_Disabled(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.GRAY);
		b.setBackground(new Color(50, 0, 70));
	}

	public void stylizeToggleButton(JToggleButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(102, 0, 153)); // Purple
	}

	public void stylizeToggleButton_Disabled(JToggleButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.GRAY);
		b.setBackground(new Color(50, 0, 70));
	}

	public void stylizeComboBox(JComboBox c){
		c.setFont(font_14_bold);
		((JLabel)c.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		c.setForeground(Color.BLACK);
		c.setBackground(Color.WHITE);
	}

	public void stylizeTextField(JTextField t){
		t.setFont(font_14_bold);
		t.setForeground(Color.BLACK);
		t.setBackground(Color.WHITE);
		t.setHorizontalAlignment(JTextField.CENTER);
	}

	public void stylizeScrollPane(JScrollPane s){
		s.getViewport().setBackground(new Color(36, 39, 45));
		Border b = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		s.setFont(font_16_bold);
		s.setBorder(b);
	}

	public void stylizeTable(JTable t){
		t.getTableHeader().setFont(font_14_bold);
		t.getTableHeader().setBackground(new Color(20, 20, 20));
		t.getTableHeader().setForeground(Color.WHITE);
		t.getTableHeader().setReorderingAllowed(false);

		t.setFont(font_14_bold);
	}

	public void stylizeTabbedPane(JTabbedPane t){
		t.setFont(font_16_bold);
		t.setBackground(Color.DARK_GRAY);
		t.setForeground(Color.BLACK);
	}

	public void stylizeHeadingLabel(JLabel l){
		l.setFont(font_20_bold);
		l.setForeground(Color.WHITE);
		l.setHorizontalAlignment(SwingConstants.LEFT);
	}

	public void stylizeInfoLabel(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Bold(JLabel l){
		l.setHorizontalAlignment(SwingConstants.RIGHT);
		l.setForeground(new Color(234, 201, 87));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Small(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_14_bold);
	}
	
	public void stylizeRadioButton(JRadioButton b) {
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(Color.DARK_GRAY);
		b.setHorizontalAlignment(SwingConstants.LEFT);
	}
	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/


	private Ctc ctc;

	private JFrame frame;
	
	private JLabel lblThroughputAmt;
	
	/*
	 * Dispatched train tables
	 */
	private Object[] dispatchedTrainsColumnNames = {"Train","Location","Sug. Speed","Authority","Passengers"};
	private Object[][] dispatchedTrainsInitialData = new Object[0][dispatchedTrainsColumnNames.length];

	protected ScheduleJTable dispatchSelectedTable;
	private JButton btnSuggestSpeed;
	private JCheckBox chckbxManualOverride;
		
	/*
	 * Creator tables
	 */
	private JButton addToDispatchToQueue;
	private ScheduleJTable trainCreationTable;
	private JTextField trainCreationDepartTime;
	private JComboBox<Line> trainCreationLine;
	private JTextField trainCreationName;
	protected JRadioButton rdbtnFixedBlockMode;
	protected JRadioButton rdbtnMovingBlockMode;
	
	/*
	 * Queue tables
	 */
	private Object[] queueTrainColumnNames = {"Train","Departure"};
	private Object[][] queueTrainInitialData = new Object[0][queueTrainColumnNames.length];
	
	private ScheduleJTable queueSelectedTable;
	private JButton btnDeleteQueueSchedule;
	private JTextField queueDepartTime;
	private JButton btnDispatchQueueSchedule;
	
	/*
	 * Block select variables
	 */
	private JSpinner blockNumberSpinner;
	private JComboBox<Line> blockLineComboBox;
	private JToggleButton selectedBlockToggle;
	private JButton btnRepairBlock;
	private JButton btnCloseTrack;
	private JLabel selectedBlockOccupiedIndicator;
	private JLabel selectedBlockStatusIndicator;
	
	private JLabel lblSpeedup;
	protected JButton btnPause;
	protected JButton btnPlay;
	private int currentSpeedupIndex = 0;
	private int[] availableSpeedups = {1,2,5,10};
	private JButton btnDecSpeed;
	private JButton btnIncSpeed;
	
	/*
	 * Constants
	 */
	private final int GUI_WINDOW_HEIGHT = 800;
	private final int GUI_WINDOW_WIDTH = 1400;

	/*
	 * Real Time
	 */
	private JLabel clockLabel = new JLabel("00:00:00");
	private JTextField suggestedSpeed;


	/**
	 * Create the application.
	 */
	public CtcGui(Ctc ctc) {
		this.ctc = ctc;
		try {
			setLookAndFeel();
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
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setBackground(new Color(26, 29, 35));
		contentPane.setLayout(null);
		
		JSeparator horizontalBar = new JSeparator();
		horizontalBar.setBackground(new Color(36, 39, 45));
		horizontalBar.setForeground(new Color(36, 39, 45));
		horizontalBar.setBounds(45, 128, 1300, 2);
		frame.getContentPane().add(horizontalBar);
		
		JSeparator horizontalBar2 = new JSeparator();
		horizontalBar2.setBackground(new Color(36, 39, 45));
		horizontalBar2.setForeground(new Color(36, 39, 45));
		frame.getContentPane().add(horizontalBar2);
		
		JSeparator leftVerticalBar = new JSeparator();
		leftVerticalBar.setOrientation(SwingConstants.VERTICAL);
		leftVerticalBar.setBackground(new Color(36, 39, 45));
		leftVerticalBar.setForeground(new Color(36, 39, 45));
		leftVerticalBar.setBounds(450, 148, 2, 440);
		frame.getContentPane().add(leftVerticalBar);
		
		JSeparator rightVerticalBar = new JSeparator();
		rightVerticalBar.setOrientation(SwingConstants.VERTICAL);
		rightVerticalBar.setBackground(new Color(36, 39, 45));
		rightVerticalBar.setForeground(new Color(36, 39, 45));
		rightVerticalBar.setBounds(920, 148, 2, 440);
		frame.getContentPane().add(rightVerticalBar);
		
		/**
		 * TOP BAR
		 */
		
		stylizeHeadingLabel(clockLabel);
		clockLabel.setFont(new Font("Courier New",Font.BOLD,28));
		clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clockLabel.setBounds(592, -1, 200, 44);
		contentPane.add(clockLabel);
		
		JLabel lblThroughput = new JLabel("THROUGHPUT: ");
		stylizeInfoLabel_Small(lblThroughput);
		lblThroughput.setBounds(616, 95, 130, 33);
		contentPane.add(lblThroughput);
		
		lblThroughputAmt = new JLabel("###");
		stylizeInfoLabel_Bold(lblThroughputAmt);
		lblThroughputAmt.setHorizontalAlignment(SwingConstants.LEFT);
		lblThroughputAmt.setBounds(724, 97, 400, 33);
		contentPane.add(lblThroughputAmt);
		
		btnPlay = new JButton("<html><center>PLAY</center></html>");
		btnPlay.setEnabled(true);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(true);
				stylizeButton(btnPause);
				btnPlay.setEnabled(false);
				stylizeButton_Disabled(btnPlay);
				ctc.play();
			}
		});
		stylizeButton(btnPlay);
		btnPlay.setBounds(612, 35, 82, 32);
		frame.getContentPane().add(btnPlay);
		
		btnPause = new JButton("<html><center>PAUSE</center></html>");
		btnPause.setEnabled(false);
		stylizeButton_Disabled(btnPause);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(false);
				stylizeButton_Disabled(btnPause);
				btnPlay.setEnabled(true);
				stylizeButton(btnPlay);
				ctc.pause();
			}
		});
		btnPause.setBounds(690, 35, 82, 32);
		frame.getContentPane().add(btnPause);
		
		btnIncSpeed = new JButton("<html><center>&gt&gt</center></html>");
		btnIncSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSpeedupIndex = Math.min(currentSpeedupIndex+1,availableSpeedups.length-1);
				lblSpeedup.setText(Integer.toString(availableSpeedups[currentSpeedupIndex])+"X");
				ctc.setSpeedup(availableSpeedups[currentSpeedupIndex]);
			}
		});
		stylizeButton(btnIncSpeed);
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
		stylizeButton(btnDecSpeed);
		btnDecSpeed.setBounds(612, 68, 60, 32);
		frame.getContentPane().add(btnDecSpeed);
		
		lblSpeedup = new JLabel("1X");
		stylizeInfoLabel_Bold(lblSpeedup);
		lblSpeedup.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeedup.setBounds(673, 67, 39, 33);
		frame.getContentPane().add(lblSpeedup);
		
		/**
		 * LEFT FRAME
		 */
		
		JButton btnimportschedule = new JButton("<html><center>IMPORT<br>SCHEDULE</center></html>");
		btnimportschedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Schedule schedule;
				BufferedReader br = null;
				String currentLine = "";
				String delimeter = ",";

				try {
					//Let user select a file
					JFileChooser fc = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("CSV file", new String[] {"csv"});
					fc.setFileFilter(filter);
					fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
					fc.showSaveDialog(frame);					
					File f = fc.getSelectedFile();
					
					String filename[] = f.getName().replace(".csv","").split("_");
					
					//Set the line
					Line line = null;
					if(filename[0].equals("GREEN")) {
						line = Line.GREEN;
					}
					else if(filename[0].equals("RED")) {
						line = Line.RED;
					}
					else {
						throw new IOException();
					}
					schedule = new Schedule(line);
							
					//Set the name
					schedule.name = filename[1];
					
					//Set the departure time
					String time = filename[2].replace("-",":");
					schedule.departureTime = new SimTime(time);
					
					//Read the file
					FileReader fr = new FileReader(f);
					br = new BufferedReader(fr);
					br.readLine(); //Skip first line
					
					int stopNum = 0;
					while ((currentLine = br.readLine()) != null){
						String [] csvline = currentLine.split(delimeter);
						schedule.addStop(stopNum++,Integer.parseInt(csvline[0]), new SimTime(csvline[1]));
					}
					
					//Update the GUI to reflect changes
					trainCreationTable.clear();
					trainCreationTable.schedule = schedule;
					trainCreationTable.fireScheduleChanged();
					trainCreationDepartTime.setText(schedule.departureTime.toString());
					trainCreationName.setText(schedule.name);
					trainCreationLine.setSelectedItem(schedule.line.toString());

					//Enable "add to queue" buttons
					enableTrainCreationComponents();					
				} catch (FileNotFoundException e) {
		        	e.printStackTrace();
		    	} catch (IOException e) {
		        	e.printStackTrace();
		    	} finally {
		        	if (br != null) {
		            	try {
		                	br.close();
		            	} catch (IOException e) {
		                	e.printStackTrace();
		            	}
		        	}
		    	}
			}
		});
		stylizeButton(btnimportschedule);
		btnimportschedule.setBounds(144, 221, 163, 64);
		frame.getContentPane().add(btnimportschedule);
		
		JLabel lblSelectedTrainsSchedule = new JLabel("SELECTED TRAIN'S SCHEDULE");
		stylizeInfoLabel(lblSelectedTrainsSchedule);
		lblSelectedTrainsSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedTrainsSchedule.setBounds(464, 367, 456, 33);
		frame.getContentPane().add(lblSelectedTrainsSchedule);
		
		JLabel lblName = new JLabel("NAME");
		stylizeInfoLabel_Small(lblName);
		lblName.setBounds(25, 336, 45, 33);
		frame.getContentPane().add(lblName);
		
		JLabel lblAutomatic = new JLabel("AUTOMATIC");
		stylizeInfoLabel(lblAutomatic);
		lblAutomatic.setHorizontalAlignment(SwingConstants.CENTER);
		lblAutomatic.setBounds(-14, 187, 477, 33);
		frame.getContentPane().add(lblAutomatic);
		
		JLabel lblManual = new JLabel("MANUAL");
		stylizeInfoLabel(lblManual);
		lblManual.setHorizontalAlignment(SwingConstants.CENTER);
		lblManual.setBounds(-14, 297, 477, 33);
		frame.getContentPane().add(lblManual);
		
		JLabel lblManualTrainCreation = new JLabel("<html>TRAIN CREATION</htm>");
		stylizeHeadingLabel(lblManualTrainCreation);
		lblManualTrainCreation.setHorizontalAlignment(SwingConstants.CENTER);
		lblManualTrainCreation.setBounds(-14, 142, 468, 33);
		contentPane.add(lblManualTrainCreation);
		
		trainCreationName = new JTextField();
		trainCreationName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String name = trainCreationName.getText();
				if(name!=null && !name.equals("")) {
					trainCreationTable.schedule.setName(name);
				}
				enableTrainCreationComponents();
			}
		});
		stylizeTextField(trainCreationName);
		trainCreationName.setColumns(12);
		trainCreationName.setBounds(18, 366, 52, 39);
		frame.getContentPane().add(trainCreationName);
		
		JLabel lblLine = new JLabel("LINE");
		stylizeInfoLabel_Small(lblLine);
		lblLine.setBounds(102, 336, 45, 33);
		contentPane.add(lblLine);
			
		trainCreationLine = new JComboBox<Line>();
		trainCreationLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trainCreationTable.setSchedule(new Schedule((Line)trainCreationLine.getSelectedItem()));
				
				//Restore name and time
				String name = trainCreationName.getText();
				String time = trainCreationDepartTime.getText();
				
				Boolean validName = name!=null && !name.equals("");
				Boolean validTime = SimTime.isValid(time);
				
				if(validName) {
					trainCreationTable.schedule.setName(name);
				}
				if(validTime) {
					trainCreationTable.schedule.setDepartureTime(new SimTime(time));
					trainCreationTable.fireScheduleChanged();	
				}
				
				enableTrainCreationComponents();
			}
		});
		trainCreationLine.setModel(new DefaultComboBoxModel<Line>(Line.values()));
		stylizeComboBox(trainCreationLine);
		trainCreationLine.setBounds(80, 366, 80, 39);
		contentPane.add(trainCreationLine);
		
		JLabel lblDepartAt = new JLabel("DEPART @");
		stylizeInfoLabel_Small(lblDepartAt);
		lblDepartAt.setBounds(178, 336, 110, 33);
		contentPane.add(lblDepartAt);
		
		trainCreationDepartTime = new JTextField();
		trainCreationDepartTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String time = trainCreationDepartTime.getText();
				if(SimTime.isValid(time)) {
					trainCreationTable.schedule.setDepartureTime(new SimTime(time));
					trainCreationTable.fireScheduleChanged();
				}
				enableTrainCreationComponents();
			}
		});
		stylizeTextField(trainCreationDepartTime);
		trainCreationDepartTime.setBounds(173, 366, 66, 39);
		contentPane.add(trainCreationDepartTime);
		trainCreationDepartTime.setColumns(12);
		
		addToDispatchToQueue = new JButton("<html><center>ADD SCHEDULE<br>TO QUEUE \u2192</center></html>");
		addToDispatchToQueue.setEnabled(false);
		stylizeButton_Disabled(addToDispatchToQueue);
		addToDispatchToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Schedule schedule = trainCreationTable.schedule;
				ctc.addSchedule(schedule.name, schedule);
				updateQueueTable();
				
				//Remove from the creator
				trainCreationTable.clear();
				trainCreationName.setText("");
				trainCreationDepartTime.setText("");
				
				trainCreationTable.setSchedule(new Schedule((Line)trainCreationLine.getSelectedItem()));
				enableTrainCreationComponents();
				
				trainCreationTable.setEnabled(false);
				addToDispatchToQueue.setEnabled(false);
				stylizeButton_Disabled(addToDispatchToQueue);
			}
		});
		stylizeButton(addToDispatchToQueue);
		addToDispatchToQueue.setBounds(251, 457, 171, 67);
		contentPane.add(addToDispatchToQueue);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		stylizeScrollPane(scrollPane_1);
		scrollPane_1.setEnabled(false);
		scrollPane_1.setBounds(18, 415, 221, 210);
		contentPane.add(scrollPane_1);
		
		trainCreationTable = new ScheduleJTable();
		stylizeTable(trainCreationTable);
		trainCreationTable.setEnabled(false);
		trainCreationTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				enableTrainCreationComponents();
			}
		});
		scrollPane_1.setViewportView(trainCreationTable);
		trainCreationTable.setSchedule(new Schedule((Line)trainCreationLine.getSelectedItem()));
		enableTrainCreationComponents();
		
		/**
		 * MID FRAME
		 */	
		
		JLabel lblQueue = new JLabel("<html>QUEUE</html>");
		stylizeHeadingLabel(lblQueue);
		lblQueue.setHorizontalAlignment(SwingConstants.CENTER);
		lblQueue.setBounds(450, 142, 472, 33);
		contentPane.add(lblQueue);
		
		JTabbedPane queueTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		queueTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(queueSelectedTable!=null) {
					queueSelectedTable.clear();
					queueDepartTime.setText("");
					
					btnDeleteQueueSchedule.setEnabled(false);
					stylizeButton_Disabled(btnDeleteQueueSchedule);
					btnDispatchQueueSchedule.setEnabled(false);
					stylizeButton_Disabled(btnDispatchQueueSchedule);
					
					for(Line line : Line.values()) {
						line.queueTable.clearSelection();
					}
				}
			}
		});
		queueTabbedPane.setBounds(489, 174, 406, 187);
		contentPane.add(queueTabbedPane);
		
		for(Line line : Line.values()) {
			JScrollPane scrollPane = new JScrollPane();
			stylizeScrollPane(scrollPane);
			queueTabbedPane.addTab(line.toString(), null, scrollPane, null);
			line.queueData = new DefaultTableModel(queueTrainInitialData,queueTrainColumnNames);
			line.queueTable = new JTable(line.queueData);
			stylizeTable(line.queueTable);
			line.queueTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int row = line.queueTable.rowAtPoint(e.getPoint());
					String trainName = (String) line.queueData.getValueAt(row, 0);
					Schedule schedule = ctc.getScheduleByName(trainName);
					queueSelectedTable.setSchedule(schedule);
					queueDepartTime.setText(schedule.departureTime.toString());
					btnDeleteQueueSchedule.setEnabled(true);
					stylizeButton(btnDeleteQueueSchedule);
					btnDispatchQueueSchedule.setEnabled(true);
					stylizeButton(btnDispatchQueueSchedule);
					queueDepartTime.setEnabled(true);
				}
			});
			scrollPane.setViewportView(line.queueTable);
		}
		stylizeTabbedPane(queueTabbedPane);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		stylizeScrollPane(scrollPane_4);
		scrollPane_4.setBounds(476, 403, 221, 210);
		contentPane.add(scrollPane_4);
		
		queueSelectedTable = new ScheduleJTable();
		stylizeTable(queueSelectedTable);
		scrollPane_4.setViewportView(queueSelectedTable);
		
		btnDeleteQueueSchedule = new JButton("DELETE SELECTED");
		btnDeleteQueueSchedule.setEnabled(false);
		stylizeButton_Disabled(btnDeleteQueueSchedule);
		btnDeleteQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Line line = getLineByName(queueTabbedPane.getTitleAt(queueTabbedPane.getSelectedIndex()));
				int row = line.queueTable.getSelectedRow();
				String trainName = (String) line.queueData.getValueAt(row, 0);
				
				ctc.removeScheduleByName(trainName);
				updateQueueTable();

				queueSelectedTable.clear();
				queueDepartTime.setText("");
				
				btnDeleteQueueSchedule.setEnabled(false);
				stylizeButton_Disabled(btnDeleteQueueSchedule);
				btnDispatchQueueSchedule.setEnabled(false);
				stylizeButton_Disabled(btnDispatchQueueSchedule);
				//manualSpeedSetEnabled(false);
			}
		});
		btnDeleteQueueSchedule.setBounds(713, 402, 171, 41);
		contentPane.add(btnDeleteQueueSchedule);
		
		queueDepartTime = new JTextField();
		queueDepartTime.setEnabled(false);
		queueDepartTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String time = queueDepartTime.getText();
				if(SimTime.isValid(time)) {
					queueSelectedTable.schedule.setDepartureTime(new SimTime(time));
					queueSelectedTable.fireScheduleChanged();
					updateQueueTable();					
				}
			}
		});
		stylizeTextField(queueDepartTime);
		queueDepartTime.setColumns(12);
		queueDepartTime.setBounds(819, 467, 66, 39);
		frame.getContentPane().add(queueDepartTime);
		
		JLabel lblAutoDepart = new JLabel("AUTO DEPART:");
		stylizeInfoLabel_Small(lblAutoDepart);
		lblAutoDepart.setBounds(716, 470, 136, 33);
		frame.getContentPane().add(lblAutoDepart);
		
		JLabel lblOr = new JLabel("- OR -");
		stylizeInfoLabel_Small(lblOr);
		lblOr.setHorizontalAlignment(SwingConstants.CENTER);
		lblOr.setBounds(714, 491, 171, 33);
		frame.getContentPane().add(lblOr);
		
		btnDispatchQueueSchedule = new JButton("DISPATCH NOW \u2192");
		btnDispatchQueueSchedule.setEnabled(false);
		stylizeButton_Disabled(btnDispatchQueueSchedule);
		btnDispatchQueueSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Line line = getLineByName(queueTabbedPane.getTitleAt(queueTabbedPane.getSelectedIndex()));
				int row = line.queueTable.getSelectedRow();
				String trainName = (String) line.queueData.getValueAt(row, 0);

				ctc.dispatchTrain(trainName);
				
				queueSelectedTable.clear();
				queueDepartTime.setText("");
				
				updateQueueTable();
				updateDispatchedTable();
				
				btnDeleteQueueSchedule.setEnabled(false);	
				stylizeButton_Disabled(btnDeleteQueueSchedule);
			}
		});
		btnDispatchQueueSchedule.setBounds(714, 520, 171, 67);
		contentPane.add(btnDispatchQueueSchedule);
		
			
		/**
		 * RIGHT FRAME
		 */
		JLabel lblDispatchedTrains = new JLabel("<html>DISPATCHED TRAINS</html>");
		stylizeHeadingLabel(lblDispatchedTrains);
		lblDispatchedTrains.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispatchedTrains.setBounds(920, 142, 468, 33);
		contentPane.add(lblDispatchedTrains);
		
		JTabbedPane dispatchedTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		dispatchedTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(dispatchSelectedTable!=null) {
					dispatchSelectedTable.clear();
					enableManualSpeedComponents();
					for(Line line : Line.values()) {
						line.dispatchedTable.clearSelection();
					}
				}
			}
		});
		dispatchedTabbedPane.setBounds(946, 174, 425, 187);
		contentPane.add(dispatchedTabbedPane);
		
		for(Line line : Line.values()) {
			JScrollPane scrollPane = new JScrollPane();
			stylizeScrollPane(scrollPane);
			
			line.dispatchedData = new DefaultTableModel(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
			line.dispatchedTable = new JTable(line.dispatchedData);
			stylizeTable(line.dispatchedTable);
			line.dispatchedTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int row = line.dispatchedTable.rowAtPoint(e.getPoint());
					String trainName = (String) line.dispatchedData.getValueAt(row, 0);
					Train train = ctc.getTrainByName(trainName);
					dispatchSelectedTable.setSchedule(train.schedule);
					enableManualSpeedComponents();
				}
			});
			scrollPane.setViewportView(line.dispatchedTable);
			dispatchedTabbedPane.addTab(line.toString(), null, scrollPane, null);
		}
		stylizeTabbedPane(dispatchedTabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		stylizeScrollPane(scrollPane);
		scrollPane.setBounds(942, 403, 221, 210);
		contentPane.add(scrollPane);
		
		dispatchSelectedTable = new ScheduleJTable();
		stylizeTable(dispatchSelectedTable);
		scrollPane.setViewportView(dispatchSelectedTable);
		
		JLabel lblSelectedTrainSchedule = new JLabel("SELECTED TRAIN'S SCHEDULE");
		stylizeInfoLabel(lblSelectedTrainSchedule);
		lblSelectedTrainSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedTrainSchedule.setBounds(920, 367, 456, 33);
		contentPane.add(lblSelectedTrainSchedule);
		
		JLabel lblSuggestSpeed = new JLabel("<html>SUGGEST<br>SPEED<br>(mph)</html>");
		stylizeInfoLabel_Small(lblSuggestSpeed);
		lblSuggestSpeed.setBounds(1175, 442, 89, 70);
		frame.getContentPane().add(lblSuggestSpeed);
		
		suggestedSpeed = new JTextField();
		stylizeTextField(suggestedSpeed);
		suggestedSpeed.setColumns(12);
		suggestedSpeed.setBounds(1250, 441, 52, 35);
		frame.getContentPane().add(suggestedSpeed);
		
		btnSuggestSpeed = new JButton("SEND");
		btnSuggestSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String speed = suggestedSpeed.getText();
				if(speed.matches(".*\\d+.*")) {
					Train train = ctc.getTrainByName(dispatchSelectedTable.schedule.name);
					train.manualSpeed = Integer.parseInt(speed) * 1.60934;
				}
			}
		});
		stylizeButton_Disabled(btnSuggestSpeed);
		btnSuggestSpeed.setBounds(1310, 441, 80, 32);
		frame.getContentPane().add(btnSuggestSpeed);
		
		chckbxManualOverride = new JCheckBox("MANUAL OVERRIDE");
		chckbxManualOverride.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Train train = ctc.getTrainByName(dispatchSelectedTable.schedule.name);
				
				//Switch into manual mode with -1 speed so that the suggestion is ignored
				train.manualSpeedMode = chckbxManualOverride.isSelected();
				train.manualSpeed = -1;
				
				//Enable the input textbox and send button
				enableManualSpeedComponents();
			}
		});
		chckbxManualOverride.setBackground(new Color(20,20,20));
		chckbxManualOverride.setForeground(Color.WHITE);
		chckbxManualOverride.setBounds(1250, 478, 135, 18);
		frame.getContentPane().add(chckbxManualOverride);
		
		enableManualSpeedComponents();
		
		/**
		 * BOTTOM FRAME
		 */
		
		JLabel logoPineapple = new JLabel();
		Image img = new ImageIcon(this.getClass().getResource("pineapple_icon.png")).getImage();
		logoPineapple.setIcon(new ImageIcon(img));
		logoPineapple.setBounds(1288,682,138,76);
		contentPane.add(logoPineapple);
		
		JLabel lbltrackStatusAnd = new JLabel("<html>TRACK STATUS<br> AND MAINTENANCE</html>");
		stylizeInfoLabel(lbltrackStatusAnd);
		lbltrackStatusAnd.setHorizontalAlignment(SwingConstants.LEFT);
		lbltrackStatusAnd.setBounds(251, 650, 163, 63);
		frame.getContentPane().add(lbltrackStatusAnd);

		JLabel hazardIcon = new JLabel();
		hazardIcon.setIcon(new ImageIcon(CtcGui.class.getResource("/javax/swing/plaf/metal/icons/ocean/warning.png")));
		hazardIcon.setBounds(377, 656, 37, 32);
		frame.getContentPane().add(hazardIcon);
		
		JLabel selectedBlockLine = new JLabel("LINE");
		stylizeInfoLabel_Small(selectedBlockLine);
		selectedBlockLine.setBounds(443, 656, 37, 33);
		frame.getContentPane().add(selectedBlockLine);
		
		JLabel selectedBlockNum = new JLabel("BLOCK");
		stylizeInfoLabel_Small(selectedBlockNum);
		selectedBlockNum.setBounds(499, 656, 100, 33);
		frame.getContentPane().add(selectedBlockNum);
		
		btnCloseTrack = new JButton("CLOSE FOR MAINTENANCE");
		stylizeButton(btnCloseTrack);
		btnCloseTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Block block = getSelectedBlock();
				ctc.setBlockMaintenance((Line)blockLineComboBox.getSelectedItem(),block.getId());
				updateSelectedBlock(true);
			}
		});
		btnCloseTrack.setBounds(683, 671, 230, 32);
		frame.getContentPane().add(btnCloseTrack);
		
		btnRepairBlock = new JButton("REPAIR BLOCK");
		stylizeButton(btnRepairBlock);
		btnRepairBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Block block = getSelectedBlock();
				ctc.repairBlock((Line)blockLineComboBox.getSelectedItem(),block.getId());
				updateSelectedBlock(true);
			}
		});
		btnRepairBlock.setBounds(683, 704, 230, 32);
		frame.getContentPane().add(btnRepairBlock);
		
		selectedBlockToggle = new JToggleButton("TOGGLE SWITCH");
		stylizeToggleButton_Disabled(selectedBlockToggle);
		selectedBlockToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Block block = getSelectedBlock();
				boolean success = ctc.setSwitchState((Line)blockLineComboBox.getSelectedItem(),block.getId(),selectedBlockToggle.isSelected());
				if(!success) {
					stylizeToggleButton(selectedBlockToggle);
					selectedBlockToggle.setSelected(selectedBlockToggle.isSelected());
				}
				else {
					stylizeToggleButton_Disabled(selectedBlockToggle);
					updateSelectedBlock(true);
				}
			}
		});
		selectedBlockToggle.setBounds(683, 638, 230, 32);
		frame.getContentPane().add(selectedBlockToggle);
		
		JLabel lblOccupied = new JLabel("OCCUPIED");
		stylizeInfoLabel_Small(lblOccupied);
		lblOccupied.setBounds(584, 687, 70, 33);
		frame.getContentPane().add(lblOccupied);
		
		selectedBlockOccupiedIndicator = new JLabel();
		selectedBlockOccupiedIndicator.setBounds(654, 687, 45, 32);
		setIndicator(selectedBlockOccupiedIndicator,"grey");
		frame.getContentPane().add(selectedBlockOccupiedIndicator);
		
		JLabel lblStatus = new JLabel("STATUS");
		stylizeInfoLabel_Small(lblStatus);
		lblStatus.setBounds(584, 654, 52, 33);
		frame.getContentPane().add(lblStatus);
		
		selectedBlockStatusIndicator = new JLabel();
		selectedBlockStatusIndicator.setBounds(654, 654, 45, 32);
		setIndicator(selectedBlockStatusIndicator,"grey");
		frame.getContentPane().add(selectedBlockStatusIndicator);
		
		blockLineComboBox = new JComboBox<Line>();
		blockLineComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBlockSpinnerLimits();
			}
		});
		stylizeComboBox(blockLineComboBox);
		blockLineComboBox.setModel(new DefaultComboBoxModel<Line>(Line.values()));
		blockLineComboBox.setBounds(426, 686, 71, 26);
		frame.getContentPane().add(blockLineComboBox);
		
		blockNumberSpinner = new JSpinner();
		setBlockSpinnerLimits();
		blockNumberSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				updateSelectedBlock(true);
			}
		});
		blockNumberSpinner.setBounds(499, 685, 70, 28);
		frame.getContentPane().add(blockNumberSpinner);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(20, 20, 20));
		panel.setBounds(577, 637, 325, 101);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(20, 20, 20));
		panel_1.setBounds(704, 455, 191, 145);
		frame.getContentPane().add(panel_1);
		
		JButton btnJustDoIt = new JButton("JUST DO IT");
		stylizeButton(btnJustDoIt);
		btnJustDoIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctc.testDispatch();
			}
		});
		btnJustDoIt.setBounds(1214, 520, 136, 68);
		frame.getContentPane().add(btnJustDoIt);
		
		JButton btnR1 = new JButton("R1");
		stylizeButton(btnR1);
		btnR1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctc.launchWaysideGui(2);
			}
		});
		btnR1.setBounds(925, 667, 65, 32);
		frame.getContentPane().add(btnR1);
		
		JButton btnR2 = new JButton("R2");
		stylizeButton(btnR2);
		btnR2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctc.launchWaysideGui(3);
			}
		});
		btnR2.setBounds(996, 667, 65, 32);
		frame.getContentPane().add(btnR2);
		
		JButton btnG2 = new JButton("G2");
		stylizeButton(btnG2);
		btnG2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctc.launchWaysideGui(1);
			}
		});
		btnG2.setBounds(996, 700, 65, 32);
		frame.getContentPane().add(btnG2);
		
		JButton btnG1 = new JButton("G1");
		stylizeButton(btnG1);
		btnG1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctc.launchWaysideGui(0);
			}
		});
		btnG1.setBounds(925, 700, 65, 32);
		frame.getContentPane().add(btnG1);
		
		JLabel lblwaysideGuis = new JLabel("<html><center>WAYSIDE GUIs</center></html>");
		stylizeInfoLabel(lblwaysideGuis);
		lblwaysideGuis.setHorizontalAlignment(SwingConstants.CENTER);
		lblwaysideGuis.setBounds(920, 621, 200, 57);
		frame.getContentPane().add(lblwaysideGuis);
		
		rdbtnFixedBlockMode = new JRadioButton("Fixed Block Mode");
		stylizeRadioButton(rdbtnFixedBlockMode);
		rdbtnFixedBlockMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnMovingBlockMode.setSelected(false);
				ctc.enableMovingBlockMode(false);
			}
		});
		rdbtnFixedBlockMode.setSelected(true);
		rdbtnFixedBlockMode.setBounds(261, 548, 153, 18);
		frame.getContentPane().add(rdbtnFixedBlockMode);
		
		rdbtnMovingBlockMode = new JRadioButton("Moving Block Mode");
		stylizeRadioButton(rdbtnMovingBlockMode);
		rdbtnMovingBlockMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnFixedBlockMode.setSelected(false);
				ctc.enableMovingBlockMode(true);
			}
		});
		rdbtnMovingBlockMode.setBounds(261, 566, 153, 18);
		frame.getContentPane().add(rdbtnMovingBlockMode);
	}
	
	/*
	 * CREATION TABLE
	 */
	private void enableTrainCreationComponents() {
		String name = trainCreationName.getText();
		String time = trainCreationDepartTime.getText();
		
		Boolean validName = name!=null && !name.equals("");
		Boolean validTime = SimTime.isValid(time);
		
		if(validName && validTime) {
			//If valid name and time, allow schedule editing
			trainCreationTable.setEnabled(true);
	
			//If valid name and time, and schedule has one stop then allow schedule to be dispatched
			if(trainCreationTable.schedule.stops.size()>0 && trainCreationTable.checkDataValid()) {
				addToDispatchToQueue.setEnabled(true);
				stylizeButton(addToDispatchToQueue);
			}
			else {
				addToDispatchToQueue.setEnabled(false);
				stylizeButton_Disabled(addToDispatchToQueue);
			}		
		}
		else {
			//Else disable table and ability to add to queue
			trainCreationTable.setEnabled(false);
			addToDispatchToQueue.setEnabled(false);
			stylizeButton_Disabled(addToDispatchToQueue);
		}
	}
	
	/*
	 * QUEUE TABLE
	 */	
	private void updateQueueTable(){
		//Clear the red and green tables
		for(Line line : Line.values()) {
			line.queueData.setDataVector(queueTrainInitialData, queueTrainColumnNames);
		}
		
		//Cycle through each dispatched train's schedule
		Schedule schedule;
		for(String name:ctc.schedules.keySet()) {
			schedule = ctc.getScheduleByName(name);
			Object[] row = {schedule.name,schedule.departureTime.toString()};
			schedule.line.queueData.addRow(row);
		}

		//Update the tables in the GUI
		for(Line line : Line.values()) {
			line.queueData.fireTableDataChanged();
		}
		
		//If a schedule was selected, restore the selection in the table
		if(queueSelectedTable.schedule!=null) {
			for(int i=0;i<queueSelectedTable.schedule.line.queueData.getRowCount();i++) {
				if(queueSelectedTable.schedule.line.queueData.getValueAt(i, 0).equals(queueSelectedTable.schedule.name)) {
					queueSelectedTable.schedule.line.queueTable.setRowSelectionInterval(i, i);
					break;
				}
			}
		}
	}
	
	protected void autoDispatchFromQueue(String name) {
		//If the dispatched train was the one selected, clear the selected 
		//table and disable appropriate buttons
		if(queueSelectedTable.schedule!=null && queueSelectedTable.schedule.name==name) {
			queueSelectedTable.clear();	
			queueDepartTime.setText("");
			btnDispatchQueueSchedule.setEnabled(false);
			stylizeButton_Disabled(btnDispatchQueueSchedule);
			queueDepartTime.setEnabled(false);
			btnDeleteQueueSchedule.setEnabled(false);
			stylizeButton_Disabled(btnDeleteQueueSchedule);
		}
		
		//Update the queue and dispatch tables to reflect changes
		updateQueueTable();
		updateDispatchedTable();
	}
	
	/*
	 * DISPATCH TABLE
	 */
	private boolean updateDispatchedTable(){
		//Clear the red and green tables
		for(Line line : Line.values()) {
			line.dispatchedData.setDataVector(dispatchedTrainsInitialData, dispatchedTrainsColumnNames);
		}

		//Cycle through each dispatched train's schedule
		Train train;
		for(String trainName:ctc.trains.keySet()) {
			train = ctc.getTrainByName(trainName);
			//Object[] row; //build the row here, but for now we fake the functionality below
			Object[] row = {train.name,
					train.line.blocks[train.currLocation],
					(train.dwelling) ? "Dwelling" : String.format("%.2f",train.suggestedSpeed*0.621371)+" mph",
					String.format("%.3f",train.authority* 0.000621371192237)+" mi",
					train.passengers};
			train.line.dispatchedData.addRow(row);
		}

		//Update the tables in the GUI
		for(Line line : Line.values()) {
			line.dispatchedData.fireTableDataChanged();
		}
		
		//If a schedule was selected, restore the selection in the table
		if(dispatchSelectedTable.schedule!=null) {
			for(int i=0;i<dispatchSelectedTable.schedule.line.dispatchedData.getColumnCount();i++) {
				if(dispatchSelectedTable.schedule.line.dispatchedData.getValueAt(i, 0).equals(dispatchSelectedTable.schedule.name)) {
					dispatchSelectedTable.schedule.line.dispatchedTable.setRowSelectionInterval(i, i);
					break;
				}
			}
		}
		
		return true;
	}
	
	private void enableManualSpeedComponents() {
		if(dispatchSelectedTable.schedule == null) {
			btnSuggestSpeed.setEnabled(false);
			suggestedSpeed.setEnabled(false);
			suggestedSpeed.setText("");
			chckbxManualOverride.setSelected(false);
			chckbxManualOverride.setEnabled(false);
			return;
		}
		
		Train train = ctc.getTrainByName(dispatchSelectedTable.schedule.name);
		
		if(train.manualSpeedMode) {
			btnSuggestSpeed.setEnabled(true);
			suggestedSpeed.setEnabled(true);
			suggestedSpeed.setText(train.manualSpeed>=0 ? String.format("%.2f",train.manualSpeed*0.621371192237) : "");
			chckbxManualOverride.setSelected(true);
			chckbxManualOverride.setEnabled(true);
		}
		else {
			btnSuggestSpeed.setEnabled(false);
			suggestedSpeed.setEnabled(false);
			suggestedSpeed.setText("");
			chckbxManualOverride.setSelected(false);
			chckbxManualOverride.setEnabled(true);
		}

		stylizeButton(btnSuggestSpeed);
	}

	/*
	 * BLOCKS
	 */
	protected void setBlockSpinnerLimits() {
		Line line = (Line)blockLineComboBox.getSelectedItem();

		blockNumberSpinner.setModel(new SpinnerNumberModel(1, 1, line.blocks.length-1, 1));
		blockNumberSpinner.setValue(1);
	}

	private Block getSelectedBlock() {
		Line line = (Line)blockLineComboBox.getSelectedItem();
		int blockNumber = (int) blockNumberSpinner.getValue();
		
		Block block = line.blocks[blockNumber - 1];

		return block;
	}
	
	void updateSelectedBlock(boolean forceFetchData) {
		Block block = getSelectedBlock();
		Line line = (Line)blockLineComboBox.getSelectedItem();
		
		if(forceFetchData) {
			ctc.updateLocalBlockFromWayside(line,block.getId());
		}
		
		if(block.getStatus()) {
			setIndicator(selectedBlockStatusIndicator,"green");
		}
		else {
			setIndicator(selectedBlockStatusIndicator,"red");
		}
		
		if(block.getOccupied()) {
			setIndicator(selectedBlockOccupiedIndicator,"green");
		}
		else {
			setIndicator(selectedBlockOccupiedIndicator,"grey");
		}
		
		if(block.getSwitch() != null) {
			selectedBlockToggle.setEnabled(true);
			selectedBlockToggle.setSelected(block.getSwitch().getState());
		}
		else {
			selectedBlockToggle.setEnabled(false);
		}		
	}

	private void setIndicator(JLabel label, String color) {
		Image imgStatus = new ImageIcon(this.getClass().getResource("statusIcon_" + color + ".png")).getImage();
		label.setIcon(new ImageIcon(imgStatus));
	}

	/*
	 * HELPERS
	 */
	private Line getLineByName(String s) {
		s = s.toLowerCase();
		Line line = null;
		for(Line l : Line.values()) {
			if(s.equals(l.toString().toLowerCase())){
				line = l;
			}
		}
		return line;
	}	
	
	
	public boolean repaint() {
		//Update Throughput Label
		lblThroughputAmt.setText(Integer.toString((int)ctc.throughput) + " passengers/hr");
		
		//Update Time
		clockLabel.setText(ctc.currentTime.toString());
		
		//Update the info of the selected block
		updateSelectedBlock(false);
		
		//Update the locations of trains
		while(!updateDispatchedTable());
		
		return true;
	}
}
