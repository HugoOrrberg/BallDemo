package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

public abstract class Figure extends View {
    protected double mass;
    protected double x, y, dx, dy;
    protected int boundX, boundY;
    protected double gravityX;
    protected double gravityY;
    protected double bounce;
    protected ShapeDrawable figure;

    public Figure(Context context) {
        super(context);
    }

    /**
     * Update the position of object
     */
    public abstract void update();

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
     * Handles collision with bounds
     * @return true if collision occured
     */
    protected abstract boolean collideWithBounds();

    /**
     * Draws this rectangle
     * @param canvas the canvas on which to draw rectangle
     */
    public void onDraw(Canvas canvas){
        figure.draw(canvas);
    }

    /**
     * Checks if this figure intersect with received figure
     * @param figure2 the figure of which intersection should be checked
     * @return true if this figure intersects with figure2
     */
    public abstract boolean intersects(Figure figure2);

    /**
     * Checks if this figure occupies the received position
     * @param x coordinate
     * @param y coordinate
     * @return true if figure occupy pos, else false
     */
    public abstract boolean occupiesPos(int x, int y);

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
}
