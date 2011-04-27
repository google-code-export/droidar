package worldData;

import gl.MeshComponent;
import util.Vec;

import components.Component;

/**
 * This class can be used to move an object which has a meshcompnent in a smooth
 * way to ne specified position
 * 
 * @author Spobo
 * 
 */
public class MoveObjComp implements Component {

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
	public void update(float timeDelta, Obj obj) {
		MeshComponent g = obj.getGraphicsComponent();
		if (g != null) {
			Vec.morphToNewVec(g.myPosition, myTargetPos, timeDelta
					* mySpeedFactor);

		}

	}
}
