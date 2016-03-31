package com.droid.icl.common;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/15.
 */
public class CheckMap extends HashMap<String, String> {

    @Override
    public String put(String key, String value) {
        if (value == null)
            return super.put(key, "");
        return super.put(key, value);
    }
}
