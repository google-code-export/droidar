package actions;

import listeners.eventManagerListeners.OrientationChangedListener;
import android.hardware.SensorManager;

public abstract class ActionUseCameraAngles2 implements
		OrientationChangedListener {

	private float[] mag;
	private float[] accel;
	private boolean sensorRead;
	float[] R = new float[16];
	float[] outR = new float[16];
	float[] I = new float[16];

	final float rad2deg = 180 / (float) Math.PI;
	float[] o = new float[3];

	@Override
	public boolean onMagnetChanged(float[] values) {
		mag = values;
		sensorRead = true;
		calcMatrix();
		return true;
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		accel = values;
		calcMatrix();
		return true;
	}

	@Override
	public boolean onOrientationChanged(float[] values) {
		// TODO
		return true;
	}

	private void calcMatrix() {
		if (mag != null && accel != null && sensorRead) {
			sensorRead = false;

			SensorManager.getRotationMatrix(R, I, accel, mag);
			SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
					SensorManager.AXIS_Z, outR);
			SensorManager.getOrientation(R, o);

			updatePitch(o[1] * rad2deg);
			// north=0 east = 90 south 180 west=180
			updateCompassAzimuth(o[0] * rad2deg + 90);
			// floor=0 horizon 90 sky=180 (or -180)
			updateRoll(o[2] * rad2deg);
		}
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

}
