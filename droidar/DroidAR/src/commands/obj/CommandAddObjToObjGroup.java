package commands.obj;

import util.Wrapper;
import worldData.AbstractObj;
import worldData.EntityContainer;

import commands.undoable.UndoableCommand;

public class CommandAddObjToObjGroup extends UndoableCommand {

	private Wrapper mySourceW;
	private Wrapper myTargetW;
	private EntityContainer backup;
	private AbstractObj backupObj;

	public CommandAddObjToObjGroup(Wrapper sourceW, Wrapper targetW) {
		mySourceW = sourceW;
		myTargetW = targetW;
	}

	@Override
	public boolean override_do() {
		if (myTargetW.getObject() instanceof EntityContainer
				&& mySourceW.getObject() instanceof AbstractObj) {
			backup = (EntityContainer) myTargetW.getObject();
			backupObj = (AbstractObj) mySourceW.getObject();
			backup.add(backupObj);
			return true;
		}
		return false;
	}

	@Override
	public boolean override_undo() {
		if (backup != null && backupObj != null) {
			return backup.getAllItems().remove(backupObj);
		}
		return false;
	}

}
