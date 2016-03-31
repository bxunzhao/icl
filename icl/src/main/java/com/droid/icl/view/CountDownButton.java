package com.droid.icl.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Administrator on 2016/1/15.
 */
public class CountDownButton extends Button {
    private CountDownTimer timer;

    private Drawable bgDoneRes;
    private Drawable bgTickerRes;
    private int textColor;

    public void setBgDoneRes(Drawable bgDoneRes) {
        this.bgDoneRes = bgDoneRes;
    }

    public void setBgTickerRes(Drawable bgTickerRes) {
        this.bgTickerRes = bgTickerRes;
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgDoneRes = getBackground();
        bgTickerRes = getBackground();
        textColor = getCurrentTextColor();
    }

    public void beginCountDown() {
        if (timer != null) {
            timer.cancel();
        }
        final Button self = this;
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                self.setEnabled(false);
                self.setText(("" + millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                self.setEnabled(true);
                self.setText("点击获取");
                self.setBackground(bgDoneRes);
            }
        };
        self.setBackground(bgTickerRes);
        timer.start();
    }

    public void finishCountDown() {
        if (timer != null) {
            timer.cancel();
        }
        this.setEnabled(true);
        this.setText("点击获取");
        this.setBackground(bgDoneRes);
    }
}