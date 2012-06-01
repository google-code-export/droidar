package system;

import geo.GeoObj;
import geo.GeoUtils;
import gl.GLCamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listeners.eventManagerListeners.LocationEventListener;
import listeners.eventManagerListeners.OrientationChangedListener;
import listeners.eventManagerListeners.TrackBallEventListener;
import util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import commands.Command;

/**
 * this EventManager is attached to the main {@link Thread} and should react on
 * any kind of event or input
 * 
 * @author Spobo
 * 
 */

public class EventManager implements LocationListener, SensorEventListener {

	private static final String LOG_TAG = "Event Manager";

	private static final long MIN_MS_BEFOR_UPDATE = 200;
	private static final float MIN_DIST_FOR_UPDATE = 1;

	private static EventManager myInstance;

	public static boolean isTabletDevice = false;

	// all the predefined actions:
	protected List<TrackBallEventListener> onTrackballEventList;
	protected List<OrientationChangedListener> onOrientationChangedList;
	protected List<LocationEventListener> onLocationChangedList;
	public HashMap<Integer, Command> myOnKeyPressedCommandList;

	public List<LocationEventListener> getOnLocationChangedAction() {
		return onLocationChangedList;
	}

	public List<OrientationChangedListener> getOnOrientationChangedAction() {
		return onOrientationChangedList;
	}

	public List<TrackBallEventListener> getOnTrackballEventAction() {
		return onTrackballEventList;
	}

	private GeoObj zeroPos;
	private GeoObj currentLocation;
	private Activity myTargetActivity;

	public EventManager() {
	}

	/**
	 * @param c
	 * @param newInstance
	 *            pass a subclass of {@link EventManager} here
	 */
	public static final void initInstance(Context c, EventManager newInstance) {
		isTabletDevice = deviceHasLargeScreenAndOrientationFlipped(c);
		initInstance(newInstance);
	}

	public static EventManager getInstance() {
		if (myInstance == null) {
			Log.e(LOG_TAG, "EventManager instance was not initialized!");
			initInstance(new EventManager());
		}
		return myInstance;
	}

	private static void initInstance(EventManager instance) {
		myInstance = instance;
	}

	public void registerListeners(Activity targetActivity,
			boolean useAccelAndMagnetoSensors) {
		myTargetActivity = targetActivity;
		registerSensorUpdates(targetActivity, useAccelAndMagnetoSensors);
		registerLocationUpdates();

	}

