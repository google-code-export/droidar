package util;

public class Vec {

	private static final float SMALLEST_DISTANCE = 0.0001f;
	/**
	 * @param x
	 *            value on red axis (east direction)
	 * @param y
	 *            value on green axis (north direction)
	 * @param z
	 *            value on blue axis (sky direction)
	 */
	public float x, y, z = 0;
	private float[] myArray;

	/**
	 * @param x
	 *            value on red axis (east direction)
	 * @param y
	 *            value on green axis (north direction)
	 * @param z
	 *            value on blue axis (sky direction)
	 */
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * inits x,y,z with 0
	 */
	public Vec() {
	}

	public Vec(Vec vecToCopy) {
		this.x = vecToCopy.x;
		this.y = vecToCopy.y;
		this.z = vecToCopy.z;
	}

	/**
	 * @param vec2
	 * @return itself, no copy! just if you want to go on doing more like
	 *         v.add(v2).add(v3)
	 */
	public Vec add(Vec vec2) {
		x += vec2.x;
		y += vec2.y;
		z += vec2.z;
		return this;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * returns the leght of a given vector
	 * 
	 * @param a
	 * @return
	 */
	public static float vectorLength(Vec a) {
		return (float) Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
	}

	public Vec sub(Vec vec2) {
		x -= vec2.x;
		y -= vec2.y;
		z -= vec2.z;
		return this;
	}

	public Vec normalize() {
		return mult(1 / vectorLength(this));
	}

	public static float abs(float d) {
		if (d < 0)
			return -d;
		return d;
	}

	public Vec mult(float factor) {
		x = x * factor;
		y = y * factor;
		z = z * factor;
		return this;
	}

	/**
	 * rotates the vector CLOCKWISE around the z axis
	 * 
	 * <br>
	 * <br>
	 * 
	 * if you want to rotate according to the current camera rotation you have
	 * to calculate 360-angle first because this values (e.g.
	 * {@link GLCamera#myAnglesInRadians} or the values passed to the
	 * {@link CameraAngleUpdateListener}) will be given COUNTERClOCKWISE
	 * 
	 * <br>
	 * <br>
	 * 
	 * example: vector (0,10,0) should point in same direction as the camera is
	 * looking and camera looks east (90 degree) then rotate the (0,10,0) vector
	 * clockwise by 90 degree
	 * 
	 * @param angleInDegree
	 */
	public synchronized void rotateAroundZAxis(double angleInDegree) {
		/*
		 * Rotation matrix:
		 * 
		 * cos a -sin a 0
		 * 
		 * sin a cos a 0
		 * 
		 * 0 0 1
		 */
		angleInDegree = Math.toRadians(angleInDegree);
		float cos = (float) Math.cos(angleInDegree);
		float sin = (float) Math.sin(angleInDegree);
		float x2 = cos * x - sin * y;
		y = sin * x + cos * y;
		x = x2;
	}

	/**
	 * To get the point 10 meters away 30 degree clockwise from north you just
	 * have to pass (10, 30) as parameters
	 * 
	 * @param distanceInMeters
	 * @param angleInDegree
	 *            CLOCKWISE rotation angle
	 * @return
	 */
	public static Vec rotatedVecInXYPlane(float distanceInMeters,
			double angleInDegree) {
		Vec v = new Vec(distanceInMeters, 0, 0);
		v.rotateAroundZAxis(angleInDegree);
		return v;
	}

	/**
	 * Rotates counterclockwise around the x axis
	 * 
	 * @param angleInDegree
	 */
	public synchronized void rotateAroundXAxis(double angleInDegree) {
		/*
		 * Rotation matrix:
		 * 
		 * 1 0 0
		 * 
		 * 0 cos a sin a
		 * 
		 * 0 -sin a cos a
		 */
		angleInDegree = Math.toRadians(angleInDegree);
		float cos = (float) Math.cos(angleInDegree);
		float sin = (float) Math.sin(angleInDegree);
		float y2 = cos * y + sin * z;
		z = cos * z - sin * y;
		y = y2;
	}

	public Vec div(float factor) {
		x = x / factor;
		y = y / factor;
		z = z / factor;
		return this;
	}

	public static Vec mult(float factor, Vec oldVec) {
		Vec v = oldVec.copy();
		v.x *= factor;
		v.y *= factor;
		v.z *= factor;
		return v;
	}

	/**
	 * @param a
	 * @param b
	 * @return a positive distance value or -1 if something was wrong
	 */
	public static float distance(Vec a, Vec b) {
		if (a == null || b == null)
			return -1;
		return vectorLength(new Vec(a.x - b.x, a.y - b.y, a.z - b.z));
	}

	/**
	 * The same method like {@link Vec#distance(Vec, Vec)} but only the XY-plane
	 * is taken into account
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float XYdistance(Vec a, Vec b) {
		if (a == null || b == null)
			return -1;
		return vectorLength(new Vec(a.x - b.x, a.y - b.y, 0));
	}

	// // TODO check how to set optional parameters
	// public static Vec crossingLines(Vec startVec1, Vec directionVec1,
	// Vec startVec2, Vec directionVec2) {
	// return crossingLines(startVec1, directionVec1, startVec2,
	// directionVec2, true);
	// }
	//
	// // TODO neu machen! siehe vorlesungfolien
	// public static Vec crossingLines(Vec startVec1, Vec directionVec1,
	// Vec startVec2, Vec directionVec2, boolean directionLengthRelevant) {
	// if (Vec.parallelVecs(directionVec1, directionVec2)) {
	// // TODO check if startVec1 lies in the second line (important for
	// // cubes eg)
	// return null;
	// }
	// // startVec1+x*directionVec1 = startVec2+ x * directionVec2
	// // <=>
	// float y = (directionVec1.x * (startVec1.x - startVec2.x) -
	// directionVec1.x
	// * (startVec1.y - startVec2.y))
	// / (directionVec2.x * directionVec1.y - directionVec1.x
	// * directionVec2.y);
	// float x = (directionVec2.x * y - startVec1.x + startVec2.x)
	// / directionVec1.x;
	// // if its not important how long the directionVecs are then just return
	// // the "collision-position"
	// if (!directionLengthRelevant)
	// return add(startVec1, mult(x, directionVec1));
	// // now compare the length of the directionVecs with x and y
	// if ((abs(Vec.vlength(directionVec1)) <= abs(x))
	// && (abs(vlength(directionVec2)) <= abs(y)))
	// return add(startVec1, mult(x, directionVec1)); // alternately y*...
	// // would be the same
	// // result
	// return null;
	// }

	private static boolean parallelVecs(Vec vec1, Vec vec2) {
		if ((vec1.copy().normalize()).equals(vec2.copy().normalize()))
			return true;
		return false;
	}

	/**
	 * @param vec
	 * @param factor
	 *            the same factor as in {@link Vec#round(float)}
	 * @return
	 */
	public boolean equals(Vec vec, float factor) {

		this.round(factor);
		vec.round(factor);

		if ((x == vec.x) && (y == vec.y) && (z == vec.z))
			return true;
		return false;
	}

	/**
	 * @param factor
	 *            pass 100 if you want to cut 0.12345678 to 0.12 and 1000 to cut
	 *            it to 0.123
	 */
	public void round(float factor) {
		x = Math.round(x * factor) / factor;
		y = Math.round(y * factor) / factor;
		z = Math.round(z * factor) / factor;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vec)
			return equals((Vec) o, 1000000f);
		return super.equals(o);
	}

