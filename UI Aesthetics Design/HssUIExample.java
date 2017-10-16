import java.awt.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.*;

@SuppressWarnings("serial")
public class HssUIExample extends JFrame{

	/* Constants */
	private static final int WINDOW_WIDTH 	= 1000;
	private static final int WINDOW_HEIGHT 	= 800;

	/* J-Components */
	private static JFrame frame = new JFrame();
	private static JPanel mainPanel;
	private static JButton button1;
	private static JLabel label1;
	private static JLabel label2;
	private static JLabel statusIcon1; 

	/* Instantiate Visualization Class */
	private static HssVisualizer visualizer = new HssVisualizer(frame);

	/* Variables */
	private static boolean statusOn = false;

	/* Main Function */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run(){
				initializeGUI();
			}
		});
	}

	/* Create Buttons */
	public static void initializeButtons(){
		button1 = new JButton("Button");
		visualizer.stylizeButton(button1);
		button1.setBounds(0, 0, button1.getWidth(), button1.getHeight());

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				int statusLightType;
				if (statusOn){`
					statusLightType = HssVisualizer.TYPE_GREEN_STATUS_ICON;
				} else {
					statusLightType = HssVisualizer.TYPE_GRAY_STATUS_ICON;
				}
				visualizer.stylizeStatusIcon(statusIcon1, statusLightType);
				statusOn = !statusOn;
			}
		});

		mainPanel.add(button1);
	}

	/* Create Labels */
	public static void initializeLabels(){
		label1 = new JLabel("Normal Label");
		label2 = new JLabel("Header Label");

		visualizer.stylizeLabel(label1, HssVisualizer.TYPE_NORMAL_LABEL);
		visualizer.stylizeLabel(label2, HssVisualizer.TYPE_HEADER_LABEL);

		label1.setBounds(0, button1.getHeight(), label1.getWidth(), label1.getHeight());
		label2.setBounds(0, button1.getHeight() + label1.getHeight(), label2.getWidth(), label2.getHeight());

		mainPanel.add(label1);
		mainPanel.add(label2);
	}

	/* Create Status Icons */
	public static void initializeStatusIcons(){
		statusIcon1 = new JLabel();
		
		visualizer.stylizeStatusIcon(statusIcon1, HssVisualizer.TYPE_GREEN_STATUS_ICON);

		statusIcon1.setBounds(button1.getWidth(), 0, statusIcon1.getWidth(), statusIcon1.getHeight());
		mainPanel.add(statusIcon1);
	}

	/* Initialize the GUI */
	public static void initializeGUI(){
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainPanel.setBackground(new Color(30, 30, 30));
		frame.add(mainPanel);

		initializeButtons();
		initializeLabels();
		initializeStatusIcons();

		frame.setVisible(true);
	}
}