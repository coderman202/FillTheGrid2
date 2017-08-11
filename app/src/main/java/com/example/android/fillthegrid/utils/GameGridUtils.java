package com.example.android.fillthegrid.utils;

import com.example.android.fillthegrid.R;
import com.example.android.fillthegrid.model.GridItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Reggie on 03/08/2017.
 * Custom utility class.
 */

public final class GameGridUtils {

    private static Integer[] colorArray = new Integer[]{
            R.color.grid_black, R.color.grid_blue, R.color.grid_red, R.color.grid_green,
            R.color.grid_yellow, R.color.grid_purple, R.color.grid_orange, R.color.grid_grey_blue,
            R.color.grid_teal, R.color.grid_pink, R.color.grid_brown, R.color.grid_deep_purple,
            R.color.grid_light_green
    };

    /**
     * Empty constructor
     */
    private GameGridUtils(){

    }

    /**
     * Returns a random color res id with the range of possible colors being specified by the
     * difficulty level.
     * @param difficultyLevel   The difficulty level of the game
     * @return                  The color res id
     */
    public static int setRandomColor(int difficultyLevel){
        return colorArray[ThreadLocalRandom.current().nextInt(0, difficultyLevel)];
    }

    public static List<Integer> getSquaresToBeChanged(List<GridItem> gridItemList, int itemPosition){
        List<Integer> positionList = new ArrayList<>();

        List<Integer> connectedSquares = gridItemList.get(itemPosition).getConnectedSquares();

        for (int position : connectedSquares) {
            positionList.addAll(checkAllDirections(gridItemList, position));
        }

        Set<Integer> dupesRemoved = new LinkedHashSet<>(positionList);
        positionList.clear();
        positionList.addAll(dupesRemoved);

        positionList.removeAll(connectedSquares);
        Collections.sort(positionList);
        return positionList;
    }

    public static List<Integer> checkAllDirections(List<GridItem> gridItemList, int position){
        List<Integer> connectedSquares = new ArrayList<>();
        connectedSquares.addAll(checkUp(gridItemList, position));
        connectedSquares.addAll(checkDown(gridItemList, position));
        connectedSquares.addAll(checkLeft(gridItemList, position));
        connectedSquares.addAll(checkRight(gridItemList, position));

        // Remove duplicated and sort in order
        Set<Integer> dupesRemoved = new LinkedHashSet<>(connectedSquares);
        connectedSquares.clear();
        connectedSquares.addAll(dupesRemoved);

        Collections.sort(connectedSquares);

        return connectedSquares;
    }

    /**
     * Checks one position up to get the list of connected square positions which will be
     * changed color.
     * @param gridItemList  List of {@link GridItem}
     * @param position      int position
     * @return              List of {@link Integer}
     */
    public static List<Integer> checkUp(List<GridItem> gridItemList, int position){
        int height = (int)Math.sqrt(gridItemList.size());
        List<Integer> connectedSquares = new ArrayList<>();
        if(position > (height - 1)){
            connectedSquares = gridItemList.get(position - height).getConnectedSquares();
        }
        return connectedSquares;
    }

    /**
     * Checks one position down to get the list of connected square positions which will be
     * changed color.
     * @param gridItemList  List of {@link GridItem}
     * @param position      int position
     * @return              List of {@link Integer}
     */
    public static List<Integer> checkDown(List<GridItem> gridItemList, int position){
        int height = (int)Math.sqrt(gridItemList.size());
        List<Integer> connectedSquares = new ArrayList<>();
        if(position < (gridItemList.size() - height)){
            connectedSquares = gridItemList.get(position + height).getConnectedSquares();
        }
        return connectedSquares;
    }

    /**
     * Checks one position left to get the list of connected square positions which will be
     * changed color.
     * @param gridItemList  List of {@link GridItem}
     * @param position      int position
     * @return              List of {@link Integer}
     */
    public static List<Integer> checkLeft(List<GridItem> gridItemList, int position){
        int width = (int)Math.sqrt(gridItemList.size());
        List<Integer> connectedSquares = new ArrayList<>();
        if(position % width != 0){
            connectedSquares = gridItemList.get(position - 1).getConnectedSquares();
        }
        return connectedSquares;
    }

    /**
     * Checks one position left to get the list of connected square positions which will be
     * changed color.
     * @param gridItemList  List of {@link GridItem}
     * @param position      int position
     * @return              List of {@link Integer}
     */
    public static List<Integer> checkRight(List<GridItem> gridItemList, int position){
        int width = (int)Math.sqrt(gridItemList.size());
        List<Integer> connectedSquares = new ArrayList<>();
        if((position + 1) % width != 0){
            connectedSquares = gridItemList.get(position + 1).getConnectedSquares();
        }
        return connectedSquares;
    }


