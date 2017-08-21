package com.example.android.fillthegrid.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Reggie on 15/08/2017.
 * Custom contract class to store info about all the tables in the DB
 */

public class FillTheGridContract {

    // The content authority and the base content URI which will be used to generate all URIs in
    // this contract class.
    public static final String CONTENT_AUTHORITY = "com.example.android.fillthegrid";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Empty private constructor. Ensures class is not going to be initialised.
    private FillTheGridContract() {
    }

    public static final class GameStageEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "GameStage";

        // Table path for URI
        public static final String PATH = TABLE_NAME.toLowerCase();

        // Content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of game stages.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single game stage.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        // Primary key
        public static final String PK_GAME_STAGE_ID = BaseColumns._ID;

        // Other column names
        public static final String FK_DIFFICULTY_LEVEL_ID = "StageDifficultyLevelID";
        public static final String SIZE = "StageSize";
        public static final String SCORE = "StageScore";
        public static final String TARGET_SCORE = "StageTargetScore";
    }

    public static final class DifficultyLevel implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "DifficultyLevel";

        // Table path for URI
        public static final String PATH = TABLE_NAME.toLowerCase();

        // Content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of difficulty levels.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single level of difficulty.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        // Primary key
        public static final String PK_DIFFICULTY_LEVEL_ID = BaseColumns._ID;

        // Other column names
        public static final String DIFFICULTY_LEVEL_NAME = "DifficultyLevelName";
    }
}