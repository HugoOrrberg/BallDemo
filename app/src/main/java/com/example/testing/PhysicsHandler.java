package com.example.testing;

import android.content.Context;
import android.hardware.SensorManager;

import java.util.HashSet;
import java.util.LinkedList;

public class PhysicsHandler {
    private LinkedList<Figure> figures;

    private float[] orientationVectors;
    HashSet<HashSet<Figure>> allFiguresInPairs;

    private int boundX;
    private int boundY;

    public PhysicsHandler(Context context, int boundX, int boundY){
        this.boundX = boundX;
        this.boundY = boundY;

        figures = new LinkedList<>();
        figures.add(new Rectangle(context, 10,10,100,100, boundX, boundY, 0.6));
        figures.add(new Circle(context,10 ,210, 200,boundX,boundY,0.75));
        figures.add(new Circle(context,400 ,10, 200,boundX,boundY,0.75));
        orientationVectors = new float[4];
        allFiguresInPairs = new HashSet();
        putAllFiguresInPairs();
    }

    public void update(){
        checkForCollisions();
        for (Figure figure: figures) {
//            if(!collisions.contains(figure)){
                figure.update();
//            }
        }
    }

    private void checkForCollisions(){
        for(HashSet<Figure> pair: allFiguresInPairs){
            Figure figure1 = (Figure) pair.toArray()[0];
            Figure figure2 = (Figure) pair.toArray()[1];
            if(intersects(figure1,figure2)){
                handleCollision(figure1, figure2);
            }
        }
    }


    private boolean intersects(Figure figure1, Figure figure2){
        if(figure1 instanceof Circle && figure2 instanceof Circle){
            return circleAndCircleIntersect((Circle) figure1, (Circle) figure2);
        }else if(figure1 instanceof Circle){
            return circleAndRectIntersect((Circle) figure1, (Rectangle) figure2);
        }else if(figure2 instanceof Circle){
            return circleAndRectIntersect((Circle) figure2, (Rectangle) figure1);
        }else{
            return rectAndRectIntersect((Rectangle) figure1, (Rectangle) figure2);
        }
    }

    private boolean circleAndCircleIntersect(Circle circle1, Circle circle2){
        return Math.sqrt(
                Math.pow((circle2.getFigureX() + circle2.getDiameter()/2) -
                        (circle1.getFigureX() + circle1.getDiameter()/2),2) +
                        Math.pow((circle2.getFigureY() + circle2.getDiameter()/2) -
                                (circle1.getFigureY() + circle1.getDiameter()/2),2))
                <  circle1.getDiameter()/2 + circle2.getDiameter()/2;
    }

    private boolean circleAndRectIntersect(Circle circle, Rectangle rect){
        // temporary variables to set edges for testing
        double cx = circle.getMidPointX() + circle.getDx();
        double cy = circle.getFigureY() + circle.getDiameter()/2 + circle.getDy();
        double rx = rect.getFigureX() + rect.getDx();
        double ry = rect.getFigureY() + rect.getDy();

        double testX = cx;
        double testY = cy;

        // which edge is closest?
        if (cx < rx)         testX = rx;      // test left edge
        else if (cx > rx+rect.getFigureWidth())
            testX = rx+rect.getFigureWidth();   // right edge
        if (cy < ry)         testY = ry;      // top edge
        else if (cy > ry+rect.getFigureHeight())
            testY = ry+rect.getFigureHeight();   // bottom edge

        // get distance from closest edges
        double distX = cx-testX;
        double distY = cy-testY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        if (distance < circle.getDiameter()/2) {
            return true;
        }
        return false;
    }

    private boolean rectAndRectIntersect(Rectangle rect1, Rectangle rect2){
        return false;
    }

    private void handleCollision(Figure figure1, Figure figure2) {
        //Change velocity of figures
        double tempDx = figure1.getDx();
        double tempDy = figure1.getDy();
        figure1.setDx(figure2.getDx() * figure1.bounce);
        figure1.setDy(figure2.getDy() * figure1.bounce);
        figure2.setDx(tempDx * figure2.bounce);
        figure2.setDy(tempDy * figure2.bounce);

        System.out.println("before: " + figure1.getMidPointX() + "  " + figure1.getMidPointY() + "  " + figure2.getMidPointX() + "  " + figure2.getMidPointY());
        //Make sure figures don't intersect anymore by changing positions
        if(Math.abs(figure1.getMidPointX() - figure2.getMidPointX()) >
                Math.abs(figure1.getMidPointY() - figure2.getMidPointY())){
            if(figure1.getFigureX() > figure2.getFigureX()){
                figure1.setX(figure2.getFigureX() + figure2.getFigureWidth());
                figure2.setX(figure2.getFigureX() - figure2.gravityX);
                figure2.setY(figure2.getFigureY() - figure2.gravityY);
            }else{
                figure1.setX(figure1.getFigureX() - figure1.gravityX);
                figure1.setY(figure1.getFigureY() - figure1.gravityY);
                figure2.setX(figure1.getFigureX() + figure1.getFigureWidth());
            }
        }else {
            if(figure1.getFigureY() > figure2.getFigureY()){
                figure1.setY(figure2.getFigureY() + figure2.getFigureHeight());
                figure2.setY(figure2.getFigureY() - figure2.gravityY);
                figure2.setX(figure2.getFigureX() - figure2.gravityX);
            }else{
                figure1.setX(figure1.getFigureX() - figure1.gravityX);
                figure1.setY(figure1.getFigureY() - figure1.gravityY);
                figure2.setY(figure1.getFigureY() + figure1.getFigureHeight());
            }
        }
        System.out.println("after: " + figure1.getMidPointX() + "  " + figure1.getMidPointY() + "  " + figure2.getMidPointX() + "  " + figure2.getMidPointY());
    }

    private void putAllFiguresInPairs(){
        for (Figure figure1 : figures) {
            for(Figure figure2 : figures){
                if(figure1 != figure2){
                    HashSet pair = new HashSet();
                    pair.add(figure1);
                    pair.add(figure2);
                    allFiguresInPairs.add(pair);
                }
            }
        }
    }

    public void throwShape(int x1, int y1, int x2, int y2){
        for (Figure figure : figures){
            if(figure.occupiesPos(x1,y1)){
                figure.throwX(x2-x1);
                figure.throwY(y2-y1);
                break;
            }
        }
    }

    public void setOrientationVectors(float[] vectors){
        orientationVectors = vectors;
    }

    public void updateGravity(){
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, orientationVectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
//      float pitch = orientation[1];   Not used
        float rollAngle = orientation[2];
        double x = Math.sin(rollAngle);
        double y = Math.cos(rollAngle);

        for(Figure figure : figures){
            figure.setGravityX(2 * x);
            figure.setGravityY(2 * y);
        }
    }

    public LinkedList<Figure> getFigures(){
        return figures;
    }
}
