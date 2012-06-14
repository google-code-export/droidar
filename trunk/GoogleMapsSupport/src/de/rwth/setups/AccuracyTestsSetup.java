package de.rwth.setups;

import com.google.android.maps.MapActivity;
import commands.Command;

import geo.GMap;
import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GL1Renderer;
import gl.GLCamera;
import gl.GLFactory;
import gui.GuiSetup;
import android.app.Activity;
import android.view.View;
import system.ConcreteSimpleLocationManager;
import system.DefaultARSetup;
import system.EventManager;
import system.Setup;
import system.SimpleLocationManager;
import worldData.SystemUpdater;
import worldData.World;

public class AccuracyTestsSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {

		super._e2_addElementsToGuiSetup(guiSetup, activity);

		final GMap map = GMap.newDefaultGMap((MapActivity) getActivity(),
				GoogleMapsDebugKeys.pc1DebugKey);
		guiSetup.addViewToBottomRight(map, 0.5f, 200);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (map.getVisibility() == View.VISIBLE)
					map.setVisibility(View.GONE);
				else
					map.setVisibility(View.VISIBLE);
				return true;
			}
		}, "Show/Hide \n map");

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				getWorld().add(
						GLFactory.getInstance().newPositionMarker(camera));
				return false;
			}
		}, "Place pin");

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				((ConcreteSimpleLocationManager) SimpleLocationManager
						.getInstance(getActivity())).getLastPositions();
				
				getWorld().add(
						GLFactory.getInstance().newPositionMarker(camera));
				return false;
			}
		}, "show measurements");

	}

}
