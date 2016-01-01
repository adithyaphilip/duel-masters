package com.darknessinc.duelmasters.activity.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.layout.DeckZoneContainer;
import com.darknessinc.duelmasters.layout.GraveZoneContainer;
import com.darknessinc.duelmasters.layout.HandZoneContainer;
import com.darknessinc.duelmasters.layout.LinearLayoutZoneContainer;
import com.darknessinc.duelmasters.layout.ShieldZoneContainer;
import com.darknessinc.duelmasters.layout.ZoneContainer;
import com.darknessinc.duelmasters.zone.BattleZone;
import com.darknessinc.duelmasters.zone.DeckZone;
import com.darknessinc.duelmasters.zone.GraveZone;
import com.darknessinc.duelmasters.zone.HandZone;
import com.darknessinc.duelmasters.zone.ManaZone;
import com.darknessinc.duelmasters.zone.ShieldZone;
import com.darknessinc.duelmasters.zone.Zone;

import java.util.ArrayList;

/**
 * Prescribes an order for accessing and storing the various zones internally. Reduces code
 * duplicatio by using arrays.
 * ORDER: Mana, Hand, Shield, Battle, Grave, Deck
 * @author abrahamphilip
 */
public class ZoneContainerCollection {
    private static int MANA_INDEX = 0;
    private static int HAND_INDEX = 1;
    private static int SHIELD_INDEX = 2;
    private static int BATTLE_INDEX = 3;
    private static int GRAVE_INDEX = 4;
    private static int DECK_INDEX = 5;

    private static final int ZONE_COUNT = 6;

    private ZoneContainer[] mZoneContainers = new ZoneContainer[ZONE_COUNT];

    public static class GameCardsLists implements Parcelable {
        private ArrayList<GameCard>[] mZoneCards;

        GameCardsLists(ArrayList<GameCard>[] zonesCards) {
            mZoneCards = zonesCards;
        }

        GameCardsLists(Parcel in) {
            int ctr = 0;
            for(Object o: in.readArray(getClass().getClassLoader())) {
                mZoneCards[ctr++] = (ArrayList<GameCard>) o;
            }
        }

        public static final Creator<GameCardsLists> CREATOR = new Creator<GameCardsLists>() {
            @Override
            public GameCardsLists createFromParcel(Parcel in) {
                return new GameCardsLists(in);
            }

            @Override
            public GameCardsLists[] newArray(int size) {
                return new GameCardsLists[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeArray(mZoneCards);
        }
    }

    public ZoneContainerCollection(GameCardsLists lists,
                                   LinearLayout manaView, LinearLayout handView,
                                   LinearLayout shieldView, LinearLayout battleView,
                                   ImageView graveView, ImageView deckView) {
        initialize(lists.mZoneCards,
                manaView, handView, shieldView, battleView, graveView, deckView);
    }

    /**
     * Only Deck is filled with supplied cards, for when game is started
     */
    public ZoneContainerCollection(ArrayList<GameCard> deckCards,
                                   LinearLayout manaView, LinearLayout handView,
                                   LinearLayout shieldView, LinearLayout battleView,
                                   ImageView graveView, ImageView deckView) {
        ArrayList<GameCard>[] cards = new ArrayList[ZONE_COUNT];
        for(int i = 0; i< cards.length; i++) cards[i] = new ArrayList<>();
        cards[DECK_INDEX] = deckCards;
        initialize(cards, manaView, handView, shieldView, battleView, graveView, deckView);
    }

    public GameCardsLists getGameCardsLists() {
        ArrayList<GameCard>[] cards = new ArrayList[ZONE_COUNT];
        for(int i = 0 ;i<mZoneContainers.length;i++) {
            cards[i] = mZoneContainers[i].getCards();
        }
        return new GameCardsLists(cards);
    }

    /**
     * initializes the containers with their views
     */
    private void initialize(ArrayList<GameCard> cards[], LinearLayout manaView, LinearLayout handView,
                           LinearLayout shieldView, LinearLayout battleView,
                           ImageView graveView, ImageView deckView) {
        mZoneContainers[HAND_INDEX] = new HandZoneContainer(handView,
                new HandZone(cards[HAND_INDEX]),deckView);
        mZoneContainers[MANA_INDEX] = new LinearLayoutZoneContainer(manaView,
                new ManaZone(cards[MANA_INDEX]),deckView);
        mZoneContainers[DECK_INDEX] = new DeckZoneContainer(deckView,
                new DeckZone(cards[DECK_INDEX]));
        mZoneContainers[GRAVE_INDEX] = new GraveZoneContainer(graveView,
                new GraveZone(cards[GRAVE_INDEX]));
        mZoneContainers[SHIELD_INDEX] = new ShieldZoneContainer(shieldView,
                new ShieldZone(cards[SHIELD_INDEX]),deckView);
        mZoneContainers[BATTLE_INDEX] = new LinearLayoutZoneContainer(battleView,
                new BattleZone(cards[BATTLE_INDEX]),deckView);
    }

    public ZoneContainer getZoneContainer(int zoneId) {
        switch(zoneId){
            case Zone.ZONE_BATTLE:
                return mZoneContainers[BATTLE_INDEX];
            case Zone.ZONE_DECK:
                return mZoneContainers[DECK_INDEX];
            case Zone.ZONE_GRAVEYARD:
                return mZoneContainers[GRAVE_INDEX];
            case Zone.ZONE_HAND:
                return mZoneContainers[HAND_INDEX];
            case Zone.ZONE_MANA:
                return mZoneContainers[MANA_INDEX];
            case Zone.ZONE_SHIELD:
                return mZoneContainers[SHIELD_INDEX];
            default:
                Log.d("GamePlayer", "getZoneContainer invalid zoneId passed: " + zoneId);
                return null;
        }
    }


}
