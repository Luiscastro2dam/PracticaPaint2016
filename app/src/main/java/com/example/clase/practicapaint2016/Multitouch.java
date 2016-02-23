package com.example.clase.practicapaint2016;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clase on 22/02/2016.
 */
public class Multitouch extends View {
//PointF tiene dos coordenadas de flotación
    private PointF p1, p2;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private Paint pincel;
    private int ancho, alto,color;
    private Path path;
    private List<Path> paths = new ArrayList<>();

    public Multitouch(Context context) {
        super(context);
        p1 = new PointF();
        p2 = new PointF();
        pincel = new Paint();
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth((float) 5);
        color = pincel.getColor();
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapaDeBits, 0, 0, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ancho = w;
        alto = h;
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

            int pointerIndex = event.getActionIndex();
            int maskedAction = event.getActionMasked();
            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    float x = event.getX(pointerIndex);
                    float y = event.getY(pointerIndex);
                    Path p = new Path();
                    p.moveTo(x,y);
                    p.lineTo(x,y);
                    paths.add(p);

                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    for (int i = 0; i < event.getPointerCount(); i++) {
                        Path p = paths.get(event.getPointerId(i));
                        p.lineTo(event.getX(i),event.getY(i));
                        lienzoFondo.drawPath(p,pincel);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                    paths.clear();
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL: {
                    break;
                }
            }
            invalidate();

            return true;

    }
}
