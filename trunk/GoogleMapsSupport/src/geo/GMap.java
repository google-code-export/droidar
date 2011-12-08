package geo;

import gui.CustomGestureListener;
import system.EventManager;
import system.TouchEventInterface;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import commands.Command;

/**
 * To get a maps api key you have to use a keytool to generate a MD5 fingerprint
 * for your computer. <br>
 * <br>
 * The keytool.exe is part of the Java SDK you are using (eg: C:\Program
 * Files\Java\jdk1.6.0_22\bin\keytool.exe). <br>
 * <br>
 * So move the debug.keystore to C, start your cmd.exe and type: <br>
 * <br>
 * C:\Program Files\Java\jdk1.6.0_22\bin>keytool -list -alias androiddebugkey
 * -keystore C:\debug.keystore -keypass android <br>
 * <br>
 * Here are more infos how to use the debug fingerprint:
 * http://code.google.com/android/add-ons/google-apis/mapkey.html#
 * getdebugfingerprint <br>
 * <br>
 * Now use the MD5 fingerprint you will get here:
 * http://code.google.com/android/maps-api-signup.html <br>
 * <br>
 * The code you get from the signup page has to be passed in the constructor of
 * the GMap class
 * 
 * @author Spobo
 * 
 */
public class GMap extends MapView implements TouchEventInterface {

	private static final String LOG_TAG = "GMap";

	private Handler myHandler = new Handler();

	// public LinearLayout myBox;

	private GestureDetector myGestureDetector;
	private Command myOnDoubleTabCommand;
	private Command myOnLongPressCommand;
	private Command myOnTabCommand;

	// private MotionEvent currentEvent;

	public GMap(Context c, String apiKey) {
		this(c, apiKey, false);
	}

	/**
	 * Same method as setBuiltInZoomControls(), only purpose is to better find
	 * this method
	 * 
	 * @param showThem
	 */
	public void enableZoomButtons(boolean showThem) {
		this.setBuiltInZoomControls(showThem);
	}

	/**
	 * @param box
	 *            the {@link MapView} will fill the complete box
	 * @param apiKey
	 *            see {@link GMap}
	 * @param longPressEnanabled
	 *            when long pressing is enabled, the default map movemant won't
	 *            work, so default if false
	 */
	public GMap(Context c, String apiKey, boolean longPressEnanabled) {
		super(c, apiKey);

		setLayoutParams(new android.view.ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));

		// create a GestureDetector:
		myGestureDetector = new GestureDetector(c, new CustomGestureListener(
				this));
		myGestureDetector.setIsLongpressEnabled(longPressEnanabled);
	}

	public boolean setCenterTo(GeoObj myGeoObj) {

		try {
			this.getController().animateTo(GMap.toGeoPoint(myGeoObj));
			this.invalidate();
			return true;
		} catch (Exception e) {
			Log.e(LOG_TAG, "Probably no internet connection available");
			e.printStackTrace();
		}
		return false;

	}

	public void addOverlay(final Overlay mapOverlay) {
		// update map through a handler (event might come not from from main
		// thread):
		final GMap map = this;
		myHandler.post(new Runnable() {
			@Override
			public void run() {
				Log.d(LOG_TAG, "Adding map overlay: " + mapOverlay);
				map.getOverlays().add(mapOverlay);
				try {
					map.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setCenterToCurrentPos() {
		setCenterTo(EventManager.getInstance().getCurrentLocationObject());
	}

	/**
	 * @param level
	 *            1 is whole world, 21 is closest zoom level
	 */
	public void setZoomLevel(int level) {
		this.getController().setZoom(level);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		/*
		 * to understand the idea of redirecting the events look at the
		 * MyGestureListener class which will inform the map when a special
		 * event like a tab, doubleTab, LongPress, .. happened
		 */
		myGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public void onDoubleTap(MotionEvent e) {
		Command c = getOnDoubleTabCommand();
		if (c != null) {
			GeoObj p = toGeoObj(getProjection().fromPixels((int) e.getX(),
					(int) e.getY()));
			c.execute(p);
		}
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Command c = getOnLongPressCommand();
		if (c != null) {
			GeoObj p = toGeoObj(getProjection().fromPixels((int) e.getX(),
					(int) e.getY()));
			c.execute(p);
		}
	}

	@Override
	public void onSingleTab(MotionEvent e) {
		Command c = getOnTabCommand();
		if (c != null) {
			GeoObj p = toGeoObj(getProjection().fromPixels((int) e.getX(),
					(int) e.getY()));
			c.execute(p);
		}
	}

	@Override
	public Command getOnDoubleTabCommand() {
		return myOnDoubleTabCommand;
	}

	@Override
	public Command getOnLongPressCommand() {
		return myOnLongPressCommand;
	}

	@Override
	public Command getOnTabCommand() {
		return myOnTabCommand;
	}

	@Override
	public void setOnDoubleTabCommand(Command c) {
		myOnDoubleTabCommand = c;
	}

	@Override
	public void setOnLongPressCommand(Command c) {
		myOnLongPressCommand = c;
	}

	@Override
	public void setOnTabCommand(Command c) {
		setOnTabCommand(c, true);
	}

	/**
	 * this is the command that will be executed if the {@link GMap} is tabed.
	 * if the DefaultOnClickCommand for the map isn't set yet this will be done
	 * too, so if there will be items on the map and those items are selected
	 * the OnMapTouchReleasedCommand will be executed too
	 */
	public void setOnTabCommand(Command c,
			boolean setDefaultItemSelectCommandToo) {

		// if (setDefaultItemSelectCommandToo
		// && getOnTabCommand() == null)
		// setDefaultOnClickCommand(c);
		myOnTabCommand = c;
	}

	@Override
	public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// do nothing, because scrolling already done by the map
	}

	public static GeoPoint toGeoPoint(GeoObj x) {
		return new GeoPoint((int) (x.getLatitude() * 1E6),
				(int) (x.getLongitude() * 1E6));
	}

	public static GeoObj toGeoObj(GeoPoint point) {
		return new GeoObj(point.getLatitudeE6() / 1E6,
				point.getLongitudeE6() / 1E6);
	}

}
