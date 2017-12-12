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

import java.awt.MouseInfo;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import java.awt.FontMetrics;

public class TrackRenderWindow extends JPanel{  
    public static final boolean PASSENGERS_EMBARKING = true;
    public static final boolean PASSENGERS_DISEMBARKING = false;

    // Reference to the main GUI's selected block
    Block blockSelected;

    // Track data structure whose information will be
    // used for the dynamic rendering
    ArrayList<Block> blocks;

    // Information for rendering each active train on the track
    int activeTrains = 0;
    ArrayList<String> trainIDs = new ArrayList<String>();
    ArrayList<Position> positions = new ArrayList<Position>();
    ArrayList<Double[]> xy_coords = new ArrayList<Double[]>();
    ArrayList<Double[]> previous_xy_coords = new ArrayList<Double[]>();
    ArrayList<Boolean> trainsMoving = new ArrayList<Boolean>();
    ArrayList<Integer[]> passengers = new ArrayList<Integer[]>(); // "passengers, passengersEntering, passengersLeaving"

    // Control variables for drawing shapes
    public final int ARR_SIZE = 4;
    public boolean showArrows = false;

    // Constructor
    public TrackRenderWindow(int width, int height, ArrayList<Block> blocks){
        initializeWindow(width, height);
        initializeTrack(blocks);
        repaint();
    }

    // Initialize the window in the context 
    // of the DynamicDisplay GUI window
    public void initializeWindow(int width, int height){
        setBounds(0, 0, width, height);
        setLayout(null);
        setBackground(new Color(46, 49, 55)); // Dark-navy
        setVisible(true);
    }

    // Refresh the GUI
    public void refresh(){
        repaint();
    }

    // Initialize the track
    public void initializeTrack(ArrayList<Block> blocks){
        String line = blocks.get(0).getLine();
        this.blockSelected = blocks.get(0);
        this.blocks = blocks;
    }

    // Display a newly dispatched train
    public void dispatchTrain(String trainID, Position pos){
        Double[] xy_coord = {(double)0.0, (double)0.0};
        Double[] previous_xy_coord = {(double)0.0, (double)0.0};
        Integer[] passengersArr = {0, 0, 0}; // Initialize curr/on/off passengers to 0
        Boolean moving = true;

        xy_coords.add(xy_coord);
        previous_xy_coords.add(previous_xy_coord);

        trainIDs.add(trainID);
        positions.add(pos);
        trainsMoving.add(moving);
        passengers.add(passengersArr);

        activeTrains++;
    }

    // Get the index of a train by its name
    public int getTrainIndex(String trainID){
        return trainIDs.indexOf(trainID);
    }

    // Update passengers boarding a train
    public void updatePassengers(String trainID, int numPassengers, boolean embarking){

        int trainIndex = 0; 
        int currPassengers = (passengers.get(trainIndex))[0];
        int embarkingPassengers = (passengers.get(trainIndex))[1]; 
        int disembarkingPassengers = (passengers.get(trainIndex))[2];

        trainIndex = getTrainIndex(trainID);
        trainsMoving.set(trainIndex, false);

        if (embarking == PASSENGERS_EMBARKING){
            currPassengers = (passengers.get(trainIndex))[0] + numPassengers;
            embarkingPassengers = numPassengers;
        } else if (embarking == PASSENGERS_DISEMBARKING){
            currPassengers = (passengers.get(trainIndex))[0] - numPassengers;
            disembarkingPassengers = numPassengers;
        }

        Integer[] passengersArr = {currPassengers, embarkingPassengers, disembarkingPassengers};
        passengers.set(trainIndex, passengersArr);
    }

    // Remove train sent to the yard 
    public void trainPoofByName(String name) {      
        int indexToRemove = trainIDs.indexOf(name);
        trainIDs.remove(indexToRemove);
        positions.remove(indexToRemove);
        
        activeTrains--;
        if(activeTrains < 0) {
            activeTrains = 0;
        }
    }

