package util;

public class Calculus {

	public static int randomInt(int min, int max) {
		return (int) (min + (Math.random() * (max + 1 - min)));
	}

	public static float getRandomFloat(float lowerBorder, float uperBorder) {
		return (float) (Math.random() * (uperBorder - lowerBorder) + lowerBorder);
	}

	public static float varyValueByPercent(float value, int percentToVary) {
		float p = value * percentToVary / 100;
		return getRandomFloat(value - p, value + p);
	}

}
