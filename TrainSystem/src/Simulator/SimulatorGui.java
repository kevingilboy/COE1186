package Simulator;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class SimulatorGui {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorGui window = new SimulatorGui();
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
	public SimulatorGui() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
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

}
