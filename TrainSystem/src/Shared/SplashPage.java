import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class SplashPage {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SplashPage window = new SplashPage();
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
	public SplashPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private int IMAGE_HEIGHT = 768;
	private int IMAGE_WIDTH = 1360;
	private int BOTTOM_BAR_HEIGHT = 100;
	private int BOTTOM_BAR_WIDTH = IMAGE_WIDTH;
	private int BUTTON_MARGIN_TOP = 20;
	private int BUTTON_MARGIN_LR = 50;
	private int BUTTON_HEIGHT = (int)(BOTTOM_BAR_HEIGHT-2*BUTTON_MARGIN_TOP);
	private int BUTTON_WIDTH = (int)((BOTTOM_BAR_WIDTH-(7*BUTTON_MARGIN_LR))/6.0);
	private String PICTURE ="icon.jpg";
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, IMAGE_WIDTH, IMAGE_HEIGHT+BOTTOM_BAR_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel(new ImageIcon(PICTURE));
		label.setBounds(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		frame.getContentPane().add(label);
		
		int x = BUTTON_MARGIN_LR;
		String[] modules = new String[] {"CTC","Track Ctrl","Track Model","Train Model","Train Ctrl","MBO"};
		for(int i=0;i<6;i++) {
			JButton newButton = new JButton(modules[i]);
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//OPEN JAR
					
				}
			});
			System.out.println(x+" "+(IMAGE_HEIGHT+BUTTON_MARGIN_TOP)+" "+BUTTON_WIDTH+" "+BUTTON_HEIGHT);
			newButton.setBounds(x, IMAGE_HEIGHT+BUTTON_MARGIN_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
			x+=BUTTON_MARGIN_LR+BUTTON_WIDTH;
			frame.getContentPane().add(newButton);
		}
		
	}
}
