package com.droid.icl.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/11.
 */
public class TTTools {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s) {
        if (TextUtils.isEmpty(s))
            return;
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    /**
     * 获得当天clock点时间
     *
     * @param clock
     * @return
     */
    public static long getTimeOfHour(int clock) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() + clock * 60 * 60 * 1000);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static long getTimesNow() {
        return new Date().getTime();
    }

    /**
     * @return
     */
    public static long getOneDayTime() {
        return 24 * 60 * 60 * 1000;
    }

    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }


    public static int getStatusHeight(Activity activity) {
        Class<?> c;
        Object obj;
        Field field;
        int x, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 整个屏幕区域
     */
    public static Dimension getAreaOne(Activity activity) {
        Dimension dimen = new Dimension();
        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        dimen.mWidth = outP.x;
        dimen.mHeight = outP.y;
        return dimen;
    }

    /**
     * 应用区域
     *
     * @param activity
     * @return
     */
    public static Dimension getAreaTwo(Activity activity) {
        Dimension dimen = new Dimension();
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        System.out.println("top:" + outRect.top + " ; left: " + outRect.left);
        dimen.mWidth = outRect.width();
        dimen.mHeight = outRect.height();
        return dimen;
    }

    /**
     * view绘制区域
     *
     * @param activity
     * @return
     */
    public static Dimension getAreaThree(Activity activity) {
        Dimension dimen = new Dimension();
        // 用户绘制区域
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        dimen.mWidth = outRect.width();
        dimen.mHeight = outRect.height();
        // end
        return dimen;
    }

    public static class Dimension {
        public int mWidth;
        public int mHeight;

        public Dimension() {
        }
    }

    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;

    /**
     * 在Android中，虽然我们可以通过Runtime.getRuntime().exec("su")的方式来判断一个手机是否Root,
     * 但是该方式会弹出对话框让用户选择是否赋予该应用程序Root权限，有点不友好。
     * 其实我们可以在环境变量$PATH所列出的所有目录中查找是否有su文件来判断一个手机是否Root。
     * 当然即使有su文件，也并不能完全表示手机已经Root，但是实际使用中作为一个初略的判断已经很好了。
     * 另外出于效率的考虑，我们可以在代码中直接把$PATH写死。
     *
     * @return
     */
    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {

            return false;
        }
        File f;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    /**
     * 判断手机是否root，不弹出root请求框<br/>
     */


    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        if (new File(xBinPath).exists() && isExecutable(xBinPath))
            return true;
        return false;

    }


    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (p != null) {
                p.destroy();

            }
        }
        return false;
    }

    /**
     * 此方法废弃，用于onListItemClick 时会多次响应吗，
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
//        private static long lastClickTime;
//        private final static int MIN_CLICK_DELAY_TIME = 300;
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        Log.e("curren", " " + (currentTime - lastClickTime));
//        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//            lastClickTime = currentTime;
//            return false;
//        }
        return false;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
