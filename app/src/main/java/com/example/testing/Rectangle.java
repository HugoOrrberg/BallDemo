package com.example.testing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class Rectangle extends Figure {
    private int width, height;

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

    @Override
    public void update(){
        updatePos();
        figure.setBounds((int) x, (int) y,(int) (width+x),(int)(height+y));
    }

    @Override
    public boolean intersects(Figure figure2) {
        return false;
    }

    @Override
    public boolean occupiesPos(int x, int y){
        return x >= this.x && x <= this.x+width
                && y >= this.y && y <= this.y + height;
    }

    @Override
    protected boolean collideWithBounds(){
        boolean collisionOccured = false;
        if((x + dx) > boundX-width){
            x = boundX - width;
            dx = -dx*bounce;
            collisionOccured = true;
        }if((x + dx) < 0){
            x = 0;
            dx = -dx*bounce;
            collisionOccured = true;
        }if((y + dy) > boundY-height){
            y = boundY-height;
            dy = -dy*bounce;
            collisionOccured = true;
        }if((y + dy) < 0){
            y = 0;
            dy = -dy*bounce;
            collisionOccured = true;
        }
        return collisionOccured;
    }

    public int getFigureWidth(){
        return width;
    }

    public int getFigureHeight(){
        return height;
    }

}
