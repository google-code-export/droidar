package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import gui.InfoScreenSettings;
import system.EventManager;
import system.Setup;
import worldData.SystemUpdater;
import android.app.Activity;

public class MySetup extends Setup {

	@Override
	public void _a_initFieldsIfNecessary() {
		// ...
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		// renderer.addRenderElement(word);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		// ...
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void _f_addInfoScreen(InfoScreenSettings infoScreenData) {
		// TODO Auto-generated method stub
		super._f_addInfoScreen(infoScreenData);
	}

}
