package de.rwth;

import gl.GLCamera;
import gl.MeshComponent;
import util.Vec;
import android.opengl.Matrix;
import android.util.Log;
import gl.MarkerObject;

public class VirtualObjectMarker implements MarkerObject {

	private MeshComponent myTargetMesh;
	private GLCamera myCamera;

	// private float[] viewMatrix = null;

	public VirtualObjectMarker(MeshComponent m, GLCamera camera) {
		myTargetMesh = m;
		myCamera = camera;
	}

	@Override
	public int getMyId() {
		return 0;
	}

	@Override
	public void OnMarkerPositionRecognized(float[] rotMatrix, int start,
			int end, int sideAngle) {
		Log.d("AR", "matrix length " + rotMatrix.length);
		Log.d("AR", "matrix end " + end);
		Log.d("AR", "matrix start " + start);

		// viewMatrix = myCamera.getRotationMatrix();
		// Matrix.multiplyMM(modelViewMatrix, 0, myCamera.getRotationMatrix(),
		// myCamera.getMatrixOffset(), GLCamera.createIdentityMatrix(),
		// 0);//rotMatrix, start);
		float[] centerVec = { rotMatrix[start + 12], rotMatrix[start + 13],
				rotMatrix[start + 14], 1 };
		float[] resultVec = { 0, 0, 0, 1 };
		float[] invViewMatrix = new float[16];
		Matrix.invertM(invViewMatrix, 0, myCamera.getRotationMatrix(), 0);
		Matrix.multiplyMV(resultVec, 0, invViewMatrix, 0, centerVec, 0);
		Vec camPos = myCamera.getMyPosition();
		myTargetMesh.myPosition = new Vec(resultVec[0] + camPos.x, resultVec[1]
				+ camPos.y, resultVec[2] + camPos.z);
		System.out.println(myTargetMesh.myPosition);
	}

}
