package listeners;

import listeners.eventManagerListeners.AccelCahngedListener;
import listeners.eventManagerListeners.CamRotationVecUpdateListener;
import listeners.eventManagerListeners.LocationEventListener;
import listeners.eventManagerListeners.MagnetChangedListener;
import listeners.eventManagerListeners.OrientationChangedListener;
import listeners.eventManagerListeners.TouchMoveListener;
import listeners.eventManagerListeners.TrackBallEventListener;

@Deprecated
public interface EventListener extends LocationEventListener,
		OrientationChangedListener, AccelCahngedListener, TouchMoveListener,
		MagnetChangedListener, TrackBallEventListener,
		CamRotationVecUpdateListener {

}