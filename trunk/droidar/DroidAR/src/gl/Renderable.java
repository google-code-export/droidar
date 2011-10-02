package gl;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import worldData.Updateable;

/**
 * Use this interface for custom rendering
 * 
 * @author Spobo
 * 
 */
public interface Renderable {
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack);
}
