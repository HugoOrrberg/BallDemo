package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
   private MainThread thread;

   private Rectangle rect;

   private Paint linePaint;
   private int[] line;
   
   private int boundX, boundY;
   private boolean stop = false;

   private float[] orientationVectors;

   public GameView(Context context, WindowManager windowManager) {
      super(context);

      getHolder().addCallback(this);

      thread = new MainThread(getHolder(), this);
      setFocusable(true);

      DisplayMetrics dp = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(dp);
      boundX = dp.widthPixels;
      boundY = dp.heightPixels;
   }

   @Override
   public void surfaceCreated(@NonNull SurfaceHolder holder){
      rect = new Rectangle(getContext(), 100,10,100,100, boundX, boundY, 1);

      line = new int[4];
      linePaint = new Paint();
      linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
      linePaint.setStrokeWidth(4);
      linePaint.setColor(Color.WHITE);

      orientationVectors = new float[4];

      thread.setRunning(true);
      thread.start();
   }

   @Override
   public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
   }

   @Override
   public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
      boolean retry = true;
      while (retry) {
         try {
            thread.setRunning(false);
            thread.join();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         retry = false;
      }
   }

   public void update(){
      updateGravity();
      if(!stop){
         rect.update();
      }

   }

   private void updateGravity(){
      float[] rotationMatrix = new float[9];
      SensorManager.getRotationMatrixFromVector(rotationMatrix, orientationVectors);
      int worldAxisX = SensorManager.AXIS_X;
      int worldAxisZ = SensorManager.AXIS_Z;
      float[] adjustedRotationMatrix = new float[9];
      SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
      float[] orientation = new float[3];
      SensorManager.getOrientation(adjustedRotationMatrix, orientation);
//      float pitch = orientation[1];   Not used
      float rollAngle = orientation[2];
      double x = Math.sin(rollAngle);
      double y = Math.cos(rollAngle);

      rect.setGravityX(2 * x);
      rect.setGravityY(2 * y);
   }

   public void draw(@NonNull Canvas canvas){
      super.draw(canvas);
      rect.draw(canvas);
      if(stop){
         canvas.drawLine(line[0],line[1],line[2],line[3], linePaint);
      }
   }

   public void stop(){
      for(int i = 0; i < 4; i++){
         line[i] = 0;
      }
      stop = true;
   }

   public void start(){

      stop = false;
   }

   public void throwShape(int x1, int y1, int x2, int y2){
      rect.throwX(x2-x1);
      rect.throwY(y2-y1);
   }

   public void setLine(int x1, int y1, int x2, int y2){
      line[0] = x1;
      line[1] = y1;
      line[2] = x2;
      line[3] = y2;
   }

   public void setOrientationVectors(float[] vectors){
      orientationVectors = vectors;
   }
}
