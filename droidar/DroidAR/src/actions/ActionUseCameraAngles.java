package actions;

import gl.GLCamera;
import gl.GLCamera.CameraAngleUpdateListener;
import util.Vec;

/**
 * Children of this class can access the angles of the specified camera
 * 
 * @author Spobo
 * 
 */
public abstract class ActionUseCameraAngles extends Action implements
		CameraAngleUpdateListener {

	private int accelCounter;
	private int accelThreshold = 10;

	// private int magnetCounter;
	// private int magnetThreshold = 10;

	/**
	 * this affects how often the updatePitch() and updateRoll() methods are
	 * called.
	 * 
	 * @param threshold
	 *            1 means update on every event 10 means update every 10 events.
	 *            default is 10
	 */
	public void setUpdateThreshold(int threshold) {
		this.accelThreshold = threshold;
	}

	public ActionUseCameraAngles(GLCamera camera) {
		// myCamera = camera;
		registerAtCamera(camera);
		camera.setAngleUpdateListener(this);
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		return true;
	}

	@Override
	public boolean onMagnetChanged(float[] values) {
		return true;
	}

	@Override
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta) {
		return true;
	}

	@Override
	public boolean onCamAccelerationUpdate(float[] target, float[] values,
			float timeDelta) {
		accelCounter++;
		if (accelCounter > accelThreshold) {
			accelCounter = 0;
			/*
			 * missing documentation for the following calculations.. TODO
			 */
			updatePitch((float) Math.toDegrees(Math.atan2(-target[1],
					Math.sqrt(target[2] * target[2] + target[0] * target[0]))));
			updateRoll(180 + (float) -Math.toDegrees(Math.atan2(target[0],
					-target[2])));

		}
		return true;
	}

	/**
	 * pitch is the rotation around the x axis. when the device would be a
	 * steering wheel, this method would indicate how much it is rotated
	 * 
	 * @param pitchAngle
	 *            0 means the car drives straight forward, positive values (0 to
	 *            90) mean that the car turns left, negative values mean that
	 *            the car turns right
	 */
	public abstract void updatePitch(float pitchAngle);

	/**
	 * the roll is the rotation around the y axis. if the device would be your
	 * head 0 would mean you are looking on the ground, 90 would mean you look
	 * in front of you and 180 would mean you look in the sky
	 * 
	 * @param rollAngle
	 *            from 0 to 360. 0 means the camera targets the ground, 180 the
	 *            camera looks into the sky
	 */
	public abstract void updateRoll(float rollAngle);

	/**
	 * @param azimuth
	 *            0=north 90=east 180=south 270=west
	 */
	public abstract void updateCompassAzimuth(float azimuth);

	@Override
	public boolean onOrientationChanged(float[] values) {
		/*
		 * the use of the orientation sensor results in other values then the
		 * normal magnetometer and accelerometer values. Therefore it was
		 * disabled for the moment.
		 */
		// float pitch = -xAngle;
		// float roll = -yAngle;
		// updateCompassDirection(-zAngle);
		return true;
	}

	final float rad2deg = (float) (180.0f / Math.PI);

	public void updateAnglesByCamera(float[] anglesInRadians,
			Vec cameraRotationVec) {

		/*
		 * this is used to extract the azimuth because it is the easiest way to
		 * access it. The pitch and roll wont be used because they are
		 * calculated in a different way where the results are more accurate and
		 * reliable
		 */

		// float pitch = anglesInRadians[1] * rad2deg;
		// float roll = anglesInRadians[2] * -rad2deg;

		float azimuth = anglesInRadians[0] * rad2deg;
		azimuth += cameraRotationVec.z;
		if (azimuth >= 360)
			azimuth -= 360;
		updateCompassAzimuth(azimuth);
	}
}
