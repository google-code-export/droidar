package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import system.EventManager;
import system.Setup;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.R;
import android.app.Activity;

import commands.Command;
import commands.geo.DebugCommandPositionEvent;

public class PositionTestsSetup extends Setup {

	protected static final int ZDELTA = 5;
	private GLCamera camera;
	private World world;
	private ActionCalcRelativePos action;
	private GeoObj posA;
	private GeoObj posB;
	private GeoObj posC;
	private GeoObj posD;

	public PositionTestsSetup() {
		camera = new GLCamera();
		world = new World(camera);
		action = new ActionCalcRelativePos(world, camera);
		posA = new GeoObj();
	}

	@Override
	public void _a_initFieldsIfNecessary() {

	}

	public World getWorld() {
		return world;
	}

	public GLCamera getCamera() {
		return camera;
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.onTouchMoveAction = new ActionMoveCameraBuffered(camera, 5, 25);
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(action);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.setRightViewAllignBottom();

		guiSetup.addImangeButtonToRightView(R.drawable.arrow_up_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(+ZDELTA);
						return false;
					}
				});
		guiSetup.addImangeButtonToRightView(R.drawable.arrow_down_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(-ZDELTA);
						return false;
					}
				});

		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(action,
				posA), "Go to pos A");

	}

}
