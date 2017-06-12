package Server.Model;

public class ServerCell {
    private Ship ship = null;

    public ServerCell() {
    }

    public void placeShip(Ship s){
        this.ship = s;
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
        return (!(this.ship.isAlive()));
    }

    public void setNull(){
        this.ship = null;
    }
}
