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
import java.lang.Thread;

public class TrackRenderWindow extends JPanel implements ActionListener{  

    // Timer used to update the dynamic track view
    Timer timer;
    
    // Track data structure whose information will be
    // used for the dynamic rendering
    ArrayList<Block> blocks;
    Color lineColor;
    Position pos;

    int blockPos = 0;
    int meterPos = 0;

    public TrackRenderWindow(int width, int height, ArrayList<Block> blocks){
        initializeWindow(width, height);
        initializeTimer();
        initializeTrack(blocks);
        pos = new Position(blocks);
    }

    // Initialize the window in the context 
    // of the DynamicDisplay GUI window
    public void initializeWindow(int width, int height){
        int windowPadding = 5;
        setBounds(windowPadding, windowPadding, width, height);
        setLayout(null);
        setBackground(Color.BLACK);
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
        
        g2d.setColor(new Color(25, 25, 25));
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j]+1, (int)y_coords[j]+3, 
                            4, 4);
            }
        }

        // Draw yellow station outlines
        g2d.setColor(Color.gray);
        for (int i = 0; i < blocks.size(); i++){
            if ((blocks.get(i).getStation()) != null){
                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                for (int j = 0; j < x_coords.length-2; j++){
                    g2d.drawRect((int)x_coords[j]-3, (int)y_coords[j]-3, 
                                8, 8);
                }
            }
        }

        // Draw the track's line color
        // g2d.setColor(lineColor);
        g2d.setColor(Color.DARK_GRAY);
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

        // Draw the switches in their current state
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getSwitch() != null){

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_TAIL){
                    int headID = blocks.get(i).getSwitch().getPortNormal();
                    boolean state = blocks.get(headID).getSwitch().getState();
                    int port = 0;

                    if (state == Switch.STATE_NORMAL){
                        port = blocks.get(headID).getSwitch().getPortNormal();
                    } else if (state == Switch.STATE_ALTERNATE){
                        port = blocks.get(headID).getSwitch().getPortAlternate();
                    }

                    if (blocks.get(i).getId() == port){
                        double[] x_coords = blocks.get(i).getXCoordinates();
                         double[] y_coords = blocks.get(i).getYCoordinates();

                        g2d.setColor(lineColor);
                        for (int j = 0; j < x_coords.length-2; j++){
                            g2d.drawRect((int)x_coords[j]-1, (int)y_coords[j]-1, 4, 4);
                        }

                        g2d.setColor(Color.black);
                        for (int j = 0; j < x_coords.length-2; j++){
                            g2d.drawRect((int)x_coords[j], (int)y_coords[j], 2, 2);
                        }
                    }
                }

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_HEAD){
                    double[] x_coords = blocks.get(i).getXCoordinates();
                    double[] y_coords = blocks.get(i).getYCoordinates();

                    g2d.setColor(lineColor);
                    for (int j = 0; j < x_coords.length-2; j++){
                        g2d.drawRect((int)x_coords[j]-1, (int)y_coords[j]-1, 4, 4);
                    }

                    g2d.setColor(Color.black);
                    for (int j = 0; j < x_coords.length-2; j++){
                        g2d.drawRect((int)x_coords[j], (int)y_coords[j], 2, 2);
                    }
                }
            }
        }

        // Draw the train (currently just iterates through each
        // position along the track, will later just appear at
        // the current block position occupied in the simulation)
        g2d.setColor(Color.white);
        double[] xy_coords = pos.getCoordinates();
        double x_coord = xy_coords[0];
        double y_coord = xy_coords[1];
        g2d.fillRect((int)x_coord-1, (int)y_coord-1, 5, 5);

        pos.moveTrain(10);


        /*
        double[] xy_coords = blocks.get(blockPos).getCoordinatesAtMeter(meterPos);
        double x_coord = xy_coords[0];
        double y_coord = xy_coords[1];
        g2d.fillRect((int)x_coord-1, (int)y_coord-1, 5, 5);

        // Draw vertical and horizontal lines tracking the train
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(0, (int)y_coord, 334, (int)y_coord);
        g2d.drawLine((int)x_coord, 0, (int)x_coord, 448);

        // Draw the coordinates of the train's current location 
        // on top of the train
        g2d.setColor(Color.white);
        g2d.drawString("(" + String.format("%.2f", x_coord) + ", " + String.format("%.2f", y_coord) + ")", (int)x_coord - 50, (int)y_coord - 15);

        // Draw the currently occupied block position
        // on top of the train
        g2d.setColor(lineColor);
        g2d.setFont(new Font("default", Font.BOLD, 12));
        g2d.drawString(blocks.get(blockPos).getSection() + Integer.toString(blocks.get(blockPos).getId() + 1) + ", " + Integer.toString(meterPos),
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
        */
    }

    // Calls paintComponent(Graphics g) every time
    // the timer goes off
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
} 