package Modules.TrackModel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Window.Type;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Choice;
import javax.swing.UIManager;
import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.List;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import java.text.DecimalFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
/*----------------------------------------------*/

import java.util.*;

@SuppressWarnings("unchecked")
public class TrackModelGUI implements ActionListener{
	
	/**
	 * CONVERSION FACTORS FOR DISPLAY
	 */
	private final double METERS_TO_YARDS_FACTOR = 1.09361;
	private final double KPH_TO_MPH_FACTOR = 0.621371;

	/**
	 * TRACK MODEL REFERENCES
	 */
	private TrackModel trackModel;
	private ArrayList<Block> trackSelected;
	public Block blockSelected;

	/**
	 * GUI COMPONENTS
	 */
	Timer infoRefreshTimer;

	private JFrame frame_tmGUI;
	private JPanel panel_dynamicRender;
	JComboBox comboBox_selectTrack;
	JComboBox comboBox_sectionId;
	JLabel label_blockID;
	JLabel label_lengthVal;
	JLabel label_gradeVal;
	JLabel label_elevationVal;
	JLabel label_cumElevationVal;
	JLabel label_speedLimitVal;
	JLabel icon_occupied;
	JLabel icon_underground;
	JLabel icon_railCrossing;
	JLabel icon_trackHeated;
	JLabel label_switchHead;
	JLabel label_switchPortNormal;
	JLabel label_switchPortAlternate;
	JLabel icon_switchState;
	JLabel icon_switch;
	JLabel label_stationName;
	JLabel icon_station;
	JLabel icon_railFailure;
	JLabel icon_powerFailure;
	JLabel icon_trackCircuitFailure;
	String selectedFailure = "RAIL FAILURE";

	/**
	 * DYNAMIC DISPLAY GUI REFERENCES
	 */
	public DynamicDisplay greenLineDisplay; // = new DynamicDisplay(greenLineBlocks);
	public DynamicDisplay redLineDisplay;
	public DynamicDisplay currentDisplay;

