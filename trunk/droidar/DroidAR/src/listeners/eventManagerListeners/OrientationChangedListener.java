package listeners.eventManagerListeners;

import android.hardware.SensorEventListener;

public interface OrientationChangedListener {
	/**
	 * see
	 * {@link SensorEventListener#onSensorChanged(android.hardware.SensorEvent)}
	 * 
	 * @param values
	 * @return
	 */
	public abstract boolean onOrientationChanged(float[] values);
}
