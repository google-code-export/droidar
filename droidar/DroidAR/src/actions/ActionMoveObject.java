package actions;

import gl.GLCamera;
import gl.MeshComponent;
import util.Vec;
import worldData.MoveObjComp;
import worldData.Obj;
import android.view.MotionEvent;

public class ActionMoveObject extends ActionDoAlongAxis {

	private Obj targetObj;

	/**
	 * @param o
	 * @param camera
	 * @param trackballFactor
	 *            should be around 2-15
	 * @param touchscreenFactor
	 *            25 would be good value to start.The higher the value the
	 *            slower the movement
	 */
	public ActionMoveObject(Obj o, GLCamera camera, float trackballFactor,
			float touchscreenFactor) {
		super(camera, trackballFactor, touchscreenFactor);
		targetObj = o;
	}

	@Override
	public void doAlongViewAxis(float x, float y) {
		MoveObjComp mc = targetObj.getComp(MoveObjComp.class);
		MeshComponent m = targetObj.getComp(MeshComponent.class);
		if (mc != null) {
			if (mc.myTargetPos == null && m != null && m.myPosition != null) {
				mc.myTargetPos = m.myPosition.copy();
			}
			mc.myTargetPos.add(x, y, 0.0f);
		} else if (m != null) {
			if (m.myPosition == null)
				m.myPosition = new Vec();
			m.myPosition.add(x, y, 0);
		}
	}
}
