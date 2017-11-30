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
	private Double[] x_coordinates;
	private Double[] y_coordinates;

	public MboBlock(String line,
	 				String section,
					int id,
					double length,
					double grade,
					double elevation,
					double cumElevation,
					int speedLimit,
					int direction,
					Double[] x_coordinates,
					Double[] y_coordinates) {
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

	public String getLine() {
		return line;
	}

	public double getLength() {
		return length;
	}

	public double getGrade() {
		return grade;
	}

	public Double[] getXCoordinates() {
		return x_coordinates;
	}

	public boolean onBlock(double x, double y) {
		boolean result = false;
		for (int i = 0; i < x_coordinates.length-2; i++) {
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