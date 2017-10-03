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