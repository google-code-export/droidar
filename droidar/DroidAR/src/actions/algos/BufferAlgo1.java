package actions.algos;

/**
 * implementation of the function <br>
 * 
 * <pre>
 *       /              0  for       |x| <  a   
 *  f(x)=|  |x|/(b-a)-a/b  for  a <  |x| <= b  
 *       \              1  for  b <= |x|
 *  
 *  graph of f(x):
 *  
 *   ^          _________
 *  1|         /| 
 *   |        / |
 *   |       /  |
 *   |      /   |
 *   |     /    |
 *   |____/     |        
 *  0 ----a-----b-------->
 * </pre>
 * 
 * @author Spobo
 * 
 */
public class BufferAlgo1 extends Algo {

	private final float a;
	private final float b;
	private final float aDIVb;
	private final float bMINUSa;

	public BufferAlgo1(float a, float b) {
		this.a = a;
		this.b = b;
		aDIVb = a / b;
		bMINUSa = b - a;
	}

	@Override
	public boolean execute(float[] target, float[] values, float bufferSize) {
		target[0] = morph(target[0], values[0]);
		target[1] = morph(target[1], values[1]);
		target[2] = morph(target[2], values[2]);
		return true;
	}

	/**
	 * @param t
	 * @param v
	 * @return newT=t+f(|v-t|)
	 */
	private float morph(float t, float v) {
		float x = v - t;
		if (x >= 0) {
			if (x < a)
				return t;
			if (b <= x)
				return v;
			return t + ((x) / bMINUSa - aDIVb);
		} else {
			if (-x < a)
				return t;
			if (b <= -x)
				return v;
			return t - ((-x) / bMINUSa - aDIVb);
		}
	}

}
