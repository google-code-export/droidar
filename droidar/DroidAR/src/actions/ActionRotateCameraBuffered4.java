package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.BufferAlgo2;
import actions.algos.SensorAlgo1;
import android.view.MotionEvent;

public class ActionRotateCameraBuffered4 extends Action {

	private GLCamera myTargetCamera;

	private Algo magnetAlgo;
	private Algo accelAlgo;

	private Algo orientAlgo;
	private Algo orientationBufferAlgo;

	private Algo accelBufferAlgo;
	private Algo magnetBufferAlgo;

	public ActionRotateCameraBuffered4(GLCamera targetCamera) {
		myTargetCamera = targetCamera;
		// myTargetCamera.setUpdateListener(this);
		registerAtCamera(targetCamera);
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);
		accelBufferAlgo = new BufferAlgo2(0.2f);
		magnetBufferAlgo = new BufferAlgo2(0.2f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo2(0.2f); // TODO
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
