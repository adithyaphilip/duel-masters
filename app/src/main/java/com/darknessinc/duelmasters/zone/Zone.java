package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;

import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.feed.InstructionDecoder;
/**
 * how dependable is zoneId?
 * huge mistake made for constructor. Check it out. Lesson to learn.
 * TODO ensure GameCard zoneId=-1 check is used everywhere. Maybe implement in the add function i.e error log if zoneId!=-1
 * TODO ? is it better to have options in a standard order rather than in order of relevance?
 */
public abstract class Zone {
	//below constants are used by GameCard to specify which zone it's in
	public final static int ZONE_BATTLE=1;
	public final static int ZONE_HAND = 2;
	public final static int ZONE_GRAVEYARD = 3;
	public final static int ZONE_MANA = 4;
	public final static int ZONE_DECK = 5;
	public final static int ZONE_SHIELD = 6;
	
	//constants for use with Instruction generation and Option generation
	public final static String ACTION_MOV_HAND= "Move to Hand";
	public final static String ACTION_MOV_GRAVE= "Move to Graveyard";
	public final static String ACTION_MOV_SHIELD= "Move to Shield";
	public final static String ACTION_MOV_BATTLE= "Move to Battle Zone";
	public final static String ACTION_MOV_DECK= "Move to Deck";
	public final static String ACTION_MOV_MANA= "Move to Mana";
	//used by all
	public final static String ACTION_INDICATE = "Indicate Card";

	//used by Deck and Shield and Hand
	public final static String ACTION_SHOW_OPPONENT="Show opponent";
	public final static String ACTION_SHUFFLE = "Shuffle";
	
	//Used by Deck and Shield
	public final static String ACTION_PEEK = "Peek";
	//Used by Deck and Grave
	public final static String ACTION_SEARCH = "Search";
	
	//Used mainly by Battle Zone
	public final static String ACTION_TAP = "Tap/Untap";
	
	
	private HashMap<String,Integer> optionMap = new HashMap<String,Integer>();
	
	private ArrayList<GameCard> mCardList = new ArrayList<GameCard>();
	/**
	 * Since addCard did more than just adding, and we should have known it all along, we should have made the constructor
	 * accept the arrayList, then added each card to the Zone in a loop
	 * We just assigned the passed list to our internal list, and therefore none of their zoneids were set
	 * mistake happened because initially there was no zoneId in cards, hence we didn't differentiate between adding a card
	 * to the Zone via add function and adding directly to the list
	 * To prevent:
	 * a) Ask yourself whether adding to the Zone is supposed to imbibe some special characteristics into the card, 
	 * 	such as making it face up, or untapped
	 * b) When you changed the add function, think, was there some way cards can be added to the zone without going through
	 * 	this add function, and check all the available functions out
	 * Similar mistake happened while re-implementing the add function in the DeckZone. This could have been prevented 
	 * 	by checking for whether the super class does anything cool, or later when the super-class method was changed
	 * c) Additional check: See if any sub-classes have implemented this method their own way, or depend on the method
	 * 	working the way it does. 
	 * 	To do this write assumptions in the sub-classes while writing them, then go check out assumptions when change
	 *	is made to super-class.
	 *	Eg:- DeckZone assumes card is always added to the end of the list and that the end of the
	 * 	list is the top of the stack
	 * 	If a subclass has implemented in its own way, ensure change brought about by modified super-class function is
	 * 	repeated in the sub-class function too. Not to worry if it calls super?
	 * @param gcs
	 */
	public Zone(ArrayList<GameCard> gcs){
		mCardList = new ArrayList<GameCard>();
		if(gcs==null)
			gcs = new ArrayList<GameCard>();
		for(int i=0;i<gcs.size();i++){
			Log.d("Zone","Zone id:"+getZoneId()+"Card added: "+gcs.get(i).getGameId());
			addCard(gcs.get(i));
		}
		initializeOptionMap();
	}
	private void initializeOptionMap(){//as of now, disturbing this order will require changes to generateInstruction
		int ctr = 1;
		optionMap.put(ACTION_MOV_HAND,ctr++);//1
		optionMap.put(ACTION_MOV_BATTLE,ctr++);//2
		optionMap.put(ACTION_MOV_GRAVE,ctr++);//3
		optionMap.put(ACTION_MOV_SHIELD,ctr++);//4
		optionMap.put(ACTION_MOV_MANA,ctr++);//5
		optionMap.put(ACTION_MOV_DECK,ctr++);//6
		
		optionMap.put(ACTION_INDICATE,ctr++);//7
		
		optionMap.put(ACTION_PEEK,ctr++);//8
		
		optionMap.put(ACTION_SHOW_OPPONENT,ctr++);//9
		
		optionMap.put(ACTION_SEARCH, ctr++);//10
		
		optionMap.put(ACTION_TAP,ctr++);//11
		
		optionMap.put(ACTION_SHUFFLE,ctr++);//12
	}
	public ArrayList<GameCard> getCards(){
		return mCardList;
	}
	public GameCard getRandomCard(){
		Random r = new Random();
		return mCardList.get(r.nextInt(mCardList.size()));
	}
	public int size(){
		return mCardList.size();
	}
	public GameCard removeCard(int id){
		GameCard gc=null;
		for(int i=0;i<mCardList.size();i++)
			if(mCardList.get(i).getGameId()==id){
				gc =  mCardList.remove(i);
				gc.setZoneId(-1);//to indicate it is no longer associated with a zone
			}
		return gc;//will be null if no card found
	}
	/**
	 * adds a card to the end of the cardlist
	 * @param gc
	 */
	public void addCard(GameCard gc){
		gc.setZoneId(getZoneId());
		mCardList.add(gc);
	}
	/**
	 * used to identify Zone
	 * @return integer which can be mapped to one of the ZONE_.. constants present in the Zone class
	 */
	public abstract int getZoneId();
	/**
	 * gc parameter is passed as some Zones may need to modify their options based on Card in question
	 * Eg:- HandZone may show different options depending on whether the card is a spell or a creature
	 * Eg:- BattleZone may show Tap or Untap depending on the current tapped state of the creature
	 * @param gc
	 * @return
	 */
	public abstract String[] getOwnCardOptions(GameCard gc);
	public abstract String[] getOpponentCardOptions(GameCard gc);
	
