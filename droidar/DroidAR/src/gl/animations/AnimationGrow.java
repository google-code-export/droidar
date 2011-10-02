package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;
import worldData.UpdateTimer;
import android.util.Log;

public class AnimationGrow implements Animation {

	private static final String LOG_TAG = "Grow Animation";
	private float myGrothSize;
	final private float myGrothFactor;
	private UpdateTimer myStopTimer;

	public AnimationGrow(float timeTillFullGrothInSeconds) {
		myStopTimer = new UpdateTimer(timeTillFullGrothInSeconds, null);
		myGrothFactor = 1 / timeTillFullGrothInSeconds;
		Log.d(LOG_TAG, "My grow factor is " + myGrothFactor);
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glScalef(myGrothSize, myGrothSize, myGrothSize);
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		if (myStopTimer.update(timeDelta, mesh, null)) {
			return false;
		}
		myGrothSize += myGrothFactor * timeDelta;
		if (myGrothSize > 1) {
			myGrothSize = 1;
			Log.e(LOG_TAG,
					"Grouth was > 1, should not happen when grothFactor correct");
		}
		return true;
	}

	@Override
	public Animation copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
