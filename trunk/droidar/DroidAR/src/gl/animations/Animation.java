package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import worldData.Obj;

public interface Animation {

	public void setAnimationMatrix(GL10 gl, MeshComponent mesh);

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
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh);

	/**
	 * The copy method can be used to copy the animation object. this is needed
	 * sometimes because if you always pass the object itself it will always be
	 * the same object. and if you only want an object that behaves in the same
	 * way but is a second object that can be changed without affecting the
	 * original object use copy(). the method has to be implemented by each
	 * animation and there you just have to return a new animation with the same
	 * values
	 * 
	 * @return
	 */
	public Animation copy();

}
