package commands.system;

import system.Container;
import util.Wrapper;

import commands.Command;

/**
 * calls the clear metho on the object inside the {@link Wrapper} if it has the
 * type {@link Container}
 * 
 * @author Spobo
 * 
 */
public class CommandClearWrapperObject extends Command {

	private Wrapper myWrapper;
	private boolean usePassedObjectIfThereIsOne;

	public CommandClearWrapperObject(Wrapper wrapper) {
		myWrapper = wrapper;
	}

	public CommandClearWrapperObject(Wrapper wrapper, String string) {
		this(wrapper);
		getInfoObject().setShortDescr(string);
	}

	public CommandClearWrapperObject(Wrapper wrapper,
			boolean usePassedObjectIfThereIsOne) {
		this(wrapper);
		this.usePassedObjectIfThereIsOne = usePassedObjectIfThereIsOne;
	}

	@Override
	public boolean execute() {
		if (myWrapper.getObject() instanceof Container) {
			((Container) myWrapper.getObject()).clear();
			return true;
		}
		return false;
	}

	@Override
	public boolean execute(Object transfairObject) {
		if (usePassedObjectIfThereIsOne
				&& transfairObject instanceof Container) {
			((Container) transfairObject).clear();
			return true;
		}
		return false;
	}

}
