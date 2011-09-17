package actions;

import listeners.EventListener;
import system.ListInterface;
import util.EfficientList;
import util.Vec;
import android.location.Location;
import android.view.MotionEvent;

public class EventListenerGroup extends Action implements ListInterface {

	EfficientList<EventListener> myActions = new EfficientList<EventListener>();

	@Override
	public boolean onOrientationChanged(float[] values) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onOrientationChanged(values);
		}
		return true;
	}

	@Override
	public boolean onCamOrientationUpdate(float[] myOrientValues,
			float[] myNewOrientValues, float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamOrientationUpdate(myOrientValues,
					myNewOrientValues, timeDelta);
		}
		return true;
	}

	@Override
	public boolean onCamAccelerationUpdate(float[] target, float[] values,
			float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamAccelerationUpdate(target, values, timeDelta);
		}
		return true;
	}

	@Override
	public boolean onCamMagnetometerUpdate(float[] target, float[] values,
			float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamMagnetometerUpdate(target, values, timeDelta);
		}
		return true;
	}

	@Override
	public void onCamOffsetVecUpdate(Vec target, Vec values, float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamOffsetVecUpdate(target, values, timeDelta);
		}
	}

	@Override
	public void onCamPositionVecUpdate(Vec target, Vec values, float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamPositionVecUpdate(target, values, timeDelta);
		}
	}

	@Override
	public void onCamRotationVecUpdate(Vec target, Vec values, float timeDelta) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onCamRotationVecUpdate(target, values, timeDelta);
		}
	}

	public boolean onLocationChanged(Location location) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onLocationChanged(location);
		}
		return true;
	}

	@Override
	public boolean onAccelChanged(float[] values) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onAccelChanged(values);
		}
		return true;
	}

	@Override
	public boolean onMagnetChanged(float[] values) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onMagnetChanged(values);
		}
		return true;
	}

	@Override
	public boolean onReleaseTouchMove() {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onReleaseTouchMove();
		}
		return true;
	}

	@Override
	public boolean onTrackballEvent(float x, float y, MotionEvent event) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onTrackballEvent(x, y, event);
		}
		return true;
	}

	public boolean onTouchMove(MotionEvent e1, MotionEvent e2, float deltaX,
			float deltaY) {
		for (int i = 0; i < myActions.myLength; i++) {
			myActions.get(i).onTouchMove(e1, e2, deltaX, deltaY);
		}
		return true;
	}

	public void add(EventListener action) {
		myActions.add(action);
	}

	@Override
	public void clear() {
		myActions.clear();
	}

	@Override
	public int length() {
		return myActions.myLength;
	}

	@Override
	public EfficientList<EventListener> getNodes() {
		return myActions;
	}

	@Override
	public boolean isCleared() {
		return (myActions.myLength == 0);
	}

	@Override
	public void removeEmptyItems() {
		/*
		 * There is now way to detect redundant actions at the moment so this
		 * method will do nothing.
		 */
	}

	public boolean remove(EventListener actionToRemove) {
		return myActions.remove(actionToRemove);
	}

}
