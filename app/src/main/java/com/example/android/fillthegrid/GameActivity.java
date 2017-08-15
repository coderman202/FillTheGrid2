package com.example.android.fillthegrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fillthegrid.model.Game;
import com.example.android.fillthegrid.model.GridItem;
import com.example.android.fillthegrid.utils.GameGridUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = GameActivity.class.getSimpleName();

    public static final String GAME_STATE = "Game State";

    public int prevColorResID;
    public static final String PREVIOUS_COLOR_RES_ID = "Previous Color Res ID";

    @BindView(R.id.game_grid_layout)
    GridLayout gameGrid;

    Game game;

    List<GridItem> gridItemList;

    public static final String CHOSEN_LEVEL = "Chosen Level";
    int levelChosen;

    @BindView(R.id.move_count)
    TextView moveCountView;
    public static final String MOVE_COUNT_STATE = "Move Count";
    public int moveCount = 0;

    @BindView(R.id.replay_level)
    FloatingActionButton replayButton;

    SharedPreferences highScorePrefs;
    public static final String SHARED_PREFS_KEY = "Prefs Key";
    public static final String HIGH_SCORE_KEY = "High Score";
    public int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        levelChosen = getIntent().getIntExtra(CHOSEN_LEVEL, 0);

        if (savedInstanceState == null) {
            game = new Game(levelChosen, Game.GAME_SIZE_XXL);
        } else {
            game = savedInstanceState.getParcelable(GAME_STATE);
            prevColorResID = savedInstanceState.getInt(PREVIOUS_COLOR_RES_ID);
            moveCount = savedInstanceState.getInt(MOVE_COUNT_STATE);
        }

        /*highScorePrefs = this.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        highScore = highScorePrefs.getInt(HIGH_SCORE_KEY, 0);*/

        initGameGrid();


    }

    /**
     * Set up the grid
     */
    private void initGameGrid() {
        gridItemList = game.getGridItemList();

        gameGrid.setColumnCount((int)Math.sqrt(game.getGridSize()));

        moveCountView.setText(getString(R.string.move_count, moveCount));

        replayButton.setOnClickListener(this);

        for (int i = 0; i < gridItemList.size(); i++) {
            final ImageView gridItemView = new ImageView(this);

            GradientDrawable gridSquare = (GradientDrawable) getDrawable(R.drawable.grid_square);
            gridSquare.setColor(ContextCompat.getColor(this, gridItemList.get(i).getColorResID()));

            gridItemView.setBackground(gridSquare);

            gridItemView.setId(i);

            gameGrid.addView(gridItemView);

            gridItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = view.getId();
                    GridItem item = gridItemList.get(position);
                    int newColorResID = item.getColorResID();
                    if (newColorResID != prevColorResID) {
                        prevColorResID = newColorResID;
                        game.setNewColors(GameGridUtils.getSquaresToBeChanged(gridItemList, position), newColorResID);
                        updateGameGridWithNewColors();
                        moveCount++;
                        moveCountView.setText(getString(R.string.move_count, moveCount));
                    }
                }
            });
        }
        // Check if the grid is full, fill the background in too.
        if (game.isComplete()) {
            gameGrid.setBackgroundResource(gridItemList.get(1).getColorResID());
            /*if(moveCount < highScore || highScore == 0){
                highScorePrefs.edit().putInt(HIGH_SCORE_KEY, moveCount);
            }*/
        } else {
            gameGrid.setBackground(null);
        }
    }


    /**
     * Updates the grid with the latest colours.
     */
    public void updateGameGridWithNewColors(){
        gridItemList = game.getGridItemList();
        for (int i = 0; i < gridItemList.size(); i++) {
            GradientDrawable gridSquare = (GradientDrawable) getDrawable(R.drawable.grid_square);
            gridSquare.setColor(ContextCompat.getColor(this, gridItemList.get(i).getColorResID()));
            gameGrid.getChildAt(i).setBackground(gridSquare);
        }
        game.updateGridItemList();

        // Check if the grid is all the same colour.
        if(game.isComplete()){
            gameGrid.setBackgroundResource(gridItemList.get(1).getColorResID());
            Toast.makeText(this, "Congratulations! You filled the grid!", Toast.LENGTH_LONG).show();
            replayButton.setVisibility(View.VISIBLE);
        } else {
            gameGrid.setBackground(null);
            replayButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.replay_level:
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(CHOSEN_LEVEL, levelChosen);
                startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(GAME_STATE, game);
        outState.putInt(PREVIOUS_COLOR_RES_ID, prevColorResID);
        outState.putInt(MOVE_COUNT_STATE, moveCount);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        game = inState.getParcelable(GAME_STATE);
        prevColorResID = inState.getInt(PREVIOUS_COLOR_RES_ID);
        moveCount = inState.getInt(MOVE_COUNT_STATE);
    }

}
