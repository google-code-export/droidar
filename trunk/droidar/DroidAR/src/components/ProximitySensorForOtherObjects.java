package components;

import gl.MeshComponent;
import util.EfficientList;
import util.Vec;
import worldData.AbstractObj;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Visitor;
import worldData.World;

import commands.Command;

public class ProximitySensorForOtherObjects implements Component {
	private static final float DEFAULT_UPDATE_TIME = 1;
	private World wmyWorld;
	private float myDistance;
	private Command myCommand;
	private UpdateTimer myTimer;

	public ProximitySensorForOtherObjects(World world, float distance,
			Command commandToExecuteWhenProximityReached) {
		wmyWorld = world;
		myDistance = distance;
		myCommand = commandToExecuteWhenProximityReached;
		myTimer = new UpdateTimer(DEFAULT_UPDATE_TIME, null);
	}

	@Override
	public void update(float timeDelta, Obj obj) {
		if (myTimer.update(timeDelta)) {
			MeshComponent myMesh = obj.getGraphicsComponent();
			if (myMesh != null) {
				EfficientList<AbstractObj> list = wmyWorld.getAllItems();
				for (int i = 0; i < list.myLength; i++) {
					if (list.get(i) != obj && list.get(i) instanceof Obj) {
						MeshComponent objMesh = ((Obj) list.get(i))
								.getGraphicsComponent();
						if (objMesh != null) {
							float currentDistance = Vec.distance(
									myMesh.myPosition, objMesh.myPosition);
							if (0 <= currentDistance
									&& currentDistance < myDistance) {
								myCommand.execute(list.get(i));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}
