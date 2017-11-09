import java.util.*;
import java.awt.*;

public class delimitTest{
	public static void main(String[] args){
		String s = "(2.0, 4.5);(1.1, 3.00);(3.4, -1.23);";
		String coordDelimiter = ";";
		String xyDelimiter = ", ";
		double[] x_coords = new double[3];
		double[] y_coords = new double[3];

		String[] coordStrs = s.split(coordDelimiter);

		// Remove "(" and ")" from parsed coordinate strings
		for (int i = 0; i < coordStrs.length; i++){
			coordStrs[i] = coordStrs[i].substring(1, coordStrs[i].length()-1);
			x_coords[i] = Double.parseDouble((coordStrs[i].split(xyDelimiter))[0]);
			y_coords[i] = Double.parseDouble((coordStrs[i].split(xyDelimiter))[1]);
		}

		for (int i = 0; i < 3; i++){
			System.out.print("x = "); System.out.print(x_coords[i]);
			System.out.print(", y = "); System.out.println(y_coords[i]);
		}

	}
}