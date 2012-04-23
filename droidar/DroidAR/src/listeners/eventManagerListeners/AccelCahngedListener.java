package listeners.eventManagerListeners;

import android.hardware.SensorEventListener;

public interface AccelCahngedListener {
	/**
	 * see
	 * {@link SensorEventListener#onSensorChanged(android.hardware.SensorEvent)}
	 * 
	 * @param values
	 * @return
	 */
	public abstract boolean onAccelChanged(float[] values);
}
