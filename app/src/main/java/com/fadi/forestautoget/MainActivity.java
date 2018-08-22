package com.fadi.forestautoget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import com.fadi.forestautoget.service.AccessibilityServiceMonitor;
import com.fadi.forestautoget.util.AccessibilitUtil;
import com.fadi.forestautoget.util.Config;
import com.fadi.forestautoget.util.ShareUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener, TimePicker.OnTimeChangedListener {

    private ShareUtil mShareUtil;

    private TimePicker timepick;

    private Switch sw_keep;
    private Switch sw_alipay_forest;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initVaule();
        initListener();
        startService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initView() {
        timepick = (TimePicker) findViewById(R.id.timepick);
        sw_keep = (Switch) findViewById(R.id.sw_keep);
        btnSettings = (Button) findViewById(R.id.btn_settings);
        sw_alipay_forest = (Switch) findViewById(R.id.sw_alipay_forest);
    }

    private void initVaule() {
        mShareUtil = new ShareUtil(this);

        timepick.setIs24HourView(true);
        timepick.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void initListener() {
        btnSettings.setOnClickListener(this);
        sw_keep.setOnCheckedChangeListener(this);
        sw_alipay_forest.setOnCheckedChangeListener(this);
        timepick.setOnTimeChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_settings:
                AccessibilitUtil.showSettingsUI(this);
                break;
        }
    }

    private void updateUI() {
        if (AccessibilitUtil.isAccessibilitySettingsOn(this, AccessibilityServiceMonitor.class.getCanonicalName())) {
            btnSettings.setEnabled(false);
        } else {
            btnSettings.setEnabled(true);
        }

        sw_keep.setChecked(mShareUtil.getBoolean(Config.APP_KEEP, true));
        sw_alipay_forest.setChecked(mShareUtil.getBoolean(Config.APP_ALIPAY_FOREST, true));

        int hour = mShareUtil.getInt(Config.KEY_HOUR, -1);
        int minute = mShareUtil.getInt(Config.KEY_MINUTE, -1);

        if (hour == -1 && minute == -1) {
            // do nothing
        } else {
            timepick.setHour(hour);
            timepick.setMinute(minute);
        }
    }

    private void startService() {
        Intent mIntent = new Intent(this, AccessibilityServiceMonitor.class);
        startService(mIntent);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_keep:
                mShareUtil.setShare(Config.APP_KEEP, b);
                Log.d(Config.TAG, "Keep is " + b);
                break;
            case R.id.sw_alipay_forest:
                mShareUtil.setShare(Config.APP_ALIPAY_FOREST, b);
                Log.d(Config.TAG, "AlipayForest is " + b);
                break;
        }

        Intent intent = new Intent(this, AccessibilityServiceMonitor.class);
        intent.setAction(AccessibilityServiceMonitor.ACTION_UPDATE_SWITCH);
        MainActivity.this.startService(intent);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        if (mShareUtil != null) {
            mShareUtil.setShare(Config.KEY_HOUR, hourOfDay);
            mShareUtil.setShare(Config.KEY_MINUTE, minute);

            MyApplication.startAlarmTask(MainActivity.this);
        }
    }
}
