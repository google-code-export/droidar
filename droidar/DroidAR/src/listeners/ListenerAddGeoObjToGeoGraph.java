package listeners;

import geo.GeoGraph;
import geo.GeoObj;

public interface ListenerAddGeoObjToGeoGraph {

	/**
	 * here you have to add the object to the graph and return true if it worked
	 * correctly
	 * 
	 * @param graph
	 *            the graph where the object should be added
	 * @param obj
	 *            the object you should add to the graph
	 * @return true if the obj was added to the graph
	 */
	boolean onAddToGraphEvent(GeoGraph graph, GeoObj obj);

}
