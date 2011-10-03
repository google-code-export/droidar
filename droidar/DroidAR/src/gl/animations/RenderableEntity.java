package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;

public interface RenderableEntity {

	public abstract void setAnimationMatrix(GL10 gl, MeshComponent mesh);

	/**
	 * called from the game thread to update the animation object
	 * 
	 * @param timeDelta
	 * @param obj
	 *            the object that owns the animation component where this
	 *            animation lies in
	 * @param mesh
	 *            the {@link MeshComponent} where the {@link Animation} belongs
	 *            to
	 * @return true if the animation isn't finished yet
	 */
	public abstract boolean update(float timeDelta, Obj obj, MeshComponent mesh);

}