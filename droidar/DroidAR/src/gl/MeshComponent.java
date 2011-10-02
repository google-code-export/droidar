package gl;

import geo.GeoObj;
import gl.animations.Animation;
import gl.animations.AnimationGroup;

import javax.microedition.khronos.opengles.GL10;

import listeners.SelectionListener;
import system.ParentStack;
import util.Vec;
import util.Wrapper;
import worldData.AbstractObj;
import worldData.Obj;
import worldData.Updateable;
import android.opengl.Matrix;
import android.util.Log;

import commands.Command;
import commands.undoable.UndoableCommand;
import components.Component;

public abstract class MeshComponent implements Component, ParentMesh,
		SelectionListener, Renderable {

	private static final String LOG_TAG = "MeshComp";
	/**
	 * positive x value is in east direction (along red axis) positive y value
	 * is i north direction (along green axis) positive z value is in sky
	 * direction
	 */
	public Vec myPosition = new Vec(); // TODO think if it would be better not
	// to init here?
	/**
	 * a vector that describes how the MeshComp is rotated. For example:
	 * Vec(90,0,0) would rotate it 90 degree around the x axis
	 */
	public Vec myRotation;
	public Vec myScale;
	public Color myColor;

	public Color myPickColor;

	public boolean graficAnimationActive = true;
	public Animation myAnimation;
	public boolean showObjectCoordinateAxis = false;

	private AbstractObj myParentObj;
	private ParentMesh myParentMesh;
	private Command myOnClickCommand;
	private Command myOnLongClickCommand;
	private Command myOnMapClickCommand;
	private Command myOnDoubleClickCommand;

	/**
	 * how to extract euler angles from a rotation matrix
	 * http://paulbourke.net/geometry/eulerangle/ TODO provide a method for this
	 * extraction
	 */
	private float[] markerRotationMatrix;

	/**
	 * for now only used for marker detection
	 */
	public void setRotationMatrix(float[] rotationMatrix) {
		this.markerRotationMatrix = rotationMatrix;
	}

	/**
	 * Example. An object at position 5,5,5 is rotated by an rotation matrix
	 * (set via {@link MeshComponent#setRotationMatrix(float[])} and we want to
	 * know there the point 0,0,1 (which normaly without rotation would be at
	 * 5,5,6 ) is now. then we can use this method and pass 0,0,1 and we will
	 * get the correct world coordinates
	 * 
	 * @param modelSpaceCoords
	 * @return the coordinates in the world system
	 */
	public Vec getWorldCoordsFromModelSpacePosition(Vec modelSpaceCoords) {
		float[] resultVec = new float[3];
		float[] modelSpaceCoordsVec = { modelSpaceCoords.x, modelSpaceCoords.y,
				modelSpaceCoords.z };
		Matrix.multiplyMV(resultVec, 0, markerRotationMatrix, 0,
				modelSpaceCoordsVec, 0);
		return new Vec(resultVec[0] + myPosition.x,
				resultVec[1] + myPosition.y, resultVec[2] + myPosition.z);
	}

	protected MeshComponent(Color canBeNull) {
		this.myColor = canBeNull;
	}

	/**
	 * resize the Mesh equally in all 3 dimensions
	 * 
	 * @param scaleRate
	 */
	public void scaleEqual(float scaleRate) {
		this.myScale = new Vec(scaleRate, scaleRate, scaleRate);
	}

	public void loadPosition(GL10 gl) {
		if (myPosition != null)
			gl.glTranslatef(myPosition.x, myPosition.y, myPosition.z);
	}

	public void loadRotation(GL10 gl) {

		if (markerRotationMatrix != null) {
			gl.glMultMatrixf(markerRotationMatrix, 0);
		}

		if (myRotation != null) {
			/*
			 * this order is important. first rotate around the blue-z-axis
			 * (like a compass) then the the green-y-axis and red-x-axis. the
			 * order of the x and y axis rotations normaly is not important but
			 * first x and then y is better in this case because of
			 * Vec.calcRotationVec which may be extendet to add also a y
			 * rotation which then would have to be rotated last to not make the
			 * x-axis rotation wrong. so z x y is the best rotation order but
			 * normaly z y x would work too:
			 */
			gl.glRotatef(myRotation.z, 0, 0, 1);
			gl.glRotatef(myRotation.x, 1, 0, 0);
			gl.glRotatef(myRotation.y, 0, 1, 0);
		}

	}

	public void setScale(GL10 gl) {
		if (myScale != null)
			gl.glScalef(myScale.x, myScale.y, myScale.z);
	}

	public synchronized void render(GL10 gl, Renderable parent,
			ParentStack<Renderable> stack) {
		// store current matrix and then modify it:
		setMatrix(gl);
		// if (showObejctCoordinateAxis) CordinateAxis.draw(gl);
		draw(gl, parent, stack);
		// restore old matrix:
		gl.glPopMatrix();
	}

	public synchronized void setMatrix(GL10 gl) {
		gl.glPushMatrix();
		loadPosition(gl);
		setScale(gl);
		loadRotation(gl);

		// first draw the color of the mesh:
		if (myColor != null && !ObjectPicker.readyToDrawWithColor) {
			gl.glColor4f(myColor.red, myColor.green, myColor.blue,
					myColor.alpha);
		}
		/*
		 * AFTER setting all properties of the mesh (position, color, ...) add
		 * animation properties. if those would be added before loading the
		 * properties the animation might be overwritten. for example a color
		 * morph animation could not be seen if the mesh itself has a color
		 * defined
		 */
		if (myAnimation != null) {
			/*
			 * TODO if there is more then 1 animation there is a big problem (?)
			 * two rotate animations and one moveAnimation then first rotation
			 * then move and then second rotate would be diferent to rotate
			 * rotate move!
			 */
			myAnimation.setAnimationMatrix(gl, this);
		}

		/*
		 * after setting the color and the animations where the color might be
		 * changed again, check if selectionMode is activated. if so color the
		 * mesh in the corresponding selectionColor
		 */
		if (myPickColor != null && ObjectPicker.readyToDrawWithColor) {

			// //TODO remove:
			// byte[] b = ObjectPicker.getByteArrayFromColor(myPickColor);
			// String key = "" + b[0] + b[1] + b[2];
			// Log.d("Color Picking", "drawing mesh with color= " + key);

			gl.glColor4f(myPickColor.red, myPickColor.green, myPickColor.blue,
					myPickColor.alpha);
		}
	}

	public abstract void draw(GL10 gl, Renderable parent,
			ParentStack<Renderable> stack);

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if ((myAnimation != null) && (graficAnimationActive)) {
			Obj obj = null;
			if (parent instanceof Obj)
				obj = (Obj) parent;
			else if (stack != null)
				obj = stack.getFirst(Obj.class);
			// if the animation does not need to be animated anymore..
			if (!myAnimation.update(timeDelta, obj, this)) {
				// ..remove it:
				Log.d(LOG_TAG, "Animation " + myAnimation
						+ " will now be removed from mesh because it "
						+ "is finished (returned false on update())");
				myAnimation = null;
			}
		}
		return true;
	}

	/**
	 * when this is called the mesh can be selected and the onClick,
	 * onLongCLick.. {@link UndoableCommand}s set for this mesh will be executed
	 * if it is clicked
	 */
	public void enableMeshPicking() {
		enableMeshPicking(this);
	}

	/**
	 * @param selectionInterface
	 *            can be the MeshComponent itself or another
	 *            {@link SelectionListener} to inform that instead (eg the
	 *            parent {@link Obj} or {@link GeoObj})
	 */
	public void enableMeshPicking(SelectionListener selectionInterface) {
		Log.d(LOG_TAG, "Enabling picking for: " + this);
		// create a random picking color:
		Color c = Color.getRandomRGBColor();
		if (myColor != null) {
			// if the mesh has a color, use this to avoid screen-flashing;
			c.copyValues(myColor);
		}
		Log.v(LOG_TAG, "   > Sending " + c + " to ColorPicker");

		Wrapper selectionsWrapper = new Wrapper(selectionInterface);

		myPickColor = ObjectPicker.getInstance().registerMesh(
				selectionsWrapper, c);
		Log.v(LOG_TAG, "   > myPickColor=" + myPickColor);
	}

	public ParentMesh getMyParentMesh() {
		return myParentMesh;
	}

	public AbstractObj getMyParentObj() {
		if (myParentObj == null) {
			final ParentMesh p = myParentMesh;
			if (p != null)
				return p.getMyParentObj();
		}
		return myParentObj;
	}

	public void setMyParentMesh(ParentMesh parent) {
		if (myParentMesh != null) {
			Log.w(LOG_TAG, "The parentObject (" + myParentMesh + ") of " + this
					+ " was changed to " + parent);
		}
		myParentMesh = parent;
	}

	public Vec getAbsolutePosition() {
		Vec pos;
		if (myPosition != null) {
			pos = myPosition.copy();
		} else {
			pos = new Vec();
		}
		final ParentMesh p = getMyParentMesh();
		if (p != null) {
			pos.add(p.getAbsolutePosition());
		}
		return pos;
	}

	public Command getOnClickCommand() {
		// if the mesh does not have a onClickCommand itself the parent command
		// is used:
		if (myOnClickCommand == null)
			return getMyParentObj().getOnClickCommand();
		return myOnClickCommand;
	}

	public Command getOnLongClickCommand() {
		if (myOnLongClickCommand == null)
			return getMyParentObj().getOnLongClickCommand();
		return myOnLongClickCommand;
	}

	public Command getOnMapClickCommand() {
		if (myOnMapClickCommand == null)
			return getMyParentObj().getOnMapClickCommand();
		return myOnMapClickCommand;
	}

	@Override
	public Command getOnDoubleClickCommand() {
		if (myOnDoubleClickCommand == null)
			return getMyParentObj().getOnDoubleClickCommand();
		return myOnDoubleClickCommand;
	}

	@Override
	public void setOnClickCommand(Command c) {
		enableMeshPicking(this);
		myOnClickCommand = c;
	}

	@Override
	public void setOnDoubleClickCommand(Command c) {
		enableMeshPicking(this);
		myOnDoubleClickCommand = c;
	}

	@Override
	public void setOnLongClickCommand(Command c) {
		enableMeshPicking(this);
		myOnLongClickCommand = c;
	}

	/**
	 * @param c
	 * @param objToInform
	 *            set the {@link SelectionListener} manually (eg the parent
	 *            {@link Obj}
	 */
	public void setOnClickCommand(Command c, SelectionListener objToInform) {
		enableMeshPicking(objToInform);
		myOnClickCommand = c;
	}

	public void setOnDoubleClickCommand(Command c, SelectionListener objToInform) {
		enableMeshPicking(objToInform);
		myOnDoubleClickCommand = c;
	}

	public void setOnLongClickCommand(Command c, SelectionListener objToInform) {
		enableMeshPicking(objToInform);
		myOnLongClickCommand = c;
	}

	public void setOnMapClickCommand(Command c) {
		myOnMapClickCommand = c;
	}

	public MeshComponent clone() throws CloneNotSupportedException {
		if (this instanceof Shape) {
			return ((Shape) this).clone();
		}
		if (this instanceof MeshGroup) {
			return ((MeshGroup) this).clone();
		}
		Log.e("", "MeshComponent.clone() subclass missed, add it there");
		return null;
	}

	public void setMyParentObj(AbstractObj obj) {
		myParentObj = obj;
	}

	public void addAnimation(Animation animation) {
		addAnimationToTargetsAnimationGroup(this, animation);
	}

	public static void addAnimationToTargetsAnimationGroup(
			MeshComponent target, Animation a) {
		if (!(target.myAnimation instanceof AnimationGroup)) {
			AnimationGroup ag = new AnimationGroup();
			// keep the old animation:
			if (target.myAnimation != null)
				ag.add(target.myAnimation);
			// and change animation to the created group:
			target.myAnimation = ag;
		}
		((AnimationGroup) target.myAnimation).add(a);
	}

}
