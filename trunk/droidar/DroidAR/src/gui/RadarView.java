package gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RadarView extends View {

	Paint paint;

	public RadarView(Context context) {
		super(context);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setColor(Color.BLACK);
		paint.setAlpha(150);
		canvas.drawCircle(50, 50, 20, paint);

	}

}
