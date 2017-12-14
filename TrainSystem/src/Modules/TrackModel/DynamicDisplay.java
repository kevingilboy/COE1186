/**
 * COE 1186
 * DynamicDisplay.java
 * 
 * Window for the dynamic track rendering
 * used to display the system visually.
 *
 * @author Kevin Le
 * @version 1.0 12/14/2017
 */

package Modules.TrackModel;

import Modules.TrainModel.Position;

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
	public int TRACK_LAYOUT_WIDTH = 2*334;
	public int TRACK_LAYOUT_HEIGHT = 2*448;

	// Dimensions of the overall GUI, currently set to the
	// dimensions of the track layout view
	public int DYNAMIC_FRAME_WIDTH = TRACK_LAYOUT_WIDTH + VERTICAL_FRAME_PADDING;
	public int DYNAMIC_FRAME_HEIGHT = TRACK_LAYOUT_HEIGHT + VERTICAL_FRAME_PADDING;
	public Color DYNAMIC_FRAME_BG_COLOR = new Color(33, 33, 33);

	// Initialize GUI frame and components
	private JFrame f;
	public TrackRenderWindow dynamicTrackView;

	// Track assigned to this dynamic display
	private ArrayList<Block> blocks;

	public DynamicDisplay(ArrayList<Block> blocks){

		this.blocks = blocks;

		// Generate the graphics rendering window embedded in this display
		dynamicTrackView = new TrackRenderWindow(TRACK_LAYOUT_WIDTH, TRACK_LAYOUT_HEIGHT, blocks);
		initializeFrame();
	}

	/**
	 * Initialize the main frame and GUI components
	 */
	public void initializeFrame(){

		// Main frame
		f = new JFrame("Dynamic Simulation Display");
		f.setSize(DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		f.setUndecorated(true);
		f.setBackground(Color.BLACK);
		f.setLayout(null);
		f.setLocationRelativeTo(null);
		f.setResizable(false);

		// Add the graphics rendering window
		f.add(dynamicTrackView);

		// Toggle track direction view mode in the 
		// graphics rendering window
		dynamicTrackView.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dynamicTrackView.showArrows = !dynamicTrackView.showArrows;
				dynamicTrackView.refresh();
			}
		});
	}

    /**
     * Add a dispatched train to the dynamic display.
     * 
     * @param trainID : identifier of train dispatched
     * @param pos     : the train's associated position
     */
	public void dispatchTrain(String trainID, Position pos){
		dynamicTrackView.dispatchTrain(trainID, pos);
	}

	/**
	 * Remove a train currently in a dynamic display
	 * that has returned to the yard.
	 * 
	 * @param name : identifier of the train to remove
	 */
	public void trainPoofByName(String name) {
		dynamicTrackView.trainPoofByName(name);
	}
}