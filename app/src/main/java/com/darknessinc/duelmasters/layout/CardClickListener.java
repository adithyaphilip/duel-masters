package com.darknessinc.duelmasters.layout;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.darknessinc.duelmasters.activity.CardOptionChooserActivity;
import com.darknessinc.duelmasters.activity.GameActivity;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.zone.Zone;

/**
 * since it's so tightly coupled with GameActivity, consider making it a static inner class of GameActivity
 *
 * @author USER
 */
public class CardClickListener implements OnClickListener {
    GameCard mGameCard;
    Zone mZone;
    Activity mActivity;

    public CardClickListener(GameCard gc, Zone z, Activity a) {
        mGameCard = gc;
        mZone = z;
        mActivity = a;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(mActivity, CardOptionChooserActivity.class);
        i.putExtra(CardOptionChooserActivity.KEY_CARD, mGameCard);
        String[] options;
        if (GameActivity.isOpponent(GameActivity.getPlayerId(mGameCard)))
            options = mZone.getOpponentCardOptions(mGameCard);
        else
            options = mZone.getOwnCardOptions(mGameCard);
        i.putExtra(CardOptionChooserActivity.KEY_OPTIONS, options);
        mActivity.startActivityForResult(i, GameActivity.CARD_OPTIONS_ACTIVITY);
    }
}
