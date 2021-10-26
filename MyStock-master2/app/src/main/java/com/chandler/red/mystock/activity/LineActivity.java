package com.chandler.red.mystock.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chandler.red.mystock.R;
import com.chandler.red.mystock.fragment.BaseFragment;
import com.chandler.red.mystock.fragment.BuyStockFragment;

public class LineActivity extends AppCompatActivity {

//    Button btnLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);
//        btnLine = (Button) findViewById(R.id.btn_line);
//        Intent intent = new Intent();
//        intent.setClass(LineActivity.this, BuyStockFragment.class);
//        LineActivity.this.startActivity(intent);
    }
}
