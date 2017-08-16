package com.example.android.fillthegrid.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.fillthegrid.R;

/**
 * Created by Reggie on 15/08/2017.
 * Custom provider class for interacting with the DB
 */

public class FillTheGridProvider extends ContentProvider {

    public static final String LOG_TAG = FillTheGridProvider.class.getSimpleName();

    // GAME STAGE TABLE CONSTANTS
    private static final int GAME_STAGE_TABLE = 100;
    private static final int GAME_STAGE_TABLE_ROW = 101;
    private static final String PATH_GAME_STAGE = FillTheGridContract.GameStageEntry.PATH;
    private static final String GAME_STAGE_TABLE_NAME = FillTheGridContract.GameStageEntry.TABLE_NAME;
    private static final String GAME_STAGE_TABLE_LIST_TYPE = FillTheGridContract.GameStageEntry.CONTENT_LIST_TYPE;
    private static final String GAME_STAGE_TABLE_ITEM_TYPE = FillTheGridContract.GameStageEntry.CONTENT_ITEM_TYPE;

    // Game Stage Columns
    private static final String GAME_STAGE_ID = FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID;
    private static final String GAME_STAGE_DIFFICULTY_LEVEL = FillTheGridContract.GameStageEntry.DIFFICULTY_LEVEL_ID;
    private static final String GAME_STAGE_TARGET_SCORE = FillTheGridContract.GameStageEntry.TARGET_SCORE;
    private static final String GAME_STAGE_SCORE = FillTheGridContract.GameStageEntry.SCORE;
    private static final String GAME_STAGE_SIZE = FillTheGridContract.GameStageEntry.SIZE;

    // DIFFICULTY LEVEL CONSTANTS
    private static final int DIFFICULTY_LEVEL_TABLE = 200;
    private static final int DIFFICULTY_LEVEL_TABLE_ROW = 201;
    private static final String PATH_DIFFICULTY_LEVEL = FillTheGridContract.DifficultyLevel.PATH;
    private static final String DIFFICULTY_LEVEL_TABLE_NAME = FillTheGridContract.DifficultyLevel.TABLE_NAME;
    private static final String DIFFICULTY_LEVEL_TABLE_LIST_TYPE = FillTheGridContract.DifficultyLevel.CONTENT_LIST_TYPE;
    private static final String DIFFICULTY_LEVEL_TABLE_ITEM_TYPE = FillTheGridContract.DifficultyLevel.CONTENT_ITEM_TYPE;

    // Game Stage Columns
    private static final String DIFFICULTY_LEVEL_ID = FillTheGridContract.DifficultyLevel.PK_DIFFICULTY_LEVEL_ID;
    private static final String DIFFICULTY_LEVEL_NAME = FillTheGridContract.DifficultyLevel.DIFFICULTY_LEVEL_NAME;

    // DB Helper object
    private FillTheGridDBHelper dbHelper;

    public static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // URIs for the Game Stage Table
        uriMatcher.addURI(FillTheGridContract.CONTENT_AUTHORITY, PATH_GAME_STAGE, GAME_STAGE_TABLE);
        uriMatcher.addURI(FillTheGridContract.CONTENT_AUTHORITY, PATH_GAME_STAGE + "/#", GAME_STAGE_TABLE_ROW);

