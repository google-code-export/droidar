package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GL1Renderer;
import gl.animations.AnimationBounce;
import gl.animations.AnimationColorBounce;
import gl.animations.AnimationFaceToCamera;
import gl.animations.AnimationPulse;
import gl.animations.AnimationRotate;
import gl.animations.AnimationSwingRotate;
import gl.animations.GLAnimation;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.RenderList;
import gl.scenegraph.Shape;
import gui.GuiSetup;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.IO;
import util.Log;
import util.Vec;
import util.Wrapper;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionWASDMovement;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import commands.Command;
import commands.CommandGroup;
import commands.DebugCommandPositionEvent;
import commands.gl.CommandCameraMoveAndLookAt;
import commands.logic.CommandSetWrapperToValue2;
import commands.system.CameraSetARInputCommand;
import commands.system.CommandPlaySound;
import commands.ui.CommandShowToast;

import de.rwth.R;

public class StaticDemoSetup extends Setup {

	private static final float MAX_DIST = 55f;

	protected static final String LOG_TAG = "StaticDemoSetup";

	World world;
	GLCamera camera;

	private TimeModifier timeModifier;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		camera = new GLCamera(new Vec(0, 0, 1));
		world = new World(camera);

		timeModifier = new TimeModifier(1);
		RenderList l = new RenderList();
		timeModifier.setChild(l);
		initWorld(l);
		world.add(timeModifier);

		initI9Tests(world);

		world.add(objectFactory.newTextObject("DroidAR", new Vec(10, 1, 1),
				getActivity(), camera));

		addTestGeoObj(world, camera);

		renderer.addRenderElement(world);

	}

	private void addTestGeoObj(World w, GLCamera c) {
		GeoObj o = new GeoObj();
		MeshComponent s = GLFactory.getInstance().newCube(Color.blue());
		MeshComponent s2 = GLFactory.getInstance().newCube(Color.red());
		s2.setPosition(new Vec(5, 0, 0));
		s2.setRotation(new Vec(0, 0, 45));
		s.addChild(s2);
		s.setRotation(new Vec(0, 0, -45));
		o.setComp(s);
		o.setVirtualPosition(new Vec(0, 20, 0));
		w.add(o);
	}

	private void initI9Tests(World w) {

		{
			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"elefantId",
							IO.loadBitmapFromId(getActivity(),
									R.drawable.elephant64));
			triangleMesh.setScale(new Vec(10, 10, 10));
			triangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
			GeoObj treangleGeo = new GeoObj(GeoObj.newRandomGeoObjAroundCamera(
					camera, MAX_DIST), triangleMesh);
			w.add(treangleGeo);
		}

		{

			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"hippoId",
							IO.loadBitmapFromId(myTargetActivity,
									R.drawable.hippopotamus64));
			triangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
			triangleMesh.setScale(new Vec(10, 10, 10));
			GeoObj treangleGeo = new GeoObj(GeoObj.newRandomGeoObjAroundCamera(
					camera, MAX_DIST), triangleMesh);
			w.add(treangleGeo);

		}

		{
			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"pandaId",
							IO.loadBitmapFromId(myTargetActivity,
									R.drawable.panda64));
			triangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
			triangleMesh.setScale(new Vec(10, 10, 10));
			GeoObj treangleGeo = new GeoObj(GeoObj.newRandomGeoObjAroundCamera(
					camera, MAX_DIST), triangleMesh);
			w.add(treangleGeo);
		}

		{
			// transform android ui elements into opengl models:
			Button b = new Button(myTargetActivity);
			b.setText("Click Me");
			MeshComponent button = GLFactory.getInstance().newTexturedSquare(
					"buttonId", IO.loadBitmapFromView(b));
			button.setOnClickCommand(new CommandShowToast(myTargetActivity,
					"Thanks alot"));

			button.addChild(new AnimationFaceToCamera(camera, 0.5f));
			button.setScale(new Vec(10, 10, 10));
			button.setColor(Color.red());

			GeoObj treangleGeo = new GeoObj(GeoObj.newRandomGeoObjAroundCamera(
					camera, MAX_DIST), button);

			w.add(treangleGeo);
		}

	}

	private synchronized void initWorld(RenderList l) {

		l.add(GLFactory.getInstance().newSolarSystem(new Vec(-7, -7, 5)));
		l.add(GLFactory.getInstance().newHexGroupTest(new Vec(0, 8, -0.1f)));

		Obj treangle = new Obj();
		MeshComponent treangleMesh = GLFactory.getInstance().newTexturedSquare(
				"worldIconId",
				IO.loadBitmapFromId(myTargetActivity, R.drawable.icon));
		treangleMesh.setPosition(new Vec(0, -8, 1));
		treangleMesh.setRotation(new Vec(0, 0, 0));
		treangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
		treangle.setComp(treangleMesh);
		l.add(treangle);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {

		ActionWASDMovement wasdAction = new ActionWASDMovement(camera, 25f,
				50f, 20f);
		ActionRotateCameraBuffered rotateAction = new ActionRotateCameraBuffered(
				camera);

		updater.addObjectToUpdateCycle(wasdAction);
		updater.addObjectToUpdateCycle(rotateAction);

		arView.addOnTouchMoveAction(wasdAction);
		eventManager.addOnOrientationChangedAction(rotateAction);

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		// add the created world to be updated:
		worldUpdater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {

		guiSetup.setBottomMinimumHeight(50);
		guiSetup.setBottomViewCentered();

		// addMapView(activity, guiSetup);

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				timeModifier.setTimeFactor(timeModifier.getTimeFactor() + 1);
				return false;
			}
		}, "T+1");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				timeModifier.setTimeFactor(timeModifier.getTimeFactor() - 1);
				return false;
			}
		}, "T-1");

	}

}
