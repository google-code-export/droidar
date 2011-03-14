package commands.obj;

import listeners.ObjectEditListener;
import util.Wrapper;

import commands.Command;

public class CommandChangeObjInWrapper extends Command {

	private Wrapper myWrapper;
	private ObjectEditListener myListener;

	public CommandChangeObjInWrapper(Wrapper wrapperToEdit,
			ObjectEditListener listener) {
		myWrapper = wrapperToEdit;
		myListener = listener;
	}

	@Override
	public boolean execute() {
		if (myListener != null) {
			return myListener.onChangeWrapperObject(myWrapper, null);
		}
		return false;
	}

	@Override
	public boolean execute(Object transfairObject) {
		if (myListener != null) {
			return myListener.onChangeWrapperObject(myWrapper, transfairObject);
		}
		return false;
	}

}
