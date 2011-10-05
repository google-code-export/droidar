package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.scenegraph.MeshComponent;
import gui.GuiSetup;
import gui.simpleUI.EditItem;
import gui.simpleUI.ModifierGroup;
import gui.simpleUI.modifiers.InfoText;
import gui.simpleUI.modifiers.TextModifier;
import listeners.ObjectCreateListener;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import util.Wrapper;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionBufferedCameraAR;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionPlaceObject;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;
import android.view.Gravity;

import commands.Command;
import commands.obj.CommandCreateObjectInWrapper;
import commands.ui.CommandShowInfoScreen;

public class PlaceObjectsSetup extends Setup {

	private GLCamera camera;
	private World world;

	private Wrapper placeObjectWrapper;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

		placeObjectWrapper = new Wrapper();

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		camera = new GLCamera(new Vec(0, 0, 10));
		world = new World(camera);

		Obj placerContainer = new Obj();
		placerContainer.setComp(objectFactory.newArrow());
		world.add(placerContainer);

		placeObjectWrapper.setTo(placerContainer);

		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));
		eventManager.addOnOrientationChangedAction(new ActionPlaceObject(
				camera, placeObjectWrapper, 50));
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
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {

		guiSetup.addButtonToTopView(new CommandCreateObjectInWrapper(
				placeObjectWrapper, new ObjectCreateListener() {

					@Override
					public boolean setWrapperToObject(Wrapper targetWrapper) {
						final Obj placerContainer = new Obj();
						Color c = Color.getRandomRGBColor();
						c.alpha = 0.7f;
						MeshComponent arrow = GLFactory.getInstance()
								.newDiamond(c);
						arrow.setOnClickCommand(new Command() {
							@Override
							public boolean execute() {
								placeObjectWrapper.setTo(placerContainer);
								return true;
							}
						});
						placerContainer.setComp(arrow);
						world.add(placerContainer);
						targetWrapper.setTo(placerContainer);
						return false;
					}
				}), "Place next!");

		guiSetup.setTopViewCentered();
	}

}
