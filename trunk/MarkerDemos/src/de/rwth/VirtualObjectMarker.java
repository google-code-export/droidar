package de.rwth;

import gl.GLCamera;
import gl.MarkerObject;
import gl.MeshComponent;
import util.Vec;
import android.opengl.Matrix;

public class VirtualObjectMarker implements MarkerObject {

	// final static float rad2deg = (float) (180.0f / Math.PI);

	private float[] invertedCameraMatrix = new float[16];
	private float[] resultPosVec = { 0, 0, 0, 1 };
	private float[] antiCameraMarkerRotMatrix = new float[16];

	private MeshComponent myTargetMesh;
	private GLCamera myCamera;
	private int myId;

	// private float[] viewMatrix = null;

	public VirtualObjectMarker(int id, MeshComponent m, GLCamera camera) {
		myTargetMesh = m;
		myCamera = camera;
		myId = id;
	}

	@Override
	public int getMyId() {
		return myId;
	}

	@Override
	public void OnMarkerPositionRecognized(float[] markerRotMatrix,
			int startOffset, int end) {

		Matrix.invertM(invertedCameraMatrix, 0, myCamera.getRotationMatrix(), 0);

		float[] markerCenterPosVec = { markerRotMatrix[startOffset + 12],
				markerRotMatrix[startOffset + 13],
				markerRotMatrix[startOffset + 14], 1 };
		Matrix.multiplyMV(resultPosVec, 0, invertedCameraMatrix, 0,
				markerCenterPosVec, 0);

		Vec camPos = myCamera.getMyPosition();
		myTargetMesh.myPosition = new Vec(resultPosVec[0] + camPos.x,
				resultPosVec[1] + camPos.y, resultPosVec[2] + camPos.z);

		Matrix.multiplyMM(antiCameraMarkerRotMatrix, 0, invertedCameraMatrix,
				0, markerRotMatrix, startOffset);

		// clear the translation values:
		antiCameraMarkerRotMatrix[12] = 0;
		antiCameraMarkerRotMatrix[13] = 0;
		antiCameraMarkerRotMatrix[14] = 0;

		// addAngle(antiCameraMarkerRotMatrix, sideAngle);
		// sideAngle = 0;

		myTargetMesh.setRotationMatrix(antiCameraMarkerRotMatrix);

		/*
		 * alternative method which does not work for now:
		 * 
		 * its not so clear to me if it would be better to extract the rotation
		 * angles and store them directly in the myRotation field. now its still
		 * possible to rotate the mesh in addition to the rotation by the marker
		 * matrix but its not possible to read the angle values and use them for
		 * something else then this concrete scenario
		 */

		// float[] resultingAngles = { 0, 0, 0, 1 };
		//
		// getAngles(resultingAngles, antiCameraMarkerRotMatrix);
		//
		// if (myTargetMesh.myRotation == null)
		// myTargetMesh.myRotation = new Vec(resultingAngles[0],
		// resultingAngles[1], resultingAngles[2]);
		// else {
		// myTargetMesh.myRotation.x = resultingAngles[0];
		// myTargetMesh.myRotation.y = resultingAngles[1];
		// myTargetMesh.myRotation.z = resultingAngles[2];
		// }
	}

	// private void getAngles(float[] resultingAngles, float[] rotationMatrix) {
	// //rotationMatrix = transpose(rotationMatrix);
	//
	// resultingAngles[2] = (float) (Math.asin(rotationMatrix[2]));
	// final float cosB = (float) Math.cos(resultingAngles[2]);
	// resultingAngles[2] = resultingAngles[2] * rad2deg;
	// resultingAngles[0] = -(float) (Math.acos(rotationMatrix[0] / cosB))
	// * rad2deg;
	// resultingAngles[1] = (float) (Math.acos(rotationMatrix[10] / cosB))
	// * rad2deg;
	//
	// }
	//
	// private float[] transpose(float[] source) {
	// final float[] result = source.clone();
	// result[1] = source[4];
	// result[2] = source[8];
	// result[4] = source[1];
	// result[6] = source[9];
	// result[8] = source[2];
	// result[9] = source[6];
	// // TODO can be optimized by changing the values in getAngles directly
	// return result;
	// }
}
