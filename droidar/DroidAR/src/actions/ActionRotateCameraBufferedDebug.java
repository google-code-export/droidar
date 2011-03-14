package actions;

import worldData.Updateable;
import gl.GLCamera;

/**
 * this class is for debugging purpose only at the moment and has to be modified
 * to work. it extracts the the yaw, pitch and roll from the sensor data arrays
 * 
 * @author Spobo
 * 
 */
public class ActionRotateCameraBufferedDebug extends Action {

	private Updateable myCamera;

	public ActionRotateCameraBufferedDebug(GLCamera camera) {
		myCamera = camera;
		// myCamera.setUpdateListener(this);
		registerAtCamera(camera);
	}

	@Override
	public boolean onAccelChanged(float[] values) {

		float pitch = (float) +Math.toDegrees(Math.atan2(-values[1], Math
				.sqrt(values[2] * values[2] + values[0] * values[0])));
		float roll = (float) -Math.toDegrees(Math.atan2(values[0], -values[2]));

		return true;
	}

	@Override
	public boolean onMagnetChanged(float[] values) {

		float yaw = (float) -Math.toDegrees(Math.atan2(-values[0], Math
				.sqrt(values[1] * values[1] + values[2] * values[2])));
		float pitch = (float) -Math
				.toDegrees(Math.atan2(-values[2], values[1]));

		return true;
	}

	@Override
	public boolean onOrientationChanged(float xAngle, float yAngle, float zAngle) {

		float pitch = -xAngle;
		float roll = -yAngle;
		float yaw = -zAngle;

		return true;
	}

}
