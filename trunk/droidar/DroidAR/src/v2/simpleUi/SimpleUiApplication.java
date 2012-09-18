package v2.simpleUi;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

/**
 * You need to add <br>
 * android:name="v2.simpleUi.SimpleUiApplication"<br>
 * in the application tag in the manifest.
 * 
 * 
 * 
 * ((SimpleUiApplication) a.getApplication()).getTransfairList().put( newKey,
 * itemToDisplay);
 * 
 * 
 * 
 * 
 * @author Simon Heinen
 * 
 */
public class SimpleUiApplication extends Application {
	private static final String LOG_TAG = "SimpleUiApplication";
	private HashMap<String, Object> transfairList;

	public HashMap<String, Object> getTransfairList() {
		if (transfairList == null)
			transfairList = new HashMap<String, Object>();
		return transfairList;
	}
}
