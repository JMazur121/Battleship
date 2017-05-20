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

    public Board(EventHandler<? super MouseEvent> mouseClickHandler) {
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
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
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public void paintHintAfterSink(int x,int y,int length,boolean vertical){
        if(vertical){
            for(int i=0;i<length+2;i++){
                getCell(x-1,y-1+i).setColorAndUsed(Color.BEIGE);
                getCell(x+1,y-1+i).setColorAndUsed(Color.BEIGE);
            }
            getCell(x,y-1).setColorAndUsed(Color.BEIGE);
            getCell(x,y+length).setColorAndUsed(Color.BEIGE);
        }
        else{
            for(int i=0;i<length+2;i++){
                getCell(x-1+i,y-1).setColorAndUsed(Color.BEIGE);
                getCell(x-1+i,y+1).setColorAndUsed(Color.BEIGE);
            }
            getCell(x-1,y).setColorAndUsed(Color.BEIGE);
            getCell(x+length,y).setColorAndUsed(Color.BEIGE);
        }
    }
}
