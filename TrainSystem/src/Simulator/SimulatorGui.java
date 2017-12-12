package Simulator;

import java.awt.EventQueue;
import javax.swing.JFrame;
import Modules.Ctc.Line;

import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

/*--- REQUIRED LIBRARIES FOR HSS DARK THEME ----*/
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
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

public class SimulatorGui {

/*----------------------------------------------------------------------*/
	/*-------------------- HSS GUI DARK THEME REDESIGN ---------------------*/
	/*----------------------------------------------------------------------*/

	/**
	 * Variations of Roboto Condensed Font
	 */
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 16);
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
	public void stylizeButton_disabled(JButton b){
		b.setFont(font_16_bold);
		b.setForeground(new Color(46, 49, 55));
		b.setBackground(new Color(46, 49, 55));
		b.setEnabled(false);
	}

	public void stylizeButton(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_16_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(46, 49, 55));
		b.setEnabled(true);
	}

	public void stylizeComboBox_disabled(JComboBox c){
		c.setFont(font_14_bold);
		((JLabel)c.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		c.setForeground(Color.BLACK);
		c.setBackground(new Color(26, 29, 35));
		c.setEnabled(false);
	}

	public void stylizeComboBox(JComboBox c){
		c.setFont(font_14_bold);
		((JLabel)c.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		c.setForeground(Color.BLACK);
		c.setBackground(Color.WHITE);
		c.setEnabled(true);
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

	private enum Waysides{
		G1, G2, R1, R2;
	};
	
	protected Simulator simulator;
	
	private JFrame frame;
	private final int GUI_WINDOW_WIDTH = 600;
	private final int GUI_WINDOW_HEIGHT = 870;

	JButton btnCtc;
	JButton btnWaysideR1;
	JButton btnWaysideR2;
	JButton btnWaysideG1;
	JButton btnWaysideG2;
	JButton btnTrackModel;
	JButton btnTraincontroller;
	JButton btnMbo;
	
	private JComboBox<String> cbTrainModelTrains;
	private JComboBox<String> cbTrainControllerTrains;

	/**
	 * Create the application.
	 */
	public SimulatorGui(Simulator sim) {
		setLookAndFeel();
		simulator = sim;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(26, 29, 35));
		frame.setResizable(false);
		frame.setBounds(100, 100, GUI_WINDOW_WIDTH, GUI_WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(null);

		/**
		 *
		 * ------------------------------
		 *  BIG LOGO
		 * ------------------------------
		 */
		JLabel icon_logo = new JLabel("");
		icon_logo.setIcon(new ImageIcon("Simulator/Images/HSS_splash_screen_logo.png"));
	
		int logo_w = 662;
		int logo_h = 211;
		int logo_x = 38;
		int logo_y = 0;

		icon_logo.setBounds(logo_x, logo_y, logo_w, logo_h);
		frame.getContentPane().add(icon_logo);

		/**
		 * FOOTER
		 */
		JLabel footer = new JLabel("HSS Train Simulator | COE1186 Final Project | FALL 2017");
		footer.setBackground(Color.BLACK);
		footer.setForeground(Color.WHITE);
		footer.setBounds(0, GUI_WINDOW_HEIGHT - 16, GUI_WINDOW_WIDTH, 16);
		frame.getContentPane().add(footer);
		
		/*
		 * ------------------------------
		 *  CTC
		 * ------------------------------
		 */
		JLabel ctc_logo = new JLabel("");
		ctc_logo.setIcon(new ImageIcon("Simulator/Images/ctc_logo.png"));

		ctc_logo.setBounds(46, 214, 72, 72);
		frame.getContentPane().add(ctc_logo);

		btnCtc = new JButton("LAUNCH");
		stylizeButton_disabled(btnCtc);

		btnCtc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.CTC);
			}
		});
		btnCtc.setBounds(420, 234, 140, 30);
		frame.getContentPane().add(btnCtc);
		
		/*
		 * ------------------------------
		 *  TRACK CONTROLLER
		 * ------------------------------
		 */
		JLabel trkCtrl_logo = new JLabel("");
		trkCtrl_logo.setIcon(new ImageIcon("Simulator/Images/trackcontroller_logo.png"));

		trkCtrl_logo.setBounds(46, 314, 72, 72);
		frame.getContentPane().add(trkCtrl_logo);

		btnWaysideR1 = new JButton("R1");
		stylizeButton_disabled(btnWaysideR1);

		btnWaysideR1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.R1);
			}
		});
		btnWaysideR1.setBounds(420, 362, 65, 30);
		frame.getContentPane().add(btnWaysideR1);
		
		btnWaysideR2 = new JButton("R2");
		stylizeButton_disabled(btnWaysideR2);

		btnWaysideR2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.R2);
			}
		});
		btnWaysideR2.setBounds(495, 362, 65, 30);
		frame.getContentPane().add(btnWaysideR2);
		
		btnWaysideG1 = new JButton("G1");	
		stylizeButton_disabled(btnWaysideG1);

		btnWaysideG1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.G1);
			}
		});
		btnWaysideG1.setBounds(420, 312, 65, 30);
		frame.getContentPane().add(btnWaysideG1);
		
		btnWaysideG2 = new JButton("G2");
		stylizeButton_disabled(btnWaysideG2);

		btnWaysideG2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.G2);
			}
		});
		btnWaysideG2.setBounds(495, 312, 65, 30);
		frame.getContentPane().add(btnWaysideG2);
		
		/*
		 * ------------------------------
		 *  TRACK MODEL
		 * ------------------------------
		 */
		JLabel trkMdl_logo = new JLabel("");
		trkMdl_logo.setIcon(new ImageIcon("Simulator/Images/trackmodel_logo.png"));

		trkMdl_logo.setBounds(46, 414, 72, 72);
		frame.getContentPane().add(trkMdl_logo);

		btnTrackModel = new JButton("LAUNCH");
		stylizeButton_disabled(btnTrackModel);

		btnTrackModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKMODEL);
			}
		});
		btnTrackModel.setBounds(420, 436, 140, 30);
		frame.getContentPane().add(btnTrackModel);
		
		/*
		 * ------------------------------
		 *  TRAIN MODEL
		 * ------------------------------
		 */
		JLabel trnMdl_logo = new JLabel("");
		trnMdl_logo.setIcon(new ImageIcon("Simulator/Images/trainmodel_logo.png"));

		trnMdl_logo.setBounds(46, 514, 72, 72);
		frame.getContentPane().add(trnMdl_logo);		

		cbTrainModelTrains = new JComboBox<String>();
		stylizeComboBox_disabled(cbTrainModelTrains);

		cbTrainModelTrains.addItem("<html><i>No Active Trains</i></html>");
		cbTrainModelTrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName = (String)cbTrainModelTrains.getSelectedItem();
				if(trainName.equals("")) {
					return;
				}
				
				openGui(ModuleType.TRAINMODEL,trainName);
				cbTrainModelTrains.setSelectedIndex(0);
			}
		});		
		cbTrainModelTrains.setBounds(420, 536, 140, 30);
		frame.getContentPane().add(cbTrainModelTrains);
		
		/*
		 * ------------------------------
		 *  TRAIN CONTROLLER
		 * ------------------------------
		 */
		JLabel trnCtrl_logo = new JLabel("");
		trnCtrl_logo.setIcon(new ImageIcon("Simulator/Images/traincontroller_logo.png"));

		trnCtrl_logo.setBounds(46, 614, 72, 72);
		frame.getContentPane().add(trnCtrl_logo);	

		btnTraincontroller = new JButton("LAUNCH");
		stylizeButton_disabled(btnTraincontroller);

		btnTraincontroller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRAINCONTROLLER);
			}
		});
		btnTraincontroller.setBounds(420, 618, 140, 30);
		frame.getContentPane().add(btnTraincontroller);
		
		cbTrainControllerTrains = new JComboBox<String>();
		stylizeComboBox_disabled(cbTrainControllerTrains);

		cbTrainControllerTrains.addItem("<html><i>No Active Trains</i></html>");
		cbTrainControllerTrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName = (String)cbTrainControllerTrains.getSelectedItem();
				if(trainName.equals("")) {
					return;
				}
				
				openGui(ModuleType.TRAINCONTROLLER,trainName);
				cbTrainControllerTrains.setSelectedIndex(0);
			}
		});
		cbTrainControllerTrains.setBounds(420, 656, 140, 30);
		frame.getContentPane().add(cbTrainControllerTrains);
		
		/*
		 * ------------------------------
		 *  MBO
		 * ------------------------------
		 */
		JLabel mbo_logo = new JLabel("");
		mbo_logo.setIcon(new ImageIcon("Simulator/Images/mbo_logo.png"));

		mbo_logo.setBounds(46, 714, 72, 72);
		frame.getContentPane().add(mbo_logo);	

		btnMbo = new JButton("MBO");
		stylizeButton_disabled(btnMbo);

		btnMbo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.MBO);
			}
		});
		btnMbo.setBounds(420, 732, 140, 30);
		frame.getContentPane().add(btnMbo);
		
		
		int y = 200;
		int dy = 100;
		int width = (int)(0.90 * GUI_WINDOW_WIDTH);
		int xStart = (GUI_WINDOW_WIDTH-width)/2;
		for(int i=0; i<6; i++) {
			JSeparator separator = new JSeparator();
			separator.setForeground(new Color(36, 39, 45));
			separator.setBackground(new Color(36, 39, 45));
			separator.setBounds(xStart, y, width, 2);
			frame.getContentPane().add(separator);
			y += dy;
		}		
	}

	protected void moduleObjectInitialized(ModuleType module) {
		//We can use this to update the GUI with what module has just been initialized
		switch (module) {
			case CTC:
				stylizeButton(btnCtc);
				break;
			case TRACKCONTROLLER:
				stylizeButton(btnWaysideR1);
				stylizeButton(btnWaysideR2);
				stylizeButton(btnWaysideG1);
				stylizeButton(btnWaysideG2);
				break;
			case TRACKMODEL:
				stylizeButton(btnTrackModel);
				break;
			case TRAINMODEL:
				stylizeComboBox(cbTrainModelTrains);
				break;
			case TRAINCONTROLLER:
				stylizeComboBox(cbTrainControllerTrains);
				stylizeButton(btnTraincontroller);
				break;
			case MBO:
				stylizeButton(btnMbo);
				break;		
		}
	}

	protected void moduleCommunicationInitialized(ModuleType module) {
		//We can use this to update the GUI with what module has just been initialized
		switch (module) {
			case CTC:
				//...
				break;
			case TRACKCONTROLLER:
				//...
				break;
			case TRACKMODEL:
				//...
				break;
			case TRAINMODEL:
				//...
				break;
			case TRAINCONTROLLER:
				//...
				break;
			case MBO:
				//...
				break;		
		}
	}
	
	private void openGui(ModuleType module) {
		//We can use this to open a respective Module's GUI
		switch (module) {
			case CTC:
				simulator.ctc.showGUI();
				break;
			case TRACKMODEL:
				simulator.trackModel.showGUI();
				break;
			case TRAINCONTROLLER:
				simulator.trainController.showGUI();
				break;
			case MBO:
				simulator.mbo.showGUI();
				break;		
		}
	}
	private void openGui(ModuleType module, Waysides wayside) {
		//We can use this to open a respective Module's GUI
		switch (module) {
			case TRACKCONTROLLER:
				simulator.ctc.launchWaysideGui(wayside.ordinal()); 
				break;
		}
	}

	private void openGui(ModuleType module, String trainName) {
		//We can use this to open a respective Module's GUI
		switch (module) {
			case TRAINMODEL:
				simulator.trainModel.getTrain(trainName).showTrainGUI();
				break;
			case TRAINCONTROLLER:
				simulator.trainController.getController(trainName).showGUI();
				break;	
		}
	}
	
	protected void repaint() {
		DefaultComboBoxModel<String> currentTrainModelTrains = (DefaultComboBoxModel<String>)cbTrainModelTrains.getModel();
		DefaultComboBoxModel<String> currentTrainControllerTrains = (DefaultComboBoxModel<String>)cbTrainControllerTrains.getModel();

		DefaultComboBoxModel<String> newTrainModelTrains = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> newTrainControllerTrains = new DefaultComboBoxModel<String>();
		
		boolean trainModelChanged = false;
		boolean trainControllerChanged = false;
		
		newTrainModelTrains.addElement("<html><i>Select a Train</i></html>");
		newTrainControllerTrains.addElement("<html><i>Select a Train</i></html>");

		for(String key : simulator.ctc.trains.keySet()) {
			newTrainModelTrains.addElement(key);
			if(currentTrainModelTrains.getIndexOf(key)==-1) {	
				trainModelChanged = true;
			}

			newTrainControllerTrains.addElement(key);
			if(currentTrainControllerTrains.getIndexOf(key)==-1){	
				trainControllerChanged = true;
			}
		}
		
		if(trainModelChanged) {
			cbTrainModelTrains.setModel(newTrainModelTrains);			
		}
		
		if(trainControllerChanged){
			cbTrainControllerTrains.setModel(newTrainControllerTrains);
		}
	}
}
