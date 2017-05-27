package main.Client;

import com.sun.deploy.util.SessionState;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.Utils.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 2017-05-20.
 */
public class Board extends Parent {
    private VBox rows = new VBox();
    private int shipsLeft = 10;
    private static final int boardsize = 10;

    public Board(EventHandler<? super MouseEvent> mouseClickHandler) {
        for (int y = 0; y < boardsize; y++) {
            HBox row = new HBox();
            for (int x = 0; x < boardsize; x++) {
                ClientCell cell = new ClientCell(x, y);
                cell.setOnMouseClicked(mouseClickHandler);
                row.getChildren().add(cell);
            }
            rows.getChildren().add(row);
        }
        this.getChildren().add(rows);
    }

    public ClientCell getCell(int x,int y){
        return (ClientCell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public void repaintOnHit(int x, int y){
        ClientCell cell = getCell(x,y);
        cell.setColorAndUsed(Color.RED);
    }

    public void repaintOnMissed(int x, int y){
        ClientCell cell = getCell(x,y);
        cell.setColorAndUsed(Color.CHOCOLATE);
    }

    public boolean isPointValid(int x, int y) {
        return x >= 0 && x < boardsize && y >= 0 && y < boardsize;
    }

    /**
     *  This method paints all cells that are adjacent to recently killed ship.
     *  If ship is vertical, it checks left adjacent column (x-1) and right adjacent column (x+1).
     *  Then checks remaining 2 cells.
     *  Analogously, for horizontal orientation checks adjacent rows (y-1,y+1) and remaining 2 cells.
     * @param  x x coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     * @param  y y coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     * @param length total length of the ship
     * @param vertical defines if ship orientation is vertical(true) or horizontal(false)
     */
    public void paintHintAfterKill(int x,int y,int length,boolean vertical){
        int tempx,tempy;
        if(vertical){
            for(int i=0;i<length+2;i++){
                tempx = x-1;
                tempy = y-1+i;
                if(isPointValid(tempx,tempy))
                    getCell(tempx,tempy).setColorAndUsed(Color.BEIGE);
                tempx = x+1;
                if(isPointValid(tempx,tempy))
                    getCell(tempx,tempy).setColorAndUsed(Color.BEIGE);
            }
            tempy = y-1;
            if(isPointValid(x,tempy))
                getCell(x,tempy).setColorAndUsed(Color.BEIGE);
            tempy = y+length;
            if(isPointValid(x,tempy))
                getCell(x,tempy).setColorAndUsed(Color.BEIGE);
        }
        else{
            for(int i=0;i<length+2;i++){
                tempx = x-1+i;
                tempy = y-1;
                if(isPointValid(tempx,tempy))
                    getCell(tempx,tempy).setColorAndUsed(Color.BEIGE);
                tempy = y+1;
                if(isPointValid(tempx,tempy))
                    getCell(tempx,tempy).setColorAndUsed(Color.BEIGE);
            }
            tempx = x-1;
            if(isPointValid(tempx,y))
                getCell(tempx,y).setColorAndUsed(Color.BEIGE);
            tempx = x+length;
            if(isPointValid(tempx,y))
                getCell(tempx,y).setColorAndUsed(Color.BEIGE);
        }
    }

    public void destroyShip(){
        this.shipsLeft--;
    }
}
