package Server.Model;

import Utils.Point;

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

    public int getLength() {
        return length;
    }

    public boolean isAlive(){
        return (this.hitpoints > 0);
    }

    public void damage(){
        this.hitpoints--;
    }

    public void setFirstCell(int x, int y) { this.firstCell = new Point(x,y);}

    public Point getFirstCell() { return firstCell;}
}
