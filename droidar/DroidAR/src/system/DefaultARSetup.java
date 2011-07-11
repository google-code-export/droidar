package system;

import java.util.Set;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.R;
import android.app.Activity;

import commands.Command;

/**
 * This is an example how you can use the default setup: <br>
 * <code>
 * ArActivity.startWithSetup(currentActicity, new DefaultARSetup() { <br>
 * 	 public void addObjectsTo(World world, GLFactory factory) { <br>
 * 		GeoObj obj = new GeoObj();<br>
 * 		obj.setComp(factory.newCube()); world.add(obj); <br>
 * 		obj.setVirtualPosition(new Vec()); <br>
 * 	 } <br> 
 * });
 * <code>
 * 
 * @author Spobo
 * 
 */
public abstract class DefaultARSetup extends Setup {

	protected static final int ZDELTA = 5;
	private GLCamera camera;
	private World world;

	public DefaultARSetup() {
		camera = new GLCamera();
		world = new World(camera);
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

/**
	 * This will be called by {@link Setup#_b_addWorldsToRenderer(GLRenderer, GLFactory, GeoObj)
	 * @param renderer
	 * @param world
	 * @param objectFactory
	 */
	public abstract void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory);

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		addObjectsTo(renderer, world, GLFactory.getInstance());
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
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
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
	}

}
