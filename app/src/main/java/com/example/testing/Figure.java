package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

public abstract class Figure extends View {
    protected double mass;
    protected double x, y, dx, dy;
    protected int width, height;
    protected int boundX, boundY;
    protected double gravityX;
    protected double gravityY;
    protected double bounce;
    protected ShapeDrawable figure;

    public Figure(Context context) {
        super(context);
    }

    /**
     * Draws this rectangle
     * @param canvas the canvas on which to draw rectangle
     */
    public void onDraw(Canvas canvas){
        figure.draw(canvas);
    }


    /**
     * Updates the state of the figure
     */
    public void update(){
        updatePos();
        figure.setBounds((int) x, (int) y,(int) (width+x),(int)(height+y));
    }

    /**
     * Updates the stored position of object
     */
    protected void updatePos(){
        if(collideWithBounds()){
            return;
        }
        dx +=  gravityX;
        dy +=  gravityY;
        x += dx;
        y += dy;
    }

    /**
     * Checks if figure will collide with
     * @return
     */
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

    /**
     * Checks if this figure occupies the received position
     * @param x coordinate
     * @param y coordinate
     * @return true if figure occupy pos, else false
     */
    public boolean occupiesPos(int x, int y){
        return x >= this.x && x <= this.x+width
                && y >= this.y && y <= this.y + height;
    }

    /**
     * Throw or accelerate circle in x axis
     * @param ddx how much to accelerate dx by
     */
    public void throwX(double ddx) {
        dx -= ddx/6;
    }

    /**
     * Throw or accelerate circle in y axis
     * @param ddy how much to accelerate dy by
     */
    public void throwY(double ddy) {
        dy -= ddy/6;
    }

    /**
     * Change the gravity acting on the circle in the direction x
     * @param gravityX The new gravity
     */
    public void setGravityX(double gravityX){
        this.gravityX = gravityX;
    }

    /**
     * Change the gravity acting on the circle in the direction y
     * @param gravityY The new gravity
     */
    public void setGravityY(double gravityY){
        this.gravityY = gravityY;
    }

    public double getFigureX(){
        return x;
    }

    public double getFigureY(){
        return y;
    }

    public double getDx(){
        return dx;
    }

    public double getDy(){
        return dy;
    }

    public void setDx(double dx){
        this.dx = dx;
    }

    public void setDy(double dy){
        this.dy = dy;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getMass(){
        return mass;
    }

    public int getFigureWidth(){
        return width;
    }

    public int getFigureHeight(){
        return height;
    }

    public double getMidPointX(){
        return x + width/2;
    }

    public double getMidPointY(){
        return y + height/2;
    }
}
