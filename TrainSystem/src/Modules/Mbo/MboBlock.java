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
	private int[] forwardBlock;
	private int[] backwardBlock;

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
					Double[] y_coordinates,
					int[] forwardBlock,
					int[] backwardBlock) {
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
			this.forwardBlock = forwardBlock;
			this.backwardBlock = backwardBlock;
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
		//System.out.printf("x: %f, y: %f\n", x, y);
		//System.out.printf("coord: %s %s\n", x_coordinates, y_coordinates);
		//System.out.printf("%d %f\n", x_coordinates.length, x_coordinates[0]);
		for (int i = 0; i < x_coordinates.length; i++) {
		//	System.out.printf("index %d\n", i);
			if (x == x_coordinates[i] && y == y_coordinates[i]) {
				result = true;
				break;
			}
		}
		return result;
	}

	public int getOffset(double[] pos) {
		int result = -1;
		for (int i = 0; i < x_coordinates.length; i++) {
			if (pos[0] == x_coordinates[i] && pos[1] == y_coordinates[i]) {
				result = i;
				break;
			}
		}
		return result;
	}

	public String getID() {
		return String.format("%s %s%d", line, section, id);
	}

	public int[] getNextBlockInfo(int direction) {
		return (direction == 1) ? forwardBlock : backwardBlock;
	}

	public boolean isYardLine() {
		return section.contains("YARD");
	}
}