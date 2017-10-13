import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

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
	private JButton button2;

	public HashSlingingVisuals(){
		initializeFrame();
		initializeButtons();
	}	

	public void initializeButtons(){
		button1 = new JButton("Normal Button");
		button2 = new JButton("Critical Button");

		stylizeBtn(button1);
		stylizeBtnCritical(button2);

		button1.setBounds(0,0,button1.getWidth(),button1.getHeight());
		button2.setBounds(button1.getWidth() + 10,0,button2.getWidth(),button2.getHeight());
		mainPanel.add(button1);
		mainPanel.add(button2);
	}



	/*
	 * Set a button's style using a wrapper function
	 * Includes font, dimensions, padding, and
	 * adds color transitions on click and hover
	 */
	public void stylizeBtn(JButton b){
		Border buttonBorder 	= new LineBorder(new Color(20, 20, 20), 2);
		int buttonInnerPadding 	= 20; 

		Color 	defaultBgColor 	= new Color(50, 50, 50),
				defaultTxtColor = Color.WHITE,
				hoverBgColor 	= Color.BLACK,
				hoverTxtColor 	= Color.GREEN,
				pressedBgColor 	= Color.GREEN,
				pressedTxtColor = new Color(20, 20, 20);

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
				b.setBackground(hoverBgColor);
				b.setForeground(hoverTxtColor);
			}
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent e){
				b.setBackground(hoverBgColor);
				b.setForeground(hoverTxtColor);
			}
			public void mouseExited(MouseEvent e){
				b.setBackground(defaultBgColor);
				b.setForeground(defaultTxtColor);
			}
		});
	}

	public void stylizeBtnCritical(JButton b){
		Border buttonBorder 	= new LineBorder(new Color(100, 10, 0), 2);
		int buttonInnerPadding 	= 20; 

		Color 	defaultBgColor 	= Color.RED,
				defaultTxtColor = Color.WHITE,
				hoverBgColor 	= new Color(255, 100, 0),
				hoverTxtColor 	= new Color(100, 0, 0),
				pressedBgColor 	= Color.RED,
				pressedTxtColor = Color.BLACK;

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
				b.setBackground(hoverBgColor);
				b.setForeground(hoverTxtColor);
			}
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent e){
				b.setBackground(hoverBgColor);
				b.setForeground(hoverTxtColor);
			}
			public void mouseExited(MouseEvent e){
				b.setBackground(defaultBgColor);
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
		mainPanel.setLayout(null);
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