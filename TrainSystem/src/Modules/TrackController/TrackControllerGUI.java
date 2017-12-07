package Modules.TrackController;

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
import javax.swing.UIManager; 
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("unchecked")
public class TrackControllerGUI extends JFrame{
	//Parent Class
	public TrackController tc;
	public TrackControllerGUI thisgui;
	//GUI Variables
	private JPanel contentPane;
	private JTextField textStatus;
	private JTextField textLine;
	private JTextField textOccupancy;
	JComboBox<String> comboBlock = new JComboBox<String>();
	JRadioButton switchButtonTop = new JRadioButton();
	JRadioButton switchButtonBottom = new JRadioButton();
	JLabel labelCrossingGraphic = new JLabel();
	JLabel labelLightGraphic = new JLabel();
	JComboBox comboOccupancy = new JComboBox();
	//Other Variables
	private String line;
	private String section;
	private String[] blocks;
	
	public TrackControllerGUI(TrackController tc, String controllerName){
		this.tc = tc;
		this.line = tc.associatedLine;
		this.blocks = Arrays.copyOf(tc.associatedBlocks, tc.associatedBlocks.length);
		for(int i=0; i<blocks.length; i++){
			blocks[i] = Integer.toString((Integer.parseInt(blocks[i])+1));//offset for displaying
		}
		this.thisgui = this;
		drawTrackControllerGui(tc, line, blocks, controllerName);
		System.out.println(this);
		this.setVisible(true);
	}
	
	/**
	 * Create the frame.
	 */
	public void drawTrackControllerGui(TrackController tc, String line, String[] blocks, String name) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Track Controller "+name);
		setBounds(100, 100, 870, 490);
		setResizable(false);
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
		separator1.setBounds(0, 7, 502, 12);
		trackSelectorPanel.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(0, 56, 502, 12);
		trackSelectorPanel.add(separator2);
		
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(0, 96, 502, 21);
		trackInfoPanel.add(separator3);
		
		//GUI Title
		JLabel labelTrackControllerInterface = new JLabel("<html><b>Track Controller "+name+"</b><html>");
		labelTrackControllerInterface.setBounds(346, 0, 502, 54);
		panel.add(labelTrackControllerInterface);
		labelTrackControllerInterface.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelTrackControllerInterface.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Track Image
		JLabel labelTrackImg = new JLabel();
		labelTrackImg.setBackground(Color.WHITE);
		labelTrackImg.setBounds(0, 0, 334, 448);
		panel.add(labelTrackImg);
		labelTrackImg.setIcon(new ImageIcon("Modules/TrackController/imgs/track.png"));
		
		//Block Selectors
		JLabel labelBlock = new JLabel("Block");
		labelBlock.setBounds(338, 23, 34, 16);
		trackSelectorPanel.add(labelBlock);
		
		/*JLabel labelSection = new JLabel("Section");
		labelSection.setBounds(164, 23, 46, 16);
		trackSelectorPanel.add(labelSection);
		*/
		JLabel labelLine = new JLabel("Line");
		labelLine.setBounds(10, 23, 26, 16);
		trackSelectorPanel.add(labelLine);
		
		comboBlock.setBounds(384, 19, 104, 27);
		comboBlock.setModel(new DefaultComboBoxModel<String>(blocks));
		comboBlock.setSelectedIndex(0);
		comboBlock.addActionListener(new UpdateInfo(tc));
		trackSelectorPanel.add(comboBlock);
		
		/*JComboBox comboSection = new JComboBox();
		comboSection.setBounds(222, 19, 104, 27);
		trackSelectorPanel.add(comboSection);
		comboSection.setModel(new DefaultComboBoxModel(new String[] {"-", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "ZZ", "YY"}));
		*/
		textLine = new JTextField();
		textLine.setBounds(48, 19, 104, 27);
		trackSelectorPanel.add(textLine);
		textLine.setHorizontalAlignment(SwingConstants.CENTER);
		textLine.setText(line);
		textLine.setEditable(false);
		textLine.setColumns(10);
		
		//Controller Specifier
		/*JLabel labelController = new JLabel("<html><b>Controller - "+name+"</b><html>");
		labelController.setBounds(0, -1, 502, 32);
		trackInfoPanel.add(labelController);
		labelController.setHorizontalAlignment(SwingConstants.CENTER);*/
	
		//Block Status
		/*JLabel labelStatus = new JLabel("Status");
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
		*/
		//Block Occupancy
		JLabel labelOccupancy = new JLabel("Occupancy");
		labelOccupancy.setBounds(135, 29, 73, 28);
		updatePanel.add(labelOccupancy);
		labelOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textOccupancy = new JTextField();
		textOccupancy.setBounds(220, 29, 134, 28);
		updatePanel.add(textOccupancy);
		textOccupancy.setHorizontalAlignment(SwingConstants.CENTER);
		textOccupancy.setText("-");
		textOccupancy.setEditable(false);
		textOccupancy.setColumns(10);
		
