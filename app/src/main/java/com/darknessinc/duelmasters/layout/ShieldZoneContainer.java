package com.darknessinc.duelmasters.layout;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.imageutils.SizeManager;
import com.darknessinc.duelmasters.views.GameCardView;
import com.darknessinc.duelmasters.zone.Zone;

public class ShieldZoneContainer extends LinearLayoutZoneContainer {

	public ShieldZoneContainer(LinearLayout r, Zone z, ImageView t) {
		super(r, z, t);
	}
	@Override
	public void addCardView(final GameCard gc, int reqWidth, int reqHeight){
		Log.d("LLZoneContaine", "card added: " + reqWidth+"x"+reqHeight);
		GameCardView cv = gc.getGameCardView(mLl.getContext(),reqWidth, reqHeight);
		cv.setImageResource(R.drawable.dmcardback);
		cv.setOnClickListener(new CardClickListener(gc, getZone(), (Activity)mLl.getContext()));
		cv.setAdjustViewBounds(true);
		cv.setScaleType(ScaleType.CENTER_INSIDE);
		LayoutParams lp = new LayoutParams(mReqWidth, LayoutParams.WRAP_CONTENT);
		int dip = SizeManager.getDip(2,cv.getContext().getResources().getDisplayMetrics());
		lp.setMargins(dip, dip, dip, dip);
		mLl.addView(cv,lp);
	}
}
