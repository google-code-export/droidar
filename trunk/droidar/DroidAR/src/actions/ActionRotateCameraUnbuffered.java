package actions;

import system.ParentStack;
import worldData.Updateable;
import android.util.Log;
import android.view.MotionEvent;
import gl.GLCamera;

public class ActionRotateCameraUnbuffered extends ActionWithSensorProcessing {

	private static final String LOG_TAG = "ActionRotateCameraUnbuffered";

	public ActionRotateCameraUnbuffered(GLCamera targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		// no buffering at all so dont init any algos
	}

}
