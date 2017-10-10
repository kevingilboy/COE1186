import java.time.LocalDateTime;

public class MBOCore {

	private TrainInfo[] trains;
	private TrackInfo track;

	private static class TrainInfo {

		private String name;
		private double[] position;
		private LocalDateTime signalReceived;
		private double[] lastPosition;
		private LocalDateTime previousSignalReceived;
		private double currentAuthority;
		private LocalDateTime signalTransmitted;

		private TrainInfo(String _name) {
			name = _name;
			position = null;
			signalReceived = null;
			lastPosition = null;
			previousSignalReceived = null;
			currentAuthority = 0;
			signalTransmitted = null;
		}

		private double[] calculateVelocity() {
			return null;
		}

		private void updateLatestSignal(double[] pos, LocalDateTime rec) {
			lastPosition = position;
			position = pos;
			previousSignalReceived = signalReceived;
			signalReceived = rec;
		}

		private Object[] getFields() {
			Object[] fields = {name, position, signalReceived, lastPosition, previousSignalReceived,
				               currentAuthority, signalTransmitted};
			return fields;
		}
	}

	private static class TrackInfo {

	}

	public static void main(String[] args) {
		TrainInfo train = new TrainInfo("RED 1");
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
