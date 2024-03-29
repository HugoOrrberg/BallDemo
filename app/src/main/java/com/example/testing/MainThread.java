package com.example.testing;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

public class MainThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    public MainThread(@NonNull SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run(){
        while(running){
            canvas = null;
            try{
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    gameView.update();
                    gameView.draw(canvas);
                }
            }catch (Exception e){
            }
            finally {
                if(canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                    }
                }
            }
        }
    }

    public void setRunning(boolean isRunning){
        running = isRunning;
    }
}
