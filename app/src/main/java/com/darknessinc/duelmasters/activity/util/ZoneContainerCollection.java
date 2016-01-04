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
 *
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

    private GameCardsLists mInitialGcl;

    public static class GameCardsLists implements Parcelable {
        private ArrayList<GameCard>[] mZoneCards = new ArrayList[ZONE_COUNT];

        GameCardsLists(ArrayList<GameCard>[] zonesCards) {
            mZoneCards = zonesCards;
        }

        GameCardsLists(Parcel in) {
            int ctr = 0;
            for (Object o : in.readArray(getClass().getClassLoader())) {
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

    /**
     * Only Deck is filled with supplied cards, for when game is started
     */
    public ZoneContainerCollection(ArrayList<GameCard> deckCards,
                                   LinearLayout manaView, LinearLayout handView,
                                   LinearLayout shieldView, LinearLayout battleView,
                                   ImageView graveView, ImageView deckView) {
        initialize(deckCards, manaView, handView, shieldView, battleView, graveView, deckView);
    }

    public ZoneContainerCollection(GameCardsLists gcl,
                                   LinearLayout manaView, LinearLayout handView,
                                   LinearLayout shieldView, LinearLayout battleView,
                                   ImageView graveView, ImageView deckView) {
        mInitialGcl = gcl;
        initialize(gcl.mZoneCards[DECK_INDEX],
                manaView, handView, shieldView, battleView, graveView, deckView);
    }

    public GameCardsLists getGameCardsLists() {
        ArrayList<GameCard>[] cards = new ArrayList[ZONE_COUNT];
        for (int i = 0; i < mZoneContainers.length; i++) {
            cards[i] = mZoneContainers[i].getCards();
        }
        return new GameCardsLists(cards);
    }

    /**
     * initializes the containers with their views and empty zones
     */
    private void initialize(ArrayList<GameCard> deckCards,
                            LinearLayout manaView, LinearLayout handView,
                            LinearLayout shieldView, LinearLayout battleView,
                            ImageView graveView, ImageView deckView) {
        mZoneContainers[HAND_INDEX] = new HandZoneContainer(handView,
                new HandZone(new ArrayList<GameCard>()), deckView);
        mZoneContainers[MANA_INDEX] = new LinearLayoutZoneContainer(manaView,
                new ManaZone(new ArrayList<GameCard>()), deckView);
        mZoneContainers[DECK_INDEX] = new DeckZoneContainer(deckView,
                new DeckZone(deckCards));
        mZoneContainers[GRAVE_INDEX] = new GraveZoneContainer(graveView,
                new GraveZone(new ArrayList<GameCard>()));
        mZoneContainers[SHIELD_INDEX] = new ShieldZoneContainer(shieldView,
                new ShieldZone(new ArrayList<GameCard>()), deckView);
        mZoneContainers[BATTLE_INDEX] = new LinearLayoutZoneContainer(battleView,
                new BattleZone(new ArrayList<GameCard>()), deckView);
    }

    public ZoneContainer getZoneContainer(int zoneId) {
        switch (zoneId) {
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

    /**
     * to be called only after deck size has been resolved to add cards to respective zones
     * necessary since other zones use deck as a reference view
     * Assumes zones return cards in the order in which they are to be added
     */
    public void initializeCardsExceptDeck() {
        if (mInitialGcl == null) return;
        for (int i = 0; i < mZoneContainers.length; i++) {
            if (i != DECK_INDEX) {
                Log.d("ZoneContainerCollection", "CardList for zone at index " + i + ": " + mInitialGcl.mZoneCards[i]);
                for (GameCard gc : mInitialGcl.mZoneCards[i]) {
                    Log.d("ZoneContainerCollection", "Adding card " + gc.getGameId() + "at index " + i);
                    mZoneContainers[i].addCard(gc);
                }
            }
        }
        mInitialGcl = null; // to prevent anything from happening if function is called again.
        // This happens since it's called in onGlobalLayoutListener currently
    }
}
