package gui.simpleUI;

import gui.simpleUI.modifiers.Headline;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ModifierGroup extends AbstractModifier {

	private static int groupPadding = 5;
	private String cancelText = "Cancel"; // TODO
	private String okText = "Ok";
	private String myTitle;
	private ArrayList<ModifierInterface> myList = new ArrayList<ModifierInterface>();

	public ModifierGroup() {
	}

	public ModifierGroup(Theme myTheme) {
		setTheme(myTheme);
	}

	public ModifierGroup(String title) {
		myTitle = title;
	}

	/**
	 * this elements can be generated to initiate the save-process for all group
	 * elements
	 * 
	 * @param c
	 * @return
	 */
	public View getCancelAndSaveButtons(final Activity c) {

		LayoutParams layParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);

		LinearLayout l = new LinearLayout(c);
		l.setLayoutParams(layParams);
		l.setPadding(SimpleUI.DEFAULT_PADDING, SimpleUI.DEFAULT_PADDING,
				SimpleUI.DEFAULT_PADDING, SimpleUI.DEFAULT_PADDING);
		l.setGravity(Gravity.CENTER_VERTICAL);

		Button cancelButton = new Button(c);
		cancelButton.setPadding(SimpleUI.DEFAULT_PADDING,
				SimpleUI.DEFAULT_PADDING, SimpleUI.DEFAULT_PADDING,
				SimpleUI.DEFAULT_PADDING);
		cancelButton.setLayoutParams(layParams);
		cancelButton.setText(cancelText);

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				c.finish();
			}
		});
		Button okButton = new Button(c);
		okButton.setPadding(SimpleUI.DEFAULT_PADDING, SimpleUI.DEFAULT_PADDING,
				SimpleUI.DEFAULT_PADDING, SimpleUI.DEFAULT_PADDING);
		okButton.setLayoutParams(layParams);
		okButton.setText(okText);

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (save()) {
					c.finish();
				}
			}
		});
		l.addView(cancelButton);
		l.addView(okButton);

		if (getTheme() != null) {
			getTheme().applyOuter1(l);
			getTheme().applyNormal1(cancelButton);
			getTheme().applyNormal1(okButton);
		}
		return l;
	}

	public void setCancelText(String cancelText) {
		this.cancelText = cancelText;
	}

	public void setOkText(String okText) {
		this.okText = okText;
	}

	public void setMyTitle(String myTitle) {
		this.myTitle = myTitle;
	}

	public void addModifier(ModifierInterface groupElement) {
		if (getTheme() != null && groupElement instanceof AbstractModifier) {
			if (((AbstractModifier) groupElement).getTheme() == null) {
				((AbstractModifier) groupElement).setTheme(getTheme());
			}
		}
		myList.add(groupElement);
	}

	@Override
	public View getView(Context context) {
		LinearLayout linLayout = new LinearLayout(context);
		if (myTitle != null) {
			linLayout.addView(new Headline(myTitle).getView(context));
		}

		/*
		 * after applying the theme the padding has to be set to assure the
		 * correct padding for groups
		 */
		// linLayout.setPadding(groupLeftPadding, linLayout.getPaddingTop(),
		// 0, linLayout.getPaddingBottom());

		linLayout.setPadding(groupPadding, groupPadding, groupPadding,
				groupPadding);

		linLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < myList.size(); i++) {
			linLayout.addView(myList.get(i).getView(context));
		}

		ScrollView sv = new ScrollView(context);
		sv.setPadding(groupPadding, groupPadding, groupPadding, groupPadding);
		sv.addView(linLayout);
		sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		if (getTheme() != null) {
			getTheme().applyOuter1(sv);
		}

		return sv;
	}

	@Override
	public boolean save() {
		boolean result = true;
		for (int i = 0; i < myList.size(); i++) {
			result &= myList.get(i).save();
		}
		return result;
	}

}