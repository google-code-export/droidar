package components;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import gl.GLCamera;
import gl.GLRenderer;
import system.ParentStack;
import util.Vec;
import worldData.Entity;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Updateable;
import worldData.Visitor;

public class ViewPosCalcerComp implements Entity {

	private static final String LOG_TAG = "ViewPosCalcerComp";
	private GLCamera myCamera;
	private int myMaxDistance;
	private UpdateTimer timer;

	public ViewPosCalcerComp(GLCamera camera, int maxDistance, float updateSpeed) {
		myCamera = camera;
		myMaxDistance = maxDistance;
		timer = new UpdateTimer(updateSpeed, null);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		if (timer.update(timeDelta, this, stack)) {
			if (parent instanceof Obj) {
				Obj obj = (Obj) parent;
				MoveObjComp m = obj.getComp(MoveObjComp.class);
				if (m != null) {
					Vec targetVec = myCamera
							.getPositionOnGroundWhereTheCameraIsLookingAt();
					targetVec.sub(myCamera.getPosition());
					if (targetVec.getLength() > myMaxDistance) {
						targetVec.setLength(myMaxDistance);
					}
					m.myTargetPos = targetVec;
				} else {
					Log.w(LOG_TAG,
							"Sensor is not child of a Obj and therefor cant run!");
				}
			}
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}