	public static Vec add(Vec a, Vec b) {
		float x = a.x + b.x;
		float y = a.y + b.y;
		float z = a.z + b.z;
		return new Vec(x, y, z);
	}

	public static Vec sub(Vec a, Vec b) {
		float x = a.x - b.x;
		float y = a.y - b.y;
		float z = a.z - b.z;
		return new Vec(x, y, z);
	}

	/**
	 * returns the orthogonal vector in 2d so z=0
	 * 
	 * @param a
	 * @return
	 */
	public static Vec getOrthogonalHorizontal(Vec a) {
		/*
		 * => a*orthogonal=0 <=> a.x*orthogonal.x+a.y*orthogonal.y=0
		 * 
		 * set orthogonal.y=-1 => orthogonal.x=a.y/a.x
		 */
		if (a.x == 0)
			return new Vec(1, 0, 0);
		return new Vec(a.y / a.x, -1, 0);
	}

	// TODO you can't mirror a 3d line an another line so extend to plane
	// // this "mirrors" a vector (for eg the mirror line is the ground =(1,0)
	// and
	// // a is the force of a ball which hits it
	// public static Vec mirror(Vec mirrorLine, Vec a) {
	// /*
	// * Its a orthogonal projection: => a'=a+(a *
	// * mirrorLine)/(|mirrorLine|^2) * mirrorLine
	// */
	//
	// // TODO not correct i think, it returns the senkrechte??
	// // has to be Vec mirrored = Vec.sub(mult(2,Vec.project(a,
	// // mirrorLine)),a); ??
	//
	// Vec mirrored = Vec.add(a, Vec.project(a, mirrorLine));
	// return mirrored;
	// }

	/**
	 * This is the scalar product of two vectors
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static float multScalar(Vec a, Vec b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	// Read also info at Vec.mirror()
	/** returns the shadow of the tree on the ground TODO add explanation here! */
	public static Vec orthogonalProjection(Vec tree, Vec ground) {
		return mult((multScalar(tree, ground) / (multScalar(ground, ground))),
				ground);
	}

