package com.droid.icl.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    private final String TAG = "AutoReadSms";
    private final static String PANDABUS_SMS = "SMS";
    private final static String BROADCAST_KEY = "SECURITY.CODE.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder body = new StringBuilder();// 短信内容
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] _pdus = (Object[]) bundle.get("pdus");
            if (_pdus == null || _pdus.length < 1)
                return;
            SmsMessage[] message = new SmsMessage[_pdus.length];
            for (int i = 0; i < _pdus.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
            }
            for (SmsMessage currentMessage : message) {
                String s = currentMessage.getMessageBody();
                String code = getyzm(s, 6);
                if (code != null)
                    broadcastMessage(context, code);
            }
        }
        this.abortBroadcast();
    }

    private void broadcastMessage(Context context, String msg) {
        Intent i = new Intent(BROADCAST_KEY);
        i.putExtra("security_code", msg);
        context.sendBroadcast(i);
    }

    public static String getyzm(String body, int YZMLENGTH) {
        // 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现
        try {
            Pattern p = Pattern
                    .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");
            Matcher m = p.matcher(body);
            if (m.find()) {
                System.out.println(m.group());
                return m.group(0);
            }
        } catch (Exception e) {

        }
        return null;
    }
}