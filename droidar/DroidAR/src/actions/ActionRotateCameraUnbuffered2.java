package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.SensorAlgo1;
import android.view.MotionEvent;

public class ActionRotateCameraUnbuffered2 extends Action {

	private Algo magnetAlgo;
	private Algo accelAlgo;
	private Algo orientAlgo;

	private GLCamera myTargetCamera;

	public ActionRotateCameraUnbuffered2(GLCamera targetCamera) {
		myTargetCamera = targetCamera;
		// myTargetCamera.setUpdateListener(this);
		registerAtCamera(targetCamera);
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
	}

	@Override
	public boolean onTouchMove(MotionEvent e1, MotionEvent e2,
			float screenDeltaX, float screenDeltaY) {
		myTargetCamera.changeZAngleBuffered(screenDeltaY);
		return true;
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		return myTargetCamera.setAccelValues(accelAlgo.execute(values));
	}

	@Override
	public boolean onOrientationChanged(float[] values) {
		myTargetCamera.setOrientationValues(orientAlgo.execute(values));
		return true;
	}

	@Override
	public boolean onMagnetChanged(float[] values) {
		return myTargetCamera.setMagnetValues(magnetAlgo.execute(values));
	}

	@Override
	public boolean onReleaseTouchMove() {
		myTargetCamera.resetBufferedAngle();
		return true;
	}

}
