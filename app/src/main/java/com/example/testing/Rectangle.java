package com.example.testing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class Rectangle extends Figure {

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

        this.bounce = bounce*0.75;

        figure = new ShapeDrawable(new RectShape());
        figure.getPaint().setColor(Color.BLUE);
        figure.setBounds(x, y, width, height);
    }
}
