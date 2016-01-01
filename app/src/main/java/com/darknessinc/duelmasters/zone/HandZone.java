package com.darknessinc.duelmasters.zone;

import java.util.ArrayList;

import com.darknessinc.duelmasters.cards.GameCard;


public class HandZone extends Zone {

    public HandZone(ArrayList<GameCard> gcs) {
        super(gcs);
    }

    @Override
    public int getZoneId() {
        return ZONE_HAND;
    }

    @Override
    public String[] getOwnCardOptions(GameCard gc) {
        return new String[]{Zone.ACTION_MOV_BATTLE, Zone.ACTION_MOV_GRAVE, Zone.ACTION_MOV_MANA, Zone.ACTION_MOV_DECK, Zone.ACTION_MOV_SHIELD, Zone.ACTION_SHOW_OPPONENT, Zone.ACTION_SHUFFLE};
    }

    @Override
    public String[] getOpponentCardOptions(GameCard gc) {
        return new String[]{ACTION_INDICATE};
    }
}
