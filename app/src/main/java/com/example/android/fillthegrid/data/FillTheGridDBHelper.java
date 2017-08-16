package com.example.android.fillthegrid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Reggie on 15/08/2017.
 * Custom DB Helper class
 */

public class FillTheGridDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = FillTheGridDBHelper.class.getSimpleName();
    private static final String DB_NAME = "FillTheGridDB.db";
    private static final int DB_VERSION = 1;

    private Context context;

    public FillTheGridDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Context getContext() {
        return context;
    }

}
