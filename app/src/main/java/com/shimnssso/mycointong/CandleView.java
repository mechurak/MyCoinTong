package com.shimnssso.mycointong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CandleView extends View {
    private static final String TAG = "CandleView";
    private static final float MAX_PERCENT = 30.0f;
    private static final float STROKE_WIDTH = 3.0f;

    private float highPercent;
    private float lowPercent;
    private float curPercent;

    public CandleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(int open, int high, int low, int close) {
        if (open != 0) {
            highPercent = (float)(high - open) / open * 100;
            lowPercent = (float)(open - low) / open * 100;
            curPercent = (float)(close - open) / open * 100;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float center = (float)width / 2;
        float unit = center / MAX_PERCENT;
        Log.d(TAG, "width: " + width + ", height: " + height + ", unit: " + unit);
        int padding = 30;

        Rect rect = new Rect(0, padding, width, height-padding);
        Log.d(TAG, "rect: " + rect.toString());

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawRect(rect, paint);

        if (curPercent > 0.0f) paint.setColor(Color.RED);
        else if (curPercent < 0.0f) paint.setColor(Color.BLUE);
        else paint.setColor(Color.WHITE);
        paint.setStrokeWidth(STROKE_WIDTH);

        float startX = center - lowPercent * unit;
        float stopX = center + highPercent * unit;
        Log.d(TAG, "lowPercent: " + lowPercent + ", highPercent: " + highPercent);
        Log.d(TAG, "startX: " + lowPercent + ", stopX: " + highPercent);

        canvas.drawLine(startX, height/2, stopX, height/2, paint);

        float curX = center + curPercent * unit;
        RectF rectF;
        if (curX > center) {
            rectF = new RectF(center, padding, curX, height - padding);
        }
        else if (center < curX) {
            rectF = new RectF(curX, padding, center, height - padding);
        }
        else {
            rectF = new RectF(curX - STROKE_WIDTH/2, padding, center + STROKE_WIDTH/2, height - padding);
        }
        canvas.drawRect(rectF, paint);
    }
}
