package com.darknessinc.duelmasters.bluetooth;

import java.util.ArrayList;

import android.util.Log;

import com.darknessinc.duelmasters.cards.DatabaseCardRetriever;
import com.darknessinc.duelmasters.cards.GameCard;

public class CardEncoder {

	/**
	 * returns in format dbid/gcid:dbid/gcid...
	 * @param cards
	 * @return
	 */	
	public static String getCardsString(ArrayList<GameCard> cards){
		String cardString = cards.get(0).getDbCard().getDbId()+"/"+cards.get(0).getGameId();
		for(int i=1;i<cards.size();i++){
			cardString+=":"+cards.get(i).getDbCard().getDbId()+"/"+cards.get(i).getGameId();
		}
		//Log.d("getCardsString",cardString);
		return cardString;
	}
	
	public static ArrayList<GameCard> getGameCards(String encoded, DatabaseCardRetriever dbcRetriever){
		String cards[] = encoded.split(":");
		ArrayList<GameCard> ret = new ArrayList<GameCard>();
		int dbIds[] = new int[cards.length];
		int gcIds[] = new int[cards.length];
		for(int i=0;i<dbIds.length;i++){
			String parts[] = cards[i].split("/");
			dbIds[i] = Integer.parseInt(parts[0]);
			gcIds[i] = Integer.parseInt(parts[1]);
		}
		for(int i=0;i<dbIds.length;i++){
			ret.add(new GameCard(gcIds[i],dbcRetriever.getCard(dbIds[i])));
		}
		return ret;
	}
}
