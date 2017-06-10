package main.java.Server;

public class ServerCell {
    private Ship ship = null;
    private boolean wasHit = false;

    public ServerCell() {
    }

    public void placeShip(Ship s){
        this.ship = s;
    }

    boolean wasHit(){
        return this.wasHit;
    }

    boolean isShip() {
        if(this.ship == null)
            return false;
        return true;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean shoot(){
        this.ship.damage();
        this.wasHit = true;
        return !this.ship.isAlive();
    }

    public void setNull(){
        this.ship = null;
    }
}
