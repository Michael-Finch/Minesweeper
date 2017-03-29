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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Play extends Application{
    
    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu mainMenu = new MainMenu();
        Scene scene = new Scene(mainMenu);
        stage.setScene(scene);
        stage.show();
    }
}