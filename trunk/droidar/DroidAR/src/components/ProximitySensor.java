package components;

import gl.GLCamera;
import gl.MeshComponent;
import util.Vec;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Visitor;

import commands.Command;

public class ProximitySensor implements Component {

	private static final float DEFAULT_UPDATE_TIME = 1;
	private GLCamera myCamera;
	private float myDistance;
	private Command myCommand;
	private UpdateTimer myTimer;

	public ProximitySensor(GLCamera camera, float distance,
			Command commandToExecuteWhenProximityReached) {
		myCamera = camera;
		myDistance = distance;
		myCommand = commandToExecuteWhenProximityReached;
		myTimer = new UpdateTimer(DEFAULT_UPDATE_TIME, null);
	}

	public void setMyCommand(Command myCommand) {
		this.myCommand = myCommand;
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
						myCamera.getMyPosition());
				if (0 <= currentDistance && currentDistance < myDistance) {
					myCommand.execute(currentDistance);
				}
			}
		}
	}

}
