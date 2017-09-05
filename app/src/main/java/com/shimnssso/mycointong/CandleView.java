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

    private float highPercent = 0.0f;
    private float lowPercent = 0.0f;
    private float curPercent = 0.0f;

    public CandleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(double open, double high, double low, double close) {
        if (open != 0) {
            highPercent = (float)((high - open) / open * 100);
            lowPercent = (float)((open - low) / open * 100);
            curPercent = (float)((close - open) / open * 100);
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

        if (curPercent > 0.0f) paint.setColor(Const.Color.LTRED);
        else if (curPercent < 0.0f) paint.setColor(Const.Color.LTBLUE);
        else paint.setColor(Color.WHITE);
        paint.setStrokeWidth(STROKE_WIDTH);

        // line for -30% ~ 30%
        float startX = center - lowPercent * unit;
        float stopX = center + highPercent * unit;
        if (startX < 0.0f) startX = 0.0f;
        if (stopX > width) stopX = width;
        canvas.drawLine(startX, height/2, stopX, height/2, paint);

        float curX = center + curPercent * unit;
        if (MAX_PERCENT < curPercent) { // over 30%
            canvas.drawRect(center, padding, width, height - padding, paint);
            paint.setColor(Color.RED);
            curX = center + (curPercent-MAX_PERCENT) * unit;

        }
        else if (curPercent < -MAX_PERCENT) { // under -30%
            canvas.drawRect(0, padding, width/2, height - padding, paint);
            paint.setColor(Color.BLUE);
            curX = center + (curPercent+MAX_PERCENT) * unit;

        }

        RectF rectF;
        if (curX < center) {
            rectF = new RectF(curX, padding, center, height - padding);
        }
        else if (center < curX) {
            rectF = new RectF(center, padding, curX, height - padding);
        }
        else {
            rectF = new RectF(curX - STROKE_WIDTH/2, padding, center + STROKE_WIDTH/2, height - padding);
        }
        canvas.drawRect(rectF, paint);

        // line for -60% ~ -30%
        if (MAX_PERCENT < lowPercent) {
            paint.setStrokeWidth(STROKE_WIDTH + 2.0f);
            paint.setColor(Color.BLUE);
            startX = center - (lowPercent - MAX_PERCENT) * unit;
            canvas.drawLine(startX, height/2, center, height/2, paint);
        }

        // line for 30% ~ 60%
        if (MAX_PERCENT < highPercent) {
            paint.setStrokeWidth(STROKE_WIDTH + 2.0f);
            paint.setColor(Color.RED);
            stopX = center + (highPercent - MAX_PERCENT) * unit;
            canvas.drawLine(center, height/2, stopX, height/2, paint);
        }
    }
}
