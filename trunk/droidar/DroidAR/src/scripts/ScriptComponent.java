package scripts;

import util.EfficientList;
import worldData.Obj;
import worldData.Visitor;

import components.Component;

public class ScriptComponent implements Component {

	EfficientList<TimeStepListener> myScripts = new EfficientList<TimeStepListener>();

	public void add(TimeStepListener script) {
		myScripts.add(script);
	}

	public void update(float timeDelta, Obj obj) {
		final int lenght = myScripts.myLength;
		for (int i = 0; i < lenght; i++) {
			myScripts.get(i).timeStep(timeDelta, obj);
		}
	}

	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
