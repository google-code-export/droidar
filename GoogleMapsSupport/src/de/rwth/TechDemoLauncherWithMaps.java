package de.rwth;

import system.ErrorHandler;
import system.Setup;
import tests.SimpleTesting;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import commands.ui.CommandShowToast;

import de.rwth.setups.ARNavigatorSetup;

public class TechDemoLauncherWithMaps extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.demoselector);

		// showSetup("With calibration dialogs", new CalibratorSetup());

		showSetup("AR Navigator", new ARNavigatorSetup());

		// showSetup("Animation Demo", new DebugSetup());
		// showSetup("Collecting Items Demo", new CollectItemsSetup());
		// showSetup("Placing objects Demo", new PlaceObjectsSetup());
		// showSetup("Sensor Processing Demo", new SensorTestSetup());
		// showSetup("Position tests", new PositionTestsSetup());

		LinearLayout l = ((LinearLayout) findViewById(R.id.demoScreenLinView));

		// showSetup("Indoor Navigator (Needs special localization service!)",
		// new IndoorSetup());

	}

	private void showSetup(String string, final Setup aSetupInstance) {
		((LinearLayout) findViewById(R.id.demoScreenLinView))
				.addView(new SimpleButton(string) {
					@Override
					public void onButtonPressed() {
						Activity theCurrentActivity = TechDemoLauncherWithMaps.this;
						ARActivityPlusMaps.startWithSetup(theCurrentActivity,
								aSetupInstance);
					}
				});
	}

	private abstract class SimpleButton extends Button {
		public SimpleButton(String text) {
			super(TechDemoLauncherWithMaps.this);
			setText(text);
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onButtonPressed();
				}
			});
		}

		public abstract void onButtonPressed();
	}

}
