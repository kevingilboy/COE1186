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
	private JTextField txtMph;
	private JTextField txtShadyside;

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
		
		JLabel lblControllerRf = new JLabel("<html><b>Controller - GA</b><html>");
		lblControllerRf.setBounds(0, -1, 502, 32);
		panel_2.add(lblControllerRf);
		lblControllerRf.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(10, 32, 487, 59);
		panel_2.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Status");
		lblNewLabel_1.setBounds(136, 0, 73, 28);
		panel_6.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel lblNewLabel_2 = new JLabel("Occupancy");
		lblNewLabel_2.setBounds(135, 29, 73, 28);
		panel_6.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		
		txtMph = new JTextField();
		txtMph.setBounds(220, 0, 134, 28);
		panel_6.add(txtMph);
		txtMph.setHorizontalAlignment(SwingConstants.CENTER);
		txtMph.setText("Open");
		txtMph.setEditable(false);
		txtMph.setColumns(10);
		
		txtShadyside = new JTextField();
		txtShadyside.setBounds(220, 29, 134, 28);
		panel_6.add(txtShadyside);
		txtShadyside.setText("True");
		txtShadyside.setHorizontalAlignment(SwingConstants.CENTER);
		txtShadyside.setEditable(false);
		txtShadyside.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		panel_3.setBounds(131, 220, 240, 66);
		panel_2.add(panel_3);
		panel_3.setLayout(null);
		
		JRadioButton rdbtnB = new JRadioButton("B1");
		rdbtnB.setBounds(153, 6, 80, 28);
		panel_3.add(rdbtnB);
		rdbtnB.setHorizontalAlignment(SwingConstants.LEFT);
		
		JRadioButton rdbtnF = new JRadioButton("F13");
		rdbtnF.setBounds(153, 35, 80, 28);
		panel_3.add(rdbtnF);
		rdbtnF.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/switch.png"));
		lblNewLabel_3.setBounds(100, 17, 55, 33);
		panel_3.add(lblNewLabel_3);
		
		JLabel lblSwitch = new JLabel("Switch State");
		lblSwitch.setBounds(0, 6, 88, 57);
		panel_3.add(lblSwitch);
		lblSwitch.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(null);
		panel_4.setBounds(131, 103, 240, 123);
		panel_2.add(panel_4);
		
		JRadioButton radioButton_6 = new JRadioButton("");
		radioButton_6.setHorizontalAlignment(SwingConstants.LEFT);
		radioButton_6.setBounds(164, 89, 68, 28);
		panel_4.add(radioButton_6);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(null);
		panel_5.setBounds(151, 0, 35, 57);
		panel_4.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/light-green.png"));
		label.setBounds(0, 0, 35, 57);
		panel_5.add(label);
		
		JLabel lblLights = new JLabel("Lights");
		lblLights.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLights.setBounds(0, 0, 88, 57);
		panel_4.add(lblLights);
		
		JLabel lblCrossing = new JLabel("Crossing");
		lblCrossing.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCrossing.setBounds(0, 60, 88, 57);
		panel_4.add(lblCrossing);
		
		JRadioButton radioButton_3 = new JRadioButton("");
		radioButton_3.setHorizontalAlignment(SwingConstants.RIGHT);
		radioButton_3.setBounds(103, 89, 68, 28);
		panel_4.add(radioButton_3);
		
		JLabel lblX = new JLabel("X");
		lblX.setForeground(Color.LIGHT_GRAY);
		lblX.setFont(new Font("Helvetica", Font.BOLD, 40));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setBounds(100, 60, 134, 44);
		panel_4.add(lblX);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 91, 502, 21);
		panel_2.add(separator_2);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(385, 298, 117, 29);
		panel_2.add(btnUpdate);
		
		JButton btnImportPlc = new JButton("Import PLC");
		btnImportPlc.setBounds(256, 298, 117, 29);
		panel_2.add(btnImportPlc);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(0, 282, 502, 21);
		panel_2.add(separator_3);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(0, 0, 334, 448);
		panel.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon("/Users/npetro/Documents/Github/COE1186/TrainSystem/src/SubSystemModules/TrackController/track.png"));
		
		JLabel lblTrackController = new JLabel("<html><b>Track Controller Interface</b><html>");
		lblTrackController.setBounds(346, 0, 502, 54);
		panel.add(lblTrackController);
		lblTrackController.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblTrackController.setHorizontalAlignment(SwingConstants.CENTER);
		
	}
}
