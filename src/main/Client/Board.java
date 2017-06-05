package main.Client;

import com.sun.deploy.util.SessionState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    protected VBox rows = new VBox();
    protected static final int boardsize = 10;

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

}
