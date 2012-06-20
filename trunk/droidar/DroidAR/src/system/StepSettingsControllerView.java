package system;

import android.content.Context;
import android.view.View;
import gui.v2.simpleUi.M_Container;
import gui.v2.simpleUi.M_Double;
import gui.v2.simpleUi.M_Integer;
import gui.v2.simpleUi.ModifierInterface;

public class StepSettingsControllerView extends M_Container {

	public StepSettingsControllerView(Context context) {
		this.add(new M_Double() {

			@Override
			public boolean save(double newValue) {
				SimpleLocationManager
						.setMinimumAverageAccuracy((float) newValue);
				return true;
			}

			@Override
			public double load() {
				return SimpleLocationManager.getMinimumAverageAccuracy();
			}

			@Override
			public String getVarName() {
				return "MinimumAverageAccuracy";
			}
		});
		this.add(new M_Integer() {

			@Override
			public boolean save(int newValue) {
				SimpleLocationManager
						.setNumberOfSimulatedStepsInSameDirection(newValue);
				return true;
			}

			@Override
			public int load() {
				return SimpleLocationManager
						.getNumberOfSimulatedStepsInSameDirection();
			}

			@Override
			public String getVarName() {
				return "NumberOfSimulatedStepsInSameDirection";
			}
		});
		final StepManager sm = SimpleLocationManager.getInstance(context)
				.getStepManager();
		if (sm != null) {

			this.add(new M_Double() {

				@Override
				public boolean save(double newValue) {
					sm.setMinStepPeakSize(newValue);
					return true;
				}

				@Override
				public double load() {
					return sm.getMinStepPeakSize();
				}

				@Override
				public String getVarName() {
					return "MinStepPeakSize";
				}
			});

			this.add(new M_Double() {

				@Override
				public boolean save(double newValue) {
					sm.setStepLengthInMeter(newValue);
					return true;
				}

				@Override
				public double load() {
					return sm.getStepLengthInMeter();
				}

				@Override
				public String getVarName() {
					return "StepLengthInMeter";
				}
			});
			this.add(new M_Integer() {

				@Override
				public boolean save(int newValue) {
					sm.setMinTimeBetweenSteps(newValue);
					return true;
				}

				@Override
				public int load() {
					return sm.getMinTimeBetweenSteps();
				}

				@Override
				public String getVarName() {
					return "MinTimeBetweenSteps";
				}
			});

		}
	}
}
