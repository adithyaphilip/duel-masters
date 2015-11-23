package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;

import com.darknessinc.duelmasters.cards.GameCard;

public class GraveZone extends Zone{

	public GraveZone(ArrayList<GameCard> gcs) {
		super(gcs);
	}

	@Override
	public int getZoneId() {
		return ZONE_GRAVEYARD;
	}
	@Override
	public String[] getOwnCardOptions(GameCard gc) {
		return new String[]{Zone.ACTION_MOV_HAND,Zone.ACTION_MOV_MANA,Zone.ACTION_MOV_DECK,Zone.ACTION_MOV_BATTLE,Zone.ACTION_MOV_SHIELD,Zone.ACTION_SEARCH};
	}	
	public GameCard getTopCard(){
		ArrayList<GameCard> gcs = getCards();
		if(gcs.size()==0)
			return null;
		return gcs.get(gcs.size()-1);
	}

	@Override
	public String[] getOpponentCardOptions(GameCard gc) {
		return new String[]{ACTION_INDICATE,ACTION_SEARCH};//TODO add option to search opponents graveyard
	}
}
