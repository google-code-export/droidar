package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Obj;

public class AnimationBounce implements Animation {

	private final int mySpeed;
	private final Vec dEnd;
	private final Vec uEnd;
	private Vec currentPos;
	private Vec targetPos;
	private float accuracy;
	private int mode; // 1=morph to uperEnd 0=morph to lowerEnd

	/**
	 * @param speed
	 * @param relativeLowerEnd
	 * @param relativeUperEnd
	 * @param accuracy
	 *            should be 0.2f (or something between 0.01f and 0.5f)
	 */
	public AnimationBounce(int speed, Vec relativeLowerEnd,
			Vec relativeUperEnd, float accuracy) {
		this.mySpeed = speed;
		this.accuracy = accuracy;
		this.dEnd = relativeLowerEnd.copy();
		this.uEnd = relativeUperEnd.copy();
		this.currentPos = dEnd.copy();
		this.targetPos = uEnd.copy();
		this.mode = 1;
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {

		// TODO this does not bounce nice, better use sort of gravity version

		Vec.morphToNewVec(currentPos, targetPos, timeDelta * mySpeed);
		final Vec distance = Vec.sub(currentPos, targetPos);
		if ((Vec.abs(distance.x) < accuracy)
				&& (Vec.abs(distance.y) < accuracy)
				&& (Vec.abs(distance.z) < accuracy)) {
			if (mode == 0) {
				mode = 1;
				targetPos = uEnd;
			} else {
				mode = 0;
				targetPos = dEnd;
			}
		}
		return true;
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glTranslatef(currentPos.x, currentPos.y, currentPos.z);
	}

	@Override
	public Animation copy() {
		return new AnimationBounce(mySpeed, dEnd, uEnd, accuracy);
	}

}
