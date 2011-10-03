package gl.animations;

import gl.MeshComponent;
import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.Vec;
import worldData.Obj;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;

/**
 * This animation rotates a {@link MeshComponent}
 * 
 * @author Spobo
 * 
 */
public class AnimationRotate implements RenderableEntity {

	private float angle = 0;
	private final float speed;
	private final Vec rotVec;

	/**
	 * @param speed
	 *            something around 30 to 100
	 * @param rotationVector
	 */
	public AnimationRotate(float speed, Vec rotationVector) {
		this.speed = speed;
		rotVec = rotationVector;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (angle > 360) {
			angle = 0;
		}
		angle = angle + timeDelta * speed;
		return true;
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		gl.glRotatef(angle, rotVec.x, rotVec.y, rotVec.z);
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
