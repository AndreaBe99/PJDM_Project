package com.example.etilometro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class NetActivity extends AppCompatActivity {

    private static final String TAG = "ANDREA";
    public static final String EXTRA_NET = "EXTRA_NET";

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        webView = findViewById(R.id.webview);

        Intent startIntent = getIntent();
        if(startIntent.hasExtra(EXTRA_NET)) {
            String url = getIntent().getStringExtra(EXTRA_NET);
            Log.d(TAG, "onCreate: " + url);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }
    }

}
