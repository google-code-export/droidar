package commands.system;

import system.Container;
import util.Wrapper;
import android.util.Log;

import commands.Command;

public class CommandRemoveEmptyItemsInWrapper extends Command {

	private Wrapper myWrapper;

	public CommandRemoveEmptyItemsInWrapper(Wrapper wrapper) {
		myWrapper = wrapper;
	}

	@Override
	public boolean execute() {
		if (myWrapper.getObject() instanceof Container) {
			((Container) myWrapper.getObject()).removeEmptyItems();
			Log.d("Commands", "" + myWrapper.getObject()
					+ " removed all emty items (new length="
					+ ((Container) myWrapper.getObject()).length()
					+ ")");
			return true;
		}
		return false;
	}

}
