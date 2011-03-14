package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.EfficientList;
import worldData.Obj;
import android.util.Log;

public class AnimationGroup implements Animation {

	private static final String LOG_TAG = "Animation Group";
	EfficientList<Animation> myAnimations = new EfficientList<Animation>();

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		for (int i = 0; i < myAnimations.myLength; i++) {
			myAnimations.get(i).setAnimationMatrix(gl, mesh);
		}
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		for (int i = 0; i < myAnimations.myLength; i++) {
			if (!myAnimations.get(i).update(timeDelta, obj, mesh)) {
				Log.d(LOG_TAG, "Animation " + myAnimations.get(i)
						+ " will now be removed from Anim.-group because it "
						+ "is finished (returned false on update())");
				myAnimations.remove(myAnimations.get(i));
			}
		}
		if (myAnimations.myLength == 0)
			return false;
		return true;
	}

	public void add(Animation animation) {
		myAnimations.add(animation);
	}

	public void remove(Animation animation) {
		myAnimations.remove(animation);
	}

	@Override
	public Animation copy() {
		AnimationGroup result = new AnimationGroup();
		for (int i = 0; i < myAnimations.myLength; i++) {
			result.add(myAnimations.get(i).copy());
		}
		return result;
	}

}