	protected void registerSensorUpdates(Activity myTargetActivity,
			boolean useAccelAndMagnetoSensors) {
		SensorManager sensorManager = (SensorManager) myTargetActivity
				.getSystemService(Context.SENSOR_SERVICE);

		if (useAccelAndMagnetoSensors) {
			/*
			 * To register the EventManger for magnet- and accelerometer-sensor
			 * events, two Sensor-objects have to be obtained and then the
			 * EventManager is set as the Listener for these type of sensor
			 * events. The update rate is set by SENSOR_DELAY_GAME to a high
			 * frequency required to react on fast device movement
			 */
			Sensor magnetSensor = sensorManager
					.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			sensorManager.registerListener(this, magnetSensor,
					SensorManager.SENSOR_DELAY_GAME);
			Sensor accelSensor = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelSensor,
					SensorManager.SENSOR_DELAY_GAME);
		} else {
			// Register orientation Sensor Listener:
			Sensor orientationSensor = sensorManager.getDefaultSensor(11);// Sensor.TYPE_ROTATION_VECTOR);
			sensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/**
	 * This method will try to find the best location source available (probably
	 * GPS if enabled). Remember to wait some seconds before calling this if you
	 * activated GPS programmatically using {@link GeoUtils#enableGPS(Activity)}
	 */
	public void registerLocationUpdates() {

		if (myTargetActivity == null) {
			Log.e(LOG_TAG, "The target activity was undefined while "
					+ "trying to register for location updates");
		}

		LocationManager locationManager = (LocationManager) myTargetActivity
				.getSystemService(Context.LOCATION_SERVICE);
		Log.i(LOG_TAG, "Got locationmanager: " + locationManager);

		try {

			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Log.i(LOG_TAG, "GPS was enabled so this method should "
						+ "come to the conclusion to use GPS as "
						+ "the location source!");
			}

			/*
			 * To register the EventManager in the LocationManager a Criteria
			 * object has to be created and as the primary attribute accuracy
			 * should be used to get as accurate position data as possible:
			 */

			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);

			String provider = locationManager.getBestProvider(criteria, true);
			if (provider == null) {
				Log.w(LOG_TAG, "No location-provider with the "
						+ "specified requierments found.. Trying to find "
						+ "an alternative.");
				List<String> providerList = locationManager.getProviders(true);
				for (String possibleProvider : providerList) {
					if (possibleProvider != null) {
						Log.w(LOG_TAG, "Location-provider alternative "
								+ "found: " + possibleProvider);
						provider = possibleProvider;
					}
				}
				if (provider == null)
					Log.w(LOG_TAG, "No location-provider alternative "
							+ "found!");
			}

			if (!provider.equals(LocationManager.GPS_PROVIDER)) {
				Log.w(LOG_TAG, "The best location provider was not "
						+ LocationManager.GPS_PROVIDER + ", it was " + provider);
			}

			locationManager.requestLocationUpdates(provider,
					MIN_MS_BEFOR_UPDATE, MIN_DIST_FOR_UPDATE, this);
		} catch (Exception e) {
			Log.e(LOG_TAG, "There was an error registering the "
					+ "EventManger for location-updates. The phone might be "
					+ "in airplane-mode..");
			e.printStackTrace();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor s, int accuracy) {
		// Log.d("sensor onAccuracyChanged", arg0 + " " + arg1);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float[] values = event.values;

		if (onOrientationChangedList != null) {

			for (int i = 0; i < onOrientationChangedList.size(); i++) {

				if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					onOrientationChangedList.get(i).onAccelChanged(values);
				}
				if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
					onOrientationChangedList.get(i).onMagnetChanged(values);
				}

				// else sensor input is set to orientation mode
				if (event.sensor.getType() == 11) {// Sensor.TYPE_ROTATION_VECTOR)
					onOrientationChangedList.get(i)
							.onOrientationChanged(values);
				}
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (onLocationChangedList != null) {
			for (int i = 0; i < onLocationChangedList.size(); i++) {
				onLocationChangedList.get(i).onLocationChanged(location);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.w(LOG_TAG, "Didnt handle onProviderDisabled of " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.w(LOG_TAG, "Didnt handle onProviderEnabled of " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

		if (myTargetActivity != null) {
			registerLocationUpdates();
		} else
			Log.w(LOG_TAG, "Didnt handle onStatusChanged of " + provider
					+ "(status=" + status + ")");

	}

	public void addOnOrientationChangedAction(OrientationChangedListener action) {
		Log.d(LOG_TAG, "Adding onOrientationChangedAction");
		if (onOrientationChangedList == null)
			onOrientationChangedList = new ArrayList<OrientationChangedListener>();
		onOrientationChangedList.add(action);
	}

	public void addOnTrackballAction(TrackBallEventListener action) {
		Log.d(LOG_TAG, "Adding onTouchMoveAction");
		if (onTrackballEventList == null)
			onTrackballEventList = new ArrayList<TrackBallEventListener>();
		onTrackballEventList.add(action);

	}

	public void addOnLocationChangedAction(LocationEventListener action) {
		Log.d(LOG_TAG, "Adding onLocationChangedAction");
		if (onLocationChangedList == null)
			onLocationChangedList = new ArrayList<LocationEventListener>();
		onLocationChangedList.add(action);
	}

	public void addOnKeyPressedCommand(int keycode, Command c) {
		if (myOnKeyPressedCommandList == null)
			myOnKeyPressedCommandList = new HashMap<Integer, Command>();
		myOnKeyPressedCommandList.put(keycode, c);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode >= 19 && keyCode <= 22) {
			/*
			 * if the keycode is on of the numbers from 19 to 22 it is a pseudo
			 * trackball event (eg the motorola milestone has pseudo trackball).
			 * here hare the codes (lets hope they are the same on each phone;):
			 * 
			 * top=19 down=20 left=21 right=22
			 */
			if (onTrackballEventList != null) {
				final float stepLength = 0.3f;
				float x = 0, y = 0;
				switch (keyCode) {
				case 19:
					y = -stepLength;
					break;
				case 20:
					y = stepLength;
					break;
				case 21:
					x = -stepLength;
					break;
				case 22:
					x = stepLength;
					break;
				}
				boolean result = true;

				for (int i = 0; i < onTrackballEventList.size(); i++) {
					result &= onTrackballEventList.get(i).onTrackballEvent(x,
							y, null);
				}

				return result;
			}

			return false;
		}

		if (myOnKeyPressedCommandList == null)
			return false;
		Command commandForThisKey = myOnKeyPressedCommandList.get(keyCode);
		if (commandForThisKey != null) {
			Log.d("Command", "Key with command was pressed so executing "
					+ commandForThisKey);
			return commandForThisKey.execute();
		}
		return false;
	}

	/**
	 * This will return the current position of the device according to the
	 * Android system values.
	 * 
	 * The resulting coordinates can differ from
	 * {@link GLCamera#getGPSLocation()} if the camera was not moved according
	 * to the GPS input (eg moved via trackball).
	 * 
	 * Also check the {@link EventManager#getZeroPositionLocationObject()}
	 * method, if you want to know where the virtual zero position (of the
	 * OpenGL world) is.
	 */
	public GeoObj getCurrentLocationObject() {

		Location locaction = getCurrentLocation();
		if (locaction != null) {
			if (currentLocation == null) {
				currentLocation = new GeoObj(locaction, false);
			} else {
				currentLocation.setLocation(locaction);
			}
			return currentLocation;
		} else {
			Log.e(LOG_TAG,
					"Couldn't receive Location object for current location");
		}

		// if its still null set it to a default geo-object:
		if (currentLocation == null) {
			Log.e(LOG_TAG, "Current position set to default 0,0 position");
			currentLocation = new GeoObj(false);
		}

		return currentLocation;
	}

	/**
	 * Uses {@link GeoUtils#getCurrentLocation(Context)}.
	 * 
	 * If you need permanent location updates better create a
	 * {@link LocationEventListener} and register it at
	 * {@link EventManager#addOnLocationChangedAction(LocationEventListener)}
	 * instead of calling this method here frequently.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {
		return GeoUtils.getCurrentLocation(myTargetActivity);
	}

	// /**
	// * The Android system will be asked directly and if the external location
	// * manager knows where the device is located at the moment, this location
	// * will be returned
	// *
	// * @return a new {@link GeoObj} or null if there could be no current
	// * location calculated
	// */
	// public GeoObj getNewCurrentLocationObjectFromSystem() {
	// return getAutoupdatingCurrentLocationObjectFromSystem().copy();
	// }

	public boolean onTrackballEvent(MotionEvent event) {
		if (onTrackballEventList != null) {
			boolean result = true;
			for (int i = 0; i < onTrackballEventList.size(); i++) {
				result &= onTrackballEventList.get(i).onTrackballEvent(
						event.getX(), event.getY(), event);
			}
			return result;
		}
		return false;
	}

	@Deprecated
	public void setCurrentLocation(Location location) {
		currentLocation.setLocation(location);
	}

	/**
	 * This method returns true if the device is a tablet, can be used to handle
	 * the different default orientation
	 * 
	 * @param c
	 * @return
	 */
	public static boolean deviceHasLargeScreenAndOrientationFlipped(Context c) {
		/*
		 * Configuration.SCREENLAYOUT_SIZE_XLARGE only available for higher
		 * Android versions, constant value is 4 so hardcoded here
		 */
		int Configuration_SCREENLAYOUT_SIZE_XLARGE = 4;
		return (c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration_SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * This method differs from the normal
	 * {@link EventManager#getCurrentLocationObject()} because it will return
	 * the geoPos of the virtual (0,0,0) position. The other method would return
	 * the current device position (and because of this also the current camera
	 * position)
	 * 
	 * @return the zero position. This will NOT be a copy so do not modify it!
	 */
	public GeoObj getZeroPositionLocationObject() {
		if (zeroPos == null) {
			Log.d(LOG_TAG, "Zero pos was not yet received! "
					+ "The last known position of the device will be used "
					+ "at the zero position.");
			zeroPos = getCurrentLocationObject().copy();
		}
		return zeroPos;
	}

	public void setZeroLocation(Location location) {
		if (zeroPos == null)
			zeroPos = new GeoObj(location);
		else
			zeroPos.setLocation(location);
	}

	public void resumeEventListeners(Activity targetActivity,
			boolean useAccelAndMagnetoSensors) {
		registerListeners(targetActivity, useAccelAndMagnetoSensors);
	}

	public void pauseEventListeners() {
		SensorManager sensorManager = (SensorManager) myTargetActivity
				.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener(this);
		LocationManager locationManager = (LocationManager) myTargetActivity
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

}
