package com.droid.icl.util.downhtml;

import android.os.Environment;

/**
 * Created by Administrator on 2016/1/15.
 * <p>
 * NewsHtml,离线新闻实体类
 */
public class Subscribe {

    public static String FILE_DOWNLOADED = "FILE_DOWNLOADED";
    private String id;
    private String title;
    private String url;

    public Subscribe(String url, String id, String title) {
        this.url = url;
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
