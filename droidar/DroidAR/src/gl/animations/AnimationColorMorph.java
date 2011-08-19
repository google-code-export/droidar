package gl.animations;

import gl.Color;
import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;

public class AnimationColorMorph implements Animation {

	private float myDurationInSec;
	private Color myTargetColor;

	public AnimationColorMorph(float durationInSec, Color targetColor) {
		myDurationInSec=durationInSec;
		myTargetColor=targetColor;
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Animation copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
