package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import gui.InfoScreenSettings;
import system.DefaultARSetup;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import worldData.AbstractObj;
import worldData.LargeWorld;
import worldData.RenderableEntity;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionWASDMovement;
import android.app.Activity;

public class LargeWorldsSetup extends DefaultARSetup {

	private static final int NUMBER_OF_OBJECTS = 1000;

	@Override
	public void _a_initFieldsIfNecessary() {
		super._a_initFieldsIfNecessary();
		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in LargeWorldsSetup");
	}

	@Override
	public void _f_addInfoScreen(InfoScreenSettings infoScreenData) {
		infoScreenData
				.addText("This setup will demonstrate a possible culling-strategy for very large virtual worlds.");
		infoScreenData
				.addText((NUMBER_OF_OBJECTS)
						+ " textured individual objects will be added to the virtual world.");
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {
		RenderQuadList list = new RenderQuadList(getCamera(), 100, 10);
		for (int x = (int) Math.sqrt(NUMBER_OF_OBJECTS); x >= 0; x--) {
			for (int y = (int) Math.sqrt(NUMBER_OF_OBJECTS); y >= 0; y--) {
				list.add(newObj(x * 5, y * 5));
			}
		}
		// when the quad-list is created, add it to the world:
		world.add(list);
	}

	private AbstractObj newObj(int x, int y) {
		String name = "x=" + x + ",y=" + y;
		return GLFactory.getInstance().newTextObject(name, new Vec(x, y, 0),
				getActivity(), camera);

	}
}
