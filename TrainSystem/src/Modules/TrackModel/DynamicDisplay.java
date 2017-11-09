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
import javax.swing.*;

public class DynamicDisplay{

	public int VERTICAL_FRAME_PADDING = 30;
	public int TRACK_LAYOUT_WIDTH = 334;
	public int TRACK_LAYOUT_HEIGHT = 448;
	public int SPLASH_WIDTH = 910;
	public int SPLASH_HEIGHT = 512;
	public int DYNAMIC_FRAME_WIDTH = TRACK_LAYOUT_WIDTH + SPLASH_WIDTH;
	public int DYNAMIC_FRAME_HEIGHT = SPLASH_HEIGHT + VERTICAL_FRAME_PADDING;
	public Color DYNAMIC_FRAME_BG_COLOR = new Color(33, 33, 33);

	private JFrame f;
	public JLabel splashImg = new JLabel();
	public JLabel trackImg = new JLabel();

	public DynamicDisplay(){
		initializeFrame();
	}

	public void initializeFrame(){
		f = new JFrame("Dynamic Simulation Display");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		f.setLayout(null);

		JPanel p = new JPanel();
		p.setBounds(0, 0, DYNAMIC_FRAME_WIDTH, DYNAMIC_FRAME_HEIGHT);
		p.setLayout(null);
		p.setBackground(DYNAMIC_FRAME_BG_COLOR);

		trackImg.setBounds(30, 30, TRACK_LAYOUT_WIDTH, TRACK_LAYOUT_HEIGHT);
		trackImg.setIcon(new ImageIcon("testTrack1.png"));
		trackImg.setSize(trackImg.getPreferredSize().width, trackImg.getPreferredSize().height);
		p.add(trackImg);
		f.add(p);

		splashImg.setBounds(TRACK_LAYOUT_WIDTH, 0, SPLASH_WIDTH, SPLASH_HEIGHT);
		splashImg.setIcon(new ImageIcon("HSS_splash.gif"));
		splashImg.setSize(splashImg.getPreferredSize().width, splashImg.getPreferredSize().height);
		p.add(splashImg);
		f.add(p);

		f.setVisible(true);
		f.setResizable(false);
	}

	public static void main(String[] args){
		new DynamicDisplay();
	}
}