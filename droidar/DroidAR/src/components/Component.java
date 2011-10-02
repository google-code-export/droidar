package components;

import worldData.Updateable;
import worldData.Visitor;

public interface Component extends Updateable {

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
