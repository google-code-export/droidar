package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MeshComponent;
import gl.MeshGroup;
import gui.GuiSetup;
import gui.InfoScreenSettings;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import worldData.AbstractObj;
import worldData.LargeWorld;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.Action;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionWASDMovement;
import android.app.Activity;

import commands.Command;
import commands.ui.CommandShowToast;
import components.ProximitySensor;

import de.rwth.R;

public class LargeWorldsSetup extends Setup {

	private static final int NUMBER_OF_OBJECTS = 1000;
	private GLCamera camera;
	private World world;
	private ActionWASDMovement wasdAction;

	@Override
	public void _a_initFieldsIfNecessary() {
		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in CollectItemsSetup");

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		camera = new GLCamera(new Vec(0, 0, 1));
		world = new LargeWorld(camera, 60f, 5f);// new World(camera);

		wasdAction = new ActionWASDMovement(camera, 25f, 50f, 20f);

		for (int x = (int) Math.sqrt(NUMBER_OF_OBJECTS); x >= 0; x--) {
			for (int y = (int) Math.sqrt(NUMBER_OF_OBJECTS); y >= 0; y--) {
				world.add(newObj(x * 5, y * 5));
			}
		}
		renderer.addRenderElement(world);
	}

	private AbstractObj newObj(int x, int y) {
		String name = "x=" + x + ",y=" + y;
		return GLFactory.getInstance().newTextObject(name, new Vec(x, y, 0),
				getActivity(), camera);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {

		arView.addOnTouchMoveAction(wasdAction);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {

		worldUpdater.addObjectToUpdateCycle(world);
		worldUpdater.addObjectToUpdateCycle(wasdAction);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {
	}

	@Override
	public void _f_addInfoScreen(InfoScreenSettings infoScreenData) {
		infoScreenData
				.addText("This setup will demonstrate a possible culling-strategy for very large virtual worlds.");
		infoScreenData
				.addText((NUMBER_OF_OBJECTS)
						+ " textured individual objects will be added to the virtual world.");
	}

}
