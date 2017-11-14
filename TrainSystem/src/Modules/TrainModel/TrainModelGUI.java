package Modules.TrainModel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class TrainModelGUI extends JFrame {

	private JPanel contentPane;
	private Line2D horizontalLine1;
	private Line2D verticalLine1;
	private Line2D verticalLine2;
	private Line2D verticalLine3;
	private Line2D verticalLine4;
	private boolean arrivalStatus = true;
	private int setPowerIn;
	private boolean serviceBrake = false;
	private boolean emerBrake = false;

	private JLabel lblSpecifications = new JLabel("Train Specifications");
	private JLabel lblFailureModeActivation = new JLabel("Failure Mode Activation");
	private JLabel lblOnboardTemperature = new JLabel("Train Operations");
	private JLabel lblSpeedauthority = new JLabel("Speed/Authority");
	private JLabel lblStationControl = new JLabel("Station Control");

	private JLabel lblHeight = new JLabel("Height:");
	private JLabel lblWeight = new JLabel("Weight:");
	private JLabel lblLength = new JLabel("Length:");
	private JLabel lblWidth = new JLabel("Width:");
	private JLabel lblOfCars = new JLabel("# of Cars:");
	private JLabel lblCapacity = new JLabel("Capacity:");
	private JLabel lblSpeedLimit = new JLabel("Speed Limit:");
	private JLabel lblGpsAntenna = new JLabel("GPS Antenna:");
	private JLabel lblMboAntenna = new JLabel("MBO Antenna:");
	private JLabel lblNextStation = new JLabel("Next Station:");
	private JLabel lblTimeOfArrival = new JLabel("Time of Arrival:");
	private JLabel lblStatus = new JLabel("Status:");
	private JLabel passengersEnRoute = new JLabel("Passengers:");
	private JLabel lblCurrentSpeed = new JLabel("Current Speed:");
	private JLabel lblCtcSpeed = new JLabel("CTC Speed:");
	private JLabel lblCtcAuthority = new JLabel("CTC Authority:");
	private JLabel lblPowerInput = new JLabel("Power:");
	private JLabel leftDoorLabel = new JLabel("Left Door:");
	private JLabel rightDoorLabel = new JLabel("Right Door:");
	private JLabel lblLight = new JLabel("Light:");
	private JLabel labelTemperature = new JLabel("Cabin Temperature");
	private JLabel lblEngineFailureMode = new JLabel("Engine Failure");
	private JLabel lblSignalFailure = new JLabel("Signal Failure");
	private JLabel lblBrakeFailure = new JLabel("Brake Failure");
	private JLabel authorityUnits = new JLabel("mi");
	private JLabel setpointSpeedUnits = new JLabel("mph");
	private JLabel ctcSpeedUnitsLabel = new JLabel("mph");
	private JLabel lblServiceBrake = new JLabel("Service Brake:");
	private JLabel lblEmergencyBrake = new JLabel("Emergency Brake:");
	
	public JPanel engineFailPanel = new JPanel();
	public JPanel signalFailPanel = new JPanel();
	public JPanel brakeFailPanel = new JPanel();
	public JMenu mnFile = new JMenu("File");
	public JMenu mnSelectTrain = new JMenu("Select Train");
	public JMenu mnHelp = new JMenu("Help");
	
	public JLabel gpsAntennaStatusLabel = new JLabel();
	public JLabel mboAntennaStatusLabel = new JLabel();
	public JLabel stationVal = new JLabel();
	public JLabel rightDoorStatusLabel = new JLabel();
	public JLabel timeVal = new JLabel();
	public JLabel lblAm = new JLabel();
	public JLabel leftDoorStatusLabel = new JLabel();
	public JLabel lightStatusLabel = new JLabel();
	public JLabel numPassengers = new JLabel();
	public JLabel authorityVal = new JLabel();
	public JLabel serviceLabel = new JLabel();
	public JLabel emergencyLabel = new JLabel();
	
	public JLabel lblLine = new JLabel();
	public JLabel heightVal = new JLabel();
	public JLabel weightVal = new JLabel();
	public JLabel lengthVal = new JLabel();
	public JLabel widthVal = new JLabel();
	public JLabel capacityVal = new JLabel();
	public JLabel arrivalStatusLabel = new JLabel();
	public JLabel currentSpeedLabel = new JLabel();
	
	public JButton btnEmergencyBrake = new JButton("Emergency Brake");
	
	public JLabel numCarsSpinner = new JLabel();
	public JLabel tempLabel = new JLabel();
	
	public JLabel powerVal = new JLabel();
	
	
	// The instance of the TrainModel class we will use to go between the back end elements
	// and the GUI elements of the Train Model
	public static Train train;
	
	/*public TrainModelNewGUI() {
		
	}*/
	
	public void paint(Graphics g) {
		 	Dimension d = this.getSize();
	        super.paint(g);  // fixes the immediate problem.
	        Graphics2D g2 = (Graphics2D) g;
	        horizontalLine1 = new Line2D.Float(45, d.height-290, d.width - 45, d.height-290);
	        verticalLine1 = new Line2D.Float(d.width/3+10, 100 , d.width/3+10, d.height - 325);
	        verticalLine2 = new Line2D.Float((2*d.width)/3-10, 100 , (2*d.width)/3-10, d.height - 325);
	        verticalLine3 = new Line2D.Float(d.width/3+10, 350 , d.width/3+10, d.height - 50);
	        verticalLine4 = new Line2D.Float((2*d.width)/3-10, 350 , (2*d.width)/3-10, d.height - 50);

	        //System.out.println("Height: "+d.height+"\tWidth: "+d.width);
	        // (45, 267, 129, 20)
	        //g2.setColor(Color.DARK_GRAY);
	        g2.draw(horizontalLine1);
	        g2.draw(verticalLine1);
	        g2.draw(verticalLine2);
	        g2.draw(verticalLine3);
	        g2.draw(verticalLine4);
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainModelGUI frame = new TrainModelGUI(train);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//double currSpeed = trainModel.setTrainValues();
		//System.out.println(currSpeed);
	}
	
	/**
	 * Returns the power the user of the train model selects to input
	 * 
	 * Author jpatterson
	 */
	public int returnPowerInput() {
		return setPowerIn;
	}
	
	public boolean serviceBrakeStatus() {
		return serviceBrake;
	}
	
	public boolean emerBrakeStatus() {
		return emerBrake;
	}
	

	/**
	 * Create the frame.
	 */
	public TrainModelGUI() {
		initComponents();
	}
	
	/**
	 * Create the frame using a train model object.
	 */
	public TrainModelGUI(Train newTrain) {
		initComponents();
		this.train = newTrain;
		
	}
		
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 975, 614);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 969, 31);
		contentPane.add(menuBar);
		
		menuBar.add(mnFile);
		
		menuBar.add(mnSelectTrain);
		
		JMenuItem mntmTrainlist = new JMenuItem("TrainList");
		mnSelectTrain.add(mntmTrainlist);
		
		menuBar.add(mnHelp);
		
		
		lblSpecifications.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpecifications.setBounds(60, 60, 196, 20);
		contentPane.add(lblSpecifications);
		
		
		lblFailureModeActivation.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblFailureModeActivation.setBounds(660, 60, 220, 20);
		contentPane.add(lblFailureModeActivation);
		
		
		lblOnboardTemperature.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOnboardTemperature.setBounds(358, 312, 197, 20);
		contentPane.add(lblOnboardTemperature);
		
		lblSpeedauthority.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpeedauthority.setBounds(358, 60, 178, 20);
		contentPane.add(lblSpeedauthority);
		
		lblStationControl.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStationControl.setBounds(60, 312, 154, 20);
		contentPane.add(lblStationControl);
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblHeight.setBounds(60, 100, 69, 20);
		contentPane.add(lblHeight);
		lblWeight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblWeight.setBounds(60, 125, 69, 20);
		contentPane.add(lblWeight);
		lblLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblLength.setBounds(60, 150, 69, 20);
		contentPane.add(lblLength);
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblWidth.setBounds(60, 175, 69, 20);
		contentPane.add(lblWidth);
		
		lblOfCars.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOfCars.setBounds(60, 200, 104, 20);
		contentPane.add(lblOfCars);
		
		numCarsSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numCarsSpinner.setBounds(153, 200, 69, 20);
		contentPane.add(numCarsSpinner);
		
		lblCapacity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCapacity.setBounds(60, 225, 69, 20);
		contentPane.add(lblCapacity);
		
		
		lblLine.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLine.setBounds(358, 348, 129, 20);
		contentPane.add(lblLine);

		lblGpsAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblGpsAntenna.setBounds(358, 458, 129, 20);
		contentPane.add(lblGpsAntenna);
		lblMboAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblMboAntenna.setBounds(358, 482, 129, 20);
		contentPane.add(lblMboAntenna);
		lblNextStation.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblNextStation.setBounds(60, 348, 129, 20);
		contentPane.add(lblNextStation);
		lblTimeOfArrival.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblTimeOfArrival.setBounds(60, 377, 129, 20);
		contentPane.add(lblTimeOfArrival);
		
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStatus.setBounds(60, 404, 69, 20);
		contentPane.add(lblStatus);
		
		arrivalStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		arrivalStatusLabel.setBounds(204, 404, 85, 20);
		contentPane.add(arrivalStatusLabel);
		
		passengersEnRoute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passengersEnRoute.setBounds(60, 429, 104, 20);
		contentPane.add(passengersEnRoute);
		
		lblCurrentSpeed.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCurrentSpeed.setBounds(358, 96, 142, 20);
		contentPane.add(lblCurrentSpeed);
		
		lblCtcSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCtcSpeed.setBounds(358, 125, 129, 20);
		contentPane.add(lblCtcSpeed);
		
		currentSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		currentSpeedLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentSpeedLabel.setBounds(490, 96, 69, 20);
		contentPane.add(currentSpeedLabel);
		
		lblCtcAuthority.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCtcAuthority.setBounds(358, 150, 142, 20);
		contentPane.add(lblCtcAuthority);
		
		lblPowerInput.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPowerInput.setBounds(358, 190, 85, 20);
		contentPane.add(lblPowerInput);
		powerVal.setFont(new Font("Tahoma", Font.BOLD, 16));
		powerVal.setBounds(475, 186, 84, 26);
		contentPane.add(powerVal);
		
		/*btnSendPowerCommand.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  // send the power command through when the button is pressed

			  setPowerIn = Integer.parseInt(powerSpinner.getValue().toString());
		  }
		});*/
		
		leftDoorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		leftDoorLabel.setBounds(358, 377, 129, 20);
		contentPane.add(leftDoorLabel);
		
		rightDoorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorLabel.setBounds(358, 406, 129, 20);
		contentPane.add(rightDoorLabel);
		
		lblLight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLight.setBounds(358, 431, 129, 20);
		contentPane.add(lblLight);
		
		labelTemperature.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelTemperature.setBounds(682, 457, 190, 20);
		contentPane.add(labelTemperature);
		tempLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		tempLabel.setBounds(739, 485, 75, 26);
		//tempLabel.setValue(70);
		contentPane.add(tempLabel);
		btnEmergencyBrake.setBackground(Color.BLACK);
		btnEmergencyBrake.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnEmergencyBrake.setForeground(Color.RED);
		btnEmergencyBrake.setBounds(672, 332, 220, 100);
		//stylizeButton(btnEmergencyBrake);
		contentPane.add(btnEmergencyBrake);
		btnEmergencyBrake.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  emerBrake = true;
		  }
		});
		
		
		lblEngineFailureMode.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEngineFailureMode.setBounds(720, 110, 129, 20);
		contentPane.add(lblEngineFailureMode);
		
		lblSignalFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignalFailure.setBounds(720, 158, 129, 20);
		contentPane.add(lblSignalFailure);
		
		lblBrakeFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBrakeFailure.setBounds(720, 208, 129, 20);
		contentPane.add(lblBrakeFailure);
		
		engineFailPanel.setBounds(660, 100, 34, 31);
		ImageIcon ledImage = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel = new JLabel("", ledImage, JLabel.CENTER);
		ledImageLabel.setBounds(660, 105, 34, 31);
		ledImageLabel.setIcon(ledImage);
		contentPane.add(ledImageLabel);
		contentPane.add(engineFailPanel);
		
		signalFailPanel.setBounds(660, 153, 34, 31);
		ImageIcon ledImage2 = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel2 = new JLabel("", ledImage2, JLabel.CENTER);
		ledImageLabel2.setBounds(660, 155, 34, 31);
		ledImageLabel2.setIcon(ledImage2);
		contentPane.add(ledImageLabel2);
		contentPane.add(signalFailPanel);
		
		brakeFailPanel.setBounds(660, 203, 34, 31);
		ImageIcon ledImageNew = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel3 = new JLabel("", ledImageNew, JLabel.CENTER);
		ledImageLabel3.setBounds(660, 205, 34, 31);
		ledImageLabel3.setIcon(ledImageNew);
		contentPane.add(ledImageLabel3);
		contentPane.add(brakeFailPanel);
		
		JLabel labelFt = new JLabel("ft.");
		labelFt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelFt.setBounds(238, 100, 69, 20);
		contentPane.add(labelFt);
		
		JLabel labelft2 = new JLabel("ft.");
		labelft2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelft2.setBounds(238, 150, 69, 20);
		contentPane.add(labelft2);
		
		JLabel labelft3 = new JLabel("ft.");
		labelft3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelft3.setBounds(238, 175, 69, 20);
		contentPane.add(labelft3);
		
		JLabel lblTons = new JLabel("lbs");
		lblTons.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTons.setBounds(238, 125, 69, 20);
		contentPane.add(lblTons);
		
		JLabel lblKw = new JLabel("kW");
		lblKw.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblKw.setBounds(568, 190, 34, 20);
		contentPane.add(lblKw);
		heightVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		
		heightVal.setBounds(154, 100, 69, 20);
		contentPane.add(heightVal);
		weightVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		
		weightVal.setBounds(154, 125, 69, 20);
		contentPane.add(weightVal);
		lengthVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lengthVal.setBounds(154, 150, 69, 20);
		contentPane.add(lengthVal);
		widthVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		widthVal.setBounds(154, 175, 69, 20);
		contentPane.add(widthVal);
		capacityVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		capacityVal.setBounds(154, 225, 69, 20);
		contentPane.add(capacityVal);
		
		/*speedLimitVal.setHorizontalAlignment(SwingConstants.RIGHT);
		speedLimitVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		speedLimitVal.setBounds(464, 175, 69, 20);
		contentPane.add(speedLimitVal);*/
		
		gpsAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gpsAntennaStatusLabel.setBounds(516, 458, 69, 20);
		contentPane.add(gpsAntennaStatusLabel);
		
		mboAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		mboAntennaStatusLabel.setBounds(516, 482, 69, 20);
		contentPane.add(mboAntennaStatusLabel);
		
		stationVal.setFont(new Font("Tahoma", Font.BOLD, 16));
		stationVal.setBounds(204, 348, 69, 20);
		contentPane.add(stationVal);
		
		timeVal.setFont(new Font("Tahoma", Font.BOLD, 16));
		timeVal.setBounds(204, 377, 52, 20);
		contentPane.add(timeVal);
		
		lblAm.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAm.setBounds(255, 377, 34, 20);
		contentPane.add(lblAm);
		
		leftDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		leftDoorStatusLabel.setBounds(516, 377, 69, 20);
		contentPane.add(leftDoorStatusLabel);
		
		rightDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorStatusLabel.setBounds(516, 406, 69, 20);
		contentPane.add(rightDoorStatusLabel);
		
		
		lightStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lightStatusLabel.setBounds(516, 431, 69, 20);
		contentPane.add(lightStatusLabel);
		
		
		numPassengers.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numPassengers.setBounds(204, 429, 69, 20);
		contentPane.add(numPassengers);
		
		ImageIcon pineapple = new ImageIcon("pineapple_icon.png");
		JLabel pineappleImageLabel = new JLabel("", pineapple, JLabel.CENTER);
		pineappleImageLabel.setBounds(40, 480, 138, 76);
		pineappleImageLabel.setIcon(pineapple);
		contentPane.add(pineappleImageLabel);
		authorityVal.setHorizontalAlignment(SwingConstants.RIGHT);
		
		authorityVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		authorityVal.setBounds(515, 150, 44, 20);
		contentPane.add(authorityVal);
		
		authorityUnits.setFont(new Font("Tahoma", Font.PLAIN, 16));
		authorityUnits.setBounds(568, 150, 34, 20);
		contentPane.add(authorityUnits);
		
		setpointSpeedUnits.setFont(new Font("Tahoma", Font.BOLD, 16));
		setpointSpeedUnits.setBounds(568, 96, 52, 20);
		contentPane.add(setpointSpeedUnits);
		
		ctcSpeedUnitsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ctcSpeedUnitsLabel.setBounds(568, 125, 34, 20);
		contentPane.add(ctcSpeedUnitsLabel);
		
		JLabel label = new JLabel("0");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(515, 125, 44, 20);
		contentPane.add(label);
		
		lblServiceBrake.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblServiceBrake.setBounds(358, 225, 129, 20);
		contentPane.add(lblServiceBrake);
		
		lblEmergencyBrake.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmergencyBrake.setBounds(358, 249, 129, 20);
		contentPane.add(lblEmergencyBrake);
		
		serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		serviceLabel.setBounds(516, 249, 69, 20);
		contentPane.add(serviceLabel);
		
		emergencyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		emergencyLabel.setBounds(516, 225, 69, 20);
		contentPane.add(emergencyLabel);
		
	}
	
	/**
	 * Created by Kevin Le to stylize buttons to all look the same
	 * @param b
	 */
	public void stylizeButton(JButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
	}
	
	/**
	 * Created by Kevin Le to stylize buttons to all look the same
	 * Edited by me to allow for toggle buttons to be stylized
	 * @param b
	 */
	public void stylizeToggleButton(JToggleButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
	}
}