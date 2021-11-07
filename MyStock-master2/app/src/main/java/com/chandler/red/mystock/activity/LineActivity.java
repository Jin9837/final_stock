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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chandler.red.mystock.R;
import com.chandler.red.mystock.adapter.BuyStockListAdapter;
import com.chandler.red.mystock.entity.StockBuy;
import com.chandler.red.mystock.fragment.BaseFragment;
import com.chandler.red.mystock.fragment.BuyStockFragment;
import com.chandler.red.mystock.util.LogUtil;

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
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.btn_line1)
    Button btnLine1;


    private boolean isInit = false;
    private List<StockBuy> stockBuyList;
    private String name;
    private String number;
    private RequestQueue queue;
    private int flag=0;
    private String[] stockArray;
    private String[] buyNameArr = {"卖五", "卖四", "卖三", "卖二", "卖一", "买一", "买二", "买三", "买四", "买五"};
    private BuyStockListAdapter buyStockListAdapter;

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

//        Intent intent=new Intent();
//
//        //设置页面转向
//        intent.setClass(LineActivity.this, BuySearchActivity.class);
//        //设置传递参数
//        //intent.putExtra("id", view.getId());
//        startActivityForResult(intent, 100);

        switch (view.getId()) {
            case R.id.et_stock1:
//                BuyStockFragment bsf = new BuyStockFragment();
//                List<StockBuy> stock_buy_list = bsf.stockBuyList;
//                Toast.makeText(this,String.valueOf(stock_buy_list.get(5).getCount()),Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "股票1", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag=1;
                break;
            case R.id.et_stock2:
                Toast.makeText(this, "股票2", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag=2;
                break;
            case R.id.et_stock3:
                Toast.makeText(this, "股票3", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag=3;
                break;
            case R.id.et_stock4:
                Toast.makeText(this, "股票4", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag=4;
                break;
            case R.id.et_stock5:
                Toast.makeText(this, "股票5", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag=5;
                break;
            // startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
            // break;
            case R.id.et_count:
                break;
            case R.id.btn_line1:
                line();

                break;
        }
    }

    private void line() {
        Toast.makeText(LineActivity.this, "已开始排队", Toast.LENGTH_SHORT).show();
//        stockBuyList = new ArrayList<>();
//        for (int i = 0; i < 4; i++)
//        {
//            if ()
//        }


//        BuyStockFragment bsf = new BuyStockFragment();
//        List<StockBuy> stock_buy_list = bsf.stockBuyList;
//
//        System.out.println(stock_buy_list.get(5).getCount());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("requestcode:" + requestCode + " resultcode:" + resultCode);


        if (requestCode == 100 && resultCode == RESULT_OK){ {
            name = data.getStringExtra("name");
            number = data.getStringExtra("number");
            Toast.makeText(this,String.valueOf(flag),Toast.LENGTH_SHORT).show();
            refreshViewAfterItemClicked();
        }
//            queryByNumber();

        }
    }


    public void refreshViewAfterItemClicked() {
        switch(flag){
            case 1:
                EditText editText = (EditText) findViewById(R.id.et_stock1);
                editText.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 2:
                EditText editText1 = (EditText) findViewById(R.id.et_stock2);
                editText1.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 3:
                EditText editText2 = (EditText) findViewById(R.id.et_stock3);
                editText2.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 4:
                EditText editText3 = (EditText) findViewById(R.id.et_stock4);
                editText3.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 5:
                EditText editText4 = (EditText) findViewById(R.id.et_stock5);
                editText4.setText(name, TextView.BufferType.EDITABLE);
                break;
//            case 6:
//                TextView line = (TextView) findViewById(R.id.btn_line1);
//                line.setText();

        }
    }


//    private void queryByNumber(){
//        etStock1.setText(name);
//        if (number != null && !number.equals("")) {
//            isInit = true;
//            querySinaStocks();
//        }
//    }
//
//
//
//    public void querySinaStocks() {
//        // Volley作为网络请求
//        if (number == null || number.equals("")) return;
//        if (queue == null)
//            queue = Volley.newRequestQueue(this);
//        //新浪股票API，url类似：http://hq.sinajs.cn/list=sh600000,sh600536
//        String url = "http://hq.sinajs.cn/list=" + number;
//
//        //实例化一个 StringRequest作为网络请求
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //解析请求的数据
//                        responseToStocks(response);
//                        if (stockArray != null && stockArray.length >= 30)
//                            //刷新UI
//                            refreshView();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        LogUtil.e("请求数据失败");
//                    }
//                });
//
//        queue.add(stringRequest);
//        queue.start();
//    }
//
//    private void refreshView() {
////        stockBuyList.clear();
////        String buy_one = stockArray[11];
////        switch (flag)
////        {
////            case 1:
////        }
////        LogUtil.i("refreshview buy_one:" + buy_one);
//    }
//
//
//    public void responseToStocks(String response) {
//        String[] leftRight = response.split("=");
//        String right = leftRight[1].replaceAll("\"", "");
//        stockArray = right.split(",");
//    }
}
