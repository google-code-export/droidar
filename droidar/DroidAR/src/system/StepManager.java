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

	private LinkedList<OnStepListener> listeners = new LinkedList<OnStepListener>();

	private int step_timeout_ms = 666;
	private double minStepPeakSize = 0.8;
	private double step_length_in_m = 0.6;

	private Handler handler = new Handler();
	private long handler_delay_millis = 1000 / 30;
	boolean handler_is_running = false;

	private float[] last_acc_event = { 0f, 0f, 0f };
	private long last_step_ms;
	private float orientation = 0.0f;
	private static final int vhSize = 6;

	private static final String LOG_TAG = "StepManager";
	private float[][] stepDetecWindow = new float[vhSize][];
	private int vhPointer = 0;

	public interface OnStepListener {

		public void onStep(double bearing, double steplength);
	}

	public void registerSensors(Context context) {
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

	public void start() {
		handler_is_running = true;
		handler.removeCallbacks(handlerStepDetection);
		handler.postDelayed(handlerStepDetection, handler_delay_millis);

	}

	public void stop() {
		handler.removeCallbacks(handlerStepDetection);
		handler_is_running = false;
	}

	public void unregisterSensors() {
		sensorManager.unregisterListener(this);
	}

	public void registerStepListener(OnStepListener l) {
		listeners.add(l);
	}

	public void unRegisterStepListener(OnStepListener l) {
		listeners.remove(l);
	}

	private void addSensorData(float[] value) {

		stepDetecWindow[vhPointer % vhSize] = value.clone();
		vhPointer++;
		vhPointer = vhPointer % vhSize;
	}

	private boolean checkForStep() {
		// Add value to values_history

		int lookahead = 5;
		for (int t = 1; t <= lookahead; t++) {

			float[] a = stepDetecWindow[(vhPointer - 1 - t + vhSize + vhSize)
					% vhSize];
			float[] b = stepDetecWindow[(vhPointer - 1 + vhSize) % vhSize];

			if (a != null) {
				double check = a[2] - b[2];
				// System.out.println("a[2]=" + a[2]);
				// System.out.println("b[2]=" + b[2]);
				// System.out.println("check=" + check);
				if (check >= minStepPeakSize) {
					Log.i(LOG_TAG, "Detected step with t = " + t
							+ ", peakSize = " + minStepPeakSize + " < " + check);
					return true;
				}
			}
		}
		return false;
	}

	// Handler code

	private Location moveLocation(Location l, double d, double bearing) {
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

		Location ret = new Location("LOCMOV");
		ret.setLatitude(Math.toDegrees(lat2));
		ret.setLongitude(Math.toDegrees(lon2));
		ret.setAccuracy((float) (2.0f * d));
		ret.setAltitude(0);
		ret.setTime(System.currentTimeMillis());
		Log.i("JNR_LOCMOV", "From " + l.getLatitude() + "/" + l.getLongitude()
				+ " to " + ret.getLatitude() + "/" + ret.getLongitude()
				+ " using " + d + "m (" + bearing + ")");

		return ret;
	}

	private Runnable handlerStepDetection = new Runnable() {

		public void run() {
			// if start is called twice: we have "two" threads, i.e clean this
			handler.removeCallbacks(handlerStepDetection);
			addSensorData(last_acc_event);
			long t = System.currentTimeMillis();
			if (t - last_step_ms > step_timeout_ms && checkForStep()) {

				System.out.println("Step detected");

				// ############ ACTION ! ##########
				// notify listeners

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

			last_acc_event[0] = event.values[0] - gravity[0];
			last_acc_event[1] = event.values[1] - gravity[1];
			last_acc_event[2] = event.values[2] - gravity[2];

			break;

		case Sensor.TYPE_ORIENTATION:
			orientation = event.values[0];
			break;
		}
	}

}