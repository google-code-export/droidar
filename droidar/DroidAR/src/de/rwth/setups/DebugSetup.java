package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MeshComponent;
import gl.Shape;
import gl.animations.AnimationBounce;
import gl.animations.AnimationColorBounce;
import gl.animations.AnimationFaceToCamera;
import gl.animations.AnimationPulse;
import gl.animations.AnimationRotate;
import gl.animations.AnimationSwingRotate;
import gui.GuiSetup;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.IO;
import util.Vec;
import util.Wrapper;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionBufferedCameraAR;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import commands.CommandGroup;
import commands.geo.DebugCommandPositionEvent;
import commands.gl.CommandAddAnimation;
import commands.gl.CommandCameraMoveAndLookAt;
import commands.logic.CommandSetWrapperToValue2;
import commands.system.CameraSetARInputCommand;
import commands.system.CommandPlaySound;
import commands.ui.CommandShowToast;

import de.rwth.R;

/**
 * This setup demonstrates the possible animations, camera movement, textured
 * meshes, trackball movement,...
 * 
 * @author Spobo
 * 
 */
public class DebugSetup extends Setup {

	private static final String STATE_MAP_MAXIMIZED = "mimimap maximized";
	private static final String STATE_MAP_MINIMIZED = "mimimap minimized";
	private static final String MAPS_KEY = "";

	// World radar;
	World world;
	GLCamera camera;

	private Wrapper selection;

	private SystemUpdater theWorldUpdater;
	private Wrapper condMapState;
	private ActionCalcRelativePos geoupdater;
	private GLRenderer renderer;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

		// init the selection wrapper:
		selection = new Wrapper();
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		camera = new GLCamera(new Vec(0, 0, 1));
		world = new World(camera);
		initWorld(world);
		initI9Tests(world);
		renderer.addRenderElement(world);

