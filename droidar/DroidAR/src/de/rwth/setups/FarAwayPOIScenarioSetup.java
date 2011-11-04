package de.rwth.setups;

import gl.GLFactory;
import gl.GLRenderer;
import gl.scenegraph.MeshComponent;
import system.DefaultARSetup;
import util.Vec;
import worldData.Obj;
import worldData.World;

public class FarAwayPOIScenarioSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			final GLFactory objectFactory) {
		Obj o = new Obj();
		o.setComp(objectFactory.newArrow());
		o.setComp(new TooFarAwayComp(50, getCamera()) {
			MeshComponent arrow = objectFactory.newArrow();

			@Override
			public void hideIn(MeshComponent parent) {
				parent.remove(arrow);
			}

			@Override
			public void addTo(MeshComponent parent, Vec direction) {
				arrow.setPosition(direction.copy().setLength(5));
				parent.addChild(arrow);
			}
		});

	}
}
