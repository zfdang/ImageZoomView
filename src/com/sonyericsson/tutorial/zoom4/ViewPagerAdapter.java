package com.sonyericsson.tutorial.zoom4;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sonyericsson.zoom.DynamicZoomControl;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.PinchZoomListener;

public class ViewPagerAdapter extends PagerAdapter {

//	private TouchImageView m_currentView;
	private Context m_context;
	ArrayList<String> m_imageUrls;
	final int[] m_pics;

	public ViewPagerAdapter(int[] pics, Context context) {
		m_context = context;
		m_pics = pics;
	}

	@Override
	public int getCount() {
		if(m_pics != null)
			return m_pics.length;
		return 0;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ImageZoomView view = (ImageZoomView)object;
		((ViewPager) container).removeView(view);
		view.cleanup();
		view = null;
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// initialize TouchImageView
		int btResource = m_pics[position];
	    Bitmap bitmap = BitmapFactory.decodeResource(m_context.getResources(), btResource);

	    DynamicZoomControl zoomControl = new DynamicZoomControl();
	    PinchZoomListener pinchZoomListener = new PinchZoomListener(m_context);
        pinchZoomListener.setZoomControl(zoomControl);
        
        ImageZoomView zoomView = new ImageZoomView(m_context, null);
        zoomView.setZoomState(zoomControl.getZoomState());
        zoomView.setImage(bitmap);

        zoomControl.setAspectQuotient(zoomView.getAspectQuotient());

        zoomView.setOnTouchListener(pinchZoomListener);
        zoomView.setCanScrollHorizontally(pinchZoomListener);
        zoomView.setOnLongClickListener((OnLongClickListener)m_context);

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		zoomView.setLayoutParams(mParams);
		((ViewPager) container).addView(zoomView, 0);

		return zoomView;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

}
