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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager; 
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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

@SuppressWarnings("unchecked")
public class TrackControllerGUI extends JFrame{

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

	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/


	//Parent Class
	public TrackController tc;
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
		setLookAndFeel();
		this.tc = tc;
		this.line = tc.associatedLine;
		this.blocks = Arrays.copyOf(tc.associatedBlocks, tc.associatedBlocks.length);
		for(int i=0; i<blocks.length; i++){
			blocks[i] = Integer.toString((Integer.parseInt(blocks[i])+1));//offset for displaying
		}
		drawTrackControllerGui(tc, line, blocks, controllerName);
		//System.out.println(this);
		//this.setVisible(true);
	}
	
	/**
	 * Create the frame.
	 */
	public void drawTrackControllerGui(TrackController tc, String line, String[] blocks, String name) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Track Controller "+name);
		setBounds(100, 100, 870, 490);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(26, 29, 35));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 0, 888, 507);
		panel.setBackground(new Color(26, 29, 35));
		getContentPane().add(panel);
		panel.setLayout(null);
		
		//Sub-Panels
		JPanel trackSelectorPanel = new JPanel();
		trackSelectorPanel.setBorder(null);
		trackSelectorPanel.setBounds(346, 55, 502, 67);
		trackSelectorPanel.setBackground(new Color(26, 29, 35));
		panel.add(trackSelectorPanel);
		trackSelectorPanel.setLayout(null);
		
		JPanel trackInfoPanel = new JPanel();
		trackInfoPanel.setBorder(null);
		trackInfoPanel.setBounds(346, 119, 502, 329);
		trackInfoPanel.setBackground(new Color(26, 29, 35));
		panel.add(trackInfoPanel);
		trackInfoPanel.setLayout(null);
		
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(10, 32, 487, 59);
		updatePanel.setBackground(new Color(26, 29, 35));
		trackInfoPanel.add(updatePanel);
		updatePanel.setLayout(null);
		
		JPanel switchPanel = new JPanel();
		switchPanel.setBorder(null);
		switchPanel.setBounds(236, 120, 240, 66);
		switchPanel.setBackground(new Color(26, 29, 35));
		trackInfoPanel.add(switchPanel);
		switchPanel.setLayout(null);
		
		JPanel lightsPanel = new JPanel();
		lightsPanel.setLayout(null);
		lightsPanel.setBorder(null);
		lightsPanel.setBackground(new Color(26, 29, 35));
		lightsPanel.setBounds(10, 119, 240, 123);
		trackInfoPanel.add(lightsPanel);
		
		//Separators
		JSeparator separator1 = new JSeparator();
		separator1.setForeground(new Color(36, 39, 45));
		separator1.setBackground(new Color(36, 39, 45));
		separator1.setBounds(0, 7, 502, 12);
		trackSelectorPanel.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setForeground(new Color(36, 39, 45));
		separator2.setBackground(new Color(36, 39, 45));
		separator2.setBounds(0, 56, 502, 12);
		trackSelectorPanel.add(separator2);
		
		JSeparator separator3 = new JSeparator();
		separator3.setForeground(new Color(36, 39, 45));
		separator3.setBackground(new Color(36, 39, 45));
		separator3.setBounds(0, 96, 502, 21);
		trackInfoPanel.add(separator3);
		
		//GUI Title
		JLabel labelTrackControllerInterface = new JLabel("<html><b>TRACK CONTROLLER "+name+"</b><html>");
		labelTrackControllerInterface.setBounds(346, 0, 502, 54);
		panel.add(labelTrackControllerInterface);
		stylizeHeadingLabel(labelTrackControllerInterface);
		labelTrackControllerInterface.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Track Image
		JLabel labelTrackImg = new JLabel();
		labelTrackImg.setBackground(Color.WHITE);
		labelTrackImg.setBounds(0, 0, 334, 448);
		panel.add(labelTrackImg);
		labelTrackImg.setIcon(new ImageIcon("Modules/TrackController/imgs/track.png"));
		
		//Block Selectors
		JLabel labelBlock = new JLabel("BLOCK");
		stylizeInfoLabel(labelBlock);
		labelBlock.setBounds(272, 23, 70, 16);
		trackSelectorPanel.add(labelBlock);
		
		/*JLabel labelSection = new JLabel("Section");
		labelSection.setBounds(164, 23, 46, 16);
		trackSelectorPanel.add(labelSection);
		*/
		JLabel labelLine = new JLabel("LINE");
		stylizeInfoLabel(labelLine);
		labelLine.setBounds(68, 23, 50, 16);
		trackSelectorPanel.add(labelLine);
		
		comboBlock.setBounds(336, 19, 104, 27);
		stylizeComboBox(comboBlock);
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
		stylizeTextField(textLine);
		textLine.setBounds(118, 19, 104, 27);
		trackSelectorPanel.add(textLine);
		textLine.setHorizontalAlignment(SwingConstants.CENTER);
		textLine.setText(line.toUpperCase());
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
		JLabel labelOccupancy = new JLabel("OCCUPANCY");
		stylizeInfoLabel(labelOccupancy);
		labelOccupancy.setBounds(99, 28, 134, 28);
		updatePanel.add(labelOccupancy);
		labelOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textOccupancy = new JTextField();
		stylizeTextField(textOccupancy);
		textOccupancy.setBounds(241, 28, 134, 28);
		updatePanel.add(textOccupancy);
		textOccupancy.setHorizontalAlignment(SwingConstants.CENTER);
		textOccupancy.setText("-");
		textOccupancy.setEditable(false);
		textOccupancy.setColumns(10);
		
		//Block Switch
		JLabel labelSwitchState = new JLabel("<html><CENTER>SWITCH<br>STATE</CENTER></html>");
		stylizeInfoLabel(labelSwitchState);
		labelSwitchState.setBounds(0, 0, 88, 70);
		switchPanel.add(labelSwitchState);
		labelSwitchState.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel labelSwitchGraphic = new JLabel();
		labelSwitchGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/switch_none.png"));
		labelSwitchGraphic.setBounds(100, 17, 55, 33);
		switchPanel.add(labelSwitchGraphic);
		
		switchButtonTop.setBounds(164, 6, 80, 28);
		switchButtonTop.setForeground(Color.WHITE);
		switchButtonTop.setBackground(new Color(20, 20, 20));
		switchPanel.add(switchButtonTop);
		switchButtonTop.setHorizontalAlignment(SwingConstants.LEFT);
		
		switchButtonBottom.setBounds(164, 35, 80, 28);
		switchButtonBottom.setForeground(Color.WHITE);
		switchButtonBottom.setBackground(new Color(20, 20, 20));
		switchPanel.add(switchButtonBottom);
		switchButtonBottom.setHorizontalAlignment(SwingConstants.LEFT);
		
		//Block Lights
		JLabel labelLights = new JLabel("LIGHTS");
		stylizeInfoLabel(labelLights);
		labelLights.setHorizontalAlignment(SwingConstants.TRAILING);
		labelLights.setBounds(0, 0, 88, 57);
		lightsPanel.add(labelLights);
		
		labelLightGraphic.setBounds(149, 0, 39, 57);
		lightsPanel.add(labelLightGraphic);
		labelLightGraphic.setIcon(new ImageIcon("Modules/TrackController/imgs/lightsOff.png"));
		
		//Block Crossing
		JLabel labelCrossing = new JLabel("CROSSING");
		stylizeInfoLabel(labelCrossing);
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
		JButton buttonImportPlc = new JButton("IMPORT PLC");
		stylizeButton(buttonImportPlc);
		buttonImportPlc.setBounds(64, 270, 117, 29);
		buttonImportPlc.addActionListener(new UploadPLC());
		trackInfoPanel.add(buttonImportPlc);
		
		//Pineapple logo
		JLabel labelPineapple = new JLabel("");
		labelPineapple.setIcon(new ImageIcon("Modules/TrackModel/Images/HSS_TrainSim_Logo.png"));
		labelPineapple.setBounds(364, 237, 138, 76);
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
			FileNameExtensionFilter filter = new FileNameExtensionFilter("plc", "PLC");
			c.setFileFilter(filter);
			int rVal = c.showOpenDialog(null);
			if(rVal == JFileChooser.APPROVE_OPTION) {
				String plcPath = c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName();
				System.out.println("path = " + plcPath);
				boolean parserSuccess = tc.tcplc.parsePLC(plcPath);
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
			textOccupancy.setText("TRUE");
		} else {
			textOccupancy.setText("FALSE");
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
				switchButtonTop.setText("ALT");
				switchButtonBottom.setSelected(false);
				switchButtonBottom.setText("NORM");
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
	
	public void showTrackControllerGUI(){
		this.setVisible(true);
	}
}