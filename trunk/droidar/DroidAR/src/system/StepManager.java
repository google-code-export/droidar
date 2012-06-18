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

public class StepManager implements SensorEventListener {

	private SensorManager sensorManager;

	private LinkedList<OnStepListener> listeners = new LinkedList<OnStepListener>();

	private int step_timeout_ms = 666;
	private double stepPeak = 0.8;
	private double step_length_in_m = 0.6;

	private Handler handler = new Handler();
	private long handler_delay_millis = 1000 / 30;
	private boolean handler_is_initialized = false;
	boolean handler_is_running = false;

	private float[] last_acc_event = { 0f, 0f, 0f };
	private long last_step_ms;
	private float orientation = 0.0f;
	private static final int vhSize = 6;

	private static final String LOG_TAG = "StepManager";
	private float[][] stepDetectionWindow = new float[vhSize][];
	private int vhPointer = 0;

	public interface OnStepListener {

		public void onStep(double bearing, double steplength);
	}

	public void registerSensors(Context context) {
		// register acceleraion sensor

		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		Sensor magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_GAME);
		Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);

	}

	public void start() {
		if (!handler_is_initialized) {
			handlerStepDetection.run();
			handler_is_initialized = true;
			handler_is_running = true;
		} else {
			handler.removeCallbacks(handlerStepDetection);
			handler.postDelayed(handlerStepDetection, handler_delay_millis);
			handler_is_running = true;
		}
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
		stepDetectionWindow[vhPointer % vhSize] = value;
		vhPointer++;
		vhPointer = vhPointer % vhSize;
	}

	private boolean checkForStep(double peakSize) {
		// Add value to values_history

		int lookahead = 5;

		for (int t = 1; t <= lookahead; t++) {
			// catch nullpointer exception at the beginning
			if (stepDetectionWindow[(vhPointer - 1 - t + vhSize + vhSize) % vhSize] != null) {
				double check = stepDetectionWindow[(vhPointer - 1 - t + vhSize + vhSize) % vhSize][2] - stepDetectionWindow[(vhPointer - 1 + vhSize) % vhSize][2];
				if (check >= peakSize) {
					// Log.i("JNR_LOCMOV", "Detected step with t = " + t
					// + ", peakSize = " + peakSize + " < " + check);
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
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dr) + Math.cos(lat1) * Math.sin(dr) * Math.cos(bearing));
		double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(d / R) * Math.cos(lat1), Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

		Location ret = new Location("LOCMOV");
		ret.setLatitude(Math.toDegrees(lat2));
		ret.setLongitude(Math.toDegrees(lon2));
		ret.setAccuracy((float) (2.0f * d));
		ret.setAltitude(0);
		ret.setTime(System.currentTimeMillis());
		Log.i("JNR_LOCMOV", "From " + l.getLatitude() + "/" + l.getLongitude() + " to " + ret.getLatitude() + "/" + ret.getLongitude() + " using " + d + "m ("
				+ bearing + ")");

		return ret;
	}

	private Runnable handlerStepDetection = new Runnable() {

		public void run() {
			// if start is called twice: we have "two" threads, i.e clean this
			handler.removeCallbacks(handlerStepDetection);

			addSensorData(last_acc_event);

			long t = System.currentTimeMillis();

			if (checkForStep(stepPeak) && last_step_ms > step_timeout_ms) {

				// ############ ACTION ! ##########
				// notify listeners

				for(OnStepListener l: listeners){
					l.onStep(orientation,step_length_in_m);
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