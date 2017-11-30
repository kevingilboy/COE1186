package Modules.Mbo;

public class MboBlock {

	private String line;
	private String section;
	private int id;
	private double length;
	private double grade;
	private double elevation;
	private double cumElevation;
	private int speedLimit;
	private int direction;
	private double[] x_coordinates;
	private double[] y_coordinates;

	public MboBlock(String line,
	 				String section,
					int id,
					double length,
					double grade,
					double elevation,
					double cumElevation,
					int speedLimit,
					int direction,
					double[] x_coordinates,
					double[] y_coordinates) {
			this.line = line;
			this.section = section;
			this.id = id;
			this.length = length;
			this.grade = grade;
			this.elevation = elevation;
			this.cumElevation = cumElevation;
			this.speedLimit = speedLimit;
			this.direction = direction;
			this.x_coordinates = x_coordinates;
			this.y_coordinates = y_coordinates;
	}

	public boolean onBlock(double x, double y) {
		boolean result = false;
		for (int i = 0; i < x_coordinates.length; i++) {
			if (x == x_coordinates[i] && y == y_coordinates[i]) {
				result = true;
				break;
			}
		}
		return result;
	}

	public String getID() {
		return String.format("%s %s%d", line, section, id);
	}
}