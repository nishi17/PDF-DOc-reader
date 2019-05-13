package com.demo.myapplication;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class OpenOnlinePdfWebview extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressbar;
    String filename = "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_online_pdf_webview);

        webView = (WebView) findViewById(R.id.webview);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        http://docs.google.com/gview?embedded=true&url=http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);

        webView.setWebViewClient(new MyBrowser());
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            progressbar.setVisibility(View.VISIBLE);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            progressbar.setVisibility(View.GONE);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // ignore ssl error
            if (handler != null) {
                handler.proceed();
            } else {
                super.onReceivedSslError(view, null, error);
            }
        }
    }

}
