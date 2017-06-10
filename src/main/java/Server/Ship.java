package main.java.Server;

import main.java.Utils.Point;

/**
 * Created by Jakub on 2017-05-15.
 */
public class Ship {
    private final int length;
    private int hitpoints;
    private boolean vertorientation;
    private Point firstCell;

    public Ship(int length, boolean orientation) {
        this.length = length;
        this.vertorientation = orientation;
        this.hitpoints = length;
    }

    public boolean getOrientation() {
        return vertorientation;
    }

    public void setOrientation(boolean orientation) {
        this.vertorientation = orientation;
    }

    public int getLength() {
        return length;
    }

    public boolean isAlive(){
        return this.hitpoints > 0;
    }

    public void damage(){
        this.hitpoints--;
    }

    public void setFirstCell(int x, int y) { this.firstCell = new Point(x,y);}

    public Point getFirstCell() { return firstCell;}
}
