package gl;

import geo.GeoObj;

import javax.microedition.khronos.opengles.GL10;

import listeners.EventListener;
import system.EventManager;
import util.HasDebugInformation;
import util.L;
import util.Vec;
import worldData.Updateable;
import actions.ActionRotateCameraBuffered;
import actions.DefaultUpdateListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.Matrix;
import android.util.Log;

/**
 * This is the virtual camera needed to display a virtual world. The 3 important
 * properties you might want to change manually are its position, rotation and
 * offset. Do this via {@link GLCamera#setNewPosition(Vec)},
 * {@link GLCamera#setNewRotation(Vec)} and {@link GLCamera#setNewOffset(Vec)}
 * 
 * @author Spobo
 * 
 */
public class GLCamera implements Updateable, HasDebugInformation {

	public interface CameraAngleUpdateListener {

		/**
		 * @param myAnglesInRadians
		 *            These values are calculated with
		 *            {@link SensorManager#getOrientation(float[], float[])} and
		 *            they represent the rotation caused by the device sensors
		 * @param myRotationVec
		 *            These values are additional rotation values which might be
		 *            set by the developer/user so they have to be considered
		 *            even if they are normally 0.
		 */
		void updateAnglesByCamera(float[] myAnglesInRadians, Vec myRotationVec);

	}

	private static final String LOG_TAG = "GLCamera";

	// private float mybufferValue = 1000;
	// private float minimumBufferValue = 20;
	// private float bufferCount = 20;

	private Vec myOffset = null;
	private Vec myNewOffset = new Vec(0, 0, 0);
	private Vec myPosition = new Vec(0, 0, 0);

	// TODO would be dangerous to set any of those vecs to null because there
	// might be references in commands to those objects so check where those are
	// set to null!
	private Vec myNewPosition = new Vec(0, 0, 0);

	/**
	 * to move from the green to the red axis (clockwise) you would have to add
	 * 90 degree
	 * 
	 * y is rotation around green achsis counterclockwise
	 * 
	 * TODO complete this description
	 */
	public Vec myRotationVec = new Vec(0, 0, 0);
	public Vec myNewRotationVec;

	/**
	 * set to false if you want the camera not to react on sensor events
	 */
	public boolean sensorInputEnabled = true;
	/**
	 * this enshures that the rotation matrix is only recalculated if the
	 * acceleration or magnetometer values have changed
	 */
	private boolean accelOrMagChanged = false;
	/**
	 * same as for {@link GLCamera#accelOrMagChanged} only for the orientation
	 * values
	 */
	private boolean orientationValuesChanged = false;

	private float[] myAccelValues = new float[3];
	private float[] myMagnetValues = new float[3];
	private float[] myOrientValues;
	private float[] myNewAccelValues;
	private float[] myNewMagnetValues;
	// private float[] myNewOrientValues;

	private float[] unrotatedMatrix = createIdentityMatrix();

	/**
	 * http://www.songho.ca/opengl/gl_transform.html
	 */
	private float[] rotationMatrix = createIdentityMatrix();
	private float[] invRotMatrix = createIdentityMatrix();

	private int matrixOffset = 0;

	private MeshComponent myCameraObject;
	private EventListener updateListener;

	/**
	 * this can be used to to extract the angles how the camera is held
	 */
	private CameraAngleUpdateListener myAngleUpdateListener;
	/**
	 * the camera rotation angles extracted from the rotation matrix. These
	 * values will only be calculated if an angleUpdateListener is set or
	 * {@link GLCamera#forceAngleCalculation} is set to true
	 * 
	 */
	public float[] myAnglesInRadians = new float[3];
	public boolean forceAngleCalculation = false;

	public GLCamera() {
	}

	public GLCamera(Vec initialCameraPosition) {
		setNewPosition(initialCameraPosition);
	}

	public void setUpdateListener(EventListener updateListener) {
		this.updateListener = updateListener;
	}

	public EventListener getUpdateListener() {
		return updateListener;
	}

	public static float[] createIdentityMatrix() {
		float[] result = new float[16];
		result[0] = 1;
		result[5] = 1;
		result[10] = 1;
		result[15] = 1;
		return result;
	}