		//Block Switch
		JLabel labelSwitchState = new JLabel("Switch State");
		labelSwitchState.setBounds(0, 6, 88, 57);
		switchPanel.add(labelSwitchState);
		labelSwitchState.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel labelSwitchGraphic = new JLabel();
		labelSwitchGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/switch.png"));
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
		labelLightGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/lightsOff.png"));
		
		//Block Crossing
		JLabel labelCrossing = new JLabel("Crossing");
		labelCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		labelCrossing.setBounds(0, 60, 88, 57);
		lightsPanel.add(labelCrossing);
		
		labelCrossingGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/crossingOff.png"));
		labelCrossingGraphic.setForeground(Color.LIGHT_GRAY);
		labelCrossingGraphic.setFont(new Font("Helvetica", Font.BOLD, 40));
		labelCrossingGraphic.setHorizontalAlignment(SwingConstants.CENTER);
		labelCrossingGraphic.setBounds(149, 60, 39, 57);
		lightsPanel.add(labelCrossingGraphic);
		
		//Buttons
		/*JButton buttonUpdate = new JButton("Update");
		buttonUpdate.setBounds(260, 294, 96, 29);
		buttonUpdate.addActionListener(new updateInfo());
		trackInfoPanel.add(buttonUpdate);
		*/
		JButton buttonImportPlc = new JButton("Import PLC");
		buttonImportPlc.setBounds(141, 294, 117, 29);
		buttonImportPlc.addActionListener(new UploadPLC());
		trackInfoPanel.add(buttonImportPlc);
		
		//Pineapple logo
		JLabel labelPineapple = new JLabel("");
		labelPineapple.setIcon(new ImageIcon("Modules/TrackController/imgs/pineapple_icon.png"));
		labelPineapple.setBounds(364, 253, 138, 76);
		trackInfoPanel.add(labelPineapple);
	}	
	
	class UpdateInfo implements ActionListener {
		private TrackController tc;
		public UpdateInfo(TrackController tc){
			this.tc = tc;
		}
	    public void actionPerformed(ActionEvent e) {
			displayInfo(tc);
	    }
	}

	class UploadPLC implements ActionListener {
	    public void actionPerformed(ActionEvent e){
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(null);
			if(rVal == JFileChooser.APPROVE_OPTION) {
				String plcPath = c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName();
				System.out.println("path = " + plcPath);
				boolean parserSuccess = tc.getTcplc().parsePLC(plcPath);
			}
	    }
	}
	
	public void displayInfo(TrackController tc){
		int blockId = getSelectedBlockId();
		String line = textLine.getText();
		
		//update gui
		/*if(tc.trackModel.getBlock(line, blockId).getStatus() == ){
			textStatus.setText();
		}*/
		if(tc.trackModel.getBlock(line, blockId).getOccupied() == true){
			textOccupancy.setText("True");
		} else {
			textOccupancy.setText("False");
		}
		if (tc.trackModel.getBlock(line, blockId).getLight() != null){
			if(tc.trackModel.getBlock(line, blockId).getLight().getState() == true){
				labelLightGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/greenLight.png"));
			} else {
				labelLightGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/redLight.png"));
			}
		}
		if (tc.trackModel.getBlock(line, blockId).getSwitch() != null){
			if(tc.trackModel.getBlock(line, blockId).getSwitch().getState() == true){
				switchButtonTop.setSelected(true);
				switchButtonTop.setText("Alt");
				switchButtonBottom.setSelected(false);
				switchButtonBottom.setText("Norm");
			} else {
				switchButtonTop.setSelected(false);
				switchButtonTop.setText("Alt");
				switchButtonBottom.setSelected(true);
				switchButtonBottom.setText("Norm");
			}
		} else {
			switchButtonTop.setSelected(false);
			switchButtonTop.setText("-");
			switchButtonBottom.setSelected(false);
			switchButtonBottom.setText("-");
		}
		if (tc.trackModel.getBlock(line, blockId).getCrossing() != null){
			if(tc.trackModel.getBlock(line, blockId).getCrossing().getState() == true){
				labelCrossingGraphic.setIcon(new ImageIcon("imgs/crossingOn.gif"));
			} else {
				labelCrossingGraphic.setIcon(new ImageIcon("imgs/crossingOff.png"));
			}
		}
	}
	
	public int getSelectedBlockId(){
		int selectedBlockId = Integer.parseInt((String)comboBlock.getSelectedItem())-1;
		return selectedBlockId;
	}
	
	public void showTrackControllerGUI(TrackControllerGUI thisgui){
		//thisgui.setVisibile(true);
	}
}