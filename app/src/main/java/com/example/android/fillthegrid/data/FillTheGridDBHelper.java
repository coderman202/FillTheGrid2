package com.example.android.fillthegrid.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.fillthegrid.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Reggie on 15/08/2017.
 * Custom DB Helper class
 */

public class FillTheGridDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = FillTheGridDBHelper.class.getSimpleName();
    private static final String DB_NAME = "FillTheGridDB.db";
    private static final int DB_VERSION = 7;

    private Context context;

    // File names of the scripts stored in assets directory
    private static final String CREATE_TABLES = "create_tables.sql";
    private static final String DROP_TABLES = "drop_tables.sql";
    private static final String INSERT_DIFFICULTY_LEVELS = "insert_difficulty_levels.sql";
    private static final String INSERT_GAME_STAGES = "insert_game_stages.sql";

    // Table names
    private static final String GAME_STAGE_TABLE = FillTheGridContract.GameStageEntry.TABLE_NAME;
    private static final String DIFFICULTY_LEVEL_TABLE = FillTheGridContract.DifficultyLevel.TABLE_NAME;

    // Game stage table columns.
    private static final String GAME_STAGE_ID = FillTheGridContract.GameStageEntry.PK_GAME_STAGE_ID;
    private static final String GAME_STAGE_DIFFICULTY_LEVEL_ID = FillTheGridContract.GameStageEntry.FK_DIFFICULTY_LEVEL_ID;
    private static final String GAME_STAGE_SIZE = FillTheGridContract.GameStageEntry.SIZE;
    private static final String GAME_STAGE_SCORE = FillTheGridContract.GameStageEntry.SCORE;
    private static final String GAME_STAGE_TARGET_SCORE = FillTheGridContract.GameStageEntry.TARGET_SCORE;

    // Difficulty Level table columns
    private static final String DIFFICULTY_LEVEL_ID = FillTheGridContract.DifficultyLevel.PK_DIFFICULTY_LEVEL_ID;
    private static final String DIFFICULTY_LEVEL_NAME = FillTheGridContract.DifficultyLevel.DIFFICULTY_LEVEL_NAME;

    public FillTheGridDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        if (isEmptyDB(db)) {
            insertData(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        runSQLScript(context, db, DROP_TABLES);
        onCreate(db);
    }

    public Context getContext() {
        return context;
    }

    /**
     * A method to execute sql scripts. This can be called to run the create_tables.sql script in
     * the onCreate method, along with the insert_data.sql file. n the onUpgrade method, the
     * drop_tables.sql file can be called along with the create and insert scripts. My resource for
     * this method is found below.
     *
     * @param context   The context
     * @param db        The db that the script is run on
     * @param sqlScript The sql script
     * @see <a href="http://www.drdobbs.com/database/using-sqlite-on-android/232900584">Here</a>
     */
    private void runSQLScript(Context context, SQLiteDatabase db, String sqlScript) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream;

        try {
            inputStream = assetManager.open(sqlScript);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String[] script = outputStream.toString().split(";");
            for (String sqlStatement : script) {
                sqlStatement = sqlStatement.trim();
                if (sqlStatement.length() > 0) {
                    db.execSQL(sqlStatement + ";");
                }
            }
        } catch (IOException e) {
            Log.e(e.toString(), sqlScript + "failed to load");
        } catch (SQLException e) {
            Log.e(e.toString(), sqlScript + "failed to execute");
        }
    }


    /**
     * A method to create tables in the db
     *
     * @param db the SQLite DB
     */
    public void createTables(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + DIFFICULTY_LEVEL_TABLE + "(" +
                DIFFICULTY_LEVEL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                DIFFICULTY_LEVEL_NAME + " TEXT NOT NULL);";
        db.execSQL(query);
        Log.e(LOG_TAG, query);
        query = "CREATE TABLE IF NOT EXISTS " + GAME_STAGE_TABLE + "(" +
                GAME_STAGE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                GAME_STAGE_SIZE + " INTEGER NOT NULL, " +
                GAME_STAGE_SCORE + " INTEGER, " +
                GAME_STAGE_TARGET_SCORE + " INTEGER NOT NULL, " +
                GAME_STAGE_DIFFICULTY_LEVEL_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + GAME_STAGE_DIFFICULTY_LEVEL_ID + ") REFERENCES " + DIFFICULTY_LEVEL_TABLE + "(" + DIFFICULTY_LEVEL_ID + ") ON DELETE SET NULL);";
        Log.e(LOG_TAG, query);
        db.execSQL(query);

    }

    /**
     * A method to check if the db is empty.
     *
     * @param db The db
     * @return True if empty, false if not.
     */
    public boolean isEmptyDB(SQLiteDatabase db) {
        String query = "SELECT * FROM " + GAME_STAGE_TABLE + ", " + DIFFICULTY_LEVEL_TABLE + ";";
        Log.e(LOG_TAG, query);
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
            int count = c.getCount();
            Log.e(LOG_TAG, count + "");
            c.close();
            return count == 0;
        }
        c.close();
        return true;
    }

    /**
     * A method to insert all the difficulty levels into the db and also then all the game stages
     * for each size and difficulty level.
     *
     * @param db The database object
     */
    private void insertData(SQLiteDatabase db) {
        String[] levelsArray = context.getResources().getStringArray(R.array.levels_array);

        for (String level : levelsArray) {
            ContentValues levelValues = new ContentValues();
            levelValues.put(FillTheGridContract.DifficultyLevel.DIFFICULTY_LEVEL_NAME, level);

            int difficultyLevelID = (int) db.insert(DIFFICULTY_LEVEL_TABLE, null, levelValues);
            insertGameStages(db, difficultyLevelID);
        }
    }

    /**
     * A method to insert all the game stages. Insert 20 game stages per each size, per each
     * difficulty level.
     *
     * @param difficultyLevelID The int id of the foreign key difficultyLevelID
     */
    private void insertGameStages(SQLiteDatabase db, int difficultyLevelID) {
        int[] sizesArray = context.getResources().getIntArray(R.array.grid_dimensions_array);

        for (int width : sizesArray) {
            int size = width * width;
            Log.e(LOG_TAG, size + "");

            for (int i = 0; i < 20; i++) {
                ContentValues values = new ContentValues();
                values.put(FillTheGridContract.GameStageEntry.FK_DIFFICULTY_LEVEL_ID, difficultyLevelID);
                values.put(FillTheGridContract.GameStageEntry.SIZE, size);
                values.put(FillTheGridContract.GameStageEntry.TARGET_SCORE, width - 1);

                db.insert(GAME_STAGE_TABLE, null, values);
            }
        }
    }


    public void testDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + GAME_STAGE_TABLE + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                int difflevelid = c.getInt(c.getColumnIndex(GAME_STAGE_DIFFICULTY_LEVEL_ID));
                int pk = c.getInt(c.getColumnIndex(GAME_STAGE_ID));
                int size = c.getInt(c.getColumnIndex(GAME_STAGE_SIZE));
                int score = c.getInt(c.getColumnIndex(GAME_STAGE_SCORE));
                int target = c.getInt(c.getColumnIndex(GAME_STAGE_TARGET_SCORE));

                Log.e("test", "PK: " + pk + "/DiffID: " + difflevelid + "/Size: " + size + "/Score: " + score + "/Target: " + target);
                c.moveToNext();
            }
            c.close();
        }
        c.close();
    }
}