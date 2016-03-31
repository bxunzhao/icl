package com.droid.icl.tools;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/3/14.
 */
public class StringTools {

    public static String cent2Yuan(long cent) {
        return String.format("%d.%02d元", cent / 100, cent % 100);
    }

    public static String cent2Yuan2(long cent) {
        if (cent >= 0) {
            return String.format("%d.%02d", cent / 100, cent % 100);
        } else {
            return String.format("%d.%02d", cent / 100, -cent % 100);
        }
    }


    public static String easyReadValue(long value) {
        if (value >= 1000000) {
            return String.format("%.2f万", value / 1000000.0);
        } else if (value > 100000000) {
            return String.format("%.2f亿", value / 10000000000.0);
        } else {
            return cent2Yuan(value);
        }
    }

    public static String cent2Separa(long cent) {
        String s = cent2Yuan2(cent);
        if(cent > 0) {
            DecimalFormat df = new DecimalFormat("#,###.00元");
            return df.format(Double.parseDouble(s));
        }else {
            return "0.00元";
        }
    }

    public static boolean isValidMobile(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean checkBankCard(String cardId) {
        if (cardId == null || cardId.length() == 0)
            return false;
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static String formateBankno(String source) {

        StringBuffer buffer = new StringBuffer(source.replace(" ", ""));
        try {
            buffer.insert(4, " ");
            buffer.insert(9, " ");
            buffer.insert(14, " ");
            buffer.insert(19, " ");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return buffer.toString();
    }

    public static String formateIdno(String source) {

        StringBuffer buffer = new StringBuffer(source.replace(" ", ""));
        try {
            buffer.insert(6, " ");
            buffer.insert(11, " ");
            buffer.insert(16, " ");
            buffer.insert(21, " ");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return buffer.toString();
    }

    public static String formatePhoneno(String source) {

        StringBuffer buffer = new StringBuffer(source.replace(" ", ""));
        try {
            buffer.insert(3, " ");
            buffer.insert(8, " ");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return buffer.toString();
    }

    public static SpannableString smallTailString(String s, int bigSize, int smallSize) {
        return smallTailString(s, bigSize, smallSize, 1);
    }

    public static SpannableString smallTailString(String s, int bigSize, int smallSize, int tailLength) {
        SpannableString msp = new SpannableString(s);
        msp.setSpan(new TypefaceSpan("sans"), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(bigSize, true), 0, msp.length() - tailLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(smallSize, true), msp.length() - tailLength, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString setStringColor(SpannableString s, int color, int start, int end) {
        SpannableString msp = new SpannableString(s);
        msp.setSpan(new ForegroundColorSpan(color),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString setStringSize(SpannableString s, int size, int start, int end) {
        SpannableString msp = new SpannableString(s);
        msp.setSpan(new AbsoluteSizeSpan(size) ,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }
}
