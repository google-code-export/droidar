package commands.gl;

import util.L;
import util.Vec;

import commands.undoable.UndoableCommand;

public class CommandAddVec extends UndoableCommand {

	private Vec myTarget;
	private Vec myValue;

	public CommandAddVec(Vec target, Vec value) {
		myTarget = target;
		myValue = value;

	}

	@Override
	public boolean override_do() {
		L.out("Adding " + myValue + " to " + myTarget);
		myTarget.add(myValue);
		return true;
	}

	@Override
	public boolean override_undo() {
		myTarget.sub(myValue);
		return true;
	}

}
