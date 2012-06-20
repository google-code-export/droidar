package gui.v2.simpleUi;

import gui.v2.simpleUi.uiDecoration.UiDecoratable;
import gui.v2.simpleUi.uiDecoration.UiDecorator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class M_TextInput implements ModifierInterface, UiDecoratable {

	private EditText editText;
	private UiDecorator myDecorator;

	@Override
	public View getView(Context context) {
		LinearLayout container = new LinearLayout(context);
		container.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
		LayoutParams p2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);

		TextView nameText = new TextView(context);
		nameText.setText(getVarName());
		nameText.setLayoutParams(p);
		container.addView(nameText);

		editText = new EditText(context);
		editText.setLayoutParams(p2);
		editText.setText(load());
		container.addView(editText);
		container.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING,
				DEFAULT_PADDING);

		if (myDecorator != null) {
			int currentLevel = myDecorator.getCurrentLevel();
			myDecorator.decorate(context, nameText, currentLevel + 1,
					UiDecorator.TYPE_INFO_TEXT);
			myDecorator.decorate(context, editText, currentLevel + 1,
					UiDecorator.TYPE_EDIT_TEXT);
		}
		return container;
	}

	@Override
	public boolean assignNewDecorator(UiDecorator decorator) {
		myDecorator = decorator;
		return true;
	}

	@Override
	public boolean save() {
		return save(editText.getText().toString());
	}

	public abstract String load();

	public abstract String getVarName();

	public abstract boolean save(String newText);

}
