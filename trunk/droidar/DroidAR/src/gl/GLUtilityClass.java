package gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLUtilityClass {

	/**
	 * TODO not efficient to create a new buffer object every time, pass an old
	 * one if available and use it instead?
	 * 
	 * @param source
	 * @return
	 */
	public static FloatBuffer createAndInitFloatBuffer(float[] source) {
		if (source == null)
			return null;
		/*
		 * a float is 4 bytes, therefore the number of elements in the array has
		 * to be multiplied with 4:
		 */
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(source.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer targetBuffer = byteBuffer.asFloatBuffer();
		targetBuffer.put(source);
		targetBuffer.position(0);
		return targetBuffer;
	}

	public static ShortBuffer createAndInitShortBuffer(short[] source) {
		if (source == null)
			return null;
		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer indiceBiteBuffer = ByteBuffer
				.allocateDirect(source.length * 2);
		indiceBiteBuffer.order(ByteOrder.nativeOrder());
		ShortBuffer targetBuffer = indiceBiteBuffer.asShortBuffer();
		targetBuffer.put(source);
		targetBuffer.position(0);
		return targetBuffer;
	}

}
