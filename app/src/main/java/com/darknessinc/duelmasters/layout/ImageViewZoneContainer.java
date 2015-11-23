package com.darknessinc.duelmasters.layout;

import android.view.View;
import android.widget.ImageView;

import com.darknessinc.duelmasters.zone.Zone;

/**
 * Only contains an ImageView
 * @author USER
 *
 */
public abstract class ImageViewZoneContainer extends ZoneContainer{
	private ImageView mFaceIv;
	public ImageViewZoneContainer(View r, Zone z) {
		super(r, z);
		mFaceIv = (ImageView)r;
	}
	public ImageView getFaceImageView(){
		return mFaceIv;
	}
}