	public boolean update(float timeDelta) {

		if (updateListener == null) {
			Log.w(LOG_TAG,
					"There where no updateListener set! Using default one..");
			updateListener = new DefaultUpdateListener();
		}

		if ((myRotationVec != null) && (myNewRotationVec != null)) {
			updateListener.onCamRotationVecUpdate(myRotationVec,
					myNewRotationVec, timeDelta);
		}

		/*
		 * TODO if you change the camera rotation type from buffered to
		 * unbuffered on runtime, then the myNewRotation vec wouldnt be null and
		 * the camera would always try to rotate to the newRotation, so you have
		 * to reset it to null or rework this method..
		 */

		if ((myNewAccelValues != null) && (myNewMagnetValues != null)
				&& sensorInputEnabled) {
			accelOrMagChanged = updateListener.onCamAccelerationUpdate(
					myAccelValues, myNewAccelValues, timeDelta);
			accelOrMagChanged |= updateListener.onCamMagnetometerUpdate(
					myMagnetValues, myNewMagnetValues, timeDelta);
		}

		// if (myNewOrientValues != null) {
		// orientationValuesChanged = updateListener.onCamOrientationUpdate(
		// myOrientValues, myNewOrientValues, timeDelta);
		// }

		if ((myOffset != null) && (myNewOffset != null)) {
			updateListener.onCamOffsetVecUpdate(myOffset, myNewOffset,
					timeDelta);

		}

		if ((myPosition != null) && (myNewPosition != null)) {
			updateListener.onCamPositionVecUpdate(myPosition, myNewPosition,
					timeDelta);

			// TODO check if myPosition and myNewPosition are nearly the
			// same
			// and then setMyNewPos=null
		}

		return true;

	}

	public void setNewPosition(Vec cameraPosition) {
		if (myNewPosition == null) {
			myNewPosition = new Vec(cameraPosition);
			if (myPosition == null) {
				myPosition = new Vec();
			}
		} else {
			myNewPosition.setToVec(cameraPosition);
		}
	}

	public void setNewCameraOffset(Vec newCameraOffset) {
		if (newCameraOffset != null) {
			if (myNewOffset == null) {
				myNewOffset = new Vec(newCameraOffset);
				if (myOffset == null) {
					myOffset = new Vec();
				}
			} else {
				myNewOffset.setToVec(newCameraOffset);
			}
		}
	}

	public void setNewRotation(Vec cameraRotation) {
		if (cameraRotation != null) {
			if (myNewRotationVec == null) {
				myNewRotationVec = new Vec(cameraRotation);
			} else {
				myNewRotationVec.setToVec(cameraRotation);
			}
		}
	}

	/**
	 * @param rayPosition
	 *            the vector where the ray pos will be stored in, so pass a
	 *            vector here that can be overwritten. Normally this value will
	 *            be the same as {@link GLCamera#myPosition} but if a marker is
	 *            used to move the {@link GLCamera} the translation will be
	 *            contained in the matrix as well and therefore the rayPosition
	 *            will be this translation in relation to the marker
	 * @param rayDirection
	 *            the vector where the ray direction will be stored in, so pass
	 *            a vector here that can be overwritten (don't pass null!)
	 * @param x
	 *            the horizontal screen-coordinates (from 0 to screen-width)
	 * @param y
	 *            the vertical screen-coordinates (from 0 to screen-height).
	 *            Just pass the value you get from the Android onClick event
	 */
	public void getPickingRay(Vec rayPosition, Vec rayDirection, float x,
			float y) {

		if (rayDirection == null) {
			Log.e(LOG_TAG, "Passed direction vector object was null");
			return;
		}

		// convert to opengl screen coords:
		x = (x - GLRenderer.halfWidth) / GLRenderer.halfWidth;
		y = (GLRenderer.height - y - GLRenderer.halfHeight)
				/ GLRenderer.halfHeight;

		Matrix.invertM(invRotMatrix, 0, rotationMatrix, matrixOffset);

		if (rayPosition != null) {
			float[] rayPos = new float[4];
			float[] initPos = { 0.0f, 0.0f, 0.0f, 1.0f };
			Matrix.multiplyMV(rayPos, 0, invRotMatrix, 0, initPos, 0);
			rayPosition.x = rayPos[0];
			rayPosition.y = rayPos[1];
			rayPosition.z = rayPos[2];
			if (myPosition != null) {
				rayPosition.add(myPosition);
			}
		}
		float[] rayDir = new float[4];
		float[] initDir = { x * GLRenderer.nearHeight * GLRenderer.aspectRatio,
				y * GLRenderer.nearHeight, -GLRenderer.minViewDistance, 0.0f };
		Matrix.multiplyMV(rayDir, 0, invRotMatrix, 0, initDir, 0);
		rayDirection.x = rayDir[0];
		rayDirection.y = rayDir[1];
		rayDirection.z = rayDir[2];
	}

