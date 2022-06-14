package com.example.testing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Circle extends Figure {
    private int diameter;

    public Circle(Context context, int x, int y, int diameter, int boundX, int boundY, double bounce ){
        super(context);

        this.x = x;
        this.y = y;
        this.dx = gravityX;
        this.dy = gravityY;
        this.diameter = diameter;
        this.mass = Math.pow(diameter/2,2)*3.14/10000;

        this.boundX = boundX;
        this.boundY = boundY;

        this.bounce = bounce;

        figure = new ShapeDrawable(new OvalShape());
        figure.getPaint().setColor(Color.BLUE);
        figure.setBounds(x, y, diameter, diameter);
    }

    @Override
    public void update(){
        updatePos();
        figure.setBounds((int) x, (int) y,(int) (diameter+x),(int)(diameter+y));
    }

    @Override
    public boolean intersects(Figure figure2) {
        return false;
    }

    @Override
    public boolean occupiesPos(int x, int y){
        int midpoint[] = {(int)(this.x+diameter/2), (int)(this.y+diameter/2)};
        int distanceToMidPoint = (int) Math.sqrt(Math.pow(x-midpoint[0],2) + Math.pow(y-midpoint[1],2));
        return diameter/2 > distanceToMidPoint;
    }

    @Override
    protected boolean collideWithBounds(){
        boolean collisionOccured = false;
        if((x + dx) > boundX-diameter){
            x = boundX - diameter;
            dx = -dx*bounce;
            collisionOccured = true;
        }if((x + dx) < 0){
            x = 0;
            dx = -dx*bounce;
            collisionOccured = true;
        }if((y + dy) > boundY-diameter){
            y = boundY-diameter;
            dy = -dy*bounce;
            collisionOccured = true;
        }if((y + dy) < 0){
            y = 0;
            dy = -dy*bounce;
            collisionOccured = true;
        }
        return collisionOccured;
    }

    public int getDiameter() {
        return diameter;
    }
}
