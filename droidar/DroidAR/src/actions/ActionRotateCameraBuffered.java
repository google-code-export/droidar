package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.BufferAlgo1;
import actions.algos.SensorAlgo1;

public class ActionRotateCameraBuffered extends Action {

	private GLCamera myTargetCamera;

	private Algo magnetAlgo;
	private Algo accelAlgo;

	private Algo orientAlgo;
	//private Algo orientationBufferAlgo;

	private Algo accelBufferAlgo;
	private Algo magnetBufferAlgo;

	public ActionRotateCameraBuffered(GLCamera targetCamera) {
		myTargetCamera = targetCamera;
		// myTargetCamera.setUpdateListener(this);
		registerAtCamera(targetCamera);
		accelAlgo = new SensorAlgo1(0.1f);
		magnetAlgo = new SensorAlgo1(1.4f);

		orientAlgo = new SensorAlgo1(0.005f);// TODO
		//orientationBufferAlgo = new BufferAlgo1(0.01f, 0.02f); // TODO

		accelBufferAlgo = new BufferAlgo1(0.1f, 4f);
		magnetBufferAlgo = new BufferAlgo1(0.1f, 4f);

	}

	@Override
	public boolean onTouchMove(float screenDeltaX, float screenDeltaY) {
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
	public boolean onOrientationChanged(float[] values) {
		myTargetCamera.setOrientationValues(orientAlgo.execute(values));
		return true;
	}

	// @Override
	// public boolean onCamOrientationUpdate(float[] target, float[] newValues,
	// float timeDelta) {
	// // target[0] = newValues[0];
	// // target[1] = newValues[1];
	// // target[2] = newValues[2];
	// return orientationBufferAlgo.execute(target, newValues,
	// timeDelta);
	// }

	@Override
	public boolean onReleaseTouchMove() {
		myTargetCamera.resetBufferedAngle();
		return true;
	}

}
