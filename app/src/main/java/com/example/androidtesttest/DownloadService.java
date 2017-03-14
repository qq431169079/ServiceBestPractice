package com.example.androidtesttest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;


    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("downloading....", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;

            //下载成功后将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);

            getNotificationManager().notify(1, getNotification("下载成功", -1));

            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_LONG).show();

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/myApp.apk")), "application/vnd.android.package-archive");
//            getApplicationContext().startActivity(intent);
        }

        @Override
        public void onFailed() {
            downloadTask = null;

            // 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);

            getNotificationManager().notify(1, getNotification("下载失败", -1));

            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_LONG).show();


        }

        @Override
        public void onPaused() {

            downloadTask = null;
            Toast.makeText(DownloadService.this, "暂停", Toast.LENGTH_LONG).show();


        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "取消", Toast.LENGTH_LONG).show();
        }
    };


    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload(String url) {

            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("正在下载中。。。。", 0));
                Toast.makeText(DownloadService.this, "正在下载中。。。。", Toast.LENGTH_LONG).show();
            }
        }


        public void pauseDownload() {

            if (downloadTask != null) {
                downloadTask.pauseDownload();

            }
        }


        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //取消下载时需将文件删除，并将通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "取消了", Toast.LENGTH_LONG).show();
                }
            }


        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setContentTitle(title);
        if (progress > 0) {
            //当progress大于或等于0 时 才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }


    public DownloadService() {
    }


}
