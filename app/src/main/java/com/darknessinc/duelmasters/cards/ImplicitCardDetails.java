package com.darknessinc.duelmasters.cards;

/**
 * like last digit of cardid is playerid
 *
 * @author USER
 */
public class ImplicitCardDetails {
    public static int getPlayerId(int gameCardId) {
        return gameCardId % 10;
    }
}