	/**
	 * GUI CONSTRUCTOR
	 */
	public TrackModelGUI(TrackModel trackModel){
		this.trackModel = trackModel;
		setLookAndFeel();
		initialize();
		frame_tmGUI.setVisible(true);
		frame_tmGUI.setResizable(false);
		initTracksOnStartup();
	}

	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Variations of Roboto Condensed Font
	 */
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 20);
	Font font_20_bold = new Font("Roboto Condensed Bold", Font.BOLD, 30);
	Font font_24_bold = new Font("Roboto Condensed", Font.BOLD, 38);

	/**
	 * Set any UI configurations done by the UI manager and
	 * register Roboto Condensed font into the system.
	 *
	 * NOTE: This method must be called first in the GUI instantiation!
	 */
	public void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Shared/fonts/RobotoCondensed-Bold.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Shared/fonts/RobotoCondensed-BoldItalic.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Shared/fonts/RobotoCondensed-Italic.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Shared/fonts/RobotoCondensed-Regular.ttf")));

		    System.out.println("Loaded custom fonts!");
		} catch (IOException|FontFormatException e) {
		    System.out.println("HssVisualizer Error: Cannot load custom font.");
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

	public void stylizeHeadingLabel(JLabel l){
		l.setFont(font_20_bold);
		l.setForeground(Color.WHITE);
		l.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void stylizeInfoLabel(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Bold(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(new Color(234, 201, 87));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Small(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_14_bold);
	}
	
	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// OVERALL FRAME
		frame_tmGUI = new JFrame();
		frame_tmGUI.setTitle("Track Model View");
		frame_tmGUI.getContentPane().setBackground(new Color(26, 29, 35));
		frame_tmGUI.setBounds(100, 100, 1080, 560);
		frame_tmGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_tmGUI.getContentPane().setLayout(null);
		
		// TRACK IMPORT BUTTON
		JButton button_importTrack = new JButton("IMPORT TRACK");
		stylizeButton(button_importTrack);
		button_importTrack.setBounds(16, 16, 160, 30);
		button_importTrack.addActionListener(new OpenL()); // OPEN FILE NAVIGATOR
		frame_tmGUI.getContentPane().add(button_importTrack);

		// DROP DOWN MENU OF IMPORTED TRACKS
		comboBox_selectTrack = new JComboBox();
		stylizeComboBox(comboBox_selectTrack);
		comboBox_selectTrack.setBounds(183, 16, 168, 30);

		ItemListener trackSelectionListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				ItemSelectable is = itemEvent.getItemSelectable();

				if (selectedString(is).equals("GREEN LINE")){
					frame_tmGUI.getContentPane().remove(panel_dynamicRender);
					trackSelected = trackModel.getTrack("green");
					blockSelected = trackSelected.get(0);
					panel_dynamicRender = greenLineDisplay.dynamicTrackView;
					panel_dynamicRender.setBounds(16, 58, 335, 448);
					frame_tmGUI.getContentPane().add(panel_dynamicRender);

					currentDisplay = greenLineDisplay;
				} else if (selectedString(is).equals("RED LINE")){
					frame_tmGUI.getContentPane().remove(panel_dynamicRender);
					trackSelected = trackModel.getTrack("red");
					blockSelected = trackSelected.get(0);
					panel_dynamicRender = redLineDisplay.dynamicTrackView;
					panel_dynamicRender.setBounds(16, 58, 335, 448);
					frame_tmGUI.getContentPane().add(panel_dynamicRender);

					currentDisplay = redLineDisplay;
				}
			}
	    };

	    comboBox_selectTrack.addItemListener(trackSelectionListener);
		frame_tmGUI.getContentPane().add(comboBox_selectTrack);

		// DYANMIC RENDER PANEL
		panel_dynamicRender = new JPanel();
		panel_dynamicRender.setBackground(Color.DARK_GRAY);
		panel_dynamicRender.setBounds(16, 58, 335, 448);

		frame_tmGUI.getContentPane().add(panel_dynamicRender);
		
		//----------------- BLOCK SELECTION ------------------//		
		// LABEL - STATIC
		JLabel label_selectBlock = new JLabel("SELECT BLOCK");
		stylizeHeadingLabel(label_selectBlock);
		label_selectBlock.setBounds(395, 21, 236, 37);
		frame_tmGUI.getContentPane().add(label_selectBlock);
		
		// SELECTED BLOCK ID
		label_blockID = new JLabel("   ");
		label_blockID.setForeground(new Color(234, 201, 87));
		label_blockID.setFont(font_24_bold);
		label_blockID.setHorizontalAlignment(SwingConstants.CENTER);
		label_blockID.setBounds(462, 59, 97, 52);
		frame_tmGUI.getContentPane().add(label_blockID);
		
		// SELECT BLOCK RIGHT BUTTON
		JButton button_selectBlockRight = new JButton("\u00BB");
		stylizeButton(button_selectBlockRight);
		button_selectBlockRight.setBounds(559, 66, 47, 38);

		button_selectBlockRight.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if (trackSelected != null){
					// Wrap around if selected block is last block 
					if (blockSelected != trackSelected.get(trackSelected.size()-1)){
						blockSelected = trackSelected.get(blockSelected.getId() + 1);
					} else {
						blockSelected = trackSelected.get(0);
					}
					currentDisplay.dynamicTrackView.blockSelected = blockSelected;
					showBlockInfo(trackSelected.get(blockSelected.getId()));
				}
			} 
		});

		frame_tmGUI.getContentPane().add(button_selectBlockRight);

		// SELECT BLOCK LEFT BUTTON
		JButton button_selectBlockLeft = new JButton("\u00AB");
		stylizeButton(button_selectBlockLeft);
		button_selectBlockLeft.setBounds(412, 66, 47, 38);
		
		button_selectBlockLeft.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if (trackSelected != null){
					// Wrap around if selected block is first block
					if (blockSelected != trackSelected.get(0)){
						blockSelected = trackSelected.get(blockSelected.getId() - 1);
					} else {
						blockSelected = trackSelected.get(trackSelected.size()-1);
					}
					currentDisplay.dynamicTrackView.blockSelected = blockSelected;
					showBlockInfo(blockSelected);
				}
			} 
		});
		
		frame_tmGUI.getContentPane().add(button_selectBlockLeft);
		
		// SECTION AND ID SELECTION (DO THIS LATER)
		JLabel label_selectSectionId = new JLabel("SECTION/ID");
		stylizeInfoLabel(label_selectSectionId);
		label_selectSectionId.setBounds(400, 121, 110, 22);
		frame_tmGUI.getContentPane().add(label_selectSectionId);

		comboBox_sectionId = new JComboBox();
		stylizeComboBox(comboBox_sectionId);
		comboBox_sectionId.setBounds(520, 116, 76, 30);
		frame_tmGUI.getContentPane().add(comboBox_sectionId);

		ItemListener blockSelectionListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				ItemSelectable is = itemEvent.getItemSelectable();

				int selectedBlockId = Integer.parseInt(selectedString(is).substring(1)) - 1;
				blockSelected = trackSelected.get(selectedBlockId);
				currentDisplay.dynamicTrackView.blockSelected = blockSelected;
				showBlockInfo(blockSelected);
			}
	    };

	    comboBox_sectionId.addItemListener(blockSelectionListener);

		//----------------- BLOCK INFO DISPLAY ------------------//	
		// BLOCK INFO LABEL - STATIC
		JLabel label_blockInfo = new JLabel("BLOCK INFO");
		stylizeHeadingLabel(label_blockInfo);
		label_blockInfo.setBounds(721, 23, 236, 37);
		frame_tmGUI.getContentPane().add(label_blockInfo);
		
		// LENGTH INFORMATION
		JLabel label_length = new JLabel("LENGTH");
		stylizeInfoLabel(label_length);
		label_length.setBounds(670, 77, 113, 22);
		frame_tmGUI.getContentPane().add(label_length);
		
		label_lengthVal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_lengthVal);
		label_lengthVal.setBounds(781, 77, 103, 22);
		frame_tmGUI.getContentPane().add(label_lengthVal);

		// GRADE INFORMATION
		JLabel label_grade = new JLabel("GRADE");
		stylizeInfoLabel(label_grade);
		label_grade.setBounds(670, 105, 113, 22);
		frame_tmGUI.getContentPane().add(label_grade);
		
		label_gradeVal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_gradeVal);
		label_gradeVal.setBounds(781, 105, 102, 22);
		frame_tmGUI.getContentPane().add(label_gradeVal);

		// ELEVATION INFORMATION
		JLabel label_elevation = new JLabel("ELEVATION");
		stylizeInfoLabel(label_elevation);
		label_elevation.setBounds(670, 132, 113, 22);
		frame_tmGUI.getContentPane().add(label_elevation);
		
		label_elevationVal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_elevationVal);
		label_elevationVal.setBounds(781, 132, 101, 22);
		frame_tmGUI.getContentPane().add(label_elevationVal);

		// CUMULATIVE ELEVATION INFORMATION
		JLabel label_cumElevation = new JLabel("CUM. ELEV.");
		stylizeInfoLabel(label_cumElevation);
		label_cumElevation.setBounds(670, 159, 113, 22);
		frame_tmGUI.getContentPane().add(label_cumElevation);
			
		label_cumElevationVal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_cumElevationVal);
		label_cumElevationVal.setBounds(781, 159, 99, 22);
		frame_tmGUI.getContentPane().add(label_cumElevationVal);

		// SPEED LIMIT INFORMATION
		JLabel label_speedLimit = new JLabel("SPEED LIMIT");
		stylizeInfoLabel(label_speedLimit);
		label_speedLimit.setBounds(670, 185, 113, 22);
		frame_tmGUI.getContentPane().add(label_speedLimit);

		label_speedLimitVal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_speedLimitVal);
		label_speedLimitVal.setBounds(781, 185, 105, 22);
		frame_tmGUI.getContentPane().add(label_speedLimitVal);

		// OCCUPANCY INFORMATION
		JLabel label_occupied = new JLabel("OCCUPIED");
		stylizeInfoLabel(label_occupied);
		label_occupied.setBounds(922, 75, 113, 22);
		frame_tmGUI.getContentPane().add(label_occupied);

		icon_occupied = new JLabel("");
		icon_occupied.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_occupied.setBounds(890, 77, 25, 23);
		frame_tmGUI.getContentPane().add(icon_occupied);
		
		// UNDERGROUND INFORMATION
		JLabel label_underground = new JLabel("UNDERGROUND");
		stylizeInfoLabel(label_underground);
		label_underground.setBounds(922, 102, 167, 22);
		frame_tmGUI.getContentPane().add(label_underground);

		icon_underground = new JLabel("");
		icon_underground.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_underground.setBounds(890, 104, 25, 23);
		frame_tmGUI.getContentPane().add(icon_underground);

		// CROSSING INFORMATION
		JLabel label_railCrossing = new JLabel("RAIL CROSSING");
		stylizeInfoLabel(label_railCrossing);
		label_railCrossing.setBounds(922, 129, 167, 22);
		frame_tmGUI.getContentPane().add(label_railCrossing);

		icon_railCrossing = new JLabel("");
		icon_railCrossing.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_railCrossing.setBounds(890, 128, 25, 23);
		frame_tmGUI.getContentPane().add(icon_railCrossing);
		
		// TRACK HEAT INFORMATION
		JLabel label_trackHeated = new JLabel("TRACK HEATED");
		stylizeInfoLabel(label_trackHeated);
		label_trackHeated.setBounds(922, 157, 167, 22);
		frame_tmGUI.getContentPane().add(label_trackHeated);
		
		icon_trackHeated = new JLabel("");
		icon_trackHeated.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));
		icon_trackHeated.setBounds(890, 157, 25, 23);
		frame_tmGUI.getContentPane().add(icon_trackHeated);

		// SWITCH INFORMATION
		JLabel label_switch = new JLabel("SWITCH");
		stylizeInfoLabel(label_switch);
		label_switch.setBounds(705, 251, 167, 22);
		frame_tmGUI.getContentPane().add(label_switch);
		
		label_switchHead = new JLabel("   ");
		stylizeInfoLabel_Bold(label_switchHead);
		label_switchHead.setBounds(742, 317, 62, 22);
		frame_tmGUI.getContentPane().add(label_switchHead);
		
		label_switchPortNormal = new JLabel("   ");
		stylizeInfoLabel_Bold(label_switchPortNormal);
		label_switchPortNormal.setBounds(905, 283, 69, 22);
		frame_tmGUI.getContentPane().add(label_switchPortNormal);
		
		label_switchPortAlternate = new JLabel("   ");
		stylizeInfoLabel_Bold(label_switchPortAlternate);
		label_switchPortAlternate.setBounds(904, 349, 64, 22);
		frame_tmGUI.getContentPane().add(label_switchPortAlternate);

		icon_switchState = new JLabel("");
		icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_none.png"));
		icon_switchState.setBounds(784, 282, 112, 88);
		frame_tmGUI.getContentPane().add(icon_switchState);

		icon_switch = new JLabel("");
		icon_switch.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_switch.setBounds(673, 251, 25, 23);
		frame_tmGUI.getContentPane().add(icon_switch);

		// HSS LOGO
		JLabel icon_logo = new JLabel("");
		icon_logo.setIcon(new ImageIcon("Modules/TrackModel/Images/HSS_TrainSim_Logo.png"));
		icon_logo.setBounds(1080 - 110, 560 - 130, 100, 100);
		frame_tmGUI.getContentPane().add(icon_logo);

		// STATION INFORMATION
		JLabel label_station = new JLabel("STATION");
		stylizeInfoLabel(label_station);
		label_station.setBounds(705, 398, 167, 22);
		frame_tmGUI.getContentPane().add(label_station);
			
		label_stationName = new JLabel("   ");
		stylizeInfoLabel_Bold(label_stationName);
		label_stationName.setBounds(786, 439, 216, 22);
		frame_tmGUI.getContentPane().add(label_stationName);

		icon_station = new JLabel("");
		icon_station.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_station.setBounds(673, 398, 25, 23);
		frame_tmGUI.getContentPane().add(icon_station);

		// FAILURE INFORMATION
		JLabel label_failures = new JLabel("FAILURES");
		stylizeInfoLabel(label_failures);
		label_failures.setBounds(399, 269, 236, 37);
		frame_tmGUI.getContentPane().add(label_failures);
		
		JLabel label_railFailure = new JLabel("RAIL");
		stylizeInfoLabel_Small(label_railFailure);
		label_railFailure.setBounds(440, 304, 55, 22);
		frame_tmGUI.getContentPane().add(label_railFailure);
		
		icon_railFailure = new JLabel("");
		icon_railFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_railFailure.setBounds(405, 304, 25, 23);
		frame_tmGUI.getContentPane().add(icon_railFailure);
		
		JLabel label_powerFailure = new JLabel("POWER");
		stylizeInfoLabel_Small(label_powerFailure);
		label_powerFailure.setBounds(439, 328, 78, 22);
		frame_tmGUI.getContentPane().add(label_powerFailure);
		
		icon_powerFailure = new JLabel("");
		icon_powerFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_powerFailure.setBounds(405, 329, 25, 23);
		frame_tmGUI.getContentPane().add(icon_powerFailure);

		JLabel label_trackCircuitFailure = new JLabel("TRACK CIRCUIT");
		stylizeInfoLabel_Small(label_trackCircuitFailure);
		label_trackCircuitFailure.setBounds(439, 352, 128, 22);
		frame_tmGUI.getContentPane().add(label_trackCircuitFailure);
		
		icon_trackCircuitFailure = new JLabel("");
		icon_trackCircuitFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		icon_trackCircuitFailure.setBounds(405, 352, 25, 23);
		frame_tmGUI.getContentPane().add(icon_trackCircuitFailure);
		
		
		// FAILURE SIMULATION
		JLabel label_simulateFailure = new JLabel("SIMULATE FAILURE");
		stylizeInfoLabel(label_simulateFailure);
		label_simulateFailure.setBounds(395, 405, 236, 37);
		frame_tmGUI.getContentPane().add(label_simulateFailure);
		
		JButton button_toggle = new JButton("TOGGLE");
		stylizeButton(button_toggle);
		button_toggle.setBounds(550, 441, 98, 52);

		button_toggle.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if (trackSelected != null){
					if (selectedFailure.equals("RAIL FAILURE")){
						blockSelected.setRailStatus(!blockSelected.getRailStatus());
					} else if (selectedFailure.equals("POWER FAILURE")){
						blockSelected.setPowerStatus(!blockSelected.getPowerStatus());
					} else if (selectedFailure.equals("TRACK CIRCUIT FAILURE")){
						blockSelected.setTrackCircuitStatus(!blockSelected.getTrackCircuitStatus());
					} else {
						// ... 
					}
				}
			} 
		});

		frame_tmGUI.getContentPane().add(button_toggle);

		JComboBox comboBox_failures = new JComboBox();
		stylizeComboBox(comboBox_failures);
		comboBox_failures.setBounds(380, 441, 158, 52);
		comboBox_failures.addItem("RAIL FAILURE");
		comboBox_failures.addItem("POWER FAILURE");
		comboBox_failures.addItem("TRACK CIRCUIT FAILURE");

		ItemListener failureSelectionListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				ItemSelectable is = itemEvent.getItemSelectable();

				selectedFailure = selectedString(is);
				System.out.println("selected: " + selectedFailure); 
			}
	    };

	    comboBox_failures.addItemListener(failureSelectionListener);
		frame_tmGUI.getContentPane().add(comboBox_failures);
	}

	public void showBlockInfo(Block block){
		/**
		 * NOTE: Calls to block.getId() in this method are added to 1 (+1) 
		 * for displaying the block ID correctly. This is because all block ID's
		 * are initialized to start from index 0 when read in from the CSV file
		 * that starts at block A1 for each line. When displaying the ID back to 
		 * the user in a GUI, (+1) will revert to the ID specified in the CSV.
		 */
		DecimalFormat df = new DecimalFormat("#.##");

		label_blockID.setText((block.getSection()).toUpperCase() + Integer.toString(block.getId() + 1));
		label_lengthVal.setText(df.format(block.getLength() * METERS_TO_YARDS_FACTOR) + " yd");
		label_gradeVal.setText(df.format(block.getGrade()) + " %");
		label_elevationVal.setText(df.format(block.getElevation() * METERS_TO_YARDS_FACTOR) + " yd");
		label_cumElevationVal.setText(df.format(block.getCumElevation() * METERS_TO_YARDS_FACTOR) + " yd");
		label_speedLimitVal.setText(df.format((double)block.getSpeedLimit() * KPH_TO_MPH_FACTOR) + " mi/h");

		if (block.getOccupied()){
			icon_occupied.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));
		} else {
			icon_occupied.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}

		if (block.getUndergroundStatus()){
			icon_underground.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));
		} else {
			icon_underground.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}

		if (block.getCrossing() != null){
			icon_railCrossing.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));
		} else {
			icon_railCrossing.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}

		/**
		 * HANDLE TRACK HEATED .....
		 */

		if (block.getSwitch() != null){
			icon_switch.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));

			Switch s = block.getSwitch();
			int n = s.getPortNormal();
			int a = s.getPortAlternate();
			
			if (s.getEdge() == Switch.EDGE_TYPE_HEAD){
				label_switchHead.setText((block.getSection()).toUpperCase() + Integer.toString(block.getId() + 1));
				label_switchPortNormal.setText((trackSelected.get(n).getSection()).toUpperCase() + Integer.toString(n));
				label_switchPortAlternate.setText((trackSelected.get(a).getSection()).toUpperCase() + Integer.toString(a));
			} else if (s.getEdge() == Switch.EDGE_TYPE_TAIL){
				label_switchHead.setText((trackSelected.get(n).getSection()).toUpperCase() + Integer.toString(n));
				
				// Check if this tail block is the normal or the alternate
				// by referencing the head block at the normal port of the tail block
				if (trackSelected.get(n).getSwitch().getPortNormal() == block.getId()){
					label_switchPortNormal.setText((block.getSection()).toUpperCase() + Integer.toString(block.getId() + 1));
					label_switchPortAlternate.setText((trackSelected.get(a).getSection()).toUpperCase() + Integer.toString(a)); 
				} else {
					label_switchPortNormal.setText((trackSelected.get(a).getSection()).toUpperCase() + Integer.toString(a)); 
					label_switchPortAlternate.setText((block.getSection()).toUpperCase() + Integer.toString(block.getId() + 1));
				}
			}

			if (block.getSwitch().getTailType() == Switch.TAIL_TYPE_NORMAL){
				if (block.getSwitch().getState() == Switch.STATE_NORMAL){
					icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_normal.png"));
				} else if (block.getSwitch().getState() == Switch.STATE_ALTERNATE){
					icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_alternate.png"));
				}
			} else {
				if (block.getSwitch().getState() == Switch.STATE_ALTERNATE){
					icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_alternate.png"));
				} else if (block.getSwitch().getState() == Switch.STATE_NORMAL){
					icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_normal.png"));
				}
			}

		} else {
			icon_switch.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));

			label_switchHead.setText("   ");
			label_switchPortNormal.setText("   ");
			label_switchPortAlternate.setText("   ");
			icon_switchState.setIcon(new ImageIcon("Modules/TrackModel/images/switch_none.png"));
		}

		if (block.getStation() != null){
			icon_station.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_green.png"));
			label_stationName.setText((block.getStation().getId()).toUpperCase());
		} else {
			icon_station.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
			label_stationName.setText("   ");
		}

		if (block.getRailStatus() == Block.STATUS_NOT_WORKING){
			icon_railFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_red.png"));
		} else {
			icon_railFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}

		if (block.getPowerStatus() == Block.STATUS_NOT_WORKING){
			icon_powerFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_red.png"));
		} else {
			icon_powerFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}

		if (block.getTrackCircuitStatus() == Block.STATUS_NOT_WORKING){
			icon_trackCircuitFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_red.png"));
		} else {
			icon_trackCircuitFailure.setIcon(new ImageIcon("Modules/TrackModel/images/statusIcon_grey.png"));
		}
	}

	//--------------------- HELPER METHODS AND CLASSES -------------------------//
	
	// Convert the item selected in the comboBox to a string
	static private String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	// Opens the directory navigation window 
	// and obtains the string of the selected file
	class OpenL implements ActionListener {
	    public void actionPerformed(ActionEvent e) {

	    	/*
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(frame_tmGUI);

			String trackFilename = c.getSelectedFile().getName();

			if ((trackFilename.toLowerCase()).equals("greenlinefinal.csv") || (trackFilename.toLowerCase()).equals("redlinefinal.csv")){
				if ((trackFilename.toLowerCase()).equals("greenlinefinal.csv")){
					// MUST BE CALLED IN THIS ORDER
					trackModel.setTrack("green", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/GreenLineFinal.csv"));
					trackSelected = trackModel.getTrack("green");
					blockSelected = trackSelected.get(0);

					greenLineDisplay = new DynamicDisplay(trackModel.getTrack("green"));
					currentDisplay = greenLineDisplay;

					comboBox_selectTrack.addItem("GREEN LINE");
					comboBox_selectTrack.setSelectedItem("GREEN LINE");
				} else if ((trackFilename.toLowerCase()).equals("redlinefinal.csv")){
					// MUST BE CALLED IN THIS ORDER
					trackModel.setTrack("red", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/RedLineFinal.csv"));
					trackSelected = trackModel.getTrack("red");
					blockSelected = trackSelected.get(0);

					redLineDisplay = new DynamicDisplay(trackModel.getTrack("red"));
					currentDisplay = redLineDisplay;

					comboBox_selectTrack.addItem("RED LINE");
					comboBox_selectTrack.setSelectedItem("RED LINE");
				}
				showBlockInfo(blockSelected);
				startTimer();
			}
			*/
	    }
	}
	
	public void initTracksOnStartup() {		
		trackModel.setTrack("red", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/RedLineFinal.csv"));
		trackSelected = trackModel.getTrack("red");
		blockSelected = trackSelected.get(0);

		redLineDisplay = new DynamicDisplay(trackModel.getTrack("red"));
		currentDisplay = redLineDisplay;

		comboBox_selectTrack.addItem("RED LINE");

		trackModel.setTrack("green", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/GreenLineFinal.csv"));
		trackSelected = trackModel.getTrack("green");
		blockSelected = trackSelected.get(0);

		greenLineDisplay = new DynamicDisplay(trackModel.getTrack("green"));
		currentDisplay = greenLineDisplay;

		comboBox_selectTrack.addItem("GREEN LINE");
		comboBox_selectTrack.setSelectedItem("GREEN LINE");

		for (int i = 0; i < trackSelected.size(); i++){
			comboBox_sectionId.addItem(trackSelected.get(i).getSection() + (i+1));
		}

		startTimer();
	}

	public void startTimer(){
		infoRefreshTimer = new Timer(20, this);
	    infoRefreshTimer.start();
	}

	// Refresh the screen info
	public void actionPerformed(ActionEvent e) {
    	showBlockInfo(blockSelected);
	}
}
