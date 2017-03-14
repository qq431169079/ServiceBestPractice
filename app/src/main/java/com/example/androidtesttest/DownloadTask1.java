package com.example.androidtesttest;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DownloadTask1 extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            while (true) {
//                int downloadPercent = doDownload();
//                publishProgress(downloadPercent);
//
//                if (downloadPercent >= 100) {
//                    break;
//                }
            }

        } catch (Exception e) {
            return false;
        }
//        return true;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setMessage("当前下载进度:" + values[0] + "%");
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.dismiss();
        if (aBoolean) {
//            Toast.makeText(g, "", Toast.LENGTH_LONG).show();
        }else {

        }

    }
}
