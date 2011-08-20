package commands.geo;

import geo.GMap;
import geo.GeoGraph;
import geo.GeoObj;
import util.Wrapper;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import commands.Command;

public class CommandMapCenter extends Command {

	private GMap myMap;
	private Wrapper myGeoWrapper;
	private Handler mHandler = new Handler();

	public CommandMapCenter(GMap map, Wrapper geoWrapper) {
		myMap = map;
		myGeoWrapper = geoWrapper;
	}

	@Override
	public boolean execute() {
		if (myGeoWrapper == null || myMap == null) {
			Log.e("Commands",
					"CommandMapCenter wasnt initialized correctly on creation");
			return false;
		}
		if (myGeoWrapper.getObject() instanceof GeoGraph) {
			// System.out.println("myGeoWrapper.getObject()="
			// + myGeoWrapper.getObject());
			// System.out.println("myGeoWrapper.getObject()).getMyItems()="
			// + ((GeoGraph) myGeoWrapper.getObject()).getMyItems());
			// System.out
			// .println("((GeoGraph) myGeoWrapper.getObject()).getMyItems()="
			// + ((GeoGraph) myGeoWrapper.getObject())
			// .getMyItems());
			if (((GeoGraph) myGeoWrapper.getObject()).getNodes().get(0) != null) {
				final GeoObj fistPointInGraph = (GeoObj) ((GeoGraph) myGeoWrapper
						.getObject()).getNodes().get(0);

				// migth not be called from main thread so use handler
				mHandler.post(new Runnable() {
					public void run() {
						myMap.setCenterTo(fistPointInGraph);
					}
				});

			}
		}
		return false;
	}

	private GeoObj p;

	@Override
	public boolean execute(Object transfairObject) {
		if (transfairObject instanceof Location) {
			if (p == null) {
				p = new GeoObj((Location) transfairObject);
			} else {
				// this way there wont be an geoObj creation on every command
				// execution
				Log.d("GeoGraph", "centering map so setting pos of " + p);
				p.setLocation((Location) transfairObject);
			}
			myMap.setCenterTo(p);
			return true;
		}
		return false;
	}

}
