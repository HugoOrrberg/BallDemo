package com.example.testing;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity{
    private GameView gameView;
    private int downX, downY;
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this, getWindowManager());
        setContentView(gameView);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                downX = X;
                downY = Y;
                gameView.stop();
                break;

            case MotionEvent.ACTION_MOVE:
                gameView.setLine(downX, downY, X, Y);
                break;

            case MotionEvent.ACTION_UP:
                gameView.start();
                gameView.throwShape(downX,downY,X,Y);
                break;
        }
        return true;
    }

    private final SensorEventListener sensorEventListener =
            new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor == mRotationSensor) {
                        if (event.values.length > 4) {
                            float[] truncatedRotationVector = new float[4];
                            System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                            gameView.setOrientationVectors(truncatedRotationVector);
                        } else {
                            gameView.setOrientationVectors(event.values);
                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    System.out.println(sensor + "    accuracy:" + accuracy);
                }
            };


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }


}