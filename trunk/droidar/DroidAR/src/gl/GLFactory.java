package gl;

import geo.GeoObj;
import gl.animations.Animation;
import gl.animations.AnimationFaceToCamera;
import gl.animations.AnimationGroup;
import gl.animations.AnimationRotate;
import gl.textures.Textured2dShape;
import gl.textures.TexturedShape;

import javax.microedition.khronos.opengles.GL10;

import util.IO;
import util.Vec;
import worldData.Obj;
import worldData.Visitor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

/**
 * Use this factory to understand how to create 3D objects with {@link Shape}s
 * and {@link MeshGroup}s. Often it is more efficient to create the objects you
 * need manually and not combine objects created with this factory. The benefits
 * of algorithmic objects are that they are much more flexible and random
 * {@link Vec}tors can be used to add a unique touch to each object.
 * 
 * Loading object from externmal files like md3 is the alternative to this
 * approach.
 * 
 * 
 * @author Spobo
 * 
 */
public class GLFactory {

	private static final String LOG_TAG = "GLFactory";

	private static GLFactory myInstance = new GLFactory();

	private GLFactory() {
	}

	public static GLFactory getInstance() {
		return myInstance;
	}

	public Shape newSquare(Color canBeNull) {
		Shape s = new Shape(canBeNull);
		s.add(new Vec(-1, 1, 0));
		s.add(new Vec(-1, -1, 0));
		s.add(new Vec(1, -1, 0));

		s.add(new Vec(1, -1, 0));
		s.add(new Vec(1, 1, 0));
		s.add(new Vec(-1, 1, 0));

		return s;
	}

	public MeshGroup newCube(Color canBeNull) {
		MeshGroup g = new MeshGroup();
		Shape s1 = newSquare(canBeNull);
		g.add(s1);

		Shape s2 = newSquare(canBeNull);
		s2.myPosition = new Vec(0, 0, 2);
		g.add(s2);

		Shape s3 = newSquare(canBeNull);
		s3.myPosition = new Vec(0, 1, 1);
		s3.myRotation = new Vec(90, 0, 0);
		g.add(s3);

		Shape s4 = newSquare(canBeNull);
		s4.myPosition = new Vec(0, -1, 1);
		s4.myRotation = new Vec(90, 0, 0);
		g.add(s4);

		return g;
	}

	public Shape newTreangle(Color canBeNull) {
		Shape s = new Shape(canBeNull);
		s.add(new Vec(0, 0, 0.8f));
		s.add(new Vec(0, 0.8f, 0));
		s.add(new Vec(0.8f, 0, 0));
		return s;
	}

	public Shape newHexagon(Color canBeNull) {
		Shape s = new Shape(canBeNull);
		s.add(new Vec(0, -1, 0));
		s.add(new Vec(0, 1, 0));
		s.add(new Vec(1, 0.5f, 0));

		s.add(new Vec(0, -1, 0));
		s.add(new Vec(0, 1, 0));
		s.add(new Vec(-1, 0.5f, 0));

		s.add(new Vec(0, -1, 0));
		s.add(new Vec(1, 0.5f, 0));
		s.add(new Vec(1, -0.5f, 0));

		s.add(new Vec(0, -1, 0));
		s.add(new Vec(-1, 0.5f, 0));
		s.add(new Vec(-1, -0.5f, 0));
		return s;
	}

	/**
	 * @param bitmap
	 * @return A 1 x 1 meter square
	 */
	public MeshComponent newTexturedSquare(String bitmapName, Bitmap bitmap) {
		return newTexturedSquare(bitmapName, bitmap, 1);
	}

	public MeshComponent newTexturedSquare(Context c, int iconId, float size) {
		return newTexturedSquare("" + iconId, IO.loadBitmapFromId(c, iconId),
				size);
	}

	public MeshComponent newTexturedSquare(String bitmapName, Bitmap bitmap,
			float size) {

		if (bitmapName == null) {
			Log.e(LOG_TAG,
					"No bitmap id set, can't be added to Texture Manager!");
			return null;
		}

		if (bitmap == null) {
			Log.e(LOG_TAG, "Passed bitmap was null!");
			return null;
		}

		TexturedShape s = new TexturedShape(bitmapName, bitmap);
		float f = (float) bitmap.getHeight() / (float) bitmap.getWidth();
		float x = size / f;

		float w2 = -x / 2;
		float h2 = -size / 2;

		Log.d(LOG_TAG, "Creating textured mesh for " + bitmapName);
		Log.v(LOG_TAG, "   > bitmap.getHeight()=" + bitmap.getHeight());
		Log.v(LOG_TAG, "   > bitmap.getWidth()=" + bitmap.getWidth());
		Log.v(LOG_TAG, "   > height/width factor=" + f);
		Log.v(LOG_TAG, "   > w2=" + w2);
		Log.v(LOG_TAG, "   > h2=" + h2);

		s.add(new Vec(-w2, 0, -h2), 0, 0);
		s.add(new Vec(-w2, 0, h2), 0, 1);
		s.add(new Vec(w2, 0, -h2), 1, 0);

		s.add(new Vec(w2, 0, h2), 1, 1);
		s.add(new Vec(-w2, 0, h2), 0, 1);
		s.add(new Vec(w2, 0, -h2), 1, 0);

		return s;
	}

