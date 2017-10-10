import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.logging.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.*;
import javax.swing.text.*;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class MBOGui extends JFrame implements ActionListener {
   
    private JLabel timeBox;
    private Font font;
    private MaterialButton generateButton;
    private JFileChooser fileChooser;

	public MBOGui() {
        init();
	}

	private void init() {

		// initialize class attributes
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		fileChooser = new JFileChooser();
		fileChooser.setForeground(Color.WHITE);

		// initialize the jframe
        setTitle("Moving Block Overlay");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set the icon
		setIconImage((new ImageIcon("..\\..\\Shared\\static\\pineapple2.png")).getImage());

		// create the infopanel
        JPanel infoPanel = new JPanel();
		JPanel schedulerPanel = new JPanel();
        initInfoPanel(infoPanel);
		initSchedulerPanel(schedulerPanel);

		// create the tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Train Information", null, infoPanel, "Displays information about the trains in the system");
		tabbedPane.addTab("Scheduler", null, schedulerPanel, "Allows the user to create a train schedule for use by the CTC office");

		add(tabbedPane);
	}
	
	private void initInfoPanel(JPanel infoPanel) {
		
		// create a search bar
		JTextField searchBox = new JTextField("Search for a particular train...", 40);

		// create a clock
		this.timeBox = new JLabel(LocalDateTime.now().toString());
		this.timeBox.setForeground(Color.WHITE);
		this.timeBox.setFont(this.font);

		//searchBar.getDocument().addDocumentListener(new SearchListener());

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

		// create the train map
		JLabel map = new JLabel(new ImageIcon("..\\..\\Shared\\static\\dummy_map.png"));
		
        // populate the information panel
		GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setAutoCreateContainerGaps(true);
		infoPanelLayout.setAutoCreateGaps(true);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createSequentialGroup()
				                                          .addGroup(infoPanelLayout.createParallelGroup()
					                                                               .addComponent(searchBox)
				                                                                   .addComponent(scrollPane))
				                                          .addGroup(infoPanelLayout.createParallelGroup()
															                       .addComponent(this.timeBox)
																				   .addComponent(map)));
        infoPanelLayout.setVerticalGroup(infoPanelLayout.createSequentialGroup()
				                                        .addGroup(infoPanelLayout.createParallelGroup()
					                                                             .addComponent(searchBox)
					                                                             .addComponent(this.timeBox))
				                                        .addGroup(infoPanelLayout.createParallelGroup()
															                     .addComponent(scrollPane)
																				 .addComponent(map)));
	}
	
	private void initSchedulerPanel(JPanel schedulerPanel) {

		// create the basic components
		JLabel trainTitle = new JLabel("Train Schedules");
		trainTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		trainTitle.setForeground(Color.WHITE);
		JLabel operatorTitle = new JLabel("Operator Schedules");
		GroupLayout layout = new GroupLayout(schedulerPanel);

        // train schedule table
		String[] trainTableHeaders = {"Train Name", "Start Time", "Stop Time"};
		Object[][] trainTableData = {{"Red 1", new Date(), new Date()}, 
		                             {"Red 2", new Date(), new Date()}, 
		                             {"Red 1", new Date(), new Date()}, 
		                             {"Green 2", new Date(), new Date()}, 
		                             {"Green 3", new Date(), new Date()}};
		JTable trainTable = new JTable(trainTableData, trainTableHeaders);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		trainTable.setFillsViewportHeight(true);
		trainTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		JScrollPane scrollPane = new JScrollPane(trainTable);

		// operator schedule table
		String[] operatorTableHeaders = {"Operator Name", "Start Time", "Stop Time"};
		Object[][] operatorTableData = {{"Bob", new Date(), new Date()}, 
		                                {"Pat", new Date(), new Date()}, 
		                                {"Ward", new Date(), new Date()}, 
		                                {"Sandy", new Date(), new Date()}, 
		                                {"Eugene", new Date(), new Date()}};
		JTable operatorTable = new JTable(operatorTableData, operatorTableHeaders);
		operatorTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		operatorTable.setFillsViewportHeight(true);

        // final parameter input
		JLabel datePromptLabel = new JLabel("Date");
		JLabel throughputPromptLabel = new JLabel("Desired Throughput");
		JTextField datePrompt = new JTextField(20);
		JTextField throughputPrompt = new JTextField(20);
		generateButton = new MaterialButton("Generate Schedule");
		generateButton.addActionListener(this);

		JPanel finalInputPanel = new JPanel();
		GroupLayout finalInputLayout = new GroupLayout(finalInputPanel);
		finalInputLayout.setAutoCreateContainerGaps(true);
		finalInputLayout.setAutoCreateGaps(true);
		finalInputPanel.setLayout(finalInputLayout);
		finalInputLayout.setHorizontalGroup(finalInputLayout.createSequentialGroup()
                .addGroup(finalInputLayout.createParallelGroup()
					                      .addComponent(datePromptLabel)
										  .addComponent(throughputPromptLabel)
										  .addComponent(generateButton))
				  .addGroup(finalInputLayout.createParallelGroup()
					                        .addComponent(datePrompt)
											.addComponent(throughputPrompt)));
		finalInputLayout.setVerticalGroup(finalInputLayout.createSequentialGroup()
                .addGroup(finalInputLayout.createParallelGroup()
					                      .addComponent(datePromptLabel)
									 	  .addComponent(datePrompt))
				   .addGroup(finalInputLayout.createParallelGroup()
					                         .addComponent(throughputPromptLabel)
											 .addComponent(throughputPrompt))
				   .addComponent(generateButton));

		// create the layout
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		schedulerPanel.setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				                        .addGroup(layout.createParallelGroup()
				                                        .addComponent(trainTitle)
				                                        .addComponent(trainTable)
														.addComponent(finalInputPanel))
										.addGroup(layout.createParallelGroup()
											            .addComponent(operatorTitle)
														.addComponent(operatorTable)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				                      .addGroup(layout.createParallelGroup()
				                                      .addComponent(trainTitle)
				                                      .addComponent(operatorTitle))
									  .addGroup(layout.createParallelGroup()
											          .addComponent(trainTable)
												      .addComponent(operatorTable))
									  .addComponent(finalInputPanel));
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

	public void actionPerformed(ActionEvent e) {
		// handle generate file action
		if (e.getSource() == generateButton) {
			int returnVal = fileChooser.showSaveDialog(MBOGui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //This is where a real application would save the file.
                System.out.println("Saving: " + file.getName() + ".");
            } else {
                System.out.println("Save command cancelled by user.");
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

	public void update() {
		this.timeBox.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	}

	public static void main(String[] args) throws InterruptedException {

    	//MaterialLookAndFeel ui = new MaterialLookAndFeel(GUITheme.DARK_THEME);
    	MBOGui mboGui = new MBOGui();
        EventQueue.invokeLater(() -> {
            //mboGui = new MBOGui();
			mboGui.setVisible(true);
		});
		while (true) {
			mboGui.update();
			System.out.println(LocalDateTime.now().toString());
			try {
			    Thread.sleep(100);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
}
