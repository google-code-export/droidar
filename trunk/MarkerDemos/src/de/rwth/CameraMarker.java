package de.rwth;

import gl.GLCamera;
import gl.MarkerObject;

public class CameraMarker implements MarkerObject {

	private int myId;
	private GLCamera myCamera;

	public CameraMarker(int id, GLCamera camera) {
		myId = id;
		myCamera = camera;
	}

	@Override
	public int getMyId() {
		return myId;
	}

	@Override
	public void OnMarkerPositionRecognized(float[] rotMatrix, int start,
			int end, int sideAngle) {

		myCamera.setRotationMatrixFromMarkerInput(rotMatrix, start, sideAngle);
	}

}
