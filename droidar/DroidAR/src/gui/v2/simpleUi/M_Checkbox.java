package gui.v2.simpleUi;

import gui.v2.simpleUi.uiDecoration.UiDecoratable;
import gui.v2.simpleUi.uiDecoration.UiDecorator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class M_Checkbox implements ModifierInterface, UiDecoratable {

	private CheckBox e;
	private UiDecorator myDecorator;

	@Override
	public View getView(Context context) {
		LinearLayout l = new LinearLayout(context);
		l.setGravity(Gravity.CENTER_VERTICAL);
		l.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING,
				DEFAULT_PADDING);

		LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
		LayoutParams p2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);

		TextView t = new TextView(context);
		t.setLayoutParams(p);
		t.setText(this.getVarName());

		l.addView(t);

		// TODO replace by better view representative:
		e = new CheckBox(context);
		e.setLayoutParams(p2);
		e.setChecked(loadVar());

		l.addView(e);

		if (myDecorator != null) {
			int level = myDecorator.getCurrentLevel();
			myDecorator.decorate(context, t, level, UiDecorator.TYPE_INFO_TEXT);
			myDecorator.decorate(context, e, level, UiDecorator.TYPE_EDIT_TEXT);
		}

		return l;
	}

	@Override
	public boolean assignNewDecorator(UiDecorator decorator) {
		myDecorator = decorator;
		return true;
	}

	public abstract boolean loadVar();

	public abstract CharSequence getVarName();

	public abstract boolean save(boolean newValue);

	@Override
	public boolean save() {
		return save(e.isChecked());
	}

}