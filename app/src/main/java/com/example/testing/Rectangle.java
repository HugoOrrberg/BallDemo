package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class Rectangle extends View {
    private double x, y, dx, dy;
    private int width, height;
    private int boundX, boundY;
    private double gravityX=0.01;
    private double gravityY=0.01;
    private final double bounce;
    private final ShapeDrawable rect;


    public Rectangle(Context context, int x, int y, int width, int height, int boundX, int boundY, double bounce ){
        super(context);

        this.x = x;
        this.y = y;
        this.dx = gravityX;
        this.dy = gravityY;
        this.width = width;
        this.height = height;

        this.boundX = boundX;
        this.boundY = boundY;

        this.bounce = bounce*0.75;

        rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(Color.BLUE);
        rect.setBounds(x, y, width, height);
    }

    /**
     * Draws this rectangle
     * @param canvas the canvas on which to draw rectangle
     */
    public void onDraw(Canvas canvas){
        rect.draw(canvas);
    }

    /**
     * Uptades position of this rectangle
     */
    public void update(){
        updatePos();
        rect.setBounds((int) x, (int) y,(int) (width+x),(int)(height+y));
    }

    private void updatePos(){
        if(collideWithBounds()){
            return;
        }
        dx +=  gravityX;
        dy +=  gravityY;
        x += dx;
        y += dy;
    }

    /**
     * Handles collision with bounds
     * @return true if collision occured
     */
    private boolean collideWithBounds(){
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

    /**
     * Throw or accelerate rectangle in x axis
     * @param ddx how much to accelerate dx by
     */
    public void throwX(int ddx) {
        dx -= ddx/6;
    }

    /**
     * Throw or accelerate rectangle in y axis
     * @param ddy how much to accelerate dy by
     */
    public void throwY(int ddy) {
        dy -= ddy/6;
    }

    /**
     * Change the gravity acting on the rectangle in the direction x
     * @param gravityX The new gravity
     */
    public void setGravityX(double gravityX){
        this.gravityX = gravityX;
    }

    /**
     * Change the gravity acting on the rectangle in the direction y
     * @param gravityY The new gravity
     */
    public void setGravityY(double gravityY){
        this.gravityY = gravityY;
    }
}
