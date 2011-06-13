package system;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;

	public CameraView(Context context) {
		super(context);
		intiCameraView(context);
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		intiCameraView(context);

	}
	
	private void intiCameraView(Context context) {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		Log.d("Activity", "Camera holder created");
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it
		// where to draw.
		mCamera = Camera.open();
		Log.d("Activity", "Camera opened");
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and
		// begin the preview.
		// Camera.Parameters parameters = mCamera.getParameters();
		// parameters.setPreviewSize(w, h);
		// mCamera.setParameters(parameters);

		resumeCamera();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the
		// preview.
		// Because the CameraDevice object is not a shared resource,
		// it's very important to release it when the activity is paused.
		releaseCamera();
	}

	public void resumeCamera() {
		if (mCamera != null) {
			mCamera.startPreview();
			Log
					.d("Activity", "Camera preview started (camera=" + mCamera
							+ ")");
		} else {
			Log.d("Activity",
					"Camera preview not started because no camera set til now");
		}
	}

	public void pause() {
		if (mCamera != null) {
			Log.d("Activity", "Camera preview stopped");
			mCamera.stopPreview();
		}
	}

	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			Log.d("Activity", "Camera released");
		}
	}

}
