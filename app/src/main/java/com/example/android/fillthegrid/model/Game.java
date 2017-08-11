package com.example.android.fillthegrid.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.android.fillthegrid.utils.GameGridUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reggie on 03/08/2017.
 * Custom class to represent the game
 */
public class Game implements Parcelable {

    /**
     * The constant LEVEL_BEGINNER.
     */
    public static int LEVEL_BEGINNER = 5;
    /**
     * The constant LEVEL_NOVICE.
     */
    public static int LEVEL_NOVICE = 6;
    /**
     * The constant LEVEL_STUDENT.
     */
    public static int LEVEL_STUDENT = 7;
    /**
     * The constant LEVEL_ADVANCED.
     */
    public static int LEVEL_ADVANCED = 8;
    /**
     * The constant LEVEL_INTERMEDIATE.
     */
    public static int LEVEL_INTERMEDIATE = 9;
    /**
     * The constant LEVEL_EXPERT.
     */
    public static int LEVEL_EXPERT = 10;
    /**
     * The constant LEVEL_GENIUS.
     */
    public static int LEVEL_GENIUS = 11;
    /**
     * The constant LEVEL_WIZARD.
     */
    public static int LEVEL_WIZARD = 12;
    /**
     * The constant LEVEL_GOD.
     */
    public static int LEVEL_GOD = 13;

    /**
     * The constant GAME_SIZE_SMALL.
     */
    public static int GAME_SIZE_SMALL = 36;
    /**
     * The constant GAME_SIZE_MEDIUM.
     */
    public static int GAME_SIZE_MEDIUM = 49;
    /**
     * The constant GAME_SIZE_LARGE.
     */
    public static int GAME_SIZE_LARGE = 64;


    private int difficultyLevel = LEVEL_BEGINNER;

    private int gridSize = GAME_SIZE_SMALL;

    private List<GridItem> gridItemList = new ArrayList<>();

    /**
     * Instantiates a new Game.
     *
     * @param difficultyLevel the difficulty level
     * @param gridSize        the grid size
     */
    public Game(int difficultyLevel, int gridSize) {
        this.difficultyLevel = difficultyLevel;
        this.gridSize = gridSize;

        for (int i = 0; i < gridSize; i++) {
            this.gridItemList.add(new GridItem(GameGridUtils.setRandomColor(difficultyLevel)));
        }

        updateGridItemList();

    }

    /**
     * Gets grid item list.
     *
     * @return the grid item list
     */
    public List<GridItem> getGridItemList() {
        return gridItemList;
    }

    /**
     * Gets grid size.
     *
     * @return the grid size
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Update grid item list.
     */
    public void updateGridItemList(){
        GameGridUtils.getConnectedSquares(this.gridItemList);
    }

    /**
     * Set new colors.
     *
     * @param positionList the position list
     * @param colorResID   the color res id
     */
    public void setNewColors(List<Integer> positionList, int colorResID){
        for (int position : positionList) {
            this.gridItemList.get(position).setColorResID(colorResID);
        }
    }

    /**
     * Checks if the game is complete. If all grid items are the same colour, return true.
     *
     * @return the boolean
     */
    public boolean isComplete(){
        int colorResID = this.gridItemList.get(0).getColorResID();
        int count = 0;
        while(count < gridSize && gridItemList.get(count).getColorResID() == colorResID){
            count++;
        }
        Log.e("Size: " + gridSize, "Count: " + count);
        return count == gridSize;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.difficultyLevel);
        dest.writeInt(this.gridSize);
        dest.writeTypedList(this.gridItemList);
    }

    /**
     * Instantiates a new Game.
     *
     * @param in the in
     */
    protected Game(Parcel in) {
        this.difficultyLevel = in.readInt();
        this.gridSize = in.readInt();
        this.gridItemList = in.createTypedArrayList(GridItem.CREATOR);
    }

    /**
     * The constant CREATOR.
     */
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
