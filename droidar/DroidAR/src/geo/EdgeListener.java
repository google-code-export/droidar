package geo;

import android.util.Log;

public interface EdgeListener {

	public class DefaultEdgeListener implements EdgeListener {

		private static final String LOG_TAG = "EdgeListener";

		@Override
		public void addEdgeToGraph(GeoGraph targetGraph, GeoObj startPoint,
				GeoObj endPoint) {

			// add an edge:
			if (startPoint != null) {
				targetGraph.addEdge(startPoint, endPoint, null);
			}

		}

	}

	void addEdgeToGraph(GeoGraph graph, GeoObj startPoint, GeoObj endPoint);

}