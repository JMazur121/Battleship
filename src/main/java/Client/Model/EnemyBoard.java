package Client.Model;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EnemyBoard extends Board {

    public EnemyBoard(EventHandler<? super MouseEvent> mouseClickHandler) {
        super(mouseClickHandler);
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

    public void resetBoard() {
        for (int x = 0; x < boardsize; x++) {
            for (int y = 0; y < boardsize; y++) {
                getCell(x, y).cleanCell();
            }
        }
    }
}
