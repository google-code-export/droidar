package gui;

import system.ListInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomBaseAdapter extends BaseAdapter {

	private ListInterface myList;

	public CustomBaseAdapter(ListInterface list) {
		myList = list;
		Log.d("GUI", "CustomBaseAdapter created with list: " + list);
	}

	@Override
	public int getCount() {
		Log.d("GUI", "CustomBaseAdapter list has length="
				+ myList.getNodes().myLength);
		return myList.getNodes().myLength;
	}

	// stelle finden wieso der geograph nicht aus dem baseaddapter rausfliegt??

	@Override
	public ListItem getItem(int pos) {
		if (pos >= myList.getNodes().myLength)
			return null;
		return (ListItem) myList.getNodes().get(pos);
	}

	@Override
	public long getItemId(int pos) {
		/*
		 * every item has its position as id
		 */
		return pos;
	}

	@Override
	public View getView(int itemPos, View convertView, ViewGroup parent) {
		Log.d("GUI", "Getting view for item " + getItem(itemPos));
		View v = getItem(itemPos).getMyListItemView(convertView, parent);
		if (v == null) {
			Log.e("GUI", "    -> Item has no own view");
			// v = defaultItemView(getItem(itemPos), convertView, parent);
		}
		Log.d("GUI", "    -> View is " + v);
		return v;
	}

}
