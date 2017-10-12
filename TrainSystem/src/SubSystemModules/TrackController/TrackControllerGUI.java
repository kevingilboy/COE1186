/*
 * Track Controller Sub-System Module / GUI Interface
 * Written by Nick Petro for COE 1186: Software Engineering, Fall 2017
 */

import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class TrackControllerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textStatus;
	private JTextField textOccupancy;
	
	JComboBox comboBlock = new JComboBox();
	JRadioButton switchButtonTop = new JRadioButton();
	JRadioButton switchButtonBottom = new JRadioButton();
	JLabel labelCrossingGraphic = new JLabel();
	JLabel labelLightGraphic = new JLabel();
	JComboBox comboOccupancy = new JComboBox();
	
	private Plc newPlc = new Plc();
	private int altSelected = 0;
	private int selectedBlockIndex = 0;
	private static ArrayList<Block> blocks = new ArrayList<Block>();
	private Block selectedBlock = new Block();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackControllerGUI frame = new TrackControllerGUI();
					frame.setVisible(true);
					parseFile("greenline.csv");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TrackControllerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 0, 888, 507);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		//Sub-Panels
		JPanel trackSelectorPanel = new JPanel();
		trackSelectorPanel.setBorder(null);
		trackSelectorPanel.setBounds(346, 55, 502, 67);
		panel.add(trackSelectorPanel);
		trackSelectorPanel.setLayout(null);
		
		JPanel trackInfoPanel = new JPanel();
		trackInfoPanel.setBorder(null);
		trackInfoPanel.setBounds(346, 119, 502, 329);
		panel.add(trackInfoPanel);
		trackInfoPanel.setLayout(null);
		
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(10, 32, 487, 59);
		trackInfoPanel.add(updatePanel);
		updatePanel.setLayout(null);
		
		JPanel switchPanel = new JPanel();
		switchPanel.setBorder(null);
		switchPanel.setBounds(131, 220, 240, 66);
		trackInfoPanel.add(switchPanel);
		switchPanel.setLayout(null);
		
		JPanel lightsPanel = new JPanel();
		lightsPanel.setLayout(null);
		lightsPanel.setBorder(null);
		lightsPanel.setBounds(131, 103, 240, 123);
		trackInfoPanel.add(lightsPanel);
		
		//Separators
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(0, 0, 502, 12);
		trackSelectorPanel.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(0, 54, 502, 12);
		trackSelectorPanel.add(separator2);
		
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(0, 91, 502, 21);
		trackInfoPanel.add(separator3);
		
		//GUI Title
		JLabel labelTrackControllerInterface = new JLabel("<html><b>Track Controller Interface</b><html>");
		labelTrackControllerInterface.setBounds(346, 0, 502, 54);
		panel.add(labelTrackControllerInterface);
		labelTrackControllerInterface.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelTrackControllerInterface.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Track Image
		JLabel labelTrackImg = new JLabel();
		labelTrackImg.setBackground(Color.WHITE);
		labelTrackImg.setBounds(0, 0, 334, 448);
		panel.add(labelTrackImg);
		labelTrackImg.setIcon(new ImageIcon("imgs/track.png"));
		
		//Block Selectors
		JLabel labelBlock = new JLabel("Block");
		labelBlock.setBounds(338, 23, 34, 16);
		trackSelectorPanel.add(labelBlock);
		
		JLabel labelSection = new JLabel("Section");
		labelSection.setBounds(164, 23, 46, 16);
		trackSelectorPanel.add(labelSection);
		
		JLabel labelLine = new JLabel("Line");
		labelLine.setBounds(10, 23, 26, 16);
		trackSelectorPanel.add(labelLine);
		
		comboBlock.setBounds(384, 19, 104, 27);
		trackSelectorPanel.add(comboBlock);
		comboBlock.setModel(new DefaultComboBoxModel(new String[] {"-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156"}));
		comboBlock.addActionListener(new BlockChange());
		
		JComboBox comboSection = new JComboBox();
		comboSection.setBounds(222, 19, 104, 27);
		trackSelectorPanel.add(comboSection);
		comboSection.setModel(new DefaultComboBoxModel(new String[] {"-", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "ZZ", "YY"}));
		
		JComboBox comboLine = new JComboBox();
		comboLine.setBounds(48, 19, 104, 27);
		trackSelectorPanel.add(comboLine);
		comboLine.setModel(new DefaultComboBoxModel(new String[] {"-", "Green"}));
		
		//Controller Specifier
		JLabel labelController = new JLabel("<html><b>Controller - G1</b><html>");
		labelController.setBounds(0, -1, 502, 32);
		trackInfoPanel.add(labelController);
		labelController.setHorizontalAlignment(SwingConstants.CENTER);
	
		//Block Status
		JLabel labelStatus = new JLabel("Status");
		labelStatus.setBounds(136, 0, 73, 28);
		updatePanel.add(labelStatus);
		labelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textStatus = new JTextField();
		textStatus.setBounds(220, 0, 134, 28);
		updatePanel.add(textStatus);
		textStatus.setHorizontalAlignment(SwingConstants.CENTER);
		textStatus.setText("-");
		textStatus.setEditable(false);
		textStatus.setColumns(10);
		
		//Block Occupancy
		JLabel labelOccupancy = new JLabel("Occupancy");
		labelOccupancy.setBounds(135, 29, 73, 28);
		updatePanel.add(labelOccupancy);
		labelOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		comboOccupancy.setBounds(220, 29, 134, 28);
		updatePanel.add(comboOccupancy);
		comboOccupancy.setModel(new DefaultComboBoxModel(new String[] {"False", "True"}));
		
		//Block Switch
		JLabel labelSwitchState = new JLabel("Switch State");
		labelSwitchState.setBounds(0, 6, 88, 57);
		switchPanel.add(labelSwitchState);
		labelSwitchState.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel labelSwitchGraphic = new JLabel();
		labelSwitchGraphic.setIcon(new ImageIcon("imgs/switch.png"));
		labelSwitchGraphic.setBounds(100, 17, 55, 33);
		switchPanel.add(labelSwitchGraphic);
		
		switchButtonTop.setBounds(153, 6, 80, 28);
		switchPanel.add(switchButtonTop);
		switchButtonTop.setHorizontalAlignment(SwingConstants.LEFT);
		
		switchButtonBottom.setBounds(153, 35, 80, 28);
		switchPanel.add(switchButtonBottom);
		switchButtonBottom.setHorizontalAlignment(SwingConstants.LEFT);
		
		//Block Lights
		JLabel labelLights = new JLabel("Lights");
		labelLights.setHorizontalAlignment(SwingConstants.TRAILING);
		labelLights.setBounds(0, 0, 88, 57);
		lightsPanel.add(labelLights);
		
		labelLightGraphic.setBounds(149, 0, 39, 57);
		lightsPanel.add(labelLightGraphic);
		labelLightGraphic.setIcon(new ImageIcon("imgs/lightsOff.png"));
		
		//Block Crossing
		JLabel labelCrossing = new JLabel("Crossing");
		labelCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		labelCrossing.setBounds(0, 60, 88, 57);
		lightsPanel.add(labelCrossing);
		
		labelCrossingGraphic.setIcon(new ImageIcon("imgs/crossingOff.png"));
		labelCrossingGraphic.setForeground(Color.LIGHT_GRAY);
		labelCrossingGraphic.setFont(new Font("Helvetica", Font.BOLD, 40));
		labelCrossingGraphic.setHorizontalAlignment(SwingConstants.CENTER);
		labelCrossingGraphic.setBounds(149, 60, 39, 57);
		lightsPanel.add(labelCrossingGraphic);
		
		//Buttons
		JButton buttonUpdate = new JButton("Update");
		buttonUpdate.setBounds(260, 294, 96, 29);
		buttonUpdate.addActionListener(new UpdateGui());
		trackInfoPanel.add(buttonUpdate);
		
		JButton buttonImportPlc = new JButton("Import PLC");
		buttonImportPlc.setBounds(141, 294, 117, 29);
		buttonImportPlc.addActionListener(new OpenPlc());
		trackInfoPanel.add(buttonImportPlc);
		
		//Pineapple logo
		JLabel labelPineapple = new JLabel("");
		labelPineapple.setIcon(new ImageIcon("imgs/pineapple_icon.png"));
		labelPineapple.setBounds(364, 253, 138, 76);
		trackInfoPanel.add(labelPineapple);

	}
	
	/*
		Action Listeners
	*/	
	class UpdateGui implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
			updateOccupancy();
			getSelectedBlock();
			updateBlockInfo();
			updateLights();
			updateCrossing();
			updateSwitch();
	    }
	}
	
	class BlockChange implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
			getSelectedBlock();
			updateBlockInfo();
			updateLights();
			updateCrossing();
			updateSwitch();
	    }
	}
	
	class OpenPlc implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(TrackControllerGUI.this);
			System.out.println("Filename = " + c.getSelectedFile().getName());
			System.out.println("Directory = " + c.getCurrentDirectory().toString());
			
			parsePlc(c.getCurrentDirectory().toString(), c.getSelectedFile().getName());
	    }
	}
	
	/*
		Update Functions
	*/	
	public void getSelectedBlock(){
		if(!comboBlock.getSelectedItem().equals("-")){
			selectedBlockIndex = Integer.parseInt((String)comboBlock.getSelectedItem());
			selectedBlock = blocks.get(selectedBlockIndex);
		}
	}
		
	public void updateBlockInfo(){
		textStatus.setText(selectedBlock.status);
		if(selectedBlock.occupancy == true){
			comboOccupancy.setSelectedItem("True");
		} else {
			comboOccupancy.setSelectedItem("False");
		}
	}
	
	public void updateLights(){
		if(!blocks.get(selectedBlockIndex+1).occupancy || altSelected == 1){
			labelLightGraphic.setIcon(new ImageIcon("imgs/greenLight.png"));
			altSelected = 0;
		} else {
			labelLightGraphic.setIcon(new ImageIcon("imgs/redLight.png"));
		}
	}
	
	public void updateCrossing(){
		try{
			if((Character.toLowerCase(selectedBlock.crossing.charAt(0)) == 'r') && (selectedBlock.occupancy)){
				labelCrossingGraphic.setIcon(new ImageIcon("imgs/crossingOn.gif"));
			} else {
				labelCrossingGraphic.setIcon(new ImageIcon("imgs/crossingOff.png"));
			}
		} catch (StringIndexOutOfBoundsException e){
			System.out.println("out of bounds");
		}
	}
	
	public void updateOccupancy(){
		if (comboOccupancy.getSelectedItem().equals("True")){
			selectedBlock.occupancy = true;
		} else {
			selectedBlock.occupancy = false;
		}
	}
	
	public void updateSwitch(){
		try{
			if((Character.toLowerCase(selectedBlock.switchBlock.charAt(0)) == 's') && !blocks.get(selectedBlockIndex+1).occupancy){
				switchButtonTop.setSelected(false);
				switchButtonTop.setText("Alt");
				switchButtonBottom.setSelected(true);
				switchButtonBottom.setText("Norm");
				altSelected = 0;
			} else if ((Character.toLowerCase(selectedBlock.switchBlock.charAt(0)) == 's') && blocks.get(selectedBlockIndex+1).occupancy){
				switchButtonTop.setSelected(true);
				switchButtonTop.setText("Alt");
				switchButtonBottom.setSelected(false);
				switchButtonBottom.setText("Norm");
				altSelected = 1;
				updateLights();
			}
		} catch (StringIndexOutOfBoundsException e){
			switchButtonTop.setSelected(false);
			switchButtonTop.setText("-");
			switchButtonBottom.setSelected(false);
			switchButtonBottom.setText("-");
			altSelected = 0;
		}
	}
	
	/*
		For Plc file
	*/
	public void parsePlc(String d, String f){
		
		String path = d + "/" + f;

		System.out.println("parsing file: " + path);
		BufferedReader 	br 			= null;
		String 			currline 	= "";
		String 			delimeter 	= ":";

		try {
			br = new BufferedReader(new FileReader(path));
			String [] plcString = new String[8];
			int location = 0;
			
			// Read from plc file and create logic for each line, then
			// add each statement to an arraylist of logic statements

			while ((currline = br.readLine()) != null){
				String [] logicStatement = currline.split(delimeter);
				location++;
				
				String 	logicFor 		= logicStatement[0].replaceAll("\\s+","");
				String 	logic 			= logicStatement[1].replaceAll("\\s+","");
				
				plcString[location-1] = logicFor;
				plcString[location] = logic;
					
				System.out.println(logicFor);
				System.out.println(logic);
			}
			newPlc = new Plc(plcString[0], plcString[1], plcString[2], plcString[3], plcString[4], plcString[5], plcString[6], plcString[7]);
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
	
	/*
		For Block Info --- needed for demo only
	*/
	public static void parseFile(String f){
		System.out.println("track imported");
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
				String	status			= "Open";
				boolean	occupancy		= false;


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
											switchType,
											status,
											occupancy );
				blocks.add(currBlock);
			}
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
}
