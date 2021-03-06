import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JButton;

import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class SplashPage {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Throwable e) {
			//lmfao
		}
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
	private int LEFT_BAR_HEIGHT = IMAGE_HEIGHT;
	private int LEFT_BAR_WIDTH = 200;
	private int BUTTON_MARGIN_TOP = 50;
	private int BUTTON_MARGIN_LR = 20;
	private int BUTTON_HEIGHT = (int)((LEFT_BAR_HEIGHT-7*BUTTON_MARGIN_TOP)/6.0);
	private int BUTTON_WIDTH = LEFT_BAR_WIDTH-2*BUTTON_MARGIN_LR;
	private String[] modules = new String[] {"CTC","Track Ctrl","Track Model","Train Model","Train Ctrl","MBO"};
	private final String[] moduleJarCmds = new String[]{
		"java -cp C:\Users\EEKevin\Desktop\Modules\CTC",
		"java -cp C:\Users\EEKevin\Desktop\Modules\TrackCtrl",
		"java -cp C:\Users\EEKevin\Desktop\Modules\TrackModel",
		"java -cp C:\Users\EEKevin\Desktop\Modules\TrainModel",
		"java -cp C:\Users\EEKevin\Desktop\Modules\TrainCtrl",
		"java -cp C:\Users\EEKevin\Desktop\Modules\MBO",
		};
	
	private String PICTURE ="icon.jpg";
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, IMAGE_WIDTH+LEFT_BAR_WIDTH, IMAGE_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel img = new JLabel(new ImageIcon("icon.jpg"));
		img.setBounds(LEFT_BAR_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		frame.getContentPane().add(img);
		
		int y = BUTTON_MARGIN_TOP;

		JButton newButton = new JButton(modules[0]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[0]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
		
		y += BUTTON_HEIGHT+BUTTON_MARGIN_TOP;

		newButton = new JButton(modules[1]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[1]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
		
		y += BUTTON_HEIGHT+BUTTON_MARGIN_TOP;

		newButton = new JButton(modules[2]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[2]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
		
		y += BUTTON_HEIGHT+BUTTON_MARGIN_TOP;

		newButton = new JButton(modules[3]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[3]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
		
		y += BUTTON_HEIGHT+BUTTON_MARGIN_TOP;

		newButton = new JButton(modules[4]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[4]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
		
		y += BUTTON_HEIGHT+BUTTON_MARGIN_TOP;

		newButton = new JButton(modules[5]);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Runtime.getRuntime().exec(moduleJarCmds[5]);
				}
				catch(Throwable e){
					//lmao wat
				}
			}
		});
		stylizeButton(newButton);
		newButton.setBounds(0+BUTTON_MARGIN_LR, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		frame.getContentPane().add(newButton);
	}

	public void stylizeButton(JButton b){
		Border thickBorder = new LineBorder(Color.WHITE, 3);
		b.setFont(new Font("Arial", Font.BOLD, 18));
    	b.setBorder(thickBorder);
		b.setContentAreaFilled(false);
		b.setOpaque(true);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}
}
