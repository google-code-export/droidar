package gamelogic;

import gui.simpleUI.ModifierGroup;

import java.util.HashMap;

import system.Container;
import util.EfficientList;

public abstract class GameElementList<T> implements Container<T> {

	private EfficientList<T> myList = new EfficientList<T>();
	/**
	 * an additional structure for fast searching for special GameElements
	 */
	private HashMap<String, T> mySearchIndex = new HashMap<String, T>();

	@Override
	public void clear() {
		myList.clear();
		mySearchIndex.clear();
	}

	@Override
	public int length() {
		return myList.myLength;
	}

	@Override
	public EfficientList<T> getAllItems() {
		return myList;
	}

	@Override
	public boolean isCleared() {
		return myList.myLength == 0;
	}

	@Override
	public void removeEmptyItems() {
		for (int i = 0; i < myList.myLength; i++) {
			if (myList.get(i) instanceof GameElement) {
				GameElement e = (GameElement) myList.get(i);
				if (e.shouldBeRemoved())
					myList.remove(e);
			}
		}
	}

	@Override
	public boolean add(T item) {

		if (item instanceof GameElement) {
			mySearchIndex.put(((GameElement) item).myName, item);
		}
		return myList.add(item);
	}

	public T get(String uniqueName) {
		return mySearchIndex.get(uniqueName);
	}

	public boolean remove(String uniqueName) {
		T itemToDelete = mySearchIndex.get(uniqueName);
		mySearchIndex.remove(uniqueName);
		return myList.remove(itemToDelete);
	}

	@Override
	public boolean remove(T item) {
		if (item instanceof GameElement) {
			mySearchIndex.remove(((GameElement) item).myName);
		}
		return myList.remove(item);
	}

	public void generateViewGUI(ModifierGroup s) {
		for (int i = 0; i < myList.myLength; i++) {
			Object o = myList.get(i);
			if (o instanceof GameElement) {
				GameElement g = (GameElement) o;
				g.generateViewGUI(s);
			}
		}
	}

	@Override
	public boolean insert(int pos, T item) {
		return myList.insert(pos, item);
	};

	public void generateEditGUI(ModifierGroup s) {
		for (int i = 0; i < myList.myLength; i++) {
			Object o = myList.get(i);
			if (o instanceof GameElement) {
				GameElement g = (GameElement) o;
				g.generateEditGUI(s);
			}
		}
	}

}
