package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Obj;

/**
 * This animation simulated the movement of a metronome. For more details see
 * the constructor
 * 
 * @author Spobo
 * 
 */
public class AnimationSwingRotate implements Animation {

	private final float speed;
	private final Vec dEnd;
	private final Vec uEnd;
	private Vec currentPos;
	private Vec targetPos;
	private float accuracy;
	private int mode; // 1=morph to uperEnd 0=morph to lowerEnd

	/**
	 * this works as an metronome, it pendels from lowerEnd vector to upperEnd
	 * vetor. could be combined with {@link AnimationBounce}
	 * 
	 * @param speed
	 *            20-40
	 * @param lowerEnd
	 *            new Vec(135, 0, 0)
	 * 
	 * @param upperEnd
	 *            new Vec(225, 0, 0)
	 * @param accuracy
	 *            should be 0.2f (or something between 0.01f and 0.5f)
	 */
	public AnimationSwingRotate(float speed, Vec lowerEnd, Vec upperEnd,
			float accuracy) {
		this.speed = speed;
		this.accuracy = accuracy;
		this.dEnd = lowerEnd.copy();
		this.uEnd = upperEnd.copy();
		this.currentPos = dEnd.copy();
		this.targetPos = uEnd.copy();
		this.mode = 1;
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		Vec.morphToNewVec(currentPos, targetPos, timeDelta * speed);
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
		gl.glRotatef(currentPos.z, 0, 0, 1);
		gl.glRotatef(currentPos.x, 1, 0, 0);
		gl.glRotatef(currentPos.y, 0, 1, 0);
	}

	@Override
	public Animation copy() {
		return new AnimationSwingRotate(speed, dEnd, uEnd, accuracy);
	}

}
