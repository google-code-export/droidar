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
		vertexBuffer = setVertexArray(turnShapeToFloatArray(shape));
	}

	public FloatBuffer setVertexArray(float[] turnShapeToFloatArray) {
		return GLUtilityClass.createAndInitFloatBuffer(turnShapeToFloatArray);
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
