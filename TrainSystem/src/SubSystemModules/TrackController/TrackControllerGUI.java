import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

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
import java.awt.Color;
import javax.swing.JButton;

public class TrackControllerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textStatus;
	private JTextField textOccupancy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackControllerGUI frame = new TrackControllerGUI();
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
	public TrackControllerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 0, 888, 507);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel trackSelectorPanel = new JPanel();
		trackSelectorPanel.setBorder(null);
		trackSelectorPanel.setBounds(346, 55, 502, 67);
		panel.add(trackSelectorPanel);
		trackSelectorPanel.setLayout(null);
		
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(0, 0, 502, 12);
		trackSelectorPanel.add(separator1);
		
		JLabel labelBlock = new JLabel("Block");
		labelBlock.setBounds(338, 23, 34, 16);
		trackSelectorPanel.add(labelBlock);
		
		JLabel labelSection = new JLabel("Section");
		labelSection.setBounds(164, 23, 46, 16);
		trackSelectorPanel.add(labelSection);
		
		JLabel labelLine = new JLabel("Line");
		labelLine.setBounds(10, 23, 26, 16);
		trackSelectorPanel.add(labelLine);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(0, 54, 502, 12);
		trackSelectorPanel.add(separator2);
		
		JComboBox comboBlock = new JComboBox();
		comboBlock.setBounds(384, 19, 104, 27);
		trackSelectorPanel.add(comboBlock);
		comboBlock.setModel(new DefaultComboBoxModel(new String[] {"2"}));
		
		JComboBox comboSection = new JComboBox();
		comboSection.setBounds(222, 19, 104, 27);
		trackSelectorPanel.add(comboSection);
		comboSection.setModel(new DefaultComboBoxModel(new String[] {"A"}));
		
		JComboBox comboLine = new JComboBox();
		comboLine.setBounds(48, 19, 104, 27);
		trackSelectorPanel.add(comboLine);
		comboLine.setModel(new DefaultComboBoxModel(new String[] {"Green"}));
		
		JPanel trackInfoPanel = new JPanel();
		trackInfoPanel.setBorder(null);
		trackInfoPanel.setBounds(346, 119, 502, 329);
		panel.add(trackInfoPanel);
		trackInfoPanel.setLayout(null);
		
		JLabel labelController = new JLabel("<html><b>Controller - GA</b><html>");
		labelController.setBounds(0, -1, 502, 32);
		trackInfoPanel.add(labelController);
		labelController.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel updatePanel = new JPanel();
		updatePanel.setBounds(10, 32, 487, 59);
		trackInfoPanel.add(updatePanel);
		updatePanel.setLayout(null);
		
		JLabel labelStatus = new JLabel("Status");
		labelStatus.setBounds(136, 0, 73, 28);
		updatePanel.add(labelStatus);
		labelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel labelOccupancy = new JLabel("Occupancy");
		labelOccupancy.setBounds(135, 29, 73, 28);
		updatePanel.add(labelOccupancy);
		labelOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textStatus = new JTextField();
		textStatus.setBounds(220, 0, 134, 28);
		updatePanel.add(textStatus);
		textStatus.setHorizontalAlignment(SwingConstants.CENTER);
		textStatus.setText("Open");
		textStatus.setEditable(false);
		textStatus.setColumns(10);
		
		textOccupancy = new JTextField();
		textOccupancy.setBounds(220, 29, 134, 28);
		updatePanel.add(textOccupancy);
		textOccupancy.setText("True");
		textOccupancy.setHorizontalAlignment(SwingConstants.CENTER);
		textOccupancy.setEditable(false);
		textOccupancy.setColumns(10);
		
		JPanel switchPanel = new JPanel();
		switchPanel.setBorder(null);
		switchPanel.setBounds(131, 220, 240, 66);
		trackInfoPanel.add(switchPanel);
		switchPanel.setLayout(null);
		
		JRadioButton switchButtonTop = new JRadioButton("B1");
		switchButtonTop.setBounds(153, 6, 80, 28);
		switchPanel.add(switchButtonTop);
		switchButtonTop.setHorizontalAlignment(SwingConstants.LEFT);
		
		JRadioButton switchButtonBottom = new JRadioButton("F13");
		switchButtonBottom.setBounds(153, 35, 80, 28);
		switchPanel.add(switchButtonBottom);
		switchButtonBottom.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel labelSwitchGraphic = new JLabel("");
		labelSwitchGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/switch.png"));
		labelSwitchGraphic.setBounds(100, 17, 55, 33);
		switchPanel.add(labelSwitchGraphic);
		
		JLabel labelSwitchState = new JLabel("Switch State");
		labelSwitchState.setBounds(0, 6, 88, 57);
		switchPanel.add(labelSwitchState);
		labelSwitchState.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JPanel lightsPanel = new JPanel();
		lightsPanel.setLayout(null);
		lightsPanel.setBorder(null);
		lightsPanel.setBounds(131, 103, 240, 123);
		trackInfoPanel.add(lightsPanel);
		
		JRadioButton radioRightCrossing = new JRadioButton("");
		radioRightCrossing.setHorizontalAlignment(SwingConstants.LEFT);
		radioRightCrossing.setBounds(164, 89, 68, 28);
		lightsPanel.add(radioRightCrossing);
		
		JLabel labelLightGraphic = new JLabel("");
		labelLightGraphic.setBounds(151, 0, 35, 57);
		lightsPanel.add(labelLightGraphic);
		labelLightGraphic.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/light-green.png"));
		
		JLabel labelLights = new JLabel("Lights");
		labelLights.setHorizontalAlignment(SwingConstants.TRAILING);
		labelLights.setBounds(0, 0, 88, 57);
		lightsPanel.add(labelLights);
		
		JLabel labelCrossing = new JLabel("Crossing");
		labelCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		labelCrossing.setBounds(0, 60, 88, 57);
		lightsPanel.add(labelCrossing);
		
		JRadioButton radioLeftCrossing = new JRadioButton("");
		radioLeftCrossing.setHorizontalAlignment(SwingConstants.RIGHT);
		radioLeftCrossing.setBounds(103, 89, 68, 28);
		lightsPanel.add(radioLeftCrossing);
		
		JLabel labelX = new JLabel("X");
		labelX.setForeground(Color.LIGHT_GRAY);
		labelX.setFont(new Font("Helvetica", Font.BOLD, 40));
		labelX.setHorizontalAlignment(SwingConstants.CENTER);
		labelX.setBounds(100, 60, 134, 44);
		lightsPanel.add(labelX);
		
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(0, 91, 502, 21);
		trackInfoPanel.add(separator3);
		
		JButton buttonUpdate = new JButton("Update");
		buttonUpdate.setBounds(385, 298, 117, 29);
		trackInfoPanel.add(buttonUpdate);
		
		JButton buttonImportPlc = new JButton("Import PLC");
		buttonImportPlc.setBounds(256, 298, 117, 29);
		trackInfoPanel.add(buttonImportPlc);
		
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(0, 282, 502, 21);
		trackInfoPanel.add(separator4);
		
		JLabel labelTrackImg = new JLabel("");
		labelTrackImg.setBackground(Color.WHITE);
		labelTrackImg.setBounds(0, 0, 334, 448);
		panel.add(labelTrackImg);
		labelTrackImg.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/track.png"));
		
		JLabel labelTrackControllerInterface = new JLabel("<html><b>Track Controller Interface</b><html>");
		labelTrackControllerInterface.setBounds(346, 0, 502, 54);
		panel.add(labelTrackControllerInterface);
		labelTrackControllerInterface.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelTrackControllerInterface.setHorizontalAlignment(SwingConstants.CENTER);
		
	}
}
