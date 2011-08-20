package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Obj;

public class AnimationMove implements Animation {

	private final float MIN_DISTANCE = 0.01f;
	private float timeToMove;
	private Vec relativeTargetPos;
	private Vec pos;
	private boolean done;

	public AnimationMove(float timeToMove, Vec relativeTargetPos) {
		this.timeToMove = timeToMove;
		this.relativeTargetPos = relativeTargetPos;
		pos = new Vec();
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glTranslatef(pos.x, pos.y, pos.z);
	}

	@Override
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		if (!done) {
			Vec.morphToNewVec(pos, relativeTargetPos, timeDelta / timeToMove);
			if (Vec.distance(pos, relativeTargetPos) < MIN_DISTANCE) {
				done = true;
			}
		}
		return true;
	}

	@Override
	public Animation copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
