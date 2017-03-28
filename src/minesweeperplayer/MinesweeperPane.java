/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: MinesweeperPane
|   Author: Michael Finch
|   Date: 2/26/17
|
|   Display a given GamePane and a reset button at the bottom
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MinesweeperPane extends VBox{
    Board board;
    GamePane gamePane;
    Pane bottomPane = new Pane();
    
    Button resetButton = new Button("Reset");
    
    MinesweeperPane(Board board){
        this.board = board;
        gamePane = new GamePane(board);
        
        bottomPane.getChildren().add(resetButton);
        
        this.getChildren().addAll(gamePane, bottomPane);
        
        resetButton.setOnAction(e ->{
            this.getChildren().remove(gamePane);
            this.getChildren().remove(bottomPane);
            
            gamePane.resetBoard();
            gamePane.getBoard().setSeeded(false);
            gamePane = new GamePane(gamePane.getBoard());
            
            this.getChildren().addAll(gamePane, bottomPane);
            
            gamePane.updateLabel();
        });
    }
}
