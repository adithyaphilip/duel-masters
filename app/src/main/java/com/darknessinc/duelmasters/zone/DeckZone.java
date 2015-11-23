package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;
import java.util.Collections;

import com.darknessinc.duelmasters.cards.GameCard;
/**
 * 
 * @author Abraham
 * pretends the ArrayList is a stack with its end being the open side
 * ArrayList chosen, as cards may be removed from the middle and search operations may also be
 * performed
 * TODO handle danger of someone getting topcard and adding to a zone without removing from deck
 * TODO handle addCard function since for Deck must be added on top. Don't forget zoneId
 */
public class DeckZone extends Zone{
	
	public DeckZone(ArrayList<GameCard> gcs) {
		super(gcs);
	}
	@Override
	public void addCard(GameCard gc){
		super.addCard(gc);
	}
	public void shuffle(){
		Collections.shuffle(getCards());
	}		
	public GameCard getTopCard(){
		ArrayList<GameCard> gcs = getCards();
		if(gcs.size()==0)
			return null;
		return gcs.get(gcs.size()-1);
	}
	@Override
	public int getZoneId() {
		return ZONE_DECK;
	}
	@Override
	public String[] getOwnCardOptions(GameCard gc) {
		return new String[]{ACTION_SHOW_OPPONENT,ACTION_MOV_HAND,ACTION_MOV_GRAVE,ACTION_MOV_BATTLE,
				ACTION_MOV_MANA,ACTION_MOV_SHIELD,ACTION_SHUFFLE,ACTION_SEARCH};
	}
	@Override
	public String[] getOpponentCardOptions(GameCard gc) {
		return new String[]{ACTION_INDICATE};
	}
}