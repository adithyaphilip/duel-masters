package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;

import com.darknessinc.duelmasters.cards.GameCard;


public class ShieldZone extends Zone {

    public ShieldZone(ArrayList<GameCard> gcs) {
        super(gcs);
    }

    @Override
    public int getZoneId() {
        return ZONE_SHIELD;
    }

    @Override
    public String[] getOwnCardOptions(GameCard gc) {
        return new String[]{ACTION_SHOW_OPPONENT, ACTION_MOV_HAND, ACTION_MOV_GRAVE, ACTION_MOV_BATTLE, ACTION_MOV_MANA, ACTION_MOV_DECK};
    }

    @Override
    public String[] getOpponentCardOptions(GameCard gc) {
        return new String[]{ACTION_INDICATE};
    }
}
