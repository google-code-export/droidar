package geo;

public interface NodeListener {

	public class DefaultNodeListener implements NodeListener {

		@Override
		public void addNodeToGraph(GeoGraph targetGraph, GeoObj newNode) {
			targetGraph.add(newNode);
		}

	}

	void addNodeToGraph(GeoGraph graph, GeoObj objectToAdd);

}