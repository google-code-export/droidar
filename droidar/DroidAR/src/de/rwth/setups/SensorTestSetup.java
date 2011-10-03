package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MeshComponent;
import gl.RenderGroup;
import gl.animations.AnimationRotate;
import gui.GuiSetup;
import listeners.EventListener;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionBufferedCameraAR;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionRotateCameraBuffered3;
import actions.ActionRotateCameraBuffered4;
import actions.ActionRotateCameraBufferedDebug;
import actions.ActionRotateCameraDirectlyBuffered;
import actions.ActionRotateCameraUnbuffered;
import actions.ActionRotateCameraUnbuffered2;
import android.app.Activity;

import commands.Command;

public class SensorTestSetup extends Setup {

	private GLCamera camera;
	private World world;
	private EventListener rotAction1;
	private EventListener rotAction2;
	private EventListener rotAction3;
	private EventListener rotAction4;
	private EventListener rotAction5;
	private EventListener rotAction6;
	private EventListener rotAction7;

	@Override
	public void _a_initFieldsIfNecessary() {
		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

		/*
		 * the following are just example rotate actions, take a look at the
		 * implementation to see how to create own CameraBuffered actions
		 */

		camera = new GLCamera();
		rotAction1 = new ActionRotateCameraBuffered(camera);
		rotAction2 = new ActionRotateCameraBuffered3(camera);
		rotAction3 = new ActionRotateCameraBuffered4(camera);
		rotAction4 = new ActionRotateCameraBufferedDebug(camera);
		rotAction5 = new ActionRotateCameraDirectlyBuffered(camera);
		rotAction6 = new ActionRotateCameraUnbuffered(camera);
		rotAction7 = new ActionRotateCameraUnbuffered2(camera);

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		world = new World(camera);

		RenderGroup compasrose = new RenderGroup();

		MeshComponent middle = objectFactory.newDiamond(Color.green());
		middle.myPosition = new Vec(0, 0, -2.8f);
		middle.addAnim(new AnimationRotate(40, new Vec(0, 0, 1)));
		compasrose.add(middle);

		int smallDistance = 10;
		int longDistance = 60;

		MeshComponent north = objectFactory.newDiamond(Color.redTransparent());
		north.myPosition = new Vec(0, smallDistance, 0);

		MeshComponent north2 = objectFactory.newDiamond(Color.red());
		north2.myPosition = new Vec(0, longDistance, 0);

		MeshComponent east = objectFactory.newDiamond(Color.blueTransparent());
		east.myPosition = new Vec(smallDistance, 0, 0);

		MeshComponent east2 = objectFactory.newDiamond(Color.blue());
		east2.myPosition = new Vec(longDistance, 0, 0);

		MeshComponent south = objectFactory.newDiamond(Color.blueTransparent());
		south.myPosition = new Vec(0, -smallDistance, 0);

		MeshComponent south2 = objectFactory.newDiamond(Color.blue());
		south2.myPosition = new Vec(0, -longDistance, 0);

		MeshComponent west = objectFactory.newDiamond(Color.blueTransparent());
		west.myPosition = new Vec(-smallDistance, 0, 0);

		MeshComponent west2 = objectFactory.newDiamond(Color.blue());
		west2.myPosition = new Vec(-longDistance, 0, 0);

		compasrose.add(north2);
		compasrose.add(north);
		compasrose.add(east2);
		compasrose.add(east);
		compasrose.add(south2);
		compasrose.add(south);
		compasrose.add(west2);
		compasrose.add(west);

		currentPosition.setComp(compasrose);
		world.add(currentPosition);

		renderer.addRenderElement(world);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		eventManager.addOnOrientationChangedAction(rotAction1);
		camera.setUpdateListener(rotAction1);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new myRotateAction(rotAction1),
				"Camera Buffered 1");
		guiSetup.addButtonToBottomView(new myRotateAction(rotAction2),
				"Camera Buffered 2");
		guiSetup.addButtonToBottomView(new myRotateAction(rotAction3),
				"Camera Buffered 3");
		guiSetup.addButtonToBottomView(new myRotateAction(rotAction6),
				"Camera Unbuffered 1");
		guiSetup.addButtonToBottomView(new myRotateAction(rotAction7),
				"Camera Unbuffered 2");
	}

	class myRotateAction extends Command {

		private EventListener myAction;

		public myRotateAction(EventListener a) {
			myAction = a;
		}

		@Override
		public boolean execute() {
			EventManager.getInstance().onOrientationChangedAction = myAction;
			// to use always the correct update method replace the old update
			// listeners for the camera:
			camera.setUpdateListener(myAction);
			return true;
		}

	}

}
