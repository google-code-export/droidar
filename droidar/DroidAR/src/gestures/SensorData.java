package gestures;

/**
 * A wrapper class for all data that will be collected by the PhoneGestureSensor
 * and passed on to the detectors. Calculates a few properties out of the given
 * sensor data that is commonly used accross detectors.
 * 
 * @author marmat (Martin Matysiak)
 */
public class SensorData {
	public final double[] gravity;
	public final double[] linearAcceleration;
	public final double absoluteAcceleration;

	public SensorData(double[] gravity, double[] linearAcceleration) {
		this.gravity = gravity;
		this.linearAcceleration = linearAcceleration;
		this.absoluteAcceleration = Math.sqrt(
				Math.pow(linearAcceleration[0], 2)
				+ Math.pow(linearAcceleration[1], 2)
				+ Math.pow(linearAcceleration[2], 2));
	}
}
