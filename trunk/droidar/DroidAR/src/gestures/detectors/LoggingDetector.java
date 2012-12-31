package gestures.detectors;

import android.util.Log;
import gestures.PhoneGesture;
import gestures.PhoneGestureDetector;
import gestures.SensorData;

/**
 * A dummy detector that's simply printing out the measured sensor data to the
 * debug log.
 * 
 * @author marmat (Martin Matysiak)
 * 
 */
public class LoggingDetector implements PhoneGestureDetector {

	@Override
	public PhoneGesture getType() {
		return PhoneGesture.NONE;
	}

	@Override
	public double getProbability() {
		return 0;
	}

	@Override
	public void feedSensorEvent(SensorData sensorData) {
		Log.d("LoggingDetector", String.format("Abs: %.4f\nGra: %s\nLin: %s",
				sensorData.absoluteAcceleration, 
				formatArray(sensorData.gravity),
				formatArray(sensorData.linearAcceleration)));
	}
	
	private String formatArray(double[] array) {
		StringBuilder builder = new StringBuilder("[ ");
		for (double value : array) {
			builder.append(String.format("% 8.4f ", value));
		}
		return builder.append("]").toString();
	}
}