	public MeshComponent newTextured2dShape(Bitmap bitmap, String bitmapName) {
		Textured2dShape s = new Textured2dShape(bitmap, bitmapName);
		return s;
	}

	public MeshGroup newArrow() {
		Color top = Color.blue();
		Color bottom = Color.red();
		Color edge1 = Color.red();
		Color edge2 = Color.redTransparent();
		float height = 4f;
		float x = 0.7f;
		float y = 0f;
		return newArrow(x, y, height, top, edge1, bottom, edge2);
	}

	public MeshGroup newCuror() {
		Color top = Color.silver1();
		Color bottom = Color.silver2();
		Color edge1 = Color.blackTransparent();
		Color edge2 = Color.blackTransparent();
		float height = 2;
		float x = 0.7f;
		float y = 0f;
		MeshGroup a = newArrow(x, y, height, top, edge1, bottom, edge2);
		a.myScale = new Vec(0.3f, 0.3f, 0.3f);
		return a;
	}

	private MeshGroup newArrow(float x, float y, float height, Color top,
			Color edge1, Color bottom, Color edge2) {

		MeshGroup pyr = new MeshGroup(null);

		MultiColoredShape s = new MultiColoredShape();

		s.add(new Vec(-x, 0, height), top);
		s.add(new Vec(1, 0, 0), edge1);
		s.add(new Vec(-y, 0, -height), bottom);

		MultiColoredShape s2 = new MultiColoredShape();
		s2.add(new Vec(0, -x, height), top);
		s2.add(new Vec(0, 1, 0), edge2);
		s2.add(new Vec(0, -y, -height), bottom);

		MultiColoredShape s3 = new MultiColoredShape();
		s3.add(new Vec(x, 0, height), top);
		s3.add(new Vec(-1, 0, 0), edge1);
		s3.add(new Vec(y, 0, -height), bottom);

		MultiColoredShape s4 = new MultiColoredShape();
		s4.add(new Vec(0, x, height), top);
		s4.add(new Vec(0, -1, 0), edge2);
		s4.add(new Vec(0, y, -height), bottom);

		pyr.add(s);
		pyr.add(s2);
		pyr.add(s3);
		pyr.add(s4);

		GLFactory.getInstance().addRotateAnimation(pyr, 120, new Vec(0, 0, 1));

		return pyr;
	}

	private void addRotateAnimation(MeshComponent target, int speed,
			Vec rotationVec) {
		AnimationRotate a = new AnimationRotate(speed, rotationVec);
		MeshComponent.addAnimationToTargetsAnimationGroup(target, a);
	}

	public void removeAnimationFromTargetsAnimationGroup(MeshComponent target,
			Animation animation) {
		if (target.myAnimation instanceof AnimationGroup) {
			((AnimationGroup) target.myAnimation).remove(animation);
		} else if (animation == target.myAnimation) {
			target.myAnimation = null;
		}

	}

	public MeshComponent newGrid(Color netColor, float spaceBetweenNetStrings,
			int lineCount) {
		Shape s = new Shape(netColor);
		s.setLineDrawing();
		float coord = (lineCount - 1) * spaceBetweenNetStrings / 2;
		Vec start = new Vec(coord, coord, 0);
		Vec end = new Vec(coord, -coord, 0);
		for (int i = 0; i < lineCount; i++) {
			s.add(start.copy());
			s.add(end.copy());
			start.x -= spaceBetweenNetStrings;
			end.x -= spaceBetweenNetStrings;
		}
		start = new Vec(coord, coord, 0);
		end = new Vec(-coord, coord, 0);
		for (int i = 0; i < lineCount; i++) {
			s.add(start.copy());
			s.add(end.copy());
			start.y -= spaceBetweenNetStrings;
			end.y -= spaceBetweenNetStrings;
		}
		return s;
	}

