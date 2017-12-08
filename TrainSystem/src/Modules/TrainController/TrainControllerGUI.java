//Michael Kotcher

package Modules.TrainController;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Color;

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
	private JPanel panel;
	
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
		p = 6000;
		i = 7.2641;
		ready = true;
		guiList = new ArrayList<TrnControllerGUI>();
		buttonList = new ArrayList<JButton>();
		yCount = 60;
		height = 360;
		logoHeight = 240;
		
		Font standardFont = new Font("Lucida Grande", Font.PLAIN, 16);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 500, 450, height);
		frame.setTitle("Train Controller");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		panel = new JPanel();
		panel.setBounds(259, 15, 162, 196);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBorder(blackline);
		
		/*JLabel titleLabel = new JLabel("Train Controller Module");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		titleLabel.setBounds(95, 15, 253, 42);
		contentPane.add(titleLabel);*/
		
		JLabel activeLabel = new JLabel("Active Trains");
		activeLabel.setFont(standardFont);
		activeLabel.setBounds(51, 15, 117, 32);
		contentPane.add(activeLabel);
		
		JLabel controllerLabel = new JLabel("PI Controller\n");
		controllerLabel.setBounds(27, 6, 104, 20);
		panel.add(controllerLabel);
		controllerLabel.setFont(standardFont);
		
		JLabel dispatchLabel = new JLabel("Dispatch Values");
		dispatchLabel.setFont(standardFont);
		dispatchLabel.setBounds(16, 35, 125, 16);
		panel.add(dispatchLabel);
		
		pField = new JTextField();			//p
		pField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ready = false;
				confirmButton.setEnabled(true);
			}
		});
		pField.setBounds(18, 66, 138, 29);
		pField.setFont(standardFont);
		panel.add(pField);
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
		iField.setBounds(18, 107, 138, 29);
		iField.setFont(standardFont);
		panel.add(iField);
		iField.setColumns(10);
		iField.setText(i + "");
		
		JLabel pLabel = new JLabel("P");
		pLabel.setBounds(6, 73, 16, 16);
		pLabel.setFont(standardFont);
		panel.add(pLabel);
		
		JLabel iLabel = new JLabel("I");
		iLabel.setBounds(8, 114, 16, 16);
		iLabel.setFont(standardFont);
		panel.add(iLabel);
		
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
		confirmButton.setBounds(16, 150, 130, 35);
		confirmButton.setFont(standardFont);
		confirmButton.setEnabled(false);
		panel.add(confirmButton);
		
		icon_logo = new JLabel("");
		icon_logo.setIcon(new ImageIcon("Modules\\TrackModel\\Images\\HSS_TrainSim_Logo.png"));
		icon_logo.setBounds(330, logoHeight, 100, 100);
		contentPane.add(icon_logo);
		
		frame.setVisible(true);
	}
	
	public void add(TrnControllerGUI g) {
		Font standardFont = new Font("Lucida Grande", Font.PLAIN, 16);
		
		//System.out.println("add");
		guiList.add(g);
		
		JLabel L = new JLabel(g.getId());
		L.setBounds(19, yCount + 8, 61, 18);
		L.setFont(standardFont);
		contentPane.add(L);
		
		JButton B = new JButton("View");
		buttonList.add(B);
		B.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = buttonList.indexOf(B);
				TrnControllerGUI I = guiList.get(i);
				I.setVisible(true);
			}
		});
		B.setBounds(93, yCount, 130, 37);
		B.setFont(standardFont);
		B.setVisible(true);
		B.setEnabled(true);
		contentPane.add(B);
		
		yCount = yCount + 50;
		if (yCount >= (height - 50)) {
			height = height + 50;
			logoHeight = logoHeight + 50;
			frame.setBounds(100, 500, 450, height);
			icon_logo.setBounds(330, logoHeight, 100, 100);
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
