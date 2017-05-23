package main.Client;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Utils.Point;

/**
 * Created by Jakub on 2017-05-20.
 */
public class ClientCell extends Rectangle {
    private final Point position;
    private boolean wasUsed = false;

    public ClientCell(int x, int y){
        super(30,30);
        position = new Point(x,y);
        this.setFill(Color.DODGERBLUE);
        this.setStroke(Color.BLACK);
    }

    public boolean wasUsed(){
        return this.wasUsed;
    }

    public void setColors(Color fill,Color stroke){
        this.setFill(fill);
        this.setStroke(stroke);
    }

    public void setColorAndUsed(Color fill){
        this.setFill(fill);
        this.wasUsed = true;
    }

    public int getXCoordinate() {
        return position.getX();
    }

    public int getYCoordinate() {
        return position.getY();
    }

    public void setWasUsed(boolean wasUsed) {
        this.wasUsed = wasUsed;
    }
}
