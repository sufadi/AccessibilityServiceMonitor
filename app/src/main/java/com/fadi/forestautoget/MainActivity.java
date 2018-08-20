package com.fadi.forestautoget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.fadi.forestautoget.service.AccessibilityServiceMonitor;
import com.fadi.forestautoget.util.AccessibilitUtil;
import com.fadi.forestautoget.util.Config;
import com.fadi.forestautoget.util.ShareUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener {

    private ShareUtil mShareUtil;

    private Switch sw_keep;
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
        sw_keep = (Switch) findViewById(R.id.sw_keep);
        btnSettings = (Button) findViewById(R.id.btn_settings);
    }

    private void initVaule() {
        mShareUtil = new ShareUtil(this);
    }

    private void initListener() {
        sw_keep.setOnCheckedChangeListener(this);
        btnSettings.setOnClickListener(this);
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

        sw_keep.setChecked(mShareUtil.getBoolean(Config.APP_KEEP, false));
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
        }

        Intent intent = new Intent(this, AccessibilityServiceMonitor.class);
        intent.setAction(AccessibilityServiceMonitor.ACTION_UPDATE_SWITCH);
        MainActivity.this.startService(intent);
    }
}
