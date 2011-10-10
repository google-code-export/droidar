package de.rwth.setups;

import gl.GLCamera;
import gl.HasPosition;
import gl.scenegraph.MeshComponent;
import system.ParentStack;
import util.Vec;
import worldData.Entity;
import worldData.Obj;
import worldData.UpdateTimer;
import worldData.Updateable;
import worldData.Visitor;

public class TooFarAwayComp implements Entity {

	private MeshComponent myReplaceMesh;
	private float maxDistance;
	private GLCamera myCamera;
	private UpdateTimer timer;

	public TooFarAwayComp(float distance, MeshComponent replaceMesh,
			GLCamera camera) {
		maxDistance = distance;
		myReplaceMesh = replaceMesh;
		myCamera = camera;
		timer = new UpdateTimer(1, null);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (parent instanceof HasPosition
				&& timer.update(timeDelta, parent, stack)) {
			if (Vec.distance(myCamera.getPosition(),
					((HasPosition) parent).getPosition()) > maxDistance) {
				showFarAwayMesh(parent);
			}
		}
		return true;
	}

	private void showFarAwayMesh(Updateable parent) {
		if (parent instanceof Obj)
			addTo((Obj) parent);

	}

	private void addTo(Obj parent) {
		if (!parent.getMeshComp().contains(myReplaceMesh))
			parent.getMeshComp().addChild(myReplaceMesh);
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}
