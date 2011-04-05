package util;

import worldData.UpdateTimer;
import worldData.Updateable;

public class DebugMonitor implements Updateable {

	private UpdateTimer myUpdater;
	private HasDebugInformation myObjectToDebug;

	public DebugMonitor(HasDebugInformation objectToDebug, float updateSpeed) {
		myUpdater = new UpdateTimer(updateSpeed, null);
		myObjectToDebug = objectToDebug;
	}

	@Override
	public boolean update(float timeDelta) {
		if (myUpdater.update(timeDelta)) {
			myObjectToDebug.showDebugInformation();
		}
		return true;
	}

}
