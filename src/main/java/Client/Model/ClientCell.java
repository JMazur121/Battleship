package Client.Model;

import Utils.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


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

    public void setColorAndUsed(Color fill){
        this.setFill(fill);
        this.wasUsed = true;
    }

    public void cleanCell(){
        this.wasUsed = false;
        this.setFill(Color.DODGERBLUE);
    }

    public int getXCoordinate() {
        return position.getX();
    }

    public int getYCoordinate() {
        return position.getY();
    }

}
