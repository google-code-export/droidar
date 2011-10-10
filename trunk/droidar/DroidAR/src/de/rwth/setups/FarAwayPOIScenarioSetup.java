package de.rwth.setups;

import geo.GeoObj;
import gl.CustomGLSurfaceView;
import gl.GLFactory;
import gl.GLRenderer;
import gui.GuiSetup;
import android.app.Activity;
import system.DefaultARSetup;
import system.EventManager;
import system.Setup;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;

public class FarAwayPOIScenarioSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {
		Obj o = new Obj();
		o.setComp(objectFactory.newArrow());
		o.setComp(new TooFarAwayComp(50, objectFactory.newDiamond(null),
				getCamera()));

	}

}
