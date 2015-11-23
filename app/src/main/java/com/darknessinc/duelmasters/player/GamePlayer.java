package com.darknessinc.duelmasters.player;

import java.util.ArrayList;

import android.util.Log;

import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.layout.ZoneContainer;
import com.darknessinc.duelmasters.zone.Zone;

public class GamePlayer extends Player{
	ZoneContainer mHandZone;
	ZoneContainer mManaZone;
	ZoneContainer mDeckZone;
	ZoneContainer mGraveZone;
	ZoneContainer mShieldZone;
	ZoneContainer mBattleZone;
	public GamePlayer(int id, String name, ArrayList<GameCard> mGameCards,
			ZoneContainer mHandZone, ZoneContainer mManaZone,
			ZoneContainer mDeckZone, ZoneContainer mGraveZone,
			ZoneContainer mShieldZone, ZoneContainer mBattleZone) {
		super(id, name, mGameCards);
		this.mHandZone = mHandZone;
		this.mManaZone = mManaZone;
		this.mDeckZone = mDeckZone;
		this.mGraveZone = mGraveZone;
		this.mShieldZone = mShieldZone;
		this.mBattleZone = mBattleZone;
	}
	public GamePlayer(Player p,	ZoneContainer mHandZone, ZoneContainer mManaZone,
			ZoneContainer mDeckZone, ZoneContainer mGraveZone,
			ZoneContainer mShieldZone, ZoneContainer mBattleZone) {
		super(p.getId(), p.getName(), p.getGameCards());
		this.mHandZone = mHandZone;
		this.mManaZone = mManaZone;
		this.mDeckZone = mDeckZone;
		this.mGraveZone = mGraveZone;
		this.mShieldZone = mShieldZone;
		this.mBattleZone = mBattleZone;
	}
	public ZoneContainer getZoneContainer(int zoneId){
		switch(zoneId){
		case Zone.ZONE_BATTLE:
			return mBattleZone;
		case Zone.ZONE_DECK:
			return mDeckZone;
		case Zone.ZONE_GRAVEYARD:
			return mGraveZone;
		case Zone.ZONE_HAND:
			return mHandZone;
		case Zone.ZONE_MANA:
			return mManaZone;
		case Zone.ZONE_SHIELD:
			return mShieldZone;
		default:
			Log.d("GamePlayer","getZoneContainer invalid zoneId passed: "+zoneId);
			return null;
		}
	}
}
