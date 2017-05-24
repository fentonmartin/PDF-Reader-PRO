package com.artifex.mupdfdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

public class MuPDFPageView extends PageView {
	private final MuPDFCore mCore;

	public MuPDFPageView(Context c, MuPDFCore core, Point parentSize) {
		super(c, parentSize);
		mCore = core;
	}

//	public int hitLinkPage(float x, float y) {
//		// Since link highlighting was implemented, the super class
//		// PageView has had sufficient information to be able to
//		// perform this method directly. Making that change would
//		// make MuPDFCore.hitLinkPage superfluous.
//		float scale = mSourceScale*(float)getWidth()/(float)mSize.x;
//		float docRelX = (x - getLeft())/scale;
//		float docRelY = (y - getTop())/scale;
//
//		return mCore.hitLinkPage(mPageNumber, docRelX, docRelY);
//	}

	@Override
	protected void drawPage(Bitmap bm, int sizeX, int sizeY,
			int patchX, int patchY, int patchWidth, int patchHeight) {
		MuPDFCore.Cookie cookie = mCore.new Cookie();
		mCore.drawPage(bm,mPageNumber, sizeX, sizeY, patchX, patchY, patchWidth, patchHeight,cookie);
	}

	@Override
	protected LinkInfo[] getLinkInfo() {
		return mCore.getPageLinks(mPageNumber);
	}
}
