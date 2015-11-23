package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;

import com.darknessinc.duelmasters.cards.GameCard;

public class ManaZone extends Zone{

	public ManaZone(ArrayList<GameCard> gcs) {
		super(gcs);
	}

	@Override
	public int getZoneId() {
		return ZONE_MANA;
	}
	@Override
	public String[] getOwnCardOptions(GameCard gc) {
		return new String[]{ACTION_MOV_BATTLE,ACTION_MOV_GRAVE,ACTION_MOV_HAND,ACTION_MOV_DECK,ACTION_MOV_SHIELD};
	}

	@Override
	public String[] getOpponentCardOptions(GameCard gc) {
		return new String[]{ACTION_INDICATE};
	}
}
