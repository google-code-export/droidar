package gl.animations;

import gl.Renderable;
import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.Vec;
import worldData.Updateable;
import worldData.Visitor;

public class AnimationMove implements GLAnimation {

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
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		gl.glTranslatef(pos.x, pos.y, pos.z);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (!done) {
			Vec.morphToNewVec(pos, relativeTargetPos, timeDelta / timeToMove);
			if (Vec.distance(pos, relativeTargetPos) < MIN_DISTANCE) {
				done = true;
			}
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}
}
