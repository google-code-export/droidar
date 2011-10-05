package gl.animations;

import gl.Renderable;
import gl.scenegraph.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;

import worldData.Obj;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class AnimationShrink implements GLAnimation {

	private static final String LOG_TAG = "Grow Animation";
	private float myGrothSize = 1;
	final private float myShrinkFactor;

	public AnimationShrink(float timeTillFullGrothInSeconds) {
		myShrinkFactor = 1 / timeTillFullGrothInSeconds;
		Log.d(LOG_TAG, "My shrink factor is " + myShrinkFactor);
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		gl.glScalef(myGrothSize, myGrothSize, myGrothSize);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (myGrothSize > 0) {
			myGrothSize -= myShrinkFactor * timeDelta;
		} else {
			myGrothSize = 0;
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
