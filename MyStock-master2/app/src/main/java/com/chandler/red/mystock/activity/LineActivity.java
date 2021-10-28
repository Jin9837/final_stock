package com.chandler.red.mystock.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chandler.red.mystock.R;
import com.chandler.red.mystock.entity.StockBuy;
import com.chandler.red.mystock.fragment.BaseFragment;
import com.chandler.red.mystock.fragment.BuyStockFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LineActivity extends AppCompatActivity {

//    EditText etStock1;
//    EditText etStock2;
//    EditText etStock3;
//    EditText etStock4;
//    EditText etStock5;
//    Button btnLine;
    @BindView(R.id.et_stock1)
    EditText etStock1;
    @BindView(R.id.et_stock2)
    EditText etStock2;
    @BindView(R.id.et_stock3)
    EditText etStock3;
    @BindView(R.id.et_stock4)
    EditText etStock4;
    @BindView(R.id.et_stock5)
    EditText etStock5;
    @BindView(R.id.btn_line1)
    Button btnLine1;


    private List<StockBuy> stockBuyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);
//        btnLine = (Button) findViewById(R.id.btn_line);
//        Intent intent = new Intent();
//        intent.setClass(LineActivity.this, BuyStockFragment.class);
//        LineActivity.this.startActivity(intent);
    }

//    @OnClick({R.id.et_stock1, R.id.et_stock2, R.id.et_stock3, R.id.et_stock4, R.id.et_stock5, R.id.btn_line1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_stock1:
            case R.id.et_stock2:
            case R.id.et_stock3:
            case R.id.et_stock4:
            case R.id.et_stock5:
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                break;
            case R.id.btn_line1:
                line();

                break;
        }
    }

    private void line() {
//        Toast.makeText(LineActivity.this, "开启排队", Toast.LENGTH_SHORT).show();
//        stockBuyList = new ArrayList<>();
//        for (int i = 0; i < 4; i++)
//        {
//            if ()
//        }
    }

}
