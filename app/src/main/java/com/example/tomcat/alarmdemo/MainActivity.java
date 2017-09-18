package com.example.tomcat.alarmdemo;

import android.graphics.Color;
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
    private static final int    ALRM_PERIOD = 1000 * 60 * 2;

    TextView    tvStartDT, tvAlarmDT;
    Button      btnStart, btnBleAlarm;

    Handler     handler=null;
    Runnable    runnable;
    private static boolean refacetFlag = true;
    private static boolean bgFlag = false;
    private long alarmPeriod=0L;
    int colorKeep;

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

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume() ...");
        //tvStartDT.setText(getCurrentDT());
        tvAlarmDT.setTextColor(colorKeep);
        tvAlarmDT.setText(getCurrentDT());

    }

    public void onClickRunning(View view)
    {
        Log.d(TAG, "onClickRunning(), refacetFlag: " + refacetFlag);

        if (refacetFlag) {
            //handler.postDelayed(runnable, 1*1000);
            setAlrmPeriod(1000*60*1);
            handler.post(runnable);
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

    public void onClickbleAlarm(View view)
    {
        if (refacetFlag) {
            Log.d(TAG, "onClickbleAlarm(), refacetFlag: " + refacetFlag);
            //handler.postDelayed(runnable, 1*1000);
            getCurrentDT();
            setAlrmPeriod(1000*60*1);
            handler.postDelayed(runnable, 1000*60*2);
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
    }

    private void initView()
    {
        tvStartDT = findViewById(R.id.tvStartDT);
        tvStartDT.setText("");
        tvAlarmDT = findViewById(R.id.tvAlarmDT);
        tvAlarmDT.setText("");
        btnStart = findViewById(R.id.btnStart);
        btnBleAlarm = findViewById(R.id.button_BLE);

    }

    private void initControl()
    {
        tvStartDT.setText(getCurrentDT());
        tvAlarmDT.setTextSize(20.0f);
        colorKeep = tvAlarmDT.getTextColors().getDefaultColor();
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

        if (handler == null) {
            handler = new Handler();
        }

        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    eventTodo(this);
                }
            };
        }
    }

    private void eventTodo(Runnable runs)
    {
        NotificationHandler nHandler = NotificationHandler.getInstance(getBaseContext());
        tvAlarmDT.setTextColor(Color.RED);
        tvAlarmDT.setText(getCurrentDT());
        nHandler.createSimpleNotification(getBaseContext(), bgFlag);

        Log.i(TAG, "eventTodo(), bgFlag: " + bgFlag);

        if (bgFlag) {

            handler.removeCallbacks(runs);
        } else {
            Log.i(TAG, "run(), Alarm set !! ");
            //handler.postDelayed(runs, ALRM_PERIOD);
            handler.postDelayed(runs, getAlarmPeriod());
        }
    }

    private long getAlarmPeriod()
    {
        return alarmPeriod;
    }

    private void setAlrmPeriod(long mPeriod)
    {
        alarmPeriod = mPeriod;
    }


}
