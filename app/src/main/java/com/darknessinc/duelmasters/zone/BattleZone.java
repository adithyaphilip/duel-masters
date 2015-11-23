package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;

import com.darknessinc.duelmasters.cards.GameCard;

public class BattleZone extends Zone {

	public BattleZone(ArrayList<GameCard> gcs) {
		super(gcs);
	}

	@Override
	public int getZoneId() {
		return Zone.ZONE_BATTLE;
	}

	@Override
	public String[] getOwnCardOptions(GameCard gc) {
		return new String[]{ACTION_TAP,ACTION_MOV_GRAVE,
				ACTION_MOV_HAND,ACTION_MOV_MANA,ACTION_MOV_DECK,ACTION_MOV_SHIELD};
	}

	@Override
	public String[] getOpponentCardOptions(GameCard gc) {
		return new String[]{ACTION_INDICATE};
	}
}
