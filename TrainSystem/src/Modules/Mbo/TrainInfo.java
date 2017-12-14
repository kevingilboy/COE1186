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
	private double mass;
	private boolean poofable;

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
		this.block = mbo.getBlockFromCoordinates(position);
		this.mass = 37103.86;
		this.poofable = false;
	}

	public Object[] toDataArray() {
		Object[] output = new Object[7];
		output[0] = name;
		output[1] = timeSignalReceived.toString();
		output[2] = String.format("(%.3f, %.3f)", position[0], position[1]);
		output[3] = block.getID();
		output[4] = String.format("%.3f", speed * (SECONDS_PER_HOUR / METERS_PER_MILE));
		output[5] = (authority != Double.MAX_VALUE) ? String.format("%.3f", authority / METERS_PER_MILE) : "n/a";
		output[6] = String.format("%.3f", safeBrakingDistance / METERS_PER_MILE);
		return output;
	}

	public void updatePosition(double[] pos, SimTime time) {

		// get the new times
		lastSignalSec = signalSec;
		signalSec = time.sec;
		timeSignalReceived = time;

		// if the block changed, used the direction of travel on the previous block to determine the direction of travel 
		// on this block
		previousPosition = position;
		position = pos;
		MboBlock newBlock = mbo.getBlockFromCoordinates(pos);
		if(newBlock==null) return;

		// determine whether the train has returned to the yard
		if (newBlock.getID().contains("YARD") && !block.getID().contains("YARD")) {
			poofable = true;
		}

		if (block != newBlock) {
			int[] blockInfo = block.getNextBlockInfo(direction);
			direction = blockInfo[1];
			block = newBlock;
		} 
		calculateSpeed();
	}

	public void updatePosition(double[] pos, String blockName, SimTime time) {
		this.blockName = blockName;
		this.updatePosition(pos, time);
	}

	public double[] getPosition() {
		return position;
	}

	public int getDirection() {
		return direction;
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

	public boolean isPoofable() {
		return poofable;
	}

	public double getSpeed() {
		return speed;
	}

	public void setAuthority(double auth) {
		authority = auth;
	}

	public double getSafeBrakingDistance() {
		return safeBrakingDistance;
	}

	public void setSafeBrakingDistance(double dist) {
		this.safeBrakingDistance = dist;
	}

	private void calculateVelocity() {
		int elapsedSec = signalSec - lastSignalSec;
		if (elapsedSec != 0) {
			velocity[0] = (position[0] - previousPosition[0]) / (double)elapsedSec;
			velocity[1] = (position[1] - previousPosition[1]) / (double)elapsedSec;
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

	public void setWeight(double weight) {
		this.mass = weight * 0.453592; // convert to kilogram
	}

	public double getWeight() {
		return this.mass * 0.453592;
	}

	public double getMass() {
		return this.mass;
	}

	private void updateLatestSignal() {

	}
}