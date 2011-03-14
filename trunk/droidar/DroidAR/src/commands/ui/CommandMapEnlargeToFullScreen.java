package commands.ui;

import geo.GMap;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import commands.undoable.UndoableCommand;

public class CommandMapEnlargeToFullScreen extends UndoableCommand {

	private GMap myMap;
	private LayoutParams myBackupParams;

	public CommandMapEnlargeToFullScreen(GMap map) {
		myMap = map;
	}

	@Override
	public boolean override_do() {

		myBackupParams = myMap.myBox.getLayoutParams();

		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		p.height = LayoutParams.FILL_PARENT;
		p.weight = 0;
		myMap.myBox.setLayoutParams(p);
		return true;
	}

	@Override
	public boolean override_undo() {
		if (myBackupParams != null) {
			myMap.myBox.setLayoutParams(myBackupParams);
			return true;
		}
		return false;
	}

}
