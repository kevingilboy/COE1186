package Modules.TrackModel;

import Modules.TrainModel.Position;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
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
    int blockPos = 0;
    
    // Track data structure whose information will be
    // used for the dynamic rendering
    ArrayList<Block> blocks;
    Color lineColor;
    
    int counter = 0;
    int trainsDeployed = 1;
    int numTrains = 3;

    ArrayList<Position> positions = new ArrayList<Position>();
    ArrayList<Double[]> xy_coords = new ArrayList<Double[]>();
    ArrayList<Double[]> previous_xy_coords = new ArrayList<Double[]>();

    public TrackRenderWindow(int width, int height, ArrayList<Block> blocks){
        initializeWindow(width, height);
        initializeTimer();
        initializeTrack(blocks);
        initializeTrains();
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

    // TESTING ONLY: Initialize some trains
    public void initializeTrains(){
        for (int i = 0; i < numTrains; i++){

            Double[] xy_coord = {(double)0.0, (double)0.0};
            Double[] previous_xy_coord = {(double)0.0, (double)0.0};

            xy_coords.add(xy_coord);
            previous_xy_coords.add(previous_xy_coord);
        }
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

                    /* 
                    g2d.setColor(Color.black);
                    for (int j = 0; j < x_coords.length-2; j++){
                        g2d.drawRect((int)x_coords[j], (int)y_coords[j], 2, 2);
                    }
                    */
                }
            }
        }

        // Draw the train (currently just iterates through each
        // position along the track, will later just appear at
        // the current block position occupied in the simulation)

        drawTrains(g2d);
    }

    public void drawTrains(Graphics2D g2d){
        for (int i = 0; i < numTrains; i++){

            if (counter >= 100 * (i+1)){
                
                int scaledMetersToMove = (int)(blocks.get(positions.get(i).getCurrentBlock()).getLength()) / 10;
                int direction = positions.get(i).moveTrain( scaledMetersToMove );

                if (direction == 1){
                    (xy_coords.get(i))[0] = (positions.get(i).getCoordinates())[0];
                    (xy_coords.get(i))[1] = (positions.get(i).getCoordinates())[1];
                    (previous_xy_coords.get(i))[0] = (xy_coords.get(i))[0];
                    (previous_xy_coords.get(i))[1] = (xy_coords.get(i))[1];
                } else if (direction == -1){
                    (xy_coords.get(i))[0] = (positions.get(i).getInverseCoordinates())[0];
                    (xy_coords.get(i))[1] = (positions.get(i).getInverseCoordinates())[1];
                    (previous_xy_coords.get(i))[0] = (xy_coords.get(i))[0];
                    (previous_xy_coords.get(i))[1] = (xy_coords.get(i))[1];
                } else {
                    (xy_coords.get(i))[0] = (previous_xy_coords.get(i))[0];
                    (xy_coords.get(i))[1] = (previous_xy_coords.get(i))[1];
                }
                
                g2d.setColor(Color.white);
                g2d.fillRect(((xy_coords.get(i))[0]).intValue() - 1, ((xy_coords.get(i))[1]).intValue() - 1, 5, 5);

                g2d.drawString("(" + 
                    String.format("%.2f", (xy_coords.get(i))[0]) + ", " + 
                    String.format("%.2f", (xy_coords.get(i))[1]) + ")", 
                    ((xy_coords.get(i))[0]).intValue() - 50, ((xy_coords.get(i))[1]).intValue() - 15);
         
                g2d.setColor(lineColor);
                int radius = (counter % 14);
                Shape circle = new Ellipse2D.Double(((xy_coords.get(i))[0]).intValue() - radius, 
                                ((xy_coords.get(i))[1]).intValue() - radius,
                                2.0*radius, 2.0*radius );
                g2d.draw(circle);
            }

        }
    }

    // Calls paintComponent(Graphics g) every time
    // the timer goes off
    public void actionPerformed(ActionEvent e) {
        counter++;

        if (trainsDeployed <= numTrains){
            if (counter == 100 * trainsDeployed){
                positions.add(new Position(blocks));
                trainsDeployed++;
            }
        } 

        repaint();
    }
} 