package com.example.lzc.daliylife.entity.gankentity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lzc on 2017/3/15.
 */

public class AndroidEntity implements Parcelable {
    String error;
    ArrayList<Result> results;


    @Override
    public String toString() {
        return "AndroidEntity{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.error);
        dest.writeTypedList(this.results);
    }

    public AndroidEntity() {
    }

    protected AndroidEntity(Parcel in) {
        this.error = in.readString();
        this.results = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Parcelable.Creator<AndroidEntity> CREATOR = new Parcelable.Creator<AndroidEntity>() {
        @Override
        public AndroidEntity createFromParcel(Parcel source) {
            return new AndroidEntity(source);
        }

        @Override
        public AndroidEntity[] newArray(int size) {
            return new AndroidEntity[size];
        }
    };
}
