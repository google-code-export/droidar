package geo;

public interface NodeListener {

	void addFirstNodeToGraph(GeoGraph graph, GeoObj objectToAdd);

	void addNodeToGraph(GeoGraph graph, GeoObj objectToAdd);

	void addLastNodeToGraph(GeoGraph graph, GeoObj objectToAdd);
}