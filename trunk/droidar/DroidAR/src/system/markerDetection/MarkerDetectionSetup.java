package system.markerDetection;

import gui.GuiSetup;

import java.util.ArrayList;

import system.CameraView;
import system.Setup;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

public abstract class MarkerDetectionSetup extends Setup {

	private static final String CALIBRATE_BUTTON_TEXT = null;

	@Override
	public void _e1_addElementsToOverlay(FrameLayout overlayView,
			Activity activity) {
		View sourceView = View.inflate(activity, defaultArLayoutId, null);
		GuiSetup guiSetup = new GuiSetup(this, sourceView);
		_e2_addElementsToGuiSetup(guiSetup, activity);
		overlayView.addView(sourceView);

		guiSetup.addItemToOptionsMenu(new CommandShowCalibrationScreen(),
				CALIBRATE_BUTTON_TEXT);
	}

	@Override
	protected final CameraView initCameraView(Activity a) {
		return new AlexCameraView(a);
	}

	@Override
	protected final void initStuff() {
		super.initStuff();
		// TODO change to ArrayList<MarkerObject>
		ArrayList a = _a2_initMarkers();
		// TODO do something with the markers
	}

	// TODO change to ArrayList<MarkerObject>
	public abstract ArrayList _a2_initMarkers();

}
