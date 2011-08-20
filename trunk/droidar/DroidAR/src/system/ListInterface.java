package system;

import gui.CustomBaseAdapter;
import gui.CustomListActivity;
import util.EfficientList;

/**
 * any object which should be displayed inside a {@link CustomListActivity} by
 * using a {@link CustomBaseAdapter} have to implement this interface
 * 
 * 
 * 
 * @author Spobo
 * 
 */
public interface ListInterface<T> {

	public void clear();

	public void removeEmptyItems();

	/**
	 * @return true if this object was cleared at least once
	 */
	public boolean isCleared();

	public int length();

	public EfficientList<T> getNodes();

}
