package com.droid.icl.util.downhtml;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlStorageHelper {
    //    private String URL = "http://eduproject.sinaapp.com/fetchurl.php/getcontent/";

    public final static String HTML_PATH = "/LajiaoNews/Htmls/";

    private AQuery aq;
    private SQLiteDatabase mDB;
    private String mDownloadPath;

    private static HtmlStorageHelper instance;

    public synchronized static HtmlStorageHelper getInstance(Context context) {
        if (instance == null)
            instance = new HtmlStorageHelper(context);
        return instance;
    }

    public HtmlStorageHelper(Context context) {
        aq = new AQuery(context);
        mDB = context.openOrCreateDatabase("LajiaoNewsHtml.db", Context.MODE_PRIVATE, null);
        mDB.execSQL("create table if not exists download_html(_id INTEGER PRIMARY KEY AUTOINCREMENT, content_id TEXT NOT NULL, title TEXT NOT NULL, url TEXT NOT NULL)");

        mDownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + HTML_PATH;

        File dir_file = new File(mDownloadPath);
        if (!dir_file.exists())
            dir_file.mkdir();
    }

    public void saveHtml(final String url, final String id, final String title) {
        try {
            if (isHtmlSaved(id))
                return;

            aq.ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String html, AjaxStatus status) {
                    File dir_file = new File(mDownloadPath + id);
                    if (!dir_file.exists())
                        dir_file.mkdir();

                    Pattern pattern = Pattern.compile("(?<=src=\")[^\"]+(?=\")");
                    Matcher matcher = pattern.matcher(html);
                    StringBuffer sb = new StringBuffer();
                    while (matcher.find()) {
                        downloadPic(id, matcher.group(0));
                        matcher.appendReplacement(sb, formatPath(matcher.group(0)));
                    }
                    matcher.appendTail(sb);
                    html = sb.toString();

                    {
                        Document doc_Dis = Jsoup.parse(html);
                        Elements ele_Img = doc_Dis.getElementsByTag("img");
                        if (ele_Img.size() != 0) {
                            for (Element e_Img : ele_Img) {
                                e_Img.attr("style", "width:100%");
                            }
                        }
                        html = doc_Dis.toString();
                    }

                    writeHtml(url, id, title, html);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadPic(String id, String url) {
        File pic_file = new File(mDownloadPath + id + "/" + formatPath(url));
        aq.download(url, pic_file, new AjaxCallback<File>() {
            @Override
            public void callback(String url, final File file, AjaxStatus status) {
            }
        });
    }

    private void writeHtml(String url, String id, String title, String html) {
        File html_file = new File(mDownloadPath + id + "/index.html");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(html_file);
            fos.write(html.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        ContentValues values = new ContentValues();
        values.put("content_id", id);
        values.put("title", title);
        values.put("url", url);
        mDB.insert("download_html", "_id", values);
    }

    public boolean isHtmlSaved(String id) {
        File file = new File(mDownloadPath + id);
        if (file.exists()) {
            file = new File(mDownloadPath + id + "/index.html");
            if (file.exists())
                return true;
        }
        deleteHtml(id);
        return false;
    }

    public String getHtmlPath(String id) {
        return mDownloadPath + id + "/index.html";
    }

    public String getTitle(String id) {
        Cursor c = mDB.rawQuery("select * from download_html where content_id=?", new String[]{id});
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        int index1 = c.getColumnIndex("title");

        return c.getString(index1);
    }

    public ArrayList<Subscribe> getHtmlList() {
        Cursor c = mDB.rawQuery("select * from download_html", null);
        ArrayList<Subscribe> list = new ArrayList<Subscribe>();
        if (c.getCount() != 0) {
            c.moveToFirst();
            int index1 = c.getColumnIndex("content_id");
            int index2 = c.getColumnIndex("title");
            int index3 = c.getColumnIndex("url");

            while (!c.isAfterLast()) {
                String id = c.getString(index1);
                if (isHtmlSaved(id)) {
                    Subscribe sub = new Subscribe(
                            c.getString(index3),
                            id,
                            c.getString(index2)
                    );
                    list.add(sub);
//                    Log.e("sub:", sub.toString());
                }
                c.moveToNext();
            }
        }

        return list;
    }

    public void deleteHtml(String id) {
        mDB.delete("download_html", "content_id=?", new String[]{id});
        File dir_file = new File(mDownloadPath + id);
        deleteFile(dir_file);
    }

    private void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            //
        }
    }

    public static String formatPath(String path) {
        if (path != null && path.length() > 0) {
            path = path.replace("\\", "_");
            path = path.replace("/", "_");
            path = path.replace(":", "_");
            path = path.replace("*", "_");
            path = path.replace("?", "_");
            path = path.replace("\"", "_");
            path = path.replace("<", "_");
            path = path.replace("|", "_");
            path = path.replace(">", "_");
        }
        return path;
    }
}