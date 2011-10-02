package components;

import gl.GLCamera;
import gl.MeshComponent;
import util.Vec;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Visitor;

import commands.Command;

public abstract class ProximitySensor implements Component {

	private static final float DEFAULT_UPDATE_TIME = 1;
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
	public void update(float timeDelta, Obj obj) {
		if (myTimer.update(timeDelta)) {
			MeshComponent m = obj.getGraphicsComponent();
			if (m != null) {
				float currentDistance = Vec.distance(m.myPosition,
						myCamera.getPosition());
				if (0 <= currentDistance && currentDistance < myDistance) {
					onObjectIsCloseToCamera(myCamera, obj, m, currentDistance);
				}
			}
		}
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
