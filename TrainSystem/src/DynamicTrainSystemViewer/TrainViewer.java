/*
 * Prototype Dynamic Rendering of Train System with Trains and Track Layout
 * Mostly just messing with Swing and getting practice for now
 * K.Le
 */

package DynamicTrainSystemViewer;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class TrainViewer {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		System.out.println("GUI successfully created: " + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Dynamic Train View");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new TrainPanel());
		f.pack();
		f.setVisible(true);
	}
}

class TrainPanel extends JPanel{

	private int panelWidth = 1000;
	private int panelHeight = 500;

	private int train_xPos = 0;
	private int train_yPos = panelHeight / 2;
	private int train_width = 50;
	private int train_height = 10;

	public TrainPanel() {
		JButton b = new JButton("Move Train");
		this.add(b);
		
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				repaint(train_xPos, train_yPos, train_width, train_height);
				train_xPos += 10;
				repaint(train_xPos, train_yPos, train_width, train_height);
			}
		});
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.black);

		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				// moveTrain(e.getX(), e.getY());
			}
		});
	}

	private void moveTrain(int x, int y) {
		int OFFSET = 1;
		if ((train_xPos != x) || (train_yPos != y)) {
			repaint(train_xPos, train_yPos, train_width, train_height);
			train_xPos = x;
			train_yPos = y;
			repaint(train_xPos, train_yPos, train_width, train_height);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(panelWidth, panelHeight);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Display Info Text
		int infoTextPosX = 20;
		int infoTextPosY = 20;
		g.setColor(Color.GREEN);
		g.drawString("This is the Dynamic Train System Viewer.", infoTextPosX, infoTextPosY);

		// Display the train
		g.setColor(Color.CYAN);
		g.fillRect(train_xPos, train_yPos, train_width, train_height);
		g.setColor(Color.BLACK);
		g.drawRect(train_xPos, train_yPos, train_width, train_height);
	}
}