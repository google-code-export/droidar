package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.BufferAlgo3;
import actions.algos.SensorAlgo1;
import android.view.MotionEvent;

public class ActionRotateCameraBuffered3 extends Action {

	private GLCamera myTargetCamera;

	private Algo magnetAlgo;
	private Algo accelAlgo;

	private Algo orientAlgo;
	private Algo orientationBufferAlgo;

	private Algo accelBufferAlgo;
	private Algo magnetBufferAlgo;

	public ActionRotateCameraBuffered3(GLCamera targetCamera) {
		myTargetCamera = targetCamera;
		// myTargetCamera.setUpdateListener(this);
		registerAtCamera(targetCamera);
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4); // TODO

		accelBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
		magnetBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
	}

	@Override
	public boolean onOrientationChanged(float[] values) {
		myTargetCamera.setOrientationValues(orientAlgo.execute(values));
		return true;
	}

	@Override
	public boolean onCamOrientationUpdate(float[] target, float[] newValues,
			float timeDelta) {
		return orientationBufferAlgo.execute(target, newValues, timeDelta);
	}

	@Override
	public boolean onTouchMove(MotionEvent e1, MotionEvent e2,
			float screenDeltaX, float screenDeltaY) {
		myTargetCamera.changeZAngleBuffered(screenDeltaY);
		return true;
	}

	@Override
	public synchronized boolean onAccelChanged(float[] values) {

		myTargetCamera.setAccelValuesBuffered(accelAlgo.execute(values));
		return true;
	}

	@Override
	public synchronized boolean onMagnetChanged(float[] values) {
		myTargetCamera.setMagnetValuesBuffered(magnetAlgo.execute(values));
		return true;
	}

	@Override
	public synchronized boolean onCamAccelerationUpdate(float[] target,
			float[] values, float timeDelta) {
		return accelBufferAlgo.execute(target, values, timeDelta);
	}

	@Override
	public synchronized boolean onCamMagnetometerUpdate(float[] target,
			float[] values, float timeDelta) {
		return magnetBufferAlgo.execute(target, values, timeDelta);
	}

	@Override
	public boolean onReleaseTouchMove() {
		myTargetCamera.resetBufferedAngle();
		return true;
	}

}
