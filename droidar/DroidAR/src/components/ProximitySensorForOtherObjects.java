package components;

import gl.MeshComponent;
import util.EfficientList;
import util.QuadTree;
import util.Vec;
import worldData.AbstractObj;
import worldData.LargeWorld;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Visitor;
import worldData.World;

import commands.Command;

public class ProximitySensorForOtherObjects implements Component {
	private static final float DEFAULT_UPDATE_TIME = 1;
	private World myWorld;
	private float myMaxDistance;
	private Command myCommand;
	private UpdateTimer myTimer;

	public ProximitySensorForOtherObjects(World world, float distance,
			Command commandToExecuteWhenProximityReached) {
		myWorld = world;
		myMaxDistance = distance;
		myCommand = commandToExecuteWhenProximityReached;
		myTimer = new UpdateTimer(DEFAULT_UPDATE_TIME, null);
	}

	@Override
	public void update(float timeDelta, Obj obj) {
		if (myTimer.update(timeDelta)) {
			MeshComponent myMesh = obj.getGraphicsComponent();
			if (myMesh != null) {
				if (myWorld instanceof LargeWorld)
					findObjectsCloseTo(obj, myMesh, (LargeWorld) myWorld);
				else
					findObjectsCloseTo(obj, myMesh, myWorld.getAllItems());
			}
		}
	}

	/**
	 * A {@link LargeWorld} uses a {@link QuadTree} and it has a getObjects
	 * close to x functionality build in.
	 * 
	 * @param obj
	 * @param myMesh
	 * @param largeWorld
	 */
	private void findObjectsCloseTo(Obj obj, MeshComponent myMesh,
			LargeWorld largeWorld) {
		EfficientList<Obj> list = largeWorld.getItems(myMesh.myPosition,
				myMaxDistance);
		for (int i = 0; i < list.myLength; i++) {
			myCommand.execute(list.get(i));
		}
	}

	private void findObjectsCloseTo(Obj obj, MeshComponent myMesh,
			EfficientList<AbstractObj> list) {
		if (list != null) {
			for (int i = 0; i < list.myLength; i++) {
				if (list.get(i) != obj && list.get(i) instanceof Obj) {
					MeshComponent objMesh = ((Obj) list.get(i))
							.getGraphicsComponent();
					if (objMesh != null) {
						float currentDistance = Vec.distance(myMesh.myPosition,
								objMesh.myPosition);
						if (0 <= currentDistance
								&& currentDistance < myMaxDistance) {
							myCommand.execute(list.get(i));
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
