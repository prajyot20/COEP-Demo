package com.nostra13.universalimageloader.sample.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.artifex.mupdfdemo.R;
import com.artifex.utils.ConstantValues;

public class QuizViewFragment extends BaseFragment {
    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        progressBar.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View rootView = inflater.inflate(R.layout.activity_webview, container, false);
        //setContentView(R.layout.activity_webview);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("My Page");
//        actionBar.setDisplayShowHomeEnabled(false);

        this.webview = (WebView) rootView.findViewById(R.id.webview);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        progressBar = ProgressDialog.show(getActivity(), "Quiz", "Loading...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        webview.loadUrl(ConstantValues.baseDomainUrl + "/AndroidExam");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quiz");
        return rootView;
    }
}