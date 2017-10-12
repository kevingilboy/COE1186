import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MBOCore {

	private TrainInfo[] trains;
	private TrackInfo track;

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
			System.out.println(previousPosition[0] + "." + previousPosition[1]);
			double elapsedHours = previousSignalReceived.until(signalReceived, ChronoUnit.MILLIS);
			System.out.println(elapsedHours);
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

	public static void main(String[] args) {
		TrainInfo train = new TrainInfo("RED 1", new double[] {0,0}, LocalDateTime.now());
		for (Object field : train.getFields()) {
			System.out.println(field);
		}
		double[] pos1 = {1,1};
		double[] pos2 = {2,2};
		train.updateLatestSignal(pos1, LocalDateTime.now());
		for (Object field : train.getFields()) {
			System.out.println(field);
		}
		train.updateLatestSignal(pos2, LocalDateTime.now());
		for (Object field : train.getFields()) {
			System.out.println(field);
		}

		
	}
}