		// the renderer is needed by a ui element so store it:
		this.renderer = renderer;

	}

	private void initI9Tests(World w) {

		{
			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"elefantId",
							IO.loadBitmapFromId(myTargetActivity,
									R.drawable.elephant64));
			triangleMesh.myScale = new Vec(10, 10, 10);
			triangleMesh.addAnimation(new AnimationFaceToCamera(camera, 0.5f));
			GeoObj treangleGeo = new GeoObj(GeoObj.iPark1, triangleMesh);
			w.add(treangleGeo);
		}

		{

			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"hippoId",
							IO.loadBitmapFromId(myTargetActivity,
									R.drawable.hippopotamus64));
			triangleMesh.addAnimation(new AnimationFaceToCamera(camera, 0.5f));
			triangleMesh.myScale = new Vec(10, 10, 10);
			GeoObj treangleGeo = new GeoObj(GeoObj.iPark2, triangleMesh);
			w.add(treangleGeo);

			CommandSetWrapperToValue2 commandSelectObj = new CommandSetWrapperToValue2(
					selection);
			triangleMesh.setOnClickCommand(commandSelectObj);

		}

		{
			MeshComponent triangleMesh = GLFactory.getInstance()
					.newTexturedSquare(
							"pandaId",
							IO.loadBitmapFromId(myTargetActivity,
									R.drawable.panda64));
			triangleMesh.addAnimation(new AnimationFaceToCamera(camera, 0.5f));
			triangleMesh.myScale = new Vec(10, 10, 10);
			GeoObj treangleGeo = new GeoObj(GeoObj.iPark3, triangleMesh);
			w.add(treangleGeo);
		}

		{
			// transform android ui elements into opengl models:

			TextView tv = new TextView(myTargetActivity);
			tv.setTextColor(Color.white().toIntARGB());
			tv.setTextSize(20);
			tv.setText("! Hallo !");

			Button b = new Button(myTargetActivity);
			b.setText("Click Me");
			MeshComponent button = GLFactory.getInstance().newTexturedSquare(
					"buttonId", IO.loadBitmapFromView(b));
			button.setOnClickCommand(new CommandShowToast(myTargetActivity,
					"Thanks alot"));

			button.addAnimation(new AnimationFaceToCamera(camera, 0.5f));
			button.myScale = new Vec(10, 10, 10);
			button.myColor = Color.red();

			GeoObj treangleGeo = new GeoObj(GeoObj.iPark4, button);

			w.add(treangleGeo);
		}

	}

	private synchronized void initWorld(World world) {

		world.add(GLFactory.getInstance().newSolarSystem(new Vec(0, 0, 5)));
		world.add(GLFactory.getInstance().newHexGroupTest(new Vec(0, 0, -0.1f)));

		CommandSetWrapperToValue2 commandSelectObj = new CommandSetWrapperToValue2(
				selection);

		MeshComponent c = GLFactory.getInstance().newCube(null);
		c.myPosition = new Vec(-3, 3, 0);
		c.myScale = new Vec(0.5f, 0.5f, 0.5f);
		c.setOnClickCommand(new CommandPlaySound("/sdcard/train.mp3"));
		Obj geoC = new Obj();
		geoC.setComp(c);
		world.add(geoC);

		MeshComponent c2 = GLFactory.getInstance().newCube(null);
		c2.myPosition = new Vec(3, 3, 0);
		c2.setOnClickCommand(commandSelectObj);
		// GeoObj geoC = new GeoObj(GeoObj.normaluhr, c);
		Obj geoC2 = new Obj();
		geoC2.setComp(c2);
		world.add(geoC2);

		Obj hex = new Obj();
		Shape hexMesh = GLFactory.getInstance().newHexagon(
				new Color(0, 0, 1, 0.7f));
		hexMesh.myPosition.add(new Vec(0, 0, -1));
		hexMesh.scaleEqual(4.5f);
		hex.setComp(hexMesh);

		hexMesh.setOnClickCommand(commandSelectObj);
		world.add(hex);

		Obj grid = new Obj();
		MeshComponent gridMesh = GLFactory.getInstance().newGrid(Color.blue(),
				1, 10);
		grid.setComp(gridMesh);
		world.add(grid);

		Obj treangle = new Obj();
		MeshComponent treangleMesh = GLFactory.getInstance().newTexturedSquare(
				"worldIconId",
				IO.loadBitmapFromId(myTargetActivity, R.drawable.icon));
		treangleMesh.myPosition = new Vec(0, -2, 1);
		treangleMesh.myRotation = new Vec(0, 0, 0);
		treangleMesh.addAnimation(new AnimationFaceToCamera(camera, 0.5f));
		treangle.setComp(treangleMesh);
		world.add(treangle);

		// Obj x = new Obj();
		// Shape s = F.f().newSquare(new Color(1, 0, 0, 0.8f));
		// x.add(s);
		// F.f().scaleEqual(s, .5f);
		// s.myPosition = new Vec(.5f, .5f, -.5f);
		// world.add(x);
		// F.f().addRotateAnimation(s,120, new Vec(0, 0, 1));

		// Obj y = new Obj();
		// MeshComponent mesh = F.f().newTexturedSquare(y, null,
		// loadBitmapFromFile(R.drawable.skate12));
		// // MeshComponent mesh = F.f().newTextured2dShape(y, null,
		// // loadBitmapFromFile(R.drawable.skate12));
		// y.myMeshComonent = mesh;
		// mesh.myPosition = new Vec(0, 0, 0);
		// mesh.myRotation = new Vec(-70, 0, 0);
		// world.add(y);

		// Obj z = new Obj();
		// MeshComponent mesh2 = F.f().newColoredPyramid(z, null);
		// z.myMeshComonent = mesh2;
		// mesh2.myPosition = new Vec(0, 0, 5);
		// mesh2.myRotation = new Vec(-90, 0, 0);
		// world.add(z);
		//
		// Obj z2 = new Obj();
		// MeshComponent mesh22 = F.f().newColoredPyramid(z2, null);
		// z2.myMeshComonent = mesh22;
		// mesh22.myPosition = new Vec(0, 0, 10);
		// mesh22.myRotation = new Vec(-70, 0, 0);
		// world.add(z2);

		// SpawnScript spawner = new SpawnScript(y);
		// y.myScriptComonent = new ScriptComponent(y);
		// y.myScriptComonent.add(spawner);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {

		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));

		geoupdater = new ActionCalcRelativePos(world, camera);
		eventManager.addOnLocationChangedAction(geoupdater);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		theWorldUpdater = worldUpdater;

		// add the created world to be updated:
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {

		guiSetup.setBottomBackroundColor(new Color(0.5f, 0.5f, 0.5f, 0.4f)
				.toIntARGB());
		guiSetup.setBottomMinimumHeight(100);
		guiSetup.setBottomViewCentered();

		// addMapView(activity, guiSetup);

		AnimationRotate rotateAnimation = new AnimationRotate(10, new Vec(0, 0,
				1));
		guiSetup.addButtonToBottomView(new CommandAddAnimation(selection,
				rotateAnimation), "Add RotateAnim");
		guiSetup.addButtonToBottomView(new CommandAddAnimation(selection,
				new AnimationColorBounce(2, Color.blue(), Color.green(), 0.2f)),
				"Add ColorAnim");
		AnimationPulse pAnimation = new AnimationPulse(2, new Vec(1, 1, 1),
				new Vec(2, 2, 2), 0.2f);
		guiSetup.addButtonToBottomView(new CommandAddAnimation(selection,
				pAnimation), "Add PulseAnim");

		AnimationBounce bAnimation = new AnimationBounce(12, new Vec(),
				new Vec(0, 0, 4), 0.01f);
		guiSetup.addButtonToBottomView(new CommandAddAnimation(selection,
				bAnimation), "Add Bounce");

		AnimationSwingRotate sAnimation = new AnimationSwingRotate(20, new Vec(
				135, 0, 0), new Vec(225, 0, 0), 0.02f);
		guiSetup.addButtonToBottomView(new CommandAddAnimation(selection,
				sAnimation), "Add Swing");

		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(
				geoupdater, GeoObj.rwthI9), "Goto i9");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(
				geoupdater, GeoObj.iPark1), "Goto p1");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(
				geoupdater, GeoObj.iPark2), "Goto p2");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(
				geoupdater, GeoObj.iPark3), "Goto p3");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(
				geoupdater, GeoObj.iPark4), "Goto p4");

		guiSetup.addButtonToBottomView(
				new CameraSetARInputCommand(camera, true), "AR ON");
		guiSetup.addButtonToBottomView(new CameraSetARInputCommand(camera,
				false), "AR OFF");

		// now add the different camera buttons:
		addCameraButtons(guiSetup);

	}

	private void addCameraButtons(GuiSetup guiSetup) {
		float th = 3;
		float dist = 3;
		float ah = 2;

		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, true));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(0, 0, 1)));
			guiSetup.addButtonToBottomView(g, "AR Cam");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, false));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(dist, dist,
					dist + ah), new Vec(0, 0, th)));
			guiSetup.addButtonToBottomView(g, "Cam 2");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, false));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(-dist, dist,
					dist + ah), new Vec(0, 0, th)));
			guiSetup.addButtonToBottomView(g, "Cam 3");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, false));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(-dist, -dist,
					dist + ah), new Vec(0, 0, th)));
			guiSetup.addButtonToBottomView(g, "Cam 4");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, false));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(dist, -dist,
					dist + ah), new Vec(0, 0, th)));
			guiSetup.addButtonToBottomView(g, "Cam 5");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, true));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(3, 3, 1.5f)));
			guiSetup.addButtonToBottomView(g, "Cube Cam");
		}
		{
			CommandGroup g = new CommandGroup();
			g.add(new CameraSetARInputCommand(camera, true));
			g.add(new CommandCameraMoveAndLookAt(camera, new Vec(0, 0, 20)));
			guiSetup.addButtonToBottomView(g, "God Cam");
		}
	}

	// private void addMapView(Context context, GuiSetup guiSetup) {
	// GMap map = guiSetup.addMapToBottomRight(MAPS_KEY, 3, 100);
	// map.setClickable(true);
	// map.setZoomLevel(20);
	// // map.enableZoomButtons(true);
	// // EventManager.getInstance().addOnLocationChangedAction(new
	// // ActionUpdateMapCenter(map));
	// map.setCenterTo(GeoObj.rwthI9);
	//
	// condMapState = new Wrapper(STATE_MAP_MINIMIZED);
	// CommandGroup maximizeMinimap = new CommandGroup();
	// maximizeMinimap
	// .add(new CommandAnimateZoom(guiSetup.getMainContainerView(),
	// new CommandMapEnlargeToFullScreen(map)));
	// maximizeMinimap.add(new CommandShowCameraPreview(this, false));
	// maximizeMinimap.add(new CommandShowWorldAnimation(theWorldUpdater,
	// false));
	// maximizeMinimap.add(new CommandShowRendering(renderer, false));
	// maximizeMinimap.add(new CommandSetWrapperToValue(condMapState,
	// STATE_MAP_MAXIMIZED));
	//
	// map.setOnTabCommand(new CommandWrapperEqualsCondition(condMapState,
	// STATE_MAP_MINIMIZED, maximizeMinimap));
	//
	// GeoGraph l = new GeoGraph();
	// l.setIsPath(true);
	// l.add(GeoObj.iPark1);
	// l.add(GeoObj.iPark2);
	// l.add(GeoObj.iPark3);
	// l.add(GeoObj.iPark4);
	// try {
	// CustomItemizedOverlay o = new CustomItemizedOverlay(l,
	// myTargetActivity.getResources()
	// .getDrawable(R.drawable.icon));
	// CustomPathOverlay o2 = new CustomPathOverlay(l, false);
	// map.addOverlay(o2);
	// map.addOverlay(o);
	// } catch (Exception e) {
	// Log.e("Gmaps", "An itemized overlay could be created but "
	// + "not added to the Google-Maps view. A reason might "
	// + "be that the mapview could not determine its "
	// + "position. Check if the phone is in airplane-mode!");
	// e.printStackTrace();
	// }
	//
	// CommandGroup minimizeMap = new CommandGroup();
	// minimizeMap.add(new CommandMinimizeMap(map, 2, 130));
	// minimizeMap.add(new CommandShowCameraPreview(this, true));
	// minimizeMap.add(new CommandShowWorldAnimation(theWorldUpdater, true));
	// minimizeMap.add(new CommandShowRendering(renderer, true));
	// minimizeMap.add(new CommandSetWrapperToValue(condMapState,
	// STATE_MAP_MINIMIZED));
	//
	// // if the minimap is maximised and the user presses back then it will be
	// // minimized instead of exiting the app:
	// EventManager.getInstance().addOnKeyPressedCommand(
	// KeyEvent.KEYCODE_BACK,
	// new CommandWrapperEqualsCondition(condMapState,
	// STATE_MAP_MAXIMIZED, minimizeMap));
	//
	// }

}
