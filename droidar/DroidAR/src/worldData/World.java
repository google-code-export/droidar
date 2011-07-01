package worldData;

import gl.CordinateAxis;
import gl.GLCamera;

import javax.microedition.khronos.opengles.GL10;

import util.EfficientList;
import util.Vec;
import android.util.Log;

//TODO not the best way to extend ArrayList here..
public class World extends EfficientList<AbstractObj> implements Updateable,
		Renderable {

	private static final String LTAG = "World";
	/**
	 * think of this as the position on the screen
	 */
	private Vec myScreenPosition;
	private Vec myRotation;
	/**
	 * think of this as the scale of the whole world on the screen
	 */
	private Vec myScale;

	/**
	 * the camera which is responsible to display the world correctly
	 */
	private GLCamera myCamera;

	public World(GLCamera glCamera) {
		myCamera = glCamera;
	}

	@Override
	public boolean add(AbstractObj x) {
		if (x == null) {
			return false;
		}
		/*
		 * check if obj already added before adding it to the world!
		 */
		if (contains(x) != -1) {
			Log.e(LTAG, "Object " + x + " already contained in this world!");
			return false;
		}
		Log.v(LTAG, "Adding " + x + " to " + this);
		return super.add(x);
	}

	private void glLoadScreenPosition(GL10 gl) {
		if (myScreenPosition != null)
			gl.glTranslatef(myScreenPosition.x, myScreenPosition.y,
					myScreenPosition.z);
	}

	private void glLoadRotation(GL10 gl) {
		if (myRotation != null) {
			// see MeshComponent and GLCamera for more infos why this order is
			// important:
			gl.glRotatef(myRotation.z, 0, 0, 1);
			gl.glRotatef(myRotation.x, 1, 0, 0);
			gl.glRotatef(myRotation.y, 0, 1, 0);
		}
	}

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
		glLoadRotation(gl);
		glLoadScale(gl);

		// TODO remove the coordinate axes here:
		
		CordinateAxis.draw(gl);
		

		for (int i = 0; i < this.myLength; i++) {
			// try {
			if (get(i) != null)
				get(i).draw(gl);
			// } catch (Exception e) {
			// Log.e(LTAG, "Tried to draw pos=" + i + ", myLength=" + myLength);
			// showArrayPos(worldObjects, i - 1);
			// showArrayPos(worldObjects, i);
			// showArrayPos(worldObjects, i + 1);
			// Log.e(LTAG, "worldObjects " + arrayToString(myArray, myLength));
			// e.printStackTrace();
			// }
		}

	}

	public boolean update(float timeDelta) {
		myCamera.update(timeDelta);

		for (int i = 0; i < this.myLength; i++) {
			// try {
			get(i).update(timeDelta);
			// } catch (Exception e) {
			// Log.e(LTAG, "Tried to update i=" + i + ", myLength=" + myLength);
			// showArrayPos(array, i - 1);
			// showArrayPos(array, i);
			// showArrayPos(array, i + 1);
			// Log.e(LTAG, arrayToString(myArray, myLength));
			// e.printStackTrace();
			// }
		}
		return true;
	}

	private void showArrayPos(final Object[] array, int i) {
		try {
			Log.e(LTAG, array.toString() + "[" + i + "]=" + array[i]);
		} catch (Exception e1) {
			Log.e(LTAG, array.toString() + "[" + i + "]=ERROR (out of bounds)");
		}
	}

	public GLCamera getMyCamera() {
		return myCamera;
	}

	public void setMyScreenPosition(Vec myScreenPosition) {
		this.myScreenPosition = myScreenPosition;
	}

	public void setMyRotation(Vec myRotation) {
		this.myRotation = myRotation;
	}

	public void setMyScale(Vec myScale) {
		this.myScale = myScale;
	}

	public void setMyCamera(GLCamera myCamera) {
		this.myCamera = myCamera;
	}

}
