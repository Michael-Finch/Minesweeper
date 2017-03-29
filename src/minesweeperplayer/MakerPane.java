/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: MakerPane
|   Author: Michael Finch
|   Date: 2/28/17
|
|   Used to create and save boards of minesweeper
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class MakerPane extends VBox{
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    Menu saveMenu = new Menu("Save");
    Menu loadMenu = new Menu("Load");
    
    ScrollPane scrollPane = new ScrollPane();
    EditorPane editorPane = new EditorPane(10, 10);
    
    HBox sizeControlPane = new HBox();
    
    TextField rowsTextField = new TextField("Rows");
    TextField columnsTextField = new TextField("Columns");
    Button resizeButton = new Button("Resize");
    
    HBox minesPane = new HBox();
    
    TextField minesTextField = new TextField("Mines");
    
    HBox colorPickerPane = new HBox();
            
    ColorPicker colorPicker = new ColorPicker();
    
    
 
    
    HBox optionsPane = new HBox();
    
    ObservableList<String> options
            = FXCollections.observableArrayList(
                    "Cell",
                    "Wall",
                    "Eraser"
            );
    
    ComboBox optionsComboBox = new ComboBox(options);
    
    
    MakerPane(){
        //Allow the combobox to work on systems with touch screens
        System.setProperty("glass.accessible.force", "false");
        
        menuBar.getMenus().addAll(fileMenu);
        fileMenu.getItems().addAll(saveMenu, loadMenu);
        
        saveMenu.setOnAction(e ->{
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper files (*.mine)", "*.mine"));
                File file = fileChooser.showSaveDialog(null);
                
                if(file != null){
                    FileWriter fileWriter = new FileWriter(file);
                    int mines = 0;
                    try{
                        mines = Integer.parseInt(minesTextField.getText());
                    }
                    catch(NumberFormatException ex){
                        
                    }
                    fileWriter.write(mines + "\r\n");
                    fileWriter.write(editorPane.getMapString());
                    fileWriter.flush();
                    fileWriter.close();
                }
            }
            catch (IOException ex) {
                
            }
        });
        loadMenu.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Minesweeper files (*.mine)", "*.mine"));
                //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".mine"));
                File file = fileChooser.showOpenDialog(null);
                
                editorPane.createFromFile(file);
                minesTextField.setText(String.valueOf(editorPane.getNumberOfMines()));
            }
            catch (Exception ex) {

            }

        });

        colorPicker.setOnAction(e ->{
            Color color = colorPicker.getValue();
            editorPane.setColor(color);
        });
        
        this.getChildren().add(menuBar);
        scrollPane.setContent(editorPane);
        scrollPane.setPrefSize(800, 800);
        sizeControlPane.getChildren().addAll(rowsTextField, columnsTextField, resizeButton);
        minesPane.getChildren().add(minesTextField);
        optionsPane.getChildren().add(optionsComboBox);
        colorPickerPane.getChildren().add(colorPicker);
        this.getChildren().addAll(scrollPane, sizeControlPane, minesPane, colorPickerPane, optionsPane);
        
        resizeButton.setOnAction(e ->{
            int rows = Integer.parseInt(rowsTextField.getText());
            int columns = Integer.parseInt(columnsTextField.getText());
            editorPane = new EditorPane(rows, columns);
            scrollPane.setContent(editorPane);
        });
        
        optionsComboBox.setOnAction(e ->{
            editorPane.setChosenOption(optionsComboBox.getValue().toString());
        });
    }
    
}

class EditorPane extends Pane{
    
    //Measures of how many pixels a cell
    final int CELL_CENTER_SIZE = 15;
    final int CELL_BORDER_SIZE = 1;
    
    //States
    final int NOTHING = 0; //Absolutely nothing, not hidden, not a game piece
    final int EMPTY = 1; //Hidden Space with no contents
    final int WALL = 2;
    
    String chosenOption = "Cell";//Option chosen by the MakerPane's comboBox
    
    Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
    
    //Array to hold the cells' graphics
    CellGraphic[][] artArray;
    
    //Array to hold cell states
    Cell[][] cells;
    
    int numberOfMines;
    int rows;
    int columns;
    
