package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.LinkedList;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
   private MainThread thread;

   PhysicsHandler physicsHandler;

   private Paint linePaint;
   private int[] line;

   private int boundX, boundY;
   private boolean stop = false;


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
      physicsHandler = new PhysicsHandler(getContext(), boundX, boundY);


      line = new int[4];
      linePaint = new Paint();
      linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
      linePaint.setStrokeWidth(4);
      linePaint.setColor(Color.WHITE);

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
      if(!stop){
         physicsHandler.updateGravity();
         physicsHandler.update();
      }
   }

   public void draw(@NonNull Canvas canvas){
      super.draw(canvas);
      for(Figure figure : physicsHandler.getFigures()){
         figure.draw(canvas);
      }
      if(stop){
         canvas.drawLine(line[0],line[1],line[2],line[3], linePaint);
      }
   }

   public void stop(){
      setLine(0,0,0,0);
      stop = true;
   }

   public void start(){
      stop = false;
   }

   public void throwShape(int x1, int y1, int x2, int y2){
      physicsHandler.throwShape(x1, y1, x2, y2);
   }

   public void setLine(int x1, int y1, int x2, int y2){
      line[0] = x1;
      line[1] = y1;
      line[2] = x2;
      line[3] = y2;
   }

   public void setOrientationVectors(float[] vectors){
      physicsHandler.setOrientationVectors(vectors);
   }
}
