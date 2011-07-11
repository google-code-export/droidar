package de.rwth.setups;

import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.LightSource;
import gl.MeshComponent;
import gl.MeshGroup;
import gl.Shape;
import gl.animations.AnimationRotate;
import gui.GuiSetup;

import javax.microedition.khronos.opengles.GL10;

import system.DefaultARSetup;
import system.EventManager;
import util.EfficientList;
import util.Vec;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.World;
import actions.ActionMoveObject;
import android.app.Activity;

import commands.Command;
import components.Component;

public class LightningSetup extends DefaultARSetup {

	private static float zMoveFactor = 1f;

	private Obj lightObject;
	private GLCamera myCamera;

	private LightSource spotLight;

	@Override
	public boolean _a2_initLightning(EfficientList<LightSource> lights) {
		// lights.add(LightSource.newDefaultAmbientLight(GL10.GL_LIGHT0));
		spotLight = LightSource.newDefaultDefuseLight(GL10.GL_LIGHT1, new Vec(
				0, 0, 0));
		lights.add(spotLight);
		return true;
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {
		Obj o4 = new Obj();

		MeshComponent mesh = objectFactory.newCube();
		// mesh = newCube();

		mesh.addAnimation(new AnimationRotate(10, new Vec(0, 0.80f, 0.9f)));

		o4.setComp(mesh);
		o4.setComp(new MoveObjComp(1));
		world.add(o4);

		myCamera = world.getMyCamera();

		world.add(o4);

		lightObject = new Obj();

		spotLight.myPosition = new Vec(1, 1, 1);
		spotLight.add(objectFactory.newCircle(null));
		// lightMesh.addAnimation(new AnimationRotate(30, new Vec(1, 1, 1)));

		lightObject.setComp(spotLight);
		lightObject.setComp(new MoveObjComp(1));
		world.add(lightObject);

	}

	private Component newCube() {
		Shape s = new Shape();
		s.add(new Vec());
		s.add(new Vec(2, 2, 0));
		s.add(new Vec(2, 4, 0));

		s.add(new Vec());
		s.add(new Vec(2, 4, 0));
		s.add(new Vec(2, 10, 0));

		return s;
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		super._c_addActionsToEvents(eventManager, arView);

		// clear some inputs set in default methods
		eventManager.onLocationChangedAction = null;
		eventManager.onTrackballEventAction = null;

		eventManager.addOnTrackballAction(new ActionMoveObject(lightObject,
				myCamera, 10, 100));
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
	}

}
