package commands.geo;

import geo.GMap;
import geo.GeoObj;
import system.EventManager;
import util.Wrapper;
import android.util.Log;

import commands.Command;

public class CommandFindWayWithGMaps extends Command {

	private GMap myMap;
	private Wrapper mySearchTextWrapper;
	private Wrapper myResultingPath;
	private GeoObj myStartPos;
	private Wrapper byWalk;

	/**
	 * @param map
	 * @param startPos
	 *            if null then the current location of the user will be used as
	 *            the start-position
	 * @param searchTextWrapper
	 * @param sresults
	 */
	public CommandFindWayWithGMaps(GMap map, GeoObj startPos,
			Wrapper searchTextWrapper, Wrapper sresults, Wrapper byWalk) {
		myMap = map;
		mySearchTextWrapper = searchTextWrapper;
		myResultingPath = sresults;
		myStartPos = startPos;
		this.byWalk = byWalk;
	}

	public CommandFindWayWithGMaps(GMap map, Wrapper searchTextWrapper,
			Wrapper sresults, Wrapper byWalk) {
		this(map, null, searchTextWrapper, sresults, byWalk);
	}

	@Override
	public boolean execute() {
		String text = mySearchTextWrapper.getStringValue();
		if (text == "")
			return false;
		Log.d("Gmaps", "Searching point near " + text);
		GeoObj target = myMap.getGeoUtils().getBestLocationForAddress(text);
		if (target == null) {
			Log.d("Gmaps", "   -> No point for search string found..");
			return false;
		}
		Log.d("Gmaps", "Found point: " + target.toString());
		if (myStartPos == null) {
			Log.d("Gmaps", "Searching way from current location to " + target);
			return myMap.getGeoUtils().getPathFromAtoB(EventManager.getInstance()
					.getCurrentLocationObject(), target,
					myResultingPath, byWalk.getBooleanValue());
		}
		Log.d("Gmaps", "Searching way from " + myStartPos + " to " + target);
		return myMap.getGeoUtils().getPathFromAtoB(myStartPos, target, myResultingPath, byWalk
				.getBooleanValue());
	}

}
