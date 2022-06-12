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
    private double gravityX;
    private double gravityY=3;
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

    public void onDraw(Canvas canvas){
        rect.draw(canvas);
    }

    public void update(){
        updatePos();
        rect.setBounds((int) x, (int) y,(int) (width+x),(int)(height+y));
    }

    private void updatePos(){
        if(collideWithBounds()){
            return;
        }
        dx += (dx == 0) ? 0 : gravityX;
        dy += (dy == 0) ? 0 : gravityY;
        x += dx;
        y += dy;
    }

    private boolean collideWithBounds(){
        boolean collisionOccured = false;
        if((x + dx) > boundX-width){
            x = boundX - width;
            dx = (dx < gravityX * bounce) ? 0 : -dx*bounce;
            collisionOccured = true;
        }if((x + dx) < 0){
            x = 0;
            dx = (dx > gravityX * bounce) ? 0 : -dx*bounce;
            collisionOccured = true;
        }if((y + dy) > boundY-height){
            y = boundY-height;
            dy = (dy < gravityY * bounce) ? 0 : -dy*bounce;
            collisionOccured = true;
        }if((y + dy) < 0){
            y = 0;
            dy = (dy > gravityY * bounce) ? 0 : -dy*bounce;
            collisionOccured = true;
        }
        return collisionOccured;
    }

    public void throwX(int ddx) {
        dx -= ddx/6;
    }

    public void throwY(int ddy) {
        dy -= ddy/6;
    }

    public void setGravityX(double gravityX){
        this.gravityX = gravityX;
    }

    public void setGravityY(double gravityY){
        this.gravityY = gravityY;
    }
}
