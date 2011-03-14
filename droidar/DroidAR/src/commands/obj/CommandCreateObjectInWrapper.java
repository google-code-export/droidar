package commands.obj;

import listeners.ObjectCreateListener;
import util.Wrapper;

import commands.Command;

public class CommandCreateObjectInWrapper extends Command {

	private Wrapper myTargetW;
	private ObjectCreateListener myListener;

	public CommandCreateObjectInWrapper(Wrapper targetWrapper,
			ObjectCreateListener listener) {
		myTargetW = targetWrapper;
		myListener = listener;
	}

	@Override
	public boolean execute() {
		if (myListener != null) {
			return myListener.setWrapperToObject(myTargetW);
		}
		return false;
	}
}