	public int getMatrixOffset() {
		return matrixOffset;
	}

	/**
	 * "Ground" means the plane where z is 0
	 * 
	 * Nearly the same code as
	 * {@link GLCamera#getPickingRay(Vec, Vec, float, float)} just a little bit
	 * optimized
	 * 
	 * @return
	 */
	public Vec getPositionOnGroundWhereTheCameraIsLookingAt() {
		Matrix.invertM(invRotMatrix, 0, rotationMatrix, matrixOffset);

		/*
		 * This is an optimized version of the getPickingRay method. The good
		 * readable code would look like this:
		 * 
		 * Vec pos = new Vec(); Vec dir = new Vec();
		 * 
		 * camera.getPickingRay(pos, dir, GLRenderer.halfWidth,
		 * GLRenderer.halfHeight);
		 * 
		 * now the calculation where the direction vec hits the ground plane.
		 * can be reduced to intersection of two lines where only the z values
		 * of start and direction are different
		 * 
		 * when you break down the intersection of two lines with nearly the
		 * same direction vectors and nearly the same start vectors then you get
		 * this:
		 * 
		 * dir.mult(-pos.z / dir.z);
		 * 
		 * dir.add(pos);
		 * 
		 * dir is the position on the ground which then could be returned
		 */

		float[] rayPos = new float[4];
		float[] initPos = { 0.0f, 0.0f, 0.0f, 1.0f };
		Matrix.multiplyMV(rayPos, 0, invRotMatrix, 0, initPos, 0);

		float[] rayDir = new float[4];
		float[] initDir = { 0, 0, -GLRenderer.minViewDistance, 0.0f };
		Matrix.multiplyMV(rayDir, 0, invRotMatrix, 0, initDir, 0);

		float f = -(rayPos[2] + myPosition.z) / rayDir[2];
		System.out.println(f);
		return new Vec((f * rayDir[0]) + (rayPos[0] + myPosition.x),
				(f * rayDir[1]) + (rayPos[1] + myPosition.y), 0);
	}

	/**
	 * This method will be called by the virtual world to load the camera
	 * parameters like the position and the rotation
	 * 
	 * @param gl
	 */
	public synchronized void glLoadCamera(GL10 gl) {
		// a camera object could be a curser or something like this: TODO use?
		// if (myCameraObject!=null) loadCameraObject(gl);

		// if the camera sould not be in the center of the rotation it has to be
		// moved out before rotating:
		glLoadPosition(gl, myOffset);

		glLoadRotationMatrix(gl);

		// rotate Camera TODO use for manual rotation:
		glLoadRotation(gl, myRotationVec);

		// set the point where to rotate around
		glLoadPosition(gl, myPosition);
	}

	public void setRotationMatrixFromMarkerInput(float[] rotMatrix, int offset) {
		rotationMatrix = rotMatrix;
		matrixOffset = offset;

		// if (myPosition == null)
		// myPosition = new Vec();
		// myPosition.x = rotMatrix[12 + offset];
		// myPosition.y = rotMatrix[13 + offset];
		// myPosition.z = rotMatrix[14 + offset];
		// System.out.println(toString(rotMatrix, offset, 16));
	}

	// private static String toString(float[] rotMatrix, int offset, int length)
	// {
	// String s = "";
	// for (int i = 0; i < length; i++) {
	// s += rotMatrix[i + offset] + "  ";
	// }
	// return s;
	// }

	private synchronized void glLoadRotationMatrix(GL10 gl) {
		if (sensorInputEnabled) {
			if (accelOrMagChanged) {
				udateRotationMatrixFromAccelAndMagnetSensorValues();
			} else if (orientationValuesChanged) {
				udateRotationMatrixFromOrientationSensorValues();
			}
		}

		gl.glMultMatrixf(rotationMatrix, matrixOffset);

	}

	private void udateRotationMatrixFromAccelAndMagnetSensorValues() {
		// first calc the unrotated matrix:
		SensorManager.getRotationMatrix(unrotatedMatrix, null, myAccelValues,
				myMagnetValues);

		// showMatrix("magAccel", unrotatedMatrix);

		// then rotate it according to the screen rotation:
		SensorManager.remapCoordinateSystem(unrotatedMatrix,
				SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
				rotationMatrix);

		updateCameraAnglesIfNeeded();

		accelOrMagChanged = false;
		matrixOffset = 0;
	}

