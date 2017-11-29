/**
 * Author: Jennifer Patterson
 * Course: CoE 1186 - Software Engineering
 * Group: HashSlinging Slashers
 * Date Created: 10/3/17
 * Date Modified: 11/26/17
 */

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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

/**
 * Class for the individual GUIs of each existing train model. Every train model will have an existing
 * GUI upon dispatch, but on simulation start up, the first train in the train list will appear and every
 * GUI after that will have to be manually selected from the Select Train dropdown menu.
 * 
 * @author Jennifer
 *
 */
public class TrainModelGUI extends JFrame {

	private JPanel contentPane;
	private Line2D horizontalLine1;
	private Line2D verticalLine1;
	private Line2D verticalLine2;
	private Line2D verticalLine3;
	private Line2D verticalLine4;
	private boolean arrivalStatus = true;
	//private int setPowerIn;
	private boolean serviceBrake = false;
	private boolean emerBrake = false;
	private int numCars = 1;

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
	//private JLabel lblSpeedLimit = new JLabel("Speed Limit:");
	private JLabel lblGpsAntenna = new JLabel("GPS Antenna:");
	private JLabel lblMboAntenna = new JLabel("MBO Antenna:");
	private JLabel lblNextStation = new JLabel("Next Station:");
	private JLabel lblTimeOfArrival = new JLabel("Current Time:");
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
	private boolean engineFail;
	private boolean sigFail;
	private boolean brakeFail;
	public JMenu mnFile = new JMenu("File");
	public JMenu mnSelectTrain = new JMenu("Select Train");
	public JMenuItem menuTrainlist = new JMenuItem(new AbstractAction("") {
		public void actionPerformed(ActionEvent e) {
	        // Button pressed logic goes here
			
	    }
	});
	
	public JLabel gpsAntennaStatusLabel = new JLabel();
	public JLabel mboAntennaStatusLabel = new JLabel();
	public JLabel stationVal = new JLabel();
	public JLabel rightDoorStatusLabel = new JLabel();
	public JLabel timeVal = new JLabel();
	//public JLabel lblAm = new JLabel();
	public JLabel leftDoorStatusLabel = new JLabel();
	public JLabel lightStatusLabel = new JLabel();
	public JLabel numPassengers = new JLabel();
	public JLabel authorityVal = new JLabel();
	public JLabel serviceLabel = new JLabel();
	public JLabel emergencyLabel = new JLabel();
	public JLabel ctcSpeedLabel = new JLabel();
	
	public JLabel lblLine = new JLabel();
	public JLabel heightVal = new JLabel();
	public JLabel weightVal = new JLabel();
	public JLabel lengthVal = new JLabel();
	public JLabel widthVal = new JLabel();
	public JLabel capacityVal = new JLabel();
	private JSpinner numCarsSpinner = new JSpinner();
	private JButton setCars = new JButton("Set");
	private JLabel lblCrew = new JLabel("Crew:");
	JLabel crewCountLabel = new JLabel();
	public JLabel arrivalStatusLabel = new JLabel();
	public JLabel currentSpeedLabel = new JLabel();
	
	public JButton btnEmergencyBrake = new JButton("Emergency Brake");
	public JLabel tempLabel = new JLabel();
	
	public JLabel powerVal = new JLabel();
	
	
	// The instance of the TrainModel class we will use to go between the back end elements
	// and the GUI elements of the Train Model
	public static Train train;
	//private final JMenuItem mntmSimulateBrakeFailure = new JMenuItem("Simulate Brake Failure");
	//private final JMenuItem mntmSimulateEngineFailure = new JMenuItem("Simulate Engine Failure");
	//private final JMenuItem mntmSimulateSignalFailure = new JMenuItem("Simulate Signal Failure");
	private final JMenuItem mntmExit = new JMenuItem("Exit All");
	private Image ledImage = new ImageIcon(this.getClass().getResource("greyStatusIcon.png")).getImage();
	private Image ledImageRed = new ImageIcon(this.getClass().getResource("redStatusIcon.png")).getImage();
	private final JLabel ledImageLabel = new JLabel();
	private final JLabel ledImageLabel2 = new JLabel();
	private final JLabel ledImageLabel3 = new JLabel();
	//private final JMenuItem mntmEndFailures = new JMenuItem("End Failure(s)");
	JButton btnCauseFailure = new JButton("Cause Failure");
	JCheckBox engineFailCheckBox = new JCheckBox("");
	JCheckBox signalFailCheckBox = new JCheckBox("");
	JCheckBox brakeFailCheckBox = new JCheckBox("");
	private final JButton btnEndFailure = new JButton("End Failure");
	
