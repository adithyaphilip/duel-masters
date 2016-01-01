package com.darknessinc.duelmasters.layout;

import java.util.ArrayList;

import android.view.View;

import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.zone.Zone;

/**
 * @author Abraham
 *         base class for Zones to manage their displays. Not meant to bes used directly
 *         Simply adds a card to its base Zone
 */
public abstract class ZoneContainer {
    private View mView;
    private Zone mZone;

    public ZoneContainer(View r, Zone z) {
        mView = r;
        mZone = z;
    }

    protected View getView() {
        return mView;
    }

    protected Zone getZone() {
        return mZone;
    }

    public void addCard(GameCard gc) {
        mZone.addCard(gc);
    }

    public GameCard removeCard(int gameId) {
        return mZone.removeCard(gameId);
    }

    /**
     * used to provide access to the generateInstruction function without having to give access to the underlying Zone
     *
     * @param gameCardId
     * @param instruction
     * @return
     */
    public String generateInstruction(int gameCardId, String option) {
        return mZone.generateInstruction(gameCardId, option);
    }

    public String[] getOwnCardOptions(GameCard gc) {
        return mZone.getOwnCardOptions(gc);
    }

    public void shuffle() {
        mZone.shuffle();
        resetLayout();
    }

    public ArrayList<GameCard> getCards() {
        return mZone.getCards();
    }

    public abstract void resetLayout();

    public abstract void revealCard(int gameId);

    public abstract void indicateCard(int gameId);

    public abstract void tapCard(int gameId);
}
