package worldData;

import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.ListInterface;
import system.ParentStack;
import util.EfficientList;

public class ObjGroup extends AbstractObj implements ListInterface {

	private EfficientList<AbstractObj> myItems = new EfficientList<AbstractObj>();

	// TODO move add in canbeshowninlist interface
	public void add(AbstractObj x) {
		myItems.add(x);
	}

	public void clear() {
		myItems.clear();
	}

	@Override
	public void removeEmptyItems() {
		int l = myItems.myLength;

		for (int i = 0; i < l; i++) {
			AbstractObj a = myItems.get(i);
			if (a instanceof ListInterface) {
				if (((ListInterface) a).isCleared()) {
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
	public boolean isCleared() {
		if (getNodes().myLength == 0) {
			return true;
		}
		return false;
	}

	@Override
	public EfficientList<AbstractObj> getNodes() {
		if (myItems == null)
			myItems = new EfficientList<AbstractObj>();
		return myItems;
	}

	@Override
	public int length() {
		return getNodes().myLength;
	}

}
