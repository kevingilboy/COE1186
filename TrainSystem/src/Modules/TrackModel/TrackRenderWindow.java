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

    // Reference to the main GUI's selected block
    Block blockSelected;

    // Timer used to update the dynamic track view
    Timer timer;

    // Track data structure whose information will be
    // used for the dynamic rendering
    ArrayList<Block> blocks;
    Color lineColor;
    Color lineColorDimmed;

    // Information for rendering each active train on the track
    // int[] pingCounters = new int[100];
    int activeTrains = 0;
    ArrayList<String> trainIDs = new ArrayList<String>();
    ArrayList<Position> positions = new ArrayList<Position>();
    ArrayList<Double[]> xy_coords = new ArrayList<Double[]>();
    ArrayList<Double[]> previous_xy_coords = new ArrayList<Double[]>();

    public TrackRenderWindow(int width, int height, ArrayList<Block> blocks){
        initializeWindow(width, height);
        initializeTimer();
        initializeTrack(blocks);
        repaint();
    }

    // Initialize the window in the context 
    // of the DynamicDisplay GUI window
    public void initializeWindow(int width, int height){
        int windowPadding = 0;
        setBounds(windowPadding, windowPadding, width, height);
        setLayout(null);
        setBackground(new Color(36, 39, 45));
        setVisible(true);
    }

    // Initialize the timer
    public void initializeTimer(){
        /*
        timer = new Timer(250, this); // milliseconds
        timer.setInitialDelay(0);
        timer.start(); // Start the timer
        */
    }

    // Refresh the GUI
    public void refresh(){
        repaint();
    }

    // Initialize the track
    public void initializeTrack(ArrayList<Block> blocks){
        String line = blocks.get(0).getLine();

        if (line.equals("green")){
            lineColor = new Color(76, 195, 85);
            lineColorDimmed = new Color(0, 60, 0);
        } else if (line.equals("red")){
            lineColor = new Color(211, 54, 76);
            lineColorDimmed = new Color(60, 0, 0);
        } else {
            System.out.println("No track line color found!");
        }

        this.blockSelected = blocks.get(0);
        this.blocks = blocks;
    }

    // Dispatch a train to the track
    public void dispatchTrain(String trainID, Position pos){
        Double[] xy_coord = {(double)0.0, (double)0.0};
        Double[] previous_xy_coord = {(double)0.0, (double)0.0};

        xy_coords.add(xy_coord);
        previous_xy_coords.add(previous_xy_coord);

        trainIDs.add(trainID);
        positions.add(pos);
        activeTrains++;
    }

    // TODO: Remove a train from the track
    public void removeTrain(){
        activeTrains--;
        trainIDs.remove(activeTrains);
        positions.remove(activeTrains);
        xy_coords.remove(activeTrains);
        previous_xy_coords.remove(activeTrains);
    }

    // Calls paintComponent(Graphics g) every time
    // the timer goes off
    public void actionPerformed(ActionEvent e){
        // repaint();
    }

    // Draw the screen (gets refreshed every time the timer is
    // called, trigger the actionPerformed() listener to call 
    // repaint()
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d  = (Graphics2D) g;

        drawTrack(g2d);
        drawSwitches(g2d);
        drawSelectedBlock(g2d);
        drawTrains(g2d);
    }

    // Render the track
    public void drawTrack(Graphics2D g2d){
        
        // Draw black track shadow
        /*
        g2d.setColor(new Color(25, 25, 25));
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j]+1, (int)y_coords[j]+3, 
                            4, 4);
            }
        }
        */

        // Draw station outlines
        g2d.setColor(Color.GRAY);
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
        // g2d.setColor(lineColorDimmed);
        g2d.setColor(new Color(26, 29, 35));
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j]-2, (int)y_coords[j]-2, 
                            6, 6);
            }
        }

        // Draw the inner black track to make it look like
        // real track rails colored as the line color
        // g2d.setColor(Color.BLACK);
        /*
        g2d.setColor(lineColorDimmed);
        for (int i = 0; i < blocks.size(); i++){
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 0; j < x_coords.length-2; j++){
                g2d.drawRect((int)x_coords[j], (int)y_coords[j], 
                            2, 2);
            }
        }
        */

        // Highlight failed blocks
        g2d.setColor(new Color(170, 0, 0));
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getStatus() == Block.STATUS_NOT_WORKING){
                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                for (int j = 0; j < x_coords.length-2; j++){
                    g2d.drawRect((int)x_coords[j], (int)y_coords[j], 
                                2, 2);
                }
            }
        }
    }

    // Render the switches
    public void drawSwitches(Graphics2D g2d){
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getSwitch() != null){

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_TAIL){
                    int headID = blocks.get(i).getSwitch().getPortNormal();
                    boolean state = blocks.get(headID).getSwitch().getState();
                    int port = 0;
                    int portAlt = 0;

                    if (state == Switch.STATE_NORMAL){
                        port = blocks.get(headID).getSwitch().getPortNormal();
                        portAlt = blocks.get(headID).getSwitch().getPortAlternate();
                    } else if (state == Switch.STATE_ALTERNATE){
                        port = blocks.get(headID).getSwitch().getPortAlternate();
                        portAlt = blocks.get(headID).getSwitch().getPortNormal();
                    }

                    if (blocks.get(i).getId() == port){
                        double[] x_coords = blocks.get(i).getXCoordinates();
                        double[] y_coords = blocks.get(i).getYCoordinates();

                        g2d.setColor(lineColor);
                        for (int j = 0; j < x_coords.length-2; j++){
                            g2d.drawRect((int)x_coords[j]-2, (int)y_coords[j]-2, 6, 6);
                        }

                        g2d.setColor(new Color(26, 29, 35));
                        for (int j = 0; j < x_coords.length-2; j++){
                            g2d.drawRect((int)x_coords[j]-1, (int)y_coords[j]-1, 4, 4);
                        }
                    }
                }

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_HEAD){
                    double[] x_coords = blocks.get(i).getXCoordinates();
                    double[] y_coords = blocks.get(i).getYCoordinates();

                    g2d.setColor(lineColor);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    for (int j = 0; j < x_coords.length-2; j++){
                        g2d.drawRect((int)x_coords[j]-2, (int)y_coords[j]-2, 6, 6);
                    }

                    g2d.setColor(new Color(26, 29, 35));
                    for (int j = 0; j < x_coords.length-2; j++){
                        g2d.drawRect((int)x_coords[j]-1, (int)y_coords[j]-1, 4, 4);
                    }
                }
            }
        }  
    }

    // Highlight the block selected in the main GUI
    public void drawSelectedBlock(Graphics2D g2d){
        g2d.setColor(new Color(254, 208, 36));
        double[] x_coords = blocks.get(blockSelected.getId()).getXCoordinates();
        double[] y_coords = blocks.get(blockSelected.getId()).getYCoordinates();
        for (int i = 0; i < x_coords.length-2; i++){
            g2d.drawRect((int)x_coords[i], (int)y_coords[i],
                        2, 2);
        }
    }

    // Render each active train
    public void drawTrains(Graphics2D g2d){
        for (int i = 0; i < activeTrains; i++){
                
            /* Hard-coded train movement (track model debug only)
            int scaledMetersToMove = (int)(blocks.get(positions.get(i).getCurrentBlock()).getLength()) / 10;
            int direction = positions.get(i).moveTrain( scaledMetersToMove );
            */
           
            int direction = positions.get(i).getCurrentDirection();

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
            // Draw the train
            g2d.fillRect(((xy_coords.get(i))[0]).intValue() - 1, ((xy_coords.get(i))[1]).intValue() - 1, 5, 5);
            g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 14)); 
            
            g2d.setColor(new Color(26, 29, 35));
            // Draw the train's information
            g2d.drawString(blocks.get(positions.get(i).getCurrentBlock()).getSection() + Integer.toString(blocks.get(positions.get(i).getCurrentBlock()).getId() + 1), 
                ((xy_coords.get(i))[0]).intValue() - 9, ((xy_coords.get(i))[1]).intValue() - 5);

            g2d.setColor(Color.white);
            g2d.drawString(blocks.get(positions.get(i).getCurrentBlock()).getSection() + Integer.toString(blocks.get(positions.get(i).getCurrentBlock()).getId() + 1), 
                ((xy_coords.get(i))[0]).intValue() - 9, ((xy_coords.get(i))[1]).intValue() - 7);
     
            g2d.setColor(lineColor);

            /*
            (pingCounters[i])++;
            int radius = (pingCounters[i] % 14);
            Shape circle = new Ellipse2D.Double(((xy_coords.get(i))[0]).intValue() - radius, 
                            ((xy_coords.get(i))[1]).intValue() - radius,
                            2.0*radius, 2.0*radius );
            g2d.draw(circle);
            */

            if (direction == -2){
                removeTrain();
            }
        }
    }
}

