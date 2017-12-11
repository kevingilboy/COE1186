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

/*--- REQUIRED LIBRARIES FOR HSS DARK THEME ----*/
import java.awt.GraphicsEnvironment;
import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import java.awt.FontFormatException;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
/*----------------------------------------------*/

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
	
	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Variations of Roboto Condensed Font
	 */
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 20);
	Font font_20_bold = new Font("Roboto Condensed Bold", Font.BOLD, 30);
	Font font_24_bold = new Font("Roboto Condensed", Font.BOLD, 38);

	/**
	 * Set any UI configurations done by the UI manager
	 *
	 * NOTE: This method must be called first in the GUI instantiation!
	 */
	public void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * JComponent styling wrappers
	 */
	public void stylizeButton(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(102, 0, 153)); // Purple
	}

	public void stylizeComboBox(JComboBox c){
		c.setFont(font_14_bold);
		((JLabel)c.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		c.setForeground(Color.BLACK);
		c.setBackground(Color.WHITE);
	}

	public void stylizeTextField(JTextField t){
		t.setFont(font_14_bold);
		t.setForeground(Color.BLACK);
		t.setBackground(Color.WHITE);
		t.setHorizontalAlignment(JTextField.CENTER);
	}

	public void stylizeHeadingLabel(JLabel l){
		l.setFont(font_20_bold);
		l.setForeground(Color.WHITE);
		l.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void stylizeInfoLabel(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Bold(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(new Color(234, 201, 87));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Small(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_14_bold);
	}

	/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Called by the SimulatorGUI class to show the GUI when this module is selected
	 */
	public void showGUI(){
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public TrainControllerGUI() {
		setLookAndFeel();

		p = 60000;
		i = 726.41;

		ready = true;
		guiList = new ArrayList<TrnControllerGUI>();
		buttonList = new ArrayList<JButton>();
		yCount = 60;
		height = 380;
		
		frame = new JFrame();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBounds(100, 500, 444, height);
		frame.setTitle("Train Controller");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 29, 35));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		panel = new JPanel();
		panel.setBackground(new Color(20, 20, 20));
		panel.setBounds(259, 15, 162, 196);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBorder(blackline);
		
		/*JLabel titleLabel = new JLabel("Train Controller Module");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		titleLabel.setBounds(95, 15, 253, 42);
		contentPane.add(titleLabel);*/
		
		JLabel activeLabel = new JLabel("ACTIVE TRAINS");
		stylizeHeadingLabel(activeLabel);
		activeLabel.setBounds(30, 15, 200, 32);
		contentPane.add(activeLabel);
		
		JLabel controllerLabel = new JLabel("PI CONTROLLER\n");
		stylizeInfoLabel_Bold(controllerLabel);
		controllerLabel.setBounds(20, 6, 140, 20);
		panel.add(controllerLabel);
		
		JLabel dispatchLabel = new JLabel("DISPATCH VALUES");
		stylizeInfoLabel_Small(dispatchLabel);
		dispatchLabel.setBounds(30, 35, 140, 16);
		panel.add(dispatchLabel);
		
		pField = new JTextField();			//p
		stylizeTextField(pField);
		pField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ready = false;
				confirmButton.setEnabled(true);
			}
		});
		pField.setBounds(40, 66, 80, 29);
		panel.add(pField);
		pField.setColumns(10);
		pField.setText(p + "");
		
		iField = new JTextField();			//i
		stylizeTextField(iField);
		iField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				ready = false;
				confirmButton.setEnabled(true);
			}
		});
		iField.setBounds(40, 107, 80, 29);
		panel.add(iField);
		iField.setColumns(10);
		iField.setText(i + "");
		
		JLabel pLabel = new JLabel("P");
		stylizeInfoLabel(pLabel);
		pLabel.setBounds(12, 73, 16, 16);
		panel.add(pLabel);
		
		JLabel iLabel = new JLabel("I");
		stylizeInfoLabel(iLabel);
		iLabel.setBounds(14, 114, 16, 16);
		panel.add(iLabel);
		
		confirmButton = new JButton("CONFIRM");
		stylizeButton(confirmButton);
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
		confirmButton.setEnabled(false);
		panel.add(confirmButton);
		
		icon_logo = new JLabel("");
		icon_logo.setIcon(new ImageIcon("Modules/TrackModel/Images/HSS_TrainSim_Logo.png"));
		icon_logo.setBounds(332, 248, 100, 100);
		contentPane.add(icon_logo);
		
		frame.setVisible(false);
	}
	
	public void add(TrnControllerGUI g) {
		//System.out.println("add");
		guiList.add(g);
		
		JLabel L = new JLabel(g.getId());
		stylizeInfoLabel_Bold(L);
		L.setBounds(30, yCount + 8, 100, 24);
		contentPane.add(L);
		
		JButton B = new JButton("VIEW");
		stylizeButton(B);
		buttonList.add(B);
		B.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = buttonList.indexOf(B);
				TrnControllerGUI I = guiList.get(i);
				I.setVisible(true);
			}
		});
		B.setBounds(150, yCount, 70, 37);
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

	public void trainPoofByName(String name) {
		// TODO Auto-generated method stub
		
	}
}
