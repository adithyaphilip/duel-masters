package com.darknessinc.duelmasters.player;
/**
 * holds implicitly assumed data about players wrt to player idsa, like opponent is always player 1
 * @author USER
 *
 */
public class ImplicitPlayerDetails {
	private static final int OWN_PLAYER_ID = 2;
	private static final int OPPONENT_PLAYER_ID = 1;
	public static int getOpponentPlayerId(){
		return OPPONENT_PLAYER_ID;
	}
	public static int getOwnPlayerId(){
		return OWN_PLAYER_ID;
	}
	public static boolean isOpponentPlayer(int id){
		return id==getOpponentPlayerId();
	}
	public static int getOtherPlayerId(int id){
		return id==getOpponentPlayerId()?getOwnPlayerId():getOpponentPlayerId();
	}
}
