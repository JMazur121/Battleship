package main.Server;

/**
 * Created by Jakub on 2017-05-15.
 */
public class Ship {
    private final int length;
    private int hitpoints;
    private boolean vertorientation;

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
}
