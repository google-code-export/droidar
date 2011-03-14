package commands.gl;

import gl.GLFactory;
import gl.MeshComponent;
import gl.animations.Animation;
import util.Wrapper;
import android.util.Log;

import commands.undoable.UndoableCommand;

public class CommandAddAnimation extends UndoableCommand {

	private final Wrapper myTarget;
	private Animation myAnimation;

	// public CommandAddAnimation(MeshComponent target, Animation animation) {
	// myTarget=new SelectionInfo();
	// myTarget.mySelectionObject = target;
	// myAnimation = animation;
	// }

	public CommandAddAnimation(Wrapper target, Animation animation) {
		myTarget = target;
		myAnimation = animation;
	}

	@Override
	public boolean override_do() {
		Log.d("Commands",
				"Trying to add " + myAnimation + " to " + myTarget.getObject());
		if (myTarget.getObject() instanceof MeshComponent) {
			Log.d("Commands", "    Animation set correctly.");
			((MeshComponent) myTarget.getObject()).addAnimation(myAnimation
					.copy());
			return true;
		}
		return false;
	}

	@Override
	public boolean override_undo() {
		if (myTarget.getObject() instanceof MeshComponent) {
			GLFactory.getInstance().removeAnimationFromTargetsAnimationGroup(
					(MeshComponent) myTarget.getObject(), myAnimation);
			return true;
		}
		return false;
	}

}
