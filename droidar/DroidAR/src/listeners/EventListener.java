package listeners;

import gl.GLCamera;
import util.Vec;
import android.location.Location;
import android.view.MotionEvent;

public interface EventListener {

	public abstract boolean onOrientationChanged(float[] values);

	public abstract boolean onLocationChanged(Location location);

	public abstract boolean onTouchMove(float screenDeltaX, float screenDeltaY);

	public abstract boolean onAccelChanged(float[] values);

	public abstract boolean onMagnetChanged(float[] values);

	public abstract boolean onReleaseTouchMove();

	/**
	 * @param x
	 * @param y
	 * @param event
	 *            might be null so check first!
	 * @return
	 */
	public abstract boolean onTrackballEvent(float x, float y, MotionEvent event);

	/**
	 * is requested from the camera and here the update algo should be called if
	 * there is one otherwise just set target to values
	 * 
	 * @param target
	 * @param newValues
	 * @param timeDelta
	 * @return true if target has been changed
	 */
	public boolean onCamAccelerationUpdate(float[] target, float[] newValues,
			float timeDelta);

	/**
	 * Is called by the {@link GLCamera} and here the update algo should be
	 * called if there is one otherwise just set target to values
	 * 
	 * @param target
	 * @param values
	 * @param timeDelta
	 * @return true if target has been changed
	 */
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta);

	public void onCamRotationVecUpdate(Vec target, Vec values, float timeDelta);

	public void onCamOffsetVecUpdate(Vec target, Vec values, float timeDelta);

	public void onCamPositionVecUpdate(Vec target, Vec values, float timeDelta);

	public abstract boolean onCamOrientationUpdate(float[] myOrientValues,
			float[] myNewOrientValues, float timeDelta);

}