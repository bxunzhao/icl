package com.droid.icl.view;

import android.view.View;

import java.util.Calendar;

public abstract class NoDoubleClickListener implements View.OnClickListener {

    private static final int CLICK_DELAY = 500;
    private static int MIN_CLICK_DELAY_TIME = CLICK_DELAY;
    private long lastClickTime = 0;

    public NoDoubleClickListener() {
        MIN_CLICK_DELAY_TIME = CLICK_DELAY;
    }

    public NoDoubleClickListener(int delayTime) {
        MIN_CLICK_DELAY_TIME = delayTime;
    }



    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
    public abstract void onNoDoubleClick(View v);

}