    /**
     * Set all the horizontally and vertically connected grid items by colour to each grid item
     * @param gridItemList  A list of {@link GridItem}.
     */
    public static void getConnectedSquares(List<GridItem> gridItemList){

        for (int i = 0; i < gridItemList.size(); i++) {
            List<Integer> connectedSquaresByColorPositions = searchAllDirections(gridItemList, i);
            gridItemList.get(i).setConnectedSquares(connectedSquaresByColorPositions);
        }
        for (int i = 0; i < gridItemList.size(); i++) {
            List<Integer> connectedSquaresByColorPositions = gridItemList.get(i).getConnectedSquares();
            for (int j = 0; j < connectedSquaresByColorPositions.size(); j++) {
                connectedSquaresByColorPositions.addAll(gridItemList.get(connectedSquaresByColorPositions.get(j)).getConnectedSquares());
                Set<Integer> dupesRemoved = new LinkedHashSet<>(connectedSquaresByColorPositions);
                connectedSquaresByColorPositions.clear();
                connectedSquaresByColorPositions.addAll(dupesRemoved);

                Collections.sort(connectedSquaresByColorPositions);
                gridItemList.get(i).setConnectedSquares(connectedSquaresByColorPositions);
            }
        }
    }

    /**
     *  Searches the list of {@link GridItem} to find all the positions directly above, below, left
     *  and right of the startPosition that are also the same colour as the GridItem at the
     *  startPosition.
     * @param gridItemList      A list of Grid Items
     * @param startPosition     The start position
     * @return                  A list of positions represented as Integers.
     */
    private static List<Integer> searchAllDirections(List<GridItem> gridItemList, int startPosition){
        List<Integer> sameColorPositions = new ArrayList<>();

        sameColorPositions.addAll(searchDownwards(gridItemList, startPosition));
        sameColorPositions.addAll(searchUpwards(gridItemList, startPosition));
        sameColorPositions.addAll(searchLeft(gridItemList, startPosition));
        sameColorPositions.addAll(searchRight(gridItemList, startPosition));

        // Remove all duplicates
        Set<Integer> dupesRemoved = new LinkedHashSet<>(sameColorPositions);
        sameColorPositions.clear();
        sameColorPositions.addAll(dupesRemoved);

        Collections.sort(sameColorPositions);

        return sameColorPositions;
    }

    //region Search up left, down and right methods.
    /**
     *  Searches the list of {@link GridItem} to find all the positions directly above the
     *  startPosition that are also the same colour as the GridItem at the startPosition
     * @param gridItemList      A list of Grid Items
     * @param startPosition     The start position
     * @return                  A list of positions represented as Integers.
     */
    private static List<Integer> searchUpwards(List<GridItem> gridItemList, int startPosition){
        List<Integer> connectedItemsByColor = new ArrayList<>();
        int height = (int)Math.sqrt(gridItemList.size());
        int colorResID = gridItemList.get(startPosition).getColorResID();
        int position = startPosition;
        while((position > -1) && (gridItemList.get(position).getColorResID() == colorResID)){
            connectedItemsByColor.add(position);
            position-= height;
        }
        return connectedItemsByColor;
    }

    /**
     *  Searches the list of {@link GridItem} to find all the positions directly below the
     *  startPosition that are also the same colour as the GridItem at the startPosition
     * @param gridItemList      A list of Grid Items
     * @param startPosition     The start position
     * @return                  A list of positions represented as Integers.
     */
    private static List<Integer> searchDownwards(List<GridItem> gridItemList, int startPosition){
        List<Integer> connectedItemsByColor = new ArrayList<>();
        int height = (int)Math.sqrt(gridItemList.size());
        int colorResID = gridItemList.get(startPosition).getColorResID();
        int position = startPosition;
        while((position < gridItemList.size()) && (gridItemList.get(position).getColorResID() == colorResID)){
            connectedItemsByColor.add(position);
            position+= height;
        }
        return connectedItemsByColor;
    }

    /**
     *  Searches the list of {@link GridItem} to find all the positions left of the startPosition
     *  that are also the same colour as the GridItem at the startPosition
     * @param gridItemList      A list of Grid Items
     * @param startPosition     The start position
     * @return                  A list of positions represented as Integers.
     */
    private static List<Integer> searchLeft(List<GridItem> gridItemList, int startPosition){
        List<Integer> connectedItemsByColor = new ArrayList<>();
        int width = (int)Math.sqrt(gridItemList.size());
        int colorResID = gridItemList.get(startPosition).getColorResID();
        int position = startPosition;
        if((position % width) == ((width - 1) % width)){
            connectedItemsByColor.add(position);
            position--;
        }
        while(((position % width) != ((width - 1) % width)) && position > -1 && (gridItemList.get(position).getColorResID() == colorResID)){
            connectedItemsByColor.add(position);
            position-= 1;
        }
        return connectedItemsByColor;
    }

    /**
     *  Searches the list of {@link GridItem} to find all the positions right of the startPosition
     *  that are also the same colour as the GridItem at the startPosition
     * @param gridItemList      A list of Grid Items
     * @param startPosition     The start position
     * @return                  A list of positions represented as Integers.
     */
    private static List<Integer> searchRight(List<GridItem> gridItemList, int startPosition){
        List<Integer> connectedItemsByColor = new ArrayList<>();
        int width = (int)Math.sqrt(gridItemList.size());
        int colorResID = gridItemList.get(startPosition).getColorResID();
        int position = startPosition;
        if((position % width) == 0){
            position++;
        }
        while((position % width != 0) && position < gridItemList.size() && (gridItemList.get(position).getColorResID() == colorResID)){
            connectedItemsByColor.add(position);
            position+= 1;
        }
        return connectedItemsByColor;
    }
    //endregion
}