	/*public TrainModelNewGUI() {
		
	}*/
	
	public void paint(Graphics g) {
		 	Dimension d = this.getSize();
	        super.paint(g);  // fixes the immediate problem.
	        Graphics2D g2 = (Graphics2D) g;
	        horizontalLine1 = new Line2D.Float(45, d.height-280, d.width - 45, d.height-280);
	        verticalLine1 = new Line2D.Float(d.width/3+10, 100 , d.width/3+10, d.height - 325);
	        verticalLine2 = new Line2D.Float((2*d.width)/3-10, 100 , (2*d.width)/3-10, d.height - 325);
	        verticalLine3 = new Line2D.Float(d.width/3+10, 370 , d.width/3+10, d.height - 50);
	        verticalLine4 = new Line2D.Float((2*d.width)/3-10, 370 , (2*d.width)/3-10, d.height - 50);

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
	
	public boolean serviceBrakeStatus() {
		return serviceBrake;
	}
	
	public boolean emerBrakeStatus() {
		return emerBrake;
	}
	
	public boolean engineFailStatus() {
		return engineFail;
	}
	
	public boolean signalFailStatus() {
		return sigFail;
	}
	
	public boolean brakeFailStatus() {
		return brakeFail;
	}
	
	public int numCars() {
		return numCars;
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 975, 630);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 969, 31);
		contentPane.add(menuBar);
		
		menuBar.add(mnFile);
	
		/*mnFile.add(mntmSimulateEngineFailure);
		mntmSimulateEngineFailure.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setEnabled(true);
				ledImageLabel.setIcon(new ImageIcon(ledImageRed));
				ledImageLabel.setBounds(692, 104, 34, 31);
				repaint();
			}
		});
		
		mnFile.add(mntmSimulateSignalFailure);
		mntmSimulateSignalFailure.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  setEnabled(true);
			  ledImageLabel2.setIcon(new ImageIcon(ledImageRed));
			  ledImageLabel2.setBounds(692, 153, 34, 31);
			  repaint();
		  }
		});
		
		mnFile.add(mntmSimulateBrakeFailure);
		mntmSimulateBrakeFailure.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  setEnabled(true);
			  ledImageLabel3.setIcon(new ImageIcon(ledImageRed));
			  ledImageLabel3.setBounds(692, 203, 34, 31);
			  repaint();
		  }
		});*/
		
