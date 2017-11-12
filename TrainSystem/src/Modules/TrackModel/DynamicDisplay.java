package Modules.TrackModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DynamicDisplay{

	/* Constant GUI properties */
	public int VERTICAL_FRAME_PADDING = 30;
	public int TRACK_LAYOUT_WIDTH = 334;
	public int TRACK_LAYOUT_HEIGHT = 448;
	public int SPLASH_WIDTH = 910;
	public int SPLASH_HEIGHT = 512;
	public int DYNAMIC_FRAME_WIDTH = TRACK_LAYOUT_WIDTH + SPLASH_WIDTH;
	public int DYNAMIC_FRAME_HEIGHT = SPLASH_HEIGHT + VERTICAL_FRAME_PADDING;
	public Color DYNAMIC_FRAME_BG_COLOR = new Color(33, 33, 33);

	/* Initialize GUI components */
	private JFrame f;
	private JPanel p = new JPanel();

	public JLabel splashImg = new JLabel();
	public JLabel trackImg = new JLabel();

	private static JPanel trainRect = new JPanel();

	public DynamicDisplay(){
		initializeFrame();
	}

	/* Initialize the Frame, set background components */
	public void initializeFrame(){

		/* Initialize main frame */
		f = new JFrame("Dynamic Simulation Display");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		f.setLayout(null);
		f.setUndecorated(true);

		/* Allow draggability of frame */
		FrameDragListener frameDragListener = new FrameDragListener(f);
		f.addMouseListener(frameDragListener);
		f.addMouseMotionListener(frameDragListener);
		f.setLocationRelativeTo(null);

		/* Format frame inner panel */
		p.setBounds(0, 0, DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		p.setLayout(null);
		p.setBackground(DYNAMIC_FRAME_BG_COLOR);

		/* Add track layout image */
		trackImg.setBounds(30, 30, TRACK_LAYOUT_WIDTH, TRACK_LAYOUT_HEIGHT);
		trackImg.setIcon(new ImageIcon("testTrack1.png"));
		trackImg.setSize(trackImg.getPreferredSize().width, trackImg.getPreferredSize().height);
		p.add(trackImg);

		/* Add "HSS Train Simulator" animation */
		splashImg.setBounds(TRACK_LAYOUT_WIDTH, 0, SPLASH_WIDTH, SPLASH_HEIGHT);
		splashImg.setIcon(new ImageIcon("HSS_splash.gif"));
		splashImg.setSize(splashImg.getPreferredSize().width, splashImg.getPreferredSize().height);
		p.add(splashImg);
		f.add(p);

		/* Show Frame */
		f.setVisible(true);
		f.setResizable(false);
	}

	/* MAIN */
	public static void main(String[] args){
		new DynamicDisplay();
	}
	
	/* INTERNAL CLASSES */

	/* Allow border-less window to be moved on click & drag */
	public static class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        // Relocate the frame relative to the position dragged
        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}