//Michael Kotcher

package Modules.TrainController;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class TrainControllerGUI {

	private int yCount;
	private int height;
	private int logoHeight;
	
	private double p;
	private double i;
	
	private boolean ready;
	
	private JFrame frame;
	
	private JTextField pField;
	private JTextField iField;
	
	private JButton confirmButton;

	private JPanel contentPane;
	
	private ArrayList<TrnControllerGUI> guiList;
	private ArrayList<JButton> buttonList;
	
	private JLabel icon_logo;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		new TrainControllerGUI();
	}*/

	/**
	 * Create the frame.
	 */
	public TrainControllerGUI() {
		p = 2000;
		i = 0.8071;
		ready = true;
		guiList = new ArrayList<TrnControllerGUI>();
		buttonList = new ArrayList<JButton>();
		yCount = 80;
		height = 400;
		logoHeight = 280;
		
		Font standardFont = new Font("Lucida Grande", Font.PLAIN, 16);
		
		icon_logo = new JLabel("");
		icon_logo.setIcon(new ImageIcon("Modules\\TrackModel\\Images\\HSS_TrainSim_Logo.png"));
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 500, 450, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titleLabel = new JLabel("Train Controller Module");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		titleLabel.setBounds(98, 6, 304, 53);
		contentPane.add(titleLabel);
		
		JLabel dispatchLabel = new JLabel("Dispatch Values");
		dispatchLabel.setFont(standardFont);
		dispatchLabel.setBounds(241, 76, 140, 18);
		contentPane.add(dispatchLabel);
		
		pField = new JTextField();			//p
		pField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ready = false;
				confirmButton.setEnabled(true);
			}
		});
		pField.setBounds(241, 106, 161, 36);
		pField.setFont(standardFont);
		contentPane.add(pField);
		pField.setColumns(10);
		pField.setText(p + "");
		
		iField = new JTextField();			//i
		iField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ready = false;
				confirmButton.setEnabled(true);
			}
		});
		iField.setBounds(241, 154, 161, 36);
		iField.setFont(standardFont);
		contentPane.add(iField);
		iField.setColumns(10);
		iField.setText(i + "");
		
		JLabel pLabel = new JLabel("P");
		pLabel.setBounds(229, 109, 16, 31);
		pLabel.setFont(standardFont);
		contentPane.add(pLabel);
		
		JLabel iLabel = new JLabel("I");
		iLabel.setBounds(229, 158, 16, 29);
		iLabel.setFont(standardFont);
		contentPane.add(iLabel);
		
		confirmButton = new JButton("Confirm");
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					p = Double.parseDouble(pField.getText());
					pField.setText(p + "");
				}
				catch (NumberFormatException E) {
					pField.setText(p + "");
				}
				try {
					i = Double.parseDouble(iField.getText());
					iField.setText(i + "");
				}
				catch (NumberFormatException E) {
					iField.setText(i + "");
				}
				ready = true;
				confirmButton.setEnabled(false);
			}
		});
		confirmButton.setBounds(241, 203, 140, 37);
		confirmButton.setFont(standardFont);
		confirmButton.setEnabled(false);
		contentPane.add(confirmButton);
		
		icon_logo.setBounds(330, logoHeight, 100, 100);
		contentPane.add(icon_logo);
		
		frame.setVisible(true);
	}
	
	public void add(TrnControllerGUI g) {
		System.out.println("add");
		guiList.add(g);
		JButton B = new JButton(g.getId());
		buttonList.add(B);
		B.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = buttonList.indexOf(B);
				TrnControllerGUI I = guiList.get(i);
				I.setVisible(true);
			}
		});
		B.setBounds(30, yCount, 130, 37);
		B.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		B.setVisible(true);
		B.setEnabled(true);
		contentPane.add(B);
		yCount = yCount + 60;
		if (yCount >= (height - 60)) {
			height = height + 60;
			logoHeight = logoHeight + 60;
			frame.setBounds(100, 100, 450, height);
			icon_logo.setBounds(300, logoHeight, 150, 150);
		}
		frame.repaint();
	}
	
	public double getP() {
		if (ready) {
			return p;
		} 
		else {
			return -1;
		}
	}
	
	public double getI() {
		if (ready) {
			return i;
		} 
		else {
			return -1;
		}
	}
}