    // Draw the screen (gets refreshed every time the timer is
    // called, trigger the actionPerformed() listener to call 
    // repaint()
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d  = (Graphics2D) g;

        // Render each of these components in
        // this sequence for correct layering
        drawLights(g2d);
        drawTrack(g2d);
        drawFailedBlocks(g2d);
        drawCrossing(g2d);
        drawCrossingLight(g2d);
        drawSwitches(g2d);
        drawYard(g2d);
        drawSelectedBlock(g2d);
        drawBeacons(g2d);
        drawTrains(g2d);

        if (showArrows){
            drawDirections(g2d);
            showSwitchInfo(g2d);
        }
    }

    // Display the section + block ID for each
    // switch head (on mouse hover over dynamic
    // display)
    public void showSwitchInfo(Graphics2D g2d){

        g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 21)); 
        g2d.setColor(Color.ORANGE);
        
        for (int i = 0; i < blocks.size(); i++){

            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            if (blocks.get(i).getSwitch() != null){

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_HEAD){
                    String section = blocks.get(i).getSection();
                    String id = Integer.toString(i + 1);
                    int x = (int)x_coords[x_coords.length/2] - 10;
                    int y = (int)y_coords[y_coords.length/2] - 2;

                    g2d.drawString(section + id, x, y);
                }
            }
        }
    }

    // Display the directions of uni-directional
    // track sections with arrows along those sections
    // (on mouse hover over dynamic display)
    public void drawDirections(Graphics2D g2d){

        // Dim the display by overlaying a
        // semi-transparent filling rectangle
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.setStroke(new BasicStroke(2));
        g2d.fillRect(0, 0, 670, 896);

        // Access uni-directional blocks
        // and draw arrows for every third block (i += 3)
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < blocks.size(); i += 3){
            if (blocks.get(i).getDirection() != 0){
                
                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                int x0, y0, x1, y1;

                if (blocks.get(i).getDirection() == 1){

                    x0 = (int)x_coords[x_coords.length/2 - 15];
                    y0 = (int)y_coords[x_coords.length/2 - 15];
                    x1 = (int)x_coords[x_coords.length/2];
                    y1 = (int)y_coords[x_coords.length/2];

                }  else {

                    x0 = (int)x_coords[x_coords.length/2];
                    y0 = (int)y_coords[x_coords.length/2];
                    x1 = (int)x_coords[x_coords.length/2 - 15];
                    y1 = (int)y_coords[x_coords.length/2 - 15];

                }

                drawArrow((Graphics)g2d, x0, y0, x1, y1);
            }
        }
    }

    // Draw the yard
    // ("YARD" over an outlined rectangle)
    public void drawYard(Graphics2D g2d){

        int yard_x = 429;
        int yard_y = 222;
        int yard_w = 66;
        int yard_h = 36;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(yard_x, yard_y+3, yard_w, yard_h);

        g2d.setColor(Color.GRAY); 
        g2d.fillRect(yard_x, yard_y, yard_w, yard_h);

        g2d.setColor(new Color(26, 29, 35));
        g2d.fillRect(yard_x+2, yard_y+2, yard_w-4, yard_h-4);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 22)); 
        g2d.drawString("YARD", yard_x+8, yard_y+25);

    }

    // Draw beacons (located before and after each station)
    // as small blue dots
    public void drawBeacons(Graphics2D g2d){

        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getBeacon() != null){
                
                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                int x = (int)x_coords[x_coords.length/2]-1;
                int y = (int)y_coords[x_coords.length/2]-1;
                int w = 3;
                int h = 3;

                // Semi transparent glow layer 1
                g2d.setColor(new Color(0, 100, 255, 50));
                g2d.fillOval(x - 5, y - 5, w + 10, h + 10);

                // Semi transparent glow layer 2
                g2d.setColor(new Color(0, 100, 255, 30));
                g2d.fillOval(x - 12, y - 12, w + 24, h + 24);

                // Nice blue color :)
                g2d.setColor(new Color(0, 100, 255));
                g2d.fillOval(x, y, w, h);
            
            }
        }
    }

    // Draw the entire track
    public void drawTrack(Graphics2D g2d){

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the track shadow
        g2d.setColor(new Color(36, 39, 45));
        g2d.setStroke(new BasicStroke(8));
        
        for (int i = 0; i < blocks.size(); i++){
            
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();
            
            for (int j = 3; j < x_coords.length-2; j+=3){
                
                int x0 = (int)x_coords[j-3];
                int y0 = (int)y_coords[j-3] + 5;
                int x1 = (int)x_coords[j];
                int y1 = (int)y_coords[j];

                g2d.drawLine(x0, y0, x1, y1);
            }
        } 

        // Draw station outlines
        // (Included in this method because the station
        // outlines are on a layer between the track's
        // shadow and primary outline rails)
        for (int i = 0; i < blocks.size(); i++){
            if ((blocks.get(i).getStation()) != null){

                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                // Station outer stroke
                g2d.setStroke(new BasicStroke(20));
                g2d.setColor(Color.BLACK);

                for (int j = 2; j < x_coords.length-2; j+=2){
                    int x0 = (int)x_coords[j-2];
                    int y0 = (int)y_coords[j-2];
                    int x1 = (int)x_coords[j];
                    int y1 = (int)y_coords[j]; 

                    g2d.drawLine(x0, y0, x1, y1);
                }

                // Highlight occupied station with nice teal color
                if (blocks.get(i).getOccupied()){
                    g2d.setColor(new Color(0, 200, 255));
                } else {
                    g2d.setColor(Color.GRAY);
                }

                // Station inner stroke
                g2d.setStroke(new BasicStroke(15)); 
                for (int j = 2; j < x_coords.length-2; j+=2){
                    int x0 = (int)x_coords[j-2];
                    int y0 = (int)y_coords[j-2];
                    int x1 = (int)x_coords[j];
                    int y1 = (int)y_coords[j]; 

                    g2d.drawLine(x0, y0, x1, y1);
                }
            }
        }

        // Draw the track outline
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(8));
        
        for (int i = 0; i < blocks.size(); i++){
            
            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();
            
            for (int j = 3; j < x_coords.length-2; j+=3){
                int x0 = (int)x_coords[j-3];
                int y0 = (int)y_coords[j-3];
                int x1 = (int)x_coords[j];
                int y1 = (int)y_coords[j];

                g2d.drawLine(x0, y0, x1, y1);
            }
        }

        // Draw the track's inner color
        g2d.setColor(new Color(32, 29, 55)); // Dark purple-ish/navy
        g2d.setStroke(new BasicStroke(6));
        
        for (int i = 0; i < blocks.size(); i++){

            double[] x_coords = blocks.get(i).getXCoordinates();
            double[] y_coords = blocks.get(i).getYCoordinates();

            for (int j = 3; j < x_coords.length-2; j+=3){
                int x0 = (int)x_coords[j-3];
                int y0 = (int)y_coords[j-3];
                int x1 = (int)x_coords[j];
                int y1 = (int)y_coords[j];

                g2d.drawLine(x0, y0, x1, y1);
            }
        }
    }

    // Highlight failed / maintenance blocks
    public void drawFailedBlocks(Graphics2D g2d){
        
        g2d.setColor(new Color(170, 0, 0)); // dim red
        g2d.setStroke(new BasicStroke(6));
        
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getStatus() == Block.STATUS_NOT_WORKING){

                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                for (int j = 3; j < x_coords.length-2; j+=3){

                    int x0 = (int)x_coords[j-3];
                    int y0 = (int)y_coords[j-3];
                    int x1 = (int)x_coords[j];
                    int y1 = (int)y_coords[j];

                    g2d.drawLine(x0, y0, x1, y1);
                }
            }
        }
    }

    // Highlight the crossing block
    public void drawCrossing(Graphics2D g2d){

        g2d.setColor(new Color(0, 100, 100)); // teal
        g2d.setStroke(new BasicStroke(6));
        
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getCrossing() != null){

                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                for (int j = 3; j < x_coords.length-2; j+=3){

                    int x0 = (int)x_coords[j-3];
                    int y0 = (int)y_coords[j-3];
                    int x1 = (int)x_coords[j];
                    int y1 = (int)y_coords[j];

                    g2d.drawLine(x0, y0, x1, y1);
                }
            }
        }
    }

    // Highlight the switch blocks
    public void drawSwitches(Graphics2D g2d){

        for (int i = 0; i < blocks.size(); i++){
            
            if (blocks.get(i).getSwitch() != null){

                // Highlight the tail port that the head port is connected to
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

                    // The tail port is at <port>, highlight this block
                    if (blocks.get(i).getId() == port){

                        double[] x_coords = blocks.get(i).getXCoordinates();
                        double[] y_coords = blocks.get(i).getYCoordinates();

                        g2d.setColor(Color.BLACK);
                        g2d.setStroke(new BasicStroke(12)); // Black outline

                        for (int j = 6; j < x_coords.length-6; j+=3){

                            int x0 = (int)x_coords[j-3];
                            int y0 = (int)y_coords[j-3];
                            int x1 = (int)x_coords[j];
                            int y1 = (int)y_coords[j];

                            g2d.drawLine(x0, y0, x1, y1);
                        }

                        g2d.setColor(new Color(152, 0, 203)); // Magenta-ish/Purple
                        g2d.setStroke(new BasicStroke(8));

                        for (int j = 3; j < x_coords.length-2; j+=3){

                            int x0 = (int)x_coords[j-3];
                            int y0 = (int)y_coords[j-3];
                            int x1 = (int)x_coords[j];
                            int y1 = (int)y_coords[j];

                            g2d.drawLine(x0, y0, x1, y1);
                        }

                        g2d.setColor(new Color(52, 29, 75)); // Purple-ish/Navy
                        g2d.setStroke(new BasicStroke(6));

                        for (int j = 3; j < x_coords.length-2; j+=3){

                            int x0 = (int)x_coords[j-3];
                            int y0 = (int)y_coords[j-3];
                            int x1 = (int)x_coords[j];
                            int y1 = (int)y_coords[j];

                            g2d.drawLine(x0, y0, x1, y1);
                        }
                    }
                }

                if (blocks.get(i).getSwitch().getEdge() == Switch.EDGE_TYPE_HEAD){

                    double[] x_coords = blocks.get(i).getXCoordinates();
                    double[] y_coords = blocks.get(i).getYCoordinates();

                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(12)); // Black outline

                    for (int j = 6; j < x_coords.length-6; j+=3){

                        int x0 = (int)x_coords[j-3];
                        int y0 = (int)y_coords[j-3];
                        int x1 = (int)x_coords[j];
                        int y1 = (int)y_coords[j];

                        g2d.drawLine(x0, y0, x1, y1);
                    }

                    g2d.setColor(new Color(152, 0, 203)); // Magenta-ish/Purple
                    g2d.setStroke(new BasicStroke(8));

                    for (int j = 3; j < x_coords.length-2; j+=3){

                        int x0 = (int)x_coords[j-3];
                        int y0 = (int)y_coords[j-3];
                        int x1 = (int)x_coords[j];
                        int y1 = (int)y_coords[j];

                        g2d.drawLine(x0, y0, x1, y1);
                    }

                    g2d.setColor(new Color(52, 29, 75)); // Purple-ish/Navy
                    g2d.setStroke(new BasicStroke(6));

                    for (int j = 3; j < x_coords.length-2; j+=3){

                        int x0 = (int)x_coords[j-3];
                        int y0 = (int)y_coords[j-3];
                        int x1 = (int)x_coords[j];
                        int y1 = (int)y_coords[j];

                        g2d.drawLine(x0, y0, x1, y1);
                    }
                }
            }
        }  
    }

    // Highlight the block selected in the main GUI
    public void drawSelectedBlock(Graphics2D g2d){
        g2d.setColor(new Color(150, 60, 10)); // burnter orange
        g2d.setStroke(new BasicStroke(10));
        
        double[] x_coords = blocks.get(blockSelected.getId()).getXCoordinates();
        double[] y_coords = blocks.get(blockSelected.getId()).getYCoordinates();

        for (int j = 6; j < x_coords.length-6; j+=3){

            int x0 = (int)x_coords[j-3];
            int y0 = (int)y_coords[j-3];
            int x1 = (int)x_coords[j];
            int y1 = (int)y_coords[j];

            g2d.drawLine(x0, y0, x1, y1);
        }

        g2d.setColor(new Color(254, 208, 36)); // burnt orange
        g2d.setStroke(new BasicStroke(6));

        for (int j = 3; j < x_coords.length-2; j+=3){

            int x0 = (int)x_coords[j-3];
            int y0 = (int)y_coords[j-3];
            int x1 = (int)x_coords[j];
            int y1 = (int)y_coords[j];

            g2d.drawLine(x0, y0, x1, y1);
        }
    }

    // Draw the crossing lights
    public void drawCrossingLight(Graphics2D g2d){

        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getCrossing() != null){

                double[] x_coords = blocks.get(i).getXCoordinates();
                double[] y_coords = blocks.get(i).getYCoordinates();

                int x_coord = (int)x_coords[x_coords.length / 2] - 12;
                int y_coord = (int)y_coords[x_coords.length / 2] - 24;

                int radius = 3;

                g2d.setColor(Color.BLACK);

                Shape circleOutline = new Ellipse2D.Double(x_coord - 3, y_coord - 2, 2.0*(radius+2), 2.0*(radius+2));
                g2d.fill(circleOutline);

                // Draw the pole
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x_coord - 4, y_coord - 20, 3, 45);

                if (blocks.get(i).getCrossing().getState() == true){
                    // Draw the gate
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(x_coord - 34, y_coord+4, x_coord - 6, y_coord+4);

                    g2d.setColor(Color.WHITE);
                    g2d.drawLine(x_coord - 30, y_coord+2, x_coord - 6, y_coord+2);

                    g2d.setColor(Color.RED);
                    g2d.drawLine(x_coord - 33, y_coord+2, x_coord - 30, y_coord + 2);
                    
                    // Set the light color to Red
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.GRAY);
                }

                Shape circle = new Ellipse2D.Double(x_coord -1, y_coord, 2.0*radius, 2.0*radius);
                g2d.fill(circle);

                // 2nd light
                g2d.setColor(Color.BLACK);
                circleOutline = new Ellipse2D.Double(x_coord - 11, y_coord - 2, 2.0*(radius+2), 2.0*(radius+2));
                g2d.fill(circleOutline);

                if (blocks.get(i).getCrossing().getState() == true){
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.GRAY);
                }

                circle = new Ellipse2D.Double(x_coord - 9, y_coord, 2.0*radius, 2.0*radius);
                g2d.fill(circle);

                //"X"
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine(x_coord - 9, y_coord-15, x_coord + 4, y_coord - 4);
                g2d.drawLine(x_coord + 4, y_coord-15, x_coord - 9, y_coord - 4);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x_coord - 9, y_coord-15, x_coord + 4, y_coord - 4);
                g2d.drawLine(x_coord + 4, y_coord-15, x_coord - 9, y_coord - 4);
            }
        }
    }

    // Draw the lights
    public void drawLights(Graphics2D g2d){
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).getLight() != null){

                int x_coord = blocks.get(i).getLight().getXCoordinate();
                int y_coord = blocks.get(i).getLight().getYCoordinate();

                int radius = 5;

                if (blocks.get(i).getLight().getState() == true){
                    g2d.setColor(new Color(0, 255, 0, 40));
                } else {
                    g2d.setColor(new Color(255, 0, 0, 40));
                }

                Shape circleGlow1 = new Ellipse2D.Double(x_coord - 6, y_coord - 6, 2.0*(radius+6), 2.0*(radius+6));
                g2d.fill(circleGlow1);

                if (blocks.get(i).getLight().getState() == true){
                    g2d.setColor(new Color(0, 255, 0, 25));
                } else {
                    g2d.setColor(new Color(255, 0, 0, 25));
                }
                Shape circleGlow2 = new Ellipse2D.Double(x_coord - 10, y_coord - 10, 2.0*(radius+10), 2.0*(radius+10));
                g2d.fill(circleGlow2);

                g2d.setColor(new Color(16, 19, 35));
                g2d.fillRect(x_coord - 2, y_coord + 8, 6, 18);

                g2d.setColor(Color.BLACK);
                Shape circleOutline = new Ellipse2D.Double(x_coord - 2, y_coord - 2, 2.0*(radius+2), 2.0*(radius+2));
                g2d.fill(circleOutline);

                if (blocks.get(i).getLight().getState() == true){
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.RED);
                }

                Shape circle = new Ellipse2D.Double(x_coord, y_coord, 2.0*radius, 2.0*radius);
                g2d.fill(circle);
            }
        }
    }

    // Render each active train
    public void drawTrains(Graphics2D g2d){
        for (int i = 0; i < activeTrains; i++){
           
            // Highlight the currently occupied block
            // Outer stroke
            g2d.setColor(new Color(0, 100, 255,150));
            g2d.setStroke(new BasicStroke(10));

            double[] x_coords = blocks.get(positions.get(i).getCurrentBlock()).getXCoordinates();
            double[] y_coords = blocks.get(positions.get(i).getCurrentBlock()).getYCoordinates();
            
            for (int j = 6; j < x_coords.length - 6; j+=3){

                int x0 = (int)x_coords[j-3];
                int y0 = (int)y_coords[j-3];
                int x1 = (int)x_coords[j]; 
                int y1 = (int)y_coords[j];

                g2d.drawLine(x0, y0, x1, y1);
            }

            // Inner stroke
            g2d.setColor(new Color(0, 80, 200));
            g2d.setStroke(new BasicStroke(6));
            
            for (int j = 6; j < x_coords.length - 6; j+=3){

                int x0 = (int)x_coords[j-3];
                int y0 = (int)y_coords[j-3];
                int x1 = (int)x_coords[j]; 
                int y1 = (int)y_coords[j];

                g2d.drawLine(x0, y0, x1, y1);
            }

           
            int direction = positions.get(i).getCurrentDirection();

            // Order the coordinates to iterate through based on the
            // train's direction
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
            
            // Draw the train
            int train_x = ((xy_coords.get(i))[0]).intValue() - 2;
            int train_y = ((xy_coords.get(i))[1]).intValue() - 2;
            int train_w = 6;
            int train_h = 6;

            // Outer Stroke
            g2d.setColor(Color.BLACK);
            g2d.fillRect(train_x-2, train_y-2, train_w + 4, train_h + 4);

            // Inside fill
            g2d.setColor(Color.WHITE);
            g2d.fillRect(train_x, train_y, train_w, train_h);

            // Draw the train's information on top of it
            // -----------------------> DISPLAY TRAIN NAME <---------------------------
            g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 16)); 
            String trainInfo = trainIDs.get(i);
            int text_x = ((xy_coords.get(i))[0]).intValue() - 16;
            int text_y = ((xy_coords.get(i))[1]).intValue() - 6;
            
            // Text shadow
            g2d.setColor(Color.BLACK);
            g2d.drawString(trainInfo, text_x, text_y);

            // Text foreground
            g2d.setColor(Color.white);
            g2d.drawString(trainInfo, text_x, text_y - 3);

            // ----------------------->  DISPLAY PASSENGERS (current, +embarking, -disembarking)
            if ((blocks.get(positions.get(i).getCurrentBlock()).getStation() != null) &&
                (trainsMoving.get(i) == false)) {

                g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 14));
                
                String currPassengersStr = ((passengers.get(i))[0]).toString();
                String embarkingPassengersStr = ((passengers.get(i))[1]).toString();
                String disembarkingPassengersStr = ((passengers.get(i))[2]).toString();

                // (current passengers)
                g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 25));
                FontMetrics fontMetrics = g2d.getFontMetrics();
                text_x = ((xy_coords.get(i))[0]).intValue() - 68;
                text_y = ((xy_coords.get(i))[1]).intValue();

                // Text shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString(currPassengersStr, text_x - fontMetrics.stringWidth(currPassengersStr), text_y);

                // Text foreground
                g2d.setColor(Color.ORANGE);
                g2d.drawString(currPassengersStr, text_x - fontMetrics.stringWidth(currPassengersStr), text_y - 3);

                // Rectangle behind (+) and (-)
                int rect_x = ((xy_coords.get(i))[0]).intValue() - 65;
                int rect_y = ((xy_coords.get(i))[1]).intValue() - 23;
                int rect_w = 50;
                int rect_h = 23;
                g2d.setColor(new Color(0,0,0, 120));
                g2d.fillRect(rect_x, rect_y, rect_w, rect_h);

                // (+ passengers)
                g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 16));
                text_x = ((xy_coords.get(i))[0]).intValue() - 65;
                text_y = ((xy_coords.get(i))[1]).intValue() - 5;

                // Text shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString("+" + embarkingPassengersStr, text_x, text_y);

                // Text foreground
                g2d.setColor(Color.GREEN);
                g2d.drawString("+" + embarkingPassengersStr, text_x, text_y - 3);

                // (- passengers)
                text_x = ((xy_coords.get(i))[0]).intValue() - 35;
                text_y = ((xy_coords.get(i))[1]).intValue() - 5;

                // Text shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString("-" + disembarkingPassengersStr, text_x, text_y);

                // Text foreground
                g2d.setColor(Color.RED);
                g2d.drawString("-" + disembarkingPassengersStr, text_x, text_y - 3);

                // -----------------------> DISPLAY STATION NAME BLOCK <---------------------------
                g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 18));
                String currBlockStr = blocks.get(positions.get(i).getCurrentBlock()).getStation().getId().toUpperCase();

                text_x = ((xy_coords.get(i))[0]).intValue() - 90;
                text_y = ((xy_coords.get(i))[1]).intValue() - 24;

                // Text shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString(currBlockStr, text_x, text_y);

                // Text foreground
                g2d.setColor(Color.WHITE);
                g2d.drawString(currBlockStr, text_x, text_y - 3);

            } else {
                trainsMoving.set(i, true);    

                // -----------------------> DISPLAY CURRENT BLOCK <---------------------------
                g2d.setFont(new Font("Roboto Condensed", Font.BOLD, 22));
                String currBlockStr = blocks.get(positions.get(i).getCurrentBlock()).getSection()
                                    + Integer.toString(positions.get(i).getCurrentBlock() + 1);
                text_x = ((xy_coords.get(i))[0]).intValue() - 16;
                text_y = ((xy_coords.get(i))[1]).intValue() - 22;

                // Text shadow
                g2d.setColor(Color.BLACK);
                g2d.drawString(currBlockStr, text_x, text_y);

                // Text foreground
                g2d.setColor(Color.ORANGE);
                g2d.drawString(currBlockStr, text_x, text_y - 3);

            }
        }
    }


    // Draw arrows (used for displaying block directions)
    public void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len+2, len-(ARR_SIZE*2+1), len-(ARR_SIZE*2+1), len+2},
                      new int[] {0, -(ARR_SIZE-1), ARR_SIZE-1, 0}, 4);
    }
}

