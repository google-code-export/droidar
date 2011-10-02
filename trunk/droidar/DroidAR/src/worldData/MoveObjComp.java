package worldData;

import gl.HasPosition;
import gl.MeshComponent;
import system.ParentStack;
import util.Vec;

import components.Entity;

/**
 * This class can be used to move an object which has a meshcompnent in a smooth
 * way to the specified position
 * 
 * @author Spobo
 * 
 */
public class MoveObjComp implements Entity {

	/**
	 * this vector is the new position, where to send the {@link MeshComponent}
	 * of the parent obj to
	 */
	public Vec myTargetPos = new Vec();
	private float mySpeedFactor;

	/**
	 * @param speedFactor
	 *            try values from 1 to 10. bigger means faster and 20 looks
	 *            nearly like instant placing so values should be < 20!
	 */
	public MoveObjComp(float speedFactor) {
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
		Vec objPos = null;

		// TODO remove these 2 lines later:
		if (parent instanceof HasPosition)
			objPos = ((Obj) parent).getPosition();

		if (objPos == null)
			objPos = stack.getFirst(HasPosition.class).getPosition();

		if (objPos != null) {
			Vec.morphToNewVec(objPos, myTargetPos, timeDelta * mySpeedFactor);

		}
		return true;
	}
}
