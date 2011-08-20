package gl.animations;

import gl.Color;
import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import util.Vec;
import worldData.Obj;

public class AnimationColorMorph implements Animation {

	private static final float MIN_DISTANCE = 0.001f;
	private float myDurationInMS;
	private Color myTargetColor;

	public AnimationColorMorph(float durationInMS, Color targetColor) {
		myDurationInMS = durationInMS;
		myTargetColor = targetColor;
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		Vec colorDistance = Color.morphToNewColor(mesh.myColor, myTargetColor,
				timeDelta / myDurationInMS);
		if (!(colorDistance.getLength() > MIN_DISTANCE)) {
			Log.d("NodeListener", "color morph finnished for " + mesh);
		}
		return (colorDistance.getLength() > MIN_DISTANCE);
	}

	@Override
	public Animation copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
