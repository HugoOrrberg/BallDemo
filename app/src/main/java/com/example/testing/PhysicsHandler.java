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
        figures.add(new Rectangle(context, 100,10,100,100, boundX, boundY, 0.6));
        figures.add(new Circle(context, 100,200, 200,boundX,boundY,0.75));

        orientationVectors = new float[4];
        allFiguresInPairs = new HashSet();
        putAllFiguresInPairs();
    }

    public void update(){
        LinkedList<Figure> collisions = collisions();
        for (Figure figure: figures) {
            if(!collisions.contains(figure)){
                figure.update();
            }
        }
    }

    private LinkedList<Figure> collisions(){
        LinkedList<Figure> collisions = new LinkedList<>();
        for(HashSet<Figure> pair: allFiguresInPairs){
            Figure figure1 = (Figure) pair.toArray()[0];
            Figure figure2 = (Figure) pair.toArray()[1];
            if(intersects(figure1,figure2)){
                collisions.addAll(pair);
//                figure1.setDx(figure2.getDx()*figure1.bounce);
//                figure1.setDy(figure2.getDy()*figure1.bounce);
//                figure2.setDx(figure1.getDx()*figure2.bounce);
//                figure2.setDy(figure1.getDy()*figure2.bounce);

                //TODO - What happens when two figures intersect?
            }
        }
        return collisions;
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
        return Math.sqrt(Math.pow(circle2.x - circle1.x,2) + Math.pow(circle2.y - circle1.y,2)) <=
                circle1.getWidth()/2 + circle2.getWidth()/2;
    }

    private boolean circleAndRectIntersect(Circle circle, Rectangle rect){
        double circleDistanceX =
                Math.abs((circle.x + circle.getDiameter()/2) - (rect.x + rect.getFigureWidth()/2));
        double circleDistanceY =
                Math.abs((circle.y + circle.getDiameter()/2) - (rect.y + rect.getFigureHeight()/2));

        if (circleDistanceX > (rect.getFigureWidth()/2 + circle.getDiameter())) { return false; }
        if (circleDistanceY > (rect.getFigureHeight()/2 + circle.getDiameter())) { return false; }

        if (circleDistanceX <= (rect.getFigureWidth()/2)) { return true; }
        if (circleDistanceY <= (rect.getFigureHeight()/2)) { return true; }

        double cornerDistance_sq = Math.pow((circleDistanceX - rect.getFigureWidth()/2), 2) +
                Math.pow((circleDistanceY - rect.getFigureHeight()/2),2);

        return (cornerDistance_sq <= Math.pow(circle.getDiameter()/2, 2));
    }

    private boolean rectAndRectIntersect(Rectangle rect1, Rectangle rect2){
        return false;
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
