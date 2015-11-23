package com.darknessinc.duelmasters.player;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.darknessinc.duelmasters.cards.GameCard;
/**
 * 
 * @author USER
 * TODO remove setId and setName because we don't want them to be changed after initialisation
 */
public class Player implements Parcelable {
	private int id;
	private String name;
	private ArrayList<GameCard> mGameCards;//all of players cards
	public Player(int id, String name, ArrayList<GameCard> mGameCards) {
		super();
		this.id = id;
		this.name = name;
		this.mGameCards = mGameCards;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<GameCard> getGameCards() {
		return mGameCards;
	}
	public void setGameCards(ArrayList<GameCard> mGameCards) {
		this.mGameCards = mGameCards;
	}
	

    protected Player(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            mGameCards = new ArrayList<GameCard>();
            in.readList(mGameCards, GameCard.class.getClassLoader());
        } else {
            mGameCards = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (mGameCards == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mGameCards);
        }
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}