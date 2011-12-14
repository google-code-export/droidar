package gamelogic;

import util.IO;
import gui.ListItem;
import gui.simpleUI.ModifierGroup;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import commands.Command;

public abstract class GameElement implements ListItem {

	/**
	 * if this boolean in true a {@link GameElementList#removeEmptyItems()} call
	 * will remove it from the {@link GameElementList}. It is set via
	 * {@link GameElement#setShouldBeRemoved(boolean)}
	 */
	private boolean shouldBeRemoved;
	public String myName;
	public int myIconid;
	public Command myListClickCommand;
	public Command myListLongClickCommand;

	public GameElement(String uniqueName, int iconId) {
		myName = uniqueName;
		myIconid = iconId;
	}

	@Override
	public Command getListClickCommand() {
		return myListClickCommand;
	}

	@Override
	public Command getListLongClickCommand() {
		return myListLongClickCommand;
	}

	@Override
	public View getMyListItemView(View viewToUseIfNotNull, ViewGroup parentView) {
		if (viewToUseIfNotNull instanceof GameElementListItemView) {
			((GameElementListItemView) viewToUseIfNotNull)
					.updateContent(parentView.getContext());
			return viewToUseIfNotNull;
		}
		return new GameElementListItemView(parentView.getContext());
	}

	public boolean shouldBeRemoved() {
		return shouldBeRemoved;
	}

	/**
	 * @param b
	 *            set this to true to remove it from a {@link GameElementList}
	 *            when {@link GameElementList#removeEmptyItems()} is called
	 */
	public void setShouldBeRemoved(boolean b) {
		shouldBeRemoved = b;
	}

	private class GameElementListItemView extends LinearLayout {

		private GameElementView myIconView;
		private TextView myDescriptionView;

		public GameElementListItemView(Context context) {
			super(context);
			myIconView = new GameElementView(context, myIconid);
			myDescriptionView = new TextView(context);
			addView(myIconView);
			addView(myDescriptionView);
			updateContent(context);
		}

		public void updateContent(Context context) {
			myDescriptionView.setText(myName);
			if (myIconid != 0)
				myIconView.setIcon(IO.loadBitmapFromId(context, myIconid));
		}

	}

	public abstract void generateViewGUI(ModifierGroup s);

	public abstract void generateEditGUI(ModifierGroup s);
}
