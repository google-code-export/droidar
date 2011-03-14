package geo;

import gl.GLFactory;
import gl.MeshComponent;

public class Edge extends GeoObj implements Comparable<Edge> {

	final GeoObj from, to;
	final int weight;

	// private Color myColor;

	public Edge(final GeoObj from, final GeoObj to, MeshComponent edgeMesh) {
		super(from, null);
		this.from = from;
		this.to = to;
		weight = (int) from.getDistance(to);
		this.setComp(edgeMesh);
	}

	public int compareTo(final Edge argEdge) {
		return weight - argEdge.weight;
	}

	public static MeshComponent getDefaultMesh(GeoGraph geoGraph, GeoObj from,
			GeoObj to) {

		if (geoGraph.isNonDirectional())
			return GLFactory.getInstance().newUndirectedPath(from, to,
					geoGraph.getInfoObject().getColor());

		return GLFactory.getInstance().newDirectedPath(from,to,
				geoGraph.getInfoObject().getColor());

	}

}