	private void udateRotationMatrixFromOrientationSensorValues() {

		// first calc the unrotated matrix:
		GLUtilityClass.getRotationMatrixFromVector(unrotatedMatrix,
				myOrientValues);

		// showMatrix("orientData", unrotatedMatrix);

		// then rotate it according to the screen rotation:
		SensorManager.remapCoordinateSystem(unrotatedMatrix,
				SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
				rotationMatrix);

		updateCameraAnglesIfNeeded();

		orientationValuesChanged = false;
		matrixOffset = 0;
	}

	private void showMatrix(String string, float[] m) {
		System.out.println(string);
		System.out.println("" + m[0] + ", >" + m[1] + ", >" + m[2] + ", >"
				+ m[3]);
		System.out.println("" + m[4] + ", >" + m[5] + ", >" + m[6] + ", >"
				+ m[7]);
		System.out.println("" + m[8] + ", >" + m[9] + ", >" + m[10] + ", >"
				+ m[11]);
		System.out.println("" + m[12] + ", >" + m[13] + ", >" + m[14] + ", >"
				+ m[15]);

	}

	private void updateCameraAnglesIfNeeded() {
		if (myAngleUpdateListener != null) {
			SensorManager.getOrientation(rotationMatrix, myAnglesInRadians);
			myAngleUpdateListener.updateAnglesByCamera(myAnglesInRadians,
					myRotationVec);
		} else if (forceAngleCalculation) {
			SensorManager.getOrientation(rotationMatrix, myAnglesInRadians);
		}
	}

	/**
	 * The alternative to this way of extracting the values is to use the view
	 * matrix directly(via {@link GLCamera#getRotationMatrix()}. There are
	 * several helper methods available to extract the rotation etc (eg
	 * {@link GLCamera#getPickingRay(Vec, Vec, float, float)}
	 * 
	 * @param myAngleUpdateListener
	 */
	public void setAngleUpdateListener(
			CameraAngleUpdateListener myAngleUpdateListener) {
		this.myAngleUpdateListener = myAngleUpdateListener;
	}

	/**
	 * TODO the Camera object will be drawn before any transformations are
	 * applied. Because of this it will always be at a fixed position on the
	 * screen. Can be used for UI elements eg. or to simulate something like a
	 * cockpit.
	 * 
	 * @param gl
	 */
	private void loadCameraObject(GL10 gl) {
		myCameraObject.setMatrixAndDraw(gl);
	}

	private void glLoadPosition(GL10 gl, Vec vec) {
		if (vec != null)
			// if you want to set the center to 0 0 5 you have to move the
			// camera -5 units OUT of the screen
			gl.glTranslatef(-vec.x, -vec.y, -vec.z);
	}

	private void glLoadRotation(GL10 gl, Vec vec) {
		/*
		 * a very important point is that its something completely different
		 * when you change the rotation order to x y z ! the order y x z is
		 * needed to use extract the angles from the rotation matrix with:
		 * 
		 * SensorManager.getOrientation(rotationMatrix, anglesInRadians);
		 * 
		 * so remember this oder when doing own rotations.
		 * 
		 * TODO find out why y is always the angle from floot to top and x
		 * always the clockwise rotation angle of the camera. does this make
		 * sense for example when y=0 and x=90 the camera is rotated clockwise
		 * and doesnt look at the horizon is this correct?
		 */
		if (vec != null) {
			gl.glRotatef(vec.y, 0, 1, 0);
			gl.glRotatef(vec.x, 1, 0, 0);
			gl.glRotatef(vec.z, 0, 0, 1);
		}
	}

	public synchronized void setAccelValuesBuffered(float[] newValues) {
		if (myNewAccelValues == null)
			myNewAccelValues = new float[3];
		myNewAccelValues[0] = newValues[0];
		myNewAccelValues[1] = newValues[1];
		myNewAccelValues[2] = newValues[2];
	}

	public synchronized void setAccelValueBuffered(float a, float b, float c) {
		if (myNewAccelValues == null)
			myNewAccelValues = new float[3];
		myNewAccelValues[0] = a;
		myNewAccelValues[1] = b;
		myNewAccelValues[2] = c;
	}

