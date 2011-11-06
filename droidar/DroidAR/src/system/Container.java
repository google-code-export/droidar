package system;

import util.EfficientList;

public interface Container<T> {

	public void clear();

	public void removeEmptyItems();

	/**
	 * @return true if this object was cleared at least once
	 */
	public boolean isCleared();

	public int length();

	public EfficientList<T> getAllItems();

	boolean add(T newElement);

	boolean remove(T x);

	void insert(int pos, T item);

}
