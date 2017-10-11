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
	
	JRadioButton switchButtonTop = new JRadioButton("");
	JRadioButton switchButtonBottom = new JRadioButton("");
	JLabel labelCrossingGraphic = new JLabel("");
	
	private String holdFilename = new String("");
	private String holdDirectory = new String("");
	private Plc newPlc = new Plc();
	private Block selectedBlock = new Block("green","A",1,100,0.5,55,"","","",0.5,0.5,"Switch 1","Head",1,"CROSSING","-","Open",false);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackControllerGUI frame = new TrackControllerGUI();
					frame.setVisible(true);
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
		
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(0, 282, 502, 21);
		trackInfoPanel.add(separator4);
		
		//GUI Title
		JLabel labelTrackControllerInterface = new JLabel("<html><b>Track Controller Interface</b><html>");
		labelTrackControllerInterface.setBounds(346, 0, 502, 54);
		panel.add(labelTrackControllerInterface);
		labelTrackControllerInterface.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelTrackControllerInterface.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Track Image
		JLabel labelTrackImg = new JLabel("");
		labelTrackImg.setBackground(Color.WHITE);
		labelTrackImg.setBounds(0, 0, 334, 448);
		panel.add(labelTrackImg);
		labelTrackImg.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/track.png"));
		
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
		
		JComboBox comboBlock = new JComboBox();
		comboBlock.setBounds(384, 19, 104, 27);
		trackSelectorPanel.add(comboBlock);
		comboBlock.setModel(new DefaultComboBoxModel(new String[] {"1"}));
		
		JComboBox comboSection = new JComboBox();
		comboSection.setBounds(222, 19, 104, 27);
		trackSelectorPanel.add(comboSection);
		comboSection.setModel(new DefaultComboBoxModel(new String[] {"A"}));
		
		JComboBox comboLine = new JComboBox();
		comboLine.setBounds(48, 19, 104, 27);
		trackSelectorPanel.add(comboLine);
		comboLine.setModel(new DefaultComboBoxModel(new String[] {"Red","Green"}));
		
		//Controller Specifier
		JLabel labelController = new JLabel("<html><b>Controller - GA</b><html>");
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
		textStatus.setText("");
		textStatus.setEditable(false);
		textStatus.setColumns(10);
		
		//Block Occupancy
		JLabel labelOccupancy = new JLabel("Occupancy");
		labelOccupancy.setBounds(135, 29, 73, 28);
		updatePanel.add(labelOccupancy);
		labelOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textOccupancy = new JTextField();
		textOccupancy.setBounds(220, 29, 134, 28);
		updatePanel.add(textOccupancy);
		textOccupancy.setText("");
		textOccupancy.setHorizontalAlignment(SwingConstants.CENTER);
		textOccupancy.setEditable(false);
		textOccupancy.setColumns(10);
		
		//Block Switch
		JLabel labelSwitchState = new JLabel("Switch State");
		labelSwitchState.setBounds(0, 6, 88, 57);
		switchPanel.add(labelSwitchState);
		labelSwitchState.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel labelSwitchGraphic = new JLabel("");
		labelSwitchGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/switch.png"));
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
		
		JLabel labelLightGraphic = new JLabel("");
		labelLightGraphic.setBounds(149, 0, 39, 57);
		lightsPanel.add(labelLightGraphic);
		labelLightGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/lightsOff.png"));
		
		//Block Crossing
		JLabel labelCrossing = new JLabel("Crossing");
		labelCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		labelCrossing.setBounds(0, 60, 88, 57);
		lightsPanel.add(labelCrossing);
		
		labelCrossingGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/crossingOff.png"));
		labelCrossingGraphic.setForeground(Color.LIGHT_GRAY);
		labelCrossingGraphic.setFont(new Font("Helvetica", Font.BOLD, 40));
		labelCrossingGraphic.setHorizontalAlignment(SwingConstants.CENTER);
		labelCrossingGraphic.setBounds(149, 60, 39, 57);
		lightsPanel.add(labelCrossingGraphic);
		
		//Buttons
		JButton buttonUpdate = new JButton("Update");
		buttonUpdate.setBounds(385, 298, 117, 29);
		buttonUpdate.addActionListener(new UpdateGui());
		trackInfoPanel.add(buttonUpdate);
		
		JButton buttonImportPlc = new JButton("Import PLC");
		buttonImportPlc.setBounds(256, 298, 117, 29);
		buttonImportPlc.addActionListener(new OpenFile());
		trackInfoPanel.add(buttonImportPlc);
	}
	
	class UpdateGui implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
			updateBlockInfo();
			updateCrossing();
			updateSwitch();
	    }
	}
	
	class OpenFile implements ActionListener {
	    public void actionPerformed(ActionEvent e) {

			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(TrackControllerGUI.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				//filename.setText(c.getSelectedFile().getName());
				//directory.setText(c.getCurrentDirectory().toString());
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				//filename.setText("You pressed cancel");
				//directory.setText("");
				System.out.println("You pressed cancel.");
			}
			System.out.println("Filename = " + c.getSelectedFile().getName());
			System.out.println("Directory = " + c.getCurrentDirectory().toString());

			//textField.setText(".../" + c.getSelectedFile().getName());
			//textField.setForeground(Color.gray);
			//textField.setEditable(false);

			holdFilename = c.getSelectedFile().getName();
			holdDirectory = c.getCurrentDirectory().toString();
			parseFile(holdDirectory, holdFilename);
	    }
	}
	
	/*
		Update Functions
	*/
		
	public void updateBlockInfo(){
		textStatus.setText(selectedBlock.status);
		if(selectedBlock.occupancy == true){
			textOccupancy.setText("True");
		} else {
			textOccupancy.setText("False");
		}
	}
	
	public void updateLights(){
		
	}
	
	public void updateCrossing(){
		if(!selectedBlock.switchID.equals("")){
			labelCrossingGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/crossingOn.gif"));
		} else {
			labelCrossingGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/crossingOff.png"));
		}
	}
	
	public void updateSwitch(){
		if(!selectedBlock.switchID.equals("")){
			
		} else {
			switchButtonTop.setSelected(false);
			switchButtonTop.setText("-");
			switchButtonBottom.setSelected(false);
			switchButtonBottom.setText("-");
		}
	}
	
	
	
	public void parseFile(String d, String f){
		
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
	
}