	public synchronized void setMagnetValuesBuffered(float[] newValues) {
		if (myNewMagnetValues == null)
			myNewMagnetValues = new float[3];
		myNewMagnetValues[0] = newValues[0];
		myNewMagnetValues[1] = newValues[1];
		myNewMagnetValues[2] = newValues[2];
	}

	public synchronized void setMagnetValuesBuffered(float a, float b, float c) {
		if (myNewMagnetValues == null)
			myNewMagnetValues = new float[3];
		myNewMagnetValues[0] = a;
		myNewMagnetValues[1] = b;
		myNewMagnetValues[2] = c;
	}

	/**
	 * This will change the rotation vector instantly. if you want to use a
	 * buffered smooth rotation you have to use
	 * {@link GLCamera#setNewRotation(Vec)}
	 * 
	 * @param xAngle
	 * @param yAngle
	 * @param zAngle
	 */
	public void setRotation(float xAngle, float yAngle, float zAngle) {
		myRotationVec.x = xAngle;
		myRotationVec.y = yAngle;
		myRotationVec.z = zAngle;
	}

	/**
	 * change camera position relative to the actual camera rotation around the
	 * z axis, so the the camera is moved along the camera coordinate system and
	 * not the world coordinate system
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public synchronized void changeXYPositionBuffered(float deltaX, float deltaY) {
		myNewPosition.x += deltaX;
		myNewPosition.y += deltaY;
	}

	/**
	 * 
	 * This will change the x and y position values instantly by
	 * adding/subtracting the passed values! If you want a smooth buffered
	 * movement to the new position, use
	 * {@link GLCamera#setNewPosition(float, float)}
	 * 
	 * @param deltaX
	 *            its important that this is not the absolute value. Its only
	 *            the value wich will be added/subtracted to the current one
	 * @param deltaY
	 *            see deltaX description
	 */
	public synchronized void changePositionUnbuffered(float deltaX, float deltaY) {
		myPosition.x += deltaX;
		myPosition.y += deltaY;
	}

	public synchronized boolean setAccelValues(float[] values) {
		if (values == null)
			return false;
		myNewAccelValues = null; // switches of update buffer method
		if (myAccelValues == null)
			myAccelValues = new float[3];
		myAccelValues[0] = values[0];
		myAccelValues[1] = values[1];
		myAccelValues[2] = values[2];
		accelOrMagChanged = true;
		return true;
	}

	public synchronized boolean setMagnetValues(float[] values) {
		if (values == null)
			return false;
		myNewMagnetValues = null; // switches of update buffer method
		if (myMagnetValues == null)
			myMagnetValues = new float[3];

		myMagnetValues[0] = values[0];
		myMagnetValues[1] = values[1];
		myMagnetValues[2] = values[2];
		accelOrMagChanged = true;
		return true;
	}

	public synchronized boolean setOrientationValues(float[] values) {
		if (values == null)
			return false;

		if (myOrientValues == null)
			myOrientValues = new float[3];

		myOrientValues[0] = values[0];
		myOrientValues[1] = values[1];
		myOrientValues[2] = values[2];
		orientationValuesChanged = true;
		return true;
	}

	/**
	 * This will reset the rotation vector of the virtual camera
	 */
	public void resetBufferedAngle() {
		L.out("Reseting camera rotation!");
		if ((myNewRotationVec != null) && (sensorInputEnabled))
			myNewRotationVec.setToZero();
	}

	/**
	 * This will change the z value of the camera-rotation instantly without
	 * buffering by adding/subtracting the specified deltaZ value. The buffered
	 * version of this method is called
	 * {@link GLCamera#changeXYPositionBuffered(float, float)}
	 * 
	 * @param deltaZ
	 */
	public void changeZAngleUnbuffered(float deltaZ) {
		myRotationVec.z += deltaZ;
	}

	/**
	 * This will change the z value of the camera-rotation by adding/subtracting
	 * the specified deltaZ value.
	 * 
	 * @param deltaZ
	 */
	public void changeZAngleBuffered(float deltaZ) {
		if (myNewRotationVec == null) {
			myNewRotationVec = new Vec();
		}
		myNewRotationVec.z += deltaZ;

	}

	/**
	 * This will change the z value of the camera-position by adding/subtracting
	 * the specified deltaZ value.
	 * 
	 * @param deltaZ
	 *            eg. -10 to move the camera 10 meters down
	 */
	public void changeZPositionBuffered(float deltaZ) {
		if (myNewPosition == null) {
			myNewPosition = new Vec();
		}
		myNewPosition.z += deltaZ;
	}

