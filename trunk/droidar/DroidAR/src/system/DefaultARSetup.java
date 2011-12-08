package system;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import util.Log;
import util.Vec;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import actions.ActionWASDMovement;
import actions.EventListenerGroup;
import actions.ActionWaitForAccuracy;
import android.R;
import android.app.Activity;
import android.location.Location;
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
	private static final String LOG_TAG = "DefaultARSetup";

	private GLCamera camera;
	private World myWorld;
	private ActionWASDMovement wasdAction;
	private GLRenderer myRenderer;
	private boolean addObjCalledOneTieme;
	private ActionWaitForAccuracy minAccuracyAction;

	public DefaultARSetup() {
		camera = new GLCamera(new Vec(0, 0, 2));
		myWorld = new World(camera);
		wasdAction = new ActionWASDMovement(camera, 25, 50, 20);
	}

	@Override
	public void _a_initFieldsIfNecessary() {

	}

	public World getWorld() {
		return myWorld;
	}

	public GLCamera getCamera() {
		return camera;
	}

	/**
	 * This will be called when the GPS accuracy is high enough
	 * 
	 * @param renderer
	 * @param world
	 * @param objectFactory
	 */
	public abstract void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory);

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		myRenderer = renderer;
		renderer.addRenderElement(myWorld);
	}

	@Override
	public void _c_addActionsToEvents(final EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.onTouchMoveAction = wasdAction;
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				myWorld, camera));
		minAccuracyAction = new ActionWaitForAccuracy(myTargetActivity, 24.0f,
				10) {
			@Override
			public void minAccuracyReachedFirstTime(Location l,
					ActionWaitForAccuracy a) {
				callAddObjectsToWorldIfNotCalledAlready();
				if (eventManager.onLocationChangedAction instanceof EventListenerGroup)
					((EventListenerGroup) eventManager.onLocationChangedAction)
							.remove(a);
			}
		};
		eventManager.addOnLocationChangedAction(minAccuracyAction);
	}

	protected void callAddObjectsToWorldIfNotCalledAlready() {
		if (!addObjCalledOneTieme)
			addObjectsTo(myRenderer, myWorld, GLFactory.getInstance());
		else
			Log.w(LOG_TAG, "callAddObjectsToWorldIfNotCalledAlready() "
					+ "called more then one time!");
		addObjCalledOneTieme = true;
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(myWorld);
		updater.addObjectToUpdateCycle(wasdAction);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.setRightViewAllignBottom();

		guiSetup.addViewToTop(minAccuracyAction.getView());
		
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
