package com.example.clase.practicapaint2016;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Vista extends View {

    private float x1, y1, x2, y2, xa, ya;
  //http://developer.android.com/intl/es/reference/android/graphics/Color.html
  //estan todos los codigos de los colores

    private int color = 0xffff0000;
    private float tamanoPin = 10;
    boolean cuadrado=false;
    private int figuras=0;
    private PointF p1, p2;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private Paint pincel;

    int ww,hh;//para guardar;

    public Vista(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapaDeBits, 10, 10, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int a = 20;
       ww=w;
         hh=h;
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        pincel = new Paint();
        lienzoFondo.drawColor(Color.WHITE);
        lienzoFondo.drawLine(xa, ya, x2, y2, pincel);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int puntos = event.getPointerCount();
        for (int i = 0; i < puntos; i++) {
            int accion = event.getActionMasked();
            float x = event.getX();
            float y = event.getY();
            int puntero = event.getPointerId(i);
            switch(accion){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    x1 = x2 = xa = x;
                    y1 = y2 = ya = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    xa = x2;
                    ya = y2;
                    x2 = x;
                    y2 = y;
                    Paint pincel = new Paint();
                    pincel.setStrokeWidth(tamanoPin);
                    pincel.setColor(color);
//validamos las figuras o el pincel en cada momento dependiendo la opcion elejida
                    if(figuras==2){
                        lienzoFondo.drawCircle(xa, ya, x2, pincel);
                    }else if(figuras==0){
                        lienzoFondo.drawLine(xa, ya, x2, y2, pincel);
                    }else if(figuras==1){
                       //cuadrado
                        lienzoFondo.drawRect(xa, xa, ya, y2, pincel);

                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    xa = x2;
                    ya = y2;
                    break;
            }
        }

        invalidate();
        return true;
    }

    //Actualiza color
    public void setColor(int newColor){
            color =newColor;
    }
    //nuevo dibujo limpiar
   public void nuevoDibujo(){
       lienzoFondo.drawColor(0, PorterDuff.Mode.CLEAR);

//       mapaDeBits = Bitmap.createBitmap(ancho, alto,
//               Bitmap.Config.ARGB_8888);
//       lienzoFondo = new Canvas(mapaDeBits);
//       invalidate();
       invalidate();
   }
    //para el tamaño del pincel
    public void tamanoPincel(float tamano){

         tamanoPin=tamano;
    }
    public void dibujarCuadrado(){
        figuras=1;
    }
    public void dibujarCirculo(){
        figuras=2;
    }
    public void dibujarLinea(){
        figuras=0;
    }
    boolean borrado=false;
    public void borrado(boolean estaborrado){
        borrado=estaborrado;
        if(borrado) {
            tamanoPin=30;
            color = 0xffffffff;
           // pincel.setColor(0xffffffff);//pintamos en blanco como el fondo y asi borramos
        }
        else {
            pincel.setColor(color);
        }
    }
    public void guardarImagen(String nombre) throws FileNotFoundException {
        try {
            File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            File archivo = new File(carpeta, nombre+".png");
            FileOutputStream fos = new FileOutputStream(archivo);
            Bitmap.createBitmap(ww, hh,Bitmap.Config.ARGB_8888).compress(Bitmap.CompressFormat.PNG, 90, fos);

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void ponerFotoBuscada(Bitmap bit){
        lienzoFondo.setBitmap(bit);
    }


}
