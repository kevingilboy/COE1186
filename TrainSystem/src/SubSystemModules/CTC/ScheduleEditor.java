import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JComboBox;

public class ScheduleEditor {
	public Schedule currentSchedule;
	public boolean editing = true;
	public JFrame frame;
	private JTextField nameInput;
	private JTextField authorityInput;
	private JComboBox<String> lineSelect;
	private JButton btnCreateSchedule;


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
			nameInput.setText(currentSchedule.name);
			nameInput.setEnabled(false);
			authorityInput.setText(Integer.toString(currentSchedule.authority));
			btnCreateSchedule.setText("Save Edits");

			if(currentSchedule.dispatched==true){
				lineSelect.setEnabled(false);
			}
		}		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 600);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);
		
		btnCreateSchedule = new JButton("Create Schedule");
		btnCreateSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSchedule.name = nameInput.getText();
				currentSchedule.line = (String) lineSelect.getSelectedItem();
				currentSchedule.authority = Integer.parseInt(authorityInput.getText());
				
				frame.dispose();
				editing = false;
			}
		});
		btnCreateSchedule.setBounds(111, 421, 171, 41);
		contentPane.add(btnCreateSchedule);
		
		JLabel lblTrainName = new JLabel("Train Name:");
		lblTrainName.setBounds(42, 63, 115, 33);
		contentPane.add(lblTrainName);

		nameInput = new JTextField();
		nameInput.setBounds(150, 60, 199, 39);
		nameInput.setColumns(10);
		contentPane.add(nameInput);
		
		JLabel lblTrainAuthority = new JLabel("Train Authority (mi):");
		lblTrainAuthority.setBounds(20, 203, 115, 33);
		contentPane.add(lblTrainAuthority);
		
		authorityInput = new JTextField();
		authorityInput.setColumns(10);
		authorityInput.setBounds(150, 200, 199, 39);
		contentPane.add(authorityInput);
		
		JLabel lblLine = new JLabel("Line");
		lblLine.setBounds(83, 127, 115, 33);
		contentPane.add(lblLine);
		
		lineSelect = new JComboBox<String>();
		lineSelect.setModel(new DefaultComboBoxModel<String>(new String[] {"Red", "Green"}));
		lineSelect.setBounds(150, 127, 199, 39);
		contentPane.add(lineSelect);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
