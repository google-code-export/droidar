package actions;

import gl.GLCamera;
import util.Vec;
import util.Wrapper;
import worldData.MoveObjComp;
import worldData.Obj;

public class ActionPlaceObject extends ActionUseCameraAngles {

	private GLCamera myCamera;
	private Wrapper myObjWrapper;
	private float maxDistance;
	private float myAzimuth;
	private MoveObjComp myMoveObjComp;
	private Obj compareObj;

	/**
	 * @param targetCamera
	 * @param objToPlace
	 *            the Wrapper should contain a {@link Obj}
	 * @param maxDistance
	 *            maximum distance in meters how far away from the camera the
	 *            elements can be places
	 */
	public ActionPlaceObject(GLCamera targetCamera, Wrapper objToPlace,
			float maxDistance) {
		super(targetCamera);
		myCamera = targetCamera;
		myObjWrapper = objToPlace;
		this.maxDistance = maxDistance;
		myMoveObjComp = new MoveObjComp(4);
	}

	@Override
	public void updateCompassAzimuth(float azimuth) {
		myAzimuth = azimuth;
	}

	@Override
	public void updatePitch(float pitch) {
		// not needed for movement, maybe for rotation?
	}

	@Override
	public void updateRoll(float rollAngle) {
		/*
		 * if the element in the wrapper changes, flip the component to the new
		 * element if its an Obj:
		 */
		final Object o = myObjWrapper.getObject();
		if (compareObj != o) {
			System.out.println(o.getClass().toString());
			if (o instanceof Obj) {
				if (compareObj != null)
					compareObj.remove(myMoveObjComp);
				((Obj) o).setComp(myMoveObjComp);
				System.out.println("myMoveCom was set");
				compareObj = (Obj) o;
				calcPosOnFloor(rollAngle);
			}
		} else {
			calcPosOnFloor(rollAngle);
		}
	}

	private void calcPosOnFloor(float rollAngle) {
		final Vec camPos = myCamera.getPosition();
		if (camPos != null) {
			Vec newPos = myMoveObjComp.myTargetPos;
			/*
			 * the following formula calculates the opposite side of the
			 * right-angled triangle where the adjacent side is the height of
			 * the camera and the alpha angle the roll angle of the device
			 * 
			 * this way the distance can be calculated by the angle
			 */
			float distance = (float) (Math.tan(Math.toRadians(rollAngle)) * camPos.z);
			if (distance > maxDistance)
				distance = maxDistance;
			newPos.x = 0;
			newPos.y = distance;
			newPos.z = 0;
			if (myAzimuth != 0) {
				// now calc the real position according to the cam rotation:
				newPos.rotateAroundZAxis(360 - myAzimuth);
			}

			// dont forget to mention that the camera doesnt have to be a
			// the zero point:
			newPos.x += camPos.x;
			newPos.y += camPos.y;
		}
	}
}
