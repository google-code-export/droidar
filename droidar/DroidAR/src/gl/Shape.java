package gl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;
import worldData.Visitor;

public class Shape extends MeshComponent {

	public ArrayList<Vec> myShapeArray = new ArrayList<Vec>();
	protected RenderData myRenderData = new RenderData();
	private boolean singeSide = false;

	public Shape() {
		super(null);
	}

	public Shape(Color color) {
		super(color);
	}

	public void add(Vec v) {
		myShapeArray.add(v.copy());
		myRenderData.updateShape(myShapeArray);
	}

	@Override
	public void draw(GL10 gl) {
		if (singeSide) {
			// which is the front? the one which is drawn counter clockwise
			gl.glFrontFace(GL10.GL_CCW);
			// enable the differentiation of which side may be visible
			gl.glEnable(GL10.GL_CULL_FACE);
			// which one should NOT be drawn
			gl.glCullFace(GL10.GL_BACK);

			myRenderData.draw(gl);

			// Disable face culling.
			gl.glDisable(GL10.GL_CULL_FACE);
		} else {
			myRenderData.draw(gl);
		}
	}

	public void setMyRenderData(RenderData myRenderData) {
		this.myRenderData = myRenderData;
	}

	public void setTriangleDrawing() {
		myRenderData.drawMode = GL10.GL_TRIANGLES;
	}

	public void setLineDrawing() {
		myRenderData.drawMode = GL10.GL_LINES;
	}

	/*
	 * also possible: GL_POINTS GL_LINE_STRIP GL_TRIANGLE_STRIP GL_TRIANGLE_FAN
	 */

	public void setLineLoopDrawing() {
		myRenderData.drawMode = GL10.GL_LINE_LOOP;
	}

	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public String toString() {
		return "Shape " + super.toString();
	}

}
