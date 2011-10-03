package gl.animations;

import gl.Color;
import gl.HasColor;
import gl.MeshComponent;
import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import system.ParentStack;
import util.Vec;
import worldData.Obj;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;

public class AnimationColorMorph implements RenderableEntity {

	private static final float MIN_DISTANCE = 0.001f;
	private float myDurationInMS;
	private Color myTargetColor;

	public AnimationColorMorph(float durationInMS, Color targetColor) {
		myDurationInMS = durationInMS;
		myTargetColor = targetColor;
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {

		if (parent instanceof HasColor) {
			Vec colorDistance = Color.morphToNewColor(
					((HasColor) parent).getColor(), myTargetColor, timeDelta
							/ myDurationInMS);
			if (!(colorDistance.getLength() > MIN_DISTANCE)) {
				Log.d("NodeListener", "color morph finnished for " + parent);
			}
			return (colorDistance.getLength() > MIN_DISTANCE);
		}
		return false;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}
}
