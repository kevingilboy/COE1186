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
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

// UI STYLING
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

/**
 * Class for the individual GUIs of each existing train model. Every train model will have an existing
 * GUI upon dispatch, but on simulation start up, the first train in the train list will appear and every
 * GUI after that will have to be manually selected from the Select Train dropdown menu.
 * 
 * @author Jennifer
 *
 */
public class TrainModelGUI extends JFrame {

	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/
	
	/**
	 * Variations of Roboto Condensed Font
	 */
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 20);
	Font font_20_bold = new Font("Roboto Condensed Bold", Font.BOLD, 24);
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

	private JSplitPane splitPane;
	private JPanel contentPane;
	private JPanel advertisePane;
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

	private JLabel lblSpecifications = new JLabel("TRAIN SPECIFICATIONS");
	private JLabel lblFailureModeActivation = new JLabel("FAILURE MODE ACTIVATION");
	private JLabel lblOnboardTemperature = new JLabel("TRAIN OPERATIONS");
	private JLabel lblSpeedauthority = new JLabel("SPEED/AUTHORITY");
	private JLabel lblStationControl = new JLabel("STATION CONTROL");

	private JLabel lblHeight = new JLabel("HEIGHT:");
	private JLabel lblWeight = new JLabel("WEIGHT:");
	private JLabel lblLength = new JLabel("LENGTH:");
	private JLabel lblWidth = new JLabel("WIDTH:");
	private JLabel lblOfCars = new JLabel("# OF CARS:");
	private JLabel lblCapacity = new JLabel("CAPACITY:");
	//private JLabel lblSpeedLimit = new JLabel("Speed Limit:");
	private JLabel lblGpsAntenna = new JLabel("GPS ANTENNA:");
	private JLabel lblMboAntenna = new JLabel("MBO ANTENNA:");
	private JLabel lblNextStation = new JLabel("NEXT STATION:");
	private JLabel lblTimeOfArrival = new JLabel("TIME:");
	private JLabel lblStatus = new JLabel("STATUS:");
	private JLabel passengersEnRoute = new JLabel("PASSENGERS:");
	private JLabel lblCurrentSpeed = new JLabel("CURRENT SPEED:");
	private JLabel lblCtcSpeed = new JLabel("CTC SPEED:");
	private JLabel lblCtcAuthority = new JLabel("CTC AUTHORITY:");
	private JLabel lblPowerInput = new JLabel("POWER:");
	private JLabel leftDoorLabel = new JLabel("LEFT DOOR:");
	private JLabel rightDoorLabel = new JLabel("RIGHT DOOR:");
	private JLabel lblLight = new JLabel("LIGHT:");
	private JLabel labelTemperature = new JLabel("CABIN TEMPERATURE");
	private JLabel lblEngineFailureMode = new JLabel("ENGINE FAILURE");
	private JLabel lblSignalFailure = new JLabel("SIGNAL FAILURE");
	private JLabel lblBrakeFailure = new JLabel("BRAKE FAILURE");
	private JLabel authorityUnits = new JLabel("mi");
	private JLabel setpointSpeedUnits = new JLabel("mi/h");
	private JLabel ctcSpeedUnitsLabel = new JLabel("mi/h");
	private JLabel lblServiceBrake = new JLabel("SERVICE BRAKE:");
	private JLabel lblEmergencyBrake = new JLabel("EMERGENCY BRAKE:");
	
	public JPanel engineFailPanel = new JPanel();
	public JPanel signalFailPanel = new JPanel();
	public JPanel brakeFailPanel = new JPanel();
	private boolean engineFail;
	private boolean sigFail;
	private boolean brakeFail;
	public JMenu mnFile = new JMenu("FILE");
	public JMenu mnSelectTrain = new JMenu("SELECT TRAIN");
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
	private JLabel lblCrew = new JLabel("CREW:");
	JLabel crewCountLabel = new JLabel();
	public JLabel arrivalStatusLabel = new JLabel();
	public JLabel currentSpeedLabel = new JLabel();
	
	public JButton btnEmergencyBrake = new JButton("EMERGENCY BRAKE");
	public JLabel tempLabel = new JLabel();
	
	public JLabel powerVal = new JLabel();
	
	
	// The instance of the TrainModel class we will use to go between the back end elements
	// and the GUI elements of the Train Model
	public static Train train;
	//private final JMenuItem mntmSimulateBrakeFailure = new JMenuItem("Simulate Brake Failure");
	//private final JMenuItem mntmSimulateEngineFailure = new JMenuItem("Simulate Engine Failure");
	//private final JMenuItem mntmSimulateSignalFailure = new JMenuItem("Simulate Signal Failure");
	private final JMenuItem mntmExit = new JMenuItem("Exit All");
	private Image ledImage = new ImageIcon(this.getClass().getResource("images/statusIcon_grey.png")).getImage();
	private Image ledImageRed = new ImageIcon(this.getClass().getResource("images/statusIcon_red.png")).getImage();
	private Image ad1 = new ImageIcon(this.getClass().getResource("ad1.jpg")).getImage();
	private Image spongebob1 = new ImageIcon(this.getClass().getResource("spongebob1.jpg")).getImage();
	private Image aerotech = new ImageIcon(this.getClass().getResource("aerotech.jpg")).getImage();
	private Image safety = new ImageIcon(this.getClass().getResource("safety.jpg")).getImage();
	private Image mouse = new ImageIcon(this.getClass().getResource("mouse.jpg")).getImage();
	private ArrayList<Image> imgArray = new ArrayList<>();
	private int i = 0;
	private final JLabel ledImageLabel = new JLabel();
	private final JLabel ledImageLabel2 = new JLabel();
	private final JLabel ledImageLabel3 = new JLabel();
	//private final JMenuItem mntmEndFailures = new JMenuItem("End Failure(s)");
	JButton btnCauseFailure = new JButton("CAUSE");
	JCheckBox engineFailCheckBox = new JCheckBox("");
	JCheckBox signalFailCheckBox = new JCheckBox("");
	JCheckBox brakeFailCheckBox = new JCheckBox("");
	private final JButton btnEndFailure = new JButton("END");
	
	/*public TrainModelNewGUI() {
		
	}*/
	
	public void paint(Graphics g) {
		 	Dimension d = contentPane.getSize();
	        super.paint(g);  // fixes the immediate problem.
	        Graphics2D g2 = (Graphics2D) g;

	        g2.setColor(new Color(50, 50, 50));
	        horizontalLine1 = new Line2D.Float(45, d.height-290, d.width - 45, d.height-290);
	        verticalLine1 = new Line2D.Float(d.width/3 - 10, 100 , d.width/3 - 10, d.height - 325);
	        verticalLine2 = new Line2D.Float((2*d.width)/3-10, 100 , (2*d.width)/3-10, d.height - 325);
	        verticalLine3 = new Line2D.Float(d.width/3 - 10, 390 , d.width/3 - 10, d.height - 25);
	        verticalLine4 = new Line2D.Float((2*d.width)/3-10, 390 , (2*d.width)/3-10, d.height - 25);

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
	
	private Image getImage() {
		if(i == imgArray.size()-1) {
			i = 0;
		}
		i++;
		return imgArray.get(i);
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
		splitPane = new JSplitPane();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(150, 150, 1050, 850);
		setResizable(false);
		contentPane = new JPanel();
		this.setBackground(new Color(36, 39, 45));
		contentPane.setBackground(new Color(26, 29, 35));
		setContentPane(splitPane);
		//setContentPane(contentPane);
		contentPane.setLayout(null);
		
		advertisePane = new JPanel();
		advertisePane.setBackground(new Color(20, 20, 20));
		advertisePane.setLayout(new BorderLayout());
		//contentPane.add(advertisePane);
		
		imgArray.add(spongebob1);
		imgArray.add(aerotech);
		imgArray.add(mouse);
		imgArray.add(safety);
		JLabel advertisementImageLabel = new JLabel();
		advertisementImageLabel.setBounds(0, 0, 1050, 200);
		Image dimg = mouse.getScaledInstance(advertisementImageLabel.getWidth(), advertisementImageLabel.getHeight(),
		        Image.SCALE_SMOOTH);
		advertisementImageLabel.setIcon(new ImageIcon(spongebob1));
		advertisePane.add(advertisementImageLabel);
		
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);  // we want it to split the window verticaly
        splitPane.setDividerLocation(650);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setTopComponent(contentPane);                  // at the top we want our "topPanel"
        splitPane.setBottomComponent(advertisePane);            // and at the bottom we want our "bottomPanel"
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBounds(0, 0, 1044, 31);
		menuBar.setBackground(new Color(20, 20, 20));
		contentPane.add(menuBar);
		
		mnFile.setForeground(Color.WHITE);
		menuBar.add(mnFile);
		stylizeButton(btnEndFailure);
		
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
		
		stylizeButton(btnCauseFailure);

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

		engineFailCheckBox.setBackground(new Color(20, 20, 20));
		engineFailCheckBox.setBounds(910, 99, 52, 29);
		contentPane.add(engineFailCheckBox);
		engineFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engineFail = true;
			}
		});

		signalFailCheckBox.setBackground(new Color(20, 20, 20));
		signalFailCheckBox.setBounds(910, 155, 52, 29);
		contentPane.add(signalFailCheckBox);
		signalFailCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigFail = true;
			}
		});

		brakeFailCheckBox.setBackground(new Color(20, 20, 20));
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
		
		mnSelectTrain.setForeground(Color.WHITE);
		menuBar.add(mnSelectTrain);
		mnSelectTrain.add(menuTrainlist);
		
		
		stylizeHeadingLabel(lblSpecifications);
		lblSpecifications.setBounds(60, 60, 300, 20);
		contentPane.add(lblSpecifications);
		
		
		stylizeHeadingLabel(lblFailureModeActivation);
		lblFailureModeActivation.setBounds(723, 60, 290, 20);
		contentPane.add(lblFailureModeActivation);
		
		
		stylizeHeadingLabel(lblOnboardTemperature);
		lblOnboardTemperature.setBounds(371, 355, 201, 20);
		contentPane.add(lblOnboardTemperature);
		
		stylizeHeadingLabel(lblSpeedauthority);
		lblSpeedauthority.setBounds(371, 60, 585, 20);
		contentPane.add(lblSpeedauthority);
		
		stylizeHeadingLabel(lblStationControl);
		lblStationControl.setBounds(60, 355, 190, 20);
		contentPane.add(lblStationControl);

		stylizeInfoLabel(lblHeight);
		lblHeight.setBounds(60, 100, 69, 20);
		contentPane.add(lblHeight);

		stylizeInfoLabel(lblWeight);
		lblWeight.setBounds(60, 125, 69, 20);
		contentPane.add(lblWeight);

		stylizeInfoLabel(lblLength);
		lblLength.setBounds(60, 150,100, 20);
		contentPane.add(lblLength);

		stylizeInfoLabel(lblWidth);
		lblWidth.setBounds(60, 175, 69, 20);
		contentPane.add(lblWidth);
		
		stylizeInfoLabel(lblOfCars);
		lblOfCars.setBounds(60, 211, 104, 20);
		contentPane.add(lblOfCars);
		
		stylizeInfoLabel(lblCapacity);
		lblCapacity.setBounds(60, 245, 104, 20);
		contentPane.add(lblCapacity);
		
		
		stylizeInfoLabel(lblLine);
		lblLine.setBounds(371, 391, 129, 20);
		contentPane.add(lblLine);

		stylizeInfoLabel(lblGpsAntenna);
		lblGpsAntenna.setBounds(371, 501, 129, 20);
		contentPane.add(lblGpsAntenna);

		stylizeInfoLabel(lblMboAntenna);
		lblMboAntenna.setBounds(371, 525, 129, 20);
		contentPane.add(lblMboAntenna);

		stylizeInfoLabel(lblNextStation);
		lblNextStation.setBounds(60, 391, 129, 20);
		contentPane.add(lblNextStation);

		stylizeInfoLabel(lblTimeOfArrival);
		lblTimeOfArrival.setBounds(60, 420, 85, 20);
		contentPane.add(lblTimeOfArrival);
		
		stylizeInfoLabel(lblStatus);
		lblStatus.setBounds(60, 447, 69, 20);
		contentPane.add(lblStatus);

		arrivalStatusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		stylizeInfoLabel_Bold(arrivalStatusLabel);
		arrivalStatusLabel.setBounds(144, 447, 159, 20);
		contentPane.add(arrivalStatusLabel);
		
		stylizeInfoLabel(passengersEnRoute);
		passengersEnRoute.setBounds(60, 472, 150, 20);
		contentPane.add(passengersEnRoute);
		
		stylizeInfoLabel(lblCurrentSpeed);
		lblCurrentSpeed.setBounds(371, 105, 142, 20);
		contentPane.add(lblCurrentSpeed);
		
		stylizeInfoLabel(lblCtcSpeed);
		lblCtcSpeed.setBounds(371, 139, 129, 20);
		contentPane.add(lblCtcSpeed);
		
		currentSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		stylizeInfoLabel_Bold(currentSpeedLabel);
		currentSpeedLabel.setBounds(514, 108, 69, 20);
		contentPane.add(currentSpeedLabel);
		
		stylizeInfoLabel(lblCtcAuthority);
		lblCtcAuthority.setBounds(371, 175, 142, 20);
		contentPane.add(lblCtcAuthority);
		
		stylizeInfoLabel(lblPowerInput);
		lblPowerInput.setBounds(371, 209, 85, 20);
		contentPane.add(lblPowerInput);

		stylizeInfoLabel_Bold(powerVal);
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
		
		stylizeInfoLabel(leftDoorLabel);
		leftDoorLabel.setBounds(371, 420, 129, 20);
		contentPane.add(leftDoorLabel);
		
		stylizeInfoLabel(rightDoorLabel);
		rightDoorLabel.setBounds(371, 449, 129, 20);
		contentPane.add(rightDoorLabel);
		
		stylizeInfoLabel(lblLight);
		lblLight.setBounds(371, 474, 129, 20);
		contentPane.add(lblLight);

		labelTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		stylizeInfoLabel(labelTemperature);
		labelTemperature.setBounds(745, 502, 233, 20);
		contentPane.add(labelTemperature);

		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stylizeInfoLabel_Bold(tempLabel);
		tempLabel.setBounds(821, 540, 75, 26);
		//tempLabel.setValue(70);
		contentPane.add(tempLabel);
		
		stylizeButton(btnEmergencyBrake);
		btnEmergencyBrake.setBackground(Color.RED);
		btnEmergencyBrake.setForeground(Color.WHITE);
		btnEmergencyBrake.setFont(font_20_bold);
		btnEmergencyBrake.setBounds(720, 371, 250, 100);
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
		
		
		stylizeInfoLabel(lblEngineFailureMode);
		lblEngineFailureMode.setBounds(765, 103, 150, 20);
		contentPane.add(lblEngineFailureMode);
		
		stylizeInfoLabel(lblSignalFailure);
		lblSignalFailure.setBounds(765, 160, 150, 20);
		contentPane.add(lblSignalFailure);
		
		stylizeInfoLabel(lblBrakeFailure);
		lblBrakeFailure.setBounds(765, 216, 150, 20);
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
		pineappleImageLabel.setBounds(8, 559, 138, 76);
		pineappleImageLabel.setIcon(new ImageIcon(pineapple));
		contentPane.add(pineappleImageLabel);
		
		authorityVal.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel labelFt = new JLabel("ft.");
		stylizeInfoLabel(labelFt);
		labelFt.setBounds(238, 100, 44, 20);
		contentPane.add(labelFt);
		
		JLabel labelft2 = new JLabel("ft.");
		stylizeInfoLabel(labelft2);
		labelft2.setBounds(238, 150, 52, 20);
		contentPane.add(labelft2);
		
		JLabel labelft3 = new JLabel("ft.");
		stylizeInfoLabel(labelft3);
		labelft3.setBounds(238, 175, 69, 20);
		contentPane.add(labelft3);
		
		JLabel lblTons = new JLabel("lbs");
		stylizeInfoLabel(lblTons);
		lblTons.setBounds(238, 125, 52, 20);
		contentPane.add(lblTons);
		
		JLabel lblKw = new JLabel("kW");
		stylizeInfoLabel(lblKw);
		lblKw.setBounds(598, 211, 34, 20);
		contentPane.add(lblKw);

		stylizeInfoLabel_Bold(heightVal);
		heightVal.setBounds(144, 100, 79, 20);
		contentPane.add(heightVal);
		
		stylizeInfoLabel_Bold(weightVal);
		weightVal.setBounds(129, 125, 94, 20);
		contentPane.add(weightVal);

		stylizeInfoLabel_Bold(lengthVal);
		lengthVal.setBounds(144, 150, 79, 20);
		contentPane.add(lengthVal);

		stylizeInfoLabel_Bold(widthVal);
		widthVal.setBounds(139, 175, 84, 20);
		contentPane.add(widthVal);

		stylizeInfoLabel_Bold(capacityVal);
		capacityVal.setBounds(154, 245, 69, 20);
		contentPane.add(capacityVal);
		
		stylizeInfoLabel_Bold(gpsAntennaStatusLabel);
		gpsAntennaStatusLabel.setBounds(528, 501, 69, 20);
		contentPane.add(gpsAntennaStatusLabel);
		
		stylizeInfoLabel_Bold(mboAntennaStatusLabel);
		mboAntennaStatusLabel.setBounds(528, 525, 69, 20);
		contentPane.add(mboAntennaStatusLabel);

		stationVal.setHorizontalAlignment(SwingConstants.TRAILING);
		stylizeInfoLabel_Bold(stationVal);
		stationVal.setBounds(178, 391, 125, 20);
		contentPane.add(stationVal);

		timeVal.setHorizontalAlignment(SwingConstants.TRAILING);
		stylizeInfoLabel_Bold(timeVal);
		timeVal.setBounds(178, 420, 125, 20);
		contentPane.add(timeVal);
		
		stylizeInfoLabel_Bold(leftDoorStatusLabel);
		leftDoorStatusLabel.setBounds(528, 420, 69, 20);
		contentPane.add(leftDoorStatusLabel);
		
		stylizeInfoLabel_Bold(rightDoorStatusLabel);
		rightDoorStatusLabel.setBounds(528, 449, 69, 20);
		contentPane.add(rightDoorStatusLabel);
		
		
		stylizeInfoLabel_Bold(lightStatusLabel);
		lightStatusLabel.setBounds(528, 474, 69, 20);
		contentPane.add(lightStatusLabel);
		
		numPassengers.setHorizontalAlignment(SwingConstants.TRAILING);
		stylizeInfoLabel_Bold(numPassengers);
		numPassengers.setBounds(200, 472, 103, 20);
		contentPane.add(numPassengers);
		
		stylizeInfoLabel_Bold(authorityVal);
		authorityVal.setBounds(514, 175, 69, 20);
		contentPane.add(authorityVal);
		
		stylizeInfoLabel(authorityUnits);
		authorityUnits.setBounds(598, 177, 34, 20);
		contentPane.add(authorityUnits);
		
		stylizeInfoLabel(setpointSpeedUnits);
		setpointSpeedUnits.setBounds(598, 107, 52, 20);
		contentPane.add(setpointSpeedUnits);
		
		stylizeInfoLabel(ctcSpeedUnitsLabel);
		ctcSpeedUnitsLabel.setBounds(598, 141, 52, 20);
		contentPane.add(ctcSpeedUnitsLabel);
		
		stylizeInfoLabel_Bold(ctcSpeedLabel);
		ctcSpeedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ctcSpeedLabel.setBounds(499, 141, 84, 20);
		contentPane.add(ctcSpeedLabel);
		
		stylizeInfoLabel(lblServiceBrake);
		lblServiceBrake.setBounds(371, 245, 170, 20);
		contentPane.add(lblServiceBrake);
		
		stylizeInfoLabel(lblEmergencyBrake);
		lblEmergencyBrake.setBounds(371, 277, 170, 20);
		contentPane.add(lblEmergencyBrake);
		
		stylizeInfoLabel_Bold(emergencyLabel);
		emergencyLabel.setBounds(529, 277, 69, 20);
		contentPane.add(emergencyLabel);
		
		stylizeInfoLabel_Bold(serviceLabel);
		serviceLabel.setBounds(529, 245, 69, 20);
		contentPane.add(serviceLabel);

		stylizeButton(btnEndFailure);
		btnEndFailure.setBounds(855, 268, 94, 29);
		contentPane.add(btnEndFailure);

		stylizeInfoLabel_Bold(numCarsSpinner);
		numCarsSpinner.setBounds(164, 213, 59, 20);
		contentPane.add(numCarsSpinner);
		
		stylizeInfoLabel(lblCrew);
		lblCrew.setBounds(60, 277, 69, 20);
		contentPane.add(lblCrew);
		
		stylizeInfoLabel_Bold(crewCountLabel);
		crewCountLabel.setBounds(154, 277, 69, 20);
		contentPane.add(crewCountLabel);
		
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