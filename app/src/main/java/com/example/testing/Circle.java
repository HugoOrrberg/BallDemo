package com.example.testing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Circle extends Figure {
    int diameter;

    public Circle(Context context, int x, int y, int diameter, int boundX, int boundY, double bounce ){
        super(context);

        this.x = x;
        this.y = y;
        this.dx = gravityX;
        this.dy = gravityY;
        this.width = diameter;
        this.height = diameter;
        this.diameter = diameter;
        this.mass = Math.pow(diameter/2,2)*3.14/10000;

        this.boundX = boundX;
        this.boundY = boundY;

        this.bounce = bounce;

        figure = new ShapeDrawable(new OvalShape());
        figure.getPaint().setColor(Color.BLUE);
        figure.setBounds(x, y, diameter, diameter);
    }

    public int getDiameter() {
        return width;
    }
}
