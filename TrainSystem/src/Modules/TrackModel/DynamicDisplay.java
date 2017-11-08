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

public class DynamicDisplay{

	private JFrame f;
	
	public int DYNAMIC_FRAME_WIDTH = 500;
	public int DYNAMIC_FRAME_HEIGHT = 500;
	public Color DYNAMIC_FRAME_BG_COLOR = Color.BLACK;

	public DynamicDisplay(){
		initializeFrame();
	}

	public void initializeFrame(){
		f = new JFrame("Dynamic Simulation Display");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		f.setVisible(true);
	}

	public static void main(String[] args){
		new DynamicDisplay();
	}
}