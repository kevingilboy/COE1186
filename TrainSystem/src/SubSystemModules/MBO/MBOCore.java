import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.time.format.*;

public class MBOCore {

	private TrainInfo[] trains;
	private TrackInfo track;

	public MBOCore(String configFile) {
		System.out.println("MBO loading configuration from " + configFile);
		demoInit();
	}

	private void demoInit() {
		trains = new TrainInfo[5];
		trains[0] = new TrainInfo("RED 1", new double[] {5,5}, LocalDateTime.now());
		trains[1] = new TrainInfo("RED 2", new double[] {5,-5}, LocalDateTime.now());
		trains[2] = new TrainInfo("GREEN 1", new double[] {-5,5}, LocalDateTime.now());
		trains[3] = new TrainInfo("GREEN 2", new double[] {-5,-5}, LocalDateTime.now());
		trains[4] = new TrainInfo("GREEN 3", new double[] {0,0}, LocalDateTime.now());
	}

	public Object[][] getTrainData() {
		updateInfo();
		Object[][] results = new Object[trains.length][6];
		for (int i = 0; i < trains.length; i++) {
			results[i] = new Object[] {trains[i].name, 
				                       trains[i].signalReceived.format(DateTimeFormatter.ofPattern("HH:mm:ss")), 
				                       Arrays.toString(trains[i].position),
				                       null,
				                       trains[i].speed,
				                       trains[i].currentAuthority};
		}
		return results;
	}

	public void updateInfo() {
		for (TrainInfo train : trains) {
			train.updateLatestSignal(train.position, LocalDateTime.now());
		}
	}

	private static class TrainInfo {

		private String name;
		private double[] position;
		private LocalDateTime signalReceived;
		private double[] previousPosition;
		private LocalDateTime previousSignalReceived;
		private double[] velocity;
		private double speed;
		private double currentAuthority;
		private LocalDateTime signalTransmitted;

		private TrainInfo(String _name, double[] pos, LocalDateTime time) {
			name = _name;
			position = pos;
			signalReceived = time;
			previousPosition = null;
			previousSignalReceived = null;
			velocity = new double[2];
			currentAuthority = 0;
			signalTransmitted = null;
		}

		private void calculateVelocity() {
			double elapsedHours = previousSignalReceived.until(signalReceived, ChronoUnit.MILLIS);
			velocity[0] = (position[0] - previousPosition[0]) / elapsedHours;
			velocity[1] = (position[1] - previousPosition[1]) / elapsedHours;
			speed = Math.pow(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2), 0.5);
		}

		private void updateLatestSignal(double[] pos, LocalDateTime rec) {
			previousPosition = position;
			position = pos;
			previousSignalReceived = signalReceived;
			signalReceived = rec;
			calculateVelocity();
		}

		private Object[] getFields() {
			Object[] fields = {name, position, signalReceived, previousPosition, previousSignalReceived,
				               velocity, speed, currentAuthority, signalTransmitted};
			return fields;
		}
	}

	private static class TrackInfo {

	}
}
