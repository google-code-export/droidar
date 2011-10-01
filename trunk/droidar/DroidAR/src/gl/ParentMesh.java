package gl;

import util.Vec;
import worldData.AbstractObj;

@Deprecated
public interface ParentMesh {

	public ParentMesh getMyParentMesh();

	public AbstractObj getMyParentObj();

	public Vec getAbsolutePosition();

}
