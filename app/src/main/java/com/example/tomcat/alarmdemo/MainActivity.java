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
    private static final int    ALRM_PERIOD = 1000 * 60;

    TextView    tvStartDT, tvAlarmDT;
    Button      btnStart;

    Handler     handler=null;
    Runnable    runnable;
    private static boolean refacetFlag = true;
    private static boolean bgFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initControl();
    }

    @Override
    protected void onStart() {
        bgFlag = false;
        refacetFlag = true;
        btnStart.setText("Sart");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        bgFlag = true;
        super.onStop();
    }

    public void onClickRunning(View view)
    {
        Log.d(TAG, "onClickRunning(), refacetFlag: " + refacetFlag);

        if (refacetFlag) {
            handler.postDelayed(runnable, 1*1000);
            refacetFlag = false;
            btnStart.setText("Stop");
            bgFlag = false;
        }
        else
        {
            handler.removeCallbacks(runnable);
            refacetFlag = true;
            btnStart.setText("Sart");
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
        bgFlag = false;
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
                    Log.i(TAG, "bgFlag: " + bgFlag);

                    nHandler.createSimpleNotification(getBaseContext());

                    if (bgFlag) {
                        handler.removeCallbacks(runnable);
                    }
                    else {
                        Log.i(TAG, "run(), Alarm set !! ");
                        handler.postDelayed(runnable, ALRM_PERIOD);
                    }
                }
            };

    }


}
