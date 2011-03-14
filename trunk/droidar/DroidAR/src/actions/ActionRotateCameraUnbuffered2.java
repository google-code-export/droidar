package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.SensorAlgo1;

public class ActionRotateCameraUnbuffered2 extends Action {

	private Algo magnetAlgo;
	private Algo accelAlgo;
	private GLCamera myTargetCamera;

	public ActionRotateCameraUnbuffered2(GLCamera targetCamera) {
		myTargetCamera = targetCamera;
		// myTargetCamera.setUpdateListener(this);
		registerAtCamera(targetCamera);
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);
	}

	@Override
	public boolean onOrientationChanged(float xAngle, float yAngle, float zAngle) {
		myTargetCamera.setNewRotation(xAngle, yAngle, zAngle);
		return true;
	}

	@Override
	public boolean onTouchMove(float screenDeltaX, float screenDeltaY) {
		myTargetCamera.changeZAngleBuffered(screenDeltaY);
		return true;
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		return myTargetCamera.setAccelValues(accelAlgo.execute(values));
	}

	@Override
	public boolean onMagnetChanged(float[] values) {
		return myTargetCamera.setMagnetValues(magnetAlgo.execute(values));
	}

	@Override
	public boolean onCamAccelerationUpdate(float[] target, float[] values,
			float timeDelta) {
		System.out.println("accelUpdate");
		return super.onCamAccelerationUpdate(target, values, timeDelta);
	}

	@Override
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta) {
		System.out.println("magnetoUpdate");
		return super.onCamMagnetometerUpdate(target, values, timeDelta);
	}

	@Override
	public boolean onReleaseTouchMove() {
		myTargetCamera.resetBufferedAngle();
		return true;
	}

}
