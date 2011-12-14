package gamelogic;

import de.rwth.R;
import system.ParentStack;
import util.IO;
import worldData.UpdateTimer;
import worldData.Updateable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GameElementView extends SimpleCustomView implements Updateable {

	private static final int DEFAULT_VIEW_SIZE = 250;
	private static final int MARGIN = 4;
	private static final float DEFAULT_UPDATE_SPEED = 0.3f;

	private Paint paint;
	private Paint loadingPaint;
	private Paint loadingLinePaint;

	private int mySize;
	private int myHalfSize;

	float myLoadingAngle = 160;

	private UpdateTimer myTimer;
	private float myUpdateSpeed = DEFAULT_UPDATE_SPEED;
	private double myTouchScaleFactor = 5;
	private Bitmap myIcon;
	private Bitmap mutable;
	private Canvas stampCanvas;

	// private String debug;

	public GameElementView(Context context, int iconid) {
		super(context);
		init(DEFAULT_VIEW_SIZE, loadBitmapFromId(context, iconid));
	}

	@Deprecated
	public GameElementView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void editorInit(Context context) {
		init(DEFAULT_VIEW_SIZE,
				loadBitmapFromId(context, R.drawable.spaceship));
	}

	public void setUpdateSpeed(float myUpdateSpeed) {
		this.myUpdateSpeed = myUpdateSpeed;
	}

	public void setIcon(Bitmap icon) {
		myIcon = icon;
		resizeIconToViewSize();
	}

	private void resizeIconToViewSize() {
		if (myIcon != null) {
			myIcon = resizeBitmap(myIcon, mySize, mySize);
			myIcon = addRoundCornersToBitmap(myIcon, 3f);
		}

	}

	private void drawLoadingCircle(Canvas canvas, int size, Paint paint) {
		float x = size * 0.2f;
		RectF arcRect = new RectF(-x, -x, size + x, size + x);
		// Draw the Minutes-Arc into that rectangle
		canvas.drawArc(arcRect, -90, myLoadingAngle, true, paint);
	}

	private void init(int viewSize, Bitmap icon) {

		paint = new Paint();

		loadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		loadingPaint.setColor(Color.RED);
		loadingPaint.setAlpha(100);

		loadingLinePaint = new Paint();
		loadingLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		loadingLinePaint.setColor(Color.BLACK);
		loadingLinePaint.setStyle(Paint.Style.STROKE);
		loadingLinePaint.setStrokeWidth(3);

		setSize(viewSize);

		if (isInEditMode())
			loadDemoValues();
		myTimer = new UpdateTimer(myUpdateSpeed, null);
		setIcon(icon);
	}

	public void setSize(int viewSize) {
		mySize = viewSize;
		myHalfSize = viewSize / 2;
		mutable = Bitmap.createBitmap(mySize, mySize, Bitmap.Config.ARGB_8888);
		stampCanvas = new Canvas(mutable);
		resizeIconToViewSize();
	}

	/**
	 * This method will only be called when the view is displayed in the eclipse
	 * xml layout editor
	 */
	private void loadDemoValues() {
		setLoadingAngle(160);
	}

	public void setLoadingAngle(float myLoadingAngle) {
		this.myLoadingAngle = myLoadingAngle;
		this.postInvalidate();
	}

	@Override
	public void onResizeEvent(int recommendedHeight, int recommendedWidth) {
		int min = Math.min(recommendedHeight, recommendedWidth);
		setSize(min);
		setMeasuredDimension(min, min);
	}

	@Override
	protected void onDraw(Canvas onDrawCanvas) {

		// stampCanvas.drawARGB(0, 0, 0, 0);
		stampCanvas.drawBitmap(myIcon, 0, 0, paint);
		// Bitmap i2 = generateDebugImage2(getContext());
		// canvas.drawBitmap(i2, 0, 0, paint);
		drawLoadingCircle(stampCanvas, mySize, loadingPaint);
		drawLoadingCircle(stampCanvas, mySize, loadingLinePaint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		stampCanvas.drawBitmap(myIcon, 0, 0, paint);
		paint.setXfermode(null);

		onDrawCanvas.drawBitmap(mutable, 0, 0, paint);

		// if (debug != null) { // TODO remove this
		// paint.setColor(Color.RED);
		// canvas.drawText(debug, 0, myHalfSize, paint);
		// }
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return onTouch(event.getX() - myHalfSize, event.getY() - myHalfSize);
	}

	private boolean onTouch(float x, float y) {
		double distFromCenter = Math.sqrt(x * x + y * y);
		distFromCenter *= myTouchScaleFactor;
		setLoadingAngle((float) (Math.random() * 359));
		postInvalidate();
		return true;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (myTimer.update(timeDelta, parent, stack)) {

		}
		/*
		 * TODO if view was removed from parent it can return false here!
		 */
		return true;
	}

}
