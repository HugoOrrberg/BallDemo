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
    private double gravityY;
    private final double bounce;
    private final ShapeDrawable rect;

    private boolean stopGravity = false;

    public Rectangle(Context context, int x, int y, int width, int height, int boundX, int boundY, double bounce ){
        super(context);

        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
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
        if(!stopGravity){
            dx += gravityX;
            dy += gravityY;
        }else{
            if(Math.abs(gravityX) > Math.abs(gravityY)){
                frictionY();
            }else{
                frictionX();
            }
        }
        x += dx;
        y += dy;
    }

    private boolean collideWithBounds(){
        boolean collisionOccured = false;
        if((x + dx) > boundX-width){
            x = boundX - width;
            if(dx < 0.1){
                dx = 0;
            }else{
                dx = -dx*bounce;
            }
            frictionY();
            collisionOccured = true;
        }if((x + dx) < 0){
            x = 0;
            if(dx > -0.1){
                dx = 0;
            }else{
                dx = -dx*bounce;
            }
            frictionY();
            collisionOccured = true;
        }if((y + dy) > boundY-height){
            y = boundY-height;
            if(dy < 0.1){
                dy = 0;
                stopGravity = true;
            }else{
                dy = -dy*bounce;
            }

            frictionX();
            collisionOccured = true;
        }if((y + dy) < 0){
            y = 0;
            dy = -dy*bounce;
            frictionX();
            collisionOccured = true;
        }
        return collisionOccured;
    }

    public void frictionX(){
        if(Math.abs(dx) < 0.1){
            dx = 0;
        }else{
            dx = dx*0.97;
        }
    }

    public void frictionY(){
        if(Math.abs(dy) < 0.1){
            dy = 0;
        }else{
            dy = dy*0.97;
        }
    }


    public void throwX(int ddx) {
        dx -= ddx/6;
    }

    public void throwY(int ddy) {
        stopGravity = false;
        dy -= ddy/6;
    }

    public void setGravityX(double gravityX){
        this.gravityX = gravityX;
    }

    public void setGravityY(double gravityY){
        this.gravityY = gravityY;
    }
}
