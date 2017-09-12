package com.example.tomcat.alarmdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    TextView    tvStartDT, tvAlarmDT;
    Button      btnStart;

    Handler     handler=null;
    Runnable    runnable;
    private static boolean runFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initControl();
    }

    public void onClickRunning(View view)
    {
        Log.d(TAG, "onClickRunning()");

        if (runFlag) {
            handler.postDelayed(runnable, 2000);
            runFlag = false;
        }
        //initHandle(true);
    }

    private void initView()
    {
        tvStartDT = findViewById(R.id.tvStartDT);
        tvStartDT.setText("");
        tvAlarmDT = findViewById(R.id.tvAlarmDT);
        tvAlarmDT.setText("");
        btnStart = findViewById(R.id.btnStart);

    }

    private void initControl()
    {
        tvStartDT.setText(getCurrentDT());
        tvAlarmDT.setTextSize(20.0f);
        initHandle(true);
        //handler.postDelayed(runnable, 2000);
    }

    private String getCurrentDT()
    {
        //Calendar calendar = Calendar.getInstance();
        final String pattern = "yyyy/MM/dd  HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String stringDT = sdf.format(new Date(System.currentTimeMillis()));
        Log.i(TAG,"getCurrentDT(), stringDT: " + stringDT);
       return stringDT;
    }

    private void initHandle(final boolean runFlag)
    {
        Log.d(TAG, "initHandle()");
        final NotificationHandler nHandler = NotificationHandler.getInstance(getBaseContext());
        if (handler == null) {
            handler = new Handler();
        }
            runnable = new Runnable() {
                @Override
                public void run() {
                    tvAlarmDT.setText(getCurrentDT());

                    nHandler.createSimpleNotification(getBaseContext());

                    if (runFlag) {
                        //tvAlarmDT.setText(getCurrentDT());
                        handler.postDelayed(runnable, 3000);
                    }
                }
            };

    }


}
