package worldData;

import javax.microedition.khronos.opengles.GL10;

import gl.HasPosition;
import gl.Renderable;
import gl.scenegraph.MeshComponent;
import system.ParentStack;
import util.Vec;

/**
 * This class can be used to move any {@link Entity} which implements the
 * {@link HasPosition} interface (like {@link Obj} or {@link MeshComponent})
 * 
 * @author Spobo
 * 
 */
public class MoveComp implements RenderableEntity {

	/**
	 * this vector is the new position, where to send the {@link MeshComponent}
	 * of the parent {@link HasPosition} to
	 */
	public Vec myTargetPos = new Vec();
	private float mySpeedFactor;

	/**
	 * @param speedFactor
	 *            try values from 1 to 10. bigger means faster and 20 looks
	 *            nearly like instant placing so values should be < 20!
	 */
	public MoveComp(float speedFactor) {
		this.mySpeedFactor = speedFactor;
	}

	@Override
	public boolean accept(Visitor visitor) {
		// doesn't need visitor processing..
		return false;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		Vec pos = null;

		// TODO remove these 2 lines later:
		if (parent instanceof HasPosition)
			pos = ((HasPosition) parent).getPosition();

		if (pos == null && stack != null)
			pos = stack.getFirst(HasPosition.class).getPosition();

		if (pos != null) {
			Vec.morphToNewVec(pos, myTargetPos, timeDelta * mySpeedFactor);

		}
		return true;
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {

	}
}
