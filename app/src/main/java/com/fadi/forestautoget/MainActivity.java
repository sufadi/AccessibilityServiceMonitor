package com.fadi.forestautoget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fadi.forestautoget.service.AccessibilityServiceMonitor;
import com.fadi.forestautoget.util.AccessibilitUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        btnSettings = (Button) findViewById(R.id.btn_settings);
    }

    private void initVaule() {

    }

    private void initListener() {
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
    }

    private void startService() {
        Intent mIntent = new Intent(this, AccessibilityServiceMonitor.class);
        startService(mIntent);
    }


}
