package worldData;

import javax.microedition.khronos.opengles.GL10;

import system.ListInterface;
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
	public void draw(GL10 gl) {
		int l = myItems.myLength;
		for (int i = 0; i < l; i++) {
			myItems.get(i).draw(gl);
		}
	}

	@Override
	public boolean update(float timeDelta) {
		int l = myItems.myLength;
		for (int i = 0; i < l; i++) {
			myItems.get(i).update(timeDelta);
		}
		return true;
	}

	@Override
	public boolean isCleared() {
		if (getMyItems().myLength == 0) {
			return true;
		}
		return false;
	}

	@Override
	public EfficientList<AbstractObj> getMyItems() {
		if (myItems == null)
			myItems = new EfficientList<AbstractObj>();
		return myItems;
	}

	@Override
	public int length() {
		return getMyItems().myLength;
	}

}
