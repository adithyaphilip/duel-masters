package com.darknessinc.duelmasters.cards;

import java.io.File;

import com.darknessinc.duelmasters.imageutils.ImageModder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Abraham
 *         used to represent a card from a database. Does not require gameId
 */
public class Card implements Parcelable {
    public final static String TYPE_CREATURE = "Creature";
    public final static String TYPE_SPELL = "Spell";
    private int dbId;
    private String name;
    private String imagePath;
    private String type;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Card(int dbId, String name, String imagePath, String type, String text) {
        this.dbId = dbId;
        this.name = name;
        this.imagePath = imagePath;
        this.type = type;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * either reqWidth or reqHeight may be zero, but not both. In case one of them is zero, it is calculated based on aspect ratio of actual image
     *
     * @param reqWidth
     * @param reqHeight
     * @return Bitmap of card image
     */
    public Bitmap getCardBitmap(int reqWidth, int reqHeight) {
        Uri uri = Uri.fromFile(new File(imagePath));
        return ImageModder.getMiniBitmap(reqWidth, reqHeight, uri);
    }

    protected Card(Parcel in) {
        dbId = in.readInt();
        name = in.readString();
        imagePath = in.readString();
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
        dest.writeString(imagePath);
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