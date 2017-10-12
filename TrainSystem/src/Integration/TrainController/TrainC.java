import java.awt.EventQueue;

import javax.swing.UIManager; 
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

public class TrainC {

	private JFrame frame;
	private JTextField speedField;
	private JTextField accelField;
	private JTextField authField;
	private JTextField newSpeedField;
	private JTextField powerField;
	private JTextField nextDistField;
	private JTextField nextStationField;
	private JTextField tempField;
	
	private JRadioButton serviceOn;
	private JRadioButton serviceOff;
	
	private double meanAccel = 0.5;
	private double emptyMass = 40900;
	private double g = 9.806;
	private double mu = .16;
	private double theta = 0;
	
	private JLabel logoPineapple = new JLabel(new ImageIcon("pineapple_icon.png"));

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setUILookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainC window = new TrainC();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrainC() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Train Controller");
		frame.setBounds(100, 100, 717, 570);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{123, 292, 263, 0};
		gridBagLayout.rowHeights = new int[]{348, 81, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblTrainList = new JLabel("Train List");
		lblTrainList.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblTrainList = new GridBagConstraints();
		gbc_lblTrainList.anchor = GridBagConstraints.WEST;
		gbc_lblTrainList.insets = new Insets(0, 0, 5, 0);
		gbc_lblTrainList.gridx = 0;
		gbc_lblTrainList.gridy = 0;
		panel.add(lblTrainList, gbc_lblTrainList);
		
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("Train 1");
		
		JList list = new JList(listModel);

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		panel.add(list, gbc_list);

		list.setSelectedIndex(0);
		
		JPanel speedPanel = new JPanel();
		GridBagConstraints gbc_speedPanel = new GridBagConstraints();
		gbc_speedPanel.fill = GridBagConstraints.BOTH;
		gbc_speedPanel.insets = new Insets(0, 0, 5, 5);
		gbc_speedPanel.gridx = 1;
		gbc_speedPanel.gridy = 0;
		frame.getContentPane().add(speedPanel, gbc_speedPanel);
		GridBagLayout gbl_panel_1 = new GridBagLayout();

		gbl_panel_1.columnWidths = new int[]{16, 174, 62, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		speedPanel.setLayout(gbl_panel_1);
		
		JLabel lblSpeedControl = new JLabel("Speed Control");
		lblSpeedControl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		speedPanel.add(lblSpeedControl, gbc_lblNewLabel_1);
		
		JLabel lblCurrentSpeed = new JLabel("Current Speed");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		speedPanel.add(lblCurrentSpeed, gbc_lblNewLabel);
		
		speedField = new JTextField();
		speedField.setEditable(false);
		GridBagConstraints gbc_speedField = new GridBagConstraints();
		gbc_speedField.fill = GridBagConstraints.HORIZONTAL;
		gbc_speedField.insets = new Insets(0, 0, 5, 5);
		gbc_speedField.gridx = 1;
		gbc_speedField.gridy = 2;
		speedPanel.add(speedField, gbc_speedField);
		speedField.setColumns(10);

		speedField.setText("10");
		
		JLabel label = new JLabel("mi/hr");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 2;
		gbc_label.gridy = 2;
		speedPanel.add(label, gbc_label);
		
		JLabel lblAcceleration = new JLabel("Acceleration");
		GridBagConstraints gbc_lblAcceleration = new GridBagConstraints();
		gbc_lblAcceleration.anchor = GridBagConstraints.WEST;
		gbc_lblAcceleration.insets = new Insets(0, 0, 5, 5);
		gbc_lblAcceleration.gridx = 1;
		gbc_lblAcceleration.gridy = 3;
		speedPanel.add(lblAcceleration, gbc_lblAcceleration);
		
		accelField = new JTextField();
		accelField.setEditable(false);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 4;
		speedPanel.add(accelField, gbc_textField_1);
		accelField.setColumns(10);

		accelField.setText("0");
		
		JLabel lblMihr = new JLabel("mi/hr^2");
		GridBagConstraints gbc_lblMihr = new GridBagConstraints();
		gbc_lblMihr.anchor = GridBagConstraints.WEST;
		gbc_lblMihr.insets = new Insets(0, 0, 5, 0);
		gbc_lblMihr.gridx = 2;
		gbc_lblMihr.gridy = 4;
		speedPanel.add(lblMihr, gbc_lblMihr);
		
		JLabel lblAuthority = new JLabel("Authority");
		GridBagConstraints gbc_lblAuthority = new GridBagConstraints();
		gbc_lblAuthority.anchor = GridBagConstraints.WEST;
		gbc_lblAuthority.insets = new Insets(0, 0, 5, 5);
		gbc_lblAuthority.gridx = 1;
		gbc_lblAuthority.gridy = 5;
		speedPanel.add(lblAuthority, gbc_lblAuthority);
		
		authField = new JTextField();
		authField.setEditable(false);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 6;
		speedPanel.add(authField, gbc_textField_2);
		authField.setColumns(10);

		authField.setText("100");
		
		JLabel lblMi = new JLabel("mi");
		GridBagConstraints gbc_lblMi = new GridBagConstraints();
		gbc_lblMi.anchor = GridBagConstraints.WEST;
		gbc_lblMi.insets = new Insets(0, 0, 5, 0);
		gbc_lblMi.gridx = 2;
		gbc_lblMi.gridy = 6;
		speedPanel.add(lblMi, gbc_lblMi);
		
		JLabel lblNewSpeed = new JLabel("New Speed");
		GridBagConstraints gbc_lblNewSpeed = new GridBagConstraints();
		gbc_lblNewSpeed.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewSpeed.anchor = GridBagConstraints.WEST;
		gbc_lblNewSpeed.gridx = 1;
		gbc_lblNewSpeed.gridy = 7;
		speedPanel.add(lblNewSpeed, gbc_lblNewSpeed);
		
		newSpeedField = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 8;
		speedPanel.add(newSpeedField, gbc_textField_3);
		newSpeedField.setColumns(10);
		
		JButton btnSetNewSpeed = new JButton("Set New Speed");
		stylizeButton(btnSetNewSpeed);
		btnSetNewSpeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String strNewSpeed = newSpeedField.getText();
				double newSpeed = Double.parseDouble(strNewSpeed);
				String strPow = calcPower(newSpeed);
				powerField.setText(strPow);
				newSpeedField.setText("");
			}
		});
		
		JLabel lblMph = new JLabel("mi/hr");
		GridBagConstraints gbc_lblMph = new GridBagConstraints();
		gbc_lblMph.anchor = GridBagConstraints.WEST;
		gbc_lblMph.insets = new Insets(0, 0, 5, 0);
		gbc_lblMph.gridx = 2;
		gbc_lblMph.gridy = 8;
		speedPanel.add(lblMph, gbc_lblMph);
		
		GridBagConstraints gbc_btnSetNewSpeed = new GridBagConstraints();
		gbc_btnSetNewSpeed.insets = new Insets(0, 0, 5, 5);
		gbc_btnSetNewSpeed.gridx = 1;
		gbc_btnSetNewSpeed.gridy = 9;
		speedPanel.add(btnSetNewSpeed, gbc_btnSetNewSpeed);
		
		JLabel lblPowerOutput = new JLabel("Power Output");
		GridBagConstraints gbc_lblPowerOutput = new GridBagConstraints();
		gbc_lblPowerOutput.insets = new Insets(0, 0, 5, 5);
		gbc_lblPowerOutput.anchor = GridBagConstraints.WEST;
		gbc_lblPowerOutput.gridx = 1;
		gbc_lblPowerOutput.gridy = 10;
		speedPanel.add(lblPowerOutput, gbc_lblPowerOutput);
		
		powerField = new JTextField();
		powerField.setEditable(false);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 11;
		speedPanel.add(powerField, gbc_textField_4);
		powerField.setColumns(10);

		powerField.setText("0");
		
		JLabel lblKw = new JLabel("kW");
		GridBagConstraints gbc_lblKw = new GridBagConstraints();
		gbc_lblKw.anchor = GridBagConstraints.WEST;
		gbc_lblKw.insets = new Insets(0, 0, 5, 0);
		gbc_lblKw.gridx = 2;
		gbc_lblKw.gridy = 11;
		speedPanel.add(lblKw, gbc_lblKw);
		
		
		
		JPanel doorLightPanel = new JPanel();
		GridBagConstraints gbc_doorLightPanel = new GridBagConstraints();
		gbc_doorLightPanel.fill = GridBagConstraints.BOTH;
		gbc_doorLightPanel.insets = new Insets(0, 0, 5, 0);
		gbc_doorLightPanel.gridx = 2;
		gbc_doorLightPanel.gridy = 0;
		frame.getContentPane().add(doorLightPanel, gbc_doorLightPanel);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{137, 103, 0};

		gbl_panel_4.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		doorLightPanel.setLayout(gbl_panel_4);
		
		JLabel lblDoorControl = new JLabel("Door Control");
		lblDoorControl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblDoorControl = new GridBagConstraints();
		gbc_lblDoorControl.anchor = GridBagConstraints.WEST;
		gbc_lblDoorControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblDoorControl.gridx = 0;
		gbc_lblDoorControl.gridy = 0;
		doorLightPanel.add(lblDoorControl, gbc_lblDoorControl);
		
		JLabel lblLeftDoors = new JLabel("Left Doors");
		GridBagConstraints gbc_lblLeftDoors = new GridBagConstraints();
		gbc_lblLeftDoors.anchor = GridBagConstraints.WEST;
		gbc_lblLeftDoors.insets = new Insets(0, 0, 5, 5);
		gbc_lblLeftDoors.gridx = 0;
		gbc_lblLeftDoors.gridy = 1;
		doorLightPanel.add(lblLeftDoors, gbc_lblLeftDoors);
		
		JRadioButton leftOpen = new JRadioButton("OPEN");
		GridBagConstraints gbc_rdbtnOpen = new GridBagConstraints();
		gbc_rdbtnOpen.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOpen.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOpen.gridx = 0;
		gbc_rdbtnOpen.gridy = 2;
		doorLightPanel.add(leftOpen, gbc_rdbtnOpen);

		leftOpen.setSelected(true);
		
		JRadioButton leftClose = new JRadioButton("CLOSE");
		GridBagConstraints gbc_rdbtnClose = new GridBagConstraints();
		gbc_rdbtnClose.anchor = GridBagConstraints.WEST;
		gbc_rdbtnClose.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnClose.gridx = 1;
		gbc_rdbtnClose.gridy = 2;
		doorLightPanel.add(leftClose, gbc_rdbtnClose);

		leftClose.setSelected(false);
		
		leftOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				leftOpen.setSelected(true);
				leftClose.setSelected(false);
			}
		});
		leftClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				leftClose.setSelected(true);
				leftOpen.setSelected(false);
			}
		});
		
		JLabel lblRightDoors = new JLabel("Right Doors");
		GridBagConstraints gbc_lblRightDoors = new GridBagConstraints();
		gbc_lblRightDoors.anchor = GridBagConstraints.WEST;
		gbc_lblRightDoors.insets = new Insets(0, 0, 5, 5);
		gbc_lblRightDoors.gridx = 0;
		gbc_lblRightDoors.gridy = 3;
		doorLightPanel.add(lblRightDoors, gbc_lblRightDoors);
		
		JRadioButton rightOpen = new JRadioButton("OPEN");
		GridBagConstraints gbc_rdbtnOpen_1 = new GridBagConstraints();
		gbc_rdbtnOpen_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOpen_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOpen_1.gridx = 0;
		gbc_rdbtnOpen_1.gridy = 4;
		doorLightPanel.add(rightOpen, gbc_rdbtnOpen_1);
		rightOpen.setSelected(false);
		
		JRadioButton rightClose = new JRadioButton("CLOSE");
		GridBagConstraints gbc_rdbtnClose_1 = new GridBagConstraints();
		gbc_rdbtnClose_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnClose_1.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnClose_1.gridx = 1;
		gbc_rdbtnClose_1.gridy = 4;
		doorLightPanel.add(rightClose, gbc_rdbtnClose_1);
		rightClose.setSelected(true);
		
		rightOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rightOpen.setSelected(true);
				rightClose.setSelected(false);
			}
		});
		rightClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rightClose.setSelected(true);
				rightOpen.setSelected(false);
			}
		});
		
		JLabel lblLightControl = new JLabel("Light Control");
		lblLightControl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblLightControl = new GridBagConstraints();
		gbc_lblLightControl.anchor = GridBagConstraints.WEST;
		gbc_lblLightControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblLightControl.gridx = 0;
		gbc_lblLightControl.gridy = 6;
		doorLightPanel.add(lblLightControl, gbc_lblLightControl);
		
		JRadioButton lightOn = new JRadioButton("ON");
		GridBagConstraints gbc_rdbtnOn = new GridBagConstraints();
		gbc_rdbtnOn.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOn.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOn.gridx = 0;
		gbc_rdbtnOn.gridy = 7;
		doorLightPanel.add(lightOn, gbc_rdbtnOn);
		lightOn.setSelected(false);
		
		JRadioButton lightOff = new JRadioButton("OFF");
		GridBagConstraints gbc_rdbtnOff = new GridBagConstraints();
		gbc_rdbtnOff.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnOff.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOff.gridx = 1;
		gbc_rdbtnOff.gridy = 7;
		doorLightPanel.add(lightOff, gbc_rdbtnOff);
		lightOff.setSelected(true);
		
		JLabel lblTemperatureControl = new JLabel("Temperature");
		lblTemperatureControl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblTemperatureControl = new GridBagConstraints();
		gbc_lblTemperatureControl.anchor = GridBagConstraints.WEST;
		gbc_lblTemperatureControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblTemperatureControl.gridx = 0;
		gbc_lblTemperatureControl.gridy = 9;
		doorLightPanel.add(lblTemperatureControl, gbc_lblTemperatureControl);
		
		tempField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();

		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 10;
		doorLightPanel.add(tempField, gbc_textField);
		tempField.setColumns(10);

		tempField.setText("70");
		
		JLabel lblF = new JLabel("F");
		GridBagConstraints gbc_lblF = new GridBagConstraints();
		gbc_lblF.anchor = GridBagConstraints.WEST;
		gbc_lblF.insets = new Insets(0, 0, 5, 0);
		gbc_lblF.gridx = 1;
		gbc_lblF.gridy = 10;
		doorLightPanel.add(lblF, gbc_lblF);
		
		JButton btnSet = new JButton("Set");
		stylizeButton(btnSet);
		GridBagConstraints gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 0, 5);
		gbc_btnSet.gridx = 0;
		gbc_btnSet.gridy = 11;
		doorLightPanel.add(btnSet, gbc_btnSet);
		
		lightOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lightOff.setSelected(true);
				lightOn.setSelected(false);
			}
		});
		lightOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lightOn.setSelected(true);
				lightOff.setSelected(false);
			}
		});
		
		JPanel modePanel = new JPanel();
		GridBagConstraints gbc_modePanel = new GridBagConstraints();
		gbc_modePanel.fill = GridBagConstraints.BOTH;
		gbc_modePanel.insets = new Insets(0, 0, 0, 5);
		gbc_modePanel.gridx = 0;
		gbc_modePanel.gridy = 1;
		frame.getContentPane().add(modePanel, gbc_modePanel);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{67, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		modePanel.setLayout(gbl_panel_2);
		
		JLabel lblMode = new JLabel("MODE");
		lblMode.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblMode = new GridBagConstraints();
		gbc_lblMode.anchor = GridBagConstraints.WEST;
		gbc_lblMode.insets = new Insets(0, 0, 5, 0);
		gbc_lblMode.gridx = 0;
		gbc_lblMode.gridy = 0;
		modePanel.add(lblMode, gbc_lblMode);
		
		JRadioButton modeManual = new JRadioButton("MANUAL");
		GridBagConstraints gbc_rdbtnManual = new GridBagConstraints();
		gbc_rdbtnManual.anchor = GridBagConstraints.WEST;
		gbc_rdbtnManual.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnManual.gridx = 0;
		gbc_rdbtnManual.gridy = 1;
		modePanel.add(modeManual, gbc_rdbtnManual);

		modeManual.setSelected(true);
		
		JRadioButton modeAuto = new JRadioButton("AUTO");
		GridBagConstraints gbc_rdbtnAuto = new GridBagConstraints();
		gbc_rdbtnAuto.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAuto.gridx = 0;
		gbc_rdbtnAuto.gridy = 2;
		modePanel.add(modeAuto, gbc_rdbtnAuto);

		modeAuto.setSelected(false);
		
		modeManual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeManual.setSelected(true);
				modeAuto.setSelected(false);
			}
		});
		modeAuto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeAuto.setSelected(true);
				modeManual.setSelected(false);
			}
		});
		
		JPanel brakePanel = new JPanel();
		GridBagConstraints gbc_brakePanel = new GridBagConstraints();
		gbc_brakePanel.fill = GridBagConstraints.BOTH;
		gbc_brakePanel.insets = new Insets(0, 0, 0, 5);
		gbc_brakePanel.gridx = 1;
		gbc_brakePanel.gridy = 1;
		frame.getContentPane().add(brakePanel, gbc_brakePanel);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{138, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		brakePanel.setLayout(gbl_panel_3);
		
		JLabel lblBrakeControl = new JLabel("Brake Control");
		lblBrakeControl.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblBrakeControl = new GridBagConstraints();
		gbc_lblBrakeControl.anchor = GridBagConstraints.WEST;
		gbc_lblBrakeControl.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrakeControl.gridx = 0;
		gbc_lblBrakeControl.gridy = 0;
		brakePanel.add(lblBrakeControl, gbc_lblBrakeControl);
		

		serviceOn = new JRadioButton("Service Brake On");
		GridBagConstraints gbc_rdbtnServiceBrakeOn = new GridBagConstraints();
		gbc_rdbtnServiceBrakeOn.anchor = GridBagConstraints.WEST;
		gbc_rdbtnServiceBrakeOn.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnServiceBrakeOn.gridx = 0;
		gbc_rdbtnServiceBrakeOn.gridy = 1;
		brakePanel.add(serviceOn, gbc_rdbtnServiceBrakeOn);
		serviceOn.setSelected(false);
		

		serviceOff = new JRadioButton("Service Brake Off");
		GridBagConstraints gbc_rdbtnServiceBrakeOff = new GridBagConstraints();
		gbc_rdbtnServiceBrakeOff.anchor = GridBagConstraints.WEST;
		gbc_rdbtnServiceBrakeOff.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnServiceBrakeOff.gridx = 1;
		gbc_rdbtnServiceBrakeOff.gridy = 1;
		brakePanel.add(serviceOff, gbc_rdbtnServiceBrakeOff);
		serviceOff.setSelected(true);
		
		serviceOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				serviceOn.setSelected(true);
				serviceOff.setSelected(false);
			}
		});
		serviceOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				serviceOff.setSelected(true);
				serviceOn.setSelected(false);
			}
		});
		
		JRadioButton emergencyOn = new JRadioButton("Emergency Brake On");
		GridBagConstraints gbc_rdbtnEmergencyBrakeOn = new GridBagConstraints();
		gbc_rdbtnEmergencyBrakeOn.anchor = GridBagConstraints.WEST;
		gbc_rdbtnEmergencyBrakeOn.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnEmergencyBrakeOn.gridx = 0;
		gbc_rdbtnEmergencyBrakeOn.gridy = 2;
		brakePanel.add(emergencyOn, gbc_rdbtnEmergencyBrakeOn);
		emergencyOn.setSelected(false);
		
		JRadioButton emergencyOff = new JRadioButton("Emergency Brake Off");
		GridBagConstraints gbc_rdbtnEmergencyBrakeOff = new GridBagConstraints();
		gbc_rdbtnEmergencyBrakeOff.gridx = 1;
		gbc_rdbtnEmergencyBrakeOff.gridy = 2;
		brakePanel.add(emergencyOff, gbc_rdbtnEmergencyBrakeOff);
		emergencyOff.setSelected(true);
		
		emergencyOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				emergencyOn.setSelected(true);
				emergencyOff.setSelected(false);
			}
		});
		emergencyOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				emergencyOff.setSelected(true);
				emergencyOn.setSelected(false);
			}
		});
		
		JPanel stationPanel = new JPanel();
		GridBagConstraints gbc_stationPanel = new GridBagConstraints();
		gbc_stationPanel.fill = GridBagConstraints.BOTH;
		gbc_stationPanel.gridx = 2;
		gbc_stationPanel.gridy = 1;
		frame.getContentPane().add(stationPanel, gbc_stationPanel);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{118, 114, 0};
		gbl_panel_5.rowHeights = new int[]{0, 23, 19, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		stationPanel.setLayout(gbl_panel_5);
		
		JLabel lblNextStation = new JLabel("Next Station");
		lblNextStation.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		GridBagConstraints gbc_lblNextStation = new GridBagConstraints();
		gbc_lblNextStation.anchor = GridBagConstraints.WEST;
		gbc_lblNextStation.insets = new Insets(0, 0, 5, 5);
		gbc_lblNextStation.gridx = 0;
		gbc_lblNextStation.gridy = 0;
		stationPanel.add(lblNextStation, gbc_lblNextStation);
		
		nextStationField = new JTextField();
		nextStationField.setEditable(false);
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 1;
		stationPanel.add(nextStationField, gbc_textField_5);
		nextStationField.setColumns(10);

		nextStationField.setText("Pioneer");
		
		JLabel lblDistanceTo = new JLabel("Distance to:");
		GridBagConstraints gbc_lblDistanceTo = new GridBagConstraints();
		gbc_lblDistanceTo.anchor = GridBagConstraints.EAST;
		gbc_lblDistanceTo.insets = new Insets(0, 0, 0, 5);
		gbc_lblDistanceTo.gridx = 0;
		gbc_lblDistanceTo.gridy = 2;
		stationPanel.add(lblDistanceTo, gbc_lblDistanceTo);
		
		nextDistField = new JTextField();
		nextDistField.setEditable(false);
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 2;
		stationPanel.add(nextDistField, gbc_textField_6);
		nextDistField.setColumns(10);
		nextDistField.setText("N/A");

		nextDistField.setText("0 mi");

		GridBagConstraints gbc_logo = new GridBagConstraints();
		gbc_logo.gridx = 2;
		gbc_logo.gridy = 2;
		frame.getContentPane().add(logoPineapple, gbc_logo);

	}
	
	private String calcPower(double newSpeed) {
		double currSpeed = Double.parseDouble(speedField.getText());
		String strPow;
		double a;
		double Pow;
		DecimalFormat df = new DecimalFormat("#.####");
		if (newSpeed > currSpeed) {
			deactivateService();
                	newSpeed = newSpeed * 0.44704;
			Pow = newSpeed * ((meanAccel * emptyMass) + (emptyMass * mu * g * Math.cos(theta)));
			strPow = df.format(Pow/1000) + "";
			a = .5 * 8052.9706;
			accelField.setText(df.format(a) + "");
		}
		else if (newSpeed == currSpeed) {
			deactivateService();
	                newSpeed = newSpeed * 0.44704;
			Pow = newSpeed * emptyMass * Math.cos(theta) * g * mu;
			strPow = df.format(Pow/1000) + "";
			a = 0;
			accelField.setText(df.format(a) + "");
		}
		else {
			strPow = "0";
			activateService();
			a = -(1.2 + (Math.cos(theta) * mu * g));
			a = a *8052.9706;
			accelField.setText(df.format(a) + "");
		}
		return strPow;
	}
	
	private void activateService() {
		serviceOn.setSelected(true);
		serviceOff.setSelected(false);
	}
	
	private void deactivateService() {
		serviceOn.setSelected(false);
		serviceOff.setSelected(true);
	}

	public void stylizeButton(JButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
    		b.setBorder(thickBorder);
		b.setContentAreaFilled(false);
		b.setOpaque(true);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}

	public static void setUILookAndFeel(){
		try  {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
