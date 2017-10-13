import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class HashSlingingVisuals extends JFrame{

	private static final int WINDOW_WIDTH 	= 1000;
	private static final int WINDOW_HEIGHT 	= 800;
	
	private JFrame frame;
	private JPanel mainPanel;

	private JButton button1;
	private boolean clicked = false;

	public HashSlingingVisuals(){
		initializeFrame();
		initializeButtons();
	}	

	public void initializeButtons(){
		button1 = new JButton("button 1");
		stylizeBtn(button1);
		frame.add(button1);
	}



	/*
	 * Set a button's style using a wrapper function
	 * Includes font, dimensions, padding, and
	 * adds color transitions on click and hover
	 */
	public void stylizeBtn(JButton b){
		Border buttonBorder 	= new LineBorder(new Color(20, 20, 20), 2);
		int buttonInnerPadding 	= 20; 

		Color defaultBgColor = new Color(50, 50, 50);
		Color defaultTxtColor = Color.WHITE;
		Color hoverBgColor = defaultBgColor;
		Color hoverTxtColor = Color.GREEN;
		Color pressedBgColor = Color.GREEN;
		Color pressedTxtColor = new Color(20, 20, 20);

		b.setContentAreaFilled(false);
		b.setOpaque(true);
		
		b.setBackground(defaultBgColor);
		b.setForeground(defaultTxtColor);
    	b.setBorder(buttonBorder);
		
		b.setText((b.getText()).toUpperCase());
		b.setFont(new Font("Roboto Condensed", Font.BOLD, 18));
		// Correctly sizes a button to display its text content
		b.setSize(	b.getPreferredSize().width + buttonInnerPadding,
					b.getPreferredSize().height + buttonInnerPadding );

		b.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e){
				b.setBackground(pressedBgColor);
				b.setForeground(pressedTxtColor);
			}
			public void mouseReleased(MouseEvent e){
				b.setBackground(defaultBgColor);
				b.setForeground(defaultTxtColor);
			}
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent e){
				b.setBackground(hoverBgColor);
				b.setForeground(hoverTxtColor);
			}
			public void mouseExited(MouseEvent e){
				b.setForeground(defaultTxtColor);
			}
		});
	}

	/*
	 * Configure the window dimensions, background color, and icon
	 */
	public void initializeFrame(){
		frame = new JFrame("Hash Slinging Visuals");
		frame.setLayout(null); 		// Use Absolute layout
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false); 	// Do not allow resizability
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon img = new ImageIcon("HSS_Window_Icon.png");
		frame.setIconImage(img.getImage());

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		mainPanel.setBackground(new Color(30, 30, 30));

		frame.add(mainPanel);
	}

   /*
	* Create the GUI window and set the look and feel
	*/
	public static void main(String[] args){
		try {
			String laf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			UIManager.setLookAndFeel(laf);
		} catch(Throwable e) {}
		EventQueue.invokeLater(new Runnable() {
			public void run(){
				try{
					HashSlingingVisuals window = new HashSlingingVisuals();

					window.frame.setVisible(true);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}