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
import java.lang.Thread;

public class TrackRenderWindow extends JPanel implements ActionListener{  

    // Timer used to update the dynamic track view
    Timer timer;
    
    // Track data structure whose information will be
    // used for the dynamic rendering
    ArrayList<Block> blocks = new ArrayList<Block>();
    Color lineColor;
    int blockPos = 0;
    int meterPos = 0;

    public TrackRenderWindow(int width, int height, ArrayList<Block> blocks){
        initializeWindow(width, height);
        initializeTimer();
        initializeTrack(blocks);
    }

    // Initialize the window in the context 
    // of the DynamicDisplay GUI window
    public void initializeWindow(int width, int height){
        setBounds(0, 0, width, height);
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        setVisible(true);
    }

    // Initialize the timer
    public void initializeTimer(){
        timer = new Timer(30, this); // milliseconds
        timer.setInitialDelay(0);
        timer.start(); // Start the timer
    }

    // Initialize the track
    public void initializeTrack(ArrayList<Block> blocks){
        String line = blocks.get(0).getLine();

        if (line.equals("green")){
            lineColor = Color.green;
        } else if (line.equals("red")){
            lineColor = Color.red;
        } else {
            System.out.println("No track line color found!");
        }

        this.blocks = blocks;
    }

    // Draw the screen (gets refreshed every time the timer is
    // called, trigger the actionPerformed() listener to call 
    // repaint()
    public void paintComponent (Graphics g)     
    {
        super.paintComponent(g);
        Graphics2D g2d  = (Graphics2D) g;

        // Draw black track shadow
        g2d.setColor(Color.black);
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j]+1, (int)y_coords[j]+3, 
                            4, 4);
            }
        }

        // Draw yellow station outlines
        g2d.setColor(Color.yellow);
        for (int i = 0; i < blocks.size(); i++){
            if ((blocks.get(i).getStation()) != null){
                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                for (int j = 0; j < x_coords.length-2; j++){
                    g2d.drawRect((int)x_coords[j]-2, (int)y_coords[j]-2, 
                                6, 6);
                }
            }
        }

        // Draw the track's line color
        g2d.setColor(lineColor);
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j]-1, (int)y_coords[j]-1, 
                            4, 4);
            }
        }

        // Draw the inner black track to make it look like
        // real track rails colored as the line color
        g2d.setColor(Color.black);
        for (int i = 0; i < blocks.size(); i++){
            if (i == blockPos){
                g2d.setColor(lineColor);
            } else {
                g2d.setColor(Color.black);
            }

            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j], (int)y_coords[j], 
                            2, 2);
            }
        }

        // Draw the train (currently just iterates through each
        // position along the track, will later just appear at
        // the current block position occupied in the simulation)
        g2d.setColor(Color.white);

        double[] xy_coords = blocks.get(blockPos).getCoordinatesAtMeter(meterPos);
        double x_coord = xy_coords[0];
        double y_coord = xy_coords[1];
        g2d.fillRect((int)x_coord-1, (int)y_coord-1, 5, 5);

        // Draw the coordinates of the train's current location 
        // on top of the train
        g2d.setColor(Color.white);
        g2d.drawString("(" + String.format("%.2f", x_coord) + ", " + String.format("%.2f", y_coord) + ")", (int)x_coord - 50, (int)y_coord - 15);

        // Draw the currently occupied block position
        // on top of the train
        g2d.setFont(new Font("default", Font.BOLD, 12));
        g2d.drawString(blocks.get(blockPos).getSection() + Integer.toString(blocks.get(blockPos).getId()) + ", " + Integer.toString(meterPos),
                        (int)x_coord - 30, 
                        (int)y_coord - 5);
        g2d.setFont(new Font("default", Font.PLAIN, 12));

        // Increment the train's block position.
        // If the incremented number of meters overflows to the
        // next block, move to overflow amount of meters along
        // the next block.
        meterPos += (int)(blocks.get(blockPos).getLength() / 10);
        if (meterPos >= blocks.get(blockPos).getLength()){
            meterPos = meterPos - (int)blocks.get(blockPos).getLength();
            blockPos++;
            if (blockPos >= blocks.size()){
                blockPos = 0;
            }
        }
    }

    // Calls paintComponent(Graphics g) every time
    // the timer goes off
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
} 