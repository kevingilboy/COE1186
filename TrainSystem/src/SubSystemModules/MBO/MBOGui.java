import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.*;
import javax.swing.text.*;

public class MBOGui extends JFrame {
   
	public MBOGui() {
        init();
	}

	private void init() {

		// initialize class attributes

		// initialize the jframe
        setTitle("Moving Block Overlay");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// create a search bar
		JTextField searchBar = new JTextField(40);
		JLabel timeBox = new JLabel("15:44:01");
		searchBar.getDocument().addDocumentListener(new SearchListener());

        // create a table with train info
		String[] trainInfoColumns = {"Train Name",
			                         "Time Most Recent Signal Received",
									 "Coordinates Received",
									 "Calculated Location",
									 "Calculated Velocity",
									 "Transmitted Authority"};
		Object[][] dummyData = {
			{"RED 1", "15:43:01", "(40.0 N, 17.0 W)", "RA4", "45 MPH", "7 mi"},
			{"RED 2", "15:43:01", "(40.0 N, 17.0 W)", "RA4", "45 MPH", "7 mi"},
			{"GREEN 1", "15:43:01", "(40.0 N, 17.0 W)", "RA4", "45 MPH", "7 mi"},
			{"GREEN 2", "15:43:01", "(40.0 N, 17.0 W)", "RA4", "45 MPH", "7 mi"},
			{"GREEN 3", "15:43:01", "(40.0 N, 17.0 W)", "RA4", "45 MPH", "7 mi"},
		};
		JTable trainInfoTable = new JTable(dummyData, trainInfoColumns);
		trainInfoTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		trainInfoTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(trainInfoTable);
		// add(scrollPane);

		// create the train map
		JLabel map = new JLabel(new ImageIcon("..\\..\\Shared\\static\\dummy_map.png"));
		//createLayout(searchBar, modeButton, map);
		
        // create the information panel
		JPanel infoPanel = new JPanel();
		GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setAutoCreateContainerGaps(true);
		infoPanelLayout.setAutoCreateGaps(true);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createSequentialGroup()
				.addGroup(infoPanelLayout.createParallelGroup()
					.addComponent(searchBar)
				    .addComponent(trainInfoTable))
				.addGroup(infoPanelLayout.createParallelGroup()
					.addComponent(searchBar)
				    .addComponent(trainInfoTable))
		);
        infoPanelLayout.setVerticalGroup(infoPanelLayout.createParallelGroup()
				.addGroup(infoPanelLayout.createSequentialGroup()
					.addComponent(searchBar)
					.addComponent(trainInfoTable))
				.addGroup(infoPanelLayout.createSequentialGroup()
					.addComponent(timeBox)
					.addComponent(map))
		);

		// create the tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Information", null, infoPanel, "Displays information about the trains in the system");
		tabbedPane.addTab("Scheduler", null, scrollPane, "Allows the user to create a train schedule for use by the CTC office");

		add(tabbedPane);
	}

    private class SearchListener implements DocumentListener {

		private String text;

        @Override
		public void insertUpdate(DocumentEvent e) {
            updateLabel(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
            updateLabel(e);
		} 

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateLabel(e);
		}

		private void updateLabel(DocumentEvent e) {
        
			Document doc = e.getDocument();
			int len = doc.getLength();

			try {
                text = doc.getText(0, len);
			} catch (BadLocationException ex) {
                Logger.getLogger(MBOGui.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	private void createLayout(JComponent... arg) {
        Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(arg[0])
				.addComponent(arg[1])
				.addComponent(arg[2])
		);
        gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(arg[0])
				.addComponent(arg[1])
				.addComponent(arg[2])
		);
		pack();
	}

	public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MBOGui mboGui = new MBOGui();
			mboGui.setVisible(true);
		});
	}
}
