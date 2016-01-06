package com.darknessinc.duelmasters.cards;

import com.darknessinc.duelmasters.imageutils.ImageModder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Abraham
 *         used to represent a card from a database. Does not require gameId
 */
public class Card implements Parcelable {
    private int dbId;
    private String name;
    private String imageResource;
    private String type;
    private String text;

    public String getText() {
        return text;
    }

    public Card(int dbId, String name, String imageResource, String type, String text) {
        this.dbId = dbId;
        this.name = name;
        this.imageResource = imageResource;
        this.type = type;
        this.text = text;
    }

    @Override
    protected Card clone() {
        return new Card(dbId, name, imageResource, type, text);
    }

    public int getDbId() {
        return dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    /**
     * either reqWidth or reqHeight may be zero, but not both. In case one of them is zero, it is calculated based on aspect ratio of actual image
     *
     * @return Bitmap of card image
     */
    public Bitmap getCardBitmap(int reqWidth, int reqHeight, Context context) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(imageResource, "drawable", context.getPackageName());
        return ImageModder.getMiniBitmap(reqWidth, reqHeight, resources, resourceId);
        // Below code was for when we copied all cards to sdcard and used
        // Uri uri = Uri.fromFile(new File(imageResource));
        // return ImageModder.getMiniBitmap(reqWidth, reqHeight, uri);
    }

    protected Card(Parcel in) {
        dbId = in.readInt();
        name = in.readString();
        imageResource = in.readString();
        type = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dbId);
        dest.writeString(name);
        dest.writeString(imageResource);
        dest.writeString(type);
        dest.writeString(text);
    }

    public String toString() {
        return this.name;
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}