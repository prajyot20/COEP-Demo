package com.artifex.mupdfdemo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.Manifest.permission.INTERNET;

public class WebviewActivity1 extends Activity {

    WebView wv1;
    String url = "http://www.youthbusiness.org/sharad-tandale-of-india-wins-ybi-young-entrepreneur-of-the-year-award/";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv1 = (WebView) findViewById(R.id.webView);


        if (getIntent().getStringExtra(MainActivity.flag).equals(MainActivity.about)) {
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.setWebViewClient(new MyBrowser());
            wv1.loadUrl(url);

            showTimer();
        } else if (getIntent().getStringExtra(MainActivity.flag).equals("bookwiki")) {
            String url = "https://en.wikipedia.org/wiki/Ravana";
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.setWebViewClient(new MyBrowser());
            wv1.loadUrl(url);
            showTimer();
        } else if (getIntent().getStringExtra(MainActivity.flag).equals("purchase")) {

            String url = "http://nexuspublishing.in/";
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.setWebViewClient(new MyBrowser());
            wv1.loadUrl(url);
            showTimer();
        } else if (getIntent().getStringExtra(MainActivity.uri).equals(MainActivity.uri)) {

            //String url = "http://nexuspublishing.in/";
            Toast.makeText(getApplicationContext(), "uri : " + getIntent().getStringExtra(MainActivity.uri), Toast.LENGTH_SHORT).show();
            url = getIntent().getStringExtra(MainActivity.uri);
            wv1.getSettings().setLoadsImagesAutomatically(true);
            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.setWebViewClient(new MyBrowser());
            wv1.loadUrl(url);
        }


    }

    private void showTimer() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Loading Page...");
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setMessage("00:05");
        alertDialog.show();   //

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("00:" + (millisUntilFinished / 1000) + "....");
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
            }
        }.start();

    }

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 1;

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(INTERNET)) {
            /*Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{INTERNET}, REQUEST_READ_CONTACTS);
                        }
                    });*/
        } else {
            requestPermissions(new String[]{INTERNET}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}


