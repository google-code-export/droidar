package actions;

import system.EventManager;
import geo.GeoCalcer;
import gl.GLCamera;
import worldData.World;
import android.location.Location;
import util.Log;

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
	private static final String LOG_TAG = "ActionCalcRelativePos";
	private static final boolean LOG_SHOW_POSITION = true;
	/**
	 * this could be replaces by the
	 * {@link EventManager#getZeroPositionLocationObject()} values. Should store
	 * the same information. where is the better place to store the data TODO
	 */
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

		if (nullLatitude == 0 || nullLongitude == 0) {
			resetWorldZeroPositions(location);
		} else {
			/*
			 * the following calculations were extracted from
			 * GeoObj.calcVirtualPosition() for further explanation how they
			 * work read the javadoc there. the two calculations were extracted
			 * to increase performance because this method will be called every
			 * time a new GPS-position arrives
			 */
			final double latitudeDistInMeters = (location.getLatitude() - nullLatitude) * 111133.3333;
			final double longitudeDistInMeters = (location.getLongitude() - nullLongitude)
					* 111319.4917 * Math.cos(nullLatitude * 0.0174532925);

			if (LOG_SHOW_POSITION) {
				Log.v(LOG_TAG, "latitudeDistInMeters=" + latitudeDistInMeters);
				Log.v(LOG_TAG, "longitudeDistInMeters=" + longitudeDistInMeters);
			}

			/*
			 * The altitude should be set to a certain position too. This can be
			 * done by using location.getAltitude() TODO first think of all
			 * consequences!
			 */
			final double altMet = 0;// location.getAltitude(); TODO

			if (worldShouldBeRecalced(latitudeDistInMeters,
					longitudeDistInMeters, altMet)) {
				resetWorldZeroPositions(location);
			} else {
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
		myCamera.setNewPosition((float) latDistMet, (float) longDistMet, 0);
	}

	private void setNewNullValues(Location location) {
		nullLatitude = location.getLatitude();
		nullLongitude = location.getLongitude();
		nullAltitude = location.getAltitude();
		EventManager.getInstance().setZeroLocation(location);
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

	public void resetWorldZeroPositions(Location location) {
		Log.d(LOG_TAG, "Reseting virtual world positions");
		setNewNullValues(location);
		resetCameraToNullPosition();
		calcNewWorldPositions();
	}

}
