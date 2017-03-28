/*
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
|
|   Class: Custom Rectangle
|   Author: Michael Finch
|   Date: 3/21/17
|
|   A custom rectangle is an extension of a javafx rectangle. It
|   is a part of what makes of a cell graphic
|
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
package minesweeperplayer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class CustomRectangle extends Rectangle{

    int centerSize;
    int borderSize;
    Color centerColor;
    Color borderColor;
    
    public CustomRectangle(int centerSize, int borderSize, Color centerColor, Color borderColor) {
        this.centerSize = centerSize;
        this.borderSize = borderSize;
        this.centerColor = centerColor;
        this.borderColor = borderColor;
        
        this.setWidth(centerSize - borderSize);
        this.setHeight(centerSize - borderSize);
        this.setFill(centerColor);
        this.setStroke(borderColor);
        this.setStrokeWidth(borderSize);
    }
    
    public int getTotalSize(){
        return centerSize + borderSize;
    }

    public int getCenterSize() {
        return centerSize;
    }

    public void setCenterSize(int centerSize) {
        this.setWidth(centerSize);
        this.setHeight(centerSize);
        this.centerSize = centerSize;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.setStrokeWidth(borderSize);
        this.borderSize = borderSize;
    }

    public Paint getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(Color centerColor) {
        this.centerColor = centerColor;
        this.setFill(centerColor);
    }

    public Paint getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.setStroke(borderColor);
        this.borderColor = borderColor;
    }
    
    public double getColorSum(){
        return centerColor.getRed() + centerColor.getGreen() + centerColor.getBlue();
    }
}
