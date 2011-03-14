package components;

import worldData.Obj;
import worldData.Visitor;

public interface Component {

	/**
	 * @param timeDelta
	 *            on default values will be around 0.020 to 0.025. this value
	 *            depents manly on the {@link WordUpdater}.GAME_THREAD_DELAY
	 *            constant but also on the device processor
	 * @param obj
	 */
	public void update(float timeDelta, Obj obj);

	/**
	 * Insert point for any {@link Visitor}
	 * 
	 * @param visitor
	 * @return this return value can be processed by the custom {@link Visitor}
	 *         so its up to you as the developer what to return and if you need
	 *         this returned value
	 */
	public boolean accept(Visitor visitor);

}
