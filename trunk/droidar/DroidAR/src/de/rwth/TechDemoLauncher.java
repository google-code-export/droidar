package de.rwth;

import system.ArActivity;
import system.ErrorHandler;
import system.Setup;
import tests.SimpleTesting;
import worldData.LargeWorld;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import commands.ui.CommandShowToast;

import de.rwth.setups.CollectItemsSetup;
import de.rwth.setups.DebugSetup;
import de.rwth.setups.GraphMovementTestSetup;
import de.rwth.setups.IndoorSetup;
import de.rwth.setups.LargeWorldsSetup;
import de.rwth.setups.LightningSetup;
import de.rwth.setups.PlaceObjectsSetup;
import de.rwth.setups.PlaceObjectsSetupTwo;
import de.rwth.setups.PositionTestsSetup;
import de.rwth.setups.SensorTestSetup;

public class TechDemoLauncher extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.demoselector);

		
		showSetup("Animation Demo", new DebugSetup());
		showSetup("Large worlds", new LargeWorldsSetup());
		showSetup("Lightning Demo", new LightningSetup());
		showSetup("Collecting Items Demo", new CollectItemsSetup());
		showSetup("Placing objects Demo", new PlaceObjectsSetup());
		showSetup("Placing objects Demo 2", new PlaceObjectsSetupTwo());
		showSetup("Graph Movement Test", new GraphMovementTestSetup());
		showSetup("Sensor Processing Demo", new SensorTestSetup());
		showSetup("Position tests", new PositionTestsSetup());

		LinearLayout l = ((LinearLayout) findViewById(R.id.demoScreenLinView));

		l.addView(new SimpleButton("Run tests") {
			public void onButtonPressed() {
				runTests();
			}
		});

		showSetup("Indoor Navigator (Needs special localization service!)",
				new IndoorSetup());

	}

	private void showSetup(String string, final Setup aSetupInstance) {
		((LinearLayout) findViewById(R.id.demoScreenLinView))
				.addView(new SimpleButton(string) {
					public void onButtonPressed() {
						Activity theCurrentActivity = TechDemoLauncher.this;
						ArActivity.startWithSetup(theCurrentActivity,
								aSetupInstance);
					}
				});
	}

	private abstract class SimpleButton extends Button {
		public SimpleButton(String text) {
			super(TechDemoLauncher.this);
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

	private void runTests() {
		// execute all tests defined in the ARTestSuite:
		try {
			SimpleTesting.runAllTests(this);
			new CommandShowToast(this, "All tests succeded on this device :)")
					.execute();
		} catch (Exception e) {
			ErrorHandler.showErrorLog(this, e, true);
		}
	}

}
