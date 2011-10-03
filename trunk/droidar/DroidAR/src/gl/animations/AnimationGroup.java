package gl.animations;

import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.Container;
import system.ParentStack;
import util.EfficientList;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class AnimationGroup implements RenderableEntity,
		Container<RenderableEntity> {

	private static final String LOG_TAG = "Animation Group";
	EfficientList<RenderableEntity> myItems = new EfficientList<RenderableEntity>();
	private boolean isClearedAtLeastOnce;

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		for (int i = 0; i < myItems.myLength; i++) {
			myItems.get(i).render(gl, parent, stack);
		}
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		for (int i = 0; i < myItems.myLength; i++) {
			if (!myItems.get(i).update(timeDelta, parent, stack)) {
				Log.d(LOG_TAG, "Animation " + myItems.get(i)
						+ " will now be removed from Anim.-group because it "
						+ "is finished (returned false on update())");
				myItems.remove(myItems.get(i));
			}
		}
		if (myItems.myLength == 0)
			return false;
		return true;
	}

	@Override
	public boolean add(RenderableEntity animation) {
		return myItems.add(animation);
	}

	@Override
	public boolean remove(RenderableEntity animation) {
		return myItems.remove(animation);
	}

	@Override
	public void clear() {
		myItems.clear();
		isClearedAtLeastOnce = true;
	}

	@Override
	public boolean isCleared() {
		return isClearedAtLeastOnce;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeEmptyItems() {
		for (int i = 0; i < myItems.myLength; i++) {
			if (((Container) myItems.get(i)).isCleared())
				myItems.remove(myItems.get(i));
		}
	}

	@Override
	public int length() {
		return myItems.myLength;
	}

	@Override
	public EfficientList<RenderableEntity> getAllItems() {
		return myItems;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit((Container) this);
	}
}
