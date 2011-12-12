package actions;

import gl.GLCamera;
import actions.algos.Algo;
import actions.algos.BufferAlgo3;
import actions.algos.SensorAlgo1;
import android.view.MotionEvent;

public class ActionRotateCameraBuffered3 extends ActionWithSensorProcessing {

	public ActionRotateCameraBuffered3(GLCamera targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4); // TODO

		accelBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
		magnetBufferAlgo = new BufferAlgo3(0.2f, 0.1f, 4);
	}

}
