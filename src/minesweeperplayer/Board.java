/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Board
|   Author: Michael Finch
|   Date: 2/23/17
|
|   A board is the entire game board which contains all of the cells
|   A board is a default size grid (including walls) unless specified
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    //States
    final int NOTHING = 0; //Absolutely nothing, not hidden, not a game piece
    final int EMPTY = 1; //Hidden Space with no contents
    final int WALL = 2;
    final int MINE = 3;
    
    Cell[][] gameBoard;
    
    int minesUnflagged;
    boolean seeded = false;
    int numberOfMines = 20;
    int rows;
    int columns;
    
    final int DEFAULT_HEIGHT = 26;
    final int DEFAULT_WIDTH = 26;
    final int DEFAULT_NUMBER_OF_MINES = 144;
    
    Board(File file) throws FileNotFoundException{
        this.gameBoard = getFileBoard(file);
    }
    
    //Method to create a rowsXcolumns board surrounded by a border of walls
    Board(int rows, int columns, int mines){
        numberOfMines = mines;
        minesUnflagged = numberOfMines;
        Cell[][] board;
        board = new Cell[rows + 2][columns + 2];
        
        //Create the walls and empty cells
        for(int i = 0; i < rows + 2; i++){
            for(int j = 0; j < columns + 2; j++){
                if(i == 0 || i == rows + 1 || j == 0 || j == columns + 1){
                    board[i][j] = new Cell(WALL, .25, .25, .25);
                }
                else{
                    board[i][j] = new Cell(EMPTY, .5, 1, .5);
                    board[i][j].setHidden(true);
                }
            }
        }
        
        gameBoard = board;
    }
    
    Cell[][] getFileBoard(File file) throws FileNotFoundException{
        Cell[][] fileBoard;
        Cell[][] loadedCells;
        
        Scanner scanner = new Scanner(file);
        System.out.println("START READ");
        
        numberOfMines = Integer.parseInt(scanner.nextLine());
        rows = Integer.parseInt(scanner.nextLine());
        columns = Integer.parseInt(scanner.nextLine());
        loadedCells = new Cell[rows][columns];
        
        try {
            int row = 0;
            while (scanner.hasNextLine()) {
                String[] lineBeingExamined = scanner.nextLine().split(" ");
                
                //loop through each group of 4
                for(int column = 0; column < columns; column++){
                    int state = Integer.parseInt(lineBeingExamined[column * 4]);
                    double red = Double.parseDouble(lineBeingExamined[column * 4 + 1]);
                    double green = Double.parseDouble(lineBeingExamined[column * 4 + 2]);
                    double blue = Double.parseDouble(lineBeingExamined[column * 4 + 3]);
                    loadedCells[row][column] = new Cell(state, red, green, blue);
                    if(state == 1){
                        loadedCells[row][column].setHidden(true);
                    }
                }
                row++;
            }
        }
        finally {
            scanner.close();
        }
        
        //int rows = stringArray.size();
        //int columns = stringArray.get(0).get(0).length;
        System.out.println(rows + ", " + columns);
        fileBoard = new Cell[rows + 2][columns + 2];
        
        minesUnflagged = numberOfMines;
        
        return loadedCells;
    }
    
    //Seed the board with mines with a given first move
    void seedBoard(int row, int column){
        for(int i = 0; i < numberOfMines;){
            int rand1 = (int)(Math.random() * getHeight()- 1);
            int rand2 = (int)(Math.random() * getWidth() - 1);
            
            //Whether or not the chosen location neighbors the cell clicked
                                       //Top Left                                   //Top Center                             //Top Right
            boolean neighborsClick = ((rand1 == row - 1 && rand2 == column - 1) || (rand1 == row - 1 && rand2 == column) || (rand1 == row - 1 && rand2 == column + 1) ||
                                       //Center Left                                //Center Center                          //Center Right
                                      (rand1 == row && rand2 == column - 1)     || (rand1 == row && rand2 == column)     || (rand1 == row && rand2 == column + 1) ||
                                       //Bottom Left                                //Bottom Center                          //Bottom Right
                                      (rand1 == row + 1 && rand2 == column - 1) || (rand1 == row + 1 && rand2 == column) || (rand1 == row + 1 && rand2 == column + 1));
            
            if(!neighborsClick && gameBoard[rand1][rand2].getState() == EMPTY){
                gameBoard[rand1][rand2].setState(MINE);
                incrementNeighborsMineCount(gameBoard, rand1, rand2);
                i++;
            }
        }
    }
    
    //Return width of board
    int getWidth(){
        return gameBoard[0].length;
    }
    
    //Return height of board
    int getHeight(){
        return gameBoard.length;
    }
    
    //Return a cell
    Cell getCell(int row, int column){
        return gameBoard[row][column];
    }
    
    //Return mines
    int getNumberOfMines(){
        return numberOfMines;
    }
    
    //Return mines unflagged
    int getMinesUnflagged(){
        return minesUnflagged;
    }
    
    //Set mines unglagged
    void setMinesUnflagged(int minesUnflagged){
        this.minesUnflagged = minesUnflagged;
    }
    
    //Return seeded
    boolean isSeeded(){
        return seeded;
    }
    
    //Set seeded
    void setSeeded(boolean seeded){
        this.seeded = seeded;
    }
    
    //Method to increase a mine's neighbors count of neighboring bombs
    void incrementNeighborsMineCount(Cell[][] defaultBoard, int row, int column){
        boolean canGoUp = row > 0;
        boolean canGoDown = row < getHeight() - 1;
        boolean canGoLeft = column > 0;
        boolean canGoRight = column < getWidth() - 1;
        
        //Up Left
        if(canGoUp && canGoLeft){
            defaultBoard[row - 1][column - 1].incrementNeighboringMines();
        }
        
        //Up Center
        if(canGoUp){
            defaultBoard[row - 1][column].incrementNeighboringMines();
        }
        
        //Up Right
        if(canGoUp && canGoRight){
            defaultBoard[row - 1][column + 1].incrementNeighboringMines();
        }
        
        //Center Left
        if(canGoLeft){
            defaultBoard[row][column - 1].incrementNeighboringMines();
        }
        
        //Center Right
        if(canGoRight){
            defaultBoard[row][column + 1].incrementNeighboringMines();
        }
        
        //Down Left
        if(canGoDown && canGoLeft){
            defaultBoard[row + 1][column - 1].incrementNeighboringMines();
        }
        
        //Down Center
        if(canGoDown){
            defaultBoard[row + 1][column].incrementNeighboringMines();
        }
        
        //Down Right
        if(canGoDown && canGoRight){
            defaultBoard[row + 1][column + 1].incrementNeighboringMines();
        }
        
    }
    
    void displayLogicBoard(){
        for(int i = 0; i < gameBoard.length; i++){
            for(int j = 0; j < gameBoard[0].length; j++){
                if(gameBoard[i][j].getState() == WALL){
                    System.out.print("W");
                }
                else if(gameBoard[i][j].getState() == MINE){
                    System.out.print("M");
                }
                else if(gameBoard[i][j].getState() == NOTHING){
                    System.out.print(" ");
                }
                else{
                    System.out.print(gameBoard[i][j].getNeighboringMines());
                }
            }
            System.out.println();
        }
    }
    
    void displayGraphicBoard(){
        for(int i = 0; i < gameBoard.length; i++){
            for(int j = 0; j < gameBoard[0].length; j++){
                if(gameBoard[i][j].isHidden()){
                    System.out.print("H");
                }
                else {
                    if (gameBoard[i][j].getState() == WALL) {
                        System.out.print("W");
                    }
                    else if (gameBoard[i][j].getState() == MINE) {
                        System.out.print("M");
                    }
                    else if (gameBoard[i][j].getState() == NOTHING) {
                        System.out.print(" ");
                    }
                    else {
                        System.out.print(gameBoard[i][j].getNeighboringMines());
                    }
                }
            }
            System.out.println();
        }
    }
}