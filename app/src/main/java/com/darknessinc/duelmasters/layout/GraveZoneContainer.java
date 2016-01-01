package com.darknessinc.duelmasters.layout;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.imageutils.ImageOverlayer;
import com.darknessinc.duelmasters.zone.GraveZone;
import com.darknessinc.duelmasters.zone.Zone;

/**
 * TODO handle resetting face with onClickListener
 *
 * @author USER
 */
public class GraveZoneContainer extends ZoneContainer {
    ImageView mFaceView;

    public GraveZoneContainer(View r, Zone z) {
        super(r, z);
        mFaceView = (ImageView) r;
        resetFace();
    }

    @Override
    public void addCard(GameCard gc) {
        super.addCard(gc);
        resetFace();
    }

    @Override
    public GameCard removeCard(int gameId) {
        GameCard gc = super.removeCard(gameId);
        resetFace();
        return gc;
    }

    public void resetFace() {
        GameCard gc = ((GraveZone) getZone()).getTopCard();
        if (gc == null) {
            Log.e("Grave", "GraveEmpty");
            mFaceView.setImageResource(R.drawable.dmcardback);
            mFaceView.setOnClickListener(null);
        } else {
            mFaceView.setImageBitmap(gc.getDbCard().getCardBitmap(mFaceView.getWidth(), 0));
            setFaceCardListener();
        }
    }

    public void setFaceCardListener() {
        GameCard gc = ((GraveZone) getZone()).getTopCard();
        mFaceView.setOnClickListener(new CardClickListener(gc, getZone(), (Activity) mFaceView.getContext()));
    }

    public void indicateCard(int gameId) {
        ImageOverlayer.indicateOverlay(mFaceView);
    }

    @Override
    public void revealCard(int gameId) {
        //do nothing
    }

    @Override
    public void tapCard(int gameCardId) {
        //do nothing
    }

    @Override
    public void resetLayout() {
        resetFace();
    }
}
