/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Play
|   Author: Michael Finch
|   Date: 2/22/17
|
|   Main class. Used to run the program
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Play extends Application{
    
    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Buttons for main menu
        Button defaultButton = new Button("Default");
        Button loadButton = new Button("Load");
        Button createButton = new Button("Create");
        
        //Textboxes for sizing
        TextField rowsTextField = new TextField("Rows");
        TextField columnsTextField = new TextField("Columns");
        TextField minesTextField = new TextField("Mines");
        
        //Organize the buttons into a row
        HBox buttonPane = new HBox(defaultButton, loadButton, createButton);
        
        VBox mainMenu = new VBox(buttonPane, rowsTextField, columnsTextField, minesTextField);
        
        //Prepare the two additional stages
        Stage gameStage = new Stage();
        Stage editorStage = new Stage();
        
        //Create a scene for the main menu and show the stage
        Scene mainMenuScene = new Scene(mainMenu);
        stage.setScene(mainMenuScene);
        stage.show();
        
        //Set button behaviors
        defaultButton.setOnAction(e ->{
            Board defaultBoard;
            try{
                int rows = Integer.parseInt(rowsTextField.getText());
                int columns = Integer.parseInt(columnsTextField.getText());
                int mines = Integer.parseInt(minesTextField.getText());
                
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
        loadButton.setOnAction(e ->{
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
        createButton.setOnAction(e ->{
            MakerPane makerPane = new MakerPane();
            Scene scene = new Scene(makerPane);
            editorStage.setScene(scene);
            editorStage.show();
        });
    }
}