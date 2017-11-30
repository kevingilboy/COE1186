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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.*;

@SuppressWarnings("unchecked")
public class TrackModelGUI implements ActionListener{
	// Conversion Factors
	private final double METERS_TO_YARDS_FACTOR = 1.09361;
	private final double KPH_TO_MPH_FACTOR = 0.621371;

	// Track Model references
	private TrackModel trackModel;
	private ArrayList<Block> trackSelected;
	public Block blockSelected;

	// GUI Components
	Timer infoRefreshTimer;

	private JFrame frame_tmGUI;
	private JPanel panel_dynamicRender;
	JComboBox comboBox_selectTrack;
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

	// GUI references
	public DynamicDisplay greenLineDisplay; // = new DynamicDisplay(greenLineBlocks);
	public DynamicDisplay redLineDisplay;
	public DynamicDisplay currentDisplay;

	/**
	 * Create the application.
	 */
	public TrackModelGUI(TrackModel trackModel){
		this.trackModel = trackModel;
		setLookAndFeel();
		initialize();
		frame_tmGUI.setVisible(true);
		frame_tmGUI.setResizable(false);
		initTracksOnStartup();
	}

	/**
	 * Initialize the look and feel
	 */
	public void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// OVERALL FRAME
		frame_tmGUI = new JFrame();
		frame_tmGUI.setTitle("Track Model View");
		frame_tmGUI.getContentPane().setBackground(new Color(50, 50, 50));
		frame_tmGUI.setBounds(100, 100, 1080, 560);
		frame_tmGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_tmGUI.getContentPane().setLayout(null);
		
		// TRACK IMPORT BUTTON
		JButton button_importTrack = new JButton("IMPORT TRACK");
		button_importTrack.setFocusPainted(false);
		button_importTrack.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
		button_importTrack.setForeground(new Color(255, 255, 255));
		button_importTrack.setBackground(new Color(102, 0, 153));
		button_importTrack.setBounds(16, 16, 136, 30);
		button_importTrack.addActionListener(new OpenL()); // OPEN FILE NAVIGATOR
		frame_tmGUI.getContentPane().add(button_importTrack);

