package actions;

import system.EventManager;
import geo.GeoCalcer;
import geo.GeoObj;
import gl.GLCamera;
import worldData.World;
import android.location.Location;
import util.Log;

/**
 * This action is the basic action for virtual camera movement in relation to
 * the physical device movement. The GPS input is used to calculate the virtual
 * position. If the distance to the center of the virtual world gets to big, the
 * virtual zero position is reseted and the virtual positions are recalculated
 * 
 * TODO combine this with the moveCamera action? good idea or not?
 * 
 * @author Spobo
 * 
 */
public class ActionCalcRelativePos extends Action {

	/**
	 * set this to false if your scenario does not need to take altitude values
	 * into account
	 */
	public static boolean USE_ALTITUDE_VALUES = true;

	private static final double MAX_METER_DISTANCE = 500; // 500 meter
	private static final String LOG_TAG = "ActionCalcRelativePos";

	private static final boolean LOG_SHOW_POSITION = true; // TODO switch to
															// false

	/**
	 * this could be replaces by the
	 * {@link EventManager#getZeroPositionLocationObject()} values. Should store
	 * the same information. where is the better place to store the data TODO
	 */
	private double nullLongitude;
	private double nullLatitude;
	private double nullAltitude;

	private World myWorld;
	private GLCamera myCamera;
	private GeoCalcer myGeoCalcer;

	public ActionCalcRelativePos(World world, GLCamera camera) {
		myWorld = world;
		myCamera = camera;
	}

	@Override
	public boolean onLocationChanged(Location location) {
		if (nullLatitude == 0 || nullLongitude == 0) {
			/*
			 * if the nullLat or nullLong are 0 this method was probably never
			 * called before (TODO problem when living in greenwhich e.g.?)
			 */
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

			/*
			 * if the altitude values should be used calculate the correct
			 * height else use 0 as the height
			 */
			final double relativeHeight = USE_ALTITUDE_VALUES ? location
					.getAltitude() - nullAltitude : 0;

			if (LOG_SHOW_POSITION) {
				Log.v(LOG_TAG, "latitudeDistInMeters=" + latitudeDistInMeters);
				Log.v(LOG_TAG, "longitudeDistInMeters=" + longitudeDistInMeters);
				Log.v(LOG_TAG, "relativeHeight=" + relativeHeight);
			}

			if (worldShouldBeRecalced(latitudeDistInMeters,
					longitudeDistInMeters)) {
				resetWorldZeroPositions(location);
			} else {
				updateCamera(latitudeDistInMeters, longitudeDistInMeters,
						relativeHeight);
			}
		}

		return true;
	}

	private void updateCamera(double latDistMet, double longDistMet,
			double altDistMet) {
		myCamera.setNewPosition((float) longDistMet,(float) latDistMet,
				(float) altDistMet);
	}

	private void resetCameraToNullPosition() {
		myCamera.resetPosition(false);
	}

	private boolean worldShouldBeRecalced(double latDistMet, double longDistMet) {
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

}
