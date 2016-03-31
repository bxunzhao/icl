package com.droid.icl.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 *
 * 对象读写
 * Created by Administrator on 2016/3/11.
 */
public class ObjectTools {

    /**
     * 对象存储
     *
     * @param activity
     * @param oAuth_1
     * @param pref_string
     */
    public static void saveObjToPref(Context activity, Object oAuth_1, String pref_string) {
        SharedPreferences preferences = activity.getSharedPreferences("base64_object", Context.MODE_APPEND);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(oAuth_1);
            // 将字节流编码成base64的字符窜

            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(pref_string, oAuth_Base64);
            editor.commit();
//            Log.e("editor.putString", oAuth_Base64);

//            Log.i("ok", "存储成功");
        } catch (IOException e) {
            // TODO Auto-generated
            e.printStackTrace();
        }
    }

    /**
     * 读取存储的对象
     *
     * @param activity
     * @param pref_tring
     * @return
     */
    public static Object readObjFromPref(Context activity, String pref_tring) {
        Object oAuth_1 = null;
        SharedPreferences preferences = activity.getSharedPreferences("base64_object", Context.MODE_PRIVATE);
        String productBase64 = preferences.getString(pref_tring, "");
//        Log.e("editor.getString", productBase64);
        //读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                oAuth_1 = bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return oAuth_1;
    }
}
