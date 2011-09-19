package worldData;

import gl.CordinateAxis;
import gl.GLCamera;

import javax.microedition.khronos.opengles.GL10;

import util.EfficientList;
import util.Vec;
import android.util.Log;

//TODO not the best way to extend ArrayList here..
public class World implements Updateable, Renderable {

	private static final String LTAG = "World";
	/**
	 * think of this as the position on the screen
	 */
	private Vec myScreenPosition;
	// private Vec myRotation;
	/**
	 * think of this as the scale of the whole world on the screen
	 */
	private Vec myScale;

	EfficientList<AbstractObj> container;

	/**
	 * the camera which is responsible to display the world correctly
	 */
	private GLCamera myCamera;

	public World(GLCamera glCamera) {
		myCamera = glCamera;
	}

	public boolean add(AbstractObj x) {
		if (x == null) {
			return false;
		}
		if (container == null)
			container = new EfficientList<AbstractObj>();
		/*
		 * check if obj already added before adding it to the world!
		 */
		if (container.contains(x) != -1) {
			Log.e(LTAG, "Object " + x + " already contained in this world!");
			return false;
		}
		Log.v(LTAG, "Adding " + x + " to " + this);
		return container.add(x);
	}

	private void glLoadScreenPosition(GL10 gl) {
		if (myScreenPosition != null)
			gl.glTranslatef(myScreenPosition.x, myScreenPosition.y,
					myScreenPosition.z);
	}

	// private void glLoadRotation(GL10 gl) {
	// if (myRotation != null) {
	// // see MeshComponent and GLCamera for more infos why this order is
	// // important:
	// gl.glRotatef(myRotation.z, 0, 0, 1);
	// gl.glRotatef(myRotation.x, 1, 0, 0);
	// gl.glRotatef(myRotation.y, 0, 1, 0);
	// }
	// }

	public boolean accept(Visitor v) {
		return v.default_visit(this);
	}

	private void glLoadScale(GL10 gl) {
		if (myScale != null)
			gl.glScalef(myScale.x, myScale.y, myScale.z);
	}

	public void draw(GL10 gl) {
		// TODO reconstruct why this order is important! or wrong..
		glLoadScreenPosition(gl);
		myCamera.glLoadCamera(gl);
		// glLoadRotation(gl);
		glLoadScale(gl);

		// TODO remove the coordinate axes here:

		CordinateAxis.draw(gl);

		drawElements(gl);

	}

	private void drawElements(GL10 gl) {
		if (container != null) {
			for (int i = 0; i < container.myLength; i++) {
				if (container.get(i) != null)
					container.get(i).draw(gl);
			}
		}
	}

	public boolean update(float timeDelta) {
		myCamera.update(timeDelta);
		if (container != null) {
			for (int i = 0; i < container.myLength; i++) {
				container.get(i).update(timeDelta);
			}
		}
		return true;
	}

	// private void showArrayPos(final Object[] array, int i) {
	// try {
	// Log.e(LTAG, array.toString() + "[" + i + "]=" + array[i]);
	// } catch (Exception e1) {
	// Log.e(LTAG, array.toString() + "[" + i + "]=ERROR (out of bounds)");
	// }
	// }

	public GLCamera getMyCamera() {
		return myCamera;
	}

	public void setMyScreenPosition(Vec myScreenPosition) {
		this.myScreenPosition = myScreenPosition;
	}

	// public void setMyRotation(Vec myRotation) {
	// this.myRotation = myRotation;
	// }

	public void setMyScale(Vec myScale) {
		this.myScale = myScale;
	}

	public void setMyCamera(GLCamera myCamera) {
		this.myCamera = myCamera;
	}

	public EfficientList<AbstractObj> getAllItems() {
		return container;
	}

}
