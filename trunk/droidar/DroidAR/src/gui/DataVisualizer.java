package gui;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

/**
 * this class was designed to display a constant stream of data to the user. the
 * visualization of data has to be done inside the android ui thread so a
 * handler is used to pass the data
 * 
 * to use the update event create a ChangeListener und write the values in the
 * target view there
 * 
 * TODO move to smartui
 * 
 * @author Spobo
 * 
 */
@Deprecated
public class DataVisualizer {

	public static interface ChangeListener {
		void onRefresh(String myValue, float myV);
	}

	private String myValue;
	public ChangeListener myListener;
	private Handler mHandler = new Handler();
	float updateSpeedInMs = 100;
	private long lastTimeInMs = 0;
	private float myV;

	private Runnable myRunnable = new Runnable() {
		public void run() {
			final long currentTime = SystemClock.uptimeMillis();
			final float timeDelta = (currentTime - lastTimeInMs);
			// System.out.println(timeDelta);
			if (timeDelta >= updateSpeedInMs) {
				lastTimeInMs = currentTime;

				if (myListener != null)
					myListener.onRefresh(myValue, myV);
			}
		}
	};

	public void setValue(String string) {
		myValue = string;
		refresh();
	}

	public void refresh() {
		if (!mHandler.post(myRunnable)) {
			Log.e("DataVisualizer", "couldnt post runnable");
		}
	}

	public void setValue(float v) {
		myV = v;
	}

}
