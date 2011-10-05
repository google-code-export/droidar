package gl.animations;

import gl.GLCamera;
import gl.Renderable;
import gl.scenegraph.MeshComponent;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.Vec;
import worldData.Obj;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;

public class AnimationFaceToCamera implements GLAnimation {

	private GLCamera myTargetCamera;
	private float lastUpdateAway = 0;
	private float myUpdateDelay;
	private Vec rotationVec = new Vec();
	private Vec newRotationVec = new Vec();

	private Vec adjustmentVec;
	private Vec myTargetCameraPosition;
	private boolean dontChangeXRotation;

	/**
	 * @param targetCamera
	 * @param targetMesh
	 * @param updateDelay
	 *            around 0.5f s
	 * @param dontChangeXRotation
	 *            if this is false, the mesh will also change the rotation x
	 *            value, otherwise only the z value to face to the camera
	 */
	public AnimationFaceToCamera(GLCamera targetCamera, float updateDelay,
			boolean dontChangeXRotation) {
		myTargetCamera = targetCamera;

		myUpdateDelay = updateDelay;
		myTargetCameraPosition = myTargetCamera.getPosition();
		this.dontChangeXRotation = dontChangeXRotation;
		// Log.d("face camera animation", "created. camera=" + myTargetCamera
		// + " targetMesh class=" + myTargetMesh.getClass()
		// + " update delay=" + myUpdateDelay);
	}

	public AnimationFaceToCamera(GLCamera targetCamera, float updateDelay) {
		this(targetCamera, updateDelay, true);
	}

	public AnimationFaceToCamera(GLCamera targetCamera) {
		this(targetCamera, 0.5f, true);
	}

	/**
	 * @param targetCamera
	 * @param targetMesh
	 * @param updateDelay
	 *            0.5f
	 * @param adjustmentVec
	 */
	public AnimationFaceToCamera(GLCamera targetCamera, float updateDelay,
			Vec adjustmentVec) {
		this(targetCamera, updateDelay);
		this.adjustmentVec = adjustmentVec;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		/*
		 * TODO use mesh instead of assigning a mesh while creating this
		 * animation!
		 */
		lastUpdateAway += timeDelta;
		if (lastUpdateAway > myUpdateDelay) {
			updateRotation(parent);
			// Log.d("face camera animation", "new rotation vec calculated:");
			// Log.d("face camera animation",
			// "x="+newRotationVec.x+" , z="+newRotationVec.z);
			lastUpdateAway = 0;
		}
		if (dontChangeXRotation) {
			Vec.morphToNewAngleVec(rotationVec, 0, 0, newRotationVec.z,
					timeDelta);
		} else {
			Vec.morphToNewAngleVec(rotationVec, newRotationVec.x,
					newRotationVec.y, newRotationVec.z, timeDelta);
		}
		return true;
	}

	private void updateRotation(Updateable parent) {
		if (parent instanceof MeshComponent) {
			Vec pos = ((MeshComponent) parent).getAbsolutePosition();
			// Log.d("face camera animation", "mesh position: "+pos);
			newRotationVec = Vec.calcAngleVec(pos, myTargetCameraPosition);
			/*
			 * x has to be adjusted because the Vec.calcAngleVec has its
			 * relative 0-vector facing to the ground. the 0,0,0 vec of all
			 * objects in relation to this ground-facing-vec is -90,0,0 so if
			 * you would rise the x 90 degree it would be the same vec. so for
			 * example if you want to face 0 0 0 you would have get -90 0 0 from
			 * the Vec.calcAngleVec() function and you would have to invert the
			 * -90 to 90 and subtract 90 to get 0,0,0
			 * 
			 * its better to do this adjustments here where they are not so
			 * often needed as in Vec.calcAngleVec() which was designed for fast
			 * GLCamera angle adjustments.
			 * 
			 * z has to be inverted because calcAngleVec returns a negative
			 * value (needed in GlCamera):
			 */
			newRotationVec.x = (-newRotationVec.x) - 90;
			newRotationVec.z = -newRotationVec.z;
		}
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {

		gl.glRotatef(rotationVec.z, 0, 0, 1);
		gl.glRotatef(rotationVec.x, 1, 0, 0);
		gl.glRotatef(rotationVec.y, 0, 1, 0);

		if (adjustmentVec != null) {
			/*
			 * if an adjustment vector is set this adjustment has to be done
			 * AFTER the rotation to be easy to use, see constructor for infos
			 * about adjustment
			 */
			gl.glRotatef(adjustmentVec.x, 1, 0, 0); // TODO find correct order
			gl.glRotatef(adjustmentVec.z, 0, 0, 1);
			gl.glRotatef(adjustmentVec.y, 0, 1, 0);
		}
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