	public Obj newSolarSystem(Vec position) {
		Obj solarSystem = new Obj();
		MeshGroup sunBox = new MeshGroup();
		if (position != null)
			sunBox.myPosition = position;
		solarSystem.setComp(sunBox);

		MeshGroup earthRing = new MeshGroup();
		MeshGroup earthBox = new MeshGroup();
		earthRing.add(earthBox);

		MeshComponent sun = GLFactory.getInstance().newNSidedPolygonWithGaps(
				20, Color.red());
		GLFactory.getInstance().addRotateAnimation(sun, 30, new Vec(1, 1, 1));
		sunBox.add(sun);

		GLFactory.getInstance().addRotateAnimation(earthRing, 40,
				new Vec(0.5f, 0.3f, 1));
		earthBox.myPosition = new Vec(3, 0, 0);
		sunBox.add(earthRing);

		MeshComponent earth = GLFactory.getInstance().newCircle(Color.green());
		earth.scaleEqual(0.5f);
		earthBox.add(earth);

		MeshGroup moonring = new MeshGroup();

		MeshComponent moon = GLFactory.getInstance().newCircle(Color.white());
		moon.myPosition = new Vec(1, 0, 0);
		moon.scaleEqual(0.2f);
		GLFactory.getInstance().addRotateAnimation(moonring, 80,
				new Vec(0, 1, -1));
		moonring.add(moon);

		earthBox.add(moonring);

		return solarSystem;
	}

	public Obj newHexGroupTest(Vec pos) {
		Obj hex = new Obj();
		MeshGroup g1 = new MeshGroup(null, pos);
		hex.setComp(g1);
		g1.add(this.newHexagon(null));
		MeshGroup g2 = new MeshGroup(Color.blue(), new Vec(0, 5, 0.1f));
		g2.myAnimation = new AnimationRotate(60, new Vec(0, 0, 1));
		g1.add(g2);

		g2.add(this.newHexagon(null));
		MeshGroup g3 = new MeshGroup(Color.red(), new Vec(0, 4, 0));
		g3.myAnimation = new AnimationRotate(30, new Vec(0, 0, 1));
		g2.add(g3);

		g3.add(this.newHexagon(null));
		MeshGroup g4 = new MeshGroup(Color.green(), new Vec(0, 2, 0));
		g4.myAnimation = new AnimationRotate(15, new Vec(0, 0, 1));
		g3.add(g4);

		g4.add(this.newHexagon(null));

		Vec v = g4.getAbsolutePosition();
		System.out.println("absolut Pos: " + v);

		return hex;
	}

	public MeshComponent newDiamond(Color canBeNull) {
		Shape s = new Shape(canBeNull);
		float width = 0.7f;
		float heigth = 2f;
		float c = -0.1f; // a factor for asymmetric shaping in x direction

		Vec top = new Vec(0, 0, heigth);
		Vec bottom = new Vec(0, 0, -heigth);

		Vec e1 = new Vec(-width + c, 0, 0);
		Vec e4 = new Vec(width - c, 0, 0);
		Vec e2 = new Vec(-width / 2 + c, width, 0);
		Vec e6 = new Vec(-width / 2 + c, -width, 0);
		Vec e3 = new Vec(width / 2 - c, width, 0);
		Vec e5 = new Vec(width / 2 - c, -width, 0);

		s.add(top);
		s.add(e1);
		s.add(e2);
		s.add(top);
		s.add(e2);
		s.add(e3);
		s.add(top);
		s.add(e3);
		s.add(e4);
		s.add(top);
		s.add(e4);
		s.add(e5);
		s.add(top);
		s.add(e5);
		s.add(e6);
		s.add(top);
		s.add(e6);
		s.add(e1);

		s.add(bottom);
		s.add(e1);
		s.add(e2);
		s.add(bottom);
		s.add(e2);
		s.add(e3);
		s.add(bottom);
		s.add(e3);
		s.add(e4);
		s.add(bottom);
		s.add(e4);
		s.add(e5);
		s.add(bottom);
		s.add(e5);
		s.add(e6);
		s.add(bottom);
		s.add(e6);
		s.add(e1);

		return s;

	}

	public MeshComponent newDirectedPath(Vec lineEndPos, Color c) {

		Shape s = new Shape(c);
		Vec orth = Vec.getOrthogonalHorizontal(lineEndPos).normalize()
				.mult(0.7f);

		s.add(orth);
		s.add(orth.getNegativeClone());
		s.add(lineEndPos);
		return s;
	}

