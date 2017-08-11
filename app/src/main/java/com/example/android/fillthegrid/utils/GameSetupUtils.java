package com.example.android.fillthegrid.utils;

import com.example.android.fillthegrid.R;

/**
 * Created by Reggie on 11/08/2017.
 * Custom utility class with methods for setting up the game
 */

public class GameSetupUtils {

    private static Integer[] level_array = new Integer[]{
            R.drawable.level_beginner, R.drawable.level_novice, R.drawable.level_student,
            R.drawable.level_intermediate, R.drawable.level_advanced, R.drawable.level_expert,
            R.drawable.level_genius, R.drawable.level_wizard, R.drawable.level_god
    };

    // Empty constructor to avoid initialisation
    private GameSetupUtils() {
    }

    public static int getLevelDrawableResID(int position) {
        return level_array[position];
    }
}