	/**
	 * TODO!! add instructions for all Zones
	 * not to worry about wrong opcode being generated as
	 * a) ZoneId is used to decide which Zone's options are being chosen. Hence only options valid for a Zone can be chosen for it
	 * b) Option with same name does same thing for all Zones
	 * @param gameCardId
	 * @param selectedOption
	 * @return
	 */
	public String generateInstruction(int gameCardId, String selectedOption){
		String instruction="";
		instruction+=getOpCodeForInstruction(selectedOption);
		instruction+=":";
		instruction+=gameCardId+":"+getZoneId()+":";
		instruction+=getToZoneIdForInstruction(selectedOption);
		return instruction;
	}
	public int getOpCodeForInstruction(String selectedOption){
		int mappedInt = optionMap.get(selectedOption);
		
		switch(mappedInt){//sets opcode, no default
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return InstructionDecoder.ACTION_SHIFT_ZONE;
		case 7:
			return InstructionDecoder.ACTION_INDICATE;
		case 8:
			return InstructionDecoder.ACTION_PEEK;
		case 9:
			return InstructionDecoder.ACTION_SHOW_OPPONENT;
		case 10:
			return InstructionDecoder.ACTION_SEARCH;
		case 11:
			return InstructionDecoder.ACTION_TAP;
		case 12:
			return InstructionDecoder.ACTION_SHUFFLE;
		default:
			Log.d("Zone", "generateInstruction invalid option: " + selectedOption + " Zone: " + this.getClass() );
			return InstructionDecoder.SENTINEL_VALUE;//Sentinel Value
		}
	}
	public int getToZoneIdForInstruction(String selectedOption){
		int mappedInt = optionMap.get(selectedOption);
		switch(mappedInt){//sets toZoneId if applicable, Sentinel value otherwise
		case 1:
			return Zone.ZONE_HAND;
		case 2:
			return Zone.ZONE_BATTLE;
		case 3:
			return Zone.ZONE_GRAVEYARD;
		case 4:
			return Zone.ZONE_SHIELD;
		case 5:
			return Zone.ZONE_MANA;
		case 6:
			return Zone.ZONE_DECK;
		default://none of the other instructions specify a toZone id
			return InstructionDecoder.SENTINEL_VALUE;
		}
	}
	public static String getName(int zoneId){
		switch(zoneId){//sets toZoneId if applicable, Sentinel value otherwise
		case ZONE_HAND:
			return "Hand";
		case ZONE_BATTLE:
			return "Battle Zone";
		case ZONE_GRAVEYARD:
			return "Graveyard";
		case ZONE_SHIELD:
			return "Shield Zone";
		case ZONE_MANA:
			return "Mana Zone";
		case ZONE_DECK:
			return "Deck";
		default://none of the other instructions specify a toZone id
			return "Unspecified zone";
		}
	}
	public void shuffle(){
		Collections.shuffle(mCardList);
	}
	public static boolean isHidden(int zoneId){
		switch(zoneId){//sets toZoneId if applicable, Sentinel value otherwise
		case ZONE_HAND:
		case ZONE_SHIELD:
		case ZONE_DECK:
			return true;
		case ZONE_BATTLE:
		case ZONE_MANA:
		case ZONE_GRAVEYARD:
			return 	false;
		default://none of the other instructions specify a toZone id
			Log.e("Zone","isHidden: UNSPECIFIED ZONE "+zoneId);
			return false;
		}
	}
}
