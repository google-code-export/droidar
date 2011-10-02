package components;

import android.util.Log;
import gl.MeshComponent;
import system.ParentStack;
import util.EfficientList;
import util.QuadTree;
import util.Vec;
import worldData.AbstractObj;
import worldData.LargeWorld;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Updateable;
import worldData.Visitor;
import worldData.World;
import worldData.RenderableEntity;

import commands.Command;

public class ProximitySensorForOtherObjects implements Entity {
	private static final float DEFAULT_UPDATE_TIME = 1;
	private static final String LOG_TAG = "ProximitySensorForOtherObjects";
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
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		if (myTimer.update(timeDelta, this, stack)) {
			if (parent instanceof Obj) {
				Obj obj = (Obj) parent;
				MeshComponent myMesh = obj.getGraphicsComponent();
				if (myMesh != null) {
					if (myWorld instanceof LargeWorld)
						findObjectsCloseTo(obj, myMesh, (LargeWorld) myWorld);
					else
						findObjectsCloseTo(obj, myMesh, myWorld.getAllItems());
				}
			} else {
				Log.w(LOG_TAG,
						"Sensor is not child of a Obj and therefor cant run!");
			}
		}
		return true;
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
			EfficientList<RenderableEntity> list) {
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
