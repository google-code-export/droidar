package de.rwth;

import commands.Command;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MeshComponent;
import gl.GLCamera.CameraAngleUpdateListener;
import gui.GuiSetup;
import system.EventManager;
import markerDetection.MarkerDetectionSetup;
import markerDetection.MarkerObjectMap;
import markerDetection.UnrecognizedMarkerListener;
import util.Vec;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionUseCameraAngles;
import android.app.Activity;

public class MultiMarkerSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;
	private MeshComponent mesh;
	private float myAzi;
	private float myRoll;
	private float myPitch;

	@Override
	public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {

		markerObjectMap.put(new VirtualObjectMarker(mesh, camera));
	}

	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera(new Vec(0, 0, 0));
		world = new World(camera);
		mesh = GLFactory.getInstance().newCircle(null);

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
		Obj o = new Obj();
		o.setComp(mesh);
		world.add(o);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.onTouchMoveAction = new ActionMoveCameraBuffered(camera, 5, 25);
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				1, 25));

		eventManager.addOnOrientationChangedAction(new ActionUseCameraAngles(
				camera) {

			@Override
			public void updateRoll(float rollAngle) {
				myRoll = rollAngle;
			}

			@Override
			public void updatePitch(float pitchAngle) {
				myPitch = pitchAngle;
			}

			@Override
			public void updateCompassAzimuth(float azimuth) {
				myAzi = azimuth;
			}
		});

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {

				Vec rayPosition = new Vec();
				Vec rayDirection = new Vec();
				camera.getPickingRay(rayPosition, rayDirection,
						GLRenderer.halfWidth, GLRenderer.halfHeight);

				System.out.println("rayPosition=" + rayPosition);
				System.out.println("rayDirection=" + rayDirection);

				rayDirection.setLength(5);

				mesh.myPosition = rayPosition.add(rayDirection);

				return false;
			}
		}, "Place 2 meters infront");

	}
}
