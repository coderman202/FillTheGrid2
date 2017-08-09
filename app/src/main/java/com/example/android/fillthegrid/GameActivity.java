package com.example.android.fillthegrid;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.fillthegrid.model.Game;
import com.example.android.fillthegrid.model.GridItem;
import com.example.android.fillthegrid.utils.GameGridUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.game_grid_layout)
    GridLayout gameGrid;

    Game game;

    List<GridItem> gridItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        game = new Game(Game.LEVEL_BEGINNER, Game.GAME_SIZE_LARGE);

        gridItemList = game.getGridItemList();

        gameGrid.setColumnCount((int)Math.sqrt(game.getGridSize()));

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
                    game.setNewColors(GameGridUtils.getSquaresToBeChanged(gridItemList, position), newColorResID);
                    updateGameGridWithNewColors();
                }
            });
        }
    }

    public void updateGameGridWithNewColors(){
        gridItemList = game.getGridItemList();
        for (int i = 0; i < gridItemList.size(); i++) {
            GradientDrawable gridSquare = (GradientDrawable) getDrawable(R.drawable.grid_square);
            gridSquare.setColor(ContextCompat.getColor(this, gridItemList.get(i).getColorResID()));
            gameGrid.getChildAt(i).setBackground(gridSquare);
        }
        game.updateGridItemList();

        if(game.isComplete()){
            Toast.makeText(this, "Woohoo!", Toast.LENGTH_LONG);
        }
    }
}
