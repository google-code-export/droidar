package geo;

import gl.GLFactory;
import util.Log;

public interface EdgeListener {

	

	void addEdgeToGraph(GeoGraph graph, GeoObj startPoint, GeoObj endPoint);

}