import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class ScheduleEditor {
	public Schedule currentSchedule;
	public boolean editing = true;
	public JFrame frame;
	private JTextField textField;


	/**
	 * Create the application.
	 */
	public ScheduleEditor() {
		initialize();
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ScheduleEditor(Schedule schedule) {		
		initialize();
		if(schedule==null) {
			currentSchedule = new Schedule();
		}
		else {
			currentSchedule = schedule;
			//TODO populate table with existing schedule
		}		
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnCreateSchedule = new JButton("Create Schedule");
		btnCreateSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSchedule.name = textField.getText();
				frame.dispose();
				editing = false;
			}
		});
		btnCreateSchedule.setBounds(113, 143, 171, 41);
		frame.getContentPane().add(btnCreateSchedule);
		
		textField = new JTextField();
		textField.setBounds(150, 60, 199, 39);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblTrainName = new JLabel("Train Name:");
		lblTrainName.setBounds(42, 63, 115, 33);
		frame.getContentPane().add(lblTrainName);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScheduleEditor window = new ScheduleEditor(new Schedule());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
