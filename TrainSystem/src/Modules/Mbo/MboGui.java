package Modules.Mbo;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.logging.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.table.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import javax.swing.border.*;

public class MboGui extends JFrame implements ActionListener {
   
    private JLabel timeBox;
    private Font font;
    private JButton generateButton;
    private JFileChooser fileChooser;
    private Mbo mbo;
    private Object[][] trainData;
    private JTable trainInfoTable;
    private String[] trainInfoColumns;
    private DefaultTableModel trainInfoTableModel, trainScheduleTableModel;
    private JLabel pineapple, pineapple2;

	public MboGui() {
        init();
	}

	private void init() {

		// initialize class attributes
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		fileChooser = new JFileChooser();
		mbo = new Mbo();
		//fileChooser.setForeground(Color.WHITE);

		// initialize the jframe
        setTitle("Moving Block Overlay");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set the icon
		pineapple = new JLabel(new ImageIcon("pineapple_icon.png"));
		pineapple2 = new JLabel(new ImageIcon("pineapple_icon.png"));
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
		//this.timeBox.setForeground(Color.WHITE);
		this.timeBox.setFont(this.font);

		//searchBar.getDocument().addDocumentListener(new SearchListener());

        // create a table with train info
		trainInfoColumns = new String [] {"<html><br><br><center>Train Name<br><br></center></html>",
			                "<html><center>Time Most<br>Recent Signal<br>Received</center></html>",
						    "<html><center>Coordinates<br>Received<br>(mi, mi)</center></html>",
							"<html><center>Calculated<br>Location</center></html>",
							"<html><center>Calculated<br>Velocity<br>(mi/s)</center></html>",
							"<html><center>Transmitted<br>Authority<br>(mi)</center></html>"};
		this.trainData = mbo.getTrainData();
		trainInfoTableModel = new DefaultTableModel(trainData, trainInfoColumns) {
    		public boolean isCellEditable(int row, int column) {
      			return false;
    		}
  		};
		trainInfoTable = new JTable(trainInfoTableModel);
		trainInfoTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		trainInfoTable.setFillsViewportHeight(true);
	    JScrollPane scrollPane = new JScrollPane(trainInfoTable);

		// create the train map
		ImageIcon mapIcon = new ImageIcon("..\\..\\Shared\\static\\dummy_map.png");
		mapIcon = new ImageIcon(mapIcon.getImage().getScaledInstance(349, 467, Image.SCALE_DEFAULT));
		JLabel map = new JLabel(mapIcon);
		
        // populate the information panel
		GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setAutoCreateContainerGaps(true);
		infoPanelLayout.setAutoCreateGaps(true);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createSequentialGroup()
				                                          .addGroup(infoPanelLayout.createParallelGroup()
															                       .addComponent(this.timeBox, GroupLayout.Alignment.CENTER)
																				   .addComponent(map))
				                                          .addGroup(infoPanelLayout.createParallelGroup()
					                                                               .addComponent(searchBox)
				                                                                   .addComponent(scrollPane)
																				   .addComponent(pineapple, GroupLayout.Alignment.TRAILING)));
        infoPanelLayout.setVerticalGroup(infoPanelLayout.createSequentialGroup()
				                                        .addGroup(infoPanelLayout.createParallelGroup()
					                                                             .addComponent(this.timeBox, GroupLayout.Alignment.CENTER)
					                                                             .addComponent(searchBox))
				                                        .addGroup(infoPanelLayout.createParallelGroup()
															                     .addComponent(map)
															                     .addGroup(infoPanelLayout.createSequentialGroup()
																										  .addComponent(scrollPane)
				                                       													  .addComponent(pineapple))));
	}
	
	private void initSchedulerPanel(JPanel schedulerPanel) {

		// create the basic components
		JLabel trainTitle = new JLabel("Train Schedules");
		trainTitle.setFont(this.font);
		//trainTitle.setForeground(Color.WHITE);
		JLabel operatorTitle = new JLabel("Operator Schedules");
		operatorTitle.setFont(this.font);
		GroupLayout layout = new GroupLayout(schedulerPanel);

        // train schedule table
		String[] trainTableHeaders = {"Train Name", "Start Time", "Stop Time"};
		Object[][] trainTableData = {{"Red 1", new String(), new String()}, 
		                             {"Red 2", new String(), new String()}, 
		                             {"Red 1", new String(), new String()}, 
		                             {"Green 2", new String(), new String()}, 
		                             {"Green 3", new String(), new String()}};
		trainScheduleTableModel = new DefaultTableModel(trainTableData, trainTableHeaders) {
    		public boolean isCellEditable(int row, int column) {
      			return column != 0;
    		}
  		};
		JTable trainTable = new JTable(trainScheduleTableModel);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		trainTable.setFillsViewportHeight(true);
		//trainTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		JScrollPane trainScrollPane = new JScrollPane(trainTable);

		// operator schedule table
		String[] operatorTableHeaders = {"Operator Name", "Start Time", "Stop Time"};
		Object[][] operatorTableData = {{new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}};
		JTable operatorTable = new JTable(operatorTableData, operatorTableHeaders);
		operatorTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		operatorTable.setFillsViewportHeight(true);
		JScrollPane operatorScrollPane = new JScrollPane(operatorTable);

        // final parameter input
		JLabel datePromptLabel = new JLabel("Date");
		JLabel throughputPromptLabel = new JLabel("Desired Throughput");
		JTextField datePrompt = new JTextField(20);
		JTextField throughputPrompt = new JTextField(20);
		generateButton = new JButton("Generate Schedule");
		Border thickBorder = new LineBorder(Color.WHITE, 3);
    	generateButton.setBorder(thickBorder);
    	generateButton.setPreferredSize(new Dimension(15, 90));
		generateButton.setContentAreaFilled(false);
		generateButton.setOpaque(true);
		generateButton.setBackground(Color.BLACK);
		generateButton.setForeground(Color.WHITE);
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
				                                        .addComponent(trainScrollPane)
														.addComponent(finalInputPanel))
										.addGroup(layout.createParallelGroup()
											            .addComponent(operatorTitle)
														.addComponent(operatorScrollPane)
										.addComponent(pineapple2, GroupLayout.Alignment.TRAILING)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				                      .addGroup(layout.createParallelGroup()
				                                      .addComponent(trainTitle)
				                                      .addComponent(operatorTitle))
									  .addGroup(layout.createParallelGroup()
											          .addComponent(trainScrollPane)
												      .addComponent(operatorScrollPane))
									  .addGroup(layout.createParallelGroup()
									  	              .addComponent(finalInputPanel)
									  	              .addComponent(pineapple2, GroupLayout.Alignment.TRAILING)));
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
                Logger.getLogger(MboGui.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		// handle generate file action
		if (e.getSource() == generateButton) {
			int returnVal = fileChooser.showSaveDialog(MboGui.this);
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
		mbo.testInitTrains();
		this.timeBox.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		this.trainData = mbo.getTrainData();
		this.trainInfoTableModel.setDataVector(this.trainData, this.trainInfoColumns);
		//for (int i; i < this.trainData.length; i++) {
		//	for (int j; j < this.trainData[0].length; j++) {
		//		this.trainInfoTableModel
		//	}
		//}
		//this.trainInfoTableModel.update(this.trainData);
		//this.trainInfoTable.repaint();
	}

	public static void main(String[] args) throws InterruptedException {

    	//MaterialLookAndFeel ui = new MaterialLookAndFeel(GUITheme.DARK_THEME);
    	MboGui MboGui = new MboGui();
        EventQueue.invokeLater(() -> {
            //MboGui = new MboGui();
			MboGui.setVisible(true);
		});
		while (true) {
			MboGui.update();
			try {
			    Thread.sleep(100);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
}
