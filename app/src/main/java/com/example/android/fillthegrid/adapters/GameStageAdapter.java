package com.example.android.fillthegrid.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.fillthegrid.R;
import com.example.android.fillthegrid.data.FillTheGridContract;
import com.example.android.fillthegrid.utils.GlobalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reggie on 18/08/2017.
 * Custom adapter to display all the game stages for each size and each difficulty level
 */

public class GameStageAdapter extends RecyclerView.Adapter<GameStageAdapter.ViewHolder> {

    private Cursor cursor;

    private int pageNumber;

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.game_stage)
        TextView gameStageView;
        @BindView(R.id.game_stage_item_border)
        LinearLayout borderView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GameStageAdapter(Context context, int pageNumber) {
        this.context = context;
        this.pageNumber = pageNumber;
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        cursor.moveToFirst();
        Log.e("Count", cursor.getCount() + "");
        return cursor.getCount();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_stage_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        Integer[] colorArray = GlobalUtils.getColorArray(context);
        GradientDrawable border = (GradientDrawable) context.getDrawable(R.drawable.level_item_border);
        border.setStroke(5, colorArray[pageNumber - 1]);
        holder.borderView.setBackground(border);

        int score = cursor.getInt(cursor.getColumnIndex(FillTheGridContract.GameStageEntry.SCORE));
        if (score != 0) {
            holder.gameStageView.setBackgroundResource(R.drawable.stage_complete);
            holder.gameStageView.setText(score + "");
        } else {
            holder.gameStageView.setBackgroundResource(R.drawable.stage_incomplete);
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
