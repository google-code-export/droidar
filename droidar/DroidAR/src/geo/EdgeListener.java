package geo;

import gl.GLFactory;
import android.util.Log;

public interface EdgeListener {

	

	void addEdgeToGraph(GeoGraph graph, GeoObj startPoint, GeoObj endPoint);

}