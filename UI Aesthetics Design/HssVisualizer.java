import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class HssVisualizer extends JFrame{

	public static final int TYPE_NORMAL_LABEL = 0;
	public static final int TYPE_HEADER_LABEL = 1;
	public static final int TYPE_GREEN_STATUS_ICON = 0;
	public static final int TYPE_GRAY_STATUS_ICON = 1;

	public HssVisualizer(JFrame frame){
		initializeFrame(frame);

		try {
			String laf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
			UIManager.setLookAndFeel(laf);
		} catch(Throwable e) {
			System.out.println("HssVisualizer Error: Cannot set Windows look and feel");
		}

		try {
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Light.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("RobotoCondensed-Lightitalic.ttf")));
		} catch (IOException|FontFormatException e) {
		    System.out.println("HssVisualizer Error: Cannot load custom font");
		}

		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}	

	public void stylizeLabel(JLabel l, int type){
		int labelPadding = 4;

		if (type == TYPE_NORMAL_LABEL){
			l.setForeground(Color.WHITE);
			l.setFont(new Font("Roboto", Font.PLAIN, 18));
		} else if (type == TYPE_HEADER_LABEL){
			l.setForeground(Color.WHITE);
			l.setFont(new Font("Roboto Condensed", Font.PLAIN, 34));
			l.setText((l.getText()).toUpperCase());
		}

		l.setSize(	l.getPreferredSize().width + labelPadding,
					l.getPreferredSize().height + labelPadding );
	}

	public void stylizeStatusIcon(JLabel icon, int type){
		if (type == TYPE_GREEN_STATUS_ICON){
			icon.setIcon(new ImageIcon("statusIconGreen.png"));
		} else if (type == TYPE_GRAY_STATUS_ICON){
			icon.setIcon(new ImageIcon("statusIconGray.png"));
		}
		icon.setSize(	icon.getPreferredSize().width, 
						icon.getPreferredSize().height 	);
	}

	/*
	 * Set a button's style using a wrapper function
	 * Includes font, dimensions, padding, and
	 * adds color transitions on click and hover
	 */
	public void stylizeBtn(JButton b){

		b.setFont(new Font("Roboto Condensed", Font.PLAIN, 28 ));

    	b.setSize(	b.getPreferredSize().width,
					b.getPreferredSize().height);
	}

	public void initializeFrame(JFrame frame){
		frame.setResizable(false); 	// Do not allow resizability
		frame.setLayout(null); 		// Use Absolute layout
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon img = new ImageIcon("HSS_Window_Icon.png");
		frame.setIconImage(img.getImage());
	}
}