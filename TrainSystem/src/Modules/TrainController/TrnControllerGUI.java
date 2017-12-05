//Michael Kotcher

package Modules.TrainController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;

public class TrnControllerGUI {

	private JFrame frame;

	private JPanel contentPane;
	
	private JTextField newSpeedField;
	private JTextField tempField;
	
	private JButton speedSet;
	private JButton tempSet;
	private JButton modeBtn;
	private JButton serviceBtn;
	private JButton emergencyBtn;
	private JButton rightBtn;
	private JButton leftBtn;
	private JButton lightBtn;
	
	private JLabel speedValue;
	private JLabel setpointValue;
	private JLabel powerValue;
	private JLabel authorityValue;
	
	/*private JRadioButton modeManual;
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
	private JRadioButton lightOff;*/
		
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
	private int suggestedDoor;	//1 for right, -1 for left, 0 for neither
	private double speedLimit;
	
	private TrnController controller;
	
	public final double SPEEDCONVERSION = 2.23694;			//1 m/s = 2.23694 mph
	public final double DISTANCECONVERSION = 0.000621371;	//1 m = 0.000621371 miles
	public final double POWERCONVERSION = 1000;				//1 kW = 1000 W
	
	DecimalFormat df = new DecimalFormat("#.####");
	
	/*public static void main(String[] args) {
		new TrnControllerGUI(new PIController(1,0), new TrnController(), "Train 1");
	}*/
	
