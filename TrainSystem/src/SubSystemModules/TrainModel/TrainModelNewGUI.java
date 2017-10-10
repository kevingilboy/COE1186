import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.ImageIcon;

public class TrainModelNewGUI extends JFrame {

	private JPanel contentPane;

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
					TrainModelNewGUI frame = new TrainModelNewGUI();
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
	public TrainModelNewGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 926, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 904, 31);
		contentPane.add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnSelectTrain = new JMenu("Select Train");
		menuBar.add(mnSelectTrain);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JLabel lblSpecifications = new JLabel("Specifications");
		lblSpecifications.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSpecifications.setBounds(45, 58, 129, 20);
		contentPane.add(lblSpecifications);
		
		JLabel lblTrackInformation = new JLabel("Track Information");
		lblTrackInformation.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTrackInformation.setBounds(350, 58, 154, 20);
		contentPane.add(lblTrackInformation);
		
		JLabel lblFailureModeActivation = new JLabel("Failure Mode Activation");
		lblFailureModeActivation.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblFailureModeActivation.setBounds(656, 58, 220, 20);
		contentPane.add(lblFailureModeActivation);
		
		JLabel lblOnboardTemperature = new JLabel("Train Operations");
		lblOnboardTemperature.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOnboardTemperature.setBounds(656, 283, 197, 20);
		contentPane.add(lblOnboardTemperature);
		
		JLabel lblSpeedauthority = new JLabel("Speed/Authority");
		lblSpeedauthority.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSpeedauthority.setBounds(350, 283, 142, 20);
		contentPane.add(lblSpeedauthority);
		
		JLabel lblStationControl = new JLabel("Station Control");
		lblStationControl.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStationControl.setBounds(45, 283, 129, 20);
		contentPane.add(lblStationControl);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(45, 94, 69, 20);
		contentPane.add(lblHeight);
		
		JLabel lblWeight = new JLabel("Weight:");
		lblWeight.setBounds(45, 123, 69, 20);
		contentPane.add(lblWeight);
		
		JLabel lblLength = new JLabel("Length:");
		lblLength.setBounds(45, 148, 69, 20);
		contentPane.add(lblLength);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(45, 173, 69, 20);
		contentPane.add(lblWidth);
		
		JLabel lblOfCars = new JLabel("# of Cars");
		lblOfCars.setBounds(45, 195, 69, 20);
		contentPane.add(lblOfCars);
		
		JLabel lblCapacity = new JLabel("Capacity");
		lblCapacity.setBounds(45, 219, 69, 20);
		contentPane.add(lblCapacity);
		
		JLabel lblGreenLine = new JLabel("GREEN LINE");
		lblGreenLine.setBounds(350, 94, 129, 20);
		contentPane.add(lblGreenLine);
		
		JLabel lblBlock = new JLabel("Block:");
		lblBlock.setBounds(350, 123, 69, 20);
		contentPane.add(lblBlock);
		
		JLabel lblGrade = new JLabel("Grade:");
		lblGrade.setBounds(350, 148, 69, 20);
		contentPane.add(lblGrade);
		
		JLabel lblSpeedLimit = new JLabel("Speed Limit:");
		lblSpeedLimit.setBounds(350, 173, 129, 20);
		contentPane.add(lblSpeedLimit);
		
		JLabel lblGpsAntenna = new JLabel("GPS Antenna");
		lblGpsAntenna.setBounds(350, 195, 129, 20);
		contentPane.add(lblGpsAntenna);
		
		JLabel lblMboAntenna = new JLabel("MBO Antenna:");
		lblMboAntenna.setBounds(350, 219, 129, 20);
		contentPane.add(lblMboAntenna);
		
		JLabel lblNextStation = new JLabel("Next Station:");
		lblNextStation.setBounds(45, 319, 129, 20);
		contentPane.add(lblNextStation);
		
		JLabel lblTimeOfArrival = new JLabel("Time of Arrival:");
		lblTimeOfArrival.setBounds(45, 348, 129, 20);
		contentPane.add(lblTimeOfArrival);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(45, 373, 69, 20);
		contentPane.add(lblStatus);
		
		JLabel lblPassengersEnRoute = new JLabel("Passengers en Route:");
		lblPassengersEnRoute.setBounds(45, 398, 178, 20);
		contentPane.add(lblPassengersEnRoute);
		
		JLabel lblCurrentSpeed = new JLabel("Setpoint Speed:");
		lblCurrentSpeed.setBounds(350, 319, 129, 20);
		contentPane.add(lblCurrentSpeed);
		
		JLabel lblCtcSpeed = new JLabel("CTC Speed:");
		lblCtcSpeed.setBounds(350, 348, 129, 20);
		contentPane.add(lblCtcSpeed);
		
		JLabel lblCtcAuthority = new JLabel("CTC Authority:");
		lblCtcAuthority.setBounds(350, 373, 142, 20);
		contentPane.add(lblCtcAuthority);
		
		JLabel lblPowerOutput = new JLabel("Power Output");
		lblPowerOutput.setBounds(350, 398, 129, 20);
		contentPane.add(lblPowerOutput);
		
		JLabel label = new JLabel("Left Door:");
		label.setBounds(656, 319, 129, 20);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Right Door:");
		label_1.setBounds(656, 348, 129, 20);
		contentPane.add(label_1);
		
		JLabel lblLight = new JLabel("Light:");
		lblLight.setBounds(656, 373, 129, 20);
		contentPane.add(lblLight);
		
		JLabel lblOnboardTemperature_1 = new JLabel("Onboard Temperature:");
		lblOnboardTemperature_1.setBounds(656, 419, 178, 20);
		contentPane.add(lblOnboardTemperature_1);
		
		JButton btnEmergencyBrake = new JButton("Emergency Brake");
		btnEmergencyBrake.setBounds(656, 454, 220, 29);
		contentPane.add(btnEmergencyBrake);
		
		JLabel lblPowerInput = new JLabel("Power Input:");
		lblPowerInput.setBounds(350, 419, 178, 20);
		contentPane.add(lblPowerInput);
		
		JButton btnSendPowerCommand = new JButton("Send Power Command");
		btnSendPowerCommand.setBounds(350, 454, 220, 29);
		contentPane.add(btnSendPowerCommand);
		
		JLabel lblEngineFailureMode = new JLabel("Engine Failure");
		lblEngineFailureMode.setBounds(716, 108, 129, 20);
		contentPane.add(lblEngineFailureMode);
		
		JLabel lblSignalFailure = new JLabel("Signal Failure");
		lblSignalFailure.setBounds(716, 159, 129, 20);
		contentPane.add(lblSignalFailure);
		
		JLabel lblBrakeFailure = new JLabel("Brake Failure");
		lblBrakeFailure.setBounds(716, 207, 129, 20);
		contentPane.add(lblBrakeFailure);
		
		JPanel panel = new JPanel();
		panel.setBounds(656, 97, 34, 31);
		ImageIcon ledImage = new ImageIcon("C:\\Users\\Jennifer\\Documents\\GitHub\\COE1186\\TrainSystem\\src\\SubSystemModules\\TrainModel");
		JLabel ledImageLabel = new JLabel("", ledImage, JLabel.CENTER);
		contentPane.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(656, 148, 34, 31);
		contentPane.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(656, 195, 34, 31);
		contentPane.add(panel_2);
	}
}
