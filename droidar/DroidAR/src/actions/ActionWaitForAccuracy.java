package actions;

import geo.GeoUtils;
import system.EventManager;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.rwth.R;

public abstract class ActionWaitForAccuracy extends Action {

	private static final String TEXT_DIALOG_TITLE = "Do you want to cancel the accuracy detection?";

	private static final String TEXT_POSITION_ACCURACY = "Position Accuracy ";

	private static final String TEXT_SKIP_ACCURACY_DETECTION = "Skip accuracy detection (not recomended!)";

	// 1 minutes in ms:
	private static final long MAX_TIME_SINCE_LAST_UPDATE_IN_MS = 1000 * 60 * 1;

	private static final String LOG_TAG = "ActionWaitForAccuracy";

	private float myCurrentAccuracy;
	private float myMinAccuracy;
	private boolean firstTimeReached = false;

	private int myMaxPosUpdateCount;

	private int stepCounter = 0;

	private Activity myContext;

	private TextView accText;

	private ProgressBar steps;

	private View viewContainer;

	private Button warningText;

	/**
	 * @param context
	 * @param minAccuracy
	 *            should be >= 25m
	 * @param maxPosUpdateCount
	 *            The max number of update events before the position should be
	 *            accurate enough (something around 6)
	 */
	public ActionWaitForAccuracy(Activity context, float minAccuracy,
			int maxPosUpdateCount) {
		myContext = context;
		myMinAccuracy = minAccuracy;
		myMaxPosUpdateCount = maxPosUpdateCount;
		analyseInitLocation(GeoUtils.getCurrentLocation(context));
	}

	private void analyseInitLocation(Location l) {
		myCurrentAccuracy = l.getAccuracy();
		long passedTime = System.currentTimeMillis() - l.getTime();
		Log.d(LOG_TAG, "Passed time since last location event="
				+ (passedTime / 1000f / 10f) + " minutes");
		if (passedTime <= MAX_TIME_SINCE_LAST_UPDATE_IN_MS)
			onLocationChanged(l);
	}

	@Override
	public boolean onLocationChanged(Location l) {
		Log.d(LOG_TAG, "Current signal accuracy=" + l.getAccuracy());
		Log.d(LOG_TAG, "Minimum needed accuracy=" + myMinAccuracy);
		Log.d(LOG_TAG, "Current pos update count=" + stepCounter);
		Log.d(LOG_TAG, "Max pos updates=" + myMaxPosUpdateCount);
		stepCounter++;
		myCurrentAccuracy = l.getAccuracy();
		updateUI();
		if ((l.getAccuracy() != 0 && l.getAccuracy() <= myMinAccuracy)
				|| stepCounter >= myMaxPosUpdateCount) {
			callFirstTimeAccReachedIfNotYetCalled(l);

		}
		return true;
	}

	private void callFirstTimeAccReachedIfNotYetCalled(Location location) {
		if (!firstTimeReached) {
			firstTimeReached = true;
			Log.d(LOG_TAG, "Required accuracy was reached!");
			minAccuracyReachedFirstTime(location, this);
			hideUI();
		} else
			Log.w(LOG_TAG, "callFirstTimeAccReachedIfNotYetCalled was "
					+ "called more then one time! This action should "
					+ "be removed once the accuracy was reached!");
	}

	/**
	 * @param location
	 *            The {@link Location} object that was accurate enough
	 * @param a
	 *            the {@link ActionWaitForAccuracy} object which can be used to
	 *            remove it from the {@link EventListenerGroup} it was contained
	 *            in (e.g. the {@link EventManager#onLocationChangedAction})
	 */
	public abstract void minAccuracyReachedFirstTime(Location location,
			ActionWaitForAccuracy a);

	public View getView() {
		viewContainer = View.inflate(myContext,
				R.layout.action_wait_for_accuracy_view, null);
		accText = (TextView) viewContainer.findViewById(R.id.awfa_accText);
		warningText = (Button) viewContainer.findViewById(R.id.awfa_warning);
		warningText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GeoUtils.enableGPS(myContext)) {
					warningText.setVisibility(View.GONE);
				}

			}
		});

		ImageView i = (ImageView) viewContainer.findViewById(R.id.awfa_image);
		i.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSkipPositionDetectionDialog();
			}

		});

		steps = (ProgressBar) viewContainer.findViewById(R.id.awfa_steps);
		updateUI();
		return viewContainer;
	}

	private void showSkipPositionDetectionDialog() {
		final Dialog dialog = new Dialog(myContext);
		Button b = new Button(myContext);
		b.setText(TEXT_SKIP_ACCURACY_DETECTION);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callFirstTimeAccReachedIfNotYetCalled(GeoUtils
						.getCurrentLocation(myContext));
				dialog.dismiss();
			}
		});
		dialog.setContentView(b);
		dialog.setTitle(TEXT_DIALOG_TITLE);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void updateUI() {
		if (accText != null && steps != null && warningText != null)
			myContext.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					accText.setText(TEXT_POSITION_ACCURACY
							+ (int) (myMinAccuracy / myCurrentAccuracy * 100)
							+ "%");
					steps.setMax(myMaxPosUpdateCount);
					steps.setProgress(stepCounter);
					showWarningIfGPSOff();
				}

				private void showWarningIfGPSOff() {
					if (GeoUtils.isGPSDisabled(myContext)) {
						Log.d(LOG_TAG, "GPS disabled!");
						warningText.setVisibility(View.VISIBLE);
						// warningText.setText("WARNING: GPS disabled!");
						warningText.setText("Enable GPS");
					} else {
						Log.d(LOG_TAG, "GPS enabled!");
						warningText.setVisibility(View.GONE);
					}
				}
			});
	}

	private void hideUI() {
		if (viewContainer != null)
			myContext.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					viewContainer.setVisibility(View.GONE);
				}
			});
	}

}