	/**
	 * @param sensorInputEnabled
	 *            set false tell the camera to ignore sensor input. You can
	 *            still use the methods like
	 *            {@link GLCamera#setNewPosition(Vec)}
	 *            {@link GLCamera#setNewRotation(Vec)} to move the camera but
	 *            the AR impression will be lost. Use this for games and defined
	 *            movement through a virtual world.
	 */
	public void setSensorInputEnabled(boolean sensorInputEnabled) {
		this.sensorInputEnabled = sensorInputEnabled;
		if (!sensorInputEnabled) {
			rotationMatrix = createIdentityMatrix();
			// myAccelValues = null;
			// myMagnetValues = null;
		} else {
			myAccelValues = new float[3];
			myMagnetValues = new float[3];
		}
	}

	/**
	 * @param resetZValueToo
	 *            if you just want to reset x and y set this to false
	 */
	public void resetPosition(boolean resetZValueToo) {
		float pz = myPosition.z;
		float npz = myNewPosition.z;
		myPosition.setToZero();
		myNewPosition.setToZero();
		if (!resetZValueToo) {
			myPosition.z = pz;
			myNewPosition.z = npz;
		}
	}

	/**
	 * This will reset the camera postion to (0,0,0)
	 */
	public void resetPosition() {
		resetPosition(true);
	}

	public void setNewPosition(float x, float y, float z) {
		myNewPosition.x = y;
		myNewPosition.y = x;
		myNewPosition.z = z;
	}

	public void setNewPosition(float x, float y) {
		myNewPosition.x = y;
		myNewPosition.y = x;
	}

	public Vec getNewCameraOffset() {
		return myNewOffset;
	}

	public void setNewOffset(Vec myNewOffset) {
		this.myNewOffset = myNewOffset;
	}

	/**
	 * @return the position in the virtual world. This vec could be used as the
	 *         users postion e.g.
	 */
	public Vec getMyPosition() {
		return myPosition;
	}

	public Vec getMyNewPosition() {

		return myNewPosition;
	}

	/**
	 * The resulting coordinates can differ from
	 * {@link EventManager#getCurrentLocationObject()} if the camera was not
	 * moved according to the GPS input (eg moved via trackball).
	 * 
	 * @return
	 */
	public Location getGPSLocation() {
		Vec coords = getGPSPositionVec();
		Location pos = new Location("customCreated");
		pos.setLatitude(coords.y);
		pos.setLongitude(coords.x);
		pos.setAltitude(coords.z);
		return pos;
	}

	/**
	 * The resulting coordinates can differ from
	 * {@link EventManager#getCurrentLocationObject()} if the camera was not
	 * moved according to the GPS input (eg moved via trackball).
	 * 
	 * @return a Vector with x=Longitude, y=Latitude, z=Altitude
	 */
	public Vec getGPSPositionVec() {
		GeoObj devicePos = EventManager.getInstance()
				.getZeroPositionLocationObject();
		return GeoObj.calcGPSPosition(this.getMyPosition(),
				devicePos.getLatitude(), devicePos.getLongitude(),
				devicePos.getAltitude());
	}

	/**
	 * The resulting coordinates can differ from
	 * {@link EventManager#getCurrentLocationObject()} if the camera was not
	 * moved according to the GPS input (eg moved via trackball).
	 * 
	 * @return
	 */
	public GeoObj getGPSPositionAsGeoObj() {
		Vec v = getGPSPositionVec();
		return new GeoObj(v.y, v.x, v.z);
	}

	public float[] getRotationMatrix() {
		return rotationMatrix;
	}

	@Override
	public void showDebugInformation() {
		Log.w(LOG_TAG, "Infos about GLCamera:");
		Log.w(LOG_TAG, "   > myPosition=" + myPosition);
		Log.w(LOG_TAG, "   > myPosition=" + myNewPosition);
		Log.w(LOG_TAG, "   > myOffset=" + myOffset);
		Log.w(LOG_TAG, "   > myNewOffset=" + myNewOffset);
		Log.w(LOG_TAG, "   > myRotationVec=" + myRotationVec);
		Log.w(LOG_TAG, "   > myNewRotationVec=" + myNewRotationVec);
		Log.w(LOG_TAG, "   > rotationMatrix=" + rotationMatrix);
		Log.w(LOG_TAG, "   > unrotatedMatrix=" + unrotatedMatrix);
	}

}
