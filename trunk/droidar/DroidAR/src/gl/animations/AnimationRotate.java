package gl.animations;

import gl.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Obj;

/**
 * This animation rotates a {@link MeshComponent}
 * 
 * @author Spobo
 * 
 */
public class AnimationRotate implements Animation {

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
	public boolean update(float timeDelta, Obj obj, MeshComponent mesh) {
		if (angle > 360) {
			angle = 0;
		}
		angle = angle + timeDelta * speed;
		return true;
	}

	@Override
	public void setAnimationMatrix(GL10 gl, MeshComponent mesh) {
		gl.glRotatef(angle, rotVec.x, rotVec.y, rotVec.z);
	}

	@Override
	public Animation copy() {
		return new AnimationRotate(speed, rotVec);
	}

}
