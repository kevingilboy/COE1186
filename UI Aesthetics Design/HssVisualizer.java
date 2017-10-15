import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class HssVisualizer extends JFrame{

	/* Normal text size configuration */
	public static final int TYPE_NORMAL_LABEL = 0;
	public static final int TYPE_HEADER_LABEL = 1;

	/* Header text size configuration */
	public static final int TYPE_GREEN_STATUS_ICON = 0;
	public static final int TYPE_GRAY_STATUS_ICON = 1;

	/* Configure JFrame upon class constructor call */
	public HssVisualizer(JFrame frame){
		configureFrame(frame);
	}	

	/* Label style wrapper */
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

	/* Status icon style wrapper */
	public void stylizeStatusIcon(JLabel icon, int type){
		if (type == TYPE_GREEN_STATUS_ICON){
			icon.setIcon(new ImageIcon("resources/images/statusIconGreen.png"));
		} else if (type == TYPE_GRAY_STATUS_ICON){
			icon.setIcon(new ImageIcon("resources/images/statusIconGray.png"));
		}
		icon.setSize(	icon.getPreferredSize().width, 
						icon.getPreferredSize().height 	);
	}

	/* Button style wrapper */
	public void stylizeButton(JButton b){
		b.setFont(new Font("Roboto Condensed", Font.PLAIN, 28 ));

    	b.setSize(	b.getPreferredSize().width,
					b.getPreferredSize().height);
	}

	/* Configure primary JFrame:
	 * - Configure layout and resizability
	 * - Set window corner icon
	 * - Set Look & Feel
	 * - Load custom fonts
	 * - Enable antialiasing 
	 */
	public void configureFrame(JFrame frame){
		frame.setResizable(false); 	// Do not allow resizability
		frame.setLayout(null); 		// Use Absolute layout
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon img = new ImageIcon("resources/images/HSS_Window_Icon.png");
		frame.setIconImage(img.getImage());

		try {
			String laf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
			UIManager.setLookAndFeel(laf);
		} catch(Throwable e) {
			System.out.println("HssVisualizer Error: Cannot set Windows look and feel");
		}

		try {
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Roboto-Light.ttf")));
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/RobotoCondensed-Lightitalic.ttf")));
		} catch (IOException|FontFormatException e) {
		    System.out.println("HssVisualizer Error: Cannot load custom font");
		}

		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
}