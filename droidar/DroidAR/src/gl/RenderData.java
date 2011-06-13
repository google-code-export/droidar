package gl;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import util.Vec;

public class RenderData {

	protected FloatBuffer vertexBuffer;
	protected int verticesCount;

	public int drawMode = GL10.GL_TRIANGLES;

	/**
	 * call whenever a {@link Shape} changes
	 * 
	 * @param shape
	 */
	public void updateShape(ArrayList<Vec> shape) {
		setVertexArray(turnShapeToFloatArray(shape));
	}

	public void setVertexArray(float[] turnShapeToFloatArray) {
		vertexBuffer = GLUtilityClass
				.createAndInitFloatBuffer(turnShapeToFloatArray);
	}

	public void setDrawModeToTriangles() {
		drawMode = GL10.GL_TRIANGLES;
	}

	public void setDrawModeToLines() {
		drawMode = GL10.GL_LINES;
	}

	protected RenderData() {
	}

	protected float[] turnShapeToFloatArray(ArrayList<Vec> shape) {
		float[] vertices = new float[shape.size() * 3];
		verticesCount = shape.size();
		int i = 0;
		for (Vec v : shape) {
			vertices[i] = v.x;
			i++;
			vertices[i] = v.y;
			i++;
			vertices[i] = v.z;
			i++;
		}
		return vertices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gl.Renderable#draw(javax.microedition.khronos.opengles.GL10)
	 */

	public void draw(GL10 gl) {
		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDrawArrays(drawMode, 0, verticesCount);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
