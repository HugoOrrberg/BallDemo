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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Activity{
    private GameView gameView;
    private FrameLayout game;
    private LinearLayout gameWidgets;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private boolean expanded;

    private int downX, downY;
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new FrameLayout(getApplicationContext());
        gameView = new GameView(this, getWindowManager());
        initButtons(this);

        game.addView(gameView);
        game.addView(gameWidgets);
        setContentView(game);




        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    private void initButtons(Context context) {
        gameWidgets = new LinearLayout(context);
        gameWidgets.setOrientation(LinearLayout.VERTICAL);

        fab1 = new FloatingActionButton(context);
        fab1.setImageResource(R.drawable.ic_add);
        expanded = false;

        fab2 = new FloatingActionButton(context);
        fab2.setImageResource(R.drawable.ic_settings);
        fab2.setScaleX((float) 0.8);
        fab2.setScaleY((float) 0.8);
        fab2.setVisibility(FloatingActionButton.INVISIBLE);

        fab3 = new FloatingActionButton(context);
        fab3.setImageResource(R.drawable.ic_add);
        fab3.setScaleX((float) 0.8);
        fab3.setScaleY((float) 0.8);
        fab3.setVisibility(FloatingActionButton.INVISIBLE);

        fab1.setOnClickListener(v -> {
            if(expanded){
                fab1.setImageResource(R.drawable.ic_add);
                fab2.setVisibility(FloatingActionButton.INVISIBLE);
                fab3.setVisibility(FloatingActionButton.INVISIBLE);
                expanded = false;
            }else{
                fab1.setImageResource(R.drawable.ic_up);
                fab2.setVisibility(FloatingActionButton.VISIBLE);
                fab3.setVisibility(FloatingActionButton.VISIBLE);
                expanded = true;
            }
        });

        fab2.setOnClickListener(v -> {
            //TODO - open settings screen
        });

        fab3.setOnClickListener(v -> {
            //TODO - should open option to add shape
        });

        gameWidgets.addView(fab1);
        gameWidgets.addView(fab2);
        gameWidgets.addView(fab3);
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

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}