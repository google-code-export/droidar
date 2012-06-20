package gui.v2.simpleUi;

import gui.v2.simpleUi.uiDecoration.UiDecoratable;
import gui.v2.simpleUi.uiDecoration.UiDecorator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class M_InfoText implements ModifierInterface, UiDecoratable {

	private String myText;
	private Bitmap myIcon;
	private int myIconId;
	private float myTextSize;
	private UiDecorator myDecorator;

	public M_InfoText(String text) {
		this(null, text);
	}

	public M_InfoText(Bitmap icon, String text) {
		myIcon = icon;
		myText = text;
	}

	public M_InfoText(int iconId, String text) {
		myIconId = iconId;
		myText = text;
	}

	public M_InfoText(int iconId, String text, float manualTextSize) {
		this(iconId, text);
		myTextSize = manualTextSize;
	}

	@Override
	public View getView(Context context) {

		int bottomAndTopPadding = 4;
		int textPadding = 7;
		int iconPadding = 10;

		LinearLayout l = new LinearLayout(context);
		l.setGravity(Gravity.CENTER_VERTICAL);

		l.setPadding(0, bottomAndTopPadding, 0, bottomAndTopPadding);

		ImageView i = null;
		if (myIcon != null) {
			i = new ImageView(context);

			i.setPadding(0, iconPadding, iconPadding, iconPadding);
			i.setImageBitmap(myIcon);
			l.addView(i);
		} else if (myIconId != 0) {
			i = new ImageView(context);

			i.setPadding(0, iconPadding, iconPadding, iconPadding);
			i.setImageResource(myIconId);
			l.addView(i);
		}

		TextView t = new TextView(context);
		t.setText(myText);
		t.setPadding(textPadding, textPadding, textPadding, textPadding);
		
		l.addView(t);

		if (myTextSize != 0)
			t.setTextSize(myTextSize);

		if (myDecorator != null) {
			int level = myDecorator.getCurrentLevel();
			if (i != null)
				myDecorator.decorate(context, i, level + 1,
						UiDecorator.TYPE_ICON);
			myDecorator.decorate(context, t, level + 1,
					UiDecorator.TYPE_INFO_TEXT);
		}

		return l;
	}

	@Override
	public boolean assignNewDecorator(UiDecorator decorator) {
		myDecorator = decorator;
		return true;
	}

	@Override
	public boolean save() {
		return true;
	}

}