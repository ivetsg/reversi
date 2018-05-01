package com.example.root.reversi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by root on 19/04/18.
 */

public class Ajuda extends AppCompatActivity {

    private TextView myTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajuda);
        WebView view = (WebView) findViewById(R.id.instruccions);
        view.loadData(getString(R.string.instruccionsHtml), "text/html", "utf-8");
    }


    public void goBack(View clickedButton){
        finish();
    }
}
