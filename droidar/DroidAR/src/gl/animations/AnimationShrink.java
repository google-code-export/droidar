package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;
import android.util.Log;

public class AnimationShrink implements Animation {

	private static final String LOG_TAG = "Grow Animation";
	private float myGrothSize = 1;
	final private float myShrinkFactor;

	public AnimationShrink(float timeTillFullGrothInSeconds) {
		myShrinkFactor = 1 / timeTillFullGrothInSeconds;
		Log.d(LOG_TAG, "My shrink factor is " + myShrinkFactor);
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glScalef(myGrothSize, myGrothSize, myGrothSize);
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		if (myGrothSize > 0) {
			myGrothSize -= myShrinkFactor * timeDelta;
		} else {
			myGrothSize = 0;
		}
		return true;
	}

	@Override
	public Animation copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
