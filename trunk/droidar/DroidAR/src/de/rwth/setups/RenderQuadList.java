package de.rwth.setups;

import gl.GLCamera;
import gl.HasPosition;
import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.Container;
import system.ParentStack;
import util.EfficientList;
import util.QuadTree;
import util.QuadTree.ResultListener;
import util.Vec;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class RenderQuadList implements RenderableEntity,
		Container<RenderableEntity> {

	private static final String LOG_TAG = "RenderQuadList";
	private float myRenderDistance;
	private float myRecalcDistanceMin;
	private float myRecalcDistanceMax;

	private EfficientList<RenderableEntity> allItems;
	private QuadTree<RenderableEntity> tree;

	@SuppressWarnings("rawtypes")
	private ResultListener itemsListener;
	private volatile EfficientList<RenderableEntity> itemsInRange;
	private float oldX;
	private float oldY;

	private GLCamera myGlCamera;
	private boolean wasClearedAtLeastOnce = false;

	public RenderQuadList(GLCamera glCamera, float renderDistance,
			float recalcDistance) {
		myGlCamera = glCamera;
		myRecalcDistanceMax = recalcDistance;
		myRecalcDistanceMin = -recalcDistance;
		myRenderDistance = renderDistance;

		itemsListener = new QuadTree<RenderableEntity>().new ResultListener() {
			@Override
			public void onResult(RenderableEntity myValue) {
				itemsInRange.add(myValue);
			}
		};
	}

	public EfficientList<RenderableEntity> getItems(Vec position,
			float maxDistance) {
		final EfficientList<RenderableEntity> result = new EfficientList<RenderableEntity>();
		if (tree != null) {
			tree.findInArea(tree.new ResultListener() {

				@Override
				public void onResult(RenderableEntity myValue) {
					result.add(myValue);
				}
			}, position.x, position.y, maxDistance);
		}
		return result;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		Vec p = myGlCamera.getPosition();
		EfficientList<RenderableEntity> list = getList(p.x, p.y);
		for (int i = 0; i < list.myLength; i++) {
			RenderableEntity obj = list.get(i);
			if (obj != null)
				obj.update(timeDelta, this, stack);
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		for (int i = 0; i < allItems.myLength; i++) {
			allItems.get(i).accept(visitor);
		}
		return true;
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		Vec p = myGlCamera.getPosition();
		EfficientList<RenderableEntity> list = getList(p.x, p.y);
		for (int i = 0; i < list.myLength; i++) {
			RenderableEntity obj = list.get(i);
			if (obj != null)
				obj.render(gl, this, stack);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized EfficientList<RenderableEntity> getList(float x,
			float y) {
		if (itemsInRange != null
				&& needsNoRecalculation(x - oldX, myRecalcDistanceMin,
						myRecalcDistanceMax)
				&& needsNoRecalculation(y - oldY, myRecalcDistanceMin,
						myRecalcDistanceMax)) {
			return itemsInRange;
		} else {
			if (itemsInRange == null)
				itemsInRange = new EfficientList<RenderableEntity>();
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

	@Override
	public void clear() {
		allItems.clear();
		tree.clear();
		wasClearedAtLeastOnce = true;
	}

	@Override
	public void removeEmptyItems() {
		for (int i = 0; i < allItems.myLength; i++) {
			if (allItems.get(i) instanceof Container) {
				Container c = (Container) allItems.get(i);
				if (c.isCleared())
					remove((RenderableEntity) c);
			}
		}
	}

	@Override
	public boolean isCleared() {
		return allItems.isEmpty() && wasClearedAtLeastOnce;
	}

	@Override
	public int length() {
		return allItems.myLength;
	}

	@Override
	public EfficientList<RenderableEntity> getAllItems() {
		return allItems;
	}

	@Override
	public boolean add(RenderableEntity newElement) {
		if (newElement instanceof HasPosition)
			return add((HasPosition) newElement);
		return false;
	}

	private boolean add(HasPosition x) {
		Vec pos = x.getPosition();
		if (pos != null) {
			addToTree(x, pos);
			addToAllItemsList(x);
			return true;
		}
		return false;
	}

	private void addToAllItemsList(HasPosition x) {
		if (allItems == null)
			allItems = new EfficientList<RenderableEntity>();
		allItems.add((RenderableEntity) x);
	}

	private boolean insertInAllItemsList(int pos, RenderableEntity item) {
		if (allItems == null)
			allItems = new EfficientList<RenderableEntity>();
		return allItems.insert(pos, item);
	}

	private void addToTree(HasPosition x, Vec pos) {
		if (tree == null)
			tree = new QuadTree<RenderableEntity>();
		tree.add(pos.x, pos.y, (RenderableEntity) x);
	}

	@Override
	public boolean remove(RenderableEntity x) {
		boolean rt = tree.remove(x);
		boolean rl = allItems.remove(x);
		if ((rt && !rl) || (rl && !rt))
			Log.e(LOG_TAG,
					"Inconsistency in tree und allItems list while removing!");
		if (rt && rl)
			return true;
		return false;
	}

	/**
	 * The current internal tree will be deleted and recreated. This is
	 * expensive so do not call this too often!
	 */
	public void rebuildTree() {
		tree.clear();
		for (int i = 0; i < allItems.myLength; i++) {
			this.add(allItems.get(i));
		}
	}

	@Override
	public boolean insert(int pos, RenderableEntity item) {
		if (item instanceof HasPosition) {
			boolean result = insertInAllItemsList(pos, item);
			if (result)
				addToTree((HasPosition) item,
						((HasPosition) item).getPosition());
			return result;
		}
		return false;
	}

}
