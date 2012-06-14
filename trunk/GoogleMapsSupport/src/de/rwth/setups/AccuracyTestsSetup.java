package de.rwth.setups;

import com.google.android.maps.MapActivity;
import commands.Command;
import components.TimerComp;

import geo.CustomItemizedOverlay;
import geo.GMap;
import geo.GeoGraph;
import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GL1Renderer;
import gl.GLCamera;
import gl.GLFactory;
import gui.GuiSetup;
import android.R;
import android.app.Activity;
import android.location.Location;
import android.view.View;
import system.ConcreteSimpleLocationManager;
import system.DefaultARSetup;
import system.EventManager;
import system.Setup;
import system.SimpleLocationManager;
import util.IO;
import util.LimitedQueue;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.UpdateTimer;
import worldData.World;

public class AccuracyTestsSetup extends DefaultARSetup {

	private GeoGraph measureData = new GeoGraph(false);
	private GeoGraph pins = new GeoGraph(false);

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {
		world.add(pins);
		world.add(measureData);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {

		super._e2_addElementsToGuiSetup(guiSetup, activity);

		final GMap map = GMap.newDefaultGMap((MapActivity) getActivity(),
				GoogleMapsDebugKeys.pc1DebugKey);

		try {
			map.addOverlay(new CustomItemizedOverlay(measureData, IO
					.loadDrawableFromId(getActivity(),
							de.rwth.R.drawable.mapdotgreen)));
			map.addOverlay(new CustomItemizedOverlay(pins, IO
					.loadDrawableFromId(getActivity(),
							de.rwth.R.drawable.mapdotblue)));

		} catch (Exception e) {
			e.printStackTrace();
		}

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
				pins.add(GLFactory.getInstance().newPositionMarker(camera));
				return true;
			}
		}, "Place pin");

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				LimitedQueue<Location> list = ((ConcreteSimpleLocationManager) SimpleLocationManager
						.getInstance(getActivity())).getLastPositions();
				for (int i = 0; i < list.size(); i++) {
					Location l = list.get(i);
					final GeoObj marker = new GeoObj(l);
					marker.setComp(GLFactory.getInstance().newCircle(
							Color.greenTransparent()));
					addARemoveAfterSomeSecondsComp(measureData, i, marker);
					measureData.add(marker);
				}

				final GeoObj average = new GeoObj(SimpleLocationManager
						.getInstance(getActivity())
						.getCurrentBUfferedLocation());
				average.setComp(GLFactory.getInstance().newDiamond(
						Color.redTransparent()));
				addARemoveAfterSomeSecondsComp(measureData, 60, average);
				measureData.add(average);

				return true;
			}

			private void addARemoveAfterSomeSecondsComp(
					final GeoGraph container, int countdownTimeInSeconds,
					final GeoObj marker) {
				marker.setComp(new TimerComp(countdownTimeInSeconds,
						new Command() {

							@Override
							public boolean execute() {
								container.remove(marker);
								return true;
							}
						}));
			}
		}, "show measurements");

	}

}
