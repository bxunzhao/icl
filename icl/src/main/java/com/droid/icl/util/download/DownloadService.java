package com.droid.icl.util.download;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.droid.icl.R;

import java.io.File;

import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.SimpleDListener;

/**
 * Created by Administrator on 2016/3/8.
 */
public class DownloadService extends Service {

    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
    public static final String DOWNLOAD_URL = "DOWNLOAD_URL";
    public static final String DOWNLOAD_SAVE_PATH = "DOWNLOAD_SAVE_PATH";

    private final String TAG = "DownloadService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra(DOWNLOAD_URL);
        String path = intent.getStringExtra(DOWNLOAD_SAVE_PATH);
        final int id = intent.getIntExtra("id", -1);
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher);

        final int[] length = new int[1];
        DLManager.getInstance(this).dlStart(url, path, null, null, new SimpleDListener() {
            @Override
            public void onStart(String fileName, String realUrl, int fileLength) {
                builder.setContentTitle(fileName);
                length[0] = fileLength;
            }

            @Override
            public void onProgress(int progress) {
                builder.setProgress(length[0], progress, false);
                nm.notify(id, builder.build());
            }

            @Override
            public void onFinish(File file) {
                nm.cancel(id);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
