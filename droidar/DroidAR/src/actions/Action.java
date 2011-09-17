package actions;

import gl.GLCamera;
import listeners.EventListener;
import util.Vec;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;

public abstract class Action implements EventListener {

	// protected static final float BUFFER_SPEED_ACCEL_SENSOR = 500;
	// protected static final float BUFFER_SPEED_MAGNET_SENSOR = 1500;

	protected static final float SMOOTH_ROTATION_SPEED = 2;
	protected static final float SMOOTH_MOTION_SPEED = 3;
	private static final String LOG_TAG = "action event";

	private boolean accelNotCatchedOutputFlag;
	private boolean magnetNotCatchedOutputFlag;

	protected void registerAtCamera(GLCamera camera) {
		EventListener u = camera.getUpdateListener();
		if (u instanceof EventListenerGroup) {
			((EventListenerGroup) u).add(this);
		} else if (u != null) {
			EventListenerGroup g = new EventListenerGroup();
			g.add(u);
			g.add(this);
			camera.setUpdateListener(g);
		} else {
			camera.setUpdateListener(this);
		}
	}

	@Override
	public boolean onOrientationChanged(float[] values) {
		Log.e("action event",
				"onOrientationChanged not catched by defined action: "
						+ this.getClass());
		return false;
	}

	@Override
	public boolean onLocationChanged(Location location) {

		String s = "";
		s += "Time: " + location.getTime() + "\n";
		s += "\t Latitude:  " + location.getLatitude() + "\n";
		s += "\t Longitude: " + location.getLongitude() + "\n";
		s += "\t Altitude:  " + location.getAltitude() + "\n";
		s += "\t Accuracy:  " + location.getAccuracy() + "\n";
		Log.d("Location Info", s);

		Log.e("action event", "changeLocation not catched by defined action: "
				+ this.getClass());
		return false;
	}

	@Override
	public boolean onTouchMove(MotionEvent e1, MotionEvent e2,
			float screenDeltaX, float screenDeltaY) {
		Log.e("action event",
				"onTouch not catched by defined action: " + this.getClass());
		return false;
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		if (!accelNotCatchedOutputFlag) {
			accelNotCatchedOutputFlag = true;
			Log.e("action event",
					"AccelerationValuesChanged not catched by defined action: "
							+ this.getClass());
		}
		return false;
	}

	@Override
	public boolean onMagnetChanged(float[] values) {
		if (!magnetNotCatchedOutputFlag) {
			magnetNotCatchedOutputFlag = true;
			Log.e("action event",
					"MegnetometerValuesChanged not catched by defined action: "
							+ this.getClass());
		}
		return false;
	}

	@Override
	public boolean onReleaseTouchMove() {
		Log.e("action event",
				"onReleaseTouchMove not catched by defined action: "
						+ this.getClass());
		return false;
	}

	@Override
	public boolean onTrackballEvent(float x, float y, MotionEvent event) {
		Log.e(LOG_TAG, "onTrackballEvent not catched by defined action: "
				+ this.getClass());
		return false;
	}

	@Override
	public boolean onCamAccelerationUpdate(float[] target, float[] values,
			float timeDelta) {
		Log.e(LOG_TAG, "onAccelerationUpdate requested by glCamera not "
				+ "catched by defined action: " + this.getClass());
		return false;
	}

	@Override
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta) {
		Log.e(LOG_TAG, "onMagnetometerUpdate requested by glCamera not "
				+ "catched by defined action: ");
		return false;
	}

	@Override
	public boolean onCamOrientationUpdate(float[] myOrientValues,
			float[] myNewOrientValues, float timeDelta) {
		Log.e(LOG_TAG, "onCamOrientationUpdate requested by glCamera not "
				+ "catched by defined action: ");
		return false;
	}

	/**
	 * default implementation done here (Vec.morphToNewAngleVec())
	 */
	@Override
	public void onCamRotationVecUpdate(Vec target, Vec values, float timeDelta) {
		Vec.morphToNewAngleVec(target, values.x, values.y, values.z, timeDelta
				* SMOOTH_ROTATION_SPEED);
	}

	/**
	 * default implementation done here (Vec.morphToNewVec())
	 */
	@Override
	public void onCamOffsetVecUpdate(Vec target, Vec values, float timeDelta) {
		Vec.morphToNewVec(target, values, timeDelta * SMOOTH_MOTION_SPEED);
	}

	/**
	 * default implementation done here (Vec.morphToNewVec())
	 */
	@Override
	public void onCamPositionVecUpdate(Vec target, Vec values, float timeDelta) {
		Vec.morphToNewVec(target, values, timeDelta * SMOOTH_MOTION_SPEED);
	}

}