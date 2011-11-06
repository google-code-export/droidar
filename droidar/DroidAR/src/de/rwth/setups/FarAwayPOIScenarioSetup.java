package de.rwth.setups;

import commands.Command;
import commands.ui.CommandShowToast;

import android.app.Activity;
import geo.GeoObj;
import gl.GLFactory;
import gl.GLRenderer;
import gl.scenegraph.MeshComponent;
import gui.GuiSetup;
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

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				// float[] rayPos = new float[4];
				// float[] rayDir = new float[4];
				CommandShowToast.show(getActivity(), "altitude="
						+ getCamera().getGPSPositionVec());

				return true;
			}
		}, "Show altitude");
	}
}
