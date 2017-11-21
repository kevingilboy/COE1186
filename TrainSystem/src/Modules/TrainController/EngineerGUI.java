package Modules.TrainController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class EngineerGUI {
	private JFrame frame;

	private JPanel contentPane;
	
	private JTextField newPField;
	private JTextField newIField;
	
	JLabel pValue;
	JLabel iValue;

	private double P;
	private double I;
	
	private String trainID;
	
	private PIController pi;
	
	/*public static void main(String[] args) {
		new EngineerGUI("Train 1");
	}*/
	
	public EngineerGUI(PIController p, String s) {
		pi = p;
		trainID = s;
		P = pi.getP();
		I = pi.getI();
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(100, 100, 400, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel idenLabel = new JLabel("Train 1");
		idenLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		idenLabel.setBounds(20, 20, 112, 35);
		contentPane.add(idenLabel);
		
		JLabel pValueLabel = new JLabel("Current P Value:");
		pValueLabel.setBounds(20, 82, 112, 16);
		contentPane.add(pValueLabel);
		
		pValue = new JLabel(P + "");
		pValue.setBounds(130, 82, 76, 16);
		contentPane.add(pValue);
		
		JLabel iValueLabel = new JLabel("Current I Value:");
		iValueLabel.setBounds(20, 134, 112, 16);
		contentPane.add(iValueLabel);
		
		iValue = new JLabel(I + "");
		iValue.setBounds(130, 134, 76, 16);
		contentPane.add(iValue);
		
		JLabel newPLabel = new JLabel("New P Value:");
		newPLabel.setBounds(206, 82, 82, 16);
		contentPane.add(newPLabel);
		
		newPField = new JTextField();
		newPField.setBounds(300, 77, 88, 26);
		contentPane.add(newPField);
		newPField.setColumns(10);
		
		JLabel newILabel = new JLabel("New I Value:");
		newILabel.setBounds(206, 134, 82, 16);
		contentPane.add(newILabel);
		
		newIField = new JTextField();
		newIField.setBounds(300, 129, 88, 26);
		contentPane.add(newIField);
		newIField.setColumns(10);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					double p;
					p = Double.parseDouble(newPField.getText());
					newPField.setText("");
					P = p;
					pValue.setText(P + "");
				} 
				catch (NumberFormatException E) {
					pValue.setText(P + "");
					newPField.setText("");
				}
				try {
					double i;
					i = Double.parseDouble(newIField.getText());
					newIField.setText("");
					I = i;
					iValue.setText(I + "");
				} 
				catch (NumberFormatException E) {
					iValue.setText(I + "");
					newIField.setText("");
				}
				//pi.setP(P);
				//pi.setI(I);
			}
		});
		confirmButton.setBounds(130, 173, 117, 29);
		contentPane.add(confirmButton);
		
		frame.setVisible(true);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
}