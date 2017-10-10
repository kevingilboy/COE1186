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
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;

public class TrackControllerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtMph;
	private JTextField txtShadyside;
	private JTextField txtOpen;

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
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		panel_1.setBounds(346, 55, 502, 67);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 0, 502, 12);
		panel_1.add(separator);
		
		JLabel lblBlock = new JLabel("Block");
		lblBlock.setBounds(338, 23, 34, 16);
		panel_1.add(lblBlock);
		
		JLabel lblSection = new JLabel("Section");
		lblSection.setBounds(164, 23, 46, 16);
		panel_1.add(lblSection);
		
		JLabel lblLine = new JLabel("Line");
		lblLine.setBounds(10, 23, 26, 16);
		panel_1.add(lblLine);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 54, 502, 12);
		panel_1.add(separator_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(384, 19, 104, 27);
		panel_1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"2"}));
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(222, 19, 104, 27);
		panel_1.add(comboBox_1);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"A"}));
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(48, 19, 104, 27);
		panel_1.add(comboBox_2);
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"Green"}));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(null);
		panel_2.setBounds(346, 119, 502, 329);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblControllerRf = new JLabel("Controller - RF");
		lblControllerRf.setBounds(0, 4, 502, 28);
		panel_2.add(lblControllerRf);
		lblControllerRf.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(9, 40, 487, 88);
		panel_2.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Speed Limit");
		lblNewLabel_1.setBounds(136, 0, 73, 28);
		panel_6.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel lblNewLabel_2 = new JLabel("Station");
		lblNewLabel_2.setBounds(135, 29, 73, 28);
		panel_6.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(135, 60, 73, 28);
		panel_6.add(lblStatus);
		lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		
		txtMph = new JTextField();
		txtMph.setBounds(220, 0, 134, 28);
		panel_6.add(txtMph);
		txtMph.setHorizontalAlignment(SwingConstants.CENTER);
		txtMph.setText("70 MPH");
		txtMph.setEditable(false);
		txtMph.setColumns(10);
		
		txtShadyside = new JTextField();
		txtShadyside.setBounds(220, 29, 134, 28);
		panel_6.add(txtShadyside);
		txtShadyside.setText("Pioneer");
		txtShadyside.setHorizontalAlignment(SwingConstants.CENTER);
		txtShadyside.setEditable(false);
		txtShadyside.setColumns(10);
		
		txtOpen = new JTextField();
		txtOpen.setBounds(220, 60, 134, 28);
		panel_6.add(txtOpen);
		txtOpen.setText("Open");
		txtOpen.setHorizontalAlignment(SwingConstants.CENTER);
		txtOpen.setEditable(false);
		txtOpen.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		panel_3.setBounds(10, 163, 240, 123);
		panel_2.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblOccupancy = new JLabel("Occupancy");
		lblOccupancy.setBounds(0, 0, 88, 28);
		panel_3.add(lblOccupancy);
		lblOccupancy.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("");
		rdbtnNewRadioButton.setBounds(100, 0, 134, 28);
		panel_3.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblHeaters = new JLabel("Heaters");
		lblHeaters.setBounds(0, 29, 88, 28);
		panel_3.add(lblHeaters);
		lblHeaters.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JRadioButton radioButton = new JRadioButton("");
		radioButton.setBounds(100, 29, 134, 28);
		panel_3.add(radioButton);
		radioButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		JRadioButton radioButton_1 = new JRadioButton("");
		radioButton_1.setBounds(153, 57, 80, 28);
		panel_3.add(radioButton_1);
		radioButton_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		JRadioButton radioButton_2 = new JRadioButton("");
		radioButton_2.setBounds(153, 86, 80, 28);
		panel_3.add(radioButton_2);
		radioButton_2.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblNewLabel_3 = new JLabel("-");
		lblNewLabel_3.setBounds(100, 57, 55, 57);
		panel_3.add(lblNewLabel_3);
		
		JLabel lblSwitch = new JLabel("Switch State");
		lblSwitch.setBounds(0, 57, 88, 57);
		panel_3.add(lblSwitch);
		lblSwitch.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(null);
		panel_4.setBounds(256, 163, 240, 123);
		panel_2.add(panel_4);
		
		JRadioButton radioButton_6 = new JRadioButton("");
		radioButton_6.setHorizontalAlignment(SwingConstants.LEFT);
		radioButton_6.setBounds(164, 86, 68, 28);
		panel_4.add(radioButton_6);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		panel_5.setBounds(151, 0, 35, 57);
		panel_4.add(panel_5);
		panel_5.setLayout(null);
		
		JRadioButton radioButton_7 = new JRadioButton("");
		radioButton_7.setBounds(3, 0, 134, 28);
		panel_5.add(radioButton_7);
		radioButton_7.setHorizontalAlignment(SwingConstants.LEFT);
		
		JRadioButton radioButton_8 = new JRadioButton("");
		radioButton_8.setBounds(3, 29, 134, 28);
		panel_5.add(radioButton_8);
		radioButton_8.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblLights = new JLabel("Lights");
		lblLights.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLights.setBounds(0, 0, 88, 57);
		panel_4.add(lblLights);
		
		JLabel lblCrossing = new JLabel("Crossing");
		lblCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCrossing.setBounds(0, 57, 88, 57);
		panel_4.add(lblCrossing);
		
		JRadioButton radioButton_3 = new JRadioButton("");
		radioButton_3.setHorizontalAlignment(SwingConstants.RIGHT);
		radioButton_3.setBounds(103, 86, 68, 28);
		panel_4.add(radioButton_3);
		
		JLabel lblX = new JLabel("X");
		lblX.setForeground(Color.LIGHT_GRAY);
		lblX.setFont(new Font("Helvetica", Font.BOLD, 32));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setBounds(100, 57, 134, 46);
		panel_4.add(lblX);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 142, 490, 21);
		panel_2.add(separator_2);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(385, 298, 117, 29);
		panel_2.add(btnUpdate);
		
		JButton btnImportPlc = new JButton("Import PLC");
		btnImportPlc.setBounds(256, 298, 117, 29);
		panel_2.add(btnImportPlc);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(9, 282, 490, 21);
		panel_2.add(separator_3);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(0, 0, 334, 448);
		panel.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon("/Users/npetro/Desktop/track.png"));
		
		JLabel lblTrackController = new JLabel("Track Controller Interface");
		lblTrackController.setBounds(346, 0, 502, 54);
		panel.add(lblTrackController);
		lblTrackController.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblTrackController.setHorizontalAlignment(SwingConstants.CENTER);
		
	}
}
