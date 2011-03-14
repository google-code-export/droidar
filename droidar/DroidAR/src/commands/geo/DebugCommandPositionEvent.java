package commands.geo;

import geo.GeoObj;
import system.EventManager;
import actions.ActionCalcRelativePos;

import commands.Command;

public class DebugCommandPositionEvent extends Command {

	private ActionCalcRelativePos myAction;
	private GeoObj myPos;

	public DebugCommandPositionEvent(ActionCalcRelativePos action,
			GeoObj posToSet) {
		myAction = action;
		myPos = posToSet;
	}

	@Override
	public boolean execute() {
		myAction.onLocationChanged(myPos.toLocation());
		EventManager.getInstance().setCurrentLocation(myPos.toLocation());
		return true;
	}

}
