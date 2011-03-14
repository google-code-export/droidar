package util;

import android.util.Log;
import android.widget.TextView;

public class L {

	private static final boolean DEBUG = true;

	public static void out(String debugText) {
		if (DEBUG)
			Log.d("Debug Log", debugText);
	}

	public static void floatMatrixToString(TextView v, String text, float[] a) {
		String s = "";
		s += "Matrix: " + text + "\n";
		s += "\t " + a[0] + "," + a[1] + "," + a[2] + "," + a[3] + " \n";
		s += "\t " + a[4] + "," + a[5] + "," + a[6] + "," + a[7] + " \n";
		s += "\t " + a[8] + "," + a[9] + "," + a[10] + "," + a[11] + " \n";
		s += "\t " + a[12] + "," + a[13] + "," + a[14] + "," + a[15] + " \n";

		v.setText(s);
	}

}
