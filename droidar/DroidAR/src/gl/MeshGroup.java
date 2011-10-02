package gl;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.EfficientList;
import util.Vec;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class MeshGroup extends MeshComponent {

	public EfficientList<MeshComponent> myMeshes = new EfficientList<MeshComponent>();

	public MeshGroup() {
		super(null);
	}

	/**
	 * @param pos
	 *            not side effect free (working with pos afterwards will change
	 *            myPosition of this {@link MeshGroup})!
	 */
	public MeshGroup(Color c, Vec pos) {
		super(c);
		myPosition = pos;
	}

	public MeshGroup(Color color) {
		super(color);
	}

	public void add(MeshComponent x) {
		if (x == null) {
			Log.e("MeshGroup", "The mesh which should be added was NULL");
			return;
		}
		if (x == this) {
			Log.e("MeshGroup", "Endless recursion! Mesh cant be own parent.");
			return;
		}
		myMeshes.add(x);
		x.setMyParentMesh(this);
	}

	public boolean remove(MeshComponent x) {
		x.setMyParentMesh(null);
		return myMeshes.remove(x);
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
		for (int i = 0; i < myMeshes.myLength; i++) {
			myMeshes.get(i).render(gl, this, stack);
		}
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (graficAnimationActive) {
			for (int i = 0; i < myMeshes.myLength; i++) {
				myMeshes.get(i).update(timeDelta, this, stack);
			}
			// additionally update the own animations too:
			super.update(timeDelta, parent, stack);
		}
		return true;
	}

	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public String toString() {
		if (myMeshes == null)
			return "Meshgroup (emtpy) " + super.toString();
		return super.toString() + "(size=" + myMeshes.myLength + ") ";
	}

	public void clear() {
		for (int i = 0; i < myMeshes.myLength; i++) {
			myMeshes.get(i).setMyParentMesh(null);
		}
		myMeshes.clear();
	}

}
