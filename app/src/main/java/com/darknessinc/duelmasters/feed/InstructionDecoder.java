package com.darknessinc.duelmasters.feed;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.darknessinc.duelmasters.activity.CardChooserActivity;
import com.darknessinc.duelmasters.activity.GameActivity;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.layout.ZoneContainer;
import com.darknessinc.duelmasters.player.ImplicitPlayerDetails;
import com.darknessinc.duelmasters.zone.Zone;

/**
 * Used to decode instructions passed between activities
 * Instruction format: optionchosen:cardid:zonefromid:zonetoid
 * @author USER
 *
 */
public class InstructionDecoder {
	//TODO possibly flawed design. Must establish link b/w Zones and Instructions
	public final static int SENTINEL_VALUE = -1;//value which can be safely used to denote a missing or invalid id or opcode
	
	public final static int ACTION_INDICATE = 1;
	public final static int ACTION_SHIFT_ZONE = 2;
	public final static int ACTION_TAP = 3;
	public final static int ACTION_SEARCH=4;//applicable to both deck and grave
	public final static int ACTION_PEEK=5;
	public final static int ACTION_SHOW_OPPONENT=6;
	public final static int ACTION_SHUFFLE=7;
	/**
	 * TODO refactor to name executeInstruction
	 * Instruction is delimited using : to differentiate fields
	 * in case instruction does not require a card, we can simply supply the player id as card id
	 * instruction is assumed to always have a zoneid
	 * @param instruction
	 * @param from
	 * @param to
	 */
	public static void decodeInstruction(String instruction,GameActivity activity){
		Log.e("TESTING","instruction: "+instruction);
		String parts[] = instruction.split(":");
		int opCode = Integer.parseInt(parts[0]);
		int gameCardId = Integer.parseInt(parts[1]);
		int zoneFromId = Integer.parseInt(parts[2]);
		int zoneToId = Integer.parseInt(parts[3]);
		ZoneContainer zcFrom = activity.getZoneContainer(gameCardId, zoneFromId);
		
		String result="";
		
		//print message here TODO
		switch(opCode){
		case ACTION_INDICATE:
			zcFrom.indicateCard(gameCardId);
			break;
		case ACTION_SHIFT_ZONE:
			ZoneContainer zcTo = activity.getZoneContainer(gameCardId, zoneToId);
			shiftZone(gameCardId, zcFrom, zcTo);
			break;
		case ACTION_SHOW_OPPONENT:
			zcFrom.revealCard(gameCardId);
			break;
		case ACTION_PEEK:
			//just print message
			break;
		case ACTION_TAP:
			zcFrom.tapCard(gameCardId);
			break;
		case ACTION_SHUFFLE:
			zcFrom.shuffle();
			break;
		case ACTION_SEARCH:
			zcFrom.getCards();
			Intent i = new Intent(activity,CardChooserActivity.class);
			i.putExtra(CardChooserActivity.KEY_GAME_CARD_ARRAY, zcFrom.getCards().toArray(new GameCard[0]));
			activity.startActivityForResult(i,GameActivity.CARD_CHOOSER_ACTIVITY);
			break;
		}
	}
	public static void shiftZone(int gameCardId, ZoneContainer from, ZoneContainer to){
		GameCard gc = from.removeCard(gameCardId);
		to.addCard(gc);
	}
	/**
	 * returns message to be displayed based on actions performed by instruction
	 * NOTE:- Assumes instruction was first executed, then the message to display was requested
	 * @param message
	 * @return
	 */
	public static String getInstructionMessage(String instruction, GameActivity activity){
		String parts[] = instruction.split(":");
		int opCode = Integer.parseInt(parts[0]);
		int gameCardId = Integer.parseInt(parts[1]);
		String playerName = activity.getPlayer(gameCardId%10).getName();
		int zoneFromId = Integer.parseInt(parts[2]);
		int zoneToId = Integer.parseInt(parts[3]);		
		GameCard gc = activity.getGameCard(gameCardId);
		String cardName = gc==null?"":gc.getDbCard().getName();
		String message=playerName + " has ";
		boolean isHidden = Zone.isHidden(zoneFromId);
		switch(opCode){
		case ACTION_INDICATE:
			if(!gc.isIndicated()){
				message="";
				return message;
			}
			message+="indicated ";
			if(gc == null || isHidden)
				message+=" Card "+gameCardId;
			else
				message+=" "+cardName;
			message+=" in " + Zone.getName(zoneFromId);
			break;
		case ACTION_SHIFT_ZONE:
			message+="moved ";
			if(gc == null)
				message+=" a card ";
			else if(isHidden&&Zone.isHidden(zoneToId))
				message+=" Card "+gameCardId;
			else
				message+=" "+cardName;
			message+=" from " + Zone.getName(zoneFromId);
			message+=" to " + Zone.getName(zoneToId);
			break;
		case ACTION_SHOW_OPPONENT:
			message+="shown "+gc.getDbCard().getName()+" in " + Zone.getName(zoneFromId) + " to his opponent";
			break;
		case ACTION_PEEK:
			message +="peeked at Card "+gameCardId+" in "+Zone.getName(zoneFromId);
			break;
		case ACTION_TAP:
			message+="tapped "+gc.getDbCard().getName()+" (Card "+gameCardId + ") in " + Zone.getName(zoneFromId);
			break;
		case ACTION_SHUFFLE:
			message+="shuffled his " + Zone.getName(zoneFromId);
			break;
		case ACTION_SEARCH:
			message+="searched his " + Zone.getName(zoneFromId);
			break;
		}
		return message + ".";
	}
	public static String getInstructionToSend(String instruction){
		String parts[] = instruction.split(":");
		int cardid = Integer.parseInt(parts[1]);
		parts[1] = (cardid/10*10 + ImplicitPlayerDetails.getOtherPlayerId(cardid%10))+"";
		String newInstr = TextUtils.join(":", parts);
		return newInstr;
	}
	public static String getPeekInstruction(int gameCardId, int zoneId){
		return ACTION_PEEK+":"+gameCardId+":"+zoneId+":"+SENTINEL_VALUE;
	}
	public static String getPeekInstruction(GameCard gc){
		return getPeekInstruction(gc.getGameId(), gc.getZoneId());
	}
}
