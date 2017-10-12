import java.awt.EventQueue;


import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;;

public class TrainModelGUI {

	private JFrame TrainModelFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainModelGUI window = new TrainModelGUI();
					window.TrainModelFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrainModelGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		TrainModelFrame = new JFrame();
		TrainModelFrame.setTitle("Train 1");
		TrainModelFrame.setBounds(100, 100, 1013, 621);
		TrainModelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		TrainModelFrame.getContentPane().setLayout(null);
		
		JLabel specifications = new JLabel("Specifications");
		/*gbc_lblSpecifications.anchor = GridBagConstraints.WEST;
		gbc_lblSpecifications.insets = new Insets(8, 8, 5, 5);
		gbc_lblSpecifications.gridx = 0;
		gbc_lblSpecifications.gridy = 0;*/
		specifications.setBounds(2, 1, 1, 1);
		TrainModelFrame.getContentPane().add(specifications);
		
		JLabel trackInformation = new JLabel("Track Information");
		trackInformation.setBounds(8, 1, 1, 1);
		TrainModelFrame.getContentPane().add(trackInformation);
		
		
	}

}
