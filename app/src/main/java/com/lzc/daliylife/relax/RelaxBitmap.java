package com.lzc.daliylife.relax;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lzc on 2017/9/13.
 *
 */

public class RelaxBitmap implements Parcelable{

    private Bitmap bitmap;
    private float width;
    private float height;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public RelaxBitmap(Bitmap bitmap, float width, float height) {
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    protected RelaxBitmap(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        width = in.readFloat();
        height = in.readFloat();
    }

    public static final Creator<RelaxBitmap> CREATOR = new Creator<RelaxBitmap>() {
        @Override
        public RelaxBitmap createFromParcel(Parcel in) {
            return new RelaxBitmap(in);
        }

        @Override
        public RelaxBitmap[] newArray(int size) {
            return new RelaxBitmap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
        dest.writeFloat(width);
        dest.writeFloat(height);
    }
}
