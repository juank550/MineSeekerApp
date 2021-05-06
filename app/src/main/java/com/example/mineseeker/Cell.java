package com.example.mineseeker;

public class Cell {
    public int content;
    public boolean visible;

    private int firstX, firstY, lastX, lastY;

    public void setXY(int initX, int row, int cellWidth){
        firstX = initX;
        firstY = row;
        lastX = firstX+cellWidth;
        lastY = firstY+cellWidth;
    }

    public boolean pixelIn(int x, int y){
        if (firstX <= x && x < lastX && firstY <= y && y < lastY){
            return true;
        }else{
            return false;
        }
    }

}
