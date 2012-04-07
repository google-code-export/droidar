package system;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * This subclass of {@link EventManager} will automatically be used for tablets
 * to fix the orientation mapping
 * 
 * @author Spobo
 * 
 */
public class TabletEventManager extends EventManager {
	private float[] accelerometerValues = new float[3];

	public TabletEventManager() {
		super();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (onOrientationChangedAction != null) {

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				/*
				 * change accel sensor data according to
				 * http://code.google.com/p
				 * /libgdx/source/browse/trunk/backends/gdx
				 * -backend-android/src/com
				 * /badlogic/gdx/backends/android/AndroidInput.java
				 */

				accelerometerValues[0] = event.values[1];
				accelerometerValues[1] = -event.values[0];
				accelerometerValues[2] = event.values[2];

				onOrientationChangedAction.onAccelChanged(accelerometerValues);
			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				onOrientationChangedAction.onMagnetChanged(event.values);
			}

			// else sensor input is set to orientation mode
			if (event.sensor.getType() == 11) {// Sensor.TYPE_ROTATION_VECTOR) {
				onOrientationChangedAction.onOrientationChanged(event.values);
			}
		}
	}
}
