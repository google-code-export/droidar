package de.rwth;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import system.EventManager;
import markerDetection.MarkerDetectionSetup;
import markerDetection.MarkerObjectMap;
import markerDetection.UnrecognizedMarkerListener;
import util.Vec;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionBufferedCameraAR;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;

public class ExampleMarkerRenderSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;

	@Override
	public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		markerObjectMap.put(new CameraMarker(0, camera));
	}

	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera();
		world = new World(camera);
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
		world.add(objectFactory.newHexGroupTest(new Vec()));
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.onTouchMoveAction = new ActionBufferedCameraAR(camera);
		// ActionMoveCameraBuffered(camera, 5, 25);
		// eventManager
		// .addOnOrientationChangedAction(new ActionRotateCameraBuffered(
		//				camera));
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				1, 25));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		// TODO Auto-generated method stub

	}

}
