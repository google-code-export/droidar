package commands.obj;

import util.Wrapper;
import worldData.AbstractObj;
import worldData.ObjGroup;

import commands.undoable.UndoableCommand;

public class CommandAddObjToObjGroup extends UndoableCommand {

	private Wrapper mySourceW;
	private Wrapper myTargetW;
	private ObjGroup backup;
	private AbstractObj backupObj;

	public CommandAddObjToObjGroup(Wrapper sourceW, Wrapper targetW) {
		mySourceW = sourceW;
		myTargetW = targetW;
	}

	@Override
	public boolean override_do() {
		if (myTargetW.getObject() instanceof ObjGroup
				&& mySourceW.getObject() instanceof AbstractObj) {
			backup = (ObjGroup) myTargetW.getObject();
			backupObj = (AbstractObj) mySourceW.getObject();
			backup.add(backupObj);
			return true;
		}
		return false;
	}

	@Override
	public boolean override_undo() {
		if (backup != null && backupObj != null) {
			return backup.getNodes().remove(backupObj);
		}
		return false;
	}

}
