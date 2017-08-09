package com.example.android.fillthegrid.model;

import com.example.android.fillthegrid.utils.GameGridUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reggie on 03/08/2017.
 * Custom class to represent the game
 */

public class Game {

    public static int LEVEL_BEGINNER = 5;
    public static int LEVEL_NOVICE = 6;
    public static int LEVEL_STUDENT = 7;
    public static int LEVEL_ADVANCED = 8;
    public static int LEVEL_INTERMEDIATE = 9;
    public static int LEVEL_EXPERT = 10;
    public static int LEVEL_WIZARD = 11;
    public static int LEVEL_GOD = 12;

    public static int GAME_SIZE_SMALL = 36;
    public static int GAME_SIZE_MEDIUM = 49;
    public static int GAME_SIZE_LARGE = 64;


    private int difficultyLevel = LEVEL_BEGINNER;

    private int gridSize = GAME_SIZE_SMALL;

    private List<GridItem> gridItemList = new ArrayList<>();

    public Game(int difficultyLevel, int gridSize) {
        this.difficultyLevel = difficultyLevel;
        this.gridSize = gridSize;

        for (int i = 0; i < gridSize; i++) {
            this.gridItemList.add(new GridItem(GameGridUtils.setRandomColor(difficultyLevel)));
        }

        updateGridItemList();

    }

    public List<GridItem> getGridItemList() {
        return gridItemList;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void updateGridItemList(){
        GameGridUtils.getConnectedSquares(this.gridItemList);
    }

    public void setNewColors(List<Integer> positionList, int colorResID){
        for (int position : positionList) {
            this.gridItemList.get(position).setColorResID(colorResID);
        }
    }

    public boolean isComplete(){
        int colorResID = this.gridItemList.get(0).getColorResID();
        int count = 0;
        while(count < gridSize && gridItemList.get(count).getColorResID() == colorResID){
            count++;
        }
        return count == gridSize;
    }
}
