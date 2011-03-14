package actions;

import geo.GeoCalcer;
import gl.GLCamera;
import worldData.World;
import android.location.Location;
import android.util.Log;

/**
 * This action is the basic action for virtual camera movement in relation to
 * the device movement. The gps input is used to calculate the virtual position.
 * If the distance to the center of the virtual world gets to big, the virtual
 * zero postion is reseted and the virtual positions are recalculated
 * 
 * TODO combine this with the moveCamera action? good idea or not?
 * 
 * @author Spobo
 * 
 */
public class ActionCalcRelativePos extends Action {

	private static final double MAX_METER_DISTANCE = 500; // 500 meter
	private static final String LOG_TAG = "Action calc relative pos";
	private double nullLongitude;
	private double nullLatitude;

	private World myWorld;
	private GLCamera myCamera;
	private GeoCalcer myGeoCalcer;
	private double nullAltitude;

	public ActionCalcRelativePos(World world, GLCamera camera) {
		myWorld = world;
		myCamera = camera;
	}

	@Override
	public boolean onLocationChanged(Location location) {
		Log.d(LOG_TAG, "new pos: lat=" + location.getLatitude() + " long="
				+ location.getLongitude());
		if (nullLatitude == 0 || nullLongitude == 0) {
			setNewNullValues(location);
			Log.d(LOG_TAG, "pos event: init nullLong and nullLat");
			resetCameraToNullPosition();
			calcNewWorldPositions();
		} else {
			/*
			 * the following calculations were extracted from
			 * GeoObj.calcVirtualPosition() for further explanation how they
			 * work read the GeoObj javadoc. the two calculations were extracted
			 * to increase performance because this method will be called every
			 * time a new GPS-position arrives
			 */
			final double latitudeDistInMeters = (location.getLatitude() - nullLatitude) * 111133.3333;
			final double longitudeDistInMeters = (location.getLongitude() - nullLongitude)
					* 111319.4917 * Math.cos(nullLatitude * 0.0174532925);
			/*
			 * The altitude should be set to a certain position too. This can be
			 * done by using location.getAltitude() TODO first check side
			 * effects
			 */
			final double altMet = 0;// location.getAltitude(); TODO
			Log.d(LOG_TAG, "pos event: check new values:");
			Log.d(LOG_TAG, "pos latitude dist.: " + latitudeDistInMeters
					+ ", longitude dist.:" + longitudeDistInMeters);
			if (worldShouldBeRecalced(latitudeDistInMeters,
					longitudeDistInMeters, altMet)) {
				Log.d(LOG_TAG, "pos event: pos values to far "
						+ "away from origin so recalc");
				setNewNullValues(location);
				resetCameraToNullPosition();
				calcNewWorldPositions();
			} else {
				Log.d(LOG_TAG,
						"pos event: pos values small, just camera update");
				updateCamera(latitudeDistInMeters, longitudeDistInMeters,
						altMet);
			}
		}

		return true;
	}

	private void updateCamera(double latDistMet, double longDistMet,
			double altDistMet) {
		// myCamera.setNewPosition((float) latDistMet, (float) longDistMet,
		// (float) altDistMet);
		myCamera.setNewPosition((float) latDistMet, (float) longDistMet);
	}

	private void setNewNullValues(Location location) {
		nullLatitude = location.getLatitude();
		nullLongitude = location.getLongitude();
		nullAltitude = location.getAltitude();
	}

	private void calcNewWorldPositions() {
		if (myGeoCalcer == null)
			myGeoCalcer = new GeoCalcer();
		myGeoCalcer.setNullPos(nullLatitude, nullLongitude, nullAltitude);
		myWorld.accept(myGeoCalcer);
	}

	private void resetCameraToNullPosition() {
		myCamera.resetPosition(false);
	}

	private boolean worldShouldBeRecalced(double latDistMet,
			double longDistMet, double altDistMet) {
		if (Math.abs(latDistMet) > MAX_METER_DISTANCE)
			return true;
		if (Math.abs(longDistMet) > MAX_METER_DISTANCE)
			return true;
		return false;
	}

}
