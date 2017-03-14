package com.example.androidtesttest;

/**
 * Created by win on 2017/3/13.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
