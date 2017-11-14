package Modules.TrackModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DynamicDisplay{

	// Dimensions of the track layout view
	public int VERTICAL_FRAME_PADDING = 0;
	public int TRACK_LAYOUT_WIDTH = 334;
	public int TRACK_LAYOUT_HEIGHT = 448;

	// Dimensions of the overall GUI, currently set to the
	// dimensions of the track layout view
	public int DYNAMIC_FRAME_WIDTH = TRACK_LAYOUT_WIDTH;
	public int DYNAMIC_FRAME_HEIGHT = TRACK_LAYOUT_HEIGHT + VERTICAL_FRAME_PADDING;
	public Color DYNAMIC_FRAME_BG_COLOR = new Color(33, 33, 33);

	// Initialize GUI window and components
	private JFrame f;
	private JPanel dynamicTrackView;

	// Instantiate dynamic display when Track Model blocks have
	// been generated from parsed layout file
	public DynamicDisplay(ArrayList<Block> blocks){

		dynamicTrackView = new TrackRenderWindow(TRACK_LAYOUT_WIDTH, TRACK_LAYOUT_HEIGHT, blocks);
		initializeFrame();
	}

	// Initialize the main frame and GUI components
	public void initializeFrame(){

		// Main frame
		f = new JFrame("Dynamic Simulation Display");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		f.setLayout(null);
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setVisible(true);

		// Make the frame draggable
		FrameDragListener frameDragListener = new FrameDragListener(f);
		f.addMouseListener(frameDragListener);
		f.addMouseMotionListener(frameDragListener);

		// Add the dynamic track view panel
		f.add(dynamicTrackView);
	}

	// FrameDragListener class to enable enable draggable
	// frame, used for dynamic track rendering since the
	// window will not have a border or minimize / maximize / close
	// buttons
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