package com.example.android.fillthegrid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.fillthegrid.R;
import com.example.android.fillthegrid.utils.GameSetupUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reggie on 09/08/2017.
 * Custom spinner adapter for selecting the level.
 */

public class LevelChooserSpinnerAdapter extends ArrayAdapter<String> {

    static class ViewHolder {
        @BindView(R.id.level_name)
        TextView levelView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public LevelChooserSpinnerAdapter(Context context, List<String> levelList) {
        super(context, R.layout.level_chooser_item, levelList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        View spinnerItemView = convertView;

        if (spinnerItemView == null) {
            spinnerItemView = LayoutInflater.from(getContext()).inflate(R.layout.level_chooser_item, parent, false);
            holder = new ViewHolder(spinnerItemView);
            spinnerItemView.setTag(holder);
        } else {
            holder = (ViewHolder) spinnerItemView.getTag();
        }

        String currentLevel = getItem(position);

        holder.levelView.setText(currentLevel);
        holder.levelView.setCompoundDrawablesWithIntrinsicBounds(GameSetupUtils.getLevelDrawableResID(position), 0, 0, 0);

        return spinnerItemView;
    }

    /*@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position));
        return label;
    }*/
}
