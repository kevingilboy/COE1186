//Michael Kotcher

//package Modules.TrainController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TrainControllerGUI {

	private JFrame frame;
	
	private int mode;
	
	private String trainID;
	
	private JLabel idenLabel;
	private JLabel modeLabel;
	private JLabel speedLabel;
	private JLabel newSpeedLabel;
	private JLabel newSpeedUnitLabel;
	private JLabel setpointLabel;
	private JLabel authLabel;
	private JLabel powerLabel;
	private JLabel sBrakes;
	private JLabel eBrakes;
	private JLabel lights;
	private JLabel rightDoor;
	private JLabel leftDoor;
	private JLabel tempLabel;
	private JLabel tempUnit;
	
	private JTextField newSpeedField;
	private JTextField tempField;
	
	private JRadioButton serviceOn;
	private JRadioButton serviceOff;
	private JRadioButton emergencyOn;
	private JRadioButton emergencyOff;
	private JRadioButton lightsOn;
	private JRadioButton lightsOff;
	private JRadioButton leftOpen;
	private JRadioButton leftClose;
	private JRadioButton rightOpen;
	private JRadioButton rightClose;
	private JRadioButton modeAuto;
	private JRadioButton modeManual;
	
	//private JButton eGUIButton;
	private JButton speedSet;
	private JButton tempSet;
	
	//private TrnController controller;
	
	//private EngineerGUI eGUI;
	
	//private PIController pi;
	
	public static void main(String[] args) {
		TrainControllerGUI window = new TrainControllerGUI("Train 1");
	}
	
	public TrainControllerGUI(/*PIController p, TrnController c, */String s) {
		//pi = p;
		//controller = c;
		//eGUI = new EngineerGUI();
		trainID = s;
		initializeGUI();
	}
	
	private void initializeGUI() {
		frame = new JFrame(trainID);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		speedLabel = new JLabel("Current Speed:	0 mi/hr");
		speedLabel.setBounds(20, 20, 100, 20);
		frame.add(speedLabel);
		
		newSpeedLabel = new JLabel("New Speed:");
		newSpeedLabel.setBounds(20, 80, 90, 20);
		frame.add(newSpeedLabel);
		
		newSpeedField = new JTextField();
		newSpeedField.setBounds(110, 80, 80, 20);
		frame.add(newSpeedField);
		
		newSpeedUnitLabel = new JLabel("mi/hr");
		newSpeedUnitLabel.setBounds(190, 80, 50, 20);
		frame.add(newSpeedUnitLabel);
		
		speedSet = new JButton("Set New Speed");
		speedSet.setBounds(20, 110, 100, 20);
		frame.add(speedSet);
		
		setpointLabel = new JLabel("Setpoint Speed:	0 mi/hr");
		speedLabel.setBounds(20, 110, 100, 20);
		frame.add(setpointLabel);
		
		powerLabel = new JLabel("Power Output:	0 kW");
		powerLabel.setBounds(20, 140, 100, 20);
		frame.add(powerLabel);
		
		authLabel = new JLabel("Current Authority:	0 mi");
		authLabel.setBounds(20, 170, 100, 20);
		frame.add(authLabel);
		
		tempLabel = new JLabel("Temperature:");
		tempLabel.setBounds(20, 200, 100, 20);
		frame.add(tempLabel);
		
		tempField = new JTextField();
		tempField.setBounds(120, 200, 80, 20);
		frame.add(tempField);
		
		tempUnit = new JLabel("F");
		tempUnit.setBounds(200, 200, 30, 20);
		frame.add(tempUnit);
		
		tempSet = new JButton("Set");
		tempSet.setBounds(20, 230, 50, 20);
		frame.add(tempSet);
		
		modeLabel = new JLabel("Driving Mode");
		modeLabel.setBounds(350, 50, 100, 20);
		frame.add(modeLabel);
		
		modeAuto = new JRadioButton("Auto");
		modeAuto.setBounds(350, 70, 75, 20);
		frame.add(modeAuto);
		
		modeManual = new JRadioButton("Manual");
		modeManual.setBounds(425, 70, 100, 20);
		frame.add(modeManual);
		
		sBrakes = new JLabel("Service Brakes");
		sBrakes.setBounds(350, 100, 100, 20);
		frame.add(sBrakes);
		
		serviceOn = new JRadioButton("On");
		serviceOn.setBounds(350, 120, 75, 20);
		frame.add(serviceOn);
		
		serviceOff = new JRadioButton("Off");
		serviceOff.setBounds(425, 120, 75, 20);
		frame.add(serviceOff);
		
		eBrakes = new JLabel("Emergency Brakes");
		eBrakes.setBounds(350, 150, 150, 20);
		frame.add(eBrakes);
		
		emergencyOn = new JRadioButton("On");
		emergencyOn.setBounds(350, 170, 75, 20);
		frame.add(emergencyOn);
		
		emergencyOff = new JRadioButton("Off");
		emergencyOff.setBounds(425, 170, 75, 20);
		frame.add(emergencyOff);
		
		rightDoor = new JLabel("Right Doors");
		rightDoor.setBounds(350, 200, 100, 20);
		frame.add(rightDoor);
		
		rightOpen = new JRadioButton("Open");
		rightOpen.setBounds(350, 220, 75, 20);
		frame.add(rightOpen);
		
		rightClose = new JRadioButton("Close");
		rightClose.setBounds(425, 220, 75, 20);
		frame.add(rightClose);
		
		leftDoor = new JLabel("Left Doors");
		leftDoor.setBounds(350, 250, 100, 20);
		frame.add(leftDoor);
		
		leftOpen = new JRadioButton("Open");
		leftOpen.setBounds(350, 270, 75, 20);
		frame.add(leftOpen);
		
		leftClose = new JRadioButton("Close");
		leftClose.setBounds(425, 270, 75, 20);
		frame.add(leftClose);
		
		lights = new JLabel("Lights");
		lights.setBounds(350, 300, 100, 20);
		frame.add(lights);
		
		lightsOn = new JRadioButton("On");
		lightsOn.setBounds(350, 320, 75, 20);
		frame.add(lightsOn);
		
		lightsOff = new JRadioButton("Off");
		lightsOff.setBounds(425, 320, 75, 20);
		frame.add(lightsOff);
		
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
}