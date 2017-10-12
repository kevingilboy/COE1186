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

public class TrainModelNewGUI extends JFrame {

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
	
	JMenu mnFile = new JMenu("File");
	JMenu mnSelectTrain = new JMenu("Select Train");
	JMenu mnHelp = new JMenu("Help");
	
	JLabel lblSpecifications = new JLabel("Train Specifications");
	JLabel lblTrackInformation = new JLabel("Track Information");
	JLabel lblFailureModeActivation = new JLabel("Failure Mode Activation");
	JLabel lblOnboardTemperature = new JLabel("Train Operations");
	JLabel lblSpeedauthority = new JLabel("Speed/Authority");
	JLabel lblStationControl = new JLabel("Station Control");

	JLabel lblHeight = new JLabel("Height:");
	JLabel lblWeight = new JLabel("Weight:");
	JLabel lblLength = new JLabel("Length:");
	JLabel lblWidth = new JLabel("Width:");
	JLabel lblOfCars = new JLabel("# of Cars:");
	JLabel lblCapacity = new JLabel("Capacity:");
	JLabel lblBlock = new JLabel("Block:");
	JLabel lblGrade = new JLabel("Grade:");
	JLabel lblSpeedLimit = new JLabel("Speed Limit:");
	JLabel lblGpsAntenna = new JLabel("GPS Antenna:");
	JLabel lblMboAntenna = new JLabel("MBO Antenna:");
	JLabel lblNextStation = new JLabel("Next Station:");
	JLabel lblTimeOfArrival = new JLabel("Time of Arrival:");
	JLabel lblStatus = new JLabel("Status:");
	JLabel passengersEnRoute = new JLabel("Passengers:");
	JLabel lblCurrentSpeed = new JLabel("Setpoint Speed:");
	JLabel lblCtcSpeed = new JLabel("CTC Speed:");
	JLabel lblCtcAuthority = new JLabel("CTC Authority:");
	JLabel lblPowerInput = new JLabel("Power:");
	
	JLabel lblLine = new JLabel("");
	JLabel heightVal = new JLabel("");
	JLabel weightVal = new JLabel("");
	JLabel lengthVal = new JLabel("");
	JLabel widthVal = new JLabel("");
	JLabel capacityVal = new JLabel("");
	JLabel blockVal = new JLabel("A2");
	JLabel gradeVal = new JLabel("");
	JLabel speedLimitVal = new JLabel("");
	JLabel arrivalStatusLabel = new JLabel("ARRIVING");
	
	JButton btnSendPowerCommand = new JButton("Set Power Input");
	JButton btnEmergencyBrake = new JButton("Emergency Brake");
	JToggleButton btnServiceBrake = new JToggleButton("Service Brake");
	
	public JSpinner numCarsSpinner = new JSpinner();
	public JSpinner tempSpinner = new JSpinner();
	JLabel setpointSpeedLabel = new JLabel("");
	JSpinner powerSpinner = new JSpinner();
	
	
	// The instance of the TrainModel class we will use to go between the back end elements
	// and the GUI elements of the Train Model
	public static TrainModel trainModel;
	
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
					TrainModelNewGUI frame = new TrainModelNewGUI(trainModel);
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
	public TrainModelNewGUI() {
		initComponents();
	}
	
