/*
 * Track Model Sub-System Module / GUI Interface
 * Written by Kevin Le for COE 1186: Software Engineering, Fall 2017
 */

import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager; 
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class TrackModelGUI extends JFrame{

	/* CONSTANTS */
	private static final int GUI_WINDOW_WIDTH 	= 880;
	private static final int GUI_WINDOW_HEIGHT 	= 480;
	private static final int GUI_WINDOW_XPOS  	= 200;
	private static final int GUI_WINDOW_YPOS	= 200;
	
	private static final int IMPORT_BOX_XPOS 	= 79;
	private static final int IMPORT_BOX_YPOS 	= 29;
	private static final int IMPORT_BOX_WIDTH 	= 197;
	private static final int IMPORT_BOX_HEIGHT 	= 20;
	private static final int IMPORT_BOX_COLUMNS = 10;

	/* GUI COMPONENTS */
	private JLabel 		labelTrack 				= new JLabel("Track:");
	private JTextField 	textField 				= new JTextField();
	private JButton 	buttonImport 			= new JButton("Import");
	private String 		trackFilename 			= new String("");
	private JTextField 	filename 				= new JTextField(), directory = new JTextField();
	
	private JLabel 		labelTrackLayout 		= new JLabel("Click to Select Block");
	private JLabel 		labelMouseOverBlock 	= new JLabel("");
	private JPanel 		panelTrackView			= new JPanel();
	
	private JPanel 		contentPane             = new JPanel();

	private JLabel 		labelSpecifyBlock 		= new JLabel("Specify Block");
	private JLabel 		labelBlockID 			= new JLabel();
	private JLabel 		iconSwitch 				= new JLabel("");

	private JButton 	buttonPrevious 			= new JButton("prev");
	private JButton 	buttonNext 				= new JButton("next");

	private JLabel 		labelRailFailure 		= new JLabel("Rail Failure");
	private JLabel 		labelTrackCtFailure 	= new JLabel("Track Circuit Failure");
	private JLabel 		labelPowerFailure 		= new JLabel("Power Failure");

	private JLabel 		labelInfrastructure 	= new JLabel("Infrastructure");
	private JLabel 		labelUnderground 		= new JLabel("Underground");
	private JLabel 		labelRailCrossing 		= new JLabel("Rail Crossing");
	private JLabel 		labelSwitch 			= new JLabel("Switch");
	private JLabel 		labelState 				= new JLabel("State");
	private JLabel 		labelStation 			= new JLabel("Station");
	private JLabel 		labelNearestStation 	= new JLabel("Nearest Station");
	private JLabel 		labelStationName 		= new JLabel("<no station>");
	private JLabel 		labelAt 				= new JLabel("@");
	private JLabel 		labelStationBlockID 	= new JLabel("StationBlockID");
	private JLabel 		labelTrackCircuit 		= new JLabel("Track Circuit");
	private JLabel 		labelSpeed 				= new JLabel("Speed");
	private JLabel 		labelAuthority 			= new JLabel("Authority");
	private JLabel 		labelTrackHeated 		= new JLabel("Track Heated");

	private JLabel 		labelSwitchSource 		= new JLabel("P");
	private JLabel 		labelSwitchDest1 		= new JLabel("S1");
	private JLabel 		labelSwitchDest2 		= new JLabel("S3");

	private JLabel 		iconUndergroundStatus 	= new JLabel("");
	private JLabel 		iconRailStatus 			= new JLabel("");
	private JLabel 		iconSwitchStatus 		= new JLabel("");
	private JLabel 		iconStationStatus 		= new JLabel("");
	private JLabel 		iconRailFailure 		= new JLabel("");
	private JLabel 		iconTrackCtFailure  	= new JLabel("");
	private JLabel 		iconPowerFailure 		= new JLabel("");
	private JLabel 		iconTrackHeated 		= new JLabel("");
	private JLabel 		logoPineapple 			= new JLabel(new ImageIcon("images/pineapple_icon.png"));

	private JLabel 		labelBlockOccupied 		= new JLabel("Block Occupied");
	private JLabel 		labelLength 			= new JLabel("Length:");
	private JLabel 		labelGrade 				= new JLabel("Grade:");
	private JLabel 		labelElevation 			= new JLabel("Elevation:");
	private JLabel 		labelCumElev 			= new JLabel("Cum. Elev:");
	private JLabel 		labelSpeedLimit 		= new JLabel("Speed Limit:");

	private JLabel 		dataBlockOccupied 		= new JLabel(new ImageIcon("images/greyStatusIcon.png"));
	private JLabel 		dataBlockLength 		= new JLabel("<no track>");
	private JLabel 		dataBlockGrade 			= new JLabel("<no track>");
	private JLabel 		dataBlockElevation 		= new JLabel("<no track>");
	private JLabel 		dataCumElevation 		= new JLabel("<no track>");
	private JLabel 		dataSpeedLimit 			= new JLabel("<no track>");

	private JSeparator 	separator1 				= new JSeparator();
	private JSeparator 	separator2 				= new JSeparator();

	/* VARIABLES */
	private ArrayList<Block> 	blocks 				= new ArrayList<Block>();
	private Block 				selectedBlock 		= new Block();
	private int 				selectedBlockIndex 	= 0;
	
	private boolean 			firstTime 			= true;
	private boolean 			switchID 			= false;

	public TrackModelGUI() {

		// Main window config
		this.setTitle("Track Model");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(	GUI_WINDOW_XPOS, 
						GUI_WINDOW_YPOS, 
						GUI_WINDOW_WIDTH, 
						GUI_WINDOW_HEIGHT );

		// Primary content pane config
		contentPane.setLayout(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		
		panelTrackView.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTrackView.setBackground(new Color(200, 200, 200));
		panelTrackView.setBounds(28, 87, 351, 341);
		panelTrackView.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e){
				if ((e.getY()/2 < blocks.size())&&(e.getY()/2 > 0)){
					int currentPos = e.getY()/2;
					labelMouseOverBlock.setText(blocks.get(currentPos-1).section + Integer.toString(currentPos));
				}
			}
			public void mouseDragged(MouseEvent e){
				if ((e.getY()/2 < blocks.size())&&(e.getY()/2 > 0)){
					selectedBlockIndex = e.getY()/2;

					labelMouseOverBlock.setText(blocks.get(selectedBlockIndex-1).section + Integer.toString(selectedBlockIndex));
					labelBlockID.setText(blocks.get(selectedBlockIndex-1).section + Integer.toString(selectedBlockIndex));
					showBlockStaticInfo(blocks.get(selectedBlockIndex-1), !firstTime);
				}
			}
		});
		panelTrackView.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e){
				labelMouseOverBlock.setForeground(Color.GREEN);
			}
			public void mouseExited(MouseEvent e){
				// Do nothing right now
			}
			public void mouseEntered(MouseEvent e){
				// Do nothing right now
			}
			public void mouseReleased(MouseEvent e){
				labelMouseOverBlock.setForeground(Color.WHITE);
			}
			public void mouseClicked(MouseEvent e){
				if ((e.getY()/2 < blocks.size())&&(e.getY()/2 > 0)){
					selectedBlockIndex = e.getY()/2;

					labelBlockID.setText(blocks.get(selectedBlockIndex-1).section + Integer.toString(selectedBlockIndex));
					showBlockStaticInfo(blocks.get(selectedBlockIndex-1), !firstTime);
				}
			}
		});
		contentPane.add(panelTrackView);
		
		labelTrack.setBounds(37, 32, 46, 14);
		contentPane.add(labelTrack);

		textField.setColumns(IMPORT_BOX_COLUMNS);
		textField.setBounds( IMPORT_BOX_XPOS, 
							 IMPORT_BOX_YPOS, 
							 IMPORT_BOX_WIDTH, 
							 IMPORT_BOX_HEIGHT );
		contentPane.add(textField);
		
		stylizeButton(buttonImport);
		buttonImport.setBounds(286, 28, 65, 23);
		buttonImport.addActionListener(new OpenL());
		contentPane.add(buttonImport);
		
		showBlockInfoHeaders();
		showBlockStaticInfo(selectedBlock, firstTime);
		
		labelSpecifyBlock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		labelSpecifyBlock.setForeground(Color.DARK_GRAY);
		labelSpecifyBlock.setBounds(480, 29, 96, 14);
		contentPane.add(labelSpecifyBlock);

		labelBlockID.setBounds(508, 63, 38, 14);
		labelBlockID.setFont(new Font("Consolas", Font.BOLD, 14));
		contentPane.add(labelBlockID);

		stylizeButton(buttonNext);
		buttonNext.setBounds(548, 59, 55, 23);
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				selectedBlockIndex++;
				if (selectedBlockIndex >= blocks.size()){
					selectedBlockIndex = 1;
				}
				labelBlockID.setText(blocks.get(selectedBlockIndex-1).section + Integer.toString(selectedBlockIndex));
				showBlockStaticInfo(blocks.get(selectedBlockIndex-1), !firstTime);
			}
		});
		contentPane.add(buttonNext);
		
		stylizeButton(buttonPrevious);
		buttonPrevious.setBounds(439, 59, 55, 23);
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedBlockIndex--;
				if (selectedBlockIndex <= 0){
					selectedBlockIndex = blocks.size();
				}
				labelBlockID.setText(blocks.get(selectedBlockIndex-1).section + Integer.toString(selectedBlockIndex));
				showBlockStaticInfo(blocks.get(selectedBlockIndex-1), !firstTime);
			}
		});
		contentPane.add(buttonPrevious);

		labelRailFailure.setBounds(439, 261, 78, 14);
		labelTrackCtFailure.setBounds(439, 276, 105, 14);
		labelPowerFailure.setBounds(439, 292, 78, 14);
		
		contentPane.add(labelRailFailure);
		contentPane.add(labelTrackCtFailure);
		contentPane.add(labelPowerFailure);
	
		
		labelInfrastructure.setForeground(Color.DARK_GRAY);		
		labelInfrastructure.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelInfrastructure.setBounds(708, 29, 112, 14);
		contentPane.add(labelInfrastructure);
		
		labelUnderground.setBounds(664, 51, 65, 14);
		labelRailCrossing.setBounds(664, 67, 65, 14);
		labelSwitch.setBounds(664, 84, 65, 14);
		
		contentPane.add(labelUnderground);
		contentPane.add(labelRailCrossing);
		contentPane.add(labelSwitch);
		
		iconSwitch.setIcon(new ImageIcon("images/switchState0.png"));
		iconSwitch.setBounds(722, 146, 46, 31);
		contentPane.add(iconSwitch);

		labelState.setBounds(735, 118, 65, 14);
		labelState.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelState);
		
		//-------Get from switch-----------
		labelSwitchSource.setBounds(713, 156, 24, 14);
		labelSwitchSource.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelSwitchSource);
		
		labelSwitchDest1.setBounds(771, 143, 24, 14);
		labelSwitchDest1.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelSwitchDest1);
		
		labelSwitchDest2.setBounds(771, 168, 24, 14);
		labelSwitchDest2.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelSwitchDest2);
		//---------------------------------

		labelTrackLayout.setBounds(130, 71, 150, 14);
		labelTrackLayout.setFont(new Font("Consolas", Font.BOLD, 12));
		labelTrackLayout.setForeground(Color.DARK_GRAY);
		contentPane.add(labelTrackLayout);
		
		labelMouseOverBlock.setBounds(142, 20, 120, 14);
		labelMouseOverBlock.setFont(new Font("Consolas", Font.BOLD, 60));
		labelMouseOverBlock.setForeground(Color.WHITE);
		panelTrackView.add(labelMouseOverBlock);

		labelStation.setBounds(664, 189, 65, 14);
		contentPane.add(labelStation);
		
		labelNearestStation.setBounds(713, 214, 87, 14);
		labelNearestStation.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelNearestStation);
		
		labelStationName.setBounds(670, 234, 100, 14);
		labelStationName.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelStationName);

		labelAt.setBounds(753, 234, 24, 14);
		labelAt.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelAt);
		
		labelStationBlockID.setBounds(771, 234, 83, 14);
		labelStationBlockID.setForeground(Color.LIGHT_GRAY);
		contentPane.add(labelStationBlockID);
		
		labelTrackCircuit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelTrackCircuit.setBounds(710, 258, 83, 14);
		contentPane.add(labelTrackCircuit);
		
		separator1.setOrientation(SwingConstants.VERTICAL);
		separator1.setBounds(627, 29, 9, 385);
		contentPane.add(separator1);
		
		separator2.setOrientation(SwingConstants.VERTICAL);
		separator2.setBounds(405, 29, 2, 387);
		contentPane.add(separator2);
		
		labelSpeed.setBounds(664, 273, 65, 14);
		contentPane.add(labelSpeed);
		
		labelAuthority.setBounds(664, 288, 65, 14);
		contentPane.add(labelAuthority);
		
		labelTrackHeated.setBounds(664, 317, 65, 14);
		contentPane.add(labelTrackHeated);
		
		iconUndergroundStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconUndergroundStatus.setBounds(646, 51, 46, 14);
		contentPane.add(iconUndergroundStatus);

		iconRailStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconRailStatus.setBounds(646, 67, 46, 14);
		contentPane.add(iconRailStatus);
		
		iconSwitchStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconSwitchStatus.setBounds(646, 84, 46, 14);
		contentPane.add(iconSwitchStatus);

		iconStationStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconStationStatus.setBounds(646, 189, 46, 14);
		contentPane.add(iconStationStatus);

		iconRailFailure.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconRailFailure.setBounds(421, 261, 46, 14);
		contentPane.add(iconRailFailure);

		iconTrackCtFailure.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconTrackCtFailure.setBounds(421, 276, 46, 14);
		contentPane.add(iconTrackCtFailure);

		iconPowerFailure.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconPowerFailure.setBounds(421, 292, 46, 14);
		contentPane.add(iconPowerFailure);

		iconTrackHeated.setIcon(new ImageIcon("images/greyStatusIcon.png"));
		iconTrackHeated.setBounds(646, 317, 65, 14);
		contentPane.add(iconTrackHeated);

		logoPineapple.setBounds(GUI_WINDOW_WIDTH - 150, GUI_WINDOW_HEIGHT-12-100, 138, 76);
		contentPane.add(logoPineapple);
	}

	public static void setUILookAndFeel(){
		try  {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	class OpenL implements ActionListener {
	    public void actionPerformed(ActionEvent e) {

			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(TrackModelGUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(c.getSelectedFile().getName());
				directory.setText(c.getCurrentDirectory().toString());
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("You pressed cancel");
				directory.setText("");
			}

			textField.setText(c.getSelectedFile().getName());
			textField.setForeground(Color.LIGHT_GRAY);
			textField.setEditable(false);

			selectedBlockIndex = 0;
			panelTrackView.setBackground(Color.DARK_GRAY);

			trackFilename = c.getSelectedFile().getName();
			parseFile(trackFilename);
	    }
	}

	public void parseFile(String f){

		BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ",";

		try {
			br = new BufferedReader(new FileReader(f));

			// Read from csv file and create blocks for each line, then
			// add each block to an arraylist of blocks

			while ((currline = br.readLine()) != null){
				String [] blockInfo = currline.split(delimeter);

				String 	line 			= blockInfo[0];
				String 	section 		= blockInfo[1];
				int 	id 				= Integer.parseInt(blockInfo[2]);
				double 	length 			= Double.parseDouble(blockInfo[3]);
				double 	grade 			= Double.parseDouble(blockInfo[4]);
				int 	speedLimit 		= Integer.parseInt(blockInfo[5]);
				String 	station 		= blockInfo[6];
				String  switchBlock     = blockInfo[7];
				String 	underground     = blockInfo[8];
				double 	elevation 		= Double.parseDouble(blockInfo[9]);
				double 	cumElevation 	= Double.parseDouble(blockInfo[10]);
				String 	switchID     	= blockInfo[11];
				String  partOfSection   = blockInfo[12];
				int     direction       = Integer.parseInt(blockInfo[13]);
				String  crossing 		= blockInfo[14];
				String  switchType      = blockInfo[15];


				Block currBlock = new Block(line,
											section,
											id,
											length,
											grade,
											speedLimit,
											station,
											switchBlock,
											underground,
											elevation,
											cumElevation,
											switchID,
											partOfSection,
											direction,
											crossing,
											switchType );

				blocks.add(currBlock);
			}

			labelBlockID.setText(blocks.get(selectedBlockIndex+1).section + Integer.toString(selectedBlockIndex+1));
			showBlockStaticInfo(blocks.get(0), !firstTime);

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

	public void showBlockInfoHeaders(){
		labelBlockOccupied.setBounds(439, 118, 78, 14);
		contentPane.add(labelBlockOccupied);
		
		labelLength.setBounds(439, 134, 78, 14);
		contentPane.add(labelLength);
		
		labelGrade.setBounds(439, 151, 78, 14);
		contentPane.add(labelGrade);
		
		labelElevation.setBounds(439, 168, 78, 14);
		contentPane.add(labelElevation);
		
		labelCumElev.setBounds(439, 185, 78, 14);
		contentPane.add(labelCumElev);
		
		labelSpeedLimit.setBounds(439, 201, 78, 14);
		contentPane.add(labelSpeedLimit);
	}

	public void showBlockStaticInfo(Block b, boolean firstTime){
		if (firstTime){
			dataBlockOccupied.setBounds(389, 118, 78, 14);
			contentPane.add(dataBlockOccupied);

			dataBlockLength.setBounds(530, 134, 78, 14);
			contentPane.add(dataBlockLength);

			dataBlockGrade.setBounds(530, 151, 78, 14);
			contentPane.add(dataBlockGrade);

			dataBlockElevation.setBounds(530, 168, 78, 14);
			contentPane.add(dataBlockElevation);

			dataCumElevation.setBounds(530, 185, 78, 14);
			contentPane.add(dataCumElevation);

			dataSpeedLimit.setBounds(530, 201, 78, 14);
			contentPane.add(dataSpeedLimit);

			iconTrackHeated.setIcon(new ImageIcon("images/greenStatusIcon.png"));
		}

		if (!firstTime){

			dataBlockOccupied.setText("");
			if (selectedBlockIndex == 2){
				dataBlockOccupied.setIcon(new ImageIcon("images/greenStatusIcon.png"));
			} else {
				dataBlockOccupied.setIcon(new ImageIcon("images/greyStatusIcon.png"));
			}
			dataBlockLength.setText(Double.toString(b.length) + " m");
			dataBlockGrade.setText(Double.toString(b.grade) + "%");
			dataBlockElevation.setText(Double.toString(b.elevation) + " m");
			dataCumElevation.setText(Double.toString(b.cumElevation) + " m");
			dataSpeedLimit.setText(Integer.toString(b.speedLimit) + " mph");

			// Check for Underground
			try{
				if (Character.toLowerCase(b.underground.charAt(0)) == 'u'){
					iconUndergroundStatus.setIcon(new ImageIcon("images/greenStatusIcon.png"));
				} else {
					iconUndergroundStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
				}
			} catch (StringIndexOutOfBoundsException e){
				iconUndergroundStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
			}

			// Check for Railroad Crossing
			try{
				if (Character.toLowerCase(b.crossing.charAt(0)) == 'r'){
					iconRailStatus.setIcon(new ImageIcon("images/greenStatusIcon.png"));
				} else {
					iconRailStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
				}
			} catch (StringIndexOutOfBoundsException e){
				iconRailStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
			}

			// Check for Switch
			try{
				if (Character.toLowerCase(b.switchID.charAt(0)) == 's'){
					// FIX THIS TO ACTUALLY SHOW THE REAL SWITCH POSITION
					iconSwitchStatus.setIcon(new ImageIcon("images/greenStatusIcon.png"));
					
					iconSwitch.setIcon(new ImageIcon("images/switchState1.png"));
					labelState.setForeground(Color.BLACK);
					labelSwitchSource.setForeground(Color.BLACK);
					labelSwitchDest1.setForeground(Color.BLACK);
					labelSwitchDest2.setForeground(Color.BLACK);
				} else {
					iconSwitchStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
					
					iconSwitch.setIcon(new ImageIcon("images/switchState0.png"));
					labelState.setForeground(Color.LIGHT_GRAY);
					labelSwitchSource.setForeground(Color.LIGHT_GRAY);
					labelSwitchDest1.setForeground(Color.LIGHT_GRAY);
					labelSwitchDest2.setForeground(Color.LIGHT_GRAY);
				}
			} catch (StringIndexOutOfBoundsException e){
				iconSwitchStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
				
				iconSwitch.setIcon(new ImageIcon("images/switchState0.png"));
				labelState.setForeground(Color.LIGHT_GRAY);
				labelSwitchSource.setForeground(Color.LIGHT_GRAY);
				labelSwitchDest1.setForeground(Color.LIGHT_GRAY);
				labelSwitchDest2.setForeground(Color.LIGHT_GRAY);
			}

			// Check for Station
			try{
				if (b.station.length() > 0){
					iconStationStatus.setIcon(new ImageIcon("images/greenStatusIcon.png"));
					labelStationName.setForeground(Color.BLACK);
					
					String stationName = new String("");
					for (int i = 0; i < b.station.length(); i++){
						if (b.station.charAt(i) != '|'){
							stationName += b.station.charAt(i);
						} else {
							break;
						}
					}

					labelStationName.setText(stationName);
				} else {
					iconStationStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
					labelStationName.setText("<no station>");
					labelStationName.setForeground(Color.LIGHT_GRAY);
				}
			} catch (StringIndexOutOfBoundsException e){
				iconStationStatus.setIcon(new ImageIcon("images/greyStatusIcon.png"));
				labelStationName.setText("<no station>");
				labelStationName.setForeground(Color.LIGHT_GRAY);
			}
		}
	}


	public void stylizeButton(JButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
    	b.setBorder(thickBorder);
		b.setContentAreaFilled(false);
		b.setOpaque(true);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}


	public static void main(String[] args) {
		setUILookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackModelGUI frame = new TrackModelGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
