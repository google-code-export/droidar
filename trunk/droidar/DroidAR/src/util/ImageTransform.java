package util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageTransform {

	/**
	 * @param bitmap
	 *            the source
	 * @param factor
	 *            should be between 2f (very visible round corners) and 20f
	 *            (nearly no round corners)
	 * @return the result
	 */
	public static Bitmap createBitmapWithRoundCorners(Bitmap bitmap,
			float factor) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
		Canvas canvas = new Canvas(result);
		Rect rect = new Rect(0, 0, width, height);
		RectF roundCornerFrameRect = new RectF(rect);
		float cornerRadius = width / factor;
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRoundRect(roundCornerFrameRect, cornerRadius, cornerRadius,
				paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return result;
	}

	/**
	 * This will scale the image first to add soft corners
	 * 
	 * @param bitmap
	 * @param angle
	 * @param smoothingFactor
	 *            try 1.5f; (1 would be no smoothing at all)
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int angle,
			float smoothingFactor) {
		Bitmap result = ImageTransform.resizeBitmap(bitmap, bitmap.getHeight()
				* smoothingFactor, bitmap.getWidth() * smoothingFactor);
		result = rotateBitmap(result, angle);
		result = ImageTransform.resizeBitmap(result, result.getHeight()
				/ smoothingFactor, result.getWidth() / smoothingFactor);
		return result;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
		Canvas canvas = new Canvas(result);
		Matrix matrix = new Matrix();
		matrix.setRotate(angle, width / 2, height / 2);
		Paint p = new Paint();
		p.setAntiAlias(true);
		canvas.drawBitmap(bitmap, matrix, p);
		return result;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, float newHeight,
			float newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/**
	 * @param targetBitmap
	 * @param type
	 *            1 is Green-Blue, 2 is Red-Blue, 3 is Red - Green
	 */
	public static void switchColors(Bitmap targetBitmap, int type) {
		int width = targetBitmap.getWidth();
		int height = targetBitmap.getHeight();
		int[] srcPixels = new int[width * height];
		targetBitmap.getPixels(srcPixels, 0, width, 0, 0, width, height);
		int[] destPixels = new int[width * height];
		switch (type) {
		case 1:
			swapGreenBlue(srcPixels, destPixels);
			break;
		case 2:
			swapRedBlue(srcPixels, destPixels);
			break;
		case 3:
			swapRedGreen(srcPixels, destPixels);
			break;
		}
		targetBitmap.setPixels(destPixels, 0, width, 0, 0, width, height);
	}

	private static void swapGreenBlue(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xffff0000) | ((src[i] & 0x000000ff) << 8)
					| ((src[i] & 0x0000ff00) >> 8);
		}
	}

	private static void swapRedBlue(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xff00ff00) | ((src[i] & 0x000000ff) << 16)
					| ((src[i] & 0x00ff0000) >> 16);
		}
	}

	private static void swapRedGreen(int[] src, int[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = (src[i] & 0xff0000ff) | ((src[i] & 0x0000ff00) << 8)
					| ((src[i] & 0x00ff0000) >> 8);
		}
	}

}