	/**
	 * Create the frame using a train model object.
	 */
	public TrainModelNewGUI(TrainModel newTrainModel) {
		initComponents();
		this.trainModel = newTrainModel;
		
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
		
		menuBar.add(mnHelp);
		
		
		lblSpecifications.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpecifications.setBounds(60, 60, 196, 20);
		contentPane.add(lblSpecifications);
		
		
		lblTrackInformation.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTrackInformation.setBounds(364, 60, 178, 20);
		contentPane.add(lblTrackInformation);
		
		
		lblFailureModeActivation.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblFailureModeActivation.setBounds(660, 60, 220, 20);
		contentPane.add(lblFailureModeActivation);
		
		
		lblOnboardTemperature.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOnboardTemperature.setBounds(660, 303, 197, 20);
		contentPane.add(lblOnboardTemperature);
		
		lblSpeedauthority.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSpeedauthority.setBounds(364, 303, 178, 20);
		contentPane.add(lblSpeedauthority);
		
		lblStationControl.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStationControl.setBounds(60, 303, 154, 20);
		contentPane.add(lblStationControl);
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblHeight.setBounds(60, 96, 69, 20);
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
		
		lblOfCars.setBounds(60, 211, 104, 20);
		contentPane.add(lblOfCars);
		numCarsSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		numCarsSpinner.setBounds(153, 208, 75, 26);
		contentPane.add(numCarsSpinner);
		lblCapacity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblCapacity.setBounds(60, 243, 69, 20);
		contentPane.add(lblCapacity);
		
		
		lblLine.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLine.setBounds(364, 96, 129, 20);
		contentPane.add(lblLine);
		lblBlock.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblBlock.setBounds(364, 125, 69, 20);
		contentPane.add(lblBlock);
		lblGrade.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblGrade.setBounds(364, 150, 69, 20);
		contentPane.add(lblGrade);
		lblSpeedLimit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblSpeedLimit.setBounds(364, 175, 129, 20);
		contentPane.add(lblSpeedLimit);
		lblGpsAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblGpsAntenna.setBounds(364, 219, 129, 20);
		contentPane.add(lblGpsAntenna);
		lblMboAntenna.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblMboAntenna.setBounds(364, 243, 129, 20);
		contentPane.add(lblMboAntenna);
		lblNextStation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblNextStation.setBounds(60, 339, 129, 20);
		contentPane.add(lblNextStation);
		lblTimeOfArrival.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblTimeOfArrival.setBounds(60, 368, 129, 20);
		contentPane.add(lblTimeOfArrival);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblStatus.setBounds(60, 424, 69, 20);
		contentPane.add(lblStatus);
		arrivalStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		arrivalStatusLabel.setBounds(204, 424, 85, 20);
		contentPane.add(arrivalStatusLabel);
		passengersEnRoute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		passengersEnRoute.setBounds(60, 449, 104, 20);
		contentPane.add(passengersEnRoute);
		
		lblCurrentSpeed.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCurrentSpeed.setBounds(364, 339, 142, 20);
		contentPane.add(lblCurrentSpeed);
		lblCtcSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
	
		lblCtcSpeed.setBounds(364, 368, 129, 20);
		contentPane.add(lblCtcSpeed);
		setpointSpeedLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		setpointSpeedLabel.setBounds(521, 339, 44, 20);
		contentPane.add(setpointSpeedLabel);
		lblCtcAuthority.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblCtcAuthority.setBounds(364, 393, 142, 20);
		contentPane.add(lblCtcAuthority);
		lblPowerInput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblPowerInput.setBounds(364, 449, 178, 20);
		contentPane.add(lblPowerInput);
		powerSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		powerSpinner.setBounds(481, 446, 84, 26);
		contentPane.add(powerSpinner);
		btnSendPowerCommand.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSendPowerCommand.setBounds(364, 495, 220, 29);
		stylizeButton(btnSendPowerCommand);
		contentPane.add(btnSendPowerCommand);
		
		btnSendPowerCommand.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  // send the power command through when the button is pressed

			  setPowerIn = Integer.parseInt(powerSpinner.getValue().toString());
		  }
		});
		
		btnServiceBrake.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnServiceBrake.setForeground(Color.BLACK);
		btnServiceBrake.setBounds(670, 459, 220, 29);
		stylizeToggleButton(btnServiceBrake);
		contentPane.add(btnServiceBrake);
		btnServiceBrake.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  serviceBrake = !serviceBrake;
		  }
		});
		
		JLabel leftDoorLabel = new JLabel("Left Door:");
		leftDoorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		leftDoorLabel.setBounds(660, 339, 129, 20);
		contentPane.add(leftDoorLabel);
		
		JLabel rightDoorLabel = new JLabel("Right Door:");
		rightDoorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rightDoorLabel.setBounds(660, 368, 129, 20);
		contentPane.add(rightDoorLabel);
		
		JLabel lblLight = new JLabel("Light:");
		lblLight.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLight.setBounds(660, 393, 129, 20);
		contentPane.add(lblLight);
		
		JLabel labelTemperature = new JLabel("Cabin Temperature:");
		labelTemperature.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelTemperature.setBounds(660, 421, 154, 20);
		contentPane.add(labelTemperature);
		tempSpinner.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		tempSpinner.setBounds(818, 418, 75, 26);
		//tempSpinner.setValue(70);
		contentPane.add(tempSpinner);
		btnEmergencyBrake.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnEmergencyBrake.setForeground(Color.RED);
		btnEmergencyBrake.setBounds(670, 495, 220, 29);
		stylizeButton(btnEmergencyBrake);
		contentPane.add(btnEmergencyBrake);
		btnEmergencyBrake.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  emerBrake = !emerBrake;
		  }
		});
		
		
		
		JLabel lblEngineFailureMode = new JLabel("Engine Failure");
		lblEngineFailureMode.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEngineFailureMode.setBounds(720, 108, 129, 20);
		contentPane.add(lblEngineFailureMode);
		
		JLabel lblSignalFailure = new JLabel("Signal Failure");
		lblSignalFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignalFailure.setBounds(720, 158, 129, 20);
		contentPane.add(lblSignalFailure);
		
		JLabel lblBrakeFailure = new JLabel("Brake Failure");
		lblBrakeFailure.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBrakeFailure.setBounds(720, 208, 129, 20);
		contentPane.add(lblBrakeFailure);
		
		JPanel engineFailPanel = new JPanel();
		engineFailPanel.setBounds(660, 100, 34, 31);
		ImageIcon ledImage = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel = new JLabel("", ledImage, JLabel.CENTER);
		ledImageLabel.setBounds(660, 100, 34, 31);
		ledImageLabel.setIcon(ledImage);
		contentPane.add(ledImageLabel);
		contentPane.add(engineFailPanel);
		
		JPanel signalFailPanel = new JPanel();
		signalFailPanel.setBounds(660, 150, 34, 31);
		ImageIcon ledImage2 = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel2 = new JLabel("", ledImage2, JLabel.CENTER);
		ledImageLabel.setBounds(660, 150, 34, 31);
		ledImageLabel.setIcon(ledImage2);
		contentPane.add(ledImageLabel2);
		contentPane.add(signalFailPanel);
		
		JPanel brakeFailPanel = new JPanel();
		brakeFailPanel.setBounds(660, 200, 34, 31);
		ImageIcon ledImageNew = new ImageIcon("greyStatusIcon.png");
		JLabel ledImageLabel3 = new JLabel("", ledImageNew, JLabel.CENTER);
		ledImageLabel3.setBounds(660, 200, 34, 31);
		ledImageLabel3.setIcon(ledImageNew);
		contentPane.add(ledImageLabel3);
		contentPane.add(brakeFailPanel);
		
		JLabel labelFt = new JLabel("ft.");
		labelFt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelFt.setBounds(238, 96, 69, 20);
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
		lblKw.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblKw.setBounds(574, 449, 34, 20);
		contentPane.add(lblKw);
		heightVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		
		heightVal.setBounds(154, 96, 69, 20);
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
		
		capacityVal.setBounds(163, 243, 69, 20);
		contentPane.add(capacityVal);
		blockVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		blockVal.setBounds(504, 125, 69, 20);
		contentPane.add(blockVal);
		gradeVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		
		gradeVal.setBounds(504, 150, 52, 20);
		contentPane.add(gradeVal);
		
		JLabel percent = new JLabel("%");
		percent.setFont(new Font("Tahoma", Font.PLAIN, 16));
		percent.setBounds(557, 150, 34, 20);
		contentPane.add(percent);
		speedLimitVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		speedLimitVal.setBounds(504, 175, 69, 20);
		contentPane.add(speedLimitVal);
		
		JLabel lblMph = new JLabel("mph");
		lblMph.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMph.setBounds(557, 175, 34, 20);
		contentPane.add(lblMph);
		
		JLabel lblOn = new JLabel("ON");
		lblOn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOn.setBounds(504, 219, 69, 20);
		contentPane.add(lblOn);
		
		JLabel label_11 = new JLabel("ON");
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_11.setBounds(504, 243, 69, 20);
		contentPane.add(label_11);
		
		JLabel stationVal = new JLabel("Pioneer");
		stationVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		stationVal.setBounds(204, 339, 69, 20);
		contentPane.add(stationVal);
		
		JLabel timeVal = new JLabel("9:15");
		timeVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		timeVal.setBounds(204, 368, 52, 20);
		contentPane.add(timeVal);
		
		JLabel lblAm = new JLabel("AM");
		lblAm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblAm.setBounds(255, 368, 34, 20);
		contentPane.add(lblAm);
		
		JLabel lblOpen = new JLabel("OPEN");
		lblOpen.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOpen.setBounds(818, 339, 69, 20);
		contentPane.add(lblOpen);
		
		JLabel lblClosed = new JLabel("CLOSED");
		lblClosed.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblClosed.setBounds(818, 368, 69, 20);
		contentPane.add(lblClosed);
		
		JLabel lblOff = new JLabel("OFF");
		lblOff.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOff.setBounds(818, 393, 69, 20);
		contentPane.add(lblOff);
		
		JLabel label_13 = new JLabel("20");
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_13.setBounds(204, 449, 69, 20);
		contentPane.add(label_13);
		
		ImageIcon pineapple = new ImageIcon("pineapple_icon.png");
		JLabel pineappleImageLabel = new JLabel("", pineapple, JLabel.CENTER);
		pineappleImageLabel.setBounds(40, 480, 138, 76);
		pineappleImageLabel.setIcon(pineapple);
		contentPane.add(pineappleImageLabel);
		
		JLabel label_14 = new JLabel("100");
		label_14.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_14.setBounds(521, 393, 44, 20);
		contentPane.add(label_14);
		
		JLabel lblMi = new JLabel("mi");
		lblMi.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMi.setBounds(574, 393, 34, 20);
		contentPane.add(lblMi);
		
		JLabel lblMph_1 = new JLabel("mph");
		lblMph_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMph_1.setBounds(574, 339, 34, 20);
		contentPane.add(lblMph_1);
		
		JLabel ctcSpeedUnitsLabel = new JLabel("mph");
		ctcSpeedUnitsLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ctcSpeedUnitsLabel.setBounds(574, 368, 34, 20);
		contentPane.add(ctcSpeedUnitsLabel);
		
	}
	
	public void stylizeButton(JButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
    	b.setBorder(thickBorder);
		b.setContentAreaFilled(false);
		b.setOpaque(true);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}
	
	public void stylizeToggleButton(JToggleButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
    	b.setBorder(thickBorder);
		b.setContentAreaFilled(false);
		b.setOpaque(true);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}
}
