package system.markerDetection;

import gl.GLCamera;

public class MyMarker1Class {

	private float[] myTargetMatrix;

	public MyMarker1Class(GLCamera camera) {
		myTargetMatrix = camera.getRotationMatrix();
	}

	private void whencalculated() {
		// myCamera.rotationMatrix=...
	}
}