	public TrnControllerGUI(TrnController c, String s) {
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
		controller = c;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(600, 500, 600, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel idenLabel = new JLabel(s + "");
		idenLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		idenLabel.setBounds(20, 20, 200, 43);
		contentPane.add(idenLabel);
		
		JLabel speedLabel = new JLabel("Current Speed:");
		speedLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		speedLabel.setBounds(20, 75, 127, 26);
		contentPane.add(speedLabel);
		
		speedValue = new JLabel(speed + " mi/hr");
		speedValue.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		speedValue.setBounds(183, 75, 139, 26);
		contentPane.add(speedValue);
		
		JLabel newSpeedLabel = new JLabel("New Speed:");
		newSpeedLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		newSpeedLabel.setBounds(20, 114, 109, 26);
		contentPane.add(newSpeedLabel);
		
		newSpeedField = new JTextField();
		newSpeedField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		newSpeedField.setBounds(183, 111, 107, 32);
		contentPane.add(newSpeedField);
		newSpeedField.setColumns(10);
		newSpeedField.setText("");
		
		JLabel newSpeedUnitLabel = new JLabel("mi/hr");
		newSpeedUnitLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		newSpeedUnitLabel.setBounds(290, 113, 61, 26);
		contentPane.add(newSpeedUnitLabel);
		
		speedSet = new JButton("Set New Speed");
		speedSet.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		speedSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					try {
						double d;
						d = Double.parseDouble(newSpeedField.getText());
						if (d > speedLimit) {
							d = speedLimit;
						}
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
		speedSet.setBounds(15, 152, 180, 37);
		contentPane.add(speedSet);
		
		JLabel setpointLabel = new JLabel("Setpoint Speed:");
		setpointLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		setpointLabel.setBounds(20, 201, 127, 26);
		contentPane.add(setpointLabel);
		
		setpointValue = new JLabel(setpoint + " mi/hr");
		setpointValue.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		setpointValue.setBounds(183, 204, 139, 22);
		contentPane.add(setpointValue);
		
		JLabel powerLabel = new JLabel("Power Output:");
		powerLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		powerLabel.setBounds(20, 239, 127, 26);
		contentPane.add(powerLabel);
		
		powerValue = new JLabel(power + " kW");
		powerValue.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		powerValue.setBounds(183, 242, 151, 22);
		contentPane.add(powerValue);
		
		JLabel authorityLabel = new JLabel("Current Authority:");
		authorityLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		authorityLabel.setBounds(20, 277, 151, 26);
		contentPane.add(authorityLabel);
		
		authorityValue = new JLabel(authority + " mi");
		authorityValue.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		authorityValue.setBounds(183, 279, 151, 22);
		contentPane.add(authorityValue);
		
		JLabel temperatureLabel = new JLabel("Temperature:");
		temperatureLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		temperatureLabel.setBounds(20, 315, 112, 21);
		contentPane.add(temperatureLabel);
		
		tempField = new JTextField();
		tempField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		tempField.setBounds(183, 308, 102, 32);
		contentPane.add(tempField);
		tempField.setColumns(10);
		tempField.setText(temperature + "");
		
		JLabel tempUnitLabel = new JLabel("F");
		tempUnitLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		tempUnitLabel.setBounds(288, 316, 61, 16);
		contentPane.add(tempUnitLabel);
		
		tempSet = new JButton("Set");
		tempSet.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		tempSet.setBounds(15, 348, 180, 37);
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
		sBrakesLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		sBrakesLabel.setBounds(376, 89, 200, 37);
		contentPane.add(sBrakesLabel);
		
		/*sBrakesOn = new JRadioButton("On");
		sBrakesOn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		sBrakesOn.setBounds(376, 116, 80, 40);
		sBrakesOn.setSelected(false);
		contentPane.add(sBrakesOn);
		
		sBrakesOff = new JRadioButton("Off");
		sBrakesOff.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		sBrakesOff.setBounds(481, 116, 80, 40);
		sBrakesOff.setSelected(true);
		contentPane.add(sBrakesOff);*/
		
		serviceBtn = new JButton();
		serviceBtn.setText("OFF");
		serviceBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		serviceBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					if (serviceBtn.getText().equals("OFF")) {
						serviceBtn.setText("ON");
						service = true;
						controller.sBrakesOn();
					}
					else {
						serviceBtn.setText("OFF");
						service = false;
						controller.sBrakesOff();
					}
				}
			}
		});
		serviceBtn.setBounds(374, 120, 185, 37);
		contentPane.add(serviceBtn);
		
		JLabel eBrakesLabel = new JLabel("Emergency Brakes");
		eBrakesLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		eBrakesLabel.setBounds(376, 153, 200, 37);
		contentPane.add(eBrakesLabel);
		
		/*eBrakesOn = new JRadioButton("On");
		eBrakesOn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		eBrakesOn.setBounds(376, 181, 80, 40);
		eBrakesOn.setSelected(false);
		contentPane.add(eBrakesOn);
		
		eBrakesOff = new JRadioButton("Off");
		eBrakesOff.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		eBrakesOff.setBounds(481, 181, 80, 40);
		eBrakesOff.setSelected(true);
		contentPane.add(eBrakesOff);*/
		
		emergencyBtn = new JButton();
		emergencyBtn.setText("OFF");
		emergencyBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		emergencyBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					if (emergencyBtn.getText().equals("OFF")) {
						emergencyBtn.setText("ON");
						emergency = true;
						controller.eBrakesOn();
					}
					else {
						emergencyBtn.setText("OFF");
						emergency = false;
						controller.eBrakesOff();
					}
				}
			}
		});
		emergencyBtn.setBounds(374, 184, 185, 37);
		contentPane.add(emergencyBtn);
		
		JLabel rightDoorLabel = new JLabel("Right Doors");
		rightDoorLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		rightDoorLabel.setBounds(376, 216, 200, 37);
		contentPane.add(rightDoorLabel);
		
		/*rightOpen = new JRadioButton("Open");
		rightOpen.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		rightOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1 && suggestedDoor == 1) {
					rightOpen.setSelected(true);
					rightClose.setSelected(false);
					right = true;
					controller.openRight();
				}
			}
		});
		rightOpen.setBounds(376, 244, 80, 40);
		rightOpen.setSelected(false);
		contentPane.add(rightOpen);
		
		rightClose = new JRadioButton("Close");
		rightClose.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		rightClose.setBounds(481, 244, 80, 40);
		rightClose.setSelected(true);
		contentPane.add(rightClose);*/
		
		rightBtn = new JButton();
		rightBtn.setText("CLOSED");
		rightBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		rightBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					if (rightBtn.getText().equals("CLOSED")) {
						rightBtn.setText("OPEN");
						right = true;
						controller.openRight();
					}
					else {
						rightBtn.setText("CLOSED");
						right = false;
						controller.closeRight();
					}
				}
			}
		});
		rightBtn.setBounds(374, 246, 185, 37);
		contentPane.add(rightBtn);
		
		JLabel leftDoorLabel = new JLabel("Left Doors");
		leftDoorLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		leftDoorLabel.setBounds(376, 277, 200, 37);
		contentPane.add(leftDoorLabel);
		
		/*leftOpen = new JRadioButton("Open");
		leftOpen.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		leftOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1 && suggestedDoor == -1) {
					leftOpen.setSelected(true);
					leftClose.setSelected(false);
					left = true;
					controller.openLeft();
				}
			}
		});
		leftOpen.setBounds(376, 305, 80, 40);
		leftOpen.setSelected(false);
		contentPane.add(leftOpen);
		
		leftClose = new JRadioButton("Close");
		leftClose.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		leftClose.setBounds(481, 305, 80, 40);
		leftClose.setSelected(true);
		contentPane.add(leftClose);*/
		
		leftBtn = new JButton();
		leftBtn.setText("CLOSED");
		leftBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		leftBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					if (leftBtn.getText().equals("CLOSED")) {
						leftBtn.setText("OPEN");
						left = true;
						controller.openLeft();
					}
					else {
						leftBtn.setText("CLOSED");
						left = false;
						controller.closeLeft();
					}
				}
			}
		});
		leftBtn.setBounds(374, 309, 185, 37);
		contentPane.add(leftBtn);
		
		JLabel lightLabel = new JLabel("Lights");
		lightLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lightLabel.setBounds(376, 341, 200, 37);
		contentPane.add(lightLabel);
		
		/*lightOn = new JRadioButton("On");
		lightOn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		lightOn.setBounds(376, 369, 80, 40);
		lightOn.setSelected(false);
		contentPane.add(lightOn);
		
		lightOff = new JRadioButton("Off");
		lightOff.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
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
		lightOff.setBounds(481, 369, 80, 40);
		lightOff.setSelected(true);
		contentPane.add(lightOff);*/
		
		lightBtn = new JButton();
		lightBtn.setText("OFF");
		lightBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lightBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (driveMode == 1) {
					if (lightBtn.getText().equals("OFF")) {
						lightBtn.setText("ON");
						lights = true;
						controller.lightsOn();
					}
					else {
						lightBtn.setText("OFF");
						lights = false;
						controller.lightsOff();
					}
				}
			}
		});
		lightBtn.setBounds(374, 374, 185, 37);
		contentPane.add(lightBtn);
		
		JLabel modeLabel = new JLabel("Driving Mode");
		modeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		modeLabel.setBounds(376, 20, 200, 37);
		contentPane.add(modeLabel);
		
		/*modeAuto = new JRadioButton("Auto");
		modeAuto.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		modeAuto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeAuto.setSelected(true);
				modeManual.setSelected(false);
				driveMode = 0;
				controller.setDriveMode(driveMode);
			}
		});
		modeAuto.setBounds(376, 48, 80, 40);
		modeAuto.setSelected(true);
		contentPane.add(modeAuto);
		
		modeManual = new JRadioButton("Manual");
		modeManual.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		modeManual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modeAuto.setSelected(false);
				modeManual.setSelected(true);
				driveMode = 1;
				controller.setDriveMode(driveMode);
			}
		});
		modeManual.setBounds(481, 48, 80, 40);
		modeManual.setSelected(false);
		contentPane.add(modeManual);*/
		
		modeBtn = new JButton();
		modeBtn.setText("AUTO");
		modeBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		modeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (modeBtn.getText().equals("MANUAL")) {
					modeBtn.setText("AUTO");
					driveMode = 0;
					controller.setDriveMode(driveMode);
				}
				else {
					modeBtn.setText("MANUAL");
					driveMode = 1;
					controller.setDriveMode(driveMode);
				}
			}
		});
		modeBtn.setBounds(374, 53, 185, 37);
		contentPane.add(modeBtn);
		
		speedSet.setEnabled(false);
		tempSet.setEnabled(false);
		/*sBrakesOn.setEnabled(false);
		sBrakesOff.setEnabled(false);
		eBrakesOn.setEnabled(false);
		eBrakesOff.setEnabled(false);
		rightOpen.setEnabled(false);
		rightClose.setEnabled(false);
		leftOpen.setEnabled(false);
		leftClose.setEnabled(false);
		lightOn.setEnabled(false);
		lightOff.setEnabled(false);*/
		serviceBtn.setEnabled(false);
		emergencyBtn.setEnabled(false);
		rightBtn.setEnabled(false);
		leftBtn.setEnabled(false);
		lightBtn.setEnabled(false);
		tempField.setEnabled(false);
		newSpeedField.setEnabled(false);
		
		frame.setVisible(false);
	}
	
	public boolean guiUpdate() {
		speedValue.setText(df.format(speed) + " mi/hr");
		setpointValue.setText(df.format(setpoint) + " mi/hr");
		authorityValue.setText(df.format(authority) + " mi");
		powerValue.setText(df.format(power) + " kW");
		if (left) {
			/*leftOpen.setSelected(true);
			leftClose.setSelected(false);*/
			leftBtn.setText("OPEN");
		}
		else {
			/*leftOpen.setSelected(false);
			leftClose.setSelected(true);*/
			leftBtn.setText("CLOSED");
		}
		if (right) {
			/*rightOpen.setSelected(true);
			rightClose.setSelected(false);*/
			rightBtn.setText("OPEN");
		}
		else {
			/*rightOpen.setSelected(false);
			rightClose.setSelected(true);*/
			rightBtn.setText("CLOSED");
		}
		if (service) {
			/*sBrakesOn.setSelected(true);
			sBrakesOff.setSelected(false);*/
			serviceBtn.setText("ON");
		}
		else {
			/*sBrakesOn.setSelected(false);
			sBrakesOff.setSelected(true);*/
			serviceBtn.setText("OFF");
		}
		if (emergency) {
			/*eBrakesOn.setSelected(true);
			eBrakesOff.setSelected(false);*/
			emergencyBtn.setText("ON");
		}
		else {
			/*eBrakesOn.setSelected(false);
			eBrakesOff.setSelected(true);*/
			emergencyBtn.setText("OFF");
		}
		if (lights) {
			/*lightOn.setSelected(true);
			lightOff.setSelected(false);*/
			lightBtn.setText("ON");
		}
		else {
			/*lightOn.setSelected(false);
			lightOff.setSelected(true);*/
			lightBtn.setText("OFF");
		}
		if (driveMode == 1) {
			speedSet.setEnabled(true);
			tempSet.setEnabled(true);
			/*sBrakesOn.setEnabled(true);
			sBrakesOff.setEnabled(true);
			eBrakesOn.setEnabled(true);
			eBrakesOff.setEnabled(true);
			rightOpen.setEnabled(true);
			rightClose.setEnabled(true);
			leftOpen.setEnabled(true);
			leftClose.setEnabled(true);
			lightOn.setEnabled(true);
			lightOff.setEnabled(true);*/
			serviceBtn.setEnabled(true);
			emergencyBtn.setEnabled(true);
			rightBtn.setEnabled(true);
			leftBtn.setEnabled(true);
			lightBtn.setEnabled(true);
			tempField.setEnabled(true);
			newSpeedField.setEnabled(true);
		}
		else {
			speedSet.setEnabled(false);
			tempSet.setEnabled(false);
			/*sBrakesOn.setEnabled(false);
			sBrakesOff.setEnabled(false);
			eBrakesOn.setEnabled(false);
			eBrakesOff.setEnabled(false);
			rightOpen.setEnabled(false);
			rightClose.setEnabled(false);
			leftOpen.setEnabled(false);
			leftClose.setEnabled(false);
			lightOn.setEnabled(false);
			lightOff.setEnabled(false);*/
			serviceBtn.setEnabled(false);
			emergencyBtn.setEnabled(false);
			rightBtn.setEnabled(false);
			leftBtn.setEnabled(false);
			lightBtn.setEnabled(false);
			tempField.setEnabled(false);
			newSpeedField.setEnabled(false);
		}
		frame.repaint();
		return true;
	}
	
	public void setSpeed(double s) {
		speed = (s * SPEEDCONVERSION);
	}
	
	public void setSpeedLimit(double s) {	//input is in m/s
		speedLimit = (s * SPEEDCONVERSION);
	}
	
	public void setSetpoint(double s) {
		setpoint = (s * SPEEDCONVERSION);
	}
	
	public void setAuth(double a) {
		authority = (a * DISTANCECONVERSION);
	}
	
	public void setPower(double p) {
		power = (p / POWERCONVERSION);
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
	
	public void setSuggestedDoor(int d) {
		suggestedDoor = d;
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public String getId() {
		return trainID;
	}
}