	public MeshComponent newUndirectedPath(Vec lineEnd, Color c) {

		Vec e2 = Vec.getOrthogonalHorizontal(lineEnd).normalize().mult(0.7f);
		Vec e1 = e2.getNegativeClone();

		Vec l1 = Vec.mult(0.25f, lineEnd);
		Vec l3 = Vec.mult(0.75f, lineEnd);

		Vec e3 = Vec.add(lineEnd, e1);
		Vec e4 = Vec.add(lineEnd, e2);

		Shape s = new Shape(c);

		s.add(e1);
		s.add(e2);
		s.add(l3);

		s.add(e3);
		s.add(e4);
		s.add(l1);

		return s;
	}

	public MeshComponent newNSidedPolygon(int numberOfSides, float radius,
			Color c) {
		Shape s = new Shape(c);

		Vec v = new Vec(radius, 0, 0);
		double factor = 360. / numberOfSides;

		// there have to be n triangles:
		for (int i = 0; i < numberOfSides; i++) {
			s.add(v.copy());
			v.rotateAroundZAxis(factor);
			s.add(v.copy());
			// v.rotateAroundZAxis(factor);
			s.add(new Vec()); // middle
		}
		return s;
	}

	public MeshComponent newCircle(Color c) {
		return newNSidedPolygon(20, 1, c);
	}

	public MeshComponent newNSidedPolygonWithGaps(int numberOfSides, Color c) {
		Shape s = new Shape(c);

		Vec v = new Vec(1, 0, 0);
		double factor = 360 / numberOfSides;

		// there have to be n triangles:
		for (int i = 0; i < numberOfSides / 2; i++) {
			s.add(v.copy());
			v.rotateAroundZAxis(factor);
			s.add(v.copy());
			v.rotateAroundZAxis(factor);
			s.add(new Vec()); // middle
		}
		return s;
	}

	private static final float HEIGHT_TO_SIDE_FACTOR = (float) (2f / Math
			.sqrt(3f));

	public MeshComponent newPyramid(Vec center, float height, Color color) {
		Shape p = new Shape(color);
		// side length:
		float a = HEIGHT_TO_SIDE_FACTOR * Math.abs(height);

		Vec p1 = new Vec(center.x - 1f / 2f * a, center.y - 1f / 3f
				* Math.abs(height), 0f);
		Vec p2 = new Vec(center.x + 1f / 2f * a, center.y - 1f / 3f
				* Math.abs(height), 0f);
		Vec p3 = new Vec(center.x, center.y + 2f / 3f * Math.abs(height), 0f);
		Vec p4 = new Vec(center.x, center.y, height);

		p.add(p1);
		p.add(p2);
		p.add(p3);

		p.add(p1);
		p.add(p2);
		p.add(p4);

		p.add(p2);
		p.add(p3);
		p.add(p4);

		p.add(p3);
		p.add(p1);
		p.add(p4);

		return p;
	}

	public static void resetInstance() {
		myInstance = new GLFactory();
	}

	public MeshComponent newUndirectedPath(GeoObj from, GeoObj to, Color color) {
		return GLFactory.getInstance().newUndirectedPath(
				to.getVirtualPosition(from), color);
	}

	public MeshComponent newDirectedPath(GeoObj from, GeoObj to, Color color) {
		return GLFactory.getInstance().newDirectedPath(
				to.getVirtualPosition(from), color);
	}

	public MeshComponent newCube() {
		return newCube(null);
	}

	public MeshComponent newCoordinateSystem() {
		return new MeshComponent(null) {

			@Override
			public boolean accept(Visitor visitor) {
				return false;
			}

			@Override
			public void draw(GL10 gl) {
				CordinateAxis.draw(gl);
			}
		};
	}

	/**
	 * will face to the camera
	 * 
	 * @param textToDisplay
	 * @param textPosition
	 * @param textSize
	 * @param context
	 * @param glCamera
	 * @return
	 */
	public Obj newTextObject(String textToDisplay, Vec textPosition,
			Context context, GLCamera glCamera) {

		float textSize=1;
		
		TextView v = new TextView(context);
		v.setTypeface(null, Typeface.BOLD);
		// Set textcolor to black:
		// v.setTextColor(new Color(0, 0, 0, 1).toIntARGB());
		v.setText(textToDisplay);

		Obj o = new Obj();
		MeshComponent mesh = this.newTexturedSquare("textBitmap" + textToDisplay,
				util.IO.loadBitmapFromView(v),textSize);
		mesh.myPosition = textPosition.copy();
		mesh.addAnimation(new AnimationFaceToCamera(glCamera));
		o.setComp(mesh);
		return o;
	}
}