		// DROP DOWN MENU OF IMPORTED TRACKS
		comboBox_selectTrack = new JComboBox();
		comboBox_selectTrack.setForeground(Color.BLACK);
		comboBox_selectTrack.setBackground(Color.WHITE);
		comboBox_selectTrack.setBounds(163, 16, 188, 30);

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
		label_selectBlock.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 28));
		label_selectBlock.setForeground(new Color(204, 204, 204));
		label_selectBlock.setHorizontalAlignment(SwingConstants.CENTER);
		label_selectBlock.setBounds(395, 21, 236, 37);
		frame_tmGUI.getContentPane().add(label_selectBlock);
		
		// SELECTED BLOCK ID
		label_blockID = new JLabel("   ");
		label_blockID.setForeground(new Color(255, 255, 0));
		label_blockID.setFont(new Font("Tw Cen MT", Font.BOLD, 33));
		label_blockID.setHorizontalAlignment(SwingConstants.CENTER);
		label_blockID.setBounds(462, 59, 97, 52);
		frame_tmGUI.getContentPane().add(label_blockID);
		
		// SELECT BLOCK RIGHT BUTTON
		JButton button_selectBlockRight = new JButton("\u00BB");
		button_importTrack.setFocusPainted(false);
		button_selectBlockRight.setForeground(new Color(255, 255, 255));
		button_selectBlockRight.setBackground(new Color(102, 51, 153));
		button_selectBlockRight.setFont(new Font("Tw Cen MT", Font.BOLD, 24));
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
					showBlockInfo(blockSelected);
				}
			} 
		});

		frame_tmGUI.getContentPane().add(button_selectBlockRight);

		// SELECT BLOCK LEFT BUTTON
		JButton button_selectBlockLeft = new JButton("\u00AB");
		button_importTrack.setFocusPainted(false);	
		button_selectBlockLeft.setForeground(Color.WHITE);
		button_selectBlockLeft.setFont(new Font("Tw Cen MT", Font.BOLD, 24));
		button_selectBlockLeft.setBackground(new Color(102, 51, 153));
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
		JLabel label_selectSection = new JLabel("SECTION");
		label_selectSection.setHorizontalAlignment(SwingConstants.RIGHT);
		label_selectSection.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_selectSection.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_selectSection.setBounds(422, 121, 83, 22);
		frame_tmGUI.getContentPane().add(label_selectSection);
		
		JLabel label_selectID = new JLabel("ID");
		label_selectID.setHorizontalAlignment(SwingConstants.RIGHT);
		label_selectID.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_selectID.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_selectID.setBounds(422, 158, 83, 22);
		frame_tmGUI.getContentPane().add(label_selectID);

		JComboBox comboBox_section = new JComboBox();
		comboBox_section.setForeground(Color.WHITE);
		comboBox_section.setBackground(Color.WHITE);
		comboBox_section.setBounds(513, 116, 76, 30);
		frame_tmGUI.getContentPane().add(comboBox_section);
		
		JComboBox comboBox_id = new JComboBox();
		comboBox_id.setForeground(Color.WHITE);
		comboBox_id.setBackground(Color.WHITE);
		comboBox_id.setBounds(513, 154, 76, 30);
		frame_tmGUI.getContentPane().add(comboBox_id);

		//----------------- BLOCK INFO DISPLAY ------------------//	
		// BLOCK INFO LABEL - STATIC
		JLabel label_blockInfo = new JLabel("BLOCK INFO");
		label_blockInfo.setHorizontalAlignment(SwingConstants.CENTER);
		label_blockInfo.setForeground(new Color(204, 204, 204));
		label_blockInfo.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 28));
		label_blockInfo.setBounds(721, 23, 236, 37);
		frame_tmGUI.getContentPane().add(label_blockInfo);
		
		// LENGTH INFORMATION
		JLabel label_length = new JLabel("LENGTH");
		label_length.setHorizontalAlignment(SwingConstants.LEFT);
		label_length.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_length.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_length.setBounds(670, 77, 113, 22);
		frame_tmGUI.getContentPane().add(label_length);
		
		label_lengthVal = new JLabel("   ");
		label_lengthVal.setHorizontalAlignment(SwingConstants.LEFT);
		label_lengthVal.setForeground(Color.WHITE);
		label_lengthVal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_lengthVal.setBounds(781, 77, 103, 22);
		frame_tmGUI.getContentPane().add(label_lengthVal);

		// GRADE INFORMATION
		JLabel label_grade = new JLabel("GRADE");
		label_grade.setHorizontalAlignment(SwingConstants.LEFT);
		label_grade.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_grade.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_grade.setBounds(670, 105, 113, 22);
		frame_tmGUI.getContentPane().add(label_grade);
		
		label_gradeVal = new JLabel("   ");
		label_gradeVal.setHorizontalAlignment(SwingConstants.LEFT);
		label_gradeVal.setForeground(Color.WHITE);
		label_gradeVal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_gradeVal.setBounds(781, 105, 102, 22);
		frame_tmGUI.getContentPane().add(label_gradeVal);

		// ELEVATION INFORMATION
		JLabel label_elevation = new JLabel("ELEVATION");
		label_elevation.setHorizontalAlignment(SwingConstants.LEFT);
		label_elevation.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_elevation.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_elevation.setBounds(670, 132, 113, 22);
		frame_tmGUI.getContentPane().add(label_elevation);
		
		label_elevationVal = new JLabel("   ");
		label_elevationVal.setHorizontalAlignment(SwingConstants.LEFT);
		label_elevationVal.setForeground(Color.WHITE);
		label_elevationVal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_elevationVal.setBounds(781, 132, 101, 22);
		frame_tmGUI.getContentPane().add(label_elevationVal);

		// CUMULATIVE ELEVATION INFORMATION
		JLabel label_cumElevation = new JLabel("CUM. ELEV.");
		label_cumElevation.setHorizontalAlignment(SwingConstants.LEFT);
		label_cumElevation.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_cumElevation.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_cumElevation.setBounds(670, 159, 113, 22);
		frame_tmGUI.getContentPane().add(label_cumElevation);
			
		label_cumElevationVal = new JLabel("   ");
		label_cumElevationVal.setHorizontalAlignment(SwingConstants.LEFT);
		label_cumElevationVal.setForeground(Color.WHITE);
		label_cumElevationVal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_cumElevationVal.setBounds(781, 159, 99, 22);
		frame_tmGUI.getContentPane().add(label_cumElevationVal);

		// SPEED LIMIT INFORMATION
		JLabel label_speedLimit = new JLabel("SPEED LIMIT");
		label_speedLimit.setHorizontalAlignment(SwingConstants.LEFT);
		label_speedLimit.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_speedLimit.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_speedLimit.setBounds(670, 185, 113, 22);
		frame_tmGUI.getContentPane().add(label_speedLimit);

		label_speedLimitVal = new JLabel("   ");
		label_speedLimitVal.setHorizontalAlignment(SwingConstants.LEFT);
		label_speedLimitVal.setForeground(Color.WHITE);
		label_speedLimitVal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_speedLimitVal.setBounds(781, 185, 105, 22);
		frame_tmGUI.getContentPane().add(label_speedLimitVal);

		// OCCUPANCY INFORMATION
		JLabel label_occupied = new JLabel("OCCUPIED");
		label_occupied.setHorizontalAlignment(SwingConstants.LEFT);
		label_occupied.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_occupied.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_occupied.setBounds(922, 75, 113, 22);
		frame_tmGUI.getContentPane().add(label_occupied);

		icon_occupied = new JLabel("");
		icon_occupied.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_occupied.setBounds(890, 77, 25, 23);
		frame_tmGUI.getContentPane().add(icon_occupied);
		
		// UNDERGROUND INFORMATION
		JLabel label_underground = new JLabel("UNDERGROUND");
		label_underground.setHorizontalAlignment(SwingConstants.LEFT);
		label_underground.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_underground.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_underground.setBounds(922, 102, 167, 22);
		frame_tmGUI.getContentPane().add(label_underground);

		icon_underground = new JLabel("");
		icon_underground.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_underground.setBounds(890, 104, 25, 23);
		frame_tmGUI.getContentPane().add(icon_underground);

		// CROSSING INFORMATION
		JLabel label_railCrossing = new JLabel("RAIL CROSSING");
		label_railCrossing.setHorizontalAlignment(SwingConstants.LEFT);
		label_railCrossing.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_railCrossing.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_railCrossing.setBounds(922, 129, 167, 22);
		frame_tmGUI.getContentPane().add(label_railCrossing);

		icon_railCrossing = new JLabel("");
		icon_railCrossing.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_railCrossing.setBounds(890, 128, 25, 23);
		frame_tmGUI.getContentPane().add(icon_railCrossing);
		
		// TRACK HEAT INFORMATION
		JLabel label_trackHeated = new JLabel("TRACK HEATED");
		label_trackHeated.setHorizontalAlignment(SwingConstants.LEFT);
		label_trackHeated.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_trackHeated.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_trackHeated.setBounds(922, 157, 167, 22);
		frame_tmGUI.getContentPane().add(label_trackHeated);
		
		icon_trackHeated = new JLabel("");
		icon_trackHeated.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_trackHeated.setBounds(890, 157, 25, 23);
		frame_tmGUI.getContentPane().add(icon_trackHeated);

		// SWITCH INFORMATION
		JLabel label_switch = new JLabel("SWITCH");
		label_switch.setHorizontalAlignment(SwingConstants.LEFT);
		label_switch.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_switch.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_switch.setBounds(705, 251, 167, 22);
		frame_tmGUI.getContentPane().add(label_switch);
		
		label_switchHead = new JLabel("   ");
		label_switchHead.setHorizontalAlignment(SwingConstants.RIGHT);
		label_switchHead.setForeground(Color.WHITE);
		label_switchHead.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_switchHead.setBounds(714, 317, 62, 22);
		frame_tmGUI.getContentPane().add(label_switchHead);
		
		label_switchPortNormal = new JLabel("   ");
		label_switchPortNormal.setHorizontalAlignment(SwingConstants.LEFT);
		label_switchPortNormal.setForeground(Color.WHITE);
		label_switchPortNormal.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_switchPortNormal.setBounds(905, 283, 69, 22);
		frame_tmGUI.getContentPane().add(label_switchPortNormal);
		
		label_switchPortAlternate = new JLabel("   ");
		label_switchPortAlternate.setHorizontalAlignment(SwingConstants.LEFT);
		label_switchPortAlternate.setForeground(Color.WHITE);
		label_switchPortAlternate.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_switchPortAlternate.setBounds(904, 349, 64, 22);
		frame_tmGUI.getContentPane().add(label_switchPortAlternate);

		icon_switchState = new JLabel("");
		icon_switchState.setIcon(new ImageIcon("Modules\\TrackModel\\images\\switch_none.png"));
		icon_switchState.setBounds(784, 282, 112, 88);
		frame_tmGUI.getContentPane().add(icon_switchState);

		icon_switch = new JLabel("");
		icon_switch.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_switch.setBounds(673, 251, 25, 23);
		frame_tmGUI.getContentPane().add(icon_switch);

		// STATION INFORMATION
		JLabel label_station = new JLabel("STATION");
		label_station.setHorizontalAlignment(SwingConstants.LEFT);
		label_station.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_station.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_station.setBounds(705, 398, 167, 22);
		frame_tmGUI.getContentPane().add(label_station);
			
		label_stationName = new JLabel("   ");
		label_stationName.setHorizontalAlignment(SwingConstants.LEFT);
		label_stationName.setForeground(Color.WHITE);
		label_stationName.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 24));
		label_stationName.setBounds(786, 439, 216, 22);
		frame_tmGUI.getContentPane().add(label_stationName);

		icon_station = new JLabel("");
		icon_station.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_station.setBounds(673, 398, 25, 23);
		frame_tmGUI.getContentPane().add(icon_station);

		// FAILURE INFORMATION
		JLabel label_failures = new JLabel("FAILURES");
		label_failures.setHorizontalAlignment(SwingConstants.CENTER);
		label_failures.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_failures.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
		label_failures.setBounds(399, 269, 236, 37);
		frame_tmGUI.getContentPane().add(label_failures);
		
		JLabel label_railFailure = new JLabel("RAIL");
		label_railFailure.setHorizontalAlignment(SwingConstants.LEFT);
		label_railFailure.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_railFailure.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_railFailure.setBounds(440, 304, 55, 22);
		frame_tmGUI.getContentPane().add(label_railFailure);
		
		icon_railFailure = new JLabel("");
		icon_railFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_railFailure.setBounds(405, 304, 25, 23);
		frame_tmGUI.getContentPane().add(icon_railFailure);
		
		JLabel label_powerFailure = new JLabel("POWER");
		label_powerFailure.setHorizontalAlignment(SwingConstants.LEFT);
		label_powerFailure.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_powerFailure.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_powerFailure.setBounds(439, 328, 78, 22);
		frame_tmGUI.getContentPane().add(label_powerFailure);
		
		icon_powerFailure = new JLabel("");
		icon_powerFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_powerFailure.setBounds(405, 329, 25, 23);
		frame_tmGUI.getContentPane().add(icon_powerFailure);

		JLabel label_trackCircuitFailure = new JLabel("TRACK CIRCUIT");
		label_trackCircuitFailure.setHorizontalAlignment(SwingConstants.LEFT);
		label_trackCircuitFailure.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_trackCircuitFailure.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
		label_trackCircuitFailure.setBounds(439, 352, 128, 22);
		frame_tmGUI.getContentPane().add(label_trackCircuitFailure);
		
		icon_trackCircuitFailure = new JLabel("");
		icon_trackCircuitFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		icon_trackCircuitFailure.setBounds(405, 352, 25, 23);
		frame_tmGUI.getContentPane().add(icon_trackCircuitFailure);
		
		
		// FAILURE SIMULATION
		JLabel label_simulateFailure = new JLabel("SIMULATE FAILURE");
		label_simulateFailure.setHorizontalAlignment(SwingConstants.CENTER);
		label_simulateFailure.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		label_simulateFailure.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
		label_simulateFailure.setBounds(395, 405, 236, 37);
		frame_tmGUI.getContentPane().add(label_simulateFailure);
		
		JButton button_toggle = new JButton("TOGGLE");
		button_toggle.setFocusPainted(false);
		button_toggle.setForeground(Color.WHITE);
		button_toggle.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
		button_toggle.setBackground(new Color(102, 0, 153));
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
		comboBox_failures.setBackground(Color.WHITE);
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
			icon_occupied.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_green.png"));
		} else {
			icon_occupied.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		}

		if (block.getUndergroundStatus()){
			icon_underground.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_green.png"));
		} else {
			icon_underground.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		}

		if (block.getCrossing() != null){
			icon_railCrossing.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_green.png"));
		} else {
			icon_railCrossing.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		}

		/**
		 * HANDLE TRACK HEATED .....
		 */

		if (block.getSwitch() != null){
			icon_switch.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_green.png"));

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

			if (block.getSwitch().getState() == Switch.STATE_NORMAL){
				icon_switchState.setIcon(new ImageIcon("Modules\\TrackModel\\images\\switch_normal.png"));
			} else if (block.getSwitch().getState() == Switch.STATE_ALTERNATE){
				icon_switchState.setIcon(new ImageIcon("Modules\\TrackModel\\images\\switch_alternate.png"));
			}

		} else {
			icon_switch.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));

			label_switchHead.setText("   ");
			label_switchPortNormal.setText("   ");
			label_switchPortAlternate.setText("   ");
			icon_switchState.setIcon(new ImageIcon("Modules\\TrackModel\\images\\switch_none.png"));
		}

		if (block.getStation() != null){
			icon_station.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_green.png"));
			label_stationName.setText((block.getStation().getId()).toUpperCase());
		} else {
			icon_station.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
			label_stationName.setText("   ");
		}

		if (block.getRailStatus() == Block.STATUS_NOT_WORKING){
			icon_railFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_red.png"));
		} else {
			icon_railFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		}

		if (block.getPowerStatus() == Block.STATUS_NOT_WORKING){
			icon_powerFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_red.png"));
		} else {
			icon_powerFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
		}

		if (block.getTrackCircuitStatus() == Block.STATUS_NOT_WORKING){
			icon_trackCircuitFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_red.png"));
		} else {
			icon_trackCircuitFailure.setIcon(new ImageIcon("Modules\\TrackModel\\images\\statusIcon_grey.png"));
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
		trackModel.setTrack("green", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/GreenLineFinal.csv"));
		trackSelected = trackModel.getTrack("green");
		blockSelected = trackSelected.get(0);

		greenLineDisplay = new DynamicDisplay(trackModel.getTrack("green"));
		currentDisplay = greenLineDisplay;

		comboBox_selectTrack.addItem("GREEN LINE");
		comboBox_selectTrack.setSelectedItem("GREEN LINE");
		
		trackModel.setTrack("red", (new TrackCsvParser()).parse("Modules/TrackModel/Track Layout/RedLineFinal.csv"));
		trackSelected = trackModel.getTrack("red");
		blockSelected = trackSelected.get(0);

		redLineDisplay = new DynamicDisplay(trackModel.getTrack("red"));
		currentDisplay = redLineDisplay;

		comboBox_selectTrack.addItem("RED LINE");
		comboBox_selectTrack.setSelectedItem("RED LINE");
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
