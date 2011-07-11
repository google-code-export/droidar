package de.rwth;

import javax.microedition.khronos.opengles.GL10;

import commands.Command;

import actions.ActionMoveObject;
import android.app.Activity;

import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.LightSource;
import gl.MeshComponent;
import gl.MeshGroup;
import gui.GuiSetup;
import system.DefaultARSetup;
import system.EventManager;
import util.EfficientList;
import util.Vec;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.World;

public class ModelLoaderSetup extends DefaultARSetup {

	private boolean lightsOnOff = false;
	protected static final float zMoveFactor = 1.4f;
	private String fileName;
	private String textureName;
	private LightSource spotLight;
	private GLCamera cam;
	private Obj lightObject;
	private GLRenderer renderer;

	public ModelLoaderSetup(String fileName, String textureName) {
		this.fileName = fileName;
		this.textureName = textureName;
	}

	@Override
	public boolean _a2_initLightning(EfficientList<LightSource> lights) {
		spotLight = LightSource.newDefaultDefuseLight(GL10.GL_LIGHT1, new Vec(
				0, 0, 0));
		lights.add(spotLight);
		return lightsOnOff;
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, final World world,
			GLFactory objectFactory) {
		this.renderer = renderer;
		cam = world.getMyCamera();
		lightObject = new Obj();
		spotLight.myPosition = new Vec(1, 1, 1);
		MeshComponent circle = objectFactory.newCircle(null);
		circle.myScale = new Vec(0.2f, 0.2f, 0.2f);
		spotLight.add(circle);
		lightObject.setComp(spotLight);
		lightObject.setComp(new MoveObjComp(1));
		world.add(lightObject);

		GDXConnection.init(myTargetActivity, renderer);

		new ModelLoader(renderer, fileName, textureName) {
			@Override
			public void modelLoaded(MeshComponent gdxMesh) {
				Obj o = new Obj();
				o.setComp(gdxMesh);
				world.add(o);
			}
		};

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		super._c_addActionsToEvents(eventManager, arView);

		// clear some inputs set in default methods
		eventManager.onLocationChangedAction = null;
		eventManager.onTrackballEventAction = null;

		eventManager.addOnTrackballAction(new ActionMoveObject(lightObject,
				cam, 10, 200));
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				lightObject.getComp(MoveObjComp.class).myTargetPos.z -= zMoveFactor;
				return false;
			}
		}, "Obj Down");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				lightObject.getComp(MoveObjComp.class).myTargetPos.z += zMoveFactor;
				return false;
			}
		}, "Obj up");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				lightsOnOff = !lightsOnOff;
				renderer.setUseLightning(lightsOnOff);
				return true;
			}
		}, "Light on/off");
	}
}
