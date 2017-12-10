package Modules.Mbo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import Shared.SimTime;

public class TrainInfo {
	
	private final double SECONDS_PER_HOUR = 3600.0;
	private final double METERS_PER_MILE = 1609.34;

	private String name;
	private double[] position;
	private MboBlock block;
	private String blockName;
	private SimTime timeSignalReceived;
	private double[] previousPosition;
	private SimTime timePreviousSignalReceived;
	private int lastSignalSec;
	private int signalSec;
	private double[] velocity;
	private double speed;
	private double authority;
	private double safeBrakingDistance;
	private int direction;
	private SimTime timeSignalTransmitted;
	private Mbo mbo;

	public TrainInfo(String name, SimTime time, double[] position, Mbo mbo) {
		this.name = name;
		this.position = new double[2];
		this.position = position;
		this.velocity = new double[]{0,0};
		timeSignalReceived = time;
		timePreviousSignalReceived = time;
		this.previousPosition = position;
		this.mbo = mbo;
		this.direction = 1;
	}

	public Object[] toDataArray() {
		Object[] output = new Object[7];
		output[0] = name;
		output[1] = timeSignalReceived.toString();
		output[2] = String.format("(%.3f, %.3f)", position[0], position[1]);
		//System.out.printf("%s on %s.\n", name, blockName);
		output[3] = blockName;
		output[4] = String.format("%.3f", speed * (SECONDS_PER_HOUR / METERS_PER_MILE));
		output[5] = (authority == Double.MAX_VALUE) ? String.format("%.3f", authority / METERS_PER_MILE) : "n/a";
		output[6] = String.format("%.3f", safeBrakingDistance / METERS_PER_MILE);
		//System.out.printf("Dist %f\n", safeBrakingDistance);
		return output;
	}

	public void updatePosition(double[] pos, SimTime time) {
		//timePreviousSignalReceived.ms = timeSignalReceived.ms;
		//timePreviousSignalReceived.sec = timeSignalReceived.sec;
		//timePreviousSignalReceived.min = timeSignalReceived.min;
		//timePreviousSignalReceived.hr = timePreviousSignalReceived.hr;
		lastSignalSec = signalSec;
		signalSec = time.sec;
		timeSignalReceived = time;/*
		timeSignalReceived.ms = time.ms;
		timeSignalReceived.sec = time.ms;
		timeSignalReceived.min = time.ms;
		timeSignalReceived.ms = time.ms;*/
		previousPosition = position;
		position = pos;
		calculateSpeed();
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

	public String getName() {
		return name;
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
		//double elapsedSec = timePreviousSignalReceived.until(timeSignalReceived, ChronoUnit.MILLIS);
		int elapsedSec = signalSec - lastSignalSec;
		// System.out.printf("time %d %d\n", signalSec, lastSignalSec);
		if (elapsedSec != 0) {
			//System.out.printf("pos %.3f prev %.3f\n", position[0], previousPosition[0]);
			//System.out.printf("pos %.3f prev %.3f\n", position[1], previousPosition[1]);
			velocity[0] = (position[0] - previousPosition[0]) / (double)elapsedSec;
			velocity[1] = (position[1] - previousPosition[1]) / (double)elapsedSec;
			//System.out.printf("0 %.3f 1 %.3f\n", velocity[0], velocity[1]);
		}

		// calculate the speed in m/s
		speed = Math.pow(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2), 0.5);
	}

	private void calculateSpeed() {
		int elapsedSec = signalSec - lastSignalSec;
		if (elapsedSec != 0) {

			// traveling an entire block between signals requires traveling at least 35m in 0.25s
			// this requires traveling at a speed of >= 313.171 MPH
			// will thus assume that no train will travel an entire block between signals
			MboBlock block = mbo.getBlockFromCoordinates(position);
			int offset = block.getOffset(position);
			MboBlock previousBlock = mbo.getBlockFromCoordinates(previousPosition);
			//System.out.printf("%s on %s at %f, %f\n", name, previousBlock, previousPosition[0], previousPosition[1]);
			int prevOffset = previousBlock.getOffset(previousPosition);

			// if trains are on same block, distance traveled is absolute distance between offsets
			// otherwise, distance to end of first block plus offset in second block
			double distance;
			if (block.equals(previousBlock)) {
				distance = Math.abs(offset - prevOffset);
			} else {
				distance = offset + (previousBlock.getLength() - prevOffset);
			}

			// and then speed is distance / time
			speed = distance / (float) elapsedSec;
		}
	}

	private void updateLatestSignal() {

	}
}