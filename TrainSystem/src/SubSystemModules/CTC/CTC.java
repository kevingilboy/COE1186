import java.awt.*;
import javax.swing.*;
import java.util.*;

public class CTC{
	JFrame frame;
	GridLayout mainGrid;
	private int frameHeight = 800;
	private int frameWidth = 1200;
	private Object[] dispatchedTrainsColumnNames = {"Train","Location","Speed","Authority","Passengers"};
	private Object[][] dispatchedTrainsInitialData = new Object[3][dispatchedTrainsColumnNames.length];
	
	private Object[] selectedTrainColumnNames = {"Stop","Arrival","Departure"};
	private Object[][] selectedTrainInitialData = new Object[2][selectedTrainColumnNames.length];

	public CTC() {
		frame = new JFrame();
		frame.setSize(new Dimension(frameWidth,frameHeight));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane mainSplitPane = new JSplitPane();
		frame.getContentPane().add(mainSplitPane,BorderLayout.CENTER);
		
		//Add left panel
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(frameWidth/2,frameHeight));
		mainSplitPane.setLeftComponent(leftPanel);
		
		//Add right split pane
		JSplitPane rightSplitPane = new JSplitPane();
		rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setRightComponent(rightSplitPane);

		//Add right top panel									
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setLayout(new BoxLayout(rightTopPanel,BoxLayout.Y_AXIS));
		rightSplitPane.setLeftComponent(rightTopPanel);
		
		JPanel top_RightTopPanel = new JPanel();
		top_RightTopPanel.setLayout(new BorderLayout());
		rightTopPanel.add(top_RightTopPanel);

		JPanel bot_RightTopPanel = new JPanel();
		bot_RightTopPanel.setLayout(new BorderLayout());
		rightTopPanel.add(bot_RightTopPanel);

		top_RightTopPanel.add(new JLabel("Dispatched Trains",JLabel.CENTER),BorderLayout.NORTH);
		JTable dispatchedTrains = new JTable(dispatchedTrainsInitialData,dispatchedTrainsColumnNames);
		JScrollPane dispatchedTrainsSP = new JScrollPane(dispatchedTrains);
		top_RightTopPanel.add(dispatchedTrainsSP,BorderLayout.CENTER);
		
		bot_RightTopPanel.add(new JLabel("Selected Train Schedule",JLabel.CENTER),BorderLayout.NORTH);
		JTable selectedTrain = new JTable(selectedTrainInitialData,selectedTrainColumnNames);
		JScrollPane selectedTrainSP = new JScrollPane(selectedTrain);
		bot_RightTopPanel.add(selectedTrainSP,BorderLayout.CENTER);
		
		JButton editSelectedTrain = new JButton("Edit Schedule");
		bot_RightTopPanel.add(editSelectedTrain,BorderLayout.EAST);
		
		//Add right bot panel
		JPanel rightBotPanel = new JPanel();
		rightBotPanel.setLayout(new BoxLayout(rightBotPanel,BoxLayout.Y_AXIS));
		rightSplitPane.setRightComponent(rightBotPanel);

		JPanel top_RightBotPanel = new JPanel();
		top_RightBotPanel.setLayout(new BorderLayout());
		rightBotPanel.add(top_RightBotPanel);

		JPanel bot_RightBotPanel = new JPanel();
		bot_RightBotPanel.setLayout(new BorderLayout());
		rightBotPanel.add(bot_RightBotPanel);

		top_RightBotPanel.add(new JLabel("Dispatch Center",JLabel.CENTER),BorderLayout.NORTH);
		JPanel toggle = new JPanel();
		toggle.setLayout(new GridLayout(1,2));
		JToggleButton manual = new JToggleButton("Manual",true);
		JToggleButton auto = new JToggleButton("Auto",false);
		toggle.add(manual);
		toggle.add(auto);
		top_RightBotPanel.add(toggle,BorderLayout.CENTER);
		
		bot_RightBotPanel.add(new JLabel("Manual Train Creation",JLabel.CENTER),BorderLayout.NORTH);
		JTable selectedTrain2 = new JTable(selectedTrainInitialData,selectedTrainColumnNames);
		JScrollPane selectedTrainSP2 = new JScrollPane(selectedTrain2);
		bot_RightBotPanel.add(selectedTrainSP2,BorderLayout.CENTER);
		
		JButton editSelectedTrain2 = new JButton("Edit Schedule");
		bot_RightBotPanel.add(editSelectedTrain2,BorderLayout.EAST);

		JComboBox line = new JComboBox(new String[]{"Red","Green"});
		bot_RightBotPanel.add(line,BorderLayout.WEST);
		
		JButton addToSchedule = new JButton("Add To Schedule");
		bot_RightBotPanel.add(addToSchedule,BorderLayout.SOUTH);

		frame.setVisible(true);
	}
	
	
	public static void main(String args[]) {
		new CTC();
	}
}
