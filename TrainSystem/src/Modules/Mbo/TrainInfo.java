package Modules.Mbo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import Shared.SimTime;

public class TrainInfo {
	
	private String name;
	private double[] position;
	private MboBlock block;
	private String blockName;
	private SimTime timeSignalReceived;
	private double[] previousPosition;
	private SimTime timePreviousSignalReceived;
	private double[] velocity;
	private double speed;
	private double authority;
	private double safeBrakingDistance;
	private SimTime timeSignalTransmitted;

	public TrainInfo(String name, SimTime time) {
		this.name = name;
		this.position = new double[2];
		this.position[0] = 0.0;
		this.position[1] = 0.0;
		this.velocity = new double[]{0,0};
		timeSignalReceived = time;
	}

	public Object[] toDataArray() {
		Object[] output = new Object[7];
		output[0] = name;
		output[1] = timeSignalReceived.toString();
		output[2] = Arrays.toString(position);
		//System.out.printf("%s on %s.\n", name, blockName);
		output[3] = blockName;
		output[4] = speed;
		output[5] = authority;
		output[6] = safeBrakingDistance;
		//System.out.printf("Dist %f\n", safeBrakingDistance);
		return output;
	}

	public void updatePosition(double[] pos, SimTime time) {
		timeSignalReceived = time;
		position = pos;
		//System.out.printf("%s at %f:%f\n", name, pos[0], pos[1]);
	}

	public void updatePosition(double[] pos, String blockName, SimTime time) {
		this.blockName = blockName;
		this.updatePosition(pos, time);
	}

	public double[] getPosition() {
		return position;
	}

	public MboBlock getBlock() {
		return block;
	}

	public String getBlockName() {
		return blockName;
	}

	public double getAuthority() {
		return authority;
	}

	public double getSpeed() {
		return speed;
	}

	public void setAuthority(double auth) {
		authority = auth;
		//System.out.printf("Authority for %s is %f\n", name, authority);
	}

	public double getSafeBrakingDistance() {
		return safeBrakingDistance;
	}

	public void setSafeBrakingDistance(double dist) {
		this.safeBrakingDistance = dist;
		//System.out.printf("%s has dist %f\n", name, safeBrakingDistance);
	}

	private void calculateVelocity() {
		//double elapsedHours = timePreviousSignalReceived.until(timeSignalReceived, ChronoUnit.MILLIS);
		double elapsedHours = timeSignalReceived.hr - timePreviousSignalReceived.hr;
		velocity[0] = (position[0] - previousPosition[0]) / elapsedHours;
		velocity[1] = (position[1] - previousPosition[1]) / elapsedHours;
		speed = Math.pow(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2), 0.5) * 100;


	}

	private void updateLatestSignal() {

	}
}