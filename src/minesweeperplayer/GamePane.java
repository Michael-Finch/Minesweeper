/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: GamePane
|   Author: Michael Finch
|   Date: 2/23/17
|
|   Display a given board of Minesweeper using JavaFX
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class GamePane extends VBox{
    //Maximum mainPane size before introducing scrolling
    double scrollPaneSize = Screen.getPrimary().getVisualBounds().getHeight() / 1.1;
        
    //GUI components
    Label labelMinesRemaining = new Label();
    
    //Different sections of the board
    Pane topPane = new Pane(labelMinesRemaining);
    Pane mainPane = new Pane();
    ScrollPane scrollPane;
    
    //Array to hold the cells' graphics
    CellGraphic[][] artArray;
    
    //Logic board being used
    Board board;
    
    //States
    final int NOTHING = 0; //Absolutely nothing, not hidden, not a game piece
    final int EMPTY = 1; //Hidden Space with no contents
    final int WALL = 2;
    final int MINE = 3;
    
    //Measures of how many pixels a cell
    final int CELL_CENTER_SIZE = 16;
    final int CELL_BORDER_SIZE = 0;
    
    GamePane(Board boardUsed){
        this.board = boardUsed;
        
        //Set the pane structure based on whether or not the field is too big to be shown all at once
        if(board.getHeight() * getCellSize() < scrollPaneSize && board.getWidth() * getCellSize() < scrollPaneSize){
            this.getChildren().addAll(topPane, mainPane);
        }
        else{
            scrollPane = new ScrollPane(mainPane);

            scrollPane.setPrefSize(scrollPaneSize, scrollPaneSize);
            this.getChildren().addAll(topPane, scrollPane);
        }
        
        //Create the board that is displayed to the player
        artArray = new CellGraphic[board.getHeight()][board.getWidth()];
        
        for(int i = 0; i < board.getHeight(); i++){
            for(int j = 0; j < board.getWidth(); j++){
                
                int state = board.getCell(i, j).getState();
                double red = board.getCell(i, j).getRed();
                double green = board.getCell(i, j).getGreen();
                double blue = board.getCell(i, j).getBlue();
                
                CellGraphic cellGraphic;
                
                if(board.getCell(i, j).getState() == NOTHING){
                    cellGraphic = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, new Color(1, 1, 1, 1), new Color(1, 1, 1, 1)), null);
                }
                else{
                    cellGraphic = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, new Color(red, green, blue, 1), Color.BLACK), null);

                }
                artArray[i][j] = cellGraphic;
                artArray[i][j].setLayoutX(j * CELL_CENTER_SIZE);
                artArray[i][j].setLayoutY(i * CELL_CENTER_SIZE);
                
                mainPane.getChildren().add(artArray[i][j]);
            }
        }
        
        //Set the behavior for when the board is clicked
        mainPane.setOnMouseClicked(e ->{
            int[] clickLocation = getMouseClick(e);
            int row = clickLocation[0];
            int column = clickLocation[1];
            
            Cell cellClicked = board.getCell(row, column);
            if(!board.isSeeded()){
                board.seedBoard(row, column);
                board.displayLogicBoard();
                board.setSeeded(true);
            }
            if(e.getButton() == MouseButton.PRIMARY){
                if(cellClicked.isHidden() && !cellClicked.isFlagged()){
                    revealCell(row, column);
                }
            }
            else if(e.getButton() == MouseButton.SECONDARY){
                flagCell(row, column);
            }
        });
        updateLabel();
    }
    
    //Method for revealing a hidden cell
    void revealCell(int row, int column){
        if (board.getCell(row, column).isHidden()) {
            if (board.getCell(row, column).getState() == EMPTY && board.getCell(row, column).getNeighboringMines() > 0) {
                mainPane.getChildren().remove(artArray[row][column]);
                artArray[row][column].setCellColor(new Color(.75, .75, .75, 1));
                switch(board.getCell(row, column).getNeighboringMines()){
                    case 1:
                        artArray[row][column].setText("1");
                        break;
                    case 2:
                        artArray[row][column].setText("2");
                        break;
                    case 3:
                        artArray[row][column].setText("3");
                        break;
                    case 4:
                        artArray[row][column].setText("4");
                        break;
                    case 5:
                        artArray[row][column].setText("5");
                        break;
                    case 6:
                        artArray[row][column].setText("6");
                        break;
                    case 7:
                        artArray[row][column].setText("7");
                        break;
                    case 8:
                        artArray[row][column].setText("8");
                        break;
                }
                mainPane.getChildren().add(artArray[row][column]);
                board.getCell(row, column).setHidden(false);
            }
            else if (board.getCell(row, column).getState() == EMPTY && board.getCell(row, column).getNeighboringMines() == 0) {
                artArray[row][column].setCellColor(new Color(.75, .75, .75, 1));
                artArray[row][column].setText(null);
                
                mainPane.getChildren().remove(artArray[row][column]);
                
                mainPane.getChildren().add(artArray[row][column]);
                board.getCell(row, column).setHidden(false);
                revealNeighbors(row, column);
            }
            else if (board.getCell(row, column).getState() == MINE) {
                
                
                gameOver();
            }
        }
    }
    
    //Method for revealing applicable neighbors
    void revealNeighbors(int row, int column){
        boolean canGoUp = row > 0;
        boolean canGoDown = row < board.getHeight() - 1;
        boolean canGoLeft = column > 0;
        boolean canGoRight = column < board.getWidth() - 1;
        
        //Up
        if (canGoUp) {
            if (board.getCell(row - 1, column).isHidden() && !board.getCell(row - 1, column).isFlagged()) {
                if (board.getCell(row - 1, column).getState() != MINE) {
                    revealCell(row - 1, column);
                }
            }
        }
        //Down
        if (canGoDown) {
            if (board.getCell(row + 1, column).isHidden() && !board.getCell(row + 1, column).isFlagged()) {
                if (board.getCell(row + 1, column).getState() != MINE) {
                    revealCell(row + 1, column);
                }
            }
        }
        //Left
        if (canGoLeft) {
            if (board.getCell(row, column - 1).isHidden() && !board.getCell(row, column - 1).isFlagged()) {
                if (board.getCell(row, column - 1).getState() != MINE) {
                    revealCell(row, column - 1);
                }
            }
        }
        //Right
        if (canGoRight) {
            if (board.getCell(row, column + 1).isHidden() && !board.getCell(row, column + 1).isFlagged()) {
                if (board.getCell(row, column + 1).getState() != MINE) {
                    revealCell(row, column + 1);
                }
            }
        }
        //Up Left
        if (canGoUp && canGoLeft) {
            if (board.getCell(row - 1, column - 1).isHidden() && !board.getCell(row - 1, column - 1).isFlagged()) {
                if (board.getCell(row - 1, column - 1).getState() != MINE) {
                    revealCell(row - 1, column - 1);
                }
            }
        }
        //Down Left
        if (canGoDown && canGoLeft) {
            if (board.getCell(row + 1, column - 1).isHidden() && !board.getCell(row + 1, column - 1).isFlagged()) {
                if (board.getCell(row + 1, column - 1).getState() != MINE) {
                    revealCell(row + 1, column - 1);
                }
            }
        }
        //Up Right
        if (canGoUp && canGoRight) {
            if (board.getCell(row - 1, column + 1).isHidden() && !board.getCell(row - 1, column + 1).isFlagged()) {
                if (board.getCell(row - 1, column + 1).getState() != MINE) {
                    revealCell(row - 1, column + 1);
                }
            }
        }
        //Down Right
        if (canGoDown && canGoRight) {
            if (board.getCell(row + 1, column + 1).isHidden() && !board.getCell(row + 1, column + 1).isFlagged()) {
                if (board.getCell(row + 1, column + 1).getState() != MINE) {
                    revealCell(row + 1, column + 1);
                }
            }
        }
    }
    
    //Method for flagging a cell
    void flagCell(int row, int column){
        if (board.getCell(row, column).isHidden()) {
            if (!board.getCell(row, column).isFlagged()) {
                //mainPane.getChildren().remove(artArray[row][column]);
                
                artArray[row][column].drawFlag(true);
                
                //mainPane.getChildren().add(artArray[row][column]);
                board.getCell(row, column).setFlagged(true);
                board.setMinesUnflagged(board.getMinesUnflagged() - 1);
            }
            else {
                //mainPane.getChildren().remove(artArray[row][column]);
                
                artArray[row][column].drawFlag(false);
                
                //mainPane.getChildren().add(artArray[row][column]);
                board.getCell(row, column).setFlagged(false);
                board.setMinesUnflagged(board.getMinesUnflagged() + 1);
            }
            updateLabel();
        }
    }
    
    //Method to reset board
    void resetBoard(){
        for (int i = 0; i < getBoard().getHeight(); i++) {
            for (int j = 0; j < getBoard().getWidth(); j++) {
                if (getBoard().getCell(i, j).getState() != WALL && getBoard().getCell(i, j).getState() != NOTHING) {
                    getBoard().getCell(i, j).setHidden(true);
                    getBoard().getCell(i, j).setState(EMPTY);
                    getBoard().getCell(i, j).setNeighboringMines(0);
                    getBoard().getCell(i, j).setFlagged(false);
                    getBoard().setMinesUnflagged(getBoard().getNumberOfMines());
                }
            }
        }
    }
    
    //Method for getting the location of a mouse click
    public int[] getMouseClick(MouseEvent e){
        //Get the row and column clicked on
        int[] clickLocation = new int[2];
        clickLocation[0] = (int)Math.round(e.getY() / CELL_CENTER_SIZE - 0.5);
        clickLocation[1] = (int)Math.round(e.getX() / CELL_CENTER_SIZE - 0.5);
        System.out.println("Click at: " + clickLocation[0] + ", " + clickLocation[1]);
        return clickLocation;
    }
    
    //Method for getting the cell size
    public double getCellSize(){
        return CELL_CENTER_SIZE;
    }
    
    //Return the board being used
    public Board getBoard(){
        return board;
    }
    
    //Update the mines remaining label
    public void updateLabel(){
        labelMinesRemaining.setText("Mines: " + getBoard().getMinesUnflagged());
    }
    
    //Method for what happens when the user clicks a mine
    public void gameOver(){
        for (int row = 0; row < board.getHeight(); row++) {
            for (int column = 0; column < board.getWidth(); column++) {
                if (board.getCell(row, column).getState() == MINE) {
                    artArray[row][column].drawMine();

                    mainPane.getChildren().remove(artArray[row][column]);
                    mainPane.getChildren().add(artArray[row][column]);
                    board.getCell(row, column).setHidden(false);
                    board.setMinesUnflagged(board.getMinesUnflagged() - 1);
                    updateLabel();
                }
                else{
                    board.getCell(row, column).setFlagged(false);
                    if(board.getCell(row, column).isFlagged()){
                        artArray[row][column].drawFlag(false);
                    }
                    artArray[row][column].redraw();
                    board.getCell(row, column).setHidden(false);
                }
            }
        }
    }
}
