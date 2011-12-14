package gui;

import util.IO;
import de.rwth.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Add the following constructor to your class which extends
 * {@link SimpleCustomView}<br>
 * <br>
 * 
 * //add @deprecated to remember not to use this constructor later<br>
 * public YourSimpleCustomViewSubclass(Context context, AttributeSet attrs) {<br>
 * super(context, attrs);<br>
 * }<br>
 * 
 * <br>
 * <br>
 * 
 * @author Spobo
 * 
 */
public abstract class SimpleCustomView extends View {

	/**
	 * This will be called by the Eclipse UI Editor and here you have to init
	 * your demo values
	 * 
	 * @param context
	 */
	public abstract void editorInit(Context context);

	/**
	 * The Eclipse UI editor cant preview ressources loaded from the assets
	 * folder so a dummy bitmap is used instead
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public Bitmap loadBitmapFromId(Context context, int id) {
		if (isInEditMode() || id == 0) {
			return createDummyBitmap();
		} else {
			return IO.loadBitmapFromId(context, id);
		}
	}

	public Bitmap createDummyBitmap() {
		int size = 128;
		Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		Paint p = new Paint();
		p.setColor(Color.BLUE);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(20);
		c.drawLine(0, 0, size, size, p);
		c.drawLine(0, size, size, 0, p);
		c.drawLine(0, size / 2, size, size / 2, p);
		c.drawLine(size / 2, 0, size / 2, size, p);
		return b;
	}

	public Bitmap generateDebugImage2(Context context) {

		int size = 128;
		Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.BLUE);
		p.setStyle(Paint.Style.FILL);
		p.setStrokeWidth(10);
		// c.drawCircle(size / 2, size / 2, size * 0.4f, p);

		drawCircle(c, size / 2, size / 2, size / 2, p);

		return b;

	}

	public SimpleCustomView(Context context) {
		super(context);
		if (isInEditMode())
			editorInit(context);
	}

	public SimpleCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		editorInit(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		int measuredHeigth = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		onResizeEvent(measuredHeigth, measuredWidth);
	}

	/**
	 * When the view wants to resize it will call this method with the
	 * recommended size values. the final size then has to be set via the
	 * following call:
	 * 
	 * <br>
	 * <br>
	 * this.setMeasuredDimension(newHeight, newWidth);<br>
	 * 
	 * @param recommendedHeigth
	 * @param recommendedWidth
	 */
	public abstract void onResizeEvent(int recommendedHeight,
			int recommendedWidth);

	/**
	 * Use this method instead of
	 * {@link Canvas#drawCircle(float, float, float, Paint)} or the Eclipse UI
	 * Editor preview will be incorrect
	 * 
	 * @param canvas
	 * @param cx
	 * @param cy
	 * @param radius
	 * @param paint
	 */
	public void drawCircle(Canvas canvas, float cx, float cy, float radius,
			Paint paint) {
		if (isInEditMode()) {
			RectF arcRect = new RectF(cx - radius, cy - radius, cx + radius, cy
					+ radius);
			// Draw the Minutes-Arc into that rectangle
			canvas.drawArc(arcRect, -90, 360, true, paint);
		} else {
			canvas.drawCircle(cx, cy, radius, paint);
		}

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

	/**
	 * @param bitmap
	 *            the source
	 * @param factor
	 *            should be between 2f (very visible round corners) and 20f
	 *            (nearly no round corners)
	 * @return the result
	 */
	public static Bitmap addRoundCornersToBitmap(Bitmap bitmap, float factor) {
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
	 *            try 1.5f;
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int angle,
			float smoothingFactor) {
		Bitmap result = IO.resizeBitmap(bitmap, bitmap.getHeight()
				* smoothingFactor, bitmap.getWidth() * smoothingFactor);
		result = rotateBitmap(result, angle);
		result = IO.resizeBitmap(result, result.getHeight() / smoothingFactor,
				result.getWidth() / smoothingFactor);
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

}
