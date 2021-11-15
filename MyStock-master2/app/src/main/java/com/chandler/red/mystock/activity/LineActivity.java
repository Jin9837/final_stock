package com.chandler.red.mystock.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chandler.red.mystock.R;
import com.chandler.red.mystock.db.StockBuisnessManager;
import com.chandler.red.mystock.entity.AccStock;
import com.chandler.red.mystock.entity.ExeStock;
import com.chandler.red.mystock.entity.StockBean;
import com.chandler.red.mystock.util.Constants;
import com.chandler.red.mystock.util.DateUtil;
import com.chandler.red.mystock.util.EncryptUtil;
import com.chandler.red.mystock.util.LogUtil;
import com.chandler.red.mystock.util.NumberUtil;

import java.util.Timer;
import java.util.TimerTask;

public class LineActivity extends AppCompatActivity {

    private static final String TAG = "LineActivity";
    private EditText etStock1;
    private EditText etStock2;
    private EditText etStock3;
    private EditText etStock4;
    private EditText etStock5;
    private EditText etCount;
    private TextView btnLine1;
    private RequestQueue queue;

    private boolean isInit = false;
    private StockBean stockBeans[];
    private String name;
    private String number;
    private int numberOfBuyOne;//买一的数量
    private int flag = 0;
    public int count = 0;
    Timer timer;//使用timer计时器来实现每隔一秒发送一次请求
    private String[] stockArray;//股票信息 20到29是买一、卖一到买五、卖五到情况
    /**
     * 下面的四个变量用来临时存储从response返回的临时数据
     */
    private String stockName;
    private Double stockPrice;
    private int stockBuyOneNum;//买一的数量
    private String stockNumber;
    /**
     * 下面这四个变量为通过账号获取用户的资产
     */
    private String phone;
    private int accId;
    private AccStock accStock;
    private Double freeMoney;
    /***
     * 这里用来存放排队股票的代码
     */
    String mStockCodes = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);
        initView();
    }


    private void initView() { //初始化所有控件，基于ViewBinding注解实现的控件在调用时有很多很多bug，所以暂时弃用了
        etStock1 = findViewById(R.id.et_stock1);
        etStock2 = findViewById(R.id.et_stock2);
        etStock3 = findViewById(R.id.et_stock3);
        etStock4 = findViewById(R.id.et_stock4);
        etStock5 = findViewById(R.id.et_stock5);
        etCount = findViewById(R.id.et_count);
        btnLine1 = findViewById(R.id.btn_line1);
        stockBeans = new StockBean[5];
        //从SharedPreferences中获取手机号
        SharedPreferences sharedPreferences = getSharedPreferences("MY_STOCK_PREF", Activity.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", null);
        accStock = StockBuisnessManager.getInstance(this).getStockAccountByPhone(EncryptUtil.md5WithSalt(phone));
        accId = accStock.getAccId();
        freeMoney = accStock.getCurValue();
//        createNotification("123");
    }

    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.et_stock1:
                Toast.makeText(this, "股票1", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag = 1;
                break;
            case R.id.et_stock2:
                Toast.makeText(this, "股票2", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag = 2;
                break;
            case R.id.et_stock3:
                Toast.makeText(this, "股票3", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag = 3;
                break;
            case R.id.et_stock4:
                Toast.makeText(this, "股票4", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag = 4;
                break;
            case R.id.et_stock5:
                Toast.makeText(this, "股票5", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(LineActivity.this, BuySearchActivity.class), 100);
                flag = 5;
                break;
            case R.id.et_count:
                break;
            case R.id.btn_line1:
                //加一个判断因为排队数量不能为空值,自选股票也不可为空
                String mNumber = etCount.getText().toString();
                if (isBadNumber(mNumber)) return; //这里对排队数量进行判断
                if (!hasStockCode()) {
                    Toast.makeText(this, "请加入需要排队的股票!", Toast.LENGTH_SHORT).show();
                    return;
                }
                line();//在这里开启排队操作

                break;
        }
    }

    private boolean hasStockCode() {
        for (StockBean bean : stockBeans) {
            if (bean != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isBadNumber(String mNumber) {
        if (mNumber.isEmpty()) {
            Toast.makeText(this, "排队数量不能为空!", Toast.LENGTH_SHORT).show();
            return true;
        }
        numberOfBuyOne = Integer.parseInt(mNumber);
        if (numberOfBuyOne <= 0) {
            Toast.makeText(this, "请输入正确的排队数量!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void line() {
        mStockCodes = "";
        updateStockCodes();//更新股票代码信息，因为排队成功后就需要移除已经购买的股票号码
        String finalMStockCodes = mStockCodes;
        if (finalMStockCodes.length() == 0) {
            //当排队列表中的股票都已经购买完成，就不需要执行后面的操作
            return;
        }
        if (timer == null) {
            timer = new Timer("stock queuing"); //初始化股票排队定时器
            Toast.makeText(LineActivity.this, "已开始排队", Toast.LENGTH_SHORT).show();
        } else {
//            timer.cancel();
            Toast.makeText(LineActivity.this, "正在排队中，更新排队信息", Toast.LENGTH_SHORT).show();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    sendRequestForRealTimeStock(finalMStockCodes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Log.d("XJW", "line: task is opening :" + mStockCodes);
        timer.schedule(task, 0, 10000);//时延为0，持续次数为10000

    }

    private void updateStockCodes() {
        for (StockBean stock : stockBeans) {
            if (stock != null) {
                mStockCodes += (stock.getNumber().substring(2) + ",");
            }
        }
        mStockCodes = mStockCodes.substring(0, mStockCodes.length() - 1);
    }

    private void sendRequestForRealTimeStock(String stockCode) {
        String url = "http://hq.sinajs.cn/list=" + stockCode;
        if (queue == null)
            queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        responseToBuyOne(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(stringRequest);
        queue.start();
        Log.d(TAG, "sendRequestForRealTimeStock: ");
    }

    private void responseToBuyOne(String response) {
        Log.d("XJW", "run: " + response);
        String responeArray[] = response.split("\\r?\\n");//将请求拆解成若干个子response
        for (String res : responeArray) {
            String[] leftRight = res.split("=");
            String right = leftRight[1].replaceAll("\"", "");
            stockArray = right.split(",");
            //将股票的关键信息提取出来，这里采用复用的方式（这里不知道买入成功后，要不要从排队队列移除已经买入的股票）
            stockName = stockArray[0];
            stockPrice = Double.parseDouble(stockArray[3]);
            stockBuyOneNum = Integer.parseInt(stockArray[10]) / 100;
            Log.d("XJW", "responseToBuyOne: " + stockBuyOneNum);
            if (stockBuyOneNum < 2100) {//买一数量小于2000时，买入股票
                Log.d("XJW", "responseToBuyOne价格符合预期: ");
                if (DateUtil.isExchangeTime(System.currentTimeMillis())) {//只有在交易时间才可购买股票
                    //这里要初始化一个execStock,因为交易需要交易号等一系列东西
                    ExeStock exeStock = new ExeStock();
                    exeStock.setName(stockName);
                    exeStock.setAccId(accId);
                    exeStock.setExeId(NumberUtil.getUUID()); //购买股票
                    exeStock.setExeValue(stockPrice); //购买价格
                    exeStock.setExeMount(Integer.parseInt(etCount.getText().toString())); //购买数量
                    for (StockBean bean : stockBeans) {
                        if (bean != null) {
                            if (bean.getName().equals(stockName))
                                stockNumber = bean.getNumber().substring(2);
                        }
                    }
                    exeStock.setNumber(stockNumber);//股票编号
                    exeStock.setExeType(Constants.TYPE_BUY);
                    exeStock.setExeTime(System.currentTimeMillis());
                    StockBuisnessManager.getInstance(this).insertExchange(exeStock);
                    final double[] cStockValue = {accStock.getCurStockValue()}; //当前持有股票数量
                    final double[] cValue = {accStock.getCurValue()}; //当前账户余额
                    cValue[0] = cValue[0] - exeStock.getExeMount() * exeStock.getExeValue();
                    if (cValue[0] < 0) {
                        createNotification("购买失败，余额不足");
//                        removeStockCodes(exeStock.getName());//购买失败也将其移除
                    } else {
                        cStockValue[0] = cStockValue[0] + exeStock.getExeMount() * exeStock.getExeValue();
                        accStock.setCurStockValue(cStockValue[0]);
                        accStock.setCurValue(cValue[0]);
                        StockBuisnessManager.getInstance(this).replaceAccount(accStock);
                        String message = String.format("成功以%.2f的价格购买%s股票%d手!", exeStock.getExeValue(), exeStock.getName(), exeStock.getExeMount());
                        createNotification(message);//购买成功后往前台发送一个通知
                        Log.d("XJW", "responseToBuyOne: " + message);
                        timer.cancel();//购买成功后移除定时器
                        //购买成功后将请求中的股票移除，避免重复购买
//                        removeStockCodes(exeStock.getName());
                    }

                }
            }


        }
    }

//    private void removeStockCodes(String name) {
//        for (StockBean stock : stockBeans) {
//            if (stock != null) {
//                if(stock.getName().equals(name)){//循环找出已经成功购买的股票
//                    stock =null;//将其置空
//                }
//            }
//        }
//    }


    private void createNotification(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //建立一个通知渠道需要三个参数（id, name(给用户看的), 优先级）
            String channelId1 = "stock";
            String channelName1 = "MyStock";
            int importance1 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel1 = new NotificationChannel(channelId1, channelName1, importance1);

            //实际上可以一条代码新建NotificationChannel
            NotificationChannel channel2 = new NotificationChannel("subscribe", "订阅消息", NotificationManager.IMPORTANCE_DEFAULT);

            //通过NotificationManager.createNotificationChannel(channel)方法建立通知渠道
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "stock");   //这里多了一个参数"stock"，代表这条消息属于stock渠道
        Intent intent = new Intent(this, Main2Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentTitle("MyStock")
                .setContentText(message)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());

        manager.notify(1, builder.build());


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("requestcode:" + requestCode + " resultcode:" + resultCode);


        if (requestCode == 100 && resultCode == RESULT_OK) {
            {
                StockBean stockBean = (StockBean) data.getSerializableExtra("stock");
                stockBeans[flag - 1] = stockBean;
                name = stockBean.getName();
                number = stockBean.getNumber();
                Toast.makeText(this, String.valueOf(flag), Toast.LENGTH_SHORT).show();
                refreshViewAfterItemClicked();
            }

        }
    }


    public void refreshViewAfterItemClicked() {
        switch (flag) {
            case 1:
                EditText editText = findViewById(R.id.et_stock1);
                editText.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 2:
                EditText editText1 = findViewById(R.id.et_stock2);
                editText1.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 3:
                EditText editText2 = findViewById(R.id.et_stock3);
                editText2.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 4:
                EditText editText3 = findViewById(R.id.et_stock4);
                editText3.setText(name, TextView.BufferType.EDITABLE);
                break;
            case 5:
                EditText editText4 = findViewById(R.id.et_stock5);
                editText4.setText(name, TextView.BufferType.EDITABLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
