package com.example.android.fillthegrid.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.fillthegrid.R;
import com.example.android.fillthegrid.model.Game;
import com.example.android.fillthegrid.model.GridItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reggie on 03/08/2017.
 * Custom adapter to handle grid.
 */

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.ViewHolder>{

    private Context context;
    private List<GridItem> gridItemList;
    private Game game;
    private RecyclerView parent;

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.grid_item) ImageView gridItemView;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        public ImageView getGridItemView(){
            return gridItemView;
        }
    }

    public GridItemAdapter(Context context, Game game){
        this.context = context;
        this.gridItemList = game.getGridItemList();
        this.game = game;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView parent){
        this.parent = parent;
    }

    @Override
    public int getItemCount(){
        return gridItemList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GridItem item = gridItemList.get(position);
        final int itemColorResID = item.getColorResID();

        GradientDrawable gridSquare = (GradientDrawable) holder.gridItemView.getBackground();
        gridSquare.setColor(ContextCompat.getColor(context, itemColorResID));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }
}
