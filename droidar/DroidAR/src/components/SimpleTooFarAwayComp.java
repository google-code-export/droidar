package components;

import gl.Color;
import gl.GLCamera;
import gl.GLFactory;
import gl.scenegraph.MeshComponent;
import gl.scenegraph.Shape;
import util.Log;
import util.Vec;
import worldData.MoveComp;
import worldData.Updateable;
import android.app.Activity;

import commands.Command;
import commands.ui.CommandShowToast;

public class SimpleTooFarAwayComp extends TooFarAwayComp {

	private static final String LOG_TAG = "SimpleTooFarAwayComp";
	private Shape arrow;
	private MoveComp mover = new MoveComp(3);

	public SimpleTooFarAwayComp(float maxDistance, GLCamera camera,
			final Activity a) {
		super(maxDistance, camera);

		arrow = new Shape(Color.green());
		arrow.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				CommandShowToast.show(a, "Distance: "
						+ (int) arrow.getPosition().getLength() + "m");
				return true;
			}
		});
		arrow.addChild(mover);

	}

	@Override
	public void isNowCloseEnough(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		if (parentsMesh != null)
			parentsMesh.remove(arrow);
	}

	@Override
	public void isNowToFarAway(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		Log.d(LOG_TAG, "Is now to far away");
		if (parentsMesh != null) {

			// Vec lineEndPos = direction.copy().setLength(-5);
			// Vec pos = direction.setLength(direction.getLength() - 10);
			//
			// pos.z -= 5;
			// arrow.setPosition(pos);
			// mover.myTargetPos = pos;
			parentsMesh.addChild(arrow);
		}
	}

	@Override
	public void onGrayZoneEvent(Updateable parent, MeshComponent parentsMesh,
			Vec direction, float grayZonePercent) {
		arrow.getColor().alpha = grayZonePercent / 100;
	}

	@Override
	public void onFarAwayEvent(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		Log.d(LOG_TAG, "far away event");
		Vec lineEndPos = direction.copy().setLength(-10);
		arrow.setMyRenderData(GLFactory.getInstance()
				.newDirectedPath(lineEndPos, null).getMyRenderData());
		Vec pos = direction.setLength(direction.getLength() - 10);
		pos.z -= 5;
		mover.myTargetPos = pos;
	}

}
