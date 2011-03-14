package gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLUtilityClass {

	public static FloatBuffer createAndInitFloatBuffer(float[] source) {
		if (source == null)
			return null;
		/*
		 * a float is 4 bytes, therefore the number of vertices has to be
		 * multiplied with 4:
		 */
		ByteBuffer verticleByteBuffer = ByteBuffer
				.allocateDirect(source.length * 4);
		verticleByteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer targetBuffer = verticleByteBuffer.asFloatBuffer();
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
