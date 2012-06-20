package gui.v2.simpleUi;

import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Don't forget to add<br>
 * <br>
 * 
 * < activity android:name="v2.simpleUi.SimpleUI" android:theme=
 * "@android:style/Theme.Translucent"/> <br>
 * <br>
 *  
 * to your Manifest.xml file!
 * 
 * @author Simon Heinen
 * 
 */
public class SimpleUI extends Activity {

	private static final String TRANSFAIR_KEY_ID = "transfairKey";
	private static HashMap<String, Object> transfairList;

	/**
	 * Will save changes when the close button is pressed
	 * 
	 * @param context
	 * @param closeButtonText
	 *            e.g. "Save & Close"
	 * @param itemsToDisplay
	 * @return
	 */
	public static boolean showInfoDialog(Context context,
			String closeButtonText, final M_Container itemsToDisplay) {
		itemsToDisplay.add(new M_Button(closeButtonText) {

			@Override
			public void onClick(Context context, Button clickedButton) {
				itemsToDisplay.save();
				if (context instanceof Activity)
					((Activity) context).finish();
			}
		});
		return showUi(context, itemsToDisplay);
	}

	/**
	 * @param currentActivity
	 * @param contentToShow
	 *            e.g. a {@link M_Container} which is filled with all the items
	 * @return
	 */
	public static boolean showUi(Context context,
			ModifierInterface modifierToDisplay) {
		Intent intent = new Intent(context, SimpleUI.class);
		if (modifierToDisplay != null) {
			String key = storeObjectInTransfairList(modifierToDisplay);
			/*
			 * The key to the object will be stored in the extras of the intent:
			 */
			intent.putExtra(TRANSFAIR_KEY_ID, key);
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	/**
	 * @param itemToDisplay
	 * @return the key for the location where it is stored
	 */
	private static String storeObjectInTransfairList(Object itemToDisplay) {
		if (transfairList == null)
			transfairList = new HashMap<String, Object>();
		String newKey = new Date().toString();
		transfairList.put(newKey, itemToDisplay);
		return newKey;
	}

	private Object loadObjectFromTransfairList(String key) {
		Object o = transfairList.get(key);
		transfairList.remove(key);
		return o;
	}

	private View myView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {
			String key = getIntent().getExtras().getString(TRANSFAIR_KEY_ID);
			myView = ((ModifierInterface) loadObjectFromTransfairList(key))
					.getView(this);
			setContentView(myView);
		} catch (Exception e) {
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/*
		 * http://stackoverflow.com/questions/151777/how-do-i-save-an-android-
		 * applications-state
		 */
		String key = storeObjectInTransfairList(myView);
		outState.putString(TRANSFAIR_KEY_ID, key);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		View restoredView = (View) loadObjectFromTransfairList(savedInstanceState
				.getString(TRANSFAIR_KEY_ID));
		myView = restoredView;
		((ViewGroup) myView.getParent()).removeView(myView);
		setContentView(restoredView);
	}

}
