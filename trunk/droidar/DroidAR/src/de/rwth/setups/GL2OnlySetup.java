package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLFactory;
import gl.GL1Renderer;
import gl.GLRenderer;
import gl2.GL2SurfaceView;
import gl2.GL2Renderer;
import gui.GuiSetup;
import android.app.Activity;
import android.util.Log;
import system.EventManager;
import system.Setup;
import worldData.SystemUpdater;

public class GL2OnlySetup extends Setup {

	private static final String LOG_TAG = "GL2OnlySetup";

	@Override
	public CustomGLSurfaceView initOpenGLView(GLRenderer renderer) {
		GL2SurfaceView v = new GL2SurfaceView(getActivity());
		v.setRenderer(renderer);
		return v;
	}

	@Override
	public GLRenderer initOpenGLRenderer() {
		return new GL2Renderer();
	}

	@Override
	public void _a_initFieldsIfNecessary() {
		// TODO Auto-generated method stub

	}

	@Override
	public void _b_addWorldsToRenderer(GL1Renderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		// TODO Auto-generated method stub

	}

}
