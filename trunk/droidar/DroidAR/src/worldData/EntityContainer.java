package worldData;

import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.Container;
import system.ParentStack;
import util.EfficientList;

public class EntityContainer extends AbstractObj implements
		Container<RenderableEntity> {

	private EfficientList<RenderableEntity> myItems = new EfficientList<RenderableEntity>();

	public boolean add(RenderableEntity x) {
		return myItems.add(x);
	}

	public void clear() {
		myItems.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeEmptyItems() {
		int l = myItems.myLength;

		for (int i = 0; i < l; i++) {
			RenderableEntity a = myItems.get(i);
			if (a instanceof Container) {
				if (((Container) a).isCleared()) {
					myItems.remove(a);
				}
			}
			// TODO more item types to check? like obj or geoobj?
		}
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		if (stack != null)
			stack.add(this);
		int l = myItems.myLength;
		for (int i = 0; i < l; i++) {
			myItems.get(i).render(gl, this, stack);
		}
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (stack != null)
			stack.add(this);
		int l = myItems.myLength;
		for (int i = 0; i < l; i++) {
			myItems.get(i).update(timeDelta, this, stack);
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public boolean isCleared() {
		if (getAllItems().myLength == 0) {
			return true;
		}
		return false;
	}

	@Override
	public EfficientList<RenderableEntity> getAllItems() {
		if (myItems == null)
			myItems = new EfficientList<RenderableEntity>();
		return myItems;
	}

	@Override
	public boolean remove(RenderableEntity x) {
		return myItems.remove(x);
	}

	@Override
	public int length() {
		return getAllItems().myLength;
	}

}
