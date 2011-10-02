package de.rwth.setups;

import gl.Color;
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
import util.Wrapper;
import worldData.Entity;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.World;
import actions.ActionMoveObject;
import android.app.Activity;

import commands.Command;

public class LightningSetup extends DefaultARSetup {

	private static float zMoveFactor = 1f;

	private Wrapper targetMoveWrapper;
	private GLCamera myCamera;

	private LightSource spotLight;

	@Override
	public boolean _a2_initLightning(EfficientList<LightSource> lights) {
		lights.add(LightSource.newDefaultAmbientLight(GL10.GL_LIGHT0));
		spotLight = LightSource.newDefaultDefuseLight(GL10.GL_LIGHT1, new Vec(
				0, 0, 0));
		lights.add(spotLight);
		return true;
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

		addNewObjToWorld(world, objectFactory);

		myCamera = world.getMyCamera();
		final Obj lightObject = new Obj();

		MeshGroup innerGroup = new MeshGroup();
		innerGroup.add(spotLight);
		innerGroup.add(objectFactory.newCircle(Color.red()));
		innerGroup.myPosition = new Vec(0, 3, 0);

		MeshGroup outerGroup = new MeshGroup();
		outerGroup.add(innerGroup);
		outerGroup.add(objectFactory.newCircle(Color.blue()));
		outerGroup.addAnimation(new AnimationRotate(30, new Vec(0, 0, 1)));

		spotLight.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(lightObject);
				return true;
			}
		});

		lightObject.setComp(outerGroup);
		lightObject.setComp(new MoveObjComp(1));
		world.add(lightObject);

		targetMoveWrapper = new Wrapper(lightObject);

	}

	private void addNewObjToWorld(World world, GLFactory objectFactory) {
		final Obj o = new Obj();

		MeshComponent mesh = objectFactory.newCube();
		// mesh = newCube();
		mesh = objectFactory.newDiamond(Color.red());
		mesh.myScale = new Vec(2, 3, 1);
		mesh.addAnimation(new AnimationRotate(30, new Vec(0, 0, -1)));

		o.setComp(mesh);
		o.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(o);
				return true;
			}
		});
		o.setComp(new MoveObjComp(1));
		world.add(o);

		world.add(o);
	}

	private Entity newCube() {
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

		eventManager.addOnTrackballAction(new ActionMoveObject(
				targetMoveWrapper, myCamera, 10, 100));
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					((Obj) targetMoveWrapper.getObject())
							.getComp(MoveObjComp.class).myTargetPos.z -= zMoveFactor;
					return true;
				}
				return false;
			}
		}, "Obj Down");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					((Obj) targetMoveWrapper.getObject())
							.getComp(MoveObjComp.class).myTargetPos.z += zMoveFactor;
					return true;
				}
				return false;
			}
		}, "Obj up");
	}

}
