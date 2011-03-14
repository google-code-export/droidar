package gl;

import util.Vec;
import worldData.AbstractObj;

public interface ParentMesh {

	public ParentMesh getMyParentMesh();

	public AbstractObj getMyParentObj();

	public Vec getAbsolutePosition();

}