	public Vec copy() {
		return new Vec(x, y, z);
	}

	/**
	 * @param length
	 * @return the resized vector to allow chains
	 */
	public Vec setLength(float length) {
		return mult(length / vectorLength(this));
	}

	public float getLength() {
		return Vec.vectorLength(this);
	}

	public void setToVec(Vec b) {
		x = b.x;
		y = b.y;
		z = b.z;
	}

	public void setToZero() {
		x = 0;
		y = 0;
		z = 0;
	}

	@Override
	public String toString() {
		return "Vec: (" + x + ", " + y + ", " + z + ")";
	}

	/**
	 * can be used for smooth scrolling or just morphing from vec a to b
	 * 
	 * @param target
	 * @param newPos
	 * @param factor
	 *            should be >0 and <1
	 * @return
	 */
	public static void morphToNewVec(Vec target, Vec newPos, float factor) {
		if (factor > 1) {
			factor = 1;
		}
		/*
		 * the other way would be:
		 * 
		 * final Vec dif = Vec.sub(newPos, target); dif.mult(factor);
		 * target.add(dif);
		 */

		target.x += factor * (newPos.x - target.x);
		target.y += factor * (newPos.y - target.y);
		target.z += factor * (newPos.z - target.z);
	}

	public static void morphToNewAngleVec(Vec target, Vec newRotation,
			float timeDelta) {
		morphToNewAngleVec(target, newRotation.x, newRotation.y, newRotation.z,
				timeDelta);
	}

	/**
	 * 
	 * @param target
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param timeDelta
	 */
	public static void morphToNewAngleVec(Vec target, float newX, float newY,
			float newZ, float timeDelta) {
		if (timeDelta > 1) {
			timeDelta = 1;
		}

		float deltaX = (newX - target.x);
		if (deltaX > 180) {
			target.x -= (360 - deltaX) * timeDelta;
		} else if (deltaX < -180) {
			target.x += (360 + deltaX) * timeDelta;
		} else if (!(-SMALLEST_DISTANCE < deltaX && deltaX < SMALLEST_DISTANCE)) {
			target.x += (deltaX) * timeDelta;
		}
		if (target.x < 0)
			target.x += 360;
		if (target.x >= 360)
			target.x -= 360;

		float deltaY = (newY - target.y);
		if (deltaY > 180) {
			target.y -= (360 - deltaY) * timeDelta;
		} else if (deltaY < -180) {
			target.y += (360 + deltaY) * timeDelta;
		} else if (!(-SMALLEST_DISTANCE < deltaY && deltaY < SMALLEST_DISTANCE)) {
			target.y += (deltaY) * timeDelta;
		}
		if (target.y < 0)
			target.y += 360;
		if (target.y >= 360)
			target.y -= 360;

		float deltaZ = newZ - target.z;
		if (deltaZ > 180) {
			target.z -= (360 - deltaZ) * timeDelta;
		} else if (deltaZ < -180) {
			target.z += (360 + deltaZ) * timeDelta;
		} else if (!(-SMALLEST_DISTANCE < deltaZ && deltaZ < SMALLEST_DISTANCE)) {
			target.z += (deltaZ) * timeDelta;
		}
		if (target.z < 0)
			target.z += 360;
		if (target.z >= 360)
			target.z -= 360;

	}

	public boolean isNullVector() {
		return ((x == 0) && (y == 0) && (z == 0));
	}

	public static final float deg2rad = 0.01745329238474369f;
	final public static float rad2deg = (float) (180.0f / Math.PI);

	/**
	 * This calculates 2 angles and returns them in a new {@link Vec}. the angle
	 * ranges go from 0 to 360 and one is always the z angle (compass angle) and
	 * the other one the gradient (sometimes stored in the x value sometimes in
	 * the y, depends on the input, see method implementation for more details)
	 * you can use this resulting angle to easily rotate the camera for example
	 * and center it on e specific target (target would be the to-Vec, camera
	 * position the from-Vec). <br>
	 * 
	 * If this does not behave as you want it to, try calculating 360-x,y,z
	 * 
	 * @param from
	 *            the Vec where you are standing
	 * @param to
	 *            the Vec where you want to look at
	 * @return
	 */
	public static Vec calcAngleVec(Vec from, Vec to) {
		final Vec source = to.copy();
		source.sub(from);
		return calcAngleVec(source);
	}

