package mtrec.rock.object;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CompassView extends ImageView {
	
	private String CompassName;
	private String color;
	private float orientation = 180;
	private Paint pointPaint = new Paint();
	private Random random = new Random(System.currentTimeMillis());
	private int[] blue = new int[] {150, 212, 233};
	private int[] green = new int[] {46, 204, 113};
	private int[] red = new int[] {231, 76, 60};
	private int[] yellow = new int[] {241, 196, 15};
	private int[] rgbColor;

	public CompassView(Context context) {
		super(context);
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOrientation(float degree) {
		orientation = degree;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (color.equals("blue")) rgbColor = blue;
		else if (color.equals("green")) rgbColor = green;
		else if (color.equals("red")) rgbColor = red;
		else rgbColor = yellow;

		float[] estimatedPointValue = new float[]{getWidth() / 2, getHeight() / 2};
		float radius = Math.min(getWidth() / 2, getHeight() / 2), centerRadius = radius / 4f;
		
		pointPaint.setStyle(Style.FILL);
		pointPaint.setColor(Color.rgb(rgbColor[0], rgbColor[1], rgbColor[2]));
		canvas.drawCircle(estimatedPointValue[0], estimatedPointValue[1], radius - 10, pointPaint);
		
		pointPaint.setColor(Color.rgb(rgbColor[0], rgbColor[1], rgbColor[2]));
		canvas.drawCircle(estimatedPointValue[0], estimatedPointValue[1], centerRadius - 1, pointPaint);
		
		pointPaint.setColor(Color.rgb(255, 255, 255));
		Path arrow = new Path();
		float pointerX = (float) (estimatedPointValue[0] + centerRadius * 2 * Math.cos(orientation / 180 * Math.PI));
		float pointerY = (float) (estimatedPointValue[1] + centerRadius * 2 * Math.sin(orientation / 180 * Math.PI));
		arrow.moveTo(pointerX, pointerY);
		arrow.arcTo(new RectF(estimatedPointValue[0] - centerRadius * 1.25f, estimatedPointValue[1] - centerRadius * 1.25f, estimatedPointValue[0] + centerRadius * 1.25f, estimatedPointValue[1] + centerRadius * 1.25f), orientation - 30, 60);
		arrow.lineTo(pointerX, pointerY);
		canvas.drawPath(arrow, pointPaint);
		
		pointPaint.setStyle(Style.FILL);
		
		canvas.drawCircle(estimatedPointValue[0], estimatedPointValue[1], centerRadius, pointPaint);
	    
		this.pointPaint.setColor(-7829368);
	    this.pointPaint.setTextSize(30.0F);
	    canvas.drawText(this.CompassName, getWidth() / 5, -20 + getHeight(), this.pointPaint);
	}

	public void setCompassName(String paramString){
	    this.CompassName = paramString;
	    if (paramString.equals("Noise-Reduced")) color = "blue";
	    else if (paramString.equals("Magnetic Field")) color = "green";
	    else if (paramString.equals("Gyroscope")) color = "red";
	    else color = "yellow";
	}

}
