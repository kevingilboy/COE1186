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
	private JLabel lblTimeOfArrival = new JLabel("Time:");
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
	public JLabel emergencyLabel = new JLabel();
	public JLabel serviceLabel = new JLabel();
	public JLabel ctcSpeedLabel = new JLabel();
	
	public JLabel lblLine = new JLabel();
	public JLabel heightVal = new JLabel();
	public JLabel weightVal = new JLabel();
	public JLabel lengthVal = new JLabel();
	public JLabel widthVal = new JLabel();
	public JLabel capacityVal = new JLabel();
	public JLabel numCarsSpinner = new JLabel();
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
	JButton btnCauseFailure = new JButton("Cause");
	JCheckBox engineFailCheckBox = new JCheckBox("");
	JCheckBox signalFailCheckBox = new JCheckBox("");
	JCheckBox brakeFailCheckBox = new JCheckBox("");
	private final JButton btnEndFailure = new JButton("End");
	
	/*public TrainModelNewGUI() {
		
	}*/
	
	public void paint(Graphics g) {
		 	Dimension d = this.getSize();
	        super.paint(g);  // fixes the immediate problem.
	        Graphics2D g2 = (Graphics2D) g;
	        horizontalLine1 = new Line2D.Float(45, d.height-290, d.width - 45, d.height-290);
	        verticalLine1 = new Line2D.Float(d.width/3 - 10, 100 , d.width/3 - 10, d.height - 325);
	        verticalLine2 = new Line2D.Float((2*d.width)/3-10, 100 , (2*d.width)/3-10, d.height - 325);
	        verticalLine3 = new Line2D.Float(d.width/3 - 10, 390 , d.width/3 - 10, d.height - 45);
	        verticalLine4 = new Line2D.Float((2*d.width)/3-10, 390 , (2*d.width)/3-10, d.height - 45);

	        //g2.setColor(Color.DARK_GRAY);
	        g2.draw(horizontalLine1);
	        g2.draw(verticalLine1);
	        g2.draw(verticalLine2);
	        g2.draw(verticalLine3);
	        g2.draw(verticalLine4);
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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/**try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			
		}
		catch (Throwable e) {
			e.printStackTrace();
		}*/
		
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
		setLookAndFeel();
		initComponents();
	}
	
	/**
	 * Create the frame using a train model object.
	 */
	public TrainModelGUI(Train newTrain) {
		setLookAndFeel();
		initComponents();
		this.train = newTrain;
		
	}
		
	private void initComponents() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(150, 150, 1050, 655);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1044, 31);
		contentPane.add(menuBar);
		
		menuBar.add(mnFile);
		btnEndFailure.setBackground(Color.WHITE);
		btnEndFailure.setForeground(Color.BLACK);
		
		btnEndFailure.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		//mnFile.add(mntmEndFailures);
		btnEndFailure.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  setEnabled(true);
			  ledImageLabel.setIcon(new ImageIcon(ledImage));
			  engineFail = false;
			  //train.engineFailureStatus();
			  engineFailCheckBox.setSelected(engineFail);
			  ledImageLabel2.setIcon(new ImageIcon(ledImage));
			  sigFail = false;
			  //train.signalFailureStatus();
			  signalFailCheckBox.setSelected(sigFail);
			  ledImageLabel3.setIcon(new ImageIcon(ledImage));
			  brakeFail = false;
			  //train.brakeFailureStatus();
			  brakeFailCheckBox.setSelected(brakeFail);
			  repaint();
		  }
		});
		btnCauseFailure.setBackground(Color.WHITE);
		btnCauseFailure.setForeground(Color.BLACK);
		
		btnCauseFailure.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnCauseFailure.setBounds(723, 268, 94, 29);
		contentPane.add(btnCauseFailure);
		btnCauseFailure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				setEnabled(true);
				if(engineFail) { 
					//train.engineFailureStatus();
					ledImageLabel.setIcon(new ImageIcon(ledImageRed)); 
				}
				
				if(sigFail)	{ 
					//train.signalFailureStatus();
					ledImageLabel2.setIcon(new ImageIcon(ledImageRed)); 
				}
				
				if(brakeFail) { 
					//train.brakeFailureStatus();
					ledImageLabel3.setIcon(new ImageIcon(ledImageRed)); 
				}
				repaint();
			}
		});
		engineFailCheckBox.setBackground(Color.LIGHT_GRAY);
		
		engineFailCheckBox.setBounds(910, 99, 52, 29);
		contentPane.add(engineFailCheckBox);
		engineFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engineFail = true;
			}
		});
		signalFailCheckBox.setBackground(Color.LIGHT_GRAY);
		
		signalFailCheckBox.setBounds(910, 155, 52, 29);
		contentPane.add(signalFailCheckBox);
		signalFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigFail = true;
			}
		});
		brakeFailCheckBox.setBackground(Color.LIGHT_GRAY);
		
		brakeFailCheckBox.setBounds(910, 214, 52, 29);
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
		
		
		lblSpecifications.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSpecifications.setBounds(60, 60, 243, 20);
		contentPane.add(lblSpecifications);
		
		
		lblFailureModeActivation.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblFailureModeActivation.setBounds(723, 60, 243, 20);
		contentPane.add(lblFailureModeActivation);
		
		
		lblOnboardTemperature.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblOnboardTemperature.setBounds(371, 355, 201, 20);
		contentPane.add(lblOnboardTemperature);
		
		lblSpeedauthority.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblSpeedauthority.setBounds(371, 60, 585, 20);
		contentPane.add(lblSpeedauthority);
		
		lblStationControl.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblStationControl.setBounds(60, 355, 190, 20);
		contentPane.add(lblStationControl);
		lblHeight.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblHeight.setBounds(60, 100, 69, 20);
		contentPane.add(lblHeight);
		lblWeight.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblWeight.setBounds(60, 125, 69, 20);
		contentPane.add(lblWeight);
		lblLength.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblLength.setBounds(60, 150, 69, 20);
		contentPane.add(lblLength);
		lblWidth.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblWidth.setBounds(60, 175, 69, 20);
		contentPane.add(lblWidth);
		
		lblOfCars.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblOfCars.setBounds(60, 211, 104, 20);
		contentPane.add(lblOfCars);
		
		lblCapacity.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblCapacity.setBounds(60, 245, 104, 20);
		contentPane.add(lblCapacity);
		
		
		lblLine.setFont(new Font("Dialog", Font.BOLD, 18));
		lblLine.setBounds(371, 391, 129, 20);
		contentPane.add(lblLine);

		lblGpsAntenna.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblGpsAntenna.setBounds(371, 501, 129, 20);
		contentPane.add(lblGpsAntenna);
		lblMboAntenna.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblMboAntenna.setBounds(371, 525, 129, 20);
		contentPane.add(lblMboAntenna);
		lblNextStation.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblNextStation.setBounds(60, 391, 129, 20);
		contentPane.add(lblNextStation);
		lblTimeOfArrival.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lblTimeOfArrival.setBounds(60, 420, 85, 20);
		contentPane.add(lblTimeOfArrival);
		
		lblStatus.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblStatus.setBounds(60, 447, 69, 20);
		contentPane.add(lblStatus);
		arrivalStatusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		
		arrivalStatusLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		arrivalStatusLabel.setBounds(144, 447, 159, 20);
		contentPane.add(arrivalStatusLabel);
		
		passengersEnRoute.setFont(new Font("Dialog", Font.PLAIN, 18));
		passengersEnRoute.setBounds(60, 472, 104, 20);
		contentPane.add(passengersEnRoute);
		
		lblCurrentSpeed.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblCurrentSpeed.setBounds(371, 105, 142, 20);
		contentPane.add(lblCurrentSpeed);
		
		lblCtcSpeed.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblCtcSpeed.setBounds(371, 139, 129, 20);
		contentPane.add(lblCtcSpeed);
		
		currentSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		currentSpeedLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		currentSpeedLabel.setBounds(514, 108, 69, 20);
		contentPane.add(currentSpeedLabel);
		
		lblCtcAuthority.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblCtcAuthority.setBounds(371, 175, 142, 20);
		contentPane.add(lblCtcAuthority);
		
		lblPowerInput.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblPowerInput.setBounds(371, 209, 85, 20);
		contentPane.add(lblPowerInput);
		powerVal.setHorizontalAlignment(SwingConstants.RIGHT);
		powerVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		powerVal.setBounds(503, 205, 80, 26);
		contentPane.add(powerVal);
		
		/*btnSendPowerCommand.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  // send the power command through when the button is pressed

			  setPowerIn = Integer.parseInt(powerSpinner.getValue().toString());
		  }
		});*/
		
		leftDoorLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		leftDoorLabel.setBounds(371, 420, 129, 20);
		contentPane.add(leftDoorLabel);
		
		rightDoorLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		rightDoorLabel.setBounds(371, 449, 129, 20);
		contentPane.add(rightDoorLabel);
		
		lblLight.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblLight.setBounds(371, 474, 129, 20);
		contentPane.add(lblLight);
		labelTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelTemperature.setFont(new Font("Tahoma", Font.BOLD, 20));
		labelTemperature.setBounds(745, 502, 233, 20);
		contentPane.add(labelTemperature);
		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		tempLabel.setBounds(821, 540, 75, 26);
		//tempLabel.setValue(70);
		contentPane.add(tempLabel);
		
		btnEmergencyBrake.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnEmergencyBrake.setBackground(Color.WHITE);
		btnEmergencyBrake.setForeground(Color.BLACK);
		btnEmergencyBrake.setBounds(745, 371, 220, 100);
		//stylizeButton(btnEmergencyBrake);
		contentPane.add(btnEmergencyBrake);
		btnEmergencyBrake.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  emerBrake = true;
			  train.setEBrake(true);
		  }
		});
		
		
		lblEngineFailureMode.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblEngineFailureMode.setBounds(765, 103, 129, 20);
		contentPane.add(lblEngineFailureMode);
		
		lblSignalFailure.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblSignalFailure.setBounds(765, 160, 129, 20);
		contentPane.add(lblSignalFailure);
		
		lblBrakeFailure.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblBrakeFailure.setBounds(765, 216, 129, 20);
		contentPane.add(lblBrakeFailure);

		ledImageLabel.setBounds(729, 99, 34, 31);
		ledImageLabel3.setBounds(729, 213, 34, 31);
		ledImageLabel.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel);
		
		ledImageLabel2.setBounds(729, 155, 34, 31);
		ledImageLabel2.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel2);

		ledImageLabel3.setIcon(new ImageIcon(ledImage));
		contentPane.add(ledImageLabel3);
		//contentPane.add(brakeFailPanel);
		
		Image pineapple = new ImageIcon(this.getClass().getResource("pineapple_icon.png")).getImage();
		JLabel pineappleImageLabel = new JLabel();
		pineappleImageLabel.setBounds(40, 523, 138, 76);
		pineappleImageLabel.setIcon(new ImageIcon(pineapple));
		contentPane.add(pineappleImageLabel);
		
		authorityVal.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel labelFt = new JLabel("ft.");
		labelFt.setFont(new Font("Dialog", Font.PLAIN, 18));
		labelFt.setBounds(238, 100, 44, 20);
		contentPane.add(labelFt);
		
		JLabel labelft2 = new JLabel("ft.");
		labelft2.setFont(new Font("Dialog", Font.PLAIN, 18));
		labelft2.setBounds(238, 150, 52, 20);
		contentPane.add(labelft2);
		
		JLabel labelft3 = new JLabel("ft.");
		labelft3.setFont(new Font("Dialog", Font.PLAIN, 18));
		labelft3.setBounds(238, 175, 69, 20);
		contentPane.add(labelft3);
		
		JLabel lblTons = new JLabel("lbs");
		lblTons.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTons.setBounds(238, 125, 52, 20);
		contentPane.add(lblTons);
		
		JLabel lblKw = new JLabel("kW");
		lblKw.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblKw.setBounds(598, 211, 34, 20);
		contentPane.add(lblKw);
		heightVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		
		heightVal.setBounds(144, 100, 79, 20);
		contentPane.add(heightVal);
		weightVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		
		weightVal.setBounds(129, 125, 94, 20);
		contentPane.add(weightVal);
		lengthVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		lengthVal.setBounds(144, 150, 79, 20);
		contentPane.add(lengthVal);
		widthVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		widthVal.setBounds(139, 175, 84, 20);
		contentPane.add(widthVal);
		capacityVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		capacityVal.setBounds(154, 245, 69, 20);
		contentPane.add(capacityVal);
		
		gpsAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gpsAntennaStatusLabel.setBounds(528, 501, 69, 20);
		contentPane.add(gpsAntennaStatusLabel);
		
		mboAntennaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		mboAntennaStatusLabel.setBounds(528, 525, 69, 20);
		contentPane.add(mboAntennaStatusLabel);
		stationVal.setHorizontalAlignment(SwingConstants.TRAILING);
		
		stationVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		stationVal.setBounds(178, 391, 125, 20);
		contentPane.add(stationVal);
		timeVal.setHorizontalAlignment(SwingConstants.TRAILING);
		
		timeVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		timeVal.setBounds(178, 420, 125, 20);
		contentPane.add(timeVal);
		
		leftDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		leftDoorStatusLabel.setBounds(528, 420, 69, 20);
		contentPane.add(leftDoorStatusLabel);
		
		rightDoorStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorStatusLabel.setBounds(528, 449, 69, 20);
		contentPane.add(rightDoorStatusLabel);
		
		
		lightStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lightStatusLabel.setBounds(528, 474, 69, 20);
		contentPane.add(lightStatusLabel);
		numPassengers.setHorizontalAlignment(SwingConstants.TRAILING);
		
		
		numPassengers.setFont(new Font("Dialog", Font.PLAIN, 18));
		numPassengers.setBounds(200, 472, 103, 20);
		contentPane.add(numPassengers);
		
		authorityVal.setFont(new Font("Dialog", Font.PLAIN, 18));
		authorityVal.setBounds(514, 175, 69, 20);
		contentPane.add(authorityVal);
		
		authorityUnits.setFont(new Font("Dialog", Font.PLAIN, 18));
		authorityUnits.setBounds(598, 177, 34, 20);
		contentPane.add(authorityUnits);
		
		setpointSpeedUnits.setFont(new Font("Dialog", Font.PLAIN, 18));
		setpointSpeedUnits.setBounds(598, 107, 52, 20);
		contentPane.add(setpointSpeedUnits);
		
		ctcSpeedUnitsLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		ctcSpeedUnitsLabel.setBounds(598, 141, 34, 20);
		contentPane.add(ctcSpeedUnitsLabel);
		
		ctcSpeedLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		ctcSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ctcSpeedLabel.setBounds(499, 141, 84, 20);
		contentPane.add(ctcSpeedLabel);
		
		lblServiceBrake.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblServiceBrake.setBounds(371, 245, 129, 20);
		contentPane.add(lblServiceBrake);
		
		lblEmergencyBrake.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblEmergencyBrake.setBounds(371, 277, 154, 20);
		contentPane.add(lblEmergencyBrake);
		
		emergencyLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		emergencyLabel.setBounds(529, 277, 69, 20);
		contentPane.add(emergencyLabel);
		
		serviceLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		serviceLabel.setBounds(529, 245, 69, 20);
		contentPane.add(serviceLabel);
		btnEndFailure.setBounds(855, 268, 94, 29);
		
		contentPane.add(btnEndFailure);
		numCarsSpinner.setFont(new Font("Dialog", Font.PLAIN, 18));
		
		numCarsSpinner.setBounds(164, 213, 59, 20);
		contentPane.add(numCarsSpinner);
		
		lblCrew.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblCrew.setBounds(60, 277, 69, 20);
		contentPane.add(lblCrew);
		
		crewCountLabel.setBounds(154, 277, 69, 20);
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