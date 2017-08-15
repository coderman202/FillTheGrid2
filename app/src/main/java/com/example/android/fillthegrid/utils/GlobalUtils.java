package com.example.android.fillthegrid.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.android.fillthegrid.R;

/**
 * Created by Reggie on 14/08/2017.
 * Custom util class with methods to help interface display attributes
 */

public class GlobalUtils {

    // Empty private constructor to stop initialisation
    private GlobalUtils() {
    }


    /**
     * Method to convert a dip attribute to px for the purposes of displaying things uniformly on
     * multiple screen resolutions
     *
     * @param context   The context
     * @param valueDips The value in dips
     * @return The value in pix
     */
    public static int convertToPixels(Context context, float valueDips) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        return (int) (valueDips * SCALE + 0.5f); // 0.5f for rounding
    }

    public static Integer[] getColorArray(Context context) {
        return new Integer[]{
                ContextCompat.getColor(context, R.color.grid_blue),
                ContextCompat.getColor(context, R.color.grid_red),
                ContextCompat.getColor(context, R.color.grid_green),
                ContextCompat.getColor(context, R.color.grid_orange),
                ContextCompat.getColor(context, R.color.grid_yellow),
                ContextCompat.getColor(context, R.color.grid_teal)};
    }
}
