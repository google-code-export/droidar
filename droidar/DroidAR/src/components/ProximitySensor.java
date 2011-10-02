package components;

import android.util.Log;
import gl.GLCamera;
import gl.MeshComponent;
import system.ParentStack;
import util.Vec;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Updateable;
import worldData.Visitor;

import commands.Command;

public abstract class ProximitySensor implements Component {

	private static final float DEFAULT_UPDATE_TIME = 1;
	private static final String LOG_TAG = "ProximitySensor";
	private GLCamera myCamera;
	private float myDistance;
	private UpdateTimer myTimer;

	public ProximitySensor(GLCamera camera, float distance) {
		myCamera = camera;
		myDistance = distance;
		myTimer = new UpdateTimer(DEFAULT_UPDATE_TIME, null);
	}

	public void setMyCamera(GLCamera myCamera) {
		this.myCamera = myCamera;
	}

	public void setMyDistance(float myDistance) {
		this.myDistance = myDistance;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		if (myTimer.update(timeDelta, this, stack)) {
			if (parent instanceof Obj) {
				Obj obj = (Obj) parent;
				MeshComponent m = obj.getGraphicsComponent();
				if (m != null) {
					float currentDistance = Vec.distance(m.myPosition,
							myCamera.getPosition());
					if (0 <= currentDistance && currentDistance < myDistance) {
						onObjectIsCloseToCamera(myCamera, obj, m,
								currentDistance);
					}
				}
			} else {
				Log.w(LOG_TAG,
						"Sensor is not child of a Obj and therefor cant run!");
			}
		}
		return true;
	}

	/**
	 * @param glCamera
	 *            the camera (which should be the users position)
	 * @param obj
	 *            the obj where the {@link ProximitySensor} is contained in
	 * @param meshComp
	 *            the {@link MeshComponent} of the obj
	 * @param currentDistance
	 *            the distance of the camera to the obj
	 */
	public abstract void onObjectIsCloseToCamera(GLCamera glCamera, Obj obj,
			MeshComponent meshComp, float currentDistance);

}
