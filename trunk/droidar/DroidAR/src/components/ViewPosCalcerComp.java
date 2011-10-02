package components;

import android.opengl.GLSurfaceView.Renderer;
import gl.GLCamera;
import gl.GLRenderer;
import util.Vec;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Visitor;

public class ViewPosCalcerComp implements Component {

	private GLCamera myCamera;
	private int myMaxDistance;
	private UpdateTimer timer;

	public ViewPosCalcerComp(GLCamera camera, int maxDistance, float updateSpeed) {
		myCamera = camera;
		myMaxDistance = maxDistance;
		timer = new UpdateTimer(updateSpeed, null);
	}

	@Override
	public void update(float timeDelta, Obj obj) {
		if (timer.update(timeDelta)) {
			MoveObjComp m = obj.getComp(MoveObjComp.class);
			if (m != null) {
				Vec targetVec = myCamera
						.getPositionOnGroundWhereTheCameraIsLookingAt();
				targetVec.sub(myCamera.getPosition());
				if (targetVec.getLength() > myMaxDistance) {
					targetVec.setLength(myMaxDistance);
				}
				m.myTargetPos = targetVec;
			}
		}
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}
