package com.darknessinc.duelmasters.views;

import android.content.Context;
import android.widget.ImageView;

import com.darknessinc.duelmasters.cards.GameCard;

/**
 * TODO retrieve Image which fits from file storage
 *
 * @author USER
 */
public class GameCardView extends ImageView {
    private GameCard mGameCard;

    public GameCardView(Context context, GameCard gc) {
        super(context);
        mGameCard = gc;
    }

    public GameCard getGameCard() {
        return mGameCard;
    }
}
