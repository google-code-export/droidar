package commands.geo;

import android.util.Log;
import geo.GeoObj;
import listeners.EventListener;
import system.EventManager;

import commands.Command;

public class DebugCommandPositionEvent extends Command {

	private static final String LOG_TAG = "DebugCommandPositionEvent";
	private EventListener myAction;
	private GeoObj myPos;

	public DebugCommandPositionEvent(EventListener action, GeoObj posToSet) {
		myAction = action;
		myPos = posToSet;
	}

	@Override
	public boolean execute() {
		myAction.onLocationChanged(myPos.toLocation());
		EventManager.getInstance().setCurrentLocation(myPos.toLocation());
		return true;
	}

	public static void goToCoords(double latitude, double longitude) {
		if (EventManager.getInstance().onLocationChangedAction != null)
			new DebugCommandPositionEvent(
					EventManager.getInstance().onLocationChangedAction,
					new GeoObj(latitude, longitude)).execute();
		else
			Log.e(LOG_TAG,
					"onLocationChangedAction was null so this debug command wont work");
	}
}
