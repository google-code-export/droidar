package gl;

import javax.microedition.khronos.opengles.GL10;

import system.Container;
import system.ParentStack;
import util.EfficientList;
import util.Vec;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class RenderGroup extends MeshComponent implements
		Container<RenderableEntity> {

	private EfficientList<RenderableEntity> myItems = new EfficientList<RenderableEntity>();
	private boolean wasClearedAtLeastOnce;

	public RenderGroup() {
		this(null);
	}

	/**
	 * @param newPos
	 *            not side effect free (working with pos afterwards will change
	 *            myPosition of this {@link RenderGroup})!
	 */
	public RenderGroup(Color c, Vec newPos) {
		this(c);
		getPosition().setToVec(newPos);
	}

	public RenderGroup(Color color) {
		super(color);
	}

	@Override
	public int length() {
		return myItems.myLength;
	}

	@Override
	public boolean isCleared() {
		if (wasClearedAtLeastOnce && myItems.myLength == 0) {
			wasClearedAtLeastOnce = true;
			return true;
		}
		return false;
	}

	@Override
	public EfficientList<RenderableEntity> getAllItems() {
		return myItems;
	}

	@Override
	public boolean add(RenderableEntity x) {
		if (x == null) {
			Log.e("MeshGroup", "The mesh which should be added was NULL");
			return false;
		}
		if (x == this) {
			Log.e("MeshGroup", "Endless recursion! Mesh cant be own parent.");
			return false;
		}
		myItems.add(x);
		setParent(x);
		return true;
	}

	@Deprecated
	private void setParent(RenderableEntity x) {
		if (x instanceof MeshComponent)
			((MeshComponent) x).setMyParentMesh(this);
	}

	@Override
	public boolean remove(RenderableEntity x) {
		setParent(x);
		return myItems.remove(x);
	}

	// @Override
	// public void setParent(MeshComponent m) {
	// // all children have to be informed:
	// for (MeshComponent c : myMeshes) {
	// c.setParent(this); // just inform them and dont update parent!
	// }
	// super.setParent(m);
	// }

	@Override
	public void draw(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		if (stack != null)
			stack.add(this);
		for (int i = 0; i < myItems.myLength; i++) {
			myItems.get(i).render(gl, this, stack);
		}
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (graficAnimationActive) {
			for (int i = 0; i < myItems.myLength; i++) {
				myItems.get(i).update(timeDelta, this, stack);
			}
			// additionally update the own animations too:
			super.update(timeDelta, parent, stack);
		}
		return true;
	}

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
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public String toString() {
		if (myItems == null)
			return "Meshgroup (emtpy) " + super.toString();
		return super.toString() + "(size=" + myItems.myLength + ") ";
	}

	@Override
	public void clear() {
		// for (int i = 0; i < myMeshes.myLength; i++) {
		// myMeshes.get(i).setMyParentMesh(null);
		// }
		myItems.clear();
	}

	@Override
	public void insert(int pos, RenderableEntity item) {
		myItems.insert(pos, item);
	}

}
