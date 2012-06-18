package system;

import java.util.ArrayList;
import java.util.List;

import listeners.eventManagerListeners.LocationEventListener;
import system.StepManager.OnStepListener;
import util.Log;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public abstract class SimpleLocationManager {

	private static final String LOG_TAG = "SimpleLocationManager";
	private static final long MIN_MS_BEFOR_UPDATE = 200;
	private static final float MIN_DIST_FOR_UPDATE = 0.5f;

	private static SimpleLocationManager instance;

	private Context context;
	private LocationListener gpslistener;
	private ArrayList<LocationListener> myListeners;
	private OnStepListener stepListener;
	private StepManager stepManager;

	public SimpleLocationManager(Context context) {
		this.context = context;
	}

	public static SimpleLocationManager getInstance(Context context) {
		if (instance == null)
			instance = new ConcreteSimpleLocationManager(context);
		return instance;
	}

	public static boolean resetInstance() {
		if (instance == null)
			return false;
		instance.pauseLocationManagerUpdates();
		instance = null;
		return true;
	}

	private LocationListener initListener() {
		return new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				for (int i = 0; i < myListeners.size(); i++) {
					myListeners.get(i)
							.onStatusChanged(provider, status, extras);
				}
			}

			@Override
			public void onProviderEnabled(String provider) {
				for (int i = 0; i < myListeners.size(); i++) {
					myListeners.get(i).onProviderEnabled(provider);
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				for (int i = 0; i < myListeners.size(); i++) {
					myListeners.get(i).onProviderDisabled(provider);
				}
			}

			@Override
			public void onLocationChanged(Location location) {
				locationUpdateFromAndroidLocationManager(location, myListeners);
			}
		};
	}

	/**
	 * This method buffers the location and passes the buffered location down to
	 * the locationListeners
	 * 
	 * @param location
	 * @param listenersToInform
	 */
	public abstract void locationUpdateFromAndroidLocationManager(
			Location location, ArrayList<LocationListener> listenersToInform);

	/**
	 * will pause updates from the {@link LocationManager} to the
	 * {@link SimpleLocationManager}
	 */
	public boolean pauseLocationManagerUpdates() {
		// its important to use instance here and not getInstance()!
		if (gpslistener != null) {
			Log.i(LOG_TAG, "Pausing position updates!");
			getLocationManager().removeUpdates(gpslistener);
			gpslistener = null;
		}
		if (stepListener != null) {
			Log.i(LOG_TAG, "Pausing step updates!");
			stepManager.stop();
			stepManager.unregisterSensors();
			stepManager.unRegisterStepListener(stepListener);
			stepListener = null;
			stepManager = null;
			return true;
		}
		return false;
	}

	private LocationManager getLocationManager() {
		return (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * @return null if there is no current location measured yet
	 */
	public abstract Location getCurrentBUfferedLocation();

	/**
	 * @param accuracy
	 *            see the {@link Criteria#setAccuracy(int)} method for possible
	 *            parameter types
	 * @return
	 */
	public Location getCurrentLocation(int accuracy) {

		Location l = getCurrentBUfferedLocation();
		if (l != null)
			return l;

		if (context != null) {
			try {
				LocationManager lm = getLocationManager();
				Criteria criteria = new Criteria();
				criteria.setAccuracy(accuracy);
				return lm.getLastKnownLocation(lm.getBestProvider(criteria,
						true));
			} catch (Exception e) {
				Log.e(LOG_TAG, "Could not receive the current location");
				e.printStackTrace();
				return null;
			}
		}
		Log.e(LOG_TAG, "The passed activity was null!");
		return null;
	}

	/**
	 * This method will try to get the most accurate position currently
	 * available. This includes also the last known position of the device if no
	 * current position sources can't be accessed so the returned position might
	 * be outdated <br>
	 * <br>
	 * If you need permanent location updates better create a
	 * {@link LocationEventListener} and register it at
	 * {@link EventManager#addOnLocationChangedAction(LocationEventListener)}
	 * instead of calling this method here frequently.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {

		Location l = getCurrentBUfferedLocation();
		if (l != null)
			return l;

		Log.w(LOG_TAG, "buffered current location object was null, "
				+ "will use the one from the android LocationManager!");

		l = getCurrentLocation(Criteria.ACCURACY_FINE);
		if (l == null) {
			Log.e(LOG_TAG,
					"Fine accuracy position could not be detected! Will use coarse location.");
			l = getCurrentLocation(Criteria.ACCURACY_COARSE);
			if (l == null) {
				Log.e(LOG_TAG,
						"Coarse accuracy position could not be detected! Last try..");
				try {
					LocationManager lm = getLocationManager();
					Log.d(LOG_TAG, "Searching through "
							+ lm.getAllProviders().size()
							+ " location providers");
					for (int i = lm.getAllProviders().size() - 1; i >= 0; i--) {
						l = lm.getLastKnownLocation(lm.getAllProviders().get(i));
						if (l != null)
							break;
					}
				} catch (Exception e) {
				}
			}
		}
		Log.d(LOG_TAG, "current position=" + l);
		return l;
	}

	public String findBestLocationProvider() {
		LocationManager locationManager = getLocationManager();

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i(LOG_TAG, "GPS was enabled so this method should "
					+ "come to the conclusion to use GPS as "
					+ "the location source!");
		}

		/*
		 * To register the EventManager in the LocationManager a Criteria object
		 * has to be created and as the primary attribute accuracy should be
		 * used to get as accurate position data as possible:
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
					Log.w(LOG_TAG, "Location-provider alternative " + "found: "
							+ possibleProvider);
					provider = possibleProvider;
				}
			}
			if (provider == null)
				Log.w(LOG_TAG, "No location-provider alternative " + "found!");
		}

		if (!provider.equals(LocationManager.GPS_PROVIDER)) {
			Log.w(LOG_TAG, "The best location provider was not "
					+ LocationManager.GPS_PROVIDER + ", it was " + provider);
		}
		return provider;
	}

	public boolean requestLocationUpdates(String provider,
			long minMsBeforUpdate, float minDistForUpdate,
			LocationListener locationListener) {

		registerSimpleEventManagerAsListenerIfNotDoneJet(provider,
				minMsBeforUpdate, minDistForUpdate);

		return addToListeners(locationListener);

	}

	private void registerSimpleEventManagerAsListenerIfNotDoneJet(
			String provider, long minMsBeforUpdate, float minDistForUpdate) {
		if (gpslistener == null) {
			gpslistener = initListener();
			Log.i(LOG_TAG,
					"Created location listener and now registering for updates..");
			Log.i(LOG_TAG, "    > provider=" + provider);
			Log.i(LOG_TAG, "    > minMsBeforUpdate=" + minMsBeforUpdate);
			Log.i(LOG_TAG, "    > minDistForUpdate=" + minDistForUpdate);
			getLocationManager().requestLocationUpdates(provider,
					minMsBeforUpdate, minDistForUpdate, gpslistener);
		}
		if (stepManager == null) {
			stepManager = new StepManager();
			stepManager.registerSensors(context);
			stepManager.start();
		}
		if (stepListener == null) {
			stepListener = new OnStepListener() {
				@Override
				public void onStep(double bearing, double steplength) {
					Log.d(LOG_TAG, "Step detected");
					Log.d(LOG_TAG, "    > angle=" + bearing);
					Log.d(LOG_TAG, "    > distance=" + steplength);
				}
			};
			stepManager.registerStepListener(stepListener);
			Log.i(LOG_TAG, "Step listener registered");
		}
	}

	public boolean requestLocationUpdates(LocationListener locationListener) {
		return requestLocationUpdates(findBestLocationProvider(),
				MIN_MS_BEFOR_UPDATE, MIN_DIST_FOR_UPDATE, locationListener);
	}

	private boolean addToListeners(LocationListener locationListener) {
		if (myListeners == null)
			myListeners = new ArrayList<LocationListener>();
		if (!myListeners.contains(locationListener)) {
			Log.i(LOG_TAG, "Adding listener " + locationListener + " to list");
			myListeners.add(locationListener);
			return true;
		}
		return false;

	}

}
