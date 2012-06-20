package gui.v2.simpleUi.uiDecoration;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultUiDecorator implements UiDecorator {
	private static final String LOG_TAG = "DefaultUiDecorator";
	private int myCurrentLevel;
	private boolean showInfoIfDecorationNotSpecified;

	public DefaultUiDecorator(boolean showInfoIfDecorationNotSpecified) {
		this.showInfoIfDecorationNotSpecified = showInfoIfDecorationNotSpecified;
	}

	@Override
	public int getCurrentLevel() {
		return myCurrentLevel;
	}

	@Override
	public void setCurrentLevel(int currentLevel) {
		myCurrentLevel = currentLevel;
	}

	private void showInfo(View targetView, int level, int type) {
		if (showInfoIfDecorationNotSpecified) {
			Log.i(LOG_TAG, "No decoration for "
					+ targetView.getClass().toString() + " (level=" + level
					+ ", UiDecorator.type=" + type);
		}
	}

	@Override
	public boolean decorate(Context context, View targetView, int level,
			int type) {
		showInfo(targetView, level, type);
		return false;
	}

	@Override
	public boolean decorate(Context context, ImageView targetView, int level,
			int type) {
		showInfo(targetView, level, type);
		return false;
	}

	@Override
	public boolean decorate(Context context, Button targetView, int level,
			int type) {
		showInfo(targetView, level, type);
		return false;
	}

	@Override
	public boolean decorate(Context context, TextView targetView, int level,
			int type) {
		showInfo(targetView, level, type);
		return false;
	}

	@Override
	public boolean decorate(Context context, EditText targetView, int level,
			int type) {
		showInfo(targetView, level, type);
		return false;
	}

}
