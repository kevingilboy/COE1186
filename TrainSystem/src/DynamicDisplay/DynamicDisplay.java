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

public class DynamicDisplay extends JPanel implements ActionListener {
  Timer timer;

  public DynamicDisplay() {
    timer = new Timer(1000, this); // 1000 ms timer
    timer.start();
  }

  public void render(Graphics2D g2) {
  }

  public void paint(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    render(g2);
  }

  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Dynamic Simulation Display");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new TimerBasedAnimation());
    frame.setSize(350, 250);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}