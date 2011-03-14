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

	public boolean isEmpty();

	public int length();

	public EfficientList<T> getMyItems();

}
