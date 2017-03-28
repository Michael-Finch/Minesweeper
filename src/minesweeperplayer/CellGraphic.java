/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Cell Graphic
|   Author: Michael Finch
|   Date: 3/21/17
|
|   A cell graphic is a pane, and a graphic representation of a
|   cell to be used in a game 
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CellGraphic extends StackPane{
    CustomRectangle customRectangle;
    String text;
    Label numberLabel;
    
    CellGraphic(CustomRectangle customRectangle, String stringToDisplay){
        this.customRectangle = customRectangle;
        this.text = stringToDisplay;
        
        numberLabel = new Label(stringToDisplay);
        
        if(customRectangle.getColorSum() <= 1.5){
            numberLabel.setTextFill(new Color(1, 1, 1, 1));
        }
        else{
            numberLabel.setTextFill(new Color(0, 0, 0, 1));
        }
        
        this.getChildren().addAll(customRectangle, numberLabel);
    }

    public String getStringToDisplay() {
        return text;
    }

    public void setText(String stringToDisplay) {
        this.numberLabel.setText(stringToDisplay);
        this.text = stringToDisplay;
    }
    
    void setCellColor(Color color){
        this.customRectangle.setCenterColor(color);
        if(customRectangle.getColorSum() <= 1.5){
            numberLabel.setTextFill(new Color(1, 1, 1, 1));
        }
        else{
            numberLabel.setTextFill(new Color(0, 0, 0, 1));
        }
        System.out.println(customRectangle.getColorSum());
    }
    
    void setBorderColor(Color color){
        this.customRectangle.setBorderColor(color);
    }
    
    void setTextColor(Color color){
        this.numberLabel.setTextFill(color);
    }
    
    void drawMine(){
        this.setCellColor(new Color(.25, .25, .25, 1));
        this.setTextColor(Color.RED);
        this.setText("X");
    }
    
    void drawFlag(boolean bool){
        if(bool){
            this.setText("F");
        }
        else{
            this.setText(null);
        }
    }
    
    void redraw(){
        this.getChildren().clear();
        this.getChildren().addAll(customRectangle, numberLabel);
    }
}
