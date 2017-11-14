public class TrackControllerGUI{
	//Parent Class
	private TrackController tc;
	//Gui Variables
	private String line;
	private String section;
	private int blockId;
	private String plcPath;
	
	public TrackControllerGUI(TrackController tc){
		this.tc = tc;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackControllerGUI frame = new TrackControllerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void displayInfo(){
		
	}

	class uploadPLC implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			String plcPath = c.getCurrentDirectory().toString() + "/" + c.getSelectedFile().getName();
			System.out.println("path = " + plcPath;
			boolean parserSuccess = tc.tcplc.parsePlc(plcPath);
	    }
	}
	
}