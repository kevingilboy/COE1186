package Simulator;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Modules.Ctc.Line;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SimulatorGui {
	private enum Waysides{
		R1,R2,G1,G2;
	};
	
	protected Simulator simulator;
	
	private JFrame frame;
	private JComboBox<String> cbTrainModelTrains;
	private JComboBox<String> cbTrainControllerTrains;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorGui window = new SimulatorGui(null);
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
	public SimulatorGui(Simulator sim) {
		simulator = sim;
		
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.getContentPane().setLayout(null);
		
		/*
		 * ------------------------------
		 *  CTC
		 * ------------------------------
		 */
		JButton btnCtc = new JButton("CTC");
		btnCtc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCtc.setBounds(26, 52, 171, 41);
		frame.getContentPane().add(btnCtc);
		
		/*
		 * ------------------------------
		 *  TRACK CONTROLLER
		 * ------------------------------
		 */
		JButton btnWaysideR1 = new JButton("R1");
		btnWaysideR1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.R1);
			}
		});
		btnWaysideR1.setBounds(26, 121, 65, 41);
		frame.getContentPane().add(btnWaysideR1);
		
		JButton btnWaysideR2 = new JButton("R2");
		btnWaysideR2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.R2);
			}
		});
		btnWaysideR2.setBounds(101, 121, 65, 41);
		frame.getContentPane().add(btnWaysideR2);
		
		JButton btnWaysideG1 = new JButton("G1");
		btnWaysideG1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.G1);
			}
		});
		btnWaysideG1.setBounds(188, 121, 65, 41);
		frame.getContentPane().add(btnWaysideG1);
		
		JButton btnWaysideG2 = new JButton("G2");
		btnWaysideG2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKCONTROLLER,Waysides.G2);
			}
		});
		btnWaysideG2.setBounds(275, 121, 65, 41);
		frame.getContentPane().add(btnWaysideG2);
		
		/*
		 * ------------------------------
		 *  TRACK MODEL
		 * ------------------------------
		 */
		JButton btnTrackModel = new JButton("Track Model");
		btnTrackModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRACKMODEL);
			}
		});
		btnTrackModel.setBounds(26, 186, 171, 41);
		frame.getContentPane().add(btnTrackModel);
		
		/*
		 * ------------------------------
		 *  TRAIN MODEL
		 * ------------------------------
		 */
		cbTrainModelTrains = new JComboBox<String>();
		cbTrainModelTrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName = (String)cbTrainModelTrains.getSelectedItem();
				openGui(ModuleType.TRAINMODEL,trainName);
			}
		});
		cbTrainModelTrains.setBounds(26, 255, 52, 39);
		frame.getContentPane().add(cbTrainModelTrains);
		
		/*
		 * ------------------------------
		 *  TRAIN CONTROLLER
		 * ------------------------------
		 */
		JButton btnTraincontroller = new JButton("TrainController");
		btnTraincontroller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.TRAINCONTROLLER);
			}
		});
		btnTraincontroller.setBounds(26, 332, 171, 41);
		frame.getContentPane().add(btnTraincontroller);
		
		cbTrainControllerTrains = new JComboBox<String>();
		cbTrainControllerTrains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trainName = (String)cbTrainControllerTrains.getSelectedItem();
				openGui(ModuleType.TRAINCONTROLLER,trainName);
			}
		});
		cbTrainControllerTrains.setBounds(223, 333, 52, 39);
		frame.getContentPane().add(cbTrainControllerTrains);
		
		/*
		 * ------------------------------
		 *  MBO
		 * ------------------------------
		 */
		JButton btnMbo = new JButton("MBO");
		btnMbo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGui(ModuleType.MBO);
			}
		});
		btnMbo.setBounds(26, 391, 171, 41);
		frame.getContentPane().add(btnMbo);
		
	}

	protected void moduleObjectInitialized(ModuleType module) {
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
				//...
				break;
			case TRACKCONTROLLER:
				//...
				break;
			case TRACKMODEL:
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
	private void openGui(ModuleType module, Waysides wayside) {
		//We can use this to open a respective Module's GUI
		switch (module) {
			case TRACKCONTROLLER:
				//...
				break;
		}
	}
	private void openGui(ModuleType module, String trainName) {
		//We can use this to open a respective Module's GUI
		switch (module) {
			case TRAINMODEL:
				//...
				break;
			case TRAINCONTROLLER:
				//...
				break;	
		}
	}
	
	protected void repaint() {
		DefaultComboBoxModel<String> currentTrains = (DefaultComboBoxModel<String>)cbTrainControllerTrains.getModel();
		DefaultComboBoxModel<String> newTrains = new DefaultComboBoxModel<String>();
		
		Boolean changed = false;
		
		for(String key : simulator.ctc.trains.keySet()) {
			if(currentTrains.getIndexOf(key)==-1) {
				changed = true;
			}
			newTrains.addElement(key);
		}
		
		if(changed) {
			cbTrainControllerTrains.setModel(newTrains);
			cbTrainModelTrains.setModel(newTrains);			
		}
	}
}