	/**
	 * If this does not behave as you want it to, try calculating 360-x,y,z
	 * 
	 * @param targetVec
	 *            the Vec is "looking" at something and this function is
	 *            calculating the angles where it is looking at. for a better
	 *            explanation see {@link #calcAngleVec(Vec, Vec)}
	 * @return
	 */
	public static Vec calcAngleVec(Vec targetVec) {
		if ((targetVec.x == 0) && (targetVec.y == 0)) {
			if (targetVec.z > 0) {
				// the angle is looking directly in the sky
				return new Vec(180, 0, 0);
			} else {
				// the angle is looking directly on the ground
				return new Vec();
			}
		}

		/*
		 * if the y value is 0 then setting x or z to 0 too would not make
		 * sense, if y is 0 you have to do the same for x angle (angle between
		 * red-x-achsis and blue-z-achsis)
		 * 
		 * calc the angle between the green and the red axis clockwise (so it
		 * goes from 0 to 359)
		 * 
		 * why "result = source.copy();"? its the first time you set something
		 * to result so you can use it instead of creating a new Vec() and
		 * wasting memory
		 */
		final Vec result = targetVec.copy();
		result.z = 0;
		/*
		 * arcCos has a value range from -180 to 180 so you have to check on
		 * which side (east (x>=0) or west (x<0) if it would be a compass) the x
		 * value is
		 */
		if (result.x >= 0) {
			result.z = (float) Math.acos(result.y / result.getLength())
					* rad2deg;
		} else {
			result.z = 360.0f
					- (float) Math.acos(result.y / result.getLength())
					* rad2deg;
		}
		// remember to clear other result values again:
		result.x = 0;
		result.y = 0;

		if (targetVec.y != 0) {
			final Vec v = targetVec.copy();
			v.x = 0;
			/*
			 * 0 would be the floor, -90 the horizon, -180 the sky. the values
			 * are negative because openGL needs them this way
			 */
			result.x = -(float) Math.acos(-v.z / v.getLength()) * rad2deg;
		} else {
			/*
			 * y is 0 so you have to rotate around the y achsis and not aroud x
			 * achsis as usual
			 * 
			 * about the resulting angle: 0 would be the floor 180 the sky
			 */
			result.x = -(float) Math.acos(-targetVec.z / targetVec.getLength())
					* rad2deg;
		}
		return result;
	}

	public static Vec copy(Vec valueVec) {
		if (valueVec == null)
			return null;
		return valueVec.copy();
	}

	public Vec getNegativeClone() {
		return new Vec(-x, -y, -z);
	}

	/**
	 * @param center
	 * @param minDistance
	 *            to the center
	 * @param maxDistance
	 *            to the center
	 * @return A random vector with the same z value
	 */
	public static Vec getNewRandomPosInXYPlane(Vec center, float minDistance,
			float maxDistance) {

		if (center == null)
			return null;

		float rndDistance = (float) (Math.random()
				* (maxDistance - minDistance) + minDistance);
		Vec rndPos = new Vec(rndDistance, 0, 0);
		rndPos.rotateAroundZAxis(Math.random() * 359);
		rndPos.x += center.x;
		rndPos.y += center.y;
		rndPos.z = 0;
		return rndPos;
	}

	public static Vec getNewRandomPosInXYZ(Vec center, float minDistance,
			float maxDistance) {

		if (center == null)
			return null;

		float rndDistance = (float) (Math.random()
				* (maxDistance - minDistance) + minDistance);
		Vec rndPos = new Vec(rndDistance, 0, 0);
		rndPos.rotateAroundXAxis(Math.random() * 359);
		rndPos.rotateAroundZAxis(Math.random() * 359);
		rndPos.x += center.x;
		rndPos.y += center.y;
		rndPos.z += center.z;
		return rndPos;
	}

	/**
	 * Given 2 vectors this calculates the vector which is orthogonal to the
	 * plane the two vectors create
	 * 
	 * @param v1
	 * @param v2
	 * @return the orthogonal vector
	 */
	public static Vec calcNormalVec(Vec uVec, Vec vVec) {
		Vec ret = new Vec();
		ret.x = (uVec.y * vVec.z) - (uVec.z * vVec.y);
		ret.y = (uVec.z * vVec.x) - (uVec.x * vVec.z);
		ret.z = (uVec.x * vVec.y) - (uVec.y * vVec.x);
		return ret;
	}

	public float[] getArrayVersion() {
		if (myArray == null) {
			myArray = new float[4];
			/*
			 * set the last of the 4 values to 1 on default. This is important
			 * for light-positioning eg, there it is used as a flag to indicate
			 * that the light should be a positional light source. See the
			 * LightSource class and
			 * http://fly.cc.fer.hr/~unreal/theredbook/chapter06.html for more
			 * details
			 * 
			 * TODO so is this the right place to do this?
			 */
			myArray[3] = 1;
		}
		myArray[0] = x;
		myArray[1] = y;
		myArray[2] = z;

		return myArray;
	}

	public float scalarMult(Vec b) {
		return x * b.x + y * b.y + z * b.z;
	}

	public void setTo(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
