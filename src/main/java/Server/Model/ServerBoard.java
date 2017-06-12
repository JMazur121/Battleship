package Server.Model;

import Utils.Point;

import java.util.ArrayList;
import java.util.List;

public class ServerBoard {
    private int unitsLeft = 10;
    private static final int boardsize = 10;
    private ServerCell[][] board = null;

    public ServerBoard(){
        initializeBoard();
    }

    public void initializeBoard() {
        if (this.board == null) {
            this.board = new ServerCell[boardsize][boardsize];
        }
        for(int i=0;i<boardsize;i++){
            for(int j=0;j<boardsize;j++){
                board[i][j] = new ServerCell();
            }
        }
        this.unitsLeft = 10;
    }

    public ServerCell getCell(int x, int y){
        return this.board[y][x];
    }

    private boolean isPointValid(int x, int y) {
        return x >= 0 && x < boardsize && y >= 0 && y < boardsize;
    }

    /**
     *  This method returns all cells that are adjacent to ship cells.
     *  If ship is vertical, it checks left adjacent column (x-1) and right adjacent column (x+1).
     *  Then checks remaining 2 cells.
     *  Analogously, for horizontal orientation checks adjacent rows (y-1,y+1) and remaining 2 cells.
     *  At any moment, method chcecks if possible cell stays in board.
     * @param  x x coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     * @param  y y coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     * @param length total length of the ship
     * @param vertical defines if ship orientation is vertical(true) or horizontal(false)
     * @return  array of cells adjacent to ship cells
     */
    private ServerCell[] getShipNeighbours(int x, int y, int length, boolean vertical){
        List<ServerCell> neighbours = new ArrayList<>();
        int tempx,tempy;
        if(vertical){
            for(int i=0;i<length+2;i++){
                tempx = x-1;    //(x-1) -> left adjacent column
                tempy = y-1+i;  //(y-1+i) -> next row in adjacent column
                if(isPointValid(tempx,tempy))
                    neighbours.add(getCell(tempx,tempy));
                tempx = x+1;    //(x+1) -> right adjacent column
                if(isPointValid(tempx,tempy))
                    neighbours.add(getCell(tempx,tempy));
            }
            tempy = y-1;    //(y-1) -> cell that lies directly below bottommost ship cell
            if(isPointValid(x,tempy))
                neighbours.add(getCell(x,tempy));
            tempy = y+length;   //(y+length) -> cell that lies directly above uppermost ship cell
            if(isPointValid(x,tempy))
                neighbours.add(getCell(x,tempy));
        }
        else{
            for(int i=0;i<length+2;i++){
                tempx = x-1+i;  //(x-1+i) -> next column in adjacent row
                tempy = y-1;    //(y-1) -> uppermost adjacent row
                if(isPointValid(tempx,tempy))
                    neighbours.add(getCell(tempx,tempy));
                tempy = y+1;    //(y+1) -> bottommost adjacent row
                if(isPointValid(tempx,tempy))
                    neighbours.add(getCell(tempx,tempy));
            }
            tempx = x-1;    //(x-1) -> cell that lies next to the most left ship cell
            if(isPointValid(tempx,y))
                neighbours.add(getCell(tempx,y));
            tempx = x+length;   //(x+length) -> cell that lies next to the most rigth ship cell
            if(isPointValid(tempx,y))
                neighbours.add(getCell(tempx,y));
        }
        return neighbours.toArray(new ServerCell[0]);
    }

    /**
     *  This method checks placement possibility.
     *  Firstly, it checks if ship stays in Borders. Then it checks if all required cells are empty.
     *  Finally, it checks all adjacent cells.
     * @param  x x coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     * @param  y y coordinate for most bottom ship cell (if vertical) or coordinate for most left cell(if horizontal)
     */
    private boolean isPlacementPossible(Ship shipToPlace, int x, int y){
        int length = shipToPlace.getLength();
        //check vertical orientation
        if (shipToPlace.getOrientation()) {
            for (int i = y; i < y + length; i++) {
                //check if ship stays in borders
                if (!isPointValid(x, i))
                    return false;
                //check if possible ship cells are empty
                if (getCell(x,i).getShip()!= null)
                    return false;
            }
            //check if adjacent cells are empty
            for (ServerCell neighbour : getShipNeighbours(x,y,length,shipToPlace.getOrientation())) {
                if (neighbour.getShip()!= null)
                    return false;
            }
        }
        //check horizontal orientation
        else{
            for (int i = x; i < x + length; i++) {
                if (!isPointValid(i, y))
                    return false;

                ServerCell cell = getCell(i, y);
                if (cell.getShip() != null)
                    return false;
            }

            for (ServerCell neighbour : getShipNeighbours(x,y,length,shipToPlace.getOrientation())) {
                if (neighbour.getShip()!= null)
                    return false;
            }
        }
        return true;
    }

    /**
     * @return result of shot - true if the ship was destroyed, false if ship stays alive.
     */
    public boolean hit(int x,int y){
        return getCell(x,y).shoot();
    }

    public boolean checkIfMissed(int x,int y) {
        return (!(getCell(x,y).isShip()));
    }

    public boolean destroyShip(){
        this.unitsLeft--;
        return (!(this.unitsLeft > 0));
    }

    public boolean placeShip(Ship shipToPlace, int x, int y){
        if(isPlacementPossible(shipToPlace, x, y)){
            shipToPlace.setFirstCell(x,y);
            if(shipToPlace.getOrientation()){ //== vertical
                for(int i=y; i<y+shipToPlace.getLength(); i++){
                    this.getCell(x,i).placeShip(shipToPlace);
                }
            }
            else{ //horizontal
                for(int i=x; i<x+shipToPlace.getLength(); i++){
                    this.getCell(i,y).placeShip(shipToPlace);
                }
            }
            return true;
        }
        return false;
    }

    public Ship removeShip(int x, int y){
        ServerCell cell = this.getCell(x, y);
        Point first = cell.getShip().getFirstCell();
        Ship ship = cell.getShip();
        if(cell.getShip().getOrientation()){//vertical
            for(int i=first.getY(); i<first.getY()+ship.getLength(); i++){
                this.getCell(x,i).setNull();
            }
        }
        else{
            for(int i=first.getX(); i<first.getX()+ship.getLength(); i++){
                this.getCell(i,y).setNull();
            }
        }
        return ship;
    }
}
