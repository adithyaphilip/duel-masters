package com.darknessinc.duelmasters.layout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.imageutils.ImageOverlayer;
import com.darknessinc.duelmasters.imageutils.SizeManager;
import com.darknessinc.duelmasters.views.GameCardView;
import com.darknessinc.duelmasters.zone.Zone;
/**
 * This class assumes: 
 * a) The passed View is a LinearLayout
 * b) When an object is added, it must be added to the LinearLayout, besides the underlying Zone
 * 
 * This class is useful for implementing Hand, Mana, Shield and BattleZone
 * 
 * IMP CANNOT BE USED WITHOUT SETTNG REQWIDTH and REQHEIGHT
 * DEPENDS ON REFVIEW never disappearing!
 * @author USER
 *
 */
public class LinearLayoutZoneContainer extends ZoneContainer {
	
	LinearLayout mLl;
	int mReqWidth;
	int mReqHeight;
	ImageView refView;
	public LinearLayoutZoneContainer(LinearLayout r, Zone z, ImageView t) {
		super(r, z);	
		mLl = r;
		refView = t;
	}
	public void setWidth(int reqWidth){
		mReqWidth=reqWidth;
	}
	@Override
	public void addCard(GameCard gc){
		super.addCard(gc);
		mReqWidth=refView.getWidth();
		addCardView(gc,mReqWidth,mReqHeight);
	}
	/**
	 * TODO get rid of having to pass width and height from outside
	 * handles adding the view, setting onClickListener, etc.
	 * @param gc
	 */
	protected void addCardView(final GameCard gc, int reqWidth, int reqHeight){
		Log.d("LLZoneContainer"," addCardView width: "+reqWidth+" height: "+reqHeight);
		GameCardView cv = gc.getGameCardView(mLl.getContext(), reqWidth, reqHeight);
		cv.setOnClickListener(new CardClickListener(gc, getZone(), (Activity)mLl.getContext()));
		cv.setAdjustViewBounds(true);
		cv.setScaleType(ScaleType.CENTER_INSIDE);
		LayoutParams lp = new LayoutParams(mReqWidth, LayoutParams.WRAP_CONTENT);
		int dip = SizeManager.getDip(2,(cv.getContext()).getResources().getDisplayMetrics());
		lp.setMargins(dip, dip, dip, dip);
		mLl.addView(cv,lp);
	}
	@Override
	public GameCard removeCard(int gameId){
		GameCard gc = super.removeCard(gameId);
		
		boolean removed = false;
		for(int i=0;i<mLl.getChildCount()&&!removed;i++){
			GameCardView cv = (GameCardView)mLl.getChildAt(i);
			if(cv.getGameCard().getGameId()==gameId){
				mLl.removeViewAt(i);
				removed=true;
			}
		}
		if(!removed){
			Log.e("LLZoneContainer","Unable to find "+gameId+" to remove!");
		}
		return gc;
	}
	@Override
	public void indicateCard(int gameId) {
		boolean found = false;
		GameCardView icv = null;
		for(int i=0;i<mLl.getChildCount()&&!found;i++){
			GameCardView cv = (GameCardView)mLl.getChildAt(i);
			if(cv.getGameCard().getGameId()==gameId){
				icv=cv;
				found=true;
			}
		}
		if(!found){
			Log.e("LLZoneContainer","Unable to find "+gameId+" to indicate!");
		}
		else{
			GameCard gc = icv.getGameCard();
			gc.indicate();
			if(gc.isIndicated())
				ImageOverlayer.indicateOverlay(icv);
			else
				ImageOverlayer.removeOverlay(icv);
		}
	}
	/**
	 * this function should ideally be used only by the Shield layout, and as such cannot be called 
	 * by other layouts as they will not be able to produce the correct instruction (assuming they are called
	 * by the InstructionDecoder). Nevertheless, it has been designed to handle such situations.
	 */
	@Override
	public void revealCard(int gameId){
		GameCardView cvToReveal = getGameCardView(gameId);
		int reqWidth = cvToReveal.getWidth();
		Bitmap cardBm = cvToReveal.getGameCard().getDbCard().getCardBitmap(reqWidth, 0);
		cvToReveal.setImageBitmap(cardBm);
	}
	protected GameCardView getGameCardView(int gameCardId){
		boolean found = false;
		GameCardView rCardView =null;
		for(int i=0;i<mLl.getChildCount()&&!found;i++){
			GameCardView cv = (GameCardView)mLl.getChildAt(i);
			if(cv.getGameCard().getGameId()==gameCardId){
				rCardView = cv;
				found=true;
			}
		}
		if(!found){
			Log.e("LLZoneContainer","getGameCardView: Unable to find "+gameCardId+" to get!");
		}
		return rCardView;
	}
	@Override
	public void tapCard(int gameCardId){
		GameCardView gcv = getGameCardView(gameCardId);
		gcv.getGameCard().tap();
		if(gcv.getGameCard().isTapped())
			ImageOverlayer.tapOverlay(gcv);
		else
			ImageOverlayer.removeOverlay(gcv);
	}
	@Override
	public void resetLayout() {
		// TODO Auto-generated method stub
		
	}
	
}
