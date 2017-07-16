package com.mapedutech.akshar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.artifex.mupdfdemo.ChoosePDFActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        finish();
        startActivity(new Intent(this, ChoosePDFActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
