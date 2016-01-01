package com.darknessinc.duelmasters.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.darknessinc.duelmasters.views.GameCardView;


public class GameCard implements Parcelable {
	private int gameId;
	private Card dbCard;
	private int zoneId=-1;
	private boolean tapped = false;
	private boolean indicated = false;
	
	public GameCardView getGameCardView(Context c, int reqWidth, int reqHeight){
		GameCardView cv = new GameCardView(c, this);
		Bitmap bm = dbCard.getCardBitmap(reqWidth, reqHeight);
		Log.d("GameCard","getGameCardView width: "+bm.getWidth()+"height: "+bm.getHeight());
		cv.setImageBitmap(bm);
		return cv;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public GameCard(int gameId, Card c, int zoneId){
		this.gameId=gameId;
		this.dbCard = c;
		this.zoneId = zoneId;
	}
	public GameCard(int gameId, Card card){
		this.gameId = gameId;
		this.dbCard = card;
		this.zoneId=-1;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public Card getDbCard() {
		return dbCard;
	}
	public void setDbCard(Card dbCard) {
		this.dbCard = dbCard;
	}
    public void indicate(){
    	indicated=!indicated;    	
    }
    public boolean isIndicated(){
    	return indicated;
    }
    public void tap(){
    	tapped=!tapped;
    }
    public boolean isTapped(){
    	return tapped;
    }
    protected GameCard(Parcel in) {
        gameId = in.readInt();
        dbCard = (Card) in.readValue(Card.class.getClassLoader());
        zoneId = in.readInt();
        tapped = in.readByte() != 0x00;
        indicated = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameId);
        dest.writeValue(dbCard);
        dest.writeInt(zoneId);
        dest.writeByte((byte) (tapped ? 0x01 : 0x00));
        dest.writeByte((byte) (indicated ? 0x01 : 0x00));
    }

    public static final Parcelable.Creator<GameCard> CREATOR = new Parcelable.Creator<GameCard>() {
        @Override
        public GameCard createFromParcel(Parcel in) {
            return new GameCard(in);
        }

        @Override
        public GameCard[] newArray(int size) {
            return new GameCard[size];
        }
    };
    @Override
    public String toString(){
    	return dbCard.getName();
    }
}