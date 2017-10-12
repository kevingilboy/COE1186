
public class TrainModelSystemActions {
	
	public static void runTrainModel () throws InterruptedException {
        TrainModel trainModel = new TrainModel("GREEN", 1);
        //Instantiate a GUI for this train
        TrainModelNewGUI trainModelGUI = trainModel.CreateNewGUI(trainModel);
        trainModel.showTrainGUI();
        
        //Constantly update velocity then the GUI
        while(true){
        	long millis = System.currentTimeMillis();
            //code to run
            trainModel.updateTrainValues();
            trainModel.setValuesForDisplay(trainModelGUI);
            if(trainModelGUI.isDisplayable() == false) {
                System.exit(0);
            }
            Thread.sleep(1000 - millis % 1000);
        }
    }
	
	public static void main(String[] args) throws InterruptedException {
		runTrainModel();
	}
}
