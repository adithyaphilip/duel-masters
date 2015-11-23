package com.darknessinc.duelmasters.layout;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.imageutils.ImageOverlayer;
import com.darknessinc.duelmasters.zone.DeckZone;
import com.darknessinc.duelmasters.zone.Zone;

/**
 * Assumes View passed to it in constructor is an ImageView
 * NOTE w.r.t Turning up face card: Assumes that getHeight and getWidth can be called to get correct values on this Deck
 * @author USER
 * TODO check for null when deck is empty. Do edge case test for all ZoneContainers.
 */
public class DeckZoneContainer extends ZoneContainer {
	ImageView mFaceView;
	public DeckZoneContainer(View r, Zone z) {
		super(r, z);
		mFaceView = (ImageView)r;
		resetFace();
	}
	@Override
	public void addCard(GameCard gc){
		super.addCard(gc);
		resetFace();
	}
	@Override
	public GameCard removeCard(int gameId){
		GameCard gc = super.removeCard(gameId);		
		resetFace();
		return gc;
	}
	/**
	 * TODO get rid of cheap patch of setFaceCardListener
	 */
	public void resetFace(){
		mFaceView.setImageResource(R.drawable.dmcardback);
		ImageOverlayer.removeOverlay(mFaceView);
		if(getZone().size()==0)
			mFaceView.setOnClickListener(null);
		else
			setFaceCardListener();
	}
	public void setFaceCardListener(){
		GameCard gc = ((DeckZone)getZone()).getTopCard();
		mFaceView.setOnClickListener(new CardClickListener(gc, getZone(), (Activity)mFaceView.getContext()));
	}
	public void showFaceCard(){
		GameCard gc = ((DeckZone)getZone()).getTopCard();
		if(gc==null){
			Log.e("DECK","DeckEmpty");
		}
		else
			mFaceView.setImageBitmap(gc.getDbCard().getCardBitmap(mFaceView.getWidth(), 0));
	}
	public void indicateCard(int gameId){
		GameCard gc = ((DeckZone)getZone()).getTopCard();;
		gc.indicate();
		if(gc.isIndicated())
			ImageOverlayer.indicateOverlay(mFaceView);
		else
			ImageOverlayer.removeOverlay(mFaceView);
	}
	@Override
	public void revealCard(int gameId) {
		showFaceCard();		
	}

	@Override
	public void tapCard(int gameCardId){
		//not required
	}
	@Override
	public void resetLayout() {
		resetFace();
	}
}
