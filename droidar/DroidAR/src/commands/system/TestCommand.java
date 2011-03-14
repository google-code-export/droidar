package commands.system;

import util.L;

import commands.undoable.UndoableCommand;

public class TestCommand extends UndoableCommand {

	private int x = 0;

	@Override
	public boolean override_do() {
		x++;
		L.out("Test Command do. Count: " + x);
		return true;
	}

	@Override
	public boolean override_undo() {
		x--;
		L.out("Test Command undo. Count: " + x);
		return true;
	}

}
