package com.example.android.fillthegrid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reggie on 03/08/2017.
 * A custom class to represent each grid item.
 */

public class GridItem implements Parcelable {

    private int colorResID;

    List<Integer> connectedSquares;

    public GridItem(int colorResID) {
        this.colorResID = colorResID;
        this.connectedSquares = new ArrayList<>();
    }

    public int getColorResID() {
        return colorResID;
    }

    public void setColorResID(int colorResID) {
        this.colorResID = colorResID;
    }

    public List<Integer> getConnectedSquares() {
        return connectedSquares;
    }

    public void setConnectedSquares(List<Integer> connectedSquares) {
        this.connectedSquares = connectedSquares;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.colorResID);
        dest.writeList(this.connectedSquares);
    }

    protected GridItem(Parcel in) {
        this.colorResID = in.readInt();
        this.connectedSquares = new ArrayList<Integer>();
        in.readList(this.connectedSquares, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<GridItem> CREATOR = new Parcelable.Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel source) {
            return new GridItem(source);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };
}
