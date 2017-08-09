package com.example.android.fillthegrid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reggie on 03/08/2017.
 * A custom class to represent each grid item.
 */

public class GridItem {

    private int colorResID;

    List<Integer> connectedSquares;

    public GridItem(int colorResID) {
        this.colorResID = colorResID;
        this.connectedSquares = new ArrayList<>();
    }

    public int getColorResID() {
        return colorResID;
    }

    public void setColorResID(int colorResID) {
        this.colorResID = colorResID;
    }

    public List<Integer> getConnectedSquares() {
        return connectedSquares;
    }

    public void setConnectedSquares(List<Integer> connectedSquares) {
        this.connectedSquares = connectedSquares;
    }
}
