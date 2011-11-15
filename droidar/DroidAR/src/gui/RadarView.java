package gui;

import gl.GLCamera;
import gl.HasColor;
import gl.HasPosition;
import gl.scenegraph.Shape;
import system.ParentStack;
import util.EfficientList;
import util.Vec;
import worldData.Obj;
import worldData.RenderableEntity;
import worldData.UpdateTimer;
import worldData.Updateable;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RadarView extends View implements Updateable {

	private static final int DEFAULT_VIEW_SIZE = 250;
	private static final int MARGIN = 4;
	private static final float DEFAULT_UPDATE_SPEED = 1;
	private static final int DEFAULT_RADAR_MAX_DISTANCE = 200;

	Paint paint;
	private Paint linePaint;

	private int mySize;
	private int myHalfSize;
	private Vec myRotVec;
	private int myDisplRadius = DEFAULT_RADAR_MAX_DISTANCE;
	private boolean displayOutOfRadarArea = true;
	private boolean rotateNeedle = false;
	private EfficientList<RenderableEntity> items;
	private GLCamera myCamera;
	private Bitmap background;
	private double myRotation;
	private UpdateTimer myTimer;

	// private String debug;

	public RadarView(Context context, GLCamera camera, int radarViewSize,
			int displRadiusInMeters, float updateSpeed, boolean rotateNeedle,
			boolean displayOutOfRadarArea) {
		super(context);
		init(radarViewSize, updateSpeed);
		myCamera = camera;
		setRotateNeedle(rotateNeedle);
		setRadarDisplRadius(displRadiusInMeters);
		setDisplayOutOfRadarArea(displayOutOfRadarArea);
	}

	public void setRotateNeedle(boolean rotateNeedle) {
		this.rotateNeedle = rotateNeedle;
	}

	public void setDisplayOutOfRadarArea(boolean displayOutOfRadarArea) {
		this.displayOutOfRadarArea = displayOutOfRadarArea;
	}

	public void setRadarDisplRadius(int displRadiusInMeters) {
		this.myDisplRadius = displRadiusInMeters;
	}

	@Deprecated
	public RadarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(DEFAULT_VIEW_SIZE, DEFAULT_UPDATE_SPEED);
	}

	@Deprecated
	public RadarView(Context context) {
		super(context);
		init(DEFAULT_VIEW_SIZE, DEFAULT_UPDATE_SPEED);
	}

	@Deprecated
	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(DEFAULT_VIEW_SIZE, DEFAULT_UPDATE_SPEED);
	}

	public RadarView(Activity myTargetActivity, GLCamera camera) {
		this(myTargetActivity, camera, DEFAULT_VIEW_SIZE,
				DEFAULT_RADAR_MAX_DISTANCE, DEFAULT_UPDATE_SPEED, false, true);
	}

	private void init(int size, float updateSpeed) {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		linePaint = new Paint();
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(2);

		mySize = size;
		myHalfSize = size / 2;
		myRotVec = new Vec(myHalfSize / 2.5f, 0, 0);
		if (isInEditMode())
			loadDemoValues();
		myTimer = new UpdateTimer(updateSpeed, null);
	}

	/**
	 * This method will only be called when the view is displayed in the eclipse
	 * xml layout editor
	 */
	private void loadDemoValues() {
		setRotation(45);
		setDisplayedAreaSize(200);
		setElementsOutOfRadarAreaVisible(true);
		setCompassNeedleShouldBeRotated(true);
		myCamera = new GLCamera();
		myCamera.setPosition(new Vec(30, 40, 0));
		items = new EfficientList<RenderableEntity>();
		items.add(newObj(40, 40));
		items.add(newObj(10, 10));
		items.add(newObj(200, 200));
		items.add(newObj(200, -200));
	}

	private RenderableEntity newObj(int x, int y) {
		Obj o = new Obj();
		Shape s = new Shape(gl.Color.getRandomRGBColor());
		s.setPosition(new Vec(x, y, 0));
		o.setComp(s);
		return o;
	}

	public void setCompassNeedleShouldBeRotated(boolean b) {
		rotateNeedle = b;
	}

	public void setElementsOutOfRadarAreaVisible(boolean b) {
		displayOutOfRadarArea = b;
	}

	public void setDisplayedAreaSize(int areaRadiusInMeters) {
		myDisplRadius = areaRadiusInMeters;
	}

	public void setRotation(double rotation) {
		myRotation = rotation;
		myRotVec.rotateAroundZAxis(rotation - 90);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(mySize, mySize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * TODO store in bitmap object and only redraw if something changes to
		 * increase performance!
		 */
		drawBackGround(canvas);

		if (items != null)
			drawItems(canvas);

		drawCompassNeedle(canvas);

		paint.setColor(Color.BLACK);
		canvas.drawCircle(myHalfSize, myHalfSize, myHalfSize / 30, paint);

		linePaint.setColor(Color.BLACK);
		canvas.drawCircle(myHalfSize, myHalfSize, myHalfSize - MARGIN,
				linePaint);

		// if (debug != null) { // TODO remove this
		// paint.setColor(Color.RED);
		// canvas.drawText(debug, 0, myHalfSize, paint);
		// }
	}

	private void drawCompassNeedle(Canvas canvas) {
		linePaint.setColor(Color.RED);
		if (rotateNeedle) {
			canvas.drawLine(myHalfSize, myHalfSize, myHalfSize + myRotVec.x,
					myHalfSize + myRotVec.y, linePaint);
		} else {
			canvas.drawLine(myHalfSize, myHalfSize, myHalfSize, myHalfSize
					- myHalfSize / 2.5f, linePaint);
		}
	}

	private void drawItems(Canvas canvas) {
		for (int i = 0; i < items.myLength; i++) {
			if (items.get(i) instanceof HasPosition) {
				RenderableEntity element = items.get(i);
				Vec pos = ((HasPosition) element).getPosition().copy()
						.sub(myCamera.getPosition());

				float length = pos.getLength();
				if (length > myDisplRadius) {
					if (displayOutOfRadarArea) {
						pos.setLength(myDisplRadius);
						length = myDisplRadius;
					} else
						continue;
				}

				if (!rotateNeedle) {
					pos.rotateAroundZAxis(-myRotation);
				}

				/*
				 * now convert the distance in meters into a distance in pixels:
				 */
				pos.setLength(length / myDisplRadius * (myHalfSize - MARGIN));

				/*
				 * the canvas coords are not like the opengl coords! 10,10 means
				 * down on the screen
				 */
				float northPos = myHalfSize + pos.y;
				float eastPos = myHalfSize - pos.x;

				drawElement(element, canvas, northPos, eastPos);
			}
		}
	}

	private void drawElement(RenderableEntity element, Canvas canvas,
			float northPos, float eastPos) {
		paint.setColor(Color.WHITE);
		if (element instanceof HasColor) {
			gl.Color c = ((HasColor) element).getColor();
			if (c != null)
				paint.setColor(c.toIntARGB());
		}
		canvas.drawCircle(northPos, eastPos, 6, paint);
	}

	private void drawBackGround(Canvas canvas) {
		canvas.drawBitmap(getBackGround(), 0, 0, paint);
	}

	private Bitmap getBackGround() {
		if (background == null)
			background = createBackground(mySize, myHalfSize);
		return background;
	}

	/**
	 * This method is used to create a static background bitmap for better
	 * performance when drawing the radar
	 * 
	 * @param size
	 * @param halfSize
	 * @return
	 */
	private static Bitmap createBackground(int size, int halfSize) {

		Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);

		Paint p = new Paint();
		p.setAntiAlias(true);

		p.setColor(Color.WHITE);
		p.setAlpha(150);
		c.drawCircle(halfSize, halfSize, halfSize - MARGIN, p);

		// shadow
		p.setColor(Color.BLACK);
		p.setAlpha(100);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(4);
		int shadowOffset = 2;
		c.drawCircle(halfSize + shadowOffset, halfSize + shadowOffset, halfSize
				- MARGIN, p);

		p.setColor(Color.BLACK);
		p.setStrokeWidth(2);
		c.drawCircle(halfSize, halfSize, halfSize - MARGIN, p);

		return b;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (myTimer.update(timeDelta, parent, stack)) {
			try {
				setRotation(myCamera.getAngleUpdateListener()
						.getCurrentAngles()[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * TODO if view was removed from parent it can return false here!
		 */
		return true;
	}
}
