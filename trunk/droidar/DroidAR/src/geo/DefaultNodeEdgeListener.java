package geo;

import gl.GLCamera;
import gl.GLFactory;
import gl.scenegraph.MeshComponent;

public class DefaultNodeEdgeListener extends SimpleNodeEdgeListener {

	public DefaultNodeEdgeListener(GLCamera glCamera) {
		super(glCamera);
	}

	public MeshComponent getEdgeMesh(GeoGraph targetGraph, GeoObj startPoint,
			GeoObj endPoint) {
		return Edge.getDefaultMesh(targetGraph, startPoint, endPoint, null);
	}

	public MeshComponent getNodeMesh() {
		return GLFactory.getInstance().newDiamond(null);
	}

}