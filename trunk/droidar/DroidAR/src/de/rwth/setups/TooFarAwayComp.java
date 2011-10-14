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

public abstract class TooFarAwayComp implements Entity {

	private float maxDistance;
	private GLCamera myCamera;
	private UpdateTimer timer;

	public TooFarAwayComp(float distance, GLCamera camera) {
		maxDistance = distance;
		myCamera = camera;
		timer = new UpdateTimer(1, null);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (parent instanceof HasPosition
				&& timer.update(timeDelta, parent, stack)) {

			Vec direction = ((HasPosition) parent).getPosition().copy()
					.sub(myCamera.getPosition());
			if (direction.getLength() > maxDistance) {
				showFarAwayMesh(parent, direction);
			} else {
				hideFarAwayMesh(parent);
			}
		}
		return true;
	}

	private void hideFarAwayMesh(Updateable parent) {
		if (parent instanceof Obj)
			hideIn((Obj) parent);
		if (parent instanceof MeshComponent)
			hideIn((MeshComponent) parent);
	}

	private void hideIn(Obj parent) {
		hideIn(parent.getMeshComp());
	}

	public abstract void hideIn(MeshComponent parent);

	private void showFarAwayMesh(Updateable parent, Vec direction) {
		if (parent instanceof Obj)
			addTo((Obj) parent, direction);
		if (parent instanceof MeshComponent)
			addTo((MeshComponent) parent, direction);
	}

	public abstract void addTo(MeshComponent parent, Vec direction);

	private void addTo(Obj parent, Vec direction) {
		addTo(parent.getMeshComp(), direction);
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}
