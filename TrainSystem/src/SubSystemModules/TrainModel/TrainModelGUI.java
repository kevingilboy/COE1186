import java.awt.EventQueue;


import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;;

public class TrainModelGUI {

	private JFrame frmTrainId;

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
					TrainModelGUI window = new TrainModelGUI();
					window.frmTrainId.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrainModelGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTrainId = new JFrame();
		frmTrainId.setTitle("Train 1");
		frmTrainId.setBounds(100, 100, 1013, 621);
		frmTrainId.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{170, 134, 0, 0, 22, 0, 0, 0, 172, 0, 0, 30, 0, 0, 31, 179, 0, 0, 36, 0, 0, 0, 38, 0};
		gridBagLayout.rowHeights = new int[]{52, 0, 0, 0, 0, 0, 0, 0, 0, -11, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmTrainId.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblSpecifications = new JLabel("Specifications");
		GridBagConstraints gbc_lblSpecifications = new GridBagConstraints();
		gbc_lblSpecifications.anchor = GridBagConstraints.WEST;
		gbc_lblSpecifications.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpecifications.gridx = 1;
		gbc_lblSpecifications.gridy = 0;
		frmTrainId.getContentPane().add(lblSpecifications, gbc_lblSpecifications);
		
		JLabel lblTrackInformation = new JLabel("Track Information");
		GridBagConstraints gbc_lblTrackInformation = new GridBagConstraints();
		gbc_lblTrackInformation.anchor = GridBagConstraints.WEST;
		gbc_lblTrackInformation.insets = new Insets(0, 0, 5, 5);
		gbc_lblTrackInformation.gridx = 8;
		gbc_lblTrackInformation.gridy = 0;
		frmTrainId.getContentPane().add(lblTrackInformation, gbc_lblTrackInformation);
		
		JLabel lblFailureModesActivation = new JLabel("Failure Modes Activation");
		GridBagConstraints gbc_lblFailureModesActivation = new GridBagConstraints();
		gbc_lblFailureModesActivation.anchor = GridBagConstraints.WEST;
		gbc_lblFailureModesActivation.insets = new Insets(0, 0, 5, 5);
		gbc_lblFailureModesActivation.gridx = 15;
		gbc_lblFailureModesActivation.gridy = 0;
		frmTrainId.getContentPane().add(lblFailureModesActivation, gbc_lblFailureModesActivation);
		
		JLabel lblFt = new JLabel("ft.");
		GridBagConstraints gbc_lblFt = new GridBagConstraints();
		gbc_lblFt.insets = new Insets(0, 0, 5, 5);
		gbc_lblFt.gridx = 5;
		gbc_lblFt.gridy = 1;
		frmTrainId.getContentPane().add(lblFt, gbc_lblFt);
		
		JLabel lblHeight = new JLabel("Height:");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.anchor = GridBagConstraints.WEST;
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 1;
		gbc_lblHeight.gridy = 1;
		frmTrainId.getContentPane().add(lblHeight, gbc_lblHeight);
		
		JLabel lblGreenLine = new JLabel("GREEN LINE");
		GridBagConstraints gbc_lblGreenLine = new GridBagConstraints();
		gbc_lblGreenLine.anchor = GridBagConstraints.WEST;
		gbc_lblGreenLine.insets = new Insets(0, 0, 5, 5);
		gbc_lblGreenLine.gridx = 8;
		gbc_lblGreenLine.gridy = 1;
		frmTrainId.getContentPane().add(lblGreenLine, gbc_lblGreenLine);
		
		JLabel lblWidth = new JLabel("Width:");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.anchor = GridBagConstraints.WEST;
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 1;
		gbc_lblWidth.gridy = 2;
		frmTrainId.getContentPane().add(lblWidth, gbc_lblWidth);
		
		JLabel lblFt_1 = new JLabel("ft.");
		GridBagConstraints gbc_lblFt_1 = new GridBagConstraints();
		gbc_lblFt_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblFt_1.gridx = 5;
		gbc_lblFt_1.gridy = 2;
		frmTrainId.getContentPane().add(lblFt_1, gbc_lblFt_1);
		
		JLabel lblBlock = new JLabel("Block:");
		GridBagConstraints gbc_lblBlock = new GridBagConstraints();
		gbc_lblBlock.anchor = GridBagConstraints.WEST;
		gbc_lblBlock.insets = new Insets(0, 0, 5, 5);
		gbc_lblBlock.gridx = 8;
		gbc_lblBlock.gridy = 2;
		frmTrainId.getContentPane().add(lblBlock, gbc_lblBlock);
		
		JLabel lblLength = new JLabel("Length:");
		GridBagConstraints gbc_lblLength = new GridBagConstraints();
		gbc_lblLength.anchor = GridBagConstraints.WEST;
		gbc_lblLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblLength.gridx = 1;
		gbc_lblLength.gridy = 3;
		frmTrainId.getContentPane().add(lblLength, gbc_lblLength);
		
		JLabel lblFt_2 = new JLabel("ft.");
		GridBagConstraints gbc_lblFt_2 = new GridBagConstraints();
		gbc_lblFt_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblFt_2.gridx = 5;
		gbc_lblFt_2.gridy = 3;
		frmTrainId.getContentPane().add(lblFt_2, gbc_lblFt_2);
		
		JLabel lblGrade = new JLabel("Grade:");
		GridBagConstraints gbc_lblGrade = new GridBagConstraints();
		gbc_lblGrade.anchor = GridBagConstraints.WEST;
		gbc_lblGrade.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrade.gridx = 8;
		gbc_lblGrade.gridy = 3;
		frmTrainId.getContentPane().add(lblGrade, gbc_lblGrade);
		
		JLabel lblWeight = new JLabel("Weight:");
		GridBagConstraints gbc_lblWeight = new GridBagConstraints();
		gbc_lblWeight.anchor = GridBagConstraints.WEST;
		gbc_lblWeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeight.gridx = 1;
		gbc_lblWeight.gridy = 4;
		frmTrainId.getContentPane().add(lblWeight, gbc_lblWeight);
		
		JLabel lblTons = new JLabel("tons");
		GridBagConstraints gbc_lblTons = new GridBagConstraints();
		gbc_lblTons.insets = new Insets(0, 0, 5, 5);
		gbc_lblTons.gridx = 5;
		gbc_lblTons.gridy = 4;
		frmTrainId.getContentPane().add(lblTons, gbc_lblTons);
		
		JLabel lblSpeedLimit = new JLabel("Speed Limit:");
		GridBagConstraints gbc_lblSpeedLimit = new GridBagConstraints();
		gbc_lblSpeedLimit.anchor = GridBagConstraints.WEST;
		gbc_lblSpeedLimit.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpeedLimit.gridx = 8;
		gbc_lblSpeedLimit.gridy = 4;
		frmTrainId.getContentPane().add(lblSpeedLimit, gbc_lblSpeedLimit);
		
		JLabel lblOfCars = new JLabel("# of Cars:");
		GridBagConstraints gbc_lblOfCars = new GridBagConstraints();
		gbc_lblOfCars.anchor = GridBagConstraints.WEST;
		gbc_lblOfCars.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfCars.gridx = 1;
		gbc_lblOfCars.gridy = 5;
		frmTrainId.getContentPane().add(lblOfCars, gbc_lblOfCars);
		
		JLabel lblGpsAntenna = new JLabel("GPS Antenna:");
		GridBagConstraints gbc_lblGpsAntenna = new GridBagConstraints();
		gbc_lblGpsAntenna.anchor = GridBagConstraints.WEST;
		gbc_lblGpsAntenna.insets = new Insets(0, 0, 5, 5);
		gbc_lblGpsAntenna.gridx = 8;
		gbc_lblGpsAntenna.gridy = 5;
		frmTrainId.getContentPane().add(lblGpsAntenna, gbc_lblGpsAntenna);
		
		JLabel lblCapacity = new JLabel("Capacity:");
		GridBagConstraints gbc_lblCapacity = new GridBagConstraints();
		gbc_lblCapacity.anchor = GridBagConstraints.WEST;
		gbc_lblCapacity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCapacity.gridx = 1;
		gbc_lblCapacity.gridy = 6;
		frmTrainId.getContentPane().add(lblCapacity, gbc_lblCapacity);
		
		JLabel lblMboAntenna = new JLabel("MBO Antenna:");
		GridBagConstraints gbc_lblMboAntenna = new GridBagConstraints();
		gbc_lblMboAntenna.anchor = GridBagConstraints.WEST;
		gbc_lblMboAntenna.insets = new Insets(0, 0, 5, 5);
		gbc_lblMboAntenna.gridx = 8;
		gbc_lblMboAntenna.gridy = 6;
		frmTrainId.getContentPane().add(lblMboAntenna, gbc_lblMboAntenna);
		
		JLabel lblPassengers = new JLabel("");
		GridBagConstraints gbc_lblPassengers = new GridBagConstraints();
		gbc_lblPassengers.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassengers.gridx = 1;
		gbc_lblPassengers.gridy = 7;
		frmTrainId.getContentPane().add(lblPassengers, gbc_lblPassengers);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 22;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 9;
		frmTrainId.getContentPane().add(separator, gbc_separator);
		
		JLabel lblStationControl = new JLabel("Station Control");
		GridBagConstraints gbc_lblStationControl = new GridBagConstraints();
		gbc_lblStationControl.anchor = GridBagConstraints.WEST;
		gbc_lblStationControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblStationControl.gridx = 1;
		gbc_lblStationControl.gridy = 10;
		frmTrainId.getContentPane().add(lblStationControl, gbc_lblStationControl);
		
		JLabel lblSpeedauthorityControl = new JLabel("Speed/Authority Control");
		GridBagConstraints gbc_lblSpeedauthorityControl = new GridBagConstraints();
		gbc_lblSpeedauthorityControl.anchor = GridBagConstraints.WEST;
		gbc_lblSpeedauthorityControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpeedauthorityControl.gridx = 8;
		gbc_lblSpeedauthorityControl.gridy = 10;
		frmTrainId.getContentPane().add(lblSpeedauthorityControl, gbc_lblSpeedauthorityControl);
		
		JLabel lblNextStation = new JLabel("Next Station:");
		GridBagConstraints gbc_lblNextStation = new GridBagConstraints();
		gbc_lblNextStation.anchor = GridBagConstraints.WEST;
		gbc_lblNextStation.insets = new Insets(0, 0, 5, 5);
		gbc_lblNextStation.gridx = 1;
		gbc_lblNextStation.gridy = 11;
		frmTrainId.getContentPane().add(lblNextStation, gbc_lblNextStation);
		
		JLabel lblActualVelocity = new JLabel("Actual Velocity:");
		GridBagConstraints gbc_lblActualVelocity = new GridBagConstraints();
		gbc_lblActualVelocity.anchor = GridBagConstraints.WEST;
		gbc_lblActualVelocity.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualVelocity.gridx = 8;
		gbc_lblActualVelocity.gridy = 11;
		frmTrainId.getContentPane().add(lblActualVelocity, gbc_lblActualVelocity);
		
		JLabel lblMph = new JLabel("mph");
		GridBagConstraints gbc_lblMph = new GridBagConstraints();
		gbc_lblMph.insets = new Insets(0, 0, 5, 5);
		gbc_lblMph.gridx = 13;
		gbc_lblMph.gridy = 11;
		frmTrainId.getContentPane().add(lblMph, gbc_lblMph);
		
		JLabel lblTimeOfArrival = new JLabel("Time of Arrival:");
		GridBagConstraints gbc_lblTimeOfArrival = new GridBagConstraints();
		gbc_lblTimeOfArrival.anchor = GridBagConstraints.WEST;
		gbc_lblTimeOfArrival.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeOfArrival.gridx = 1;
		gbc_lblTimeOfArrival.gridy = 12;
		frmTrainId.getContentPane().add(lblTimeOfArrival, gbc_lblTimeOfArrival);
		
		JLabel lblCtcVelocity = new JLabel("CTC Velocity");
		GridBagConstraints gbc_lblCtcVelocity = new GridBagConstraints();
		gbc_lblCtcVelocity.anchor = GridBagConstraints.WEST;
		gbc_lblCtcVelocity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCtcVelocity.gridx = 8;
		gbc_lblCtcVelocity.gridy = 12;
		frmTrainId.getContentPane().add(lblCtcVelocity, gbc_lblCtcVelocity);
		
		JLabel lblMph_1 = new JLabel("mph");
		GridBagConstraints gbc_lblMph_1 = new GridBagConstraints();
		gbc_lblMph_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMph_1.gridx = 13;
		gbc_lblMph_1.gridy = 12;
		frmTrainId.getContentPane().add(lblMph_1, gbc_lblMph_1);
		
		JLabel lblStatus = new JLabel("Status:");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 13;
		frmTrainId.getContentPane().add(lblStatus, gbc_lblStatus);
		
		JLabel lblCtcAuthority = new JLabel("CTC Authority:");
		GridBagConstraints gbc_lblCtcAuthority = new GridBagConstraints();
		gbc_lblCtcAuthority.anchor = GridBagConstraints.WEST;
		gbc_lblCtcAuthority.insets = new Insets(0, 0, 5, 5);
		gbc_lblCtcAuthority.gridx = 8;
		gbc_lblCtcAuthority.gridy = 13;
		frmTrainId.getContentPane().add(lblCtcAuthority, gbc_lblCtcAuthority);
		
		JLabel lblFt_3 = new JLabel("ft.");
		GridBagConstraints gbc_lblFt_3 = new GridBagConstraints();
		gbc_lblFt_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblFt_3.gridx = 13;
		gbc_lblFt_3.gridy = 13;
		frmTrainId.getContentPane().add(lblFt_3, gbc_lblFt_3);
		
		JLabel lblPassengers_1 = new JLabel("Passengers:");
		GridBagConstraints gbc_lblPassengers_1 = new GridBagConstraints();
		gbc_lblPassengers_1.anchor = GridBagConstraints.WEST;
		gbc_lblPassengers_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassengers_1.gridx = 1;
		gbc_lblPassengers_1.gridy = 14;
		frmTrainId.getContentPane().add(lblPassengers_1, gbc_lblPassengers_1);
		
		JLabel lblPowerCommand = new JLabel("Power Command:");
		GridBagConstraints gbc_lblPowerCommand = new GridBagConstraints();
		gbc_lblPowerCommand.anchor = GridBagConstraints.WEST;
		gbc_lblPowerCommand.insets = new Insets(0, 0, 5, 5);
		gbc_lblPowerCommand.gridx = 8;
		gbc_lblPowerCommand.gridy = 14;
		frmTrainId.getContentPane().add(lblPowerCommand, gbc_lblPowerCommand);
		
		JLabel lblKw = new JLabel("kW");
		GridBagConstraints gbc_lblKw = new GridBagConstraints();
		gbc_lblKw.insets = new Insets(0, 0, 5, 5);
		gbc_lblKw.gridx = 13;
		gbc_lblKw.gridy = 14;
		frmTrainId.getContentPane().add(lblKw, gbc_lblKw);
		
		JLabel lblLeftDoor = new JLabel("Left Door:");
		GridBagConstraints gbc_lblLeftDoor = new GridBagConstraints();
		gbc_lblLeftDoor.anchor = GridBagConstraints.WEST;
		gbc_lblLeftDoor.insets = new Insets(0, 0, 5, 5);
		gbc_lblLeftDoor.gridx = 1;
		gbc_lblLeftDoor.gridy = 16;
		frmTrainId.getContentPane().add(lblLeftDoor, gbc_lblLeftDoor);
		
		JLabel lblPowerInput = new JLabel("Power Input:");
		GridBagConstraints gbc_lblPowerInput = new GridBagConstraints();
		gbc_lblPowerInput.anchor = GridBagConstraints.WEST;
		gbc_lblPowerInput.insets = new Insets(0, 0, 5, 5);
		gbc_lblPowerInput.gridx = 8;
		gbc_lblPowerInput.gridy = 16;
		frmTrainId.getContentPane().add(lblPowerInput, gbc_lblPowerInput);
		
		JSpinner spinner = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.gridwidth = 3;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.gridx = 10;
		gbc_spinner.gridy = 16;
		frmTrainId.getContentPane().add(spinner, gbc_spinner);
		
		JLabel lblKw_1 = new JLabel("kW");
		GridBagConstraints gbc_lblKw_1 = new GridBagConstraints();
		gbc_lblKw_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblKw_1.gridx = 13;
		gbc_lblKw_1.gridy = 16;
		frmTrainId.getContentPane().add(lblKw_1, gbc_lblKw_1);
		
		JLabel lblRightDoor = new JLabel("Right Door:");
		GridBagConstraints gbc_lblRightDoor = new GridBagConstraints();
		gbc_lblRightDoor.anchor = GridBagConstraints.WEST;
		gbc_lblRightDoor.insets = new Insets(0, 0, 5, 5);
		gbc_lblRightDoor.gridx = 1;
		gbc_lblRightDoor.gridy = 17;
		frmTrainId.getContentPane().add(lblRightDoor, gbc_lblRightDoor);
		
		JButton btnSendNewPower = new JButton("Send Power Command");
		GridBagConstraints gbc_btnSendNewPower = new GridBagConstraints();
		gbc_btnSendNewPower.insets = new Insets(0, 0, 5, 5);
		gbc_btnSendNewPower.gridx = 8;
		gbc_btnSendNewPower.gridy = 17;
		frmTrainId.getContentPane().add(btnSendNewPower, gbc_btnSendNewPower);
		
		JLabel lblLights = new JLabel("Lights:");
		GridBagConstraints gbc_lblLights = new GridBagConstraints();
		gbc_lblLights.anchor = GridBagConstraints.WEST;
		gbc_lblLights.insets = new Insets(0, 0, 5, 5);
		gbc_lblLights.gridx = 1;
		gbc_lblLights.gridy = 18;
		frmTrainId.getContentPane().add(lblLights, gbc_lblLights);
		
		JMenuBar menuBar = new JMenuBar();
		frmTrainId.setJMenuBar(menuBar);
		
		JMenuItem mntmFile = new JMenuItem("File");
		menuBar.add(mntmFile);
		
		JMenuItem mntmSelectTrain = new JMenuItem("Select Train");
		menuBar.add(mntmSelectTrain);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		menuBar.add(mntmHelp);
	}

}