        // URIs for the Difficulty Level Table
        uriMatcher.addURI(FillTheGridContract.CONTENT_AUTHORITY, PATH_DIFFICULTY_LEVEL, DIFFICULTY_LEVEL_TABLE);
        uriMatcher.addURI(FillTheGridContract.CONTENT_AUTHORITY, PATH_DIFFICULTY_LEVEL + "/#", DIFFICULTY_LEVEL_TABLE_ROW);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        dbHelper = new FillTheGridDBHelper(getContext());
        return true;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAME_STAGE_TABLE:
                return GAME_STAGE_TABLE_LIST_TYPE;
            case GAME_STAGE_TABLE_ROW:
                return GAME_STAGE_TABLE_ITEM_TYPE;
            case DIFFICULTY_LEVEL_TABLE:
                return DIFFICULTY_LEVEL_TABLE_LIST_TYPE;
            case DIFFICULTY_LEVEL_TABLE_ROW:
                return DIFFICULTY_LEVEL_TABLE_ITEM_TYPE;
            default:
                throw new IllegalStateException(dbHelper.getContext().getString(R.string.content_uri_unknown_exception, uri, match));
        }
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection,
     * selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // This cursor will hold query result.
        Cursor cursor;

        // Making sure the URI is valid
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAME_STAGE_TABLE:
                cursor = db.query(GAME_STAGE_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case GAME_STAGE_TABLE_ROW:
                selection = FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(GAME_STAGE_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DIFFICULTY_LEVEL_TABLE:
                cursor = db.query(DIFFICULTY_LEVEL_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DIFFICULTY_LEVEL_TABLE_ROW:
                selection = FillTheGridContract.DifficultyLevel.PK_DIFFICULTY_LEVEL_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DIFFICULTY_LEVEL_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_unsupported_insertion_exception, uri));
        }
        return cursor;
    }

    //region Data insertion methods

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Making sure the URI is valid
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAME_STAGE_TABLE:
                return insertGameStage(uri, values);
            case DIFFICULTY_LEVEL_TABLE:
                return insertDifficultyLevel(uri, values);
            default:
                throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_unsupported_insertion_exception, uri));
        }
    }

    /**
     * A method to ensure all the values for game stage to be inserted are correct and valid.
     *
     * @param uri    the uri
     * @param values the values to be inserted for each column
     * @return the uri including if all values are valid, otherwise null
     */
    public Uri insertGameStage(Uri uri, ContentValues values) {

        // Check the game stage score. It should be null at insertion. Only changed on update
        Integer score = values.getAsInteger(GAME_STAGE_SCORE);
        if (score != null) {
            throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_stage_score_exception));
        }

        // Check the game stage size. It should be a square number
        int size = values.getAsInteger(GAME_STAGE_SIZE);
        if (size != (int) Math.sqrt(size) * (int) Math.sqrt(size) || size == 0) {
            throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_stage_size_exception));
        }


        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = db.insert(GAME_STAGE_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, dbHelper.getContext().getString(R.string.content_uri_insert_failure_exception, uri));
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * A method to ensure all the values for difficulty level to be inserted are correct and valid.
     *
     * @param uri    the uri
     * @param values the values to be inserted for each column
     * @return the new URI with the ID (of the newly inserted row) appended at the end, otherwise null
     */
    public Uri insertDifficultyLevel(Uri uri, ContentValues values) {

        // Check the category name is not null
        String name = values.getAsString(DIFFICULTY_LEVEL_NAME);
        if (name == null) {
            throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_difficulty_level_name_exception));
        }

        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert the new difficulty level with the given values
        long id = db.insert(DIFFICULTY_LEVEL_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, dbHelper.getContext().getString(R.string.content_uri_insert_failure_exception, uri));
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }
    //endregion

    //region Update database methods

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAME_STAGE_TABLE:
                return updateGameStage(uri, contentValues, selection, selectionArgs);
            case GAME_STAGE_TABLE_ROW:

                selection = GAME_STAGE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateGameStage(uri, contentValues, selection, selectionArgs);
            case DIFFICULTY_LEVEL_TABLE:
                return updateDifficultyLevel(uri, contentValues, selection, selectionArgs);
            case DIFFICULTY_LEVEL_TABLE_ROW:

                selection = DIFFICULTY_LEVEL_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDifficultyLevel(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_unsupported_update_exception, uri));
        }
    }

    /**
     * A method which validates the update of a game stage or number of game stages.
     *
     * @param uri           the uri of the game stage entry
     * @param values        the content values
     * @param selection     selection
     * @param selectionArgs selection arguments
     * @return Returns the number of database rows affected by the update statement
     */
    private int updateGameStage(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        return database.update(GAME_STAGE_TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * A method which validates the update of a difficulty level or number of difficulty levels
     * in the db.
     *
     * @param uri           the uri of the difficulty level entry
     * @param values        the content values
     * @param selection     selection
     * @param selectionArgs selection arguments
     * @return Returns the number of database rows affected by the update statement
     */
    private int updateDifficultyLevel(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link DifficultyLevel#NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(DIFFICULTY_LEVEL_NAME)) {
            String name = values.getAsString(DIFFICULTY_LEVEL_NAME);
            if (name == null) {
                throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_difficulty_level_name_exception));
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        return database.update(DIFFICULTY_LEVEL_TABLE_NAME, values, selection, selectionArgs);
    }
    //endregion

    //region Delete method(s)

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAME_STAGE_TABLE:
                // Delete all rows that match the selection and selection args
                return db.delete(GAME_STAGE_TABLE_NAME, selection, selectionArgs);
            case GAME_STAGE_TABLE_ROW:
                // Delete a single row given by the ID in the URI
                selection = GAME_STAGE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(GAME_STAGE_TABLE_NAME, selection, selectionArgs);
            case DIFFICULTY_LEVEL_TABLE:
                // Delete all rows that match the selection and selection args
                return db.delete(DIFFICULTY_LEVEL_TABLE_NAME, selection, selectionArgs);
            case DIFFICULTY_LEVEL_TABLE_ROW:
                // Delete a single row given by the ID in the URI
                selection = DIFFICULTY_LEVEL_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(DIFFICULTY_LEVEL_TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(dbHelper.getContext().getString(R.string.content_uri_unsupported_deletion_exception, uri));
        }
    }
    //endregion

}
