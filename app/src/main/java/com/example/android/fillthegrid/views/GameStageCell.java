package com.example.android.fillthegrid.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.fillthegrid.R;
import com.example.android.fillthegrid.utils.GlobalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reggie on 18/08/2017.
 * Custom view to represent each game stage item.
 */

public class GameStageCell extends LinearLayout {

    @BindView(R.id.game_stage)
    TextView gameStageView;
    @BindView(R.id.game_stage_item_border)
    LinearLayout borderView;

    private Context context;

    public GameStageCell(Context context) {
        super(context);
        initialise(context);
        this.context = context;
    }

    public void initialise(Context context) {
        LayoutInflater.from(context).inflate(R.layout.game_stage_item, this);
        ButterKnife.bind(this);
    }

    public void setBackground(int backgroundResID) {
        gameStageView.setBackgroundResource(backgroundResID);
    }

    public void setBorderColor(int pageNumber) {
        Integer[] colorArray = GlobalUtils.getColorArray(context);
        GradientDrawable border = (GradientDrawable) context.getDrawable(R.drawable.level_item_border);
        border.setStroke(5, colorArray[pageNumber - 1]);
        borderView.setBackground(border);
    }

    public void setText(String text) {
        gameStageView.setText(text);
    }
}
