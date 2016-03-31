package com.droid.icl.view.indictorview.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SViewPager extends ViewPager {

	private boolean canScroll;

	public SViewPager(Context context) {
		super(context);
		canScroll = false;
	}

	public SViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		canScroll = false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (canScroll) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childSize = getChildCount();
		int maxHeight = 0;
		for (int i = 0; i < childSize; i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			if (child.getMeasuredHeight() > maxHeight) {
				maxHeight = child.getMeasuredHeight();
			}
		}

		if (maxHeight > 0) {
			setMeasuredDimension(getMeasuredWidth(), maxHeight);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canScroll) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	public void toggleLock() {
		canScroll = !canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

	public boolean isCanScroll() {
		return canScroll;
	}

}
