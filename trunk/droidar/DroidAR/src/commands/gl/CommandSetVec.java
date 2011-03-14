package commands.gl;

import util.Vec;

import commands.undoable.UndoableCommand;

public class CommandSetVec extends UndoableCommand {

	private Vec backup;
	private Vec myTarget;
	private Vec myValue;

	public CommandSetVec(Vec target, Vec value) {
		myTarget = target;
		myValue = value;
		backup = target.copy();
	}

	@Override
	public boolean override_do() {
		myTarget.setToVec(myValue);
		return true;
	}

	@Override
	public boolean override_undo() {
		myTarget.setToVec(backup);
		return true;
	}

}
