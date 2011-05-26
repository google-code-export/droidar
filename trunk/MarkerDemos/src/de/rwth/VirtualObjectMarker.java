package de.rwth;

import gl.GLCamera;
import gl.MeshComponent;
import util.Vec;
import android.opengl.Matrix;
import gl.MarkerObject;

public class VirtualObjectMarker implements MarkerObject {

	private MeshComponent myTargetMesh;
	private GLCamera myCamera;
	private float[] modelViewMatrix = GLCamera.createIdentityMatrix();

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
		Matrix.multiplyMM(modelViewMatrix, 0, myCamera.getRotationMatrix(),
				myCamera.getMatrixOffset(), rotMatrix, start);
		float[] centerVec = { 0, 0, 0, 1 };
		float[] resultVec = { 0, 0, 0, 1 };
		Matrix.multiplyMV(resultVec, 0, modelViewMatrix, 0, centerVec, 0);
		myTargetMesh.myPosition = new Vec(resultVec[0], resultVec[1],
				resultVec[2]);
		System.out.println(myTargetMesh.myPosition);
	}

}
