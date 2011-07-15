package actions;

import worldData.Updateable;

/**
 * accel sensor: has normal values from -11 to +11 and shake values from -19 to
 * 19
 * 
 * magneto sensor: has normal values from -60 to +60 and metal/megnet values
 * from -120 to 120
 * 
 * @author Spobo
 * 
 */
public class ActionRotateCameraDirectlyBuffered extends Action {

	private Updateable myCamera;
	// private float max = -1000;
	// private float min = +1000;
	private float mx;
	private float my;
	private float mz;
	private float magnetBarrier = 0.8f;
	private float ax;
	private float ay;
	private float az;
	private float accelBarrier = 0.5f;

	public ActionRotateCameraDirectlyBuffered(Updateable camera) {
		myCamera = camera;
	}

	@Override
	public boolean onAccelChanged(float[] v) {
		System.out.println("accel");
		ax = checkAndCalc(ax, v[0], accelBarrier);
		ay = checkAndCalc(ay, v[1], accelBarrier);
		az = checkAndCalc(az, v[2], accelBarrier);
		// myCamera.changeAccelValues(ax, ay, az);
		return true;
	}

	@Override
	public boolean onMagnetChanged(float[] v) {
		System.out.println("magnet");
		mx = checkAndCalc(mx, v[0], magnetBarrier);
		my = checkAndCalc(my, v[1], magnetBarrier);
		mz = checkAndCalc(mz, v[2], magnetBarrier);
		// myCamera.changeMagnetValues(mx, my, mz);

		return true;
	}

	private float checkAndCalc(float oldV, float newV, float barrier) {
		float aa = oldV - newV;
		if (!(-barrier < aa && aa < barrier)) {
			return (oldV + newV) / 2;
		}
		return oldV;
	}

}
