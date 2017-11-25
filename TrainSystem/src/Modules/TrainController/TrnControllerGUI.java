//Michael Kotcher

package Modules.TrainController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class TrnControllerGUI {

	private JFrame frame;

	private JPanel contentPane;
	
	private JTextField newSpeedField;
	private JTextField tempField;
	
	private JButton speedSet;
	private JButton tempSet;
	private JButton eGUIButton;
	
	private JLabel speedValue;
	private JLabel setpointValue;
	private JLabel powerValue;
	private JLabel authorityValue;
	
	private JRadioButton modeManual;
	private JRadioButton modeAuto;
	private JRadioButton sBrakesOn;
	private JRadioButton sBrakesOff;
	private JRadioButton eBrakesOn;
	private JRadioButton eBrakesOff;
	private JRadioButton rightOpen;
	private JRadioButton rightClose;
	private JRadioButton leftOpen;
	private JRadioButton leftClose;
	private JRadioButton lightOn;
	private JRadioButton lightOff;
	
	private int mode;	
	private String trainID;
	private double speed;		//miles per hour
	private double setpoint;	//miles per hour
	private double authority;	//miles
	private double power;		//watts
	private int temperature;	//F
	private int driveMode;		//0 is auto, 1 is manual
	private boolean left;
	private boolean right;
	private boolean service;
	private boolean emergency;
	private boolean lights;
	
	private TrnController controller;
	
	private EngineerGUI eGUI;
	
	private PIController pi;
	
	public final double SPEEDCONVERSION = 2.23694;			//1 m/s = 2.23694 mph
	public final double DISTANCECONVERSION = 0.000621371;	//1 m = 0.000621371 miles
	
	/*public static void main(String[] args) {
		new TrnControllerGUI("Train 1");
	}*/
	
	public TrnControllerGUI(PIController p, TrnController c, String s) {
		trainID = s;
		driveMode = 0;
		speed = 0;
		temperature = 70;
		setpoint = 0;
		authority = 0;
		power = 0;
		left = false;
		right = false;
		service = false;
		emergency = false;
		lights = false;
		pi = p;
		controller = c;
		eGUI = new EngineerGUI(pi, trainID);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(100, 100, 500, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel idenLabel = new JLabel(s + "");
		idenLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		idenLabel.setBounds(20, 20, 130, 26);
		contentPane.add(idenLabel);
		
		JLabel speedLabel = new JLabel("Current Speed:");
		speedLabel.setBounds(20, 75, 99, 16);
		contentPane.add(speedLabel);
		
		speedValue = new JLabel(speed + " mi/hr");
		speedValue.setBounds(146, 75, 139, 16);
		contentPane.add(speedValue);
		
		JLabel newSpeedLabel = new JLabel("New Speed:");
		newSpeedLabel.setBounds(20, 114, 77, 16);
		contentPane.add(newSpeedLabel);
		
		newSpeedField = new JTextField();
		newSpeedField.setBounds(141, 109, 107, 26);
		contentPane.add(newSpeedField);
		newSpeedField.setColumns(10);
		newSpeedField.setText("");
		
		JLabel newSpeedUnitLabel = new JLabel("mi/hr");
		newSpeedUnitLabel.setBounds(251, 114, 61, 16);
		contentPane.add(newSpeedUnitLabel);
		
		speedSet = new JButton("Set New Speed");
		speedSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					try {
						double d;
						d = Double.parseDouble(newSpeedField.getText());
						setpointValue.setText(newSpeedField.getText() + " mi/hr");
						newSpeedField.setText("");
						setpoint = d;
						controller.setSetpointSpeed(setpoint / SPEEDCONVERSION);
					} 
					catch (NumberFormatException E) {
						newSpeedField.setText("");
					}
				}
			}
		});
		speedSet.setBounds(15, 142, 117, 29);
		contentPane.add(speedSet);
		
		JLabel setpointLabel = new JLabel("Setpoint Speed:");
		setpointLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		setpointLabel.setBounds(20, 183, 104, 16);
		contentPane.add(setpointLabel);
		
		setpointValue = new JLabel(setpoint + " mi/hr");
		setpointValue.setBounds(146, 183, 139, 16);
		contentPane.add(setpointValue);
		
		JLabel powerLabel = new JLabel("Power Output:");
		powerLabel.setBounds(20, 211, 99, 16);
		contentPane.add(powerLabel);
		
		powerValue = new JLabel(power + " W");
		powerValue.setBounds(146, 211, 139, 16);
		contentPane.add(powerValue);
		
		JLabel authorityLabel = new JLabel("Current Authority:");
		authorityLabel.setBounds(20, 238, 117, 16);
		contentPane.add(authorityLabel);
		
		authorityValue = new JLabel(authority + " mi");
		authorityValue.setBounds(146, 238, 139, 16);
		contentPane.add(authorityValue);
		
		JLabel temperatureLabel = new JLabel("Temperature:");
		temperatureLabel.setBounds(20, 266, 99, 16);
		contentPane.add(temperatureLabel);
		
		tempField = new JTextField();
		tempField.setBounds(141, 261, 107, 26);
		contentPane.add(tempField);
		tempField.setColumns(10);
		tempField.setText(temperature + "");
		
		JLabel tempUnitLabel = new JLabel("F");
		tempUnitLabel.setBounds(251, 266, 61, 16);
		contentPane.add(tempUnitLabel);
		
		tempSet = new JButton("Set");
		tempSet.setBounds(15, 294, 117, 29);
		tempSet.setEnabled(false);
		contentPane.add(tempSet);
		
		tempField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (driveMode == 1) {
					tempSet.setEnabled(true);
				}
			}
		});
		tempSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					try {
						int t = Integer.parseInt(tempField.getText());
						temperature = t;
						controller.setTemperature(temperature);
					} 
					catch (NumberFormatException E) {
						tempField.setText(temperature + "");
					}
					tempSet.setEnabled(false);
				}
			}
		});
		
		JLabel sBrakesLabel = new JLabel("Service Brakes");
		sBrakesLabel.setBounds(334, 78, 99, 16);
		contentPane.add(sBrakesLabel);
		
		sBrakesOn = new JRadioButton("On");
		sBrakesOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					sBrakesOn.setSelected(true);
					sBrakesOff.setSelected(false);
					service = true;
					controller.sBrakesOn();
				}
			}
		});
		sBrakesOn.setBounds(324, 92, 61, 23);
		sBrakesOn.setSelected(false);
		contentPane.add(sBrakesOn);
		
		sBrakesOff = new JRadioButton("Off");
		sBrakesOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					sBrakesOn.setSelected(false);
					sBrakesOff.setSelected(true);
					service = false;
					controller.sBrakesOff();
				}
			}
		});
		sBrakesOff.setBounds(404, 92, 61, 23);
		sBrakesOff.setSelected(true);
		contentPane.add(sBrakesOff);
		
		JLabel eBrakesLabel = new JLabel("Emergency Brakes");
		eBrakesLabel.setBounds(334, 129, 117, 16);
		contentPane.add(eBrakesLabel);
		
		eBrakesOn = new JRadioButton("On");
		eBrakesOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					eBrakesOn.setSelected(true);
					eBrakesOff.setSelected(false);
					emergency = true;
					controller.eBrakesOn();
				}
			}
		});
		eBrakesOn.setBounds(324, 143, 61, 23);
		eBrakesOn.setSelected(false);
		contentPane.add(eBrakesOn);
		
		eBrakesOff = new JRadioButton("Off");
		eBrakesOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					eBrakesOn.setSelected(false);
					eBrakesOff.setSelected(true);
					emergency = false;
					controller.eBrakesOff();
				}
			}
		});
		eBrakesOff.setBounds(404, 143, 61, 23);
		eBrakesOff.setSelected(true);
		contentPane.add(eBrakesOff);
		
		JLabel rightDoorLabel = new JLabel("Right Doors");
		rightDoorLabel.setBounds(334, 182, 99, 16);
		contentPane.add(rightDoorLabel);
		
		rightOpen = new JRadioButton("Open");
		rightOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					rightOpen.setSelected(true);
					rightClose.setSelected(false);
					right = true;
					controller.openRight();
				}
			}
		});
		rightOpen.setBounds(324, 196, 65, 23);
		rightOpen.setSelected(false);
		contentPane.add(rightOpen);
		
		rightClose = new JRadioButton("Close");
		rightClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					rightOpen.setSelected(false);
					rightClose.setSelected(true);
					right = false;
					controller.closeRight();
				}
			}
		});
		rightClose.setBounds(404, 196, 67, 23);
		rightClose.setSelected(true);
		contentPane.add(rightClose);
		
		JLabel leftDoorLabel = new JLabel("Left Doors");
		leftDoorLabel.setBounds(334, 231, 86, 16);
		contentPane.add(leftDoorLabel);
		
		leftOpen = new JRadioButton("Open");
		leftOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					leftOpen.setSelected(true);
					leftClose.setSelected(false);
					left = true;
					controller.openLeft();
				}
			}
		});
		leftOpen.setBounds(324, 247, 65, 23);
		leftOpen.setSelected(false);
		contentPane.add(leftOpen);
		
		leftClose = new JRadioButton("Close");
		leftClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					leftOpen.setSelected(false);
					leftClose.setSelected(true);
					left = false;
					controller.closeLeft();
				}
			}
		});
		leftClose.setBounds(404, 247, 67, 23);
		leftClose.setSelected(true);
		contentPane.add(leftClose);
		
		JLabel lightLabel = new JLabel("Lights");
		lightLabel.setBounds(334, 286, 61, 16);
		contentPane.add(lightLabel);
		
		lightOn = new JRadioButton("On");
		lightOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					lightOn.setSelected(true);
					lightOff.setSelected(false);
					lights = true;
					controller.lightsOn();
				}
			}
		});
		lightOn.setBounds(324, 300, 61, 23);
		lightOn.setSelected(false);
		contentPane.add(lightOn);
		
		lightOff = new JRadioButton("Off");
		lightOff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					lightOn.setSelected(false);
					lightOff.setSelected(true);
					lights = false;
					controller.lightsOff();
				}
			}
		});
		lightOff.setBounds(404, 300, 61, 23);
		lightOff.setSelected(true);
		contentPane.add(lightOff);
		
		JLabel modeLabel = new JLabel("Driving Mode");
		modeLabel.setBounds(334, 26, 103, 16);
		contentPane.add(modeLabel);
		
		modeAuto = new JRadioButton("Auto");
		modeAuto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeAuto.setSelected(true);
				modeManual.setSelected(false);
				driveMode = 0;
				controller.setDriveMode(driveMode);
			}
		});
		modeAuto.setBounds(324, 40, 65, 23);
		modeAuto.setSelected(true);
		contentPane.add(modeAuto);
		
		modeManual = new JRadioButton("Manual");
		modeManual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeAuto.setSelected(false);
				modeManual.setSelected(true);
				driveMode = 1;
				controller.setDriveMode(driveMode);
			}
		});
		modeManual.setBounds(404, 40, 77, 23);
		modeManual.setSelected(false);
		contentPane.add(modeManual);
		
		eGUIButton = new JButton("Engineer Mode");
		eGUIButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				eGUI.setVisible(true);
			}
		});
		eGUIButton.setBounds(162, 21, 117, 29);
		contentPane.add(eGUIButton);
		
		speedSet.setEnabled(false);
		tempSet.setEnabled(false);
		sBrakesOn.setEnabled(false);
		sBrakesOff.setEnabled(false);
		eBrakesOn.setEnabled(false);
		eBrakesOff.setEnabled(false);
		rightOpen.setEnabled(false);
		rightClose.setEnabled(false);
		leftOpen.setEnabled(false);
		leftClose.setEnabled(false);
		lightOn.setEnabled(false);
		lightOff.setEnabled(false);
		tempField.setEnabled(false);
		newSpeedField.setEnabled(false);
		
		frame.setVisible(true);
	}
	
	public boolean guiUpdate(boolean status) {		//send true if in yard, false otherwise
		speedValue.setText(speed + " mi/hr");
		setpointValue.setText(setpoint + " mi/hr");
		authorityValue.setText(authority + " mi");
		powerValue.setText(power + " W");
		if (left) {
			leftOpen.setSelected(true);
			leftClose.setSelected(false);
		}
		else {
			leftOpen.setSelected(false);
			leftClose.setSelected(true);
		}
		if (right) {
			rightOpen.setSelected(true);
			rightClose.setSelected(false);
		}
		else {
			rightOpen.setSelected(false);
			rightClose.setSelected(true);
		}
		if (service) {
			sBrakesOn.setSelected(true);
			sBrakesOff.setSelected(false);
		}
		else {
			sBrakesOn.setSelected(false);
			sBrakesOff.setSelected(true);
		}
		if (emergency) {
			eBrakesOn.setSelected(true);
			eBrakesOff.setSelected(false);
		}
		else {
			eBrakesOn.setSelected(false);
			eBrakesOff.setSelected(true);
		}
		if (lights) {
			lightOn.setSelected(true);
			lightOff.setSelected(false);
		}
		else {
			lightOn.setSelected(false);
			lightOff.setSelected(true);
		}
		if (status) {
			eGUIButton.setEnabled(true);
		}
		else {
			eGUIButton.setEnabled(false);
		}
		if (driveMode == 1) {
			speedSet.setEnabled(true);
			tempSet.setEnabled(true);
			sBrakesOn.setEnabled(true);
			sBrakesOff.setEnabled(true);
			eBrakesOn.setEnabled(true);
			eBrakesOff.setEnabled(true);
			rightOpen.setEnabled(true);
			rightClose.setEnabled(true);
			leftOpen.setEnabled(true);
			leftClose.setEnabled(true);
			lightOn.setEnabled(true);
			lightOff.setEnabled(true);
			tempField.setEnabled(true);
			newSpeedField.setEnabled(true);
		}
		else {
			speedSet.setEnabled(false);
			tempSet.setEnabled(false);
			sBrakesOn.setEnabled(false);
			sBrakesOff.setEnabled(false);
			eBrakesOn.setEnabled(false);
			eBrakesOff.setEnabled(false);
			rightOpen.setEnabled(false);
			rightClose.setEnabled(false);
			leftOpen.setEnabled(false);
			leftClose.setEnabled(false);
			lightOn.setEnabled(false);
			lightOff.setEnabled(false);
			tempField.setEnabled(false);
			newSpeedField.setEnabled(false);
		}
		frame.repaint();
		return true;
	}
	
	public void setSpeed(double s) {
		speed = (s * SPEEDCONVERSION);
	}
	
	public void setSetpoint(double s) {
		setpoint = (s * SPEEDCONVERSION);
	}
	
	public void setAuth(double a) {
		authority = (a * DISTANCECONVERSION);
	}
	
	public void setPower(double p) {
		power = p;
	}
	
	public void setTemp(int t) {
		temperature = t;
	}
	
	public void setLeft(boolean b) {
		left = b;
	}
	
	public void setRight(boolean b) {
		right = b;
	}
	
	public void setService(boolean b) {
		service = b;
	}
	
	public void setEmergency(boolean b) {
		emergency = b;
	}
	
	public void setLights(boolean b) {
		lights = b;
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public String getId() {
		return trainID;
	}
}