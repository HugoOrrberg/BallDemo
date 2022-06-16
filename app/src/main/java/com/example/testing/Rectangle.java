package com.example.testing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class Rectangle extends Figure {

    /**
     * Rectangle constructor
     * @param context current context
     * @param x left x pos
     * @param y upper y pos
     * @param width width of rectangle
     * @param height height of rectangle
     * @param boundX how many pixels device has in x direction
     * @param boundY how many pixels device has in y direction
     * @param bounce the bounce coefficient of rectangle, has to be inside [0,1] interval
     * @throws IllegalArgumentException if bounce not in [0,1] interval
     */
    public Rectangle(Context context, int x, int y, int width, int height, int boundX, int boundY, double bounce ){
        super(context);

        this.x = x;
        this.y = y;
        this.dx = gravityX;
        this.dy = gravityY;
        this.width = width;
        this.height = height;
        this.mass = width*height/10000;

        this.boundX = boundX;
        this.boundY = boundY;

        if(bounce > 1 || bounce < 0){
            throw new IllegalArgumentException("bounce has to be in [0,1] interval");
        }
        this.bounce = bounce;

        figure = new ShapeDrawable(new RectShape());
        figure.getPaint().setColor(Color.BLUE);
        figure.setBounds(x, y, width, height);
    }
}
