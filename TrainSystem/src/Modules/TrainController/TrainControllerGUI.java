//Michael Kotcher

package Modules.TrainController;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class TrainControllerGUI {

	private int yCount;
	private int height;
	private int logoHeight;
	
	private JFrame frame;

	private JPanel contentPane;
	
	private ArrayList<TrnControllerGUI> guiList;
	private ArrayList<JButton> buttonList;
	
	private JLabel logoPineapple = new JLabel(new ImageIcon("pineapple_icon.png"));

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
		guiList = new ArrayList<TrnControllerGUI>();
		buttonList = new ArrayList<JButton>();
		yCount = 80;
		height = 350;
		logoHeight = 210;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titleLabel = new JLabel("Train Controller Module");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		titleLabel.setBounds(118, 17, 200, 42);
		contentPane.add(titleLabel);
		
		logoPineapple.setBounds(300, logoHeight, 150, 150);
		contentPane.add(logoPineapple);
		
		frame.setVisible(true);
		
		//add("train 1");
		//add("train 2");
		//updateGUI();
		//frame.setVisible(true);
	}
	
	public void add(TrnControllerGUI g) {
		System.out.println("add");
		guiList.add(g);
		JButton B = new JButton(g.getId());
		buttonList.add(B);
		B.setBounds(30, yCount, 120, 30);
		B.setVisible(true);
		B.setEnabled(true);
		contentPane.add(B);
		yCount = yCount + 60;
		if (yCount >= (height - 60)) {
			height = height + 60;
			logoHeight = logoHeight + 60;
			frame.setBounds(100, 100, 450, height);
			logoPineapple.setBounds(300, logoHeight, 150, 150);
		}
		frame.repaint();
	}
}
