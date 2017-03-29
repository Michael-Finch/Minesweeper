/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Main Menu
|   Author: Michael Finch
|   Date: 3/29/17
|
|   Menu displayed upon execution of the program. Used as the main
|   means for interacting with the program.
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainMenu extends VBox{
    //Buttons portion of the main menu
    
    Button buttonStandard = new Button("Standard Map");
    Button buttonLoad = new Button("Load Map");
    Button buttonEditor = new Button("Level Editor");
    
    HBox containerButtons = new HBox(buttonStandard, buttonLoad, buttonEditor);
    
    //Options portion of the main menu
    
    Label labelOptions = new Label("Options");
    
    Label labelRows = new Label("Standard Map Rows: ");
    TextField textRows = new TextField("10");
    //HBox containerRows = new HBox(labelRows, textRows);
            
    Label labelColumns = new Label("Standard Map Columns: ");
    TextField textColumns = new TextField("10");
    //HBox containerColumns = new HBox(labelColumns, textColumns);
    
    Label labelMines = new Label("Standard Map Mines: ");
    TextField textMines = new TextField("10");
    //HBox containerMines = new HBox(labelMines, textMines);
    
    //VBox containerOptions = new VBox(labelOptions, containerRows, containerColumns, containerMines);
    GridPane containerOptions = new GridPane();
    
    public MainMenu() {
        setSpacing(5);
        
        containerButtons.setSpacing(20);
        
        containerOptions.add(labelOptions, 0, 0);
        containerOptions.add(labelRows, 0, 1);
        containerOptions.add(textRows, 1, 1);
        containerOptions.add(labelColumns, 0, 2);
        containerOptions.add(textColumns, 1, 2);
        containerOptions.add(labelMines, 0, 3);
        containerOptions.add(textMines, 1, 3);
        containerOptions.setVgap(5);
        
        Stage gameStage = new Stage();
        Stage editorStage = new Stage();
        
        buttonStandard.setOnAction(e ->{
            Board defaultBoard;
            try{
                int rows = Integer.parseInt(textRows.getText());
                int columns = Integer.parseInt(textColumns.getText());
                int mines = Integer.parseInt(textMines.getText());
                
                defaultBoard = new Board(rows, columns, mines);
            }
            catch(NumberFormatException ex){
                System.out.println(ex);
                defaultBoard = new Board(9, 9, 10);
            }
            MinesweeperPane defaultGame = new MinesweeperPane(defaultBoard);
            Scene scene = new Scene(defaultGame);
            gameStage.setScene(scene);
            gameStage.show();
        });
        buttonLoad.setOnAction(e ->{
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper files (*.mine)", "*.mine"));
                File file = fileChooser.showOpenDialog(null);

                if (file != null) {
                    System.out.println("File");
                    Board fileBoard = new Board(file);
                    System.out.println("FileBoard Created\n");
                    MinesweeperPane fileGame = new MinesweeperPane(fileBoard);
                    System.out.println("MinesweeperPane Created");
                    Scene scene = new Scene(fileGame);
                    System.out.println("Scene Created");
                    gameStage.setScene(scene);
                    gameStage.show();
                    System.out.println("Stage shown");
                }
            }
            catch (Exception ex) {

            }
        });
        buttonEditor.setOnAction(e ->{
            MakerPane makerPane = new MakerPane();
            Scene scene = new Scene(makerPane);
            editorStage.setScene(scene);
            editorStage.show();
        });
        
        getChildren().addAll(containerButtons, containerOptions);
    }
    
}
