package scripts;

import worldData.Obj;

public interface TimeStepListener {

	/**
	 * @param timeDelta
	 * @param obj
	 *            the object the script component, which contains this script,
	 *            belongs to
	 */
	public void timeStep(float timeDelta, Obj obj);

}