    EditorPane(int rows, int columns){
        artArray = new CellGraphic[rows][columns];
        cells = new Cell[rows][columns];
        resizePane(rows, columns);
        createEmpty(rows, columns);
        
        this.setOnMouseClicked(e ->{
            int[] clickPosition = getMouseClick(e);
            int row = clickPosition[0];
            int column = clickPosition[1];
            setCell(row, column);
        });
        this.setOnMouseDragged(e ->{
            int[] clickPosition = getMouseClick(e);
            int row = clickPosition[0];
            int column = clickPosition[1];
            setCell(row, column);
        });
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
    
    //Create an empty board and display
    public void createEmpty(int rows, int columns){
        getChildren().clear();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(NOTHING, 1, 1, 1);
                artArray[i][j] = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, new Color(1, 1, 1, 1), new Color(0, 0, 0, 1)), "");
                
                artArray[i][j].setLayoutX(j * CELL_CENTER_SIZE);
                artArray[i][j].setLayoutY(i * CELL_CENTER_SIZE);
                
                getChildren().add(artArray[i][j]);
            }
        }
    }

    public void createFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        numberOfMines = Integer.parseInt(scanner.nextLine());
        rows = Integer.parseInt(scanner.nextLine());
        columns = Integer.parseInt(scanner.nextLine());
        
        getChildren().clear();
        resizePane(rows, columns);
        cells = new Cell[rows][columns];
        artArray = new CellGraphic[rows][columns];
        
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
                    
                    cells[row][column] = new Cell(state, red, green, blue);
                    artArray[row][column] = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, new Color(red, green, blue, 1), new Color(0, 0, 0, 1)), "");
                    
                    this.getChildren().remove(artArray[row][column]);
                    artArray[row][column].setLayoutX(column * CELL_CENTER_SIZE);
                    artArray[row][column].setLayoutY(row * CELL_CENTER_SIZE);
                    this.getChildren().add(artArray[row][column]);
                }
                row++;
            }
        }
        finally {
            scanner.close();
        }
    }

    public void resizePane(int rows, int columns){
            this.resize(columns * CELL_CENTER_SIZE, rows * CELL_CENTER_SIZE);
            System.out.println("Height: " + this.getHeight() + " Width: " + this.getWidth());
    }
    
    void setChosenOption(String chosenOption){
        this.chosenOption = chosenOption;
    }
    
    void setCell(int row, int column){
        if(chosenOption.equals("Cell")){
            cells[row][column] = new Cell(EMPTY, color.getRed(), color.getGreen(), color.getBlue());
            artArray[row][column] = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, color, new Color(0, 0, 0, 1)), "");
            this.getChildren().remove(artArray[row][column]);
            artArray[row][column].setLayoutX(column * CELL_CENTER_SIZE);
            artArray[row][column].setLayoutY(row * CELL_CENTER_SIZE);
            this.getChildren().add(artArray[row][column]);
        }
        else if(chosenOption.equals("Wall")){
            cells[row][column] = new Cell(WALL, color.getRed(), color.getGreen(), color.getBlue());
            artArray[row][column] = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, color, new Color(0, 0, 0, 1)), "");
            this.getChildren().remove(artArray[row][column]);
            artArray[row][column].setLayoutX(column * CELL_CENTER_SIZE);
            artArray[row][column].setLayoutY(row * CELL_CENTER_SIZE);
            this.getChildren().add(artArray[row][column]);
        }
        else if(chosenOption.equals("Eraser")){
            cells[row][column] = new Cell(NOTHING, 1, 1, 1);
            artArray[row][column] = new CellGraphic(new CustomRectangle(CELL_CENTER_SIZE, CELL_BORDER_SIZE, new Color(1, 1, 1, 1), new Color(0, 0, 0, 1)), "");
            this.getChildren().remove(artArray[row][column]);
            artArray[row][column].setLayoutX(column * CELL_CENTER_SIZE);
            artArray[row][column].setLayoutY(row * CELL_CENTER_SIZE);
            this.getChildren().add(artArray[row][column]);
        }
    }
    
    //Get a string representation of the board for saving files
    String getMapString(){
        String string = "";
        string += cells.length + "\r\n";
        string += cells[0].length + "\r\n";
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[0].length; j++){
                String red = String.valueOf(cells[i][j].getRed());
                string += cells[i][j].getState() + " " + Math.round(cells[i][j].getRed()*100.0)/100.0 + " " + Math.round(cells[i][j].getGreen()*100.0)/100.0 + " " + Math.round(cells[i][j].getBlue()*100.0)/100.0 + " ";
            }
            string += "\r\n";
        }
        return string;
    }
    
    //Get number of miens
    public int getNumberOfMines(){
        return numberOfMines;
    }
    
    void setColor(Color color){
        this.color = color;
    }
}