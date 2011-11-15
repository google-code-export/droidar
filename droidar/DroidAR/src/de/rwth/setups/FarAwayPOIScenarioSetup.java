package de.rwth.setups;

import geo.GeoObj;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import system.DefaultARSetup;
import util.Vec;
import worldData.World;
import android.app.Activity;
import android.util.Log;

import commands.Command;
import commands.ui.CommandShowToast;
import components.SimpleTooFarAwayComp;

public class FarAwayPOIScenarioSetup extends DefaultARSetup {

	private String LOG_TAG = "FarAwayPOIScenarioSetup";

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			final GLFactory objectFactory) {
		GeoObj o = new GeoObj();
		o.setComp(objectFactory.newCube());
		o.setVirtualPosition(new Vec(20, 50, 0));
		o.setComp(new SimpleTooFarAwayComp(30, getCamera(), getActivity()));
		world.add(o);
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
						+ getCamera().getGPSPositionVec().z);

				return true;
			}
		}, "Show altitude");

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				Vec pos = getCamera().getGPSPositionVec();
				Log.d(LOG_TAG, "Placing object at " + pos);

				final GeoObj o = new GeoObj(pos.y, pos.x, pos.z);
				o.setComp(GLFactory.getInstance().newArrow());
				o.setComp(new SimpleTooFarAwayComp(30, getCamera(),
						getActivity()));
				o.setOnClickCommand(new Command() {

					@Override
					public boolean execute() {
						CommandShowToast.show(getActivity(), "o.getAltitude()="
								+ o.getAltitude());
						return true;
					}
				});
				Log.d(LOG_TAG, "virtual pos=" + o.getVirtualPosition());
				Log.d(LOG_TAG, "cam pos=" + getCamera().getPosition());
				getWorld().add(o);
				return true;
			}
		}, "Place GeoObj");
	}
}
