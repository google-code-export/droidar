package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Obj;

public class AnimationPulse implements Animation {

	private final float speed;
	private final Vec myLowerEnd;
	private final Vec myUperEnd;
	private Vec currentScale;
	private Vec targetScale;
	private float accuracy;
	private boolean mode; // true=morph to uperEnd false=morph to lowerEnd

	/**
	 * @param speed
	 *            1 to 10
	 * @param lowerEnd
	 * @param uperEnd
	 * @param accuracy
	 *            should be 0.2f (or something between 0.01f and 0.5f)
	 */
	public AnimationPulse(float speed, Vec lowerEnd, Vec uperEnd, float accuracy) {
		this.speed = speed;
		this.accuracy = accuracy;
		this.myLowerEnd = lowerEnd.copy();
		this.myUperEnd = uperEnd.copy();
		this.currentScale = myLowerEnd.copy();
		this.targetScale = myUperEnd.copy();
		this.mode = true;
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		Vec.morphToNewVec(currentScale, targetScale, timeDelta * speed);
		final Vec distance = Vec.sub(currentScale, targetScale);
		if ((Vec.abs(distance.x) < accuracy)
				&& (Vec.abs(distance.y) < accuracy)
				&& (Vec.abs(distance.z) < accuracy)) {
			if (mode) {
				mode = false;
				targetScale = myUperEnd;
			} else {
				mode = true;
				targetScale = myLowerEnd;
			}
		}
		return true;
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glScalef(currentScale.x, currentScale.y, currentScale.z);
	}

	@Override
	public Animation copy() {
		return new AnimationPulse(speed, myLowerEnd, myUperEnd, accuracy);
	}

}
