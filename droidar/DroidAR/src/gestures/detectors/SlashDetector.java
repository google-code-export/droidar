package gestures.detectors;

import gestures.PhoneGesture;
import gestures.PhoneGestureDetector;
import gestures.SensorData;

/**
 * A detector to detect "Slashing" movements by simple peak detection.
 * 
 * @author marmat (Martin Matysiak)
 *
 */
public class SlashDetector implements PhoneGestureDetector {

	/**
	 * The rate with which the probability will decrease when there's not
	 * enough movement for a slash.
	 */
	private static final double PROBABILITY_DECAY = 0.5;

	/**
	 * The acceleration in m/s^2 for a move being detected as a slash.
	 */
	private static final double SLASH_THRESHOLD = 10;
	
	/**
	 * The current gesture probability.
	 */
	private double gestureProbability = 0;
	
	@Override
	public PhoneGesture getType() {
		return PhoneGesture.SLASH;
	}

	@Override
	public double getProbability() {
		return gestureProbability;
	}

	@Override
	public void feedSensorEvent(SensorData sensorData) {
		if (sensorData.absoluteAcceleration > SLASH_THRESHOLD) {
			gestureProbability = 1;
		} else {
			gestureProbability *= (1 - PROBABILITY_DECAY);
		}
	}
}
