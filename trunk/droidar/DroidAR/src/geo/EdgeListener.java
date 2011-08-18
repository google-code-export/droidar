package geo;

public interface EdgeListener {

	public class DefaultEdgeListener implements EdgeListener {

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