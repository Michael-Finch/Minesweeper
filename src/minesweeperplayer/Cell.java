/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Cell
|   Author: Michael Finch
|   Date: 2/23/17
|
|   A cell is the smallest game piece in minesweeper. It contains
|   information regarding a single space on the board
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

public class Cell {
    //States
    final int NOTHING = 0; //Absolutely nothing, not hidden, not a game piece
    final int EMPTY = 1; //Hidden Space with no contents
    final int WALL = 2;
    final int MINE = 3;
    
    double red = 0.0;
    double green = 0.0;
    double blue = 0.0;
    
    boolean hidden = false;
    boolean flagged = false;
    int state = 0; //0 = empty, 1 = mine, 2 = wall
    int neighboringMines = 0;
    
    //A default contstructor will generate a hiden empty cell
    Cell(){
        hidden = true;
        state = EMPTY;
    }
    
    //Create a mine with a given state
    Cell(int state, double red, double green, double blue){
        this.state = state;
        this.red = red;
        this.green = green;
        this.blue = blue;
        if(this.state != WALL && this.state != NOTHING){
            this.hidden = true;
        }
    }
    
    void printSummary(){
        System.out.println("Cell: ");
        System.out.println("Hidden: " + hidden);
        System.out.println("Flagged: " + flagged);
        System.out.println("NM: " + neighboringMines);
        System.out.println("State: " + state);
        System.out.println("Red: " + red);
        System.out.println("Green: " + green);
        System.out.println("Blue: " + blue);
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }

    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }
    
    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
    }

    public void incrementNeighboringMines(){
        this.neighboringMines += 1;
    }
    
    public boolean isFlagged(){
        return flagged;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }
    
    public void setFlagged(boolean flagged){
        this.flagged = flagged;
    }
}