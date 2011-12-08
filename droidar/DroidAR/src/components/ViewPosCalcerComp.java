package components;

import gl.GLCamera;
import system.ParentStack;
import util.Vec;
import worldData.Entity;
import worldData.UpdateTimer;
import worldData.Updateable;
import worldData.Visitor;

public abstract class ViewPosCalcerComp implements Entity {

	private static final String LOG_TAG = "ViewPosCalcerComp";
	private GLCamera myCamera;
	private int myMaxDistance;
	private UpdateTimer timer;

	/**
	 * @param camera
	 * @param maxDistance
	 *            suggestion: around 20 to 100
	 * @param updateSpeed
	 *            e.g. every 0.1f seconds
	 */
	public ViewPosCalcerComp(GLCamera camera, int maxDistance, float updateSpeed) {
		myCamera = camera;
		myMaxDistance = maxDistance;
		timer = new UpdateTimer(updateSpeed, null);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		if (timer.update(timeDelta, this, stack)) {

			Vec targetVec = myCamera
					.getPositionOnGroundWhereTheCameraIsLookingAt();

			if (targetVec.getLength() > myMaxDistance) {
				targetVec.setLength(myMaxDistance);
			}

			onPositionUpdate(parent, targetVec);
		}
		return true;
	}

	/**
	 * This will be called in constant time intervals 
	 * 
	 * @param parent
	 * @param targetVec
	 */
	public abstract void onPositionUpdate(Updateable parent, Vec targetVec);

	@Override
	public boolean accept(Visitor visitor) {
		return true;
	}

}
