package system;

import java.util.LinkedList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

/**
 * @author Paul Smith code@uvwxy.de
 * 
 */
public class StepManager implements SensorEventListener {

	private SensorManager sensorManager;

	private LinkedList<OnStepListener> listeners;

	private int step_timeout_ms = 666;
	private double minStepPeakSize = 0.8;
	private double step_length_in_m = 0.6;

	private Handler handler = new Handler();
	private long handler_delay_millis = 1000 / 30;
	boolean handler_is_running = false;

	private float last_acc_event = 0f;
	private long last_step_ms;
	private float orientation = 0.0f;
	private static final int vhSize = 6;

	private static final String LOG_TAG = "StepManager";
	private float[] stepDetecWindow = new float[vhSize];
	private int vhPointer = 0;

	public interface OnStepListener {

		public void onStep(double bearing, double steplength);
	}

	private void registerSensors(Context context) {
		// register acceleraion sensor

		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		Sensor magnetSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, magnetSensor,
				SensorManager.SENSOR_DELAY_GAME);
		Sensor accelSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelSensor,
				SensorManager.SENSOR_DELAY_GAME);

	}

	private void start() {
		handler_is_running = true;
		handler.removeCallbacks(handlerStepDetection);
		handler.postDelayed(handlerStepDetection, handler_delay_millis);

	}

	private void stop() {
		handler.removeCallbacks(handlerStepDetection);
		handler_is_running = false;
	}

	private void unregisterSensors() {
		sensorManager.unregisterListener(this);
	}

	public void registerStepListener(Context context, OnStepListener l) {
		if (listeners == null) {
			listeners = new LinkedList<OnStepListener>();
			registerSensors(context);
			start();
		}
		listeners.add(l);
	}

	public void unRegisterStepListener(OnStepListener l) {
		listeners.remove(l);
		if (listeners.isEmpty()) {
			stop();
			unregisterSensors();
			listeners = null;
		}
	}

	private void addCurrentSensorData() {
		stepDetecWindow[vhPointer % vhSize] = last_acc_event;
		vhPointer++;
		vhPointer = vhPointer % vhSize;
	}

	private boolean checkIfStepHappend() {
		// Add value to values_history
		int lookahead = 5;
		for (int t = 1; t <= lookahead; t++) {
			double check = stepDetecWindow[(vhPointer - 1 - t + vhSize + vhSize)
					% vhSize]
					- stepDetecWindow[(vhPointer - 1 + vhSize) % vhSize];
			if (check >= minStepPeakSize) {
				// Log.i(LOG_TAG, "Detected step with t = " + t +
				// ", peakSize = "
				// + minStepPeakSize + " < " + check);
				return true;
			}

		}
		return false;
	}

	// Handler code

	/**
	 * Takes a location and updates its position according to the step
	 * 
	 * @param l
	 * @param d
	 * @param bearing
	 * @return
	 */
	public static Location moveLocationOneStep(Location l, double d,
			double bearing) {
		bearing = Math.toRadians(bearing);
		double R = 6378100; // m equatorial radius
		double lat1 = Math.toRadians(l.getLatitude());
		double lon1 = Math.toRadians(l.getLongitude());
		double dr = d / R;
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dr) + Math.cos(lat1)
				* Math.sin(dr) * Math.cos(bearing));
		double lon2 = lon1
				+ Math.atan2(
						Math.sin(bearing) * Math.sin(d / R) * Math.cos(lat1),
						Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

		Location ret = l;// new Location("LOCMOV");
		ret.setLatitude(Math.toDegrees(lat2));
		ret.setLongitude(Math.toDegrees(lon2));
		ret.setAccuracy((float) (2.0f * d));
		ret.setAltitude(0);
		ret.setTime(System.currentTimeMillis());
		return ret;
	}

	private Runnable handlerStepDetection = new Runnable() {

		public void run() {
			// if start is called twice: we have "two" threads, i.e clean this
			handler.removeCallbacks(handlerStepDetection);
			addCurrentSensorData();
			long t = System.currentTimeMillis();
			if (t - last_step_ms > step_timeout_ms && checkIfStepHappend()) {

				System.out.println("Step detected");

				for (OnStepListener l : listeners) {
					l.onStep(orientation, step_length_in_m);
				}

				last_step_ms = t;
			}

			// no movements if no steps/jumps detected at all, or not in the
			// standing_timeout_ms intervall

			if (handler_is_running)
				handler.postDelayed(this, handler_delay_millis);
		}
	};

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	final float alpha = 0.8f;
	float[] gravity = { .0f, .0f, .0f };

	@Override
	public void onSensorChanged(SensorEvent event) {

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			float x = event.values[0] - gravity[0];
			float y = event.values[1] - gravity[1];
			float z = event.values[2] - gravity[2];
			last_acc_event = (float) Math.sqrt(x * x + y * y + z * z);

			break;

		case Sensor.TYPE_ORIENTATION:
			orientation = event.values[0];
			break;
		}
	}

}