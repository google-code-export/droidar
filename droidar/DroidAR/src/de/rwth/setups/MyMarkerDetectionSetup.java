package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;

import java.util.ArrayList;

import system.ErrorHandler;
import system.EventManager;
import system.markerDetection.MarkerDetectionSetup;
import system.markerDetection.MyMarker1Class;
import util.Vec;
import worldData.World;
import worldData.SystemUpdater;
import android.app.Activity;

import commands.gl.CommandCameraMoveAndLookAt;

public class MyMarkerDetectionSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;

	@Override
	public void _a_initFieldsIfNecessary() {
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");
		camera = new GLCamera(new Vec(0, 0, 1));
		world = new World(camera);

	}

	@Override
	public ArrayList _a2_initMarkers() {
		ArrayList a = new ArrayList();
		a.add(new MyMarker1Class(camera));
		return a;
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		world.add(objectFactory.newHexGroupTest(new Vec(0, 0, -0.1f)));
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new CommandCameraMoveAndLookAt(camera,
				new Vec(5, 5, 3), new Vec()), "Test Cam 1");
		guiSetup.addButtonToBottomView(new CommandCameraMoveAndLookAt(camera,
				new Vec(-5, 5, 3), new Vec()), "Test Cam 2");
	}

}
