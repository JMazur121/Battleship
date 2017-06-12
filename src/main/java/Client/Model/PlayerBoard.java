package Client.Model;

import Client.Controller.ClientViewController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PlayerBoard extends Board {
    private int currentPlacingSize = 4;
    private boolean isCurrentVertical = true;
    private ClientCell currentCell;
    private IntegerProperty leftToPlace;
    private ClientViewController controller;
    private IntegerProperty sizeToPlace[];

    public PlayerBoard(EventHandler<? super MouseEvent> mouseClickHandler, ClientViewController viewController) {
        super(mouseClickHandler);
        this.controller = viewController;
        this.sizeToPlace = new SimpleIntegerProperty[4];
        for(int i=0; i<4; i++){
            this.sizeToPlace[i] = new SimpleIntegerProperty(4-i);
        }
        this.leftToPlace = new SimpleIntegerProperty(10);
        this.addListeners();
    }

    public void setCurrentPlacingSize(int currentPlacingSize) {
        this.currentPlacingSize = currentPlacingSize;
    }

    public void resetBoard(){
        this.currentPlacingSize = 4;
        this.isCurrentVertical = true;
        for(int x = 0; x<boardsize; x++){
            for(int y = 0; y<boardsize; y++){
                getCell(x,y).cleanCell();
            }
        }
        for(int i=0; i<4; i++){
            this.sizeToPlace[i].set(4-i);
        }
        this.leftToPlace.set(10);
    }

    public void setCurrentCell(ClientCell currentCell) {
        this.currentCell = currentCell;
    }

    public void setCurrentVertical(boolean currentVertical) {
        isCurrentVertical = currentVertical;
    }

    public int getCurrentPlacingSize(){return this.currentPlacingSize;}

    public int getShipsLeft(){return this.leftToPlace.get();}

    public void placeCurrentShip(){
        if(this.isCurrentVertical){ //== vertical
            for(int i=this.currentCell.getYCoordinate(); i<this.currentCell.getYCoordinate()+this.currentPlacingSize; i++){
                getCell(this.currentCell.getXCoordinate(),i).setColorAndUsed(Color.BURLYWOOD);
            }
            int tmp = this.leftToPlace.get();
            this.leftToPlace.set(tmp-1);
            tmp = this.sizeToPlace[this.currentPlacingSize-1].get();
            this.sizeToPlace[this.currentPlacingSize-1].set(tmp-1);

        }
        else { //horizontal
            for (int i = this.currentCell.getXCoordinate(); i < this.currentCell.getXCoordinate()+this.currentPlacingSize; i++) {
                getCell(i, this.currentCell.getYCoordinate()).setColorAndUsed(Color.BURLYWOOD);
            }
            int tmp = this.leftToPlace.get();
            this.leftToPlace.set(tmp-1);
            tmp = this.sizeToPlace[this.currentPlacingSize-1].get();
            this.sizeToPlace[this.currentPlacingSize-1].set(tmp-1);
        }
    }

    public void removeShip(boolean vertical,int x,int y, int length){
        if(vertical){
            for(int i=y; i<y+length; i++){
                this.getCell(x,i).cleanCell();
            }
        }
        else {
            for(int i=x; i<x+length; i++){
                this.getCell(i,y).cleanCell();
            }
        }
        int tmp = this.leftToPlace.get();
        this.leftToPlace.set(tmp+1);
        tmp = this.sizeToPlace[length-1].get();
        this.sizeToPlace[length-1].set(tmp+1);
    }

    private void addListeners(){
        this.sizeToPlace[0].addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(0)).setDisable(true);
                    controller.setNewSizeRadio();
                }
                else if((oldValue.intValue() == 0) && (newValue.intValue() == 1)) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(0)).setDisable(false);
                    controller.setNewSizeRadio();
                }
            }
        });

        this.sizeToPlace[1].addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(1)).setDisable(true);
                    controller.setNewSizeRadio();
                }
                else if((oldValue.intValue() == 0) && (newValue.intValue() == 1)) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(1)).setDisable(false);
                    controller.setNewSizeRadio();
                }
            }
        });

        this.sizeToPlace[2].addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(2)).setDisable(true);
                    controller.setNewSizeRadio();
                }
                else if((oldValue.intValue() == 0) && (newValue.intValue() == 1)) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(2)).setDisable(false);
                    controller.setNewSizeRadio();
                }
            }
        });

        this.sizeToPlace[3].addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(3)).setDisable(true);
                    controller.setNewSizeRadio();
                }
                else if((oldValue.intValue() == 0) && (newValue.intValue() == 1)) {
                    ((RadioButton) controller.getSizeGroup().getToggles().get(3)).setDisable(false);
                    controller.setNewSizeRadio();
                }
            }
        });

        this.leftToPlace.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0){
                    controller.radioActivate();
                }
                else if((oldValue.intValue() == 0) && (newValue.intValue() == 1)){
                    controller.radioDeactivate();
                }
            }
        });
    }
}