		//mnFile.add(mntmEndFailures);
		btnEndFailure.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  setEnabled(true);
			  ledImageLabel.setIcon(new ImageIcon(ledImage));
			  engineFail = false;
			  train.engineFailureStatus();
			  engineFailCheckBox.setSelected(engineFail);
			  ledImageLabel2.setIcon(new ImageIcon(ledImage));
			  sigFail = false;
			  train.signalFailureStatus();
			  signalFailCheckBox.setSelected(sigFail);
			  ledImageLabel3.setIcon(new ImageIcon(ledImage));
			  brakeFail = false;
			  train.brakeFailureStatus();
			  brakeFailCheckBox.setSelected(brakeFail);
			  repaint();
		  }
		});
		
		
		btnCauseFailure.setBounds(666, 245, 129, 29);
		contentPane.add(btnCauseFailure);
		btnCauseFailure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				setEnabled(true);
				if(engineFail) { 
					train.engineFailureStatus();
					ledImageLabel.setIcon(new ImageIcon(ledImageRed)); 
				}
				
				if(sigFail)	{ 
					train.signalFailureStatus();
					ledImageLabel2.setIcon(new ImageIcon(ledImageRed)); 
				}
				
				if(brakeFail) { 
					train.brakeFailureStatus();
					ledImageLabel3.setIcon(new ImageIcon(ledImageRed)); 
				}
				repaint();
			}
		});
		
		engineFailCheckBox.setBounds(873, 96, 52, 29);
		contentPane.add(engineFailCheckBox);
		engineFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engineFail = true;
			}
		});
		
		signalFailCheckBox.setBounds(873, 146, 52, 29);
		contentPane.add(signalFailCheckBox);
		signalFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigFail = true;
			}
		});
		
		brakeFailCheckBox.setBounds(873, 196, 52, 29);
		contentPane.add(brakeFailCheckBox);
		brakeFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brakeFail = true;
			}
		});
		
		// Not working currently...not sure what's going on, but will just remove if needed
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  setEnabled(true);
			  train.setExitAllGuis(true);
		  }
		});
		
		menuBar.add(mnSelectTrain);
		
		mnSelectTrain.add(menuTrainlist);
		
		
		lblSpecifications.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpecifications.setBounds(60, 60, 196, 20);
		contentPane.add(lblSpecifications);
		
		
		lblFailureModeActivation.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblFailureModeActivation.setBounds(682, 60, 220, 20);
		contentPane.add(lblFailureModeActivation);
		
		
		lblOnboardTemperature.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOnboardTemperature.setBounds(358, 334, 197, 20);
		contentPane.add(lblOnboardTemperature);
		
		lblSpeedauthority.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpeedauthority.setBounds(358, 60, 178, 20);
		contentPane.add(lblSpeedauthority);
		
		lblStationControl.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStationControl.setBounds(60, 334, 154, 20);
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
		lblOfCars.setBounds(60, 203, 104, 20);
		contentPane.add(lblOfCars);
		
		lblCapacity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCapacity.setBounds(60, 236, 69, 20);
		contentPane.add(lblCapacity);
		
		
		lblLine.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLine.setBounds(358, 370, 129, 20);
		contentPane.add(lblLine);

		lblGpsAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblGpsAntenna.setBounds(358, 480, 129, 20);
		contentPane.add(lblGpsAntenna);
		lblMboAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblMboAntenna.setBounds(358, 504, 129, 20);
		contentPane.add(lblMboAntenna);
		lblNextStation.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblNextStation.setBounds(60, 370, 129, 20);
		contentPane.add(lblNextStation);
		lblTimeOfArrival.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblTimeOfArrival.setBounds(60, 399, 129, 20);
		contentPane.add(lblTimeOfArrival);
		
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStatus.setBounds(60, 426, 69, 20);
		contentPane.add(lblStatus);
		
		arrivalStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		arrivalStatusLabel.setBounds(204, 428, 121, 20);
		contentPane.add(arrivalStatusLabel);
		
		passengersEnRoute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passengersEnRoute.setBounds(60, 451, 104, 20);
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
		powerVal.setHorizontalAlignment(SwingConstants.RIGHT);
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
		leftDoorLabel.setBounds(358, 399, 129, 20);
		contentPane.add(leftDoorLabel);
		
		rightDoorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorLabel.setBounds(358, 428, 129, 20);
		contentPane.add(rightDoorLabel);
		
		lblLight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLight.setBounds(358, 453, 129, 20);
		contentPane.add(lblLight);
		labelTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelTemperature.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelTemperature.setBounds(692, 470, 190, 20);
		contentPane.add(labelTemperature);
		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		tempLabel.setBounds(749, 498, 75, 26);
		//tempLabel.setValue(70);
		contentPane.add(tempLabel);
		
		btnEmergencyBrake.setBackground(Color.BLACK);
		btnEmergencyBrake.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnEmergencyBrake.setForeground(Color.RED);
		btnEmergencyBrake.setBounds(682, 348, 220, 100);
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
		lblEngineFailureMode.setBounds(742, 102, 129, 20);
		contentPane.add(lblEngineFailureMode);
		
		lblSignalFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignalFailure.setBounds(742, 150, 129, 20);
		contentPane.add(lblSignalFailure);
		
		lblBrakeFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBrakeFailure.setBounds(742, 200, 129, 20);
		contentPane.add(lblBrakeFailure);

		ledImageLabel.setBounds(692, 96, 34, 31);
		ledImageLabel2.setBounds(692, 145, 34, 31);
		ledImageLabel3.setBounds(692, 195, 34, 31);
		
		ledImageLabel.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel);
		
		ledImageLabel2.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel2);

		ledImageLabel3.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel3);
		//contentPane.add(brakeFailPanel);
		
		Image pineapple = new ImageIcon(this.getClass().getResource("pineapple_icon.png")).getImage();
		JLabel pineappleImageLabel = new JLabel();
		pineappleImageLabel.setBounds(25, 498, 138, 76);
		pineappleImageLabel.setIcon(new ImageIcon(pineapple));
		contentPane.add(pineappleImageLabel);
		
		authorityVal.setHorizontalAlignment(SwingConstants.RIGHT);
		
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
		
		capacityVal.setBounds(154, 236, 69, 20);
		contentPane.add(capacityVal);
		
		gpsAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gpsAntennaStatusLabel.setBounds(515, 480, 69, 20);
		contentPane.add(gpsAntennaStatusLabel);
		
		mboAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		mboAntennaStatusLabel.setBounds(515, 504, 69, 20);
		contentPane.add(mboAntennaStatusLabel);
		
		stationVal.setFont(new Font("Tahoma", Font.BOLD, 16));
		stationVal.setBounds(204, 370, 69, 20);
		contentPane.add(stationVal);
		
		timeVal.setFont(new Font("Tahoma", Font.BOLD, 16));
		timeVal.setBounds(204, 399, 121, 20);
		contentPane.add(timeVal);
		
		leftDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		leftDoorStatusLabel.setBounds(515, 399, 69, 20);
		contentPane.add(leftDoorStatusLabel);
		
		rightDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorStatusLabel.setBounds(515, 428, 69, 20);
		contentPane.add(rightDoorStatusLabel);
		
		
		lightStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lightStatusLabel.setBounds(515, 453, 69, 20);
		contentPane.add(lightStatusLabel);
		
		
		numPassengers.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numPassengers.setBounds(204, 451, 103, 20);
		contentPane.add(numPassengers);
		
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
		
		ctcSpeedLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ctcSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ctcSpeedLabel.setBounds(515, 125, 44, 20);
		contentPane.add(ctcSpeedLabel);
		
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
		btnEndFailure.setBounds(806, 245, 115, 29);
		
		contentPane.add(btnEndFailure);
		
		numCarsSpinner.setBounds(143, 204, 59, 20);
		contentPane.add(numCarsSpinner);
		
		setCars.setBounds(204, 204, 69, 20);
		contentPane.add(setCars);
		setCars.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  // send the power command through when the button is pressed

			  numCars = Integer.parseInt(numCarsSpinner.getValue().toString());
		  }
		});
		
		lblCrew.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCrew.setBounds(60, 261, 69, 20);
		contentPane.add(lblCrew);
		
		crewCountLabel.setBounds(154, 261, 69, 20);
		contentPane.add(crewCountLabel);
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
	
	public void addTraintoGUIList(Train train) {
		/*public JMenuItem menuTrainlist = new JMenuItem(new AbstractAction("") {
			public void actionPerformed(ActionEvent e) {
		        // Button pressed logic goes here
				
		    }
		});*/
		mnSelectTrain.add(new JMenuItem(new AbstractAction(train.getTrainID()) {
			public void actionPerformed(ActionEvent e) {
		        // Button pressed logic goes here
				setEnabled(true);
				train.showTrainGUI();
		    }
		}));
	}
}