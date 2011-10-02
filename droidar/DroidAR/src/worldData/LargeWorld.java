package worldData;

import gl.GLCamera;
import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.EfficientList;
import util.QuadTree;
import util.QuadTree.ResultListener;
import util.Vec;

public class LargeWorld extends World {

	private float myRenderDistance;
	private float myRecalcDistanceMin;
	private float myRecalcDistanceMax;
	private QuadTree<Obj> tree;

	@SuppressWarnings("rawtypes")
	private ResultListener itemsListener;
	private EfficientList<Obj> itemsInRange;
	private float oldX;
	private float oldY;

	public LargeWorld(GLCamera glCamera, float renderDistance,
			float recalcDistance) {
		super(glCamera);
		myRenderDistance = renderDistance;
		myRecalcDistanceMin = -recalcDistance;
		myRecalcDistanceMax = recalcDistance;
		tree = new QuadTree<Obj>();

		itemsListener = tree.new ResultListener() {

			@Override
			public void onResult(Obj myValue) {
				itemsInRange.add(myValue);
			}
		};
	}

	@Override
	public EfficientList<AbstractObj> getAllItems() {
		EfficientList<AbstractObj> allItems = super.getAllItems();
		if (allItems != null) {
			final EfficientList<AbstractObj> result = allItems.copy();
			tree.getAllItems(tree.new ResultListener() {
				@Override
				public void onResult(Obj myValue) {
					result.add(myValue);
				}
			});
			return result;
		}
		return null;
	}

	public EfficientList<Obj> getItems(Vec position, float maxDistance) {

		final EfficientList<Obj> result = new EfficientList<Obj>();
		tree.findInArea(tree.new ResultListener() {

			@Override
			public void onResult(Obj myValue) {
				result.add(myValue);
			}
		}, position.x, position.y, maxDistance);
		return result;
	}

	@Override
	public boolean add(AbstractObj x) {
		if (x instanceof Obj)
			if (add((Obj) x))
				return true;
		return super.add(x);
	}

	private boolean add(Obj x) {
		if (x.getGraphicsComponent() != null
				&& x.getGraphicsComponent().myPosition != null) {
			tree.add(x.getGraphicsComponent().myPosition.x,
					x.getGraphicsComponent().myPosition.y, x);
			return true;
		}
		return false;
	}

	/**
	 * The current internal tree will be deleted and recreated. This is
	 * expensive so do not call this too often!
	 */
	public void rebuildTree() {
		final EfficientList<Obj> list = new EfficientList<Obj>();
		tree.getAllItems(tree.new ResultListener() {
			@Override
			public void onResult(Obj myValue) {
				list.add(myValue);
			}
		});
		tree.clear();
		for (int i = 0; i < list.myLength; i++) {
			this.add(list.get(i));
		}
	}

	@Override
	public void drawElements(GLCamera camera, GL10 gl,
			ParentStack<Renderable> stack) {

		EfficientList<Obj> list = getList(camera.getPosition().x,
				camera.getPosition().y);
		for (int i = 0; i < list.myLength; i++) {
			Obj obj = list.get(i);
			if (obj != null)
				obj.render(gl, this, stack);
		}
		super.drawElements(camera, gl, stack);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		EfficientList<Obj> list = getList(getMyCamera().getPosition().x,
				getMyCamera().getPosition().y);
		for (int i = 0; i < list.myLength; i++) {
			Obj obj = list.get(i);
			if (obj != null)
				obj.update(timeDelta, this, stack);
		}
		return super.update(timeDelta, parent, stack);
	}

	@SuppressWarnings("unchecked")
	private EfficientList<Obj> getList(float x, float y) {
		if (itemsInRange != null
				&& needsNoRecalculation(x - oldX, myRecalcDistanceMin,
						myRecalcDistanceMax)
				&& needsNoRecalculation(y - oldY, myRecalcDistanceMin,
						myRecalcDistanceMax)) {
			return itemsInRange;
		} else {
			if (itemsInRange == null)
				itemsInRange = new EfficientList<Obj>();
			else
				itemsInRange.clear();
			oldX = x;
			oldY = y;
			tree.findInArea(itemsListener, x, y, myRenderDistance);
			return itemsInRange;
		}
	}

	private boolean needsNoRecalculation(float v, float min, float max) {
		return (min < v) && (v < max);
	}

}
