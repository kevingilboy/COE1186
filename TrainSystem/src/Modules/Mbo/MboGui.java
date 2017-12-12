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

import Shared.Module;
import Shared.SimTime;

// UI STYLING
import java.awt.GraphicsEnvironment;
import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import java.awt.FontFormatException;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Insets;

public class MboGui extends JFrame implements ActionListener {

	/*-------------------------------------------------------------------*/
	/**
	 * <COMMON AESTHETICS>
	 * ALLOWABLE FONTS
	 */
	
	Font font_14_bold = new Font("Roboto Condensed", Font.BOLD, 16);
	Font font_16_bold = new Font("Roboto Condensed", Font.BOLD, 20);
	Font font_20_bold = new Font("Roboto Condensed Bold", Font.BOLD, 30);
	Font font_24_bold = new Font("Roboto Condensed", Font.BOLD, 38);

	/**
	 * <COMMON AESTHETICS>
	 * SET LOOK AND FEEL - CALL THIS FIRST!!!
	 */
	public void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
			UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
			UIManager.put("TabbedPane.selected", Color.GRAY);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * <COMMON AESTHETICS>
	 * STANDARD BUTTON, COMBOBOX, LABEL WRAPPERS
	 */
	public void stylizeButton(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(102, 0, 153)); // Purple
	}

	public void stylizeButton_Disabled(JButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.GRAY);
		b.setBackground(new Color(50, 0, 70));
	}

	public void stylizeToggleButton(JToggleButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(102, 0, 153)); // Purple
	}

	public void stylizeToggleButton_Disabled(JToggleButton b){
		b.setFocusPainted(false);
		b.setFont(font_14_bold);
		b.setForeground(Color.GRAY);
		b.setBackground(new Color(50, 0, 70));
	}

	public void stylizeComboBox(JComboBox c){
		c.setFont(font_14_bold);
		((JLabel)c.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		c.setForeground(Color.BLACK);
		c.setBackground(Color.WHITE);
	}

	public void stylizeTextField(JTextField t){
		t.setFont(font_14_bold);
		t.setForeground(Color.BLACK);
		t.setBackground(Color.WHITE);
		t.setHorizontalAlignment(JTextField.CENTER);
	}

	public void stylizeScrollPane(JScrollPane s){
		s.getViewport().setBackground(new Color(36, 39, 45));
		Border b = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		s.setFont(font_16_bold);
		s.setBorder(b);
	}

	public void stylizePanel(JPanel p){
		Border b = BorderFactory.createEmptyBorder(0, 0, 0, 0);	
		p.setBackground(new Color(26, 29, 35));
		p.setBorder(b);
	}

	public void stylizeTable(JTable t){
		t.getTableHeader().setFont(font_14_bold);
		t.getTableHeader().setBackground(new Color(20, 20, 20));
		t.getTableHeader().setForeground(Color.WHITE);
		t.getTableHeader().setReorderingAllowed(false);

		t.setFont(font_14_bold);
	}

	public void stylizeTabbedPane(JTabbedPane t){
		t.setFont(font_16_bold);
		t.setBackground(Color.DARK_GRAY);
		t.setForeground(Color.BLACK);
	}

	public void stylizeHeadingLabel(JLabel l){
		l.setFont(font_20_bold);
		l.setForeground(Color.WHITE);
		l.setHorizontalAlignment(SwingConstants.LEFT);
	}

	public void stylizeMessageLabel(JLabel l){
		l.setFont(font_16_bold);
		l.setForeground(Color.WHITE);
		l.setHorizontalAlignment(SwingConstants.LEFT);
	}

	public void stylizeInfoLabel(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Bold(JLabel l){
		l.setHorizontalAlignment(SwingConstants.RIGHT);
		l.setForeground(new Color(234, 201, 87));
		l.setFont(font_16_bold);
	}

	public void stylizeInfoLabel_Small(JLabel l){
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setForeground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		l.setFont(font_14_bold);
	}
	/*-------------------------------------------------------------------*/
   
    private JLabel timeBox;
    private Font font;
    private JButton generateButton;
    private JFileChooser fileChooser;
    private Mbo mbo;
    private MboScheduler scheduler;
    private Object[][] trainData;
    private JTable trainInfoTable;
    private String[] trainInfoColumns;
    private DefaultTableModel trainInfoTableModel, trainScheduleTableModel;
    private JLabel pineapple, modeLight;
    private ImageIcon offLight, onLight;
    private JTextField datePrompt, throughputPrompt;
    private JTable trainTable, operatorTable;

	public MboGui(Mbo mbo) {
		this.mbo = mbo;
		scheduler = new MboScheduler();
        init();
	}

	private void init() {

		// initialize class attributes
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 40);
		fileChooser = new JFileChooser();
		//fileChooser.setForeground(Color.WHITE);

		// initialize the jframe
        setTitle("Moving Block Overlay");
		setSize(1350, 600);
		getContentPane().setBackground(new Color(20, 20, 20));
		setLocationRelativeTo(null);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set the icon
		pineapple = new JLabel(new ImageIcon("Modules/Mbo/static/HSS_TrainSim_Logo.png"));
		offLight = new ImageIcon("Modules/Mbo/static/statusIcon_grey.png");
		onLight = new ImageIcon("Modules/Mbo/static/statusIcon_green.png");
		modeLight = mbo.isMovingBlockModeEnabled() ? new JLabel(onLight) : new JLabel(offLight);
		setIconImage((new ImageIcon("Modules/Mbo/static/HSS_TrainSim_Logo.png")).getImage());

		// create the infopanel
        JPanel infoPanel = new JPanel();
        stylizePanel(infoPanel);

		JPanel schedulerPanel = new JPanel();
		stylizePanel(schedulerPanel);

        initInfoPanel(infoPanel);
		initSchedulerPanel(schedulerPanel);

		// create the tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane();
		stylizeTabbedPane(tabbedPane);
		tabbedPane.addTab("TRAIN INFORMATION", null, infoPanel, "Displays information about the trains in the system");
		tabbedPane.addTab("SCHEDULER", null, schedulerPanel, "Allows the user to create a train schedule for use by the CTC office");

		add(tabbedPane);
	}
	
	private void initInfoPanel(JPanel infoPanel) {
		
		// create a search bar
		JTextField searchBox = new JTextField(" SEARCH FOR A PARTICULAR TRAIN...");
		stylizeTextField(searchBox);

		// create a clock
		this.timeBox = new JLabel("<html><div style='text-align: center;'>" + 
							      "07:00:00" + "</div></html>");
		stylizeHeadingLabel(this.timeBox);

		JLabel modeLabel = new JLabel("<html><div style='text-align: center;'>" + 
									  "LIGHT WILL SHINE<br>WHEN MOVING BLOCK MODE<br>ENABLED</div></html>",
									  SwingConstants.CENTER);
		stylizeMessageLabel(modeLabel);


        // create a table with train info
		trainInfoColumns = new String [] {"<html><br><br><center>Train Name<br><br></center></html>",
			                "<html><center>Time Most<br>Recent Signal<br>Received</center></html>",
						    "<html><center>Coordinates<br>Received<br>(mi, mi)</center></html>",
							"<html><center>Calculated<br>Location</center></html>",
							"<html><center>Calculated<br>Velocity<br>(mi/h)</center></html>",
							"<html><center>Transmitted<br>Authority<br>(mi)</center></html>",
							"<html><center>Transmitted<br>Safe Braking<br>Distance (mi)</center></html>"};
		//trainInfoColumns.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		//System.out.println("About to try with " + mbo);
		this.trainData = mbo.getTrainData();
		trainInfoTableModel = new DefaultTableModel(trainData, trainInfoColumns) {
    		public boolean isCellEditable(int row, int column) {
      			return false;
    		}
  		};
  		//trainInfoTable = new JTable(this.trainData, this.trainInfoColumns);
		trainInfoTable = new JTable(trainInfoTableModel);
		stylizeTable(trainInfoTable);
		trainInfoTable.setPreferredScrollableViewportSize(new Dimension(500, 70));

	    JScrollPane scrollPane = new JScrollPane(trainInfoTable);
	    stylizeScrollPane(scrollPane);

		// create the train map
		ImageIcon mapIcon = new ImageIcon("..\\..\\Shared\\static\\dummy_map.png");
		mapIcon = new ImageIcon(mapIcon.getImage().getScaledInstance(349, 467, Image.SCALE_DEFAULT));
		JLabel map = new JLabel(mapIcon);
		
		// set sizes of layout components
		//searchBox.setPreferredSize(new Dimension(40, 40));
		//this.timeBox.setPreferredSize(new Dimension(40, 40));

        // populate the information panel
		GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setAutoCreateContainerGaps(true);
		infoPanelLayout.setAutoCreateGaps(true);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createSequentialGroup()
				                                          .addGroup(infoPanelLayout.createParallelGroup()
															                       .addComponent(this.timeBox, GroupLayout.Alignment.CENTER,
															                       				 GroupLayout.PREFERRED_SIZE, 160,
          																						 GroupLayout.PREFERRED_SIZE)
															                       .addComponent(modeLabel, GroupLayout.Alignment.CENTER,
															                       				 GroupLayout.PREFERRED_SIZE, 160,
          																						 GroupLayout.PREFERRED_SIZE)
																				   .addComponent(modeLight, GroupLayout.Alignment.CENTER))
				                                          .addGroup(infoPanelLayout.createParallelGroup()
					                                                               .addComponent(searchBox)
				                                                                   .addComponent(scrollPane)
																				   .addComponent(pineapple, GroupLayout.Alignment.TRAILING)));
        infoPanelLayout.setVerticalGroup(infoPanelLayout.createSequentialGroup()
				                                        .addGroup(infoPanelLayout.createParallelGroup()
					                                                             .addComponent(this.timeBox,
					                                                             			   GroupLayout.Alignment.CENTER,
															                       			   GroupLayout.PREFERRED_SIZE, 50,
          																					   GroupLayout.PREFERRED_SIZE)
					                                                             .addComponent(searchBox,
					                                                             			   GroupLayout.Alignment.CENTER,
															                       			   GroupLayout.PREFERRED_SIZE, 50,
          																					   GroupLayout.PREFERRED_SIZE))
				                                        .addGroup(infoPanelLayout.createParallelGroup()
															                     .addGroup(infoPanelLayout.createSequentialGroup()
															                     						  .addComponent(modeLabel)
															                     						  .addComponent(modeLight))
															                     .addGroup(infoPanelLayout.createSequentialGroup()
																										  .addComponent(scrollPane)
				                                       													  .addComponent(pineapple))));
	}
	
	private void initSchedulerPanel(JPanel schedulerPanel) {

		// create the basic components
		JLabel trainTitle = new JLabel("TRAIN SCHEDULES");
		stylizeHeadingLabel(trainTitle);

		JLabel operatorTitle = new JLabel("OPERATOR SCHEDULES");
		stylizeHeadingLabel(operatorTitle);

		GroupLayout layout = new GroupLayout(schedulerPanel);

        // train schedule table
		String[] trainTableHeaders = {"Train Name", "Start Time", "Stop Time"};
		Object[][] trainTableData = {{new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}};
		trainTable = new JTable(trainTableData, trainTableHeaders);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		stylizeTable(trainTable);

		JScrollPane trainScrollPane = new JScrollPane(trainTable);
		stylizeScrollPane(trainScrollPane);

		// operator schedule table
		String[] operatorTableHeaders = {"Operator Name", "Start Time", "Stop Time"};
		Object[][] operatorTableData = {{new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}, 
		                                {new String(), new String(), new String()}};
		operatorTable = new JTable(operatorTableData, operatorTableHeaders);
		operatorTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		stylizeTable(operatorTable);

		JScrollPane operatorScrollPane = new JScrollPane(operatorTable);
		stylizeScrollPane(operatorScrollPane);

        // final parameter input
		JLabel datePromptLabel = new JLabel("SCHEDULE TITLE");
		stylizeInfoLabel(datePromptLabel);

		JLabel throughputPromptLabel = new JLabel("DESIRED THROUGHPUT");
		stylizeInfoLabel(throughputPromptLabel);

		datePrompt = new JTextField(20);
		stylizeTextField(datePrompt);

		throughputPrompt = new JTextField(20);
		stylizeTextField(throughputPrompt);

		generateButton = new JButton("GENERATE SCHEDULE");
		stylizeButton(generateButton);

		generateButton.addActionListener(this);

		JPanel finalInputPanel = new JPanel();
		stylizePanel(finalInputPanel);

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
										.addComponent(pineapple, GroupLayout.Alignment.TRAILING)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				                      .addGroup(layout.createParallelGroup()
				                                      .addComponent(trainTitle)
				                                      .addComponent(operatorTitle))
									  .addGroup(layout.createParallelGroup()
											          .addComponent(trainScrollPane)
												      .addComponent(operatorScrollPane))
									  .addGroup(layout.createParallelGroup()
									  	              .addComponent(finalInputPanel)
									  	              .addComponent(pineapple, GroupLayout.Alignment.TRAILING)));
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
                
                // update the Scheduler's train schedules
                String[][] trains = new String[trainTable.getModel().getRowCount()][3];
                //System.out.printf("Rows: %d\n", trainTable.getModel().getRowCount());
                for (int i = 0; i < trainTable.getModel().getRowCount(); i++){
 					trains[i][0] = (String)trainTable.getModel().getValueAt(i,0);
 					trains[i][1] = (String)trainTable.getModel().getValueAt(i,1);
 					trains[i][2] = (String)trainTable.getModel().getValueAt(i,2);
				}
                //String[][] debugSched = {{"A", "B", "C"}, {"A", "B", "C"}};
                scheduler.updateTrainSchedules(trains);

                // update the Scheduler's operator schedules
                String[][] operators = new String[operatorTable.getModel().getRowCount()][3];
                for (int i = 0; i < trainTable.getModel().getRowCount(); i++){
 					operators[i][0] = (String)operatorTable.getModel().getValueAt(i,0);
 					operators[i][1] = (String)operatorTable.getModel().getValueAt(i,1);
 					operators[i][2] = (String)operatorTable.getModel().getValueAt(i,2);
				}
                scheduler.updateOperatorSchedules(operators);

                // generate the schedule
                String sched = scheduler.generateSchedule(file.getName(), datePrompt.getText(), Double.parseDouble(throughputPrompt.getText()));
                System.out.println(sched);

                // save the schedule
                PrintWriter out = null;
    			try {
        			out = new PrintWriter(file.getPath());
        			out.println(sched);
        		} catch (IOException ioExc) {
    				System.err.println("Exception generating while writing schedule: " + ioExc.getMessage());
				} finally {
        			out.close();
        		}

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

	public void update(SimTime time) {
		this.timeBox.setText("<html><div style='text-align: center;'>" + 
							 time.toString() + "</div></html>");
		this.trainData = mbo.getTrainData();
		//this.trainInfoTableModel.setDataVector(this.trainData, this.trainInfoColumns);
		DefaultTableModel model = new DefaultTableModel(this.trainData, this.trainInfoColumns);
		//for (Object[] row : trainData) model.addRow(row);
		trainInfoTable.setModel(model);
		//trainInfoTableModel.fireTableDataChanged();
		//((DefaultTableModel) this.trainInfoTable.getModel()).fireTableDataChanged();
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
    	MboGui MboGui = new MboGui(new Mbo());
        EventQueue.invokeLater(() -> {
            //MboGui = new MboGui();
			MboGui.setVisible(true);
		});
		while (true) {
			MboGui.update(new SimTime(7,0,0));
			try {
			    Thread.sleep(100);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
